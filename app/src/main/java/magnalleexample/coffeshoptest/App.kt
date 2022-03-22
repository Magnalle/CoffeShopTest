package magnalleexample.coffeshoptest

import android.app.Application
import android.content.Context
import magnalleexample.coffeshoptest.data.RetrofitService
import magnalleexample.coffeshoptest.data.SessionManager
import magnalleexample.coffeshoptest.domain.CoffeeShopData
import magnalleexample.coffeshoptest.domain.CoffeeShopMenuItem

class App : Application() {
    val sessionManager: SessionManager by lazy {
        SessionManager(this.applicationContext)
    }
    val retrofitService : RetrofitService by lazy {
        RetrofitService()
    }

    var menuList : List<CoffeeShopMenuItem> = listOf()
    var lastCoffeeShop : CoffeeShopData? = null
    var coffeeShopsList : List<CoffeeShopData> = listOf()
    var apiKeySet : Boolean = false
}

val Context.app
    get() = applicationContext as App