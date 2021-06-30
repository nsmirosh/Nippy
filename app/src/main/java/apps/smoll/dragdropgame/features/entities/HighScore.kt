package apps.smoll.dragdropgame.features.entities

data class HighScore(
    var email: String? = null,
    val noOfCompletedLevels: Int? = null,
    val totalTime: Long? = null,
    val dateCompleted: String? = null
) : Comparable<HighScore> {

    override fun compareTo(other: HighScore): Int {
        if (noOfCompletedLevels!! > other.noOfCompletedLevels!!) {
            return 1
        }
        if (noOfCompletedLevels == other.noOfCompletedLevels) {
            return (other.totalTime!! - totalTime!!).toInt()
        }
        return -1
    }
}
