package com.geekbrains.stopwatch.domain

class StopwatchStateCalculator(
    private val timestampProvider: TimestampProvider,
    private val elapsedTimeCalculator: ElapsedTimeCalculator
) {

    fun calculateRunningState(oldState: StopwatchState): StopwatchState.Running =
        when (oldState) {
            is StopwatchState.Running -> oldState
            is StopwatchState.Paused -> {
                StopwatchState.Running(
                    startTimeMs = timestampProvider.getMilliseconds(),
                    elapsedTimeMs = oldState.elapsedTimeMs
                )
            }
        }

    fun calculatePausedState(oldState: StopwatchState): StopwatchState.Paused =
        when (oldState) {
            is StopwatchState.Running -> {
                StopwatchState.Paused(
                    elapsedTimeMs = elapsedTimeCalculator.calculate(oldState)
                )
            }

            is StopwatchState.Paused -> oldState
        }
}