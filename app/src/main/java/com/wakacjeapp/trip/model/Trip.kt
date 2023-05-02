package com.wakacjeapp.trip.model

import android.os.Parcelable
import com.wakacjeapp.User
import kotlinx.parcelize.Parcelize


@Parcelize
data class Trip(
    var cena: Double = 0.0,
    var data_pocz: String = "",
    var data_powrotu: String = "",
    var miejsce: String = "",
    var kraj: String = "",
    var ocena: Double = 0.0,
    var opis: String = "",
    var zdjecie: String = "",
    var ilosc_miejsc: Int = 0,
    var plan: ArrayList<TripDay> = ArrayList(),
    var uzytkownicy: ArrayList<User> = ArrayList(),
    var id_wycieczki: String = ""
) : Parcelable {
    constructor() : this(
        0.0,
        "",
        "",
        "",
        "",
        0.0,
        "",
        "",
        0,
        ArrayList(),
        ArrayList(),
        ""
    )
}
