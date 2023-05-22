package com.wakacjeapp.trip.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class TripDay(
    var numer: Int,
    var opis: String,
    var plan_godzinowy: ArrayList<TripDayItem> = ArrayList()
) : Parcelable

{
    constructor() : this(
        0,
        "",
        ArrayList()
    )
}