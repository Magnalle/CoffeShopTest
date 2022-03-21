package magnalleexample.coffeshoptest.domain

data class LoginResponse(
    val token : String,
    val tokenLifeTime : Long
)
