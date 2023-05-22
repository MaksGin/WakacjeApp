package com.wakacjeapp.trip.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class TripDayItem (
    var godzina: String,
    var opis: String
) : Parcelable
{
    constructor() :this (
        "",
        ""
    )
}