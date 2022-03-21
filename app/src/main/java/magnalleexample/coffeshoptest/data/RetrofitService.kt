package magnalleexample.coffeshoptest.data

import magnalleexample.coffeshoptest.domain.CoffeeShopData
import magnalleexample.coffeshoptest.domain.LoginRequest
import magnalleexample.coffeshoptest.domain.CoffeeShopMenuItem
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {
    val CODE_QUERY_ERROR = 400
    val CODE_USER_DONT_EXISTS = 404
    val CODE_USER_NAME_IN_USE = 406
    val CODE_USER_NOT_LOGIN = 401


    private val retrofit = Retrofit.Builder()
        .baseUrl(API_URL_STRING)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private var api: RetrofitApi = retrofit.create(RetrofitApi::class.java)

    fun getError(code : Int) : String{
        return when(code){
            CODE_QUERY_ERROR -> "Ошибка в запросе"
            CODE_USER_DONT_EXISTS -> "Пользователь не существует"
            CODE_USER_NAME_IN_USE -> "Такое имя уже успользуется"
            CODE_USER_NOT_LOGIN -> "Пользователь не авторизован!"
            else -> "Неизвестная ошибка"
        }
    }

    fun login(loginRequest: LoginRequest, onSuccess : (String) -> Unit, onFailure : (String, Boolean) -> Unit){
        try {
            val response = api.login(loginRequest).execute()
            if(response.isSuccessful){
                onSuccess.invoke(response.body()?.token?:"")
            }else{
                val code = response.code()
                onFailure.invoke(getError(code),(code == CODE_USER_NOT_LOGIN))
            }
        }
        catch (e: Exception){
            onFailure.invoke(e.toString(), false)
        }
    }

    fun register(
        loginRequest: LoginRequest,
        onSuccess : (String) -> Unit,
        onFailure : (String, Boolean) -> Unit){
        try {
            val response = api.register(loginRequest).execute()
            if (response.isSuccessful) {
                onSuccess.invoke(response.body()?.token ?: "")
            } else {
                val code = response.code()
                onFailure.invoke(getError(code), (code == CODE_USER_NOT_LOGIN))
            }
        }
        catch (e: Exception){
            onFailure.invoke(e.toString(), false)
        }
    }

    fun getCoffeeShopList(
        token: String,
        onSuccess : (List<CoffeeShopData>) -> Unit,
        onFailure : (String, Boolean) -> Unit){
        try {
            val response = api.getCoffeeShopList("Bearer $token").execute()
            if (response.isSuccessful) {
                onSuccess.invoke(response.body()?: listOf<CoffeeShopData>())
            } else {
                val code = response.code()
                onFailure.invoke(getError(code), (code == CODE_USER_NOT_LOGIN))
            }
        }
        catch (e: Exception){
            onFailure.invoke(e.toString(), false)
        }
    }

    fun getMenu(
        token: String,
        coffeeShopData: CoffeeShopData,
        onSuccess : (List<CoffeeShopMenuItem>) -> Unit,
        onFailure : (String, Boolean) -> Unit) {
        try {
            val response = api.getMenu("Bearer $token", coffeeShopData.id).execute()
            if (response.isSuccessful) {
                onSuccess.invoke(response.body()?: listOf<CoffeeShopMenuItem>())
            } else {
                val code = response.code()
                onFailure.invoke(getError(code), (code == CODE_USER_NOT_LOGIN))
            }
        }
        catch (e: Exception){
            onFailure.invoke(e.toString(), false)
        }
    }

}