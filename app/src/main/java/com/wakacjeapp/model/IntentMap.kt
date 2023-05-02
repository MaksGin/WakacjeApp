package com.wakacjeapp.model
import com.wakacjeapp.client_interface.AllTrips
import com.wakacjeapp.client_interface.ClientTrips

class IntentMap {
    val intentMap = mapOf(
        "moje_wycieczki" to ClientTrips::class.java,
        "oferty" to AllTrips::class.java,
    )
}