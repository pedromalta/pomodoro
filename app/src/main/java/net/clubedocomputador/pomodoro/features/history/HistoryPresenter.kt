package net.clubedocomputador.pomodoro.features.history

import com.vicpin.krealmextensions.query
import io.realm.Sort
import net.clubedocomputador.pomodoro.features.base.BasePresenter
import net.clubedocomputador.pomodoro.models.local.PomodoroHistory
import net.clubedocomputador.pomodoro.persistence.models.Pomodoro
import java.util.*

class HistoryPresenter : BasePresenter<HistoryMvpView>() {

    fun getList(fromDate: Date? = null): List<PomodoroHistory> {
        return Pomodoro().query {
            isNotNull("finish")
            notEqualTo("status", Pomodoro.Status.Running.value)
            if (fromDate != null) {
                lessThan("finish", fromDate)
            }
            sort("finish", Sort.DESCENDING)
            limit(30)
            //get 30 most recent pomodoros after the fromDate parameter or of all if null
        }.map {
            //transform to immutable model for easier use further
            PomodoroHistory(it.start, it.finish!!, PomodoroHistory.Status.fromInt(it.status)!!)
        }
    }


}