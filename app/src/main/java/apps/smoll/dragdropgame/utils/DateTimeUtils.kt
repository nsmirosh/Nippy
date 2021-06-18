package apps.smoll.dragdropgame.utils

import java.text.SimpleDateFormat
import java.util.*

fun formatDateTime(pattern: String, millis: Long): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    val date = Date(millis)
    return formatter.format(date)
}


fun getCurrentTimeAsDate(): String {
    val time  = Calendar.getInstance().time
    val formatter  = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
    return formatter.format(time)
}