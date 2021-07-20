package apps.smoll.dragdropgame.utils.firestore


sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T): ResultWrapper<T>()
    data class GenericError(val message: String): ResultWrapper<Nothing>()
    object NetworkError: ResultWrapper<Nothing>()
}