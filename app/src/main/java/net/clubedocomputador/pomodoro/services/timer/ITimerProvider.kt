package net.clubedocomputador.pomodoro.services.timer

import android.content.Context

interface ITimerProvider {
    fun start(context: Context)
    fun stop(context: Context)
    val isRunning: Boolean
}
