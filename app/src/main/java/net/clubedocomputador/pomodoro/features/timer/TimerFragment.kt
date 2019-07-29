package net.clubedocomputador.pomodoro.features.timer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.Single
import io.reactivex.rxkotlin.addTo
import net.clubedocomputador.pomodoro.PomodoroApp
import net.clubedocomputador.pomodoro.R
import net.clubedocomputador.pomodoro.features.base.BaseFragment
import net.clubedocomputador.pomodoro.features.principal.PrincipalTabbedView
import net.clubedocomputador.pomodoro.util.Analytics
import org.jetbrains.anko.textColor
import java.util.concurrent.TimeUnit
import android.view.animation.TranslateAnimation
import io.reactivex.android.schedulers.AndroidSchedulers
import net.clubedocomputador.pomodoro.messaging.Events
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.image


class TimerFragment : BaseFragment(), TimerMvpView, PrincipalTabbedView {

    private val presenter = TimerPresenter()

    private lateinit var buttonStartStop: FloatingActionButton
    private lateinit var textViewTimer: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        presenter.attachView(this)
        buttonStartStop = view.findViewById(R.id.button_start_stop)
        textViewTimer = view.findViewById(R.id.text_view_timer)
        updateLayoutState()
        setupButtonStartStop()
        setupTextViewTimer()

        Analytics.logEvent(Analytics.EVENT_CONTENT_VIEW, Analytics.Contents.FRAGMENT_TIMER)
        return view
    }

    private fun setupTextViewTimer() {
        textViewTimer.text = presenter.getTimer()
        startTimerUpdates()

    }

    private fun startTimerUpdates() {
        Single.create<String> { it.onSuccess(presenter.getTimer()) }
                .delaySubscription(1, TimeUnit.SECONDS)
                .repeatUntil { !presenter.isPomodoroRunning() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { remainingTime ->
                    //TODO animate?
                    textViewTimer.text = remainingTime
                }.addTo(presenter.disposable)
    }

    private fun setupButtonStartStop() {
        buttonStartStop.setOnClickListener {
            if (PomodoroApp.persistence.pomodoro == null) {
                presenter.startPomodoro()
            } else {
                presenter.stopPomodoro()
            }
        }
    }

    private fun updateLayoutState() {
        if (presenter.isPomodoroRunning()) {
            stateRunning()
        } else {
            stateStopped()
        }
    }

    private fun stateRunning() {
        context?.apply {
            textViewTimer.textColor = ContextCompat.getColor(this, R.color.red)
            buttonStartStop.image = ContextCompat.getDrawable(this, R.drawable.ic_stop_white_32dp)

        }
    }

    private fun stateStopped() {
        context?.apply {
            textViewTimer.textColor = ContextCompat.getColor(this, R.color.grey)
            buttonStartStop.image = ContextCompat.getDrawable(this, R.drawable.ic_play_arrow_white_32dp)

        }
    }

    override fun startedPomodoro() {
        updateLayoutState()
        startTimerUpdates()
    }

    override fun stoppedPomodoro() {
        textViewTimer.text = presenter.getTimer()
        updateLayoutState()
    }

    @Suppress("UNUSED")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvents(event: Events) {

        when (event.action) {
            Events.FINISHED_POMODORO -> stoppedPomodoro()
        }
    }

    override fun getTabTitle(context: Context): String {
        return context.getString(R.string.label_menu_new)
    }

    override fun getLayout(): Int {
        return R.layout.fragment_timer
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

}