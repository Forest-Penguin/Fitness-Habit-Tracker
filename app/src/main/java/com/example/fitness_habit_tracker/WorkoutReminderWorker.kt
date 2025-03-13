package com.example.fitness_habit_tracker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import android.app.NotificationManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class WorkoutReminderWorker(context: Context, workerParams: WorkerParameters)
    : Worker(context, workerParams) {

    override fun doWork(): Result {
        sendNotification("Stay on track! Don't skip your workout today!")
        return Result.success()
    }

    private fun sendNotification(message: String) {
        val notificationManager = NotificationManagerCompat.from(applicationContext)

        // 🔹 Check if permission is granted (for Android 13+)
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val notification = NotificationCompat.Builder(applicationContext, "workout_channel")
                .setSmallIcon(R.drawable.ic_fitness)
                .setContentTitle("Workout Reminder")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

            notificationManager.notify(1, notification)
        }
    }
}
