package com.wakacjeapp.trip.model

import com.wakacjeapp.User

class Trip {
    var cena: Double = 0.0
    var data_pocz: String = ""
    var data_powrotu: String =""
    var miejsce: String = ""
    var kraj: String = ""
    var ocena: Double = 0.0
    var opis: String = ""
    var zdjecie: String = ""
    var ilosc_miejsc: Int = 0
    var plan: ArrayList<TripDay> = ArrayList()
    var uzytkownicy: ArrayList<User> = ArrayList()
    var id_wycieczki: String = ""


    constructor()
    constructor(
        data_pocz: String,
        cena: Double,
        data_powrotu: String,
        miejsce: String,
        kraj: String,
        ocena: Double,
        opis: String,
        zdjecie: String,
        ilosc_miejsc: Int,
        plan: ArrayList<TripDay>,
        uzytkownicy: ArrayList<User>,
        id_wycieczki: String
    ) {

        this.data_pocz = data_pocz
        this.cena = cena
        this.data_powrotu = data_powrotu
        this.miejsce = miejsce
        this.kraj = kraj
        this.ocena = ocena
        this.opis = opis
        this.ilosc_miejsc = ilosc_miejsc
        this.plan = plan
        this.zdjecie = zdjecie
        this.uzytkownicy = uzytkownicy
        this.id_wycieczki = id_wycieczki
    }
    constructor(
        data_pocz: String,
        cena: Double,
        data_powrotu: String,
        miejsce: String,
        kraj: String,
        ocena: Double,
        opis: String,
        zdjecie: String,
        ilosc_miejsc: Int,
        plan: ArrayList<TripDay>,
        uzytkownicy: ArrayList<User>
    ) {

        this.data_pocz = data_pocz
        this.cena = cena
        this.data_powrotu = data_powrotu
        this.miejsce = miejsce
        this.kraj = kraj
        this.ocena = ocena
        this.opis = opis
        this.ilosc_miejsc = ilosc_miejsc
        this.plan = plan
        this.zdjecie = zdjecie
        this.uzytkownicy = uzytkownicy

    }


}
