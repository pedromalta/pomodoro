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
    const val EVENT_TIMER = "event_timer"


    object Contents {
        const val FRAGMENT_TIMER = "Timer"
        const val FRAGMENT_HISTORY = "Histórico"
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
                EVENT_TIMER -> eventTimer(data as String)

            }

        } catch (e: Exception) {
            logError("Erro ao logar eventos", e)
        }
    }

    private fun eventContentView(action: String) {
        logger().logCustom(CustomEvent(EVENT_TIMER)
                .putCustomAttribute("action", action)
                .putCustomAttribute("phone", "${Build.MANUFACTURER} ${Build.MODEL}")
        )

    }

    private fun eventTimer(content: String) {
        logger().logContentView(ContentViewEvent()
                .putContentName(content)
                .putCustomAttribute("phone", "${Build.MANUFACTURER} ${Build.MODEL}"))

    }

    private fun logger(): Answers {
        return Answers.getInstance()
    }
}