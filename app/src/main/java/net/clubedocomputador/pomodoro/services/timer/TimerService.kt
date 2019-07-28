package net.clubedocomputador.pomodoro.services.timer

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.RemoteViews
import androidx.annotation.Nullable
import androidx.core.app.NotificationCompat
import com.vicpin.krealmextensions.save
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import net.clubedocomputador.pomodoro.R
import net.clubedocomputador.pomodoro.features.principal.PrincipalActivity
import net.clubedocomputador.pomodoro.persistence.models.Pomodoro
import net.clubedocomputador.pomodoro.services.notification.Notification
import net.clubedocomputador.pomodoro.util.Analytics
import net.clubedocomputador.pomodoro.util.Helpers
import org.joda.time.DateTime
import org.joda.time.Seconds
import java.util.*
import java.util.concurrent.TimeUnit


class TimerService : Service() {

    companion object {
        private const val START_ACTION = "action.start"
        private const val STOP_ACTION = "action.stop"
        private const val POMODORO_MINUTES = 25

        var lastElapsedTime = "$POMODORO_MINUTES:00"
            private set
        var remaining: Seconds? = null
            private set

        fun start(context: Context) {
            val startIntent = Intent(context, TimerService::class.java)
            startIntent.action = START_ACTION
            context.startService(startIntent)
        }

        fun stop(context: Context) {
            val startIntent = Intent(context, TimerService::class.java)
            startIntent.action = STOP_ACTION
            context.startService(startIntent)
        }

        var isRunning = false
            private set
    }


    private var disposable = CompositeDisposable()
    private lateinit var pomodoro: Pomodoro
    private lateinit var finishTime: DateTime

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
        if (isRunning) return

        isRunning = true
        lastElapsedTime = "$POMODORO_MINUTES:00"
        remaining = null

        pomodoro = createPomodoro()
        finishTime = DateTime.now().plusMinutes(POMODORO_MINUTES)
        updateTimer()
        val notification = updateNotification()

        startForeground(Notification.NOTIFICATION_ID, notification)
        startUpdateTimer()

    }

    private fun createPomodoro(): Pomodoro {
        val pomodoro = Pomodoro()
        pomodoro.id = UUID.randomUUID().toString()
        pomodoro.save()
        return pomodoro
    }

    private fun startUpdateTimer() {
        disposable.dispose()
        disposable = CompositeDisposable()
        Observable.create<String> {
                it.onNext(lastElapsedTime)
        }.delaySubscription(1, TimeUnit.SECONDS).repeat()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                        updateTimer()
                    updateNotification()


                }, { Analytics.logError("Erro ao atualizar Timer", it) }
                ).addTo(disposable)
    }

    private fun updateTimer(){
        lastElapsedTime = Helpers.Dates.getElapsedTime(finishTime)
        checkTime(pomodoro)

    }

    private fun updateNotification(): android.app.Notification {
        val notificationIntent = Intent(this, PrincipalActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 634, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val expandedView = RemoteViews(packageName, R.layout.notification_expanded)

        expandedView.setTextViewText(R.id.timestamp, lastElapsedTime)
        expandedView.setTextViewText(R.id.content_title, descricao)
        expandedView.setTextViewText(R.id.notification_expected_end_time, getString(R.string.label_notification_expected_end_time, formatTime(this)))
        expandedView.setTextViewText(R.id.notification_start_time, getString(R.string.label_notification_start_time, formatTime(indisponibilidade.dataInicioDateTime)))

        //val collapsedView = RemoteViews(packageName, R.layout.notification_collapsed_indisponibilidade)
        //collapsedView.setTextViewText(R.id.timestamp, timer)

        val notification = NotificationCompat.Builder(this, Notification.NOTIFICATION_CHANNEL)
                .setContentTitle("V1 Motorista")
                .setContentText(lastElapsedTime)
                .setSmallIcon(R.drawable.ic_notification_logo)
                .setContentIntent(pendingIntent)
                .setCustomContentView(expandedView)
                .setCustomBigContentView(expandedView)
                .setOngoing(true).build()

        Notification.updateNotification(
                this@TimerService,
                Notification.NOTIFICATION_ID,
                notification)

        return notification


    }


    private fun stopService() {
        isRunning = false
        remaining = null
        lastElapsedTime = "$POMODORO_MINUTES:00"
        disposable.dispose()
        stopForeground(true)
    }

    private fun checkTime(pomodoro: Pomodoro) {
        if (DateTime.now().isAfter(finishTime)) {
            notifyFinish()
            pomodoro.finish = DateTime.now().toDate()
            pomodoro.save()
            stopService()
        }

    }

    private fun notifyFinish(){
        Helpers.Notifications.startVibrationShort(this@TimerService)
        Helpers.Notifications.startSound(this@TimerService, false, R.raw.alarm)
    }


    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        return null
    }


}


