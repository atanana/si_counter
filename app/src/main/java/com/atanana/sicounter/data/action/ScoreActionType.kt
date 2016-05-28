package com.atanana.sicounter.data.action

import android.os.Parcel
import android.os.Parcelable

enum class ScoreActionType : Parcelable {
    PLUS,
    MINUS;

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(ordinal)
    }

    override fun describeContents(): Int {
        return 0;
    }

    @Suppress("unused")
    val CREATOR: Parcelable.Creator<ScoreActionType> = object : Parcelable.Creator<ScoreActionType> {
        override fun createFromParcel(parcel: Parcel): ScoreActionType {
            return values()[parcel.readInt()]
        }

        override fun newArray(size: Int): Array<ScoreActionType?> {
            return arrayOfNulls(size)
        }
    }
}