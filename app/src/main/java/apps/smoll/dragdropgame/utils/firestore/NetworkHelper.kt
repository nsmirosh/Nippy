package apps.smoll.dragdropgame.utils.firestore

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException


suspend fun <T> safeApiCall(
    apiCall: Task<T>
): ResultWrapper<T> {
    return withContext(Dispatchers.IO) {
        try {
             ResultWrapper.Success(apiCall.await())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> ResultWrapper.NetworkError
                else -> {
                    Timber.e("error during the api call = %s", throwable.message)
                    ResultWrapper.GenericError(throwable.message ?: "something strange happened!")
                }
            }
        }
    }
}


suspend fun <T, R> safeApiCall2(
    transform: (T) -> R,
    apiCall: Task<T>,
): ResultWrapper<R> {
    return withContext(Dispatchers.IO) {
        try {
            ResultWrapper.Success(transform(apiCall.await()))
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> ResultWrapper.NetworkError
                else -> {
                    Timber.e("safe api call error = %s", throwable.message)
                    ResultWrapper.GenericError(throwable.message ?: "something strange happened!")
                }
            }
        }
    }
}