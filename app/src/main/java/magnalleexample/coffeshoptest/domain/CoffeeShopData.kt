package magnalleexample.coffeshoptest.domain

import java.io.Serializable

data class CoffeeShopData(
    val id: Long,
    val name: String,
    val point : Point,
    var dist : Double = 0.0
) : Serializable

data class Point(
    val latitude : Double,
    val longitude : Double
)