package net.clubedocomputador.pomodoro.features.timer

import net.clubedocomputador.pomodoro.features.base.MvpView


interface TimerMvpView : MvpView {
    fun startedPomodoro()
    fun stoppedPomodoro()

}