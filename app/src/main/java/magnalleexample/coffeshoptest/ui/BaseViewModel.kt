package magnalleexample.coffeshoptest.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import magnalleexample.coffeshoptest.data.RetrofitService
import magnalleexample.coffeshoptest.data.SessionManager

abstract class BaseViewModel(app: Application) : AndroidViewModel(app) {

    protected val scope = CoroutineScope(Job() + Dispatchers.Main)

    protected val _displayError : MutableLiveData<String?> = MutableLiveData()
    val displayError : LiveData<String?>
        get() = _displayError

    fun disableError(){
        _displayError.postValue(null)
    }
}