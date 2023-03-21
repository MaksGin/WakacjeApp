package com.wakacjeapp.model

class Trip_old {
    var offer: String? = null
        private set
    var image = 0
        private set

    constructor() {
    }

    constructor(offer: String?, image: Int) {
        this.offer = offer
        this.image = image
    }
}



//
//    fun Uzupelnij_harmonogram(
//        Lista : ArrayList<String>,
//        nazwa : String
//    ) {
////        val db = Firebase.firestore
////        val collectionRef = db.collection("nazwa_kolekcji")
//
//        var dzien = 1
//        val data = arrayListOf<HashMap<String, Any>>()
//
//        for(element in Lista) {
//            val map = hashMapOf<String, Any>()
//            map["dzien${dzien}"] = element
//            data.add(map)
//            dzien++
//        }
//        for (map in data) {
////            collectionRef.document().set(map)
//        }



