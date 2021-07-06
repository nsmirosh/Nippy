package apps.smoll.dragdropgame.repository.mappers

import apps.smoll.dragdropgame.features.entities.domain.HighScore
import apps.smoll.dragdropgame.features.entities.network.NetworkHighScore
import apps.smoll.dragdropgame.utils.getDateFrom

class HighScoreListMapper : ListMapper<NetworkHighScore, HighScore> {

    override fun map(input: List<NetworkHighScore>): List<HighScore> =
        input.map {
            HighScore(
                it.email!!,
                it.noOfCompletedLevels!!,
                it.totalTime!!,
                getDateFrom(it.dateCompleted!!)
            )
        }
}