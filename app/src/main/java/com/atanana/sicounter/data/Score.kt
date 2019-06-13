package com.atanana.sicounter.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Score(val name: String, val score: Int) : Parcelable