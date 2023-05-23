package net.clubedocomputador.pomodoro.persistence

import com.vicpin.krealmextensions.queryLast
import com.vicpin.krealmextensions.save
import net.clubedocomputador.pomodoro.persistence.models.Pomodoro

class PersistenceProvider {

    /**
     * We keep the running Pomodoro instance here so we don't overload the persistence system with find() calls
     */
    var pomodoro: Pomodoro? = null
        set(value) {
            value?.save()
            field = value
        }

    init {
        pomodoro = Pomodoro().queryLast { equalTo("status", Pomodoro.Status.Running.value) }
    }
}
