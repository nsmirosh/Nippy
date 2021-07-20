package apps.smoll.dragdropgame.repository.mappers

import apps.smoll.dragdropgame.features.entities.domain.HighScore
import apps.smoll.dragdropgame.repository.LevelStats
import apps.smoll.dragdropgame.utils.ui.getDateFrom

class LevelStatsToHighScoreMapper : Mapper<LevelStats, HighScore> {

    override fun map(input: LevelStats) = with(input) {
        HighScore(
            email = "",
            noOfCompletedLevels = levelToBePlayed.dec(),
            totalTime = totalTimeInMillis,
            dateCompleted = getDateFrom(dateCompleted)
        )
    }
}