package apps.smoll.dragdropgame.features.entities.domain

import apps.smoll.dragdropgame.features.entities.network.NetworkHighScore
import java.util.*

data class HighScore (
    var email: String,
    val noOfCompletedLevels: Int,
    val totalTime: Long,
    val dateCompleted: Date
) : Comparable<HighScore> {

    override fun compareTo(other: HighScore): Int {
        if (noOfCompletedLevels > other.noOfCompletedLevels) {
            return 1
        }
        if (noOfCompletedLevels == other.noOfCompletedLevels) {
            return (other.totalTime - totalTime).toInt()
        }
        return -1
    }
}