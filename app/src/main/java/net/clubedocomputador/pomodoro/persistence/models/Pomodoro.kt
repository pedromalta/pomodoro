package net.clubedocomputador.pomodoro.persistence.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import net.clubedocomputador.pomodoro.config.Config
import net.clubedocomputador.pomodoro.util.Helpers
import org.joda.time.DateTime
import java.util.*

open class Pomodoro : RealmObject() {

    @PrimaryKey
    var id: String = ""
    var start: Date = Date()
    var finish: Date? = null
    var status: Int = Status.Running.value

    val elapsedTime: String
        get() {
            this.finish?.apply {
                return Helpers.Dates.getDurationString(start, this)
            }
            return Helpers.Dates.getDurationString(finishDate.toDate(), Date())
        }

    val finishDate: DateTime
        get() {
            return DateTime(start).plusMinutes(Config.POMODORO_MINUTES)
        }

    enum class Status(val value: Int) {
        Running(0),
        Finished(1),
        Stopped(2);

        companion object {
            private val map = values().associateBy(Status::value)
            fun fromInt(type: Int) = map[type]
        }
    }
}