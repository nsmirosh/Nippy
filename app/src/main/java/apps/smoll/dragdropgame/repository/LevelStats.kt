package apps.smoll.dragdropgame.repository

data class LevelStats(

    val dateCompletedMillis: Long = System.currentTimeMillis(),
    val durationMilli: Long = dateCompletedMillis,
    val score: Int = -1,
    var level: Int = -1)