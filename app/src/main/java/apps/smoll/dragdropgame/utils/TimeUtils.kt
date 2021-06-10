package apps.smoll.dragdropgame.utils

import apps.smoll.dragdropgame.features.game.intervalInMilliseconds

fun formatSeconds(millisUntilFinished: Long) : String{
    val afterComa = (millisUntilFinished % 1000) / intervalInMilliseconds
    val beforeComa = (millisUntilFinished / 1000).toInt()
    return "$beforeComa,$afterComa"
}