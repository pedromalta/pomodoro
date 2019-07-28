package net.clubedocomputador.pomodoro.persistence.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

class Pomodoro: RealmObject() {

    @PrimaryKey
    var id: String = ""
    val start: Date = Date()
    var finish: Date? = null
    var status: Int = Status.Running.value

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