package com.wakacjeapp

class User {
    var name: String? = null
    var email: String? = null
    var uid: String? = null
    var data_rezerwacji: String? =null


    constructor()

    constructor(name: String?,email: String?,uid:String?){
        this.name = name
        this.email = email
        this.uid = uid
    }

    constructor(name: String?,email: String?,uid:String?,data_rezerwacji:String?){
        this.name = name
        this.email = email
        this.uid = uid
        this.data_rezerwacji = data_rezerwacji
    }
}