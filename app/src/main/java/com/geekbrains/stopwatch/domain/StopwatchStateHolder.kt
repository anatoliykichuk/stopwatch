package com.geekbrains.stopwatch.domain

class StopwatchStateHolder(
    private val stopwatchStateCalculator: StopwatchStateCalculator,
    private val elapsedTimeCalculator: ElapsedTimeCalculator,
    private val timestampMillisecondsFormatter: TimestampMillisecondsFormatter
) {

    var currentState: StopwatchState = StopwatchState.Paused(0)
        private set

    fun start() {
        currentState = stopwatchStateCalculator.calculateRunningState(currentState)
    }

    fun pause() {
        currentState = stopwatchStateCalculator.calculatePausedState(currentState)
    }

    fun stop() {
        currentState = StopwatchState.Paused(0)
    }

    fun getStringTimeRepresentation(): String {
        val elapsedTimeMs =
            when (val currentState = currentState) {
                is StopwatchState.Paused -> currentState.elapsedTimeMs
                is StopwatchState.Running -> elapsedTimeCalculator.calculate(currentState)
            }
        return timestampMillisecondsFormatter.format(elapsedTimeMs)
    }
}