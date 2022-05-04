package es.ucm.foodlook.types

import android.os.Parcel
import android.os.Parcelable

class Recent : Parcelable {
    var id = ""
    var name = ""
    var image = ""

    constructor(id: String, name: String, image: String) {
        this.id = id
        this.name = name
        this.image = image
    }

    constructor(p0: Parcel) {
        id = p0.readString().toString()
        name = p0.readString().toString()
        image = p0.readString().toString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeString(id)
        p0?.writeString(name)
        p0?.writeString(image)
    }

    companion object CREATOR : Parcelable.Creator<Recent> {
        override fun createFromParcel(parcel: Parcel): Recent {
            return Recent(parcel)
        }

        override fun newArray(size: Int): Array<Recent?> {
            return arrayOfNulls(size)
        }
    }
}
