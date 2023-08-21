package com.geekbrains.stopwatch.domain

interface TimestampProvider {
    fun getMilliseconds(): Long
}