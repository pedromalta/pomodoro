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
import net.clubedocomputador.pomodoro.models.view.HistoryItem
import net.clubedocomputador.pomodoro.util.Helpers
import org.joda.time.DateTime

class HistoryAdapter(private val context: Context) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private val history: ArrayList<HistoryItem> = arrayListOf()

    private var separatorToday = true
    private var separatorYesterday = true
    private var separatorThisWeek = true
    private var separatorThisMonth = true
    private var separatorThisYear = true
    private var separatorOlder = true


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_list_item_pomodoro, parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return history.size
    }

    override fun onBindViewHolder(viewHolder: HistoryViewHolder, position: Int) {
        viewHolder.bind(context, history[position])
    }

    private fun processList(pomodoros: List<PomodoroHistory>): List<HistoryItem> {
        val now = DateTime.now()

        for (pomodoro in pomodoros) {
            if (Helpers.Dates.isToday(now, pomodoro.finish.toDateTime()) && separatorToday) {
                separatorToday = false
                history.add(tranformPomodoroToHistoryItem(pomodoro, HistoryItem.Separator.TODAY))
                continue
            }
            if (Helpers.Dates.isYesterday(now, pomodoro.finish.toDateTime()) && separatorYesterday) {
                separatorYesterday = false
                history.add(tranformPomodoroToHistoryItem(pomodoro, HistoryItem.Separator.YESTERDAY))
                continue
            }
            if (!Helpers.Dates.isToday(now, pomodoro.finish.toDateTime())
                    && !Helpers.Dates.isYesterday(now, pomodoro.finish.toDateTime())
                    && Helpers.Dates.isThisWeek(now, pomodoro.finish.toDateTime())
                    && separatorThisWeek) {
                separatorThisWeek = false
                history.add(tranformPomodoroToHistoryItem(pomodoro, HistoryItem.Separator.THIS_WEEK))
                continue
            }
            if (!Helpers.Dates.isToday(now, pomodoro.finish.toDateTime())
                    && !Helpers.Dates.isYesterday(now, pomodoro.finish.toDateTime())
                    && !Helpers.Dates.isThisWeek(now, pomodoro.finish.toDateTime())
                    && Helpers.Dates.isThisMonth(now, pomodoro.finish.toDateTime()) && separatorThisMonth) {
                separatorThisMonth = false
                history.add(tranformPomodoroToHistoryItem(pomodoro, HistoryItem.Separator.THIS_MONTH))
                continue
            }
            if (!Helpers.Dates.isToday(now, pomodoro.finish.toDateTime())
                    && !Helpers.Dates.isYesterday(now, pomodoro.finish.toDateTime())
                    && !Helpers.Dates.isThisWeek(now, pomodoro.finish.toDateTime())
                    && !Helpers.Dates.isThisMonth(now, pomodoro.finish.toDateTime())
                    && Helpers.Dates.isThisYear(now, pomodoro.finish.toDateTime()) && separatorThisYear) {
                separatorThisYear = false
                history.add(tranformPomodoroToHistoryItem(pomodoro, HistoryItem.Separator.THIS_YEAR))
                continue
            }
            if (!Helpers.Dates.isToday(now, pomodoro.finish.toDateTime())
                    && !Helpers.Dates.isYesterday(now, pomodoro.finish.toDateTime())
                    && !Helpers.Dates.isThisWeek(now, pomodoro.finish.toDateTime())
                    && !Helpers.Dates.isThisMonth(now, pomodoro.finish.toDateTime())
                    && !Helpers.Dates.isThisYear(now, pomodoro.finish.toDateTime())
                    && !Helpers.Dates.isThisYear(now, pomodoro.finish.toDateTime()) && separatorOlder) {
                separatorOlder = false
                history.add(tranformPomodoroToHistoryItem(pomodoro, HistoryItem.Separator.OLDER))
                continue
            }

            history.add(tranformPomodoroToHistoryItem(pomodoro, HistoryItem.Separator.NO_SEPARATOR))
        }

        return arrayListOf()
    }

    private fun tranformPomodoroToHistoryItem(pomodoro: PomodoroHistory, separator: HistoryItem.Separator): HistoryItem {
        val elapsedTimer = Helpers.Dates.getDurationString(pomodoro.finish, pomodoro.start)

        return HistoryItem(elapsedTimer, pomodoro.finish, "", "", separator)

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
            textViewSeparator.visibility = if (item.separator == HistoryItem.Separator.NO_SEPARATOR) View.GONE else View.VISIBLE
            /* tvCarro.text = item.descricaoVeiculo
             tvTextoInicio.text = context.getString(R.string.text_start_viagem, item.dataInicioRealizada, item.horaInicioRealizada)
             tvTextoFim.text = context.getString(R.string.text_finish_viagem, item.dataFimRealizada, item.horaFimRealizada)*/
        }

    }


}


