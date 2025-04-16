package com.example.fitness_habit_tracker

import android.content.Context
import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log

class MotionSensorService : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var currentActivityType: String? = null
    private lateinit var db: WorkoutDatabase

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        db = WorkoutDatabase.getInstance(applicationContext)

        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        currentActivityType = intent?.getStringExtra("activityType") ?: "UNKNOWN"
        return START_STICKY
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val x = it.values[0]
            val y = it.values[1]
            val z = it.values[2]
            val timestamp = System.currentTimeMillis()

            // Store in database
            Thread {
                db.motionDataDao().insertData(
                    MotionData(
                        timestamp = timestamp,
                        x = x,
                        y = y,
                        z = z,
                        activityType = currentActivityType ?: "UNKNOWN"
                    )
                )

            }.start()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }
}
