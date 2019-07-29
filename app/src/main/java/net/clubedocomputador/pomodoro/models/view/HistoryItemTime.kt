package net.clubedocomputador.pomodoro.models.view

data class HistoryItemTime(
        val timeCount: Int,
        val timeType: TimeType) {

    enum class TimeType{
        MOMENTS, MINUTE, HOUR, TIME
    }
}