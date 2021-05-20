package apps.smoll.dragdropgame.features.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

/**
 * Created by Vladyslav Bondar on 14.01.2021.
 * skype: diginital
 */
abstract class BaseViewModel : ViewModel() {
    internal val hideKeyBoard = MutableLiveData<Boolean>()
    protected val _genericErrorMessage: MutableLiveData<Throwable> = MutableLiveData()
    val genericErrorMessage: LiveData<Throwable> get() = _genericErrorMessage
    protected val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun hideKeyboard(): () -> Unit = { hideKeyBoard.postValue(true) }

    fun showKeyboard(): () -> Unit = { hideKeyBoard.postValue(false) }

}