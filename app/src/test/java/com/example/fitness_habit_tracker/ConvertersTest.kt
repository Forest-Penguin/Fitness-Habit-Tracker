package com.example.fitness_habit_tracker

import com.example.fitness_habit_tracker.model.ActivityType
import com.example.fitness_habit_tracker.util.Converters
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ConvertersTest {

    private val converters = Converters()
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @Test
    fun fromTimestamp_validTimestamp_returnsLocalDateTime() {
        val timestampString = "2024-03-15T10:30:00"
        val expectedDateTime = LocalDateTime.parse(timestampString, dateFormatter)

        val actualDateTime = converters.fromTimestamp(timestampString)

        assertEquals(expectedDateTime, actualDateTime)
    }

    @Test
    fun fromTimestamp_nullTimestamp_returnsNull() {
        val actualDateTime = converters.fromTimestamp(null)

        assertNull(actualDateTime)
    }

    @Test
    fun fromTimestamp_invalidTimestamp_returnsNull(){
        val invalidString = "not-a-timestamp"

        val actualDateTime = converters.fromTimestamp(invalidString)

        assertNull(actualDateTime)
    }

    @Test
    fun dateToTimestamp_validLocalDateTime_returnsTimestamp() {
        val localDateTime = LocalDateTime.of(2024, 3, 15, 10, 30, 0)
        val expectedTimestampString = localDateTime.format(dateFormatter)

        val actualTimestampString = converters.dateToTimestamp(localDateTime)

        assertEquals(expectedTimestampString, actualTimestampString)
    }

    @Test
    fun dateToTimestamp_nullLocalDateTime_returnsNull() {
        val actualTimestampString = converters.dateToTimestamp(null)

        assertNull(actualTimestampString)
    }

    @Test
    fun fromActivityType_returnsStringName(){
        val activityType = ActivityType.RUNNING
        val expectedName = "RUNNING"

        val actualName = converters.fromActivityType(activityType)

        assertEquals(expectedName, actualName)
    }

    @Test
    fun toActivityType_returnsActivityType(){
        val activityString = "RUNNING"
        val expectedActivity = ActivityType.RUNNING

        val actualActivity = converters.toActivityType(activityString)

        assertEquals(expectedActivity, actualActivity)
    }

    @Test(expected = IllegalArgumentException::class)
    fun toActivityType_invalidString_throwsException(){
        val activityString = "invalid activity"

        converters.toActivityType(activityString)
    }
}