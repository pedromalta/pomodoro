package net.clubedocomputador.pomodoro.models.local

import java.util.*

data class PomodoroHistory(
    val start: Date,
    val finish: Date,
    val status: Status,
) {
    enum class Status(val value: Int) {
        Finished(1),
        Stopped(2),
        ;

        companion object {
            private val map = values().associateBy(Status::value)
            fun fromInt(type: Int) = map[type]
        }
    }
}
