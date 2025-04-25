package com.example.fitness_habit_tracker.service

import android.app.*
import android.content.Intent
import android.os.IBinder
import android.hardware.SensorManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import androidx.core.app.NotificationCompat
import com.example.fitness_habit_tracker.R
import com.example.fitness_habit_tracker.model.ActivityEntity
import com.example.fitness_habit_tracker.model.ActivityType
import com.example.fitness_habit_tracker.model.SensorData
import com.example.fitness_habit_tracker.database.WorkoutDatabase
import com.example.fitness_habit_tracker.ui.MainActivity
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import kotlinx.coroutines.*

class ActivityRecognitionService : Service(), SensorEventListener {
    companion object {
        const val NOTIFICATION_CHANNEL_ID = "activity_tracking_channel"
        const val NOTIFICATION_ID = 1
        const val ACTION_START_TRACKING = "com.example.fitness_habit_tracker.START_TRACKING"
        const val ACTION_STOP_TRACKING = "com.example.fitness_habit_tracker.STOP_TRACKING"
        const val EXTRA_ACTIVITY_TYPE = "activity_type"
    }

    private lateinit var sensorManager: SensorManager
    private var currentActivity: ActivityEntity? = null
    private val sensorDataBuffer = mutableListOf<SensorData>()
    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)
    private lateinit var database: WorkoutDatabase

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
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
        currentActivity = ActivityEntity(
            type = activityType,
            startTime = LocalDateTime.now()
        )

        // Register sensors
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)?.also { gyroscope ->
            sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL)
        }

        startForeground(NOTIFICATION_ID, createNotification(activityType))
    }

    private fun stopTracking() {
        currentActivity?.let { activity ->
            serviceScope.launch {
                val endTime = LocalDateTime.now()
                val duration = ChronoUnit.MILLIS.between(activity.startTime, endTime)
                
                val endedActivity = activity.copy(
                    endTime = endTime,
                    duration = duration
                )
                database.activityDao().insert(endedActivity)
            }
        }
        currentActivity = null
        sensorManager.unregisterListener(this)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
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

    override fun onSensorChanged(event: SensorEvent) {
        val sensorData = when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> SensorData(
                timestamp = System.currentTimeMillis(),
                accelerometerX = event.values[0],
                accelerometerY = event.values[1],
                accelerometerZ = event.values[2],
                gyroscopeX = 0f,
                gyroscopeY = 0f,
                gyroscopeZ = 0f
            )
            Sensor.TYPE_GYROSCOPE -> {
                val lastData = sensorDataBuffer.lastOrNull() ?: return
                lastData.copy(
                    gyroscopeX = event.values[0],
                    gyroscopeY = event.values[1],
                    gyroscopeZ = event.values[2]
                )
            }
            else -> return
        }
        
        sensorDataBuffer.add(sensorData)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not needed for this implementation
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
        sensorManager.unregisterListener(this)
    }
} 