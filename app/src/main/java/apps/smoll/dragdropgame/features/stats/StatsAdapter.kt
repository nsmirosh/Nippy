package apps.smoll.dragdropgame.features.stats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import apps.smoll.dragdropgame.R
import apps.smoll.dragdropgame.features.entities.domain.HighScore
import apps.smoll.dragdropgame.features.entities.network.NetworkHighScore
import apps.smoll.dragdropgame.utils.formatDateFromString
import apps.smoll.dragdropgame.utils.formatDateTime
import apps.smoll.dragdropgame.utils.getStringFromDate

class StatsAdapter(private val dataSet: List<HighScore>) :
    RecyclerView.Adapter<StatsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val email: TextView = view.findViewById(R.id.email)
        val noOfLevels: TextView = view.findViewById(R.id.noOfLevelsCompleted)
        val totalTime: TextView = view.findViewById(R.id.totalTime)
        val dateCompleted: TextView = view.findViewById(R.id.dateCompleted)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_stats_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = with(viewHolder) {
        dataSet[position].let {
            email.text = it.email
            dateCompleted.text = getStringFromDate(it.dateCompleted)
            noOfLevels.text = it.noOfCompletedLevels.toString()
            totalTime.text = formatDateTime("mm:ss:SSS", it.totalTime)
        }
    }

    override fun getItemCount() = dataSet.size
}
