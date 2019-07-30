package net.clubedocomputador.pomodoro.util

import android.content.Context
import android.os.Build
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.ContentViewEvent
import com.crashlytics.android.answers.CustomEvent
import io.fabric.sdk.android.Fabric


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
        Fabric.with(context, Crashlytics())
    }

    fun logError(action: String, error: Throwable) {
        //Online logging
        Crashlytics.logException(Exception(action, error))
        //Local loggind
        //ComputedErrors.compute(ComputeError(error))
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
        logger().logCustom(CustomEvent("Pomodoro Started")
                .putCustomAttribute("phone", "${Build.MANUFACTURER} ${Build.MODEL}")
        )
    }

    private fun eventTimerStop(elapsed: String) {
        logger().logCustom(CustomEvent("Pomodoro Interrupted")
                .putCustomAttribute("elapsed", "${elapsed.substring(0,2)} minutes")
                .putCustomAttribute("phone", "${Build.MANUFACTURER} ${Build.MODEL}")
        )
    }

    private fun eventTimerFinished() {
        logger().logCustom(CustomEvent("Pomodoro Finished")
                .putCustomAttribute("phone", "${Build.MANUFACTURER} ${Build.MODEL}")
        )
    }

    private fun eventContentView(content: String) {
        logger().logContentView(ContentViewEvent()
                .putContentName(content)
                .putCustomAttribute("phone", "${Build.MANUFACTURER} ${Build.MODEL}"))

    }

    private fun logger(): Answers {
        return Answers.getInstance()
    }
}