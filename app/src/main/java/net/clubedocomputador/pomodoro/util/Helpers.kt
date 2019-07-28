package net.clubedocomputador.pomodoro.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.ViewAnimationUtils
import android.view.Window
import android.view.animation.AccelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import net.clubedocomputador.pomodoro.R
import net.clubedocomputador.pomodoro.extensions.toDateTime
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.Period
import org.joda.time.Seconds
import org.joda.time.format.PeriodFormatterBuilder
import java.text.DecimalFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap


object Helpers {

    object Keyboard {

        fun hide(activity: Activity) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
        }
    }

    object Dialogs {

        fun progress(context: Context, message: String, show: Boolean = true, cancelable: Boolean = false): ProgressDialog {
            val progressBar = ProgressDialog(context)
            progressBar.window?.setBackgroundDrawable(context.getDrawable(R.drawable.round_white_box))
            progressBar.setMessage(message)
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progressBar.setCancelable(cancelable)
            if (show) {
                progressBar.show()
            }
            return progressBar
        }

        fun progress(context: Context, @StringRes message: Int, show: Boolean = true, cancelable: Boolean = false): ProgressDialog {
            return progress(context, context.getString(message), show, cancelable)
        }

        fun dialogAlertAction(context: Context, message: String?, action: DialogInterface.OnClickListener? = null, cancelable: Boolean = false, show: Boolean = true): Dialog {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(cancelable)
            dialog.setContentView(R.layout.custom_dialogbox)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val text = dialog.findViewById(R.id.mensagem) as TextView
            text.text = message

            val cancelButton = dialog.findViewById(R.id.btn_sim) as Button
            cancelButton.visibility = View.GONE

            val okButton = dialog.findViewById(R.id.btn_nao) as Button
            okButton.setText(R.string.bt_continuar)
            if (action == null) {
                okButton.setOnClickListener {
                    dialog.dismiss()
                }
            } else {
                okButton.setOnClickListener { action.onClick(dialog, R.id.btn_sim) }
            }
            if (show)
                dialog.show()
            return dialog
        }

        fun dialogAlertAction(context: Context, @StringRes message: Int, action: DialogInterface.OnClickListener? = null, cancelable: Boolean = false, show: Boolean = true): Dialog {
            return dialogAlertAction(context, context.getString(message), action, cancelable, show)
        }
    }

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
            view.setBackgroundResource(R.drawable.bg_radius_dark)
            val text = view.findViewById<TextView>(android.R.id.message)
            text.setTextColor(context.getColor(android.R.color.white))
            if (show) {
                toast.show()
            }
            return toast
        }

        fun message(context: Context, @StringRes message: Int, show: Boolean = true): Toast {
            return message(context, context.getString(message), show)
        }
    }

    object Validates {

        private const val EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$"

        fun email(email: String?): Boolean {
            if (email.isNullOrBlank())
                return false

            val pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(email.trim())
            return matcher.matches()

        }

        fun textViewconfirmPassword(password: TextView, confirmPassword: TextView): Boolean {
            return if (password.text.toString() == confirmPassword.text.toString()) {
                confirmPassword.error = null
                true
            } else {
                confirmPassword.error = confirmPassword.context.getString(R.string.validate_confirm_password)
                false
            }
        }

        fun textViewEmpty(textView: TextView, fieldName: String): Boolean {
            if (textView.text.isNullOrBlank()) {
                textView.error = textView.context.getString(R.string.validate_empty, fieldName)
                return false
            } else {
                textView.error = null
            }
            return true
        }

        fun textViewMinLength(textView: TextView, minLength: Int, fieldName: String): Boolean {
            if (!textViewEmpty(textView, fieldName)) {
                return false
            } else {
                textView.error = null
            }
            if (textView.text.length < minLength) {
                textView.error = textView.context.getString(R.string.validate_length, fieldName, minLength)
                return false
            } else {
                textView.error = null
            }
            return true
        }

        fun textViewEmail(textView: TextView, fieldName: String = "email"): Boolean {
            if (!textViewEmpty(textView, fieldName)) {
                return false
            } else {
                textView.error = null
            }
            if (!email(textView.text.toString())) {
                textView.error = textView.context.getString(R.string.validate_email, fieldName)
                return false
            } else {
                textView.error = null
            }
            return true
        }
    }

    object Image {

        fun getDrawable(context: Context, id: String): Int {
            return context.resources.getIdentifier(id, "drawable", context.packageName)
        }
    }

    object Layout {

        fun startRevealAnimation(viewInside: View, viewOutside: View) {
            val cx = viewOutside.measuredWidth / 2
            val cy = viewOutside.measuredHeight / 2
            val anim = ViewAnimationUtils.createCircularReveal(viewInside, cx, cy, 50f, viewOutside.width.toFloat())
            anim.duration = 500
            anim.interpolator = AccelerateInterpolator(2f)
            anim.addListener(object : AnimatorListenerAdapter() {})
            anim.start()
        }


        fun animateViewVisibility(view: View, visibility: Int) {
            // cancel runnning animations and remove any listeners
            view.animate().cancel()
            view.animate().setListener(null)
            // animate making view visible
            if (visibility == View.VISIBLE) {
                view.animate().alpha(1f).start()
                view.visibility = View.VISIBLE
            } else {
                view.animate().setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        view.visibility = visibility
                    }
                }).alpha(0f).start()
            }
        }

        fun getThemedResId(activity: Activity, @AttrRes attr: Int): Int {
            val a = activity.theme.obtainStyledAttributes(intArrayOf(attr))
            val resId = a.getResourceId(0, 0)
            a.recycle()
            return resId
        }
    }

    object Timers {

        private val timers = HashMap<String, Timer>()

        fun startTimer(id: String, seconds: Int, task: TimerTask, period: Int? = null) {
            val time = seconds * 1000L //conversion to milliseconds

            cancelTimer(id)
            val timer = Timer()
            if (period == null) {
                timer.schedule(task, time)
            } else {
                //conversion to milliseconds for periodic task
                timer.schedule(task, time, period * 1000L)
            }
            timers[id] = timer
        }

        fun cancelTimer(id: String) {
            timers[id]?.cancel()
            timers[id]?.purge()
            timers.remove(id)
        }

    }

    object Dates {

        fun formatDefault(date: Date): String {
            return android.text.format.DateFormat.format("hh:mm", date).toString()
        }

        fun formatWithSeconds(date: Date): String {
            return android.text.format.DateFormat.format("hh:mm:ss", date).toString()
        }

        fun formatWithYear(date: Date): String {
            return android.text.format.DateFormat.format("dd/MM/YYYY - hh:mm", date).toString()
        }

        fun isToday(now: DateTime, date: DateTime): Boolean {
            return now.withTimeAtStartOfDay().isEqual(date.withTimeAtStartOfDay())
        }

        fun isYesterday(now: DateTime, date: DateTime): Boolean {
            val oneDayBeforeDate = date.withTimeAtStartOfDay().minusDays(1)
            return now.withTimeAtStartOfDay().isEqual(oneDayBeforeDate)
        }

        fun isThisWeek(now: DateTime, date: DateTime): Boolean {
            return now.year == date.year && now.monthOfYear == date.monthOfYear && now.weekOfWeekyear == date.weekOfWeekyear
        }

        fun isThisMonth(now: DateTime, date: DateTime): Boolean {
            return now.year == date.year && now.monthOfYear == date.monthOfYear
        }

        fun isThisYear(now: DateTime, date: DateTime): Boolean {
            return now.year == date.year
        }

        fun getDurationString(start: Date, finished: Date): String{
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

        fun getElapsedTime(finishTime: DateTime): String {
            val remaining = Seconds.secondsBetween(DateTime.now(), finishTime)
            return getDurationString(remaining.seconds)

        }
    }

    object Notifications {

        val vibrationPattern = longArrayOf(200, 100, 200, 275, 425, 100, 200, 100, 200, 275, 425, 100, 75, 25, 75, 125, 75, 25, 75, 125, 100, 100)
        private var vibrator: Vibrator? = null
        private var soundPool: SoundPool? = null

        fun startSound(context: Context, isLooping: Boolean = true, @RawRes sound: Int? = null) {
            stopSound()

            val audioAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()

            soundPool = SoundPool.Builder().setAudioAttributes(audioAttributes).build()
            var resource = R.raw.v1_sound
            if (sound != null) {
                resource = sound
            }
            val loop = if (isLooping) -1 else 0 // -1 ele faz loop, 0 ele toca 1 vez
            soundPool?.setOnLoadCompleteListener { _, sampleId, _ ->
                soundPool?.play(sampleId, 1F, 1F, 1, loop, 1f)
            }
            soundPool?.load(context, resource, 1)

        }

        fun startVibrationLong(context: Context) {
            stopVibration()
            vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator?.vibrate(vibrationPattern, 0)

        }

        fun startVibrationShort(context: Context) {
            stopVibration()
            vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(300, 5))
            } else {
                vibrator?.vibrate(300)
            }
        }

        fun stopSound() {
            soundPool?.release()
            soundPool = null
        }

        fun stopVibration() {
            vibrator?.cancel()
        }
    }

}