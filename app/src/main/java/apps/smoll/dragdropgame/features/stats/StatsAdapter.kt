package apps.smoll.dragdropgame.features.stats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import apps.smoll.dragdropgame.R
import apps.smoll.dragdropgame.repository.LevelStats

class StatsAdapter(dataSet: List<LevelStats>) :
    RecyclerView.Adapter<StatsAdapter.ViewHolder>() {

    private val sortedDataSet: List<LevelStats> = dataSet.sortedByDescending { it.dateCompleted }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView = view.findViewById(R.id.userName)
        val noOfLevels: TextView = view.findViewById(R.id.noOfLevelsCompleted)
        val totalTime: TextView = view.findViewById(R.id.totalTime)
        val dateCompleted: TextView = view.findViewById(R.id.dateCompleted)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_stats_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val levelStats = sortedDataSet[position]

        val date = /*DateUtils.getRelativeTimeSpanString(
            levelStats.dateCompleted,
            now,
            DateUtils.DAY_IN_MILLIS
        )*/ ""

        with(viewHolder) {
            dateCompleted.text = date
            noOfLevels.text = levelStats.levelToBePlayed.toString()
            totalTime.text = levelStats.levelTimeInMillis.toString()
        }
    }

    override fun getItemCount() = sortedDataSet.size
}
