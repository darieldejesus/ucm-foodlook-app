package es.ucm.foodlook.types

import android.os.Parcel
import android.os.Parcelable

class Dish : Parcelable {
    var id = ""
    var name = ""

    constructor(id: String, name: String) {
        this.id = id
        this.name = name
    }

    constructor(p0: Parcel) {
        id = p0.readString().toString()
        name = p0.readString().toString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeString(id)
        p0?.writeString(name)
    }

    companion object CREATOR : Parcelable.Creator<Dish> {
        override fun createFromParcel(parcel: Parcel): Dish {
            return Dish(parcel)
        }

        override fun newArray(size: Int): Array<Dish?> {
            return arrayOfNulls(size)
        }
    }
}
