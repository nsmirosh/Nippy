package apps.smoll.dragdropgame.utils

import java.text.SimpleDateFormat
import java.util.*

fun formatDateTime(pattern: String, millisUntilFinished: Long): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    val date = Date(millisUntilFinished)
    return formatter.format(date)
}