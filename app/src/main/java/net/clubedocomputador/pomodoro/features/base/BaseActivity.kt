package net.clubedocomputador.pomodoro.features.base

import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import net.clubedocomputador.pomodoro.R
import net.clubedocomputador.pomodoro.messaging.Events
import net.clubedocomputador.pomodoro.util.Ui
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class BaseActivity : AppCompatActivity() {

    var loadingDialog: ProgressDialog? = null

    companion object {
        var lastingLoading: ProgressDialog? = null
    }


    fun showMessage(message: String?) {
        if (message != null) {
            Ui.Messages.message(this, message)
        }
    }

    fun showError(message: String?) {
        if (message != null) {
            Ui.Messages.message(this, message)
        }
    }

    fun showGenericError() {
        Ui.Messages.message(this, this.getString(R.string.error_generic))
    }

    fun hideLoading() {
        loadingDialog?.cancel()
        loadingDialog?.dismiss()
    }

    fun showLoading(@StringRes message: Int) {
        loadingDialog?.cancel()
        loadingDialog?.dismiss()
        loadingDialog = Ui.Dialogs.progress(this, message, true, false)
    }

    fun showLoadingDismissible(@StringRes message: Int) {
        loadingDialog?.cancel()
        loadingDialog?.dismiss()
        loadingDialog = Ui.Dialogs.progress(this, message)
    }


    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
        Events(Events.APP_FOREGROUND)
    }

    override fun onStop() {
        Events(Events.APP_BACKGROUND)
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
        super.onStop()

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessage(message: String) {
        Log.i("ACTION", message)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }


}