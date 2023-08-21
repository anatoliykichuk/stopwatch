package com.geekbrains.stopwatch.domain

sealed class StopwatchState {

    data class Paused(val elapsedTimeMs: Long) : StopwatchState()
    data class Running(val startTimeMs: Long, val elapsedTimeMs: Long) : StopwatchState()
}
