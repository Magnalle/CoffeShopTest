package magnalleexample.coffeshoptest.ui.menu

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import magnalleexample.coffeshoptest.App
import magnalleexample.coffeshoptest.app
import magnalleexample.coffeshoptest.domain.CoffeeShopData
import magnalleexample.coffeshoptest.domain.CoffeeShopMenuItem
import magnalleexample.coffeshoptest.ui.BaseViewModel

class MenuViewModel(app: Application) : BaseViewModel(app) {

    private val _navigateToLogin = MutableLiveData<Boolean>(false)
    val navigateToLogin : LiveData<Boolean>
        get() = _navigateToLogin

    private val _navigateToBasket = MutableLiveData<CoffeeShopData?>(null)
    val navigateToBasket : LiveData<CoffeeShopData?>
        get() = _navigateToBasket

    fun disableNavigation(){
        _navigateToLogin.postValue(false)
        _navigateToBasket.postValue(null)
    }

    fun onBasketButtonClicked(data: CoffeeShopData){
        if(getApplication<App>().menuList.find { menuItem ->
            menuItem.amount > 0
        } == null)
            _displayError.postValue("В меню ничего не выбрано!")
        else{
            _navigateToBasket.postValue(data)
        }
    }
}