package com.example.fitness_habit_tracker.service

import android.app.*
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.fitness_habit_tracker.R
import com.example.fitness_habit_tracker.model.ActivityEntity
import com.example.fitness_habit_tracker.model.ActivityType
import com.example.fitness_habit_tracker.model.SensorData
import com.example.fitness_habit_tracker.database.WorkoutDatabase
import com.example.fitness_habit_tracker.ui.MainActivity
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlinx.coroutines.*
import java.io.File
import java.io.FileWriter
import kotlin.random.Random
import com.example.fitness_habit_tracker.ActivityClassifier
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager



class ActivityRecognitionService : Service() {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "activity_tracking_channel"
        const val NOTIFICATION_ID = 1
        const val ACTION_START_TRACKING = "com.example.fitness_habit_tracker.START_TRACKING"
        const val ACTION_STOP_TRACKING = "com.example.fitness_habit_tracker.STOP_TRACKING"
        const val EXTRA_ACTIVITY_TYPE = "activity_type"
    }

    private var currentActivity: ActivityEntity? = null
    private val sensorDataBuffer = mutableListOf<SensorData>()
    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)
    private lateinit var database: WorkoutDatabase
    private var fakeDataJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        database = WorkoutDatabase.getDatabase(this)
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_TRACKING -> {
                val activityType = intent.getStringExtra(EXTRA_ACTIVITY_TYPE)?.let {
                    ActivityType.valueOf(it)
                } ?: ActivityType.WALKING
                startTracking(activityType)
            }
            ACTION_STOP_TRACKING -> stopTracking()
        }
        return START_STICKY
    }

    private fun startTracking(activityType: ActivityType) {
        Log.d("Debug", "startTracking() called for ${activityType.name}")

        currentActivity = ActivityEntity(
            type = activityType,
            startTime = LocalDateTime.now()
        )

        startGeneratingFakeSensorData()
        Log.d("Debug", "Started generating fake data")

        startForeground(NOTIFICATION_ID, createNotification(activityType))
    }

    private fun stopTracking() {
        fakeDataJob?.cancel()

        currentActivity?.let { activity ->
            serviceScope.launch {
                val endTime = LocalDateTime.now()
                val duration = ChronoUnit.MILLIS.between(activity.startTime, endTime)

                val endedActivity = activity.copy(
                    endTime = endTime,
                    duration = duration
                )
                database.activityDao().insert(endedActivity)

                // 🔁 Copy the buffer BEFORE it's cleared by saveSensorDataToCsv
                val bufferCopy = sensorDataBuffer.toList()

                saveSensorDataToCsv(activity.type.name.lowercase())

                // ✅ Use the copy for prediction
                if (bufferCopy.isNotEmpty()) {
                    val last = bufferCopy.last()
                    val features = floatArrayOf(
                        last.accelerometerX,
                        last.accelerometerY,
                        last.accelerometerZ,
                        last.gyroscopeX,
                        last.gyroscopeY,
                        last.gyroscopeZ,
                        last.heartRate ?: 0f
                    )

                    val classifier = ActivityClassifier(this@ActivityRecognitionService)
                    val prediction = classifier.classify(features)

                    Log.d("Prediction", "Predicted activity: $prediction")

                    val broadcastIntent = Intent("com.example.fitness_habit_tracker.PREDICTION_RESULT")
                    broadcastIntent.putExtra("prediction", prediction)
                    LocalBroadcastManager.getInstance(this@ActivityRecognitionService).sendBroadcast(broadcastIntent)



                } else {
                    Log.d("Prediction", "Sensor buffer was empty!")
                }


                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }

        currentActivity = null
    }



    private fun startGeneratingFakeSensorData() {
        fakeDataJob = serviceScope.launch {
            while (isActive) {
                val currentTime = System.currentTimeMillis()

                val (accelMin, accelMax) = when (currentActivity?.type) {
                    ActivityType.WALKING -> 0.5f to 1.5f
                    ActivityType.RUNNING -> 1.5f to 3.0f
                    ActivityType.CYCLING -> 0.7f to 2.0f
                    ActivityType.STATIONARY -> 0.0f to 0.2f
                    ActivityType.UNKNOWN -> 0.1f to 0.5f
                    else -> 0.1f to 0.5f
                }


                val (gyroMin, gyroMax) = accelMin to accelMax

                val sensorData = SensorData(
                    timestamp = currentTime,
                    accelerometerX = Random.nextFloat() * (accelMax - accelMin) + accelMin,
                    accelerometerY = Random.nextFloat() * (accelMax - accelMin) + accelMin,
                    accelerometerZ = Random.nextFloat() * (accelMax - accelMin) + accelMin,
                    gyroscopeX = Random.nextFloat() * (gyroMax - gyroMin) + gyroMin,
                    gyroscopeY = Random.nextFloat() * (gyroMax - gyroMin) + gyroMin,
                    gyroscopeZ = Random.nextFloat() * (gyroMax - gyroMin) + gyroMin,
                    heartRate = (60..130).random().toFloat()
                )

                sensorDataBuffer.add(sensorData)
                Log.d("Debug", "Generating fake data... Total now: ${sensorDataBuffer.size}")
                delay(100)
            }
        }
    }

    private fun saveSensorDataToCsv(activityLabel: String) {
        try {
            val dir = File(getExternalFilesDir(null), "training_data")
            if (!dir.exists()) dir.mkdirs()

            val file = File(dir, "$activityLabel.csv")
            val writer = FileWriter(file, true)

            for (data in sensorDataBuffer) {
                writer.appendLine("${data.timestamp},${data.accelerometerX},${data.accelerometerY},${data.accelerometerZ}," +
                        "${data.gyroscopeX},${data.gyroscopeY},${data.gyroscopeZ},${data.heartRate ?: 0f},$activityLabel")
            }

            writer.flush()
            writer.close()
            sensorDataBuffer.clear()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Activity Tracking",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Tracks your fitness activities"
        }
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification(activityType: ActivityType): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Tracking Activity")
            .setContentText("Currently tracking: ${activityType.name.lowercase()}")
            .setSmallIcon(R.drawable.ic_directions_run)
            .setContentIntent(pendingIntent)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }
}
