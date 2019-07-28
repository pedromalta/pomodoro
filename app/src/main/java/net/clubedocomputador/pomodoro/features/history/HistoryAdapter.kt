package net.clubedocomputador.pomodoro.features.history

import net.clubedocomputador.pomodoro.extensions.toDateTime
import net.clubedocomputador.pomodoro.models.local.PomodoroHistory
import net.clubedocomputador.pomodoro.models.view.HistoryItem
import net.clubedocomputador.pomodoro.util.Helpers
import org.joda.time.DateTime


private fun transformPomodorosToHistoryItens(pomodoros: List<PomodoroHistory>): List<HistoryItem> {
    val now = DateTime.now()
    val history = ArrayList<HistoryItem>(pomodoros.size)

    var separatorToday = true
    var separatorYesterday = true
    var separatorThisWeek = true
    var separatorThisMonth = true
    var separatorThisYear = true

    for (pomodoro in pomodoros) {
        if (Helpers.Dates.isToday(now, pomodoro.finish.toDateTime()) && separatorToday) {
            separatorToday = false
            history.add(tranformPomodoroToHistoryItem(pomodoro, HistoryItem.Separator.TODAY))
        }
    }

    return arrayListOf()
}

private fun tranformPomodoroToHistoryItem(pomodoro: PomodoroHistory, separator: HistoryItem.Separator): HistoryItem {
    val elapsedTimer = Helpers.Dates.getDurationString(pomodoro.start, pomodoro.finish)

    return HistoryItem(elapsedTimer, pomodoro.finish, "", "", separator)

}