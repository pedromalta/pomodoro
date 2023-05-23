package net.clubedocomputador.pomodoro.extensions

import org.joda.time.DateTime
import java.util.*

fun Date.toDateTime(): DateTime {
    return DateTime(this)
}
