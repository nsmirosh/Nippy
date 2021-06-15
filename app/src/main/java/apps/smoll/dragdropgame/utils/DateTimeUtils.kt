package apps.smoll.dragdropgame.utils

import apps.smoll.dragdropgame.features.game.intervalInMilliseconds
import java.text.SimpleDateFormat
import java.util.*


class DateTimeUtils {


}


fun formatDateTime(millisUntilFinished: Long): String {
    val formatter = SimpleDateFormat("mm:ss", Locale.getDefault())
    val date = Date(millisUntilFinished)
    return formatter.format(date)

}
fun formatSeconds(millisUntilFinished: Long) : String{
    val afterComa = (millisUntilFinished % 1000) / intervalInMilliseconds
    val beforeComa = (millisUntilFinished / 1000).toInt()
    return "$beforeComa,$afterComa"
}