package com.codinginflow.firebasewatching

class User{
    var uid : String? = null
    var name : String? = null
    var email : String? = null

    constructor(){}

    constructor(name: String? , email: String?, uid: String?){
        this.name = name
        this.email = email
        this.uid = uid
    }
}