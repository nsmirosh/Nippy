package apps.smoll.dragdropgame.utils.ui

import timber.log.Timber
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

fun formatDateTime(pattern: String, millis: Long?): String =
    try {
        SimpleDateFormat(pattern, Locale.getDefault()).let {
            return it.format(Date(millis!!))
        }
    } catch (e: Exception) {
        Timber.e(e)
        "Could not parse time :("
    }

fun getCurrentTimeAsDate(): String {
    val time = Calendar.getInstance().time
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
    return formatter.format(time)
}


fun formatDateFromString(date: String?): String {
    val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
    val parsedDate: Date?

    try {
        parsedDate = inputFormatter.parse(date!!)
    } catch (e: Exception) {
        Timber.e(e)
        return "Could not parse date :("
    }

    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).let {
        return it.format(parsedDate!!)
    }
}


fun getStringFromDate(date: Date): String =
    try {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
    } catch (e: Exception) {
        Timber.e(e)
        "Could not parse date :("
    }


fun getDateFrom(date: String?): Date =
    try {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault()).parse(date!!)
    } catch (e: Exception) {
        Timber.e(e)
        Calendar.getInstance().time
    }
