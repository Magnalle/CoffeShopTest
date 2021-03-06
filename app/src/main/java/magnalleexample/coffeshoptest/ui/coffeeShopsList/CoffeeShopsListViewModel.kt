package magnalleexample.coffeshoptest.ui.coffeeShopsList

import android.app.Application
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import magnalleexample.coffeshoptest.App
import magnalleexample.coffeshoptest.domain.CoffeeShopData
import magnalleexample.coffeshoptest.ui.BaseViewModel

class CoffeeShopsListViewModel(app: Application) : BaseViewModel(app) {

    private val _updateCoffeeShopsList: MutableLiveData<Boolean> = MutableLiveData(false)
    val updateCoffeeShopsList : LiveData<Boolean>
    get() = _updateCoffeeShopsList
    private val _navigateToMenu = MutableLiveData<CoffeeShopData?>(null)
    val navigateToMenu : LiveData<CoffeeShopData?>
        get() = _navigateToMenu

    private val _menuUpdating = MutableLiveData<CoffeeShopData?>(null)
    val menuUpdating : LiveData<CoffeeShopData?>
        get() = _menuUpdating

    private val _navigateToLogin = MutableLiveData<Boolean>(false)
    val navigateToLogin : LiveData<Boolean>
        get() = _navigateToLogin

    val location : MutableLiveData<Location?> = MutableLiveData(null)

    fun loadData(){
        scope.launch(Dispatchers.IO) {
            val token = getApplication<App>().sessionManager.fetchAuthToken()
            if(token == null){
                _displayError.postValue("Пользователь не авторизован!")
            }else {
                getApplication<App>().retrofitService.getCoffeeShopList(
                    token,
                    {
                        getApplication<App>().coffeeShopsList = it
                        location.value?.let { updateDistance() }
                        _updateCoffeeShopsList.postValue(true)
                    },
                    { error, needToLogin ->
                        _displayError.postValue(error)
                        if(needToLogin)
                            _navigateToLogin.postValue(true)
                    })
            }
        }
    }

    fun onCoffeeShopClick(data: CoffeeShopData){
        _menuUpdating.postValue(data)
    }

    fun updateMenu(data: CoffeeShopData){
        scope.launch(Dispatchers.IO) {
            val token = getApplication<App>().sessionManager.fetchAuthToken()
            if(token == null){
                _displayError.postValue("Пользователь не авторизован!")
                _navigateToLogin.postValue(true)
            }else {
                if(!data.equals(getApplication<App>().lastCoffeeShop))
                    getApplication<App>().retrofitService.getMenu(
                        token,
                        data,
                        {
                            getApplication<App>().menuList = it
                            getApplication<App>().lastCoffeeShop = data
                            _menuUpdating.postValue(null)
                            _navigateToMenu.postValue(data)
                        },
                        { error, needToLogin ->
                            _displayError.postValue(error)
                            if(needToLogin)
                                _navigateToLogin.postValue(true)
                        })
                else {
                    _menuUpdating.postValue(null)
                    _navigateToMenu.postValue(data)
                }
            }
        }
    }

    fun disableNavigation(){
        _navigateToMenu.postValue(null)
        _navigateToLogin.postValue(false)
    }

    fun coffeeShopsListLoaded(){
        _updateCoffeeShopsList.postValue(false)
    }

    fun updateDistance(){
        location.value?.let {
            for(shop in getApplication<App>().coffeeShopsList){
                val loc = Location("")
                loc.latitude = shop.point.latitude
                loc.longitude = shop.point.longitude
                shop.dist = it.distanceTo(loc).toDouble()
            }
            _updateCoffeeShopsList.postValue(true)
        }
    }

}