package apps.smoll.dragdropgame.repository.mappers

import apps.smoll.dragdropgame.features.entities.domain.HighScore
import apps.smoll.dragdropgame.features.entities.network.NetworkHighScore
import apps.smoll.dragdropgame.utils.getDateFrom

class HighScoreListMapper : ListMapper<NetworkHighScore, HighScore> {

    private val highScoreMapper = HighScoreMapper()

    override fun map(input: List<NetworkHighScore>): List<HighScore> =
        input.map {
            highScoreMapper.map(it)
        }
}