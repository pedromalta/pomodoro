package net.clubedocomputador.pomodoro.features.base

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import net.clubedocomputador.pomodoro.R
import net.clubedocomputador.pomodoro.messaging.Events
import net.clubedocomputador.pomodoro.util.Helpers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class BaseActivity : AppCompatActivity() {

    fun showMessage(message: String?) {
        if (message != null) {
            Helpers.Messages.message(this, message)
        }
    }

    fun showError(message: String?) {
        if (message != null) {
            Helpers.Messages.message(this, message)
        }
    }

    fun showGenericError() {
        Helpers.Messages.message(this, this.getString(R.string.error_generic))
    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        Events(Events.APP_FOREGROUND)
    }

    override fun onStop() {
        Events(Events.APP_BACKGROUND)
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        super.onStop()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(message: String) {
        Log.i("ACTION", message)
    }
}
