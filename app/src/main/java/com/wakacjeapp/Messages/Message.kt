package com.wakacjeapp.Messages

class Message {

    var message: String? = null
    var senderId: String? = null
    var senderName: String? = null
    constructor(){}

    constructor(message: String?, senderId: String?,senderName: String?){
        this.message = message
        this.senderId = senderId
        this.senderName = senderName
    }

}