package com.wakacjeapp.trip.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class TripDay(
    var numer: Int,
    var opis: String) : Parcelable
{
    constructor() : this(
        0,
        ""
    )
}