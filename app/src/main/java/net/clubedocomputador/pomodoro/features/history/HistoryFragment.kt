package net.clubedocomputador.pomodoro.features.history

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.clubedocomputador.pomodoro.R
import net.clubedocomputador.pomodoro.features.base.BaseFragment
import net.clubedocomputador.pomodoro.features.principal.PrincipalTabbedView
import net.clubedocomputador.pomodoro.util.Analytics
import net.clubedocomputador.pomodoro.views.EndlessScrollView
import java.util.*

class HistoryFragment : BaseFragment(), HistoryMvpView, PrincipalTabbedView {

    private val presenter = HistoryPresenter()

    private lateinit var recyclerView: RecyclerView
    private var lastDate: Date? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        presenter.attachView(this)
        recyclerView = view.findViewById(R.id.list_history)

        setupRecyclerView()

        Analytics.logEvent(Analytics.EVENT_CONTENT_VIEW, Analytics.Contents.FRAGMENT_HISTORY)
        return view
    }

    private fun setupRecyclerView() {
        val context = activity ?: return

        recyclerView.adapter = HistoryAdapter(context)
        loadMoreHistoryItens()
        val linearLayout = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayout
        recyclerView.addOnScrollListener(object : EndlessScrollView(linearLayout) {
            override fun onLoadMore(currentPage: Int) {
                loadMoreHistoryItens()
            }

        })
    }

    private fun loadMoreHistoryItens() {
        val historyList = presenter.getList(lastDate)
        if (historyList.isNotEmpty()) {
            lastDate = historyList.last().finish
            val adapter = recyclerView.adapter
            if (adapter is HistoryAdapter) {
                adapter.swap(historyList)
            }
        }
    }


    override fun getTabTitle(context: Context): String {
        return context.getString(R.string.label_menu_history)
    }

    override fun getLayout(): Int {
        return R.layout.fragment_history
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

}