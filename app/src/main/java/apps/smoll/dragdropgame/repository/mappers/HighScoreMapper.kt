package apps.smoll.dragdropgame.repository.mappers

import apps.smoll.dragdropgame.features.entities.domain.HighScore
import apps.smoll.dragdropgame.features.entities.network.NetworkHighScore
import apps.smoll.dragdropgame.utils.ui.getDateFrom

class HighScoreMapper : Mapper<NetworkHighScore, HighScore> {

    override fun map(input: NetworkHighScore) = with(input) {
        HighScore(
            email!!,
            noOfCompletedLevels!!,
            totalTime!!,
            getDateFrom(dateCompleted!!)
        )
    }
}