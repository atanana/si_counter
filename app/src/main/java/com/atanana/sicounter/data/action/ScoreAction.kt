package com.atanana.sicounter.data.action

import android.os.Parcel
import android.os.Parcelable
import com.atanana.sicounter.exceptions.SiCounterException

data class ScoreAction(val type: ScoreActionType, val price: Int?, val id: Int) : Parcelable {
    constructor(type: ScoreActionType, id: Int) : this(type, null, id)

    val absolutePrice: Int
        get() {
            val safePrice = price ?: throw SiCounterException("No price defined!")
            return when (type) {
                ScoreActionType.PLUS -> safePrice
                ScoreActionType.MINUS -> -safePrice
            }
        }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeParcelable(type, 0)
        dest?.writeValue(price)
        dest?.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    @Suppress("unused")
    val CREATOR: Parcelable.Creator<ScoreAction> = object : Parcelable.Creator<ScoreAction> {
        override fun createFromParcel(parcel: Parcel): ScoreAction {
            val type = parcel.readParcelable<ScoreActionType>(ScoreActionType::class.java.classLoader)
            val price = parcel.readValue(Int::class.java.classLoader) as Int
            return ScoreAction(type, price, parcel.readInt())
        }

        override fun newArray(size: Int): Array<ScoreAction?> {
            return arrayOfNulls(size)
        }
    }
}

