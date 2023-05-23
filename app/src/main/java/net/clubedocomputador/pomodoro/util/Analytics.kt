package net.clubedocomputador.pomodoro.util

import android.content.Context

object Analytics {

    const val EVENT_CONTENT_VIEW = "event_content_view"
    const val EVENT_TIMER_START = "event_timer_start"
    const val EVENT_TIMER_STOP = "event_timer_stop"
    const val EVENT_TIMER_FINISHED = "event_timer_finished"

    object Contents {
        const val FRAGMENT_TIMER = "Timer"
        const val FRAGMENT_HISTORY = "History"
    }

    fun configCrashReport(context: Context) {
    }

    fun logError(action: String, error: Throwable) {
    }

    fun logEvent(event: String, data: Any? = null) {
        try {
            when (event) {
                EVENT_CONTENT_VIEW -> eventContentView(data as String)
                EVENT_TIMER_START -> eventTimerStart()
                EVENT_TIMER_STOP -> eventTimerStop(data as String)
                EVENT_TIMER_FINISHED -> eventTimerFinished()
            }
        } catch (e: Exception) {
            logError("Erro ao logar eventos", e)
        }
    }

    private fun eventTimerStart() {
    }

    private fun eventTimerStop(elapsed: String) {
    }

    private fun eventTimerFinished() {
    }

    private fun eventContentView(content: String) {
    }
}
