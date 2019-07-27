package net.clubedocomputador.pomodoro.features.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.clubedocomputador.pomodoro.R
import net.clubedocomputador.pomodoro.features.base.BaseFragment
import net.clubedocomputador.pomodoro.features.principal.PrincipalTabbedView

class TimerFragment : BaseFragment(), TimerMvpView, PrincipalTabbedView {

    private val presenter = TimerPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        presenter.attachView(this)


        return view
    }

    override fun getTabTitle(): String {
        return context?.getString(R.string.menu_label_new) ?: "New"
    }

    override fun getLayout(): Int {
        return R.layout.fragment_timer
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

}