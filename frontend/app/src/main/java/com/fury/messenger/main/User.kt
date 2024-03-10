package com.fury.messenger.main

class User {

    var name:String?=null;
    var email:String?=null;
    var phoneNumber:String?=null;
    constructor(){}
    constructor(name:String?,email:String?,uid:String?){
        this.email=email
        this.name=name
        this.phoneNumber=uid
    }
}