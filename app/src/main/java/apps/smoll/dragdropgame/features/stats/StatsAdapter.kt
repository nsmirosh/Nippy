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

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateCompleted: TextView
        val levelNo: TextView
        val timeToComplete: TextView


        init {
            dateCompleted = view.findViewById(R.id.dateCompleted)
            levelNo = view.findViewById(R.id.levelNo)
            timeToComplete = view.findViewById(R.id.timeToComplete)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_stats_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        
        val levelStats = dataSet[position]

        viewHolder.dateCompleted.text = levelStats.dateCompletedMillis.toString()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
