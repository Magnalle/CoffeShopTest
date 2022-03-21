package magnalleexample.coffeshoptest.ui.registration

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import magnalleexample.coffeshoptest.App
import magnalleexample.coffeshoptest.domain.LoginRequest
import magnalleexample.coffeshoptest.ui.BaseViewModel

class RegistrationViewModel(app: Application) : BaseViewModel(app) {

    private val _navigateToCoffeeShopsList : MutableLiveData<Boolean> = MutableLiveData(false)
    val navigateToCoffeeShopList : LiveData<Boolean>
        get() = _navigateToCoffeeShopsList

    fun register(login : String, password : String, repeatPassword : String){
        when {
            login == "" -> {
                _displayError.postValue("Не заполнен e-mail!")
                return
            }
            password == "" -> {
                _displayError.postValue("Не заполнен пароль!")
                return
            }
            password != repeatPassword ->{
                _displayError.postValue("Пароль и повторение пароля не совпадают!")
                return
            }
            else -> {
                scope.launch(Dispatchers.IO) {
                    getApplication<App>().retrofitService.register(
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