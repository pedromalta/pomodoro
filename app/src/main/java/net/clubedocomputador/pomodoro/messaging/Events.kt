package net.clubedocomputador.pomodoro.messaging

import org.greenrobot.eventbus.EventBus

data class Events(
        val action: String,
        var message: String? = null,
        var sticky: Boolean = false
) {
    init {
        if (sticky) {
            EventBus.getDefault().postSticky(this)
        } else {
            EventBus.getDefault().post(this)
        }
    }

    companion object {
        const val STARTED_POMODORO = "started_pomodoro"
        const val STOPPED_POMODORO = "stopped_pomodoro"
        const val FINISHED_POMODORO = "finished_pomodoro"

        const val APP_FOREGROUND = "app_foreground"
        const val APP_BACKGROUND = "app_background"
    }
}

