package net.clubedocomputador.pomodoro.models.view

import net.clubedocomputador.pomodoro.models.local.PomodoroHistory
import java.util.*

data class HistoryItem(
    val timer: String,
    val finish: Date,
    val status: PomodoroHistory.Status,
    val time: HistoryItemTime,
    val separator: Separator,
) {

    enum class Separator {
        NO_SEPARATOR, TODAY, YESTERDAY, DATE
    }
}
