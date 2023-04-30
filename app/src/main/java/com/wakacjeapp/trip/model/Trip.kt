package com.wakacjeapp.trip.model

import java.util.Date

class Trip {
    var cena: Double
    var data_pocz: Date
    var data_powrotu: Date
    var miejsce: String
    var kraj: String
    var ocena: Double
    var opis: String
    var plan: ArrayList<TripDay>

    constructor(
        data_pocz: Date,
        cena: Double,
        data_powrotu: Date,
        miejsce: String,
        kraj: String,
        ocena: Double,
        opis: String,
        plan: ArrayList<TripDay>
    ) {

        this.data_pocz = data_pocz
        this.cena = cena
        this.data_powrotu = data_powrotu
        this.miejsce = miejsce
        this.kraj = kraj
        this.ocena = ocena
        this.opis = opis
        this.plan = plan
    }


}
