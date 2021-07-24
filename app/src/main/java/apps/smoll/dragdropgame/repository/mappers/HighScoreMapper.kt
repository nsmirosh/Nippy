package apps.smoll.dragdropgame.repository.mappers

import apps.smoll.dragdropgame.features.entities.domain.HighScore
import apps.smoll.dragdropgame.features.entities.network.NetworkHighScore
import apps.smoll.dragdropgame.utils.ui.getDateFrom

inline fun mapNetworkHighScore(
    input: NetworkHighScore?,
) = HighScore(
    input?.email.orEmpty(),
    input?.noOfCompletedLevels ?: 0,
    input?.totalTime ?: 0,
    getDateFrom(input?.dateCompleted)
)