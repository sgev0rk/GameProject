package com.android.gameproject

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GameData(
    val team1Name: String,
    val team2Name: String,
    val durationInMills: Long,
    val team1Result: Int = 0,
    val team2Result: Int = 0
) :
    Parcelable
