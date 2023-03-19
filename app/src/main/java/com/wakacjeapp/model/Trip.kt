package com.wakacjeapp.model

data class Trip(
    val kraj: String ="",
    val zdjecie: Int=0,
    val miejsce: String ="",
    val opis: String="",
    val data_wyjazdu: String="", //TODO Do zmiany format daty ale to na końcu
    val data_powrotu: String="",
    val ilosc_miejsc: Int=0,
    val ocena: Int=0,
    val harmonogram: ArrayList<String>
    ){

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



