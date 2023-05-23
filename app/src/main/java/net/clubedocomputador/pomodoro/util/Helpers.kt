package net.clubedocomputador.pomodoro.util

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.format.DateFormat
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import net.clubedocomputador.pomodoro.R
import net.clubedocomputador.pomodoro.extensions.toDateTime
import net.clubedocomputador.pomodoro.models.view.HistoryItemTime
import org.joda.time.*
import org.joda.time.format.PeriodFormatterBuilder
import java.util.*


object Helpers {


    object Messages {
        private fun processBreakLines(message: String?): String? {
            if (message != null) {
                return message.replace("<br>", "\n")
            }
            return null
        }

        fun message(context: Context, message: String?, show: Boolean = true): Toast {
            val toast = Toast.makeText(context, processBreakLines(message), Toast.LENGTH_LONG)
            val view = toast.view
            view?.setBackgroundResource(R.drawable.bg_radius_dark)
            val text = view?.findViewById<TextView>(android.R.id.message)
            text?.setTextColor(ContextCompat.getColor(context, android.R.color.white))
            if (show) {
                toast.show()
            }
            return toast
        }

        fun message(context: Context, @StringRes message: Int, show: Boolean = true): Toast {
            return message(context, context.getString(message), show)
        }
    }

    object Dates {


        fun dateFormat(context: Context, date: Date): String {
            return DateFormat.getDateFormat(context).format(date).toString()
        }

        fun timeFormat(context: Context, date: Date): String {
            return DateFormat.getTimeFormat(context).format(date).toString()
        }

        fun isToday(now: DateTime, date: DateTime): Boolean {
            return date.withTimeAtStartOfDay().isEqual(now.withTimeAtStartOfDay())
        }

        fun isYesterday(now: DateTime, date: DateTime): Boolean {
            val oneDayBeforeDate = now.minusDays(1).withTimeAtStartOfDay()
            return date.withTimeAtStartOfDay().isEqual(oneDayBeforeDate)
        }


        fun getDurationString(start: Date, finished: Date): String {
            val elapsed = Seconds.secondsBetween(finished.toDateTime(), start.toDateTime())
            return getDurationString(elapsed.seconds)
        }

        fun getDurationString(durationSeconds: Int): String {
            val period = Period(durationSeconds * 1000L)
            val formatted = PeriodFormatterBuilder()
                    .printZeroAlways()
                    .minimumPrintedDigits(2)
                    .appendMinutes()
                    .appendSeparator(":")
                    .appendSeconds()
                    .toFormatter()
            return formatted.print(period)

        }

        fun getElapsedHistoryTimeItem(finishTime: DateTime): HistoryItemTime {
            val minutes = Minutes.minutesBetween(finishTime, DateTime.now())
            if (minutes.minutes == 0) {
                return HistoryItemTime(0, HistoryItemTime.TimeType.MOMENTS)
            }
            val hours = Hours.hoursBetween(finishTime, DateTime.now())
            if (hours.hours == 0) {
                return HistoryItemTime(minutes.minutes, HistoryItemTime.TimeType.MINUTE)
            }
            val days = Days.daysBetween(finishTime, DateTime.now())
            if (days.days == 0 && hours.hours < 2) {
                return HistoryItemTime(hours.hours, HistoryItemTime.TimeType.HOUR)
            }
            return HistoryItemTime(0, HistoryItemTime.TimeType.TIME)
        }

    }

    object Notifications {

        private var vibrator: Vibrator? = null
        private var mediaPlayer: MediaPlayer? = null

        fun startSound(context: Context, @RawRes soundResource: Int = R.raw.ding) {
            stopSound()
            val sound = context.resources.openRawResourceFd(soundResource) ?: return
            mediaPlayer = MediaPlayer().apply {
                setDataSource(sound.fileDescriptor, sound.startOffset, sound.declaredLength)
                setOnPreparedListener {
                    it.start()
                }
                prepareAsync()
            }


        }

        fun startVibrationShort(context: Context, time: Long = 450) {
            stopVibration()
            vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(time, 5))
            } else {
                vibrator?.vibrate(time)
            }
        }

        fun stopSound() {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }

        fun stopVibration() {
            vibrator?.cancel()
        }
    }

}