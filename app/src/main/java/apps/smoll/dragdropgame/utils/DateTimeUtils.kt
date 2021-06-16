package apps.smoll.dragdropgame.utils

import java.text.SimpleDateFormat
import java.util.*

fun formatDateTime(pattern: String, millis: Long): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    val date = Date(millis)
    return formatter.format(date)
}