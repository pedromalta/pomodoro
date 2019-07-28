package net.clubedocomputador.pomodoro.models.view

import java.util.*

data class HistoryItem(val timer: String, val finish: Date, val status: String, val date: String, val separator: Separator) {

    enum class Separator{
       NO_SEPARATOR, TODAY, YESTERDAY, THIS_WEEK, THIS_MONTH, THIS_YEAR, OLDER
    }
}
