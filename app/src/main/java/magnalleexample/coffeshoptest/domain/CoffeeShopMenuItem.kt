package magnalleexample.coffeshoptest.domain

import android.os.Parcel
import android.os.Parcelable

data class CoffeeShopMenuItem(
    val id: Long,
    val name: String,
    val imageURL: String,
    val price: Double,
    var amount : Int = 0
): Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(imageURL)
        parcel.writeDouble(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CoffeeShopMenuItem> {
        override fun createFromParcel(parcel: Parcel): CoffeeShopMenuItem {
            return CoffeeShopMenuItem(parcel)
        }

        override fun newArray(size: Int): Array<CoffeeShopMenuItem?> {
            return arrayOfNulls(size)
        }
    }
}
