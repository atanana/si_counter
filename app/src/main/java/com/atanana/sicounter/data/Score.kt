package com.atanana.sicounter.data

import android.os.Parcel
import android.os.Parcelable

data class Score(val name: String, val score: Int) : Parcelable {
    private constructor(parcel: Parcel) : this(parcel.readString(), parcel.readInt())

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(name)
        dest?.writeInt(score)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @Suppress("unused")
        @JvmField
        final val CREATOR: Parcelable.Creator<Score> = object : Parcelable.Creator<Score> {
            override fun createFromParcel(parcel: Parcel): Score {
                return Score(parcel)
            }

            override fun newArray(size: Int): Array<Score?> {
                return arrayOfNulls(size)
            }
        }
    }
}