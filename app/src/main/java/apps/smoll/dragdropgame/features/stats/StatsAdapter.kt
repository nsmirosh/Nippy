package apps.smoll.dragdropgame.features.stats

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import apps.smoll.dragdropgame.R
import apps.smoll.dragdropgame.repository.LevelStats

class StatsAdapter(dataSet: List<LevelStats>) :
    RecyclerView.Adapter<StatsAdapter.ViewHolder>() {

    private val sortedDataSet: List<LevelStats> = dataSet.sortedByDescending { it.dateCompletedMillis }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateCompleted: TextView = view.findViewById(R.id.dateCompleted)
        val levelNo: TextView = view.findViewById(R.id.levelNo)
        val timeToComplete: TextView = view.findViewById(R.id.timeToComplete)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_stats_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val levelStats = sortedDataSet[position]

        val now = System.currentTimeMillis()
        val date = DateUtils.getRelativeTimeSpanString(
            levelStats.dateCompletedMillis,
            now,
            DateUtils.DAY_IN_MILLIS
        )

        with(viewHolder) {
            dateCompleted.text = date
            levelNo.text = levelStats.levelToBePlayed.toString()
            timeToComplete.text = levelStats.levelTimeInMillis.toString()
        }
    }

    override fun getItemCount() = sortedDataSet.size
}
