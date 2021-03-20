package apps.smoll.dragdropgame.features.stats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import apps.smoll.dragdropgame.R
import apps.smoll.dragdropgame.database.LevelStats

class StatsAdapter(private val dataSet: List<LevelStats>) :
    RecyclerView.Adapter<StatsAdapter.ViewHolder>() {

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
        
        val levelStats = dataSet[position]

        with(viewHolder) {
            dateCompleted.text = levelStats.dateCompletedMillis.toString()
            levelNo.text = levelStats.levelNo.toString()
            timeToComplete.text = levelStats.durationMilli.toString()
        }
    }

    override fun getItemCount() = dataSet.size
}
