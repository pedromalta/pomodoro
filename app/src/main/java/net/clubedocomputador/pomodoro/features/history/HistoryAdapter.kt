package net.clubedocomputador.pomodoro.features.history

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.clubedocomputador.pomodoro.R
import net.clubedocomputador.pomodoro.extensions.toDateTime
import net.clubedocomputador.pomodoro.models.local.PomodoroHistory
import net.clubedocomputador.pomodoro.models.local.PomodoroHistory.Status.Finished
import net.clubedocomputador.pomodoro.models.local.PomodoroHistory.Status.Stopped
import net.clubedocomputador.pomodoro.models.view.HistoryItem
import net.clubedocomputador.pomodoro.models.view.HistoryItem.Separator.*
import net.clubedocomputador.pomodoro.models.view.HistoryItemTime.TimeType.*
import net.clubedocomputador.pomodoro.util.Helpers
import org.joda.time.DateTime

class HistoryAdapter(private val context: Context) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private val history: ArrayList<HistoryItem> = arrayListOf()

    private var separatorToday = true
    private var separatorYesterday = true
    private var lastDate: DateTime = DateTime.now()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_list_item_pomodoro, parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return history.size
    }

    fun reset(){
        history.clear()
        separatorToday = true
        separatorYesterday = true
        lastDate = DateTime.now()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(viewHolder: HistoryViewHolder, position: Int) {
        viewHolder.bind(context, history[position])
    }

    private fun processList(pomodoros: List<PomodoroHistory>) {
        val now = DateTime.now()

        for (pomodoro in pomodoros) {
            lastDate = pomodoro.finish.toDateTime()

            //Today
            if (Helpers.Dates.isToday(now, pomodoro.finish.toDateTime()) && separatorToday) {
                separatorToday = false
                history.add(tranformPomodoroToHistoryItem(pomodoro, TODAY))
                continue
            }
            //Yesterday
            if (Helpers.Dates.isYesterday(now, pomodoro.finish.toDateTime()) && separatorYesterday) {
                separatorYesterday = false
                history.add(tranformPomodoroToHistoryItem(pomodoro, YESTERDAY))
                continue
            }
            //Other days
            if (!Helpers.Dates.isToday(now, pomodoro.finish.toDateTime())
                    && !Helpers.Dates.isYesterday(now, pomodoro.finish.toDateTime())
                    && !Helpers.Dates.isToday(pomodoro.finish.toDateTime(), lastDate)) {
                history.add(tranformPomodoroToHistoryItem(pomodoro, DATE))
                continue
            }

            //No Separator
            history.add(tranformPomodoroToHistoryItem(pomodoro, NO_SEPARATOR))
        }
    }

    private fun tranformPomodoroToHistoryItem(pomodoro: PomodoroHistory, separator: HistoryItem.Separator): HistoryItem {
        val elapsedTimer = Helpers.Dates.getDurationString(pomodoro.finish, pomodoro.start)
        val time = Helpers.Dates.getElapsedHistoryTimeItem(pomodoro.finish.toDateTime())

        return HistoryItem(
                elapsedTimer,
                pomodoro.finish,
                pomodoro.status,
                time,
                separator)

    }

    fun swap(list: List<PomodoroHistory>) {
        val size = history.size
        processList(list)
        notifyItemRangeInserted(size, list.size)
    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewTimer: TextView = itemView.findViewById(R.id.text_view_timer)
        private val textViewSeparator: TextView = itemView.findViewById(R.id.text_view_date_separator)
        private val textViewStatus: TextView = itemView.findViewById(R.id.text_view_status)
        private val textViewDate: TextView = itemView.findViewById(R.id.text_view_date)

        fun bind(context: Context, item: HistoryItem) {

            textViewTimer.text = item.timer
            textViewSeparator.visibility = if (item.separator == NO_SEPARATOR) View.GONE else View.VISIBLE

            when (item.separator) {
                NO_SEPARATOR -> {
                }
                TODAY -> textViewSeparator.text = context.getText(R.string.label_date_list_separator_today)
                YESTERDAY -> textViewSeparator.text = context.getText(R.string.label_date_list_separator_yesterday)
                DATE -> textViewSeparator.text = Helpers.Dates.dateFormat(context, item.finish)
            }

            when (item.status) {
                Finished -> textViewStatus.text = context.getText(R.string.label_status_finished)
                Stopped -> textViewStatus.text = context.getText(R.string.label_status_stopped)
            }

            when (item.time.timeType) {
                TIME -> textViewDate.text = Helpers.Dates.timeFormat(context, item.finish)
                MOMENTS -> textViewDate.text = context.getText(R.string.label_moments_ago)
                MINUTE -> textViewDate.text = context.resources.getQuantityString(R.plurals.label_minutes_ago, item.time.timeCount, item.time.timeCount)
                HOUR -> textViewDate.text = context.resources.getQuantityString(R.plurals.label_hours_ago, item.time.timeCount, item.time.timeCount)

            }
        }

    }


}


