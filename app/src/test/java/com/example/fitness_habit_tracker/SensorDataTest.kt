package com.example.fitness_habit_tracker

import com.example.fitness_habit_tracker.model.SensorData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SensorDataTest {
    @Test
    fun createSensorData_holdsCorrectValues() {
        val timestamp = 1680000000000L
        val accX = 1.2f
        val accY = -0.5f
        val accZ = 9.8f
        val gyroX = 0.1f
        val gyroY = -0.2f
        val gyroZ = 0.3f
        val heartRate = 72.0f

        val data = SensorData(
            timestamp = timestamp,
            accelerometerX = accX,
            accelerometerY = accY,
            accelerometerZ = accZ,
            gyroscopeX = gyroX,
            gyroscopeY = gyroY,
            gyroscopeZ = gyroZ,
            heartRate = heartRate
        )

        assertEquals(timestamp, data.timestamp)
        assertEquals(accX, data.accelerometerX, 0.001f)
        assertEquals(accY, data.accelerometerY, 0.001f)
        assertEquals(accZ, data.accelerometerZ, 0.001f)
        assertEquals(gyroX, data.gyroscopeX, 0.001f)
        assertEquals(gyroY, data.gyroscopeY, 0.001f)
        assertEquals(gyroZ, data.gyroscopeZ, 0.001f)
        assertEquals(heartRate, data.heartRate)
    }

    @Test
    fun createSensorData_withNullHeartRate() {
        val data = SensorData(
            timestamp = 1680000000000L,
            accelerometerX = 0.0f,
            accelerometerY = 0.0f,
            accelerometerZ = 0.0f,
            gyroscopeX = 0.0f,
            gyroscopeY = 0.0f,
            gyroscopeZ = 0.0f,
            heartRate = null
        )

        assertNull(data.heartRate)
    }
}