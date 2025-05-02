package com.example.fitness_habit_tracker.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

fun <T> LiveData<T>.getOrAwaitValue(timeout: Long = 2): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(value: T) {
            data = value
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    if (!latch.await(timeout, TimeUnit.SECONDS)) {
        throw TimeoutException("LiveData value was never set.")
    }

    return data ?: throw TimeoutException("LiveData value was null.")
}

class TimeoutException(message: String) : Exception(message)
