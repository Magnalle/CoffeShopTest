package magnalleexample.coffeshoptest.data

import magnalleexample.coffeshoptest.domain.CoffeeShopData
import magnalleexample.coffeshoptest.domain.LoginRequest
import magnalleexample.coffeshoptest.domain.LoginResponse
import magnalleexample.coffeshoptest.domain.CoffeeShopMenuItem
import retrofit2.Call
import retrofit2.http.*

const val API_URL_STRING = "http://185.244.172.108:8080/"
const val LOGIN_URL = "auth/login"
const val REGISTER_URL = "auth/register"
const val LOCATIONS_URL = "locations"
const val MENU_URL = "location/{id}/menu"

interface RetrofitApi {

    @POST(LOGIN_URL)
    fun login(@Body request: LoginRequest)
    : Call<LoginResponse>

    @POST(REGISTER_URL)
    fun register(@Body request: LoginRequest)
            : Call<LoginResponse>

    @GET(LOCATIONS_URL)
    fun getCoffeeShopList(@Header("Authorization") token: String) : Call<List<CoffeeShopData>>

    @GET(MENU_URL)
    fun getMenu(@Header("Authorization") token: String, @Path("id") id : Long) : Call<List<CoffeeShopMenuItem>>
}