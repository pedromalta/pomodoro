package net.clubedocomputador.pomodoro.util

import android.content.Context
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.ContentViewEvent
import io.fabric.sdk.android.Fabric


object Analytics {

    const val EVENT_CONTENT_VIEW = "event_content_view"

    object Contents {
        const val FRAGMENT_ALTERAR_SENHA = "Alterar senha"
        const val FRAGMENT_AVALIACOES = "Minhas avaliações"
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

            }

        } catch (e: Exception) {
            logError("Erro ao logar eventos", e)
        }
    }

    private fun eventContentView(content: String) {
        logger().logContentView(ContentViewEvent()
                .putContentName(content))

    }

    private fun logger(): Answers {
        return Answers.getInstance()
    }
}