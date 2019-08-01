package net.clubedocomputador.pomodoro.services.timer

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.annotation.Nullable
import androidx.core.app.NotificationCompat
import com.vicpin.krealmextensions.save
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import net.clubedocomputador.pomodoro.PomodoroApp
import net.clubedocomputador.pomodoro.R
import net.clubedocomputador.pomodoro.config.Config
import net.clubedocomputador.pomodoro.features.principal.PrincipalActivity
import net.clubedocomputador.pomodoro.messaging.Events
import net.clubedocomputador.pomodoro.persistence.models.Pomodoro
import net.clubedocomputador.pomodoro.services.notification.Notification
import net.clubedocomputador.pomodoro.util.Analytics
import net.clubedocomputador.pomodoro.util.Helpers
import org.joda.time.DateTime
import java.util.concurrent.TimeUnit


class TimerService : Service() {

    companion object : ITimerProvider {
        private const val START_ACTION = "action.start"
        private const val STOP_ACTION = "action.stop"

        override fun start(context: Context) {
            val startIntent = Intent(context, TimerService::class.java)
            startIntent.action = START_ACTION
            context.startService(startIntent)
        }

        override fun stop(context: Context) {
            val startIntent = Intent(context, TimerService::class.java)
            startIntent.action = STOP_ACTION
            context.startService(startIntent)
        }

        override var isRunning = false
            private set
    }


    private var disposable = CompositeDisposable()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.apply {
            when (this.action) {
                START_ACTION -> startService()
                STOP_ACTION -> stopService()
            }
        }

        return START_STICKY
    }


    private fun startService() {
        if (isRunning || PomodoroApp.persistence.pomodoro == null) return

        isRunning = true

        startUpdateTimer()
        val notification = updateNotification()

        startForeground(Notification.NOTIFICATION_ID, notification)
        startUpdateTimer()

    }

    private fun startUpdateTimer() {
        disposable.dispose()
        disposable = CompositeDisposable()
        Single.create<String> {
            val remainingTime = PomodoroApp.persistence.pomodoro?.elapsedTime
                    ?: Config.POMODORO_MINUTES_STRING
            it.onSuccess(remainingTime)
        }.delaySubscription(1, TimeUnit.SECONDS).repeat()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    checkTime()
                    updateNotification()
                }.addTo(disposable)
    }

    private fun updateNotification(): android.app.Notification {
        Notification.startChannel(this)
        val notificationIntent = Intent(this, PrincipalActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 634, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this, Notification.NOTIFICATION_CHANNEL)
                .setContentTitle(getString(R.string.notification_running))
                .setContentText(PomodoroApp.persistence.pomodoro?.elapsedTime
                        ?: Config.POMODORO_MINUTES_STRING)
                .setSmallIcon(R.drawable.icon_pomodoro)
                .setContentIntent(pendingIntent)
                .setOngoing(true).build()
        //avoid adding notification if we are stopping
        if (isRunning) {
            Notification.updateNotification(
                    this@TimerService,
                    Notification.NOTIFICATION_ID,
                    notification)
        }

        return notification


    }


    private fun stopService() {
        isRunning = false
        disposable.dispose()
        stopForeground(true)
    }

    private fun checkTime() {
        val pomodoro = PomodoroApp.persistence.pomodoro
        if (pomodoro == null) {
            stopService()
        } else if (DateTime.now().isAfter(pomodoro.finishDate)) {
            Analytics.logEvent(Analytics.EVENT_TIMER_FINISHED)
            finishPomodoro(pomodoro)
            notifyFinish()
            stopService()
        }

    }

    private fun finishPomodoro(pomodoro: Pomodoro) {
        pomodoro.status = Pomodoro.Status.Finished.value
        pomodoro.finish = pomodoro.finishDate.toDate()
        pomodoro.save()
        PomodoroApp.persistence.pomodoro = null
        Events(Events.FINISHED_POMODORO)
    }

    private fun notifyFinish() {
        Helpers.Notifications.startVibrationShort(this)
        Helpers.Notifications.startSound(this)
    }


    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        return null
    }


}


