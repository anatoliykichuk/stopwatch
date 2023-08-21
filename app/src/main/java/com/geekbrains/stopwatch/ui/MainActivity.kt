package com.geekbrains.stopwatch.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.geekbrains.stopwatch.databinding.ActivityMainBinding
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

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding!!

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(
            Dispatchers.Main + SupervisorJob()
        ).launch {
            stopwatchListOrchestrator.ticker.collect {
                binding.time.text = it
            }
        }

        binding.startTime.setOnClickListener {
            stopwatchListOrchestrator.start()
        }

        binding.pauseTime.setOnClickListener {
            stopwatchListOrchestrator.pause()
        }

        binding.stopTime.setOnClickListener {
            stopwatchListOrchestrator.stop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}