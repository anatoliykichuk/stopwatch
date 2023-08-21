package com.geekbrains.stopwatch.domain

class ElapsedTimeCalculator(
    private val timestampProvider: TimestampProvider
) {

    fun calculate(state: StopwatchState.Running): Long {
        val currentTimestampMs = timestampProvider.getMilliseconds()
        val timePassedSinceStartMs =
            if (currentTimestampMs > state.startTimeMs) {
                currentTimestampMs - state.startTimeMs
            } else {
                0
            }
        return timePassedSinceStartMs + state.elapsedTimeMs
    }
}