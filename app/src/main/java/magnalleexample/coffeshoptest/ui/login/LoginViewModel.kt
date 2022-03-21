package magnalleexample.coffeshoptest.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import magnalleexample.coffeshoptest.App
import magnalleexample.coffeshoptest.data.RetrofitService
import magnalleexample.coffeshoptest.data.SessionManager
import magnalleexample.coffeshoptest.domain.LoginRequest
import magnalleexample.coffeshoptest.ui.BaseViewModel

class LoginViewModel(app: Application) : BaseViewModel(app) {

    private val _navigateToCoffeeShopsList : MutableLiveData<Boolean> = MutableLiveData(false)
    val navigateToCoffeeShopList : LiveData<Boolean>
    get() = _navigateToCoffeeShopsList

    fun login(login : String, password : String){
        when {
            login == "" -> {
                _displayError.postValue("Не заполнен e-mail!")
                return
            }
            password == "" -> {
                _displayError.postValue("Не заполнен пароль!")
                return
            }
            else -> {
                scope.launch(Dispatchers.IO) {
                    getApplication<App>().retrofitService.login(
                        LoginRequest(login, password), { token ->
                            getApplication<App>().sessionManager.saveAuthToken(token)
                            _navigateToCoffeeShopsList.postValue(true)
                        },
                        { error, needToLogin ->
                            _displayError.postValue(error)
                        }
                    )
                }
            }
        }
    }

    fun disableNavigation(){
        _navigateToCoffeeShopsList.postValue(false)
    }
}