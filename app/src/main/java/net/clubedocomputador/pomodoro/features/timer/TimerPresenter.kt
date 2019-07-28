package net.clubedocomputador.pomodoro.features.timer

import com.vicpin.krealmextensions.save
import net.clubedocomputador.pomodoro.PomodoroApp
import net.clubedocomputador.pomodoro.config.Config
import net.clubedocomputador.pomodoro.features.base.BasePresenter
import net.clubedocomputador.pomodoro.messaging.Events
import net.clubedocomputador.pomodoro.persistence.models.Pomodoro
import net.clubedocomputador.pomodoro.util.Analytics
import java.util.*

class TimerPresenter : BasePresenter<TimerMvpView>() {

    fun startPomodoro(){
        if (PomodoroApp.persistence.pomodoro == null) {
            val pomodoro = Pomodoro()
            pomodoro.id = UUID.randomUUID().toString()
            PomodoroApp.persistence.pomodoro = pomodoro
        } else {
            Analytics.logError("Iniciar Pomodoro", Exception("Pomodoro ja existe"))
        }
        mvpView?.startedPomodoro()
        Events(Events.STARTED_POMODORO)
    }

    fun stopPomodoro(){
        val pomodoro = PomodoroApp.persistence.pomodoro
        if (pomodoro == null) {
            Analytics.logError("Parar Pomodoro", Exception("Pomodoro nao existe"))
        } else {
            pomodoro.finish = Date()
            pomodoro.status = Pomodoro.Status.Stopped.value
            pomodoro.save()
            PomodoroApp.persistence.pomodoro = null
        }
        mvpView?.stoppedPomodoro()
        Events(Events.STOPPED_POMODORO)
    }

    fun getTimer(): String {
        return PomodoroApp.persistence.pomodoro?.elapsedTime ?: Config.POMODORO_MINUTES_STRING
    }

    fun isPomodoroRunning(): Boolean{
        return PomodoroApp.persistence.pomodoro != null
    }

}