package magnalleexample.coffeshoptest.ui.basket

import android.app.Application
import magnalleexample.coffeshoptest.App
import magnalleexample.coffeshoptest.domain.CoffeeShopMenuItem
import magnalleexample.coffeshoptest.ui.BaseViewModel

class BasketViewModel(app: Application) : BaseViewModel(app) {
    fun loadData() : List<CoffeeShopMenuItem>{
        return getApplication<App>().menuList.filter {
            it.amount > 0
        }
    }
}