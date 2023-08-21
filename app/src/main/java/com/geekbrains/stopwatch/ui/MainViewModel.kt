package com.geekbrains.stopwatch.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.stopwatch.domain.ElapsedTimeCalculator
import com.geekbrains.stopwatch.domain.StopwatchListOrchestrator
import com.geekbrains.stopwatch.domain.StopwatchStateCalculator
import com.geekbrains.stopwatch.domain.StopwatchStateHolder
import com.geekbrains.stopwatch.domain.TimestampMillisecondsFormatter
import com.geekbrains.stopwatch.domain.TimestampProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val liveData: MutableLiveData<String> = MutableLiveData()

    private val timestampProvider = object : TimestampProvider {
        override fun getMilliseconds(): Long {
            return System.currentTimeMillis()
        }
    }

    private val stopwatchListOrchestrator = StopwatchListOrchestrator(
        StopwatchStateHolder(
            StopwatchStateCalculator(
                timestampProvider,
                ElapsedTimeCalculator(timestampProvider)
            ),
            ElapsedTimeCalculator(timestampProvider),
            TimestampMillisecondsFormatter()
        ),
        CoroutineScope(
            Dispatchers.Main + SupervisorJob()
        )
    )

    fun getLiveData(): LiveData<String> = liveData

    fun getData() {
        CoroutineScope(
            Dispatchers.Main + SupervisorJob()
        ).launch {
            stopwatchListOrchestrator.ticker.collect {
                liveData.value = it
           }
        }
    }

    fun start() {
        stopwatchListOrchestrator.start()
    }

    fun pause() {
        stopwatchListOrchestrator.pause()
    }

    fun stop() {
        stopwatchListOrchestrator.stop()
    }
}