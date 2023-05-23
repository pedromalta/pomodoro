package net.clubedocomputador.pomodoro.features.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import net.clubedocomputador.pomodoro.R
import net.clubedocomputador.pomodoro.util.Helpers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

abstract class BaseFragment : androidx.fragment.app.DialogFragment() {

    @LayoutRes
    protected abstract fun getLayout(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(getLayout(), container, false)
    }

    fun showMessage(message: String?) {
        val context = context ?: return
        if (message != null) {
            Helpers.Messages.message(context, message)
        }
    }

    fun showError(message: String?) {
        val context = context ?: return
        if (message != null) {
            Helpers.Messages.message(context, message)
        }
    }

    fun showGenericError() {
        context?.apply {
            Helpers.Messages.message(this, this.getString(R.string.error_generic))
        }
    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onStop() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        super.onStop()
    }

    @Subscribe
    fun onMessage(message: String) {
        // metodo necessario pois nem toda classe filho utiliza o eventbus
    }
}
