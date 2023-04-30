package com.wakacjeapp.trip.model

class TripDay {
    var numer = 0
    var opis: String = ""

    constructor()
    constructor(numer: Int, opis: String)
    {
        this.numer = numer
        this.opis = opis
    }
}
