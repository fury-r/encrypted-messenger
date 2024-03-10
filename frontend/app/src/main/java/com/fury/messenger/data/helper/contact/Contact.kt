package com.fury.messenger.data.helper.contact

import android.util.Log
import com.fury.messenger.utils.Constants.getCountryCodeFromPhone
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber

public class Contact {
    private  var id:String
    private   var name:String?=null
    private   var phoneNumber:String=""
    private   var image:String?=null
    private   var pubKey:String?=null

    private  var countryCode:String?=null
    private var isVerified:Boolean=false
    private  var uuid:String?=null

    constructor(id:String,name:String?,phoneNumber:String,image:String?,pubKey:String?="",uuid:String?=""){
        this.id=id
        if (name != null) {
            this.name=name
        }
        this.setPhoneNumber(phoneNumber)
        if (image != null) {
            this.image=image
        }
        this.setPubKey(pubKey)
        this.setUUID(uuid)
    }
    fun   getUUID(): String? {
        return this.uuid
    }
    fun   setUUID(uuid:String?="") {
        this.uuid=uuid
    }

    fun getCountryCode():String?{
       return this.countryCode
    }
    fun setPubKey(key:String?=""){
        this.pubKey=key
    }
    fun getPubKey():String?{
        return this.pubKey
    }
    fun setCountryCode(code:String?=""){
        this.countryCode=code
    }
 fun   getId(): String {
        return this.id
    }

    fun   getName(): String? {
        return this.name
    }
    fun   setName(name:String?="") {
       this.name=name
    }
    fun   setPhoneNumber(phoneNumber:String) {
        var number=phoneNumber
        if (phoneNumber != null && number!=null) {
            val countryCode= getCountryCodeFromPhone(phoneNumber)
            if(countryCode!=null){
                number = number.replace("+$countryCode", "")
                this.setCountryCode(countryCode)

            }
            this.phoneNumber=number

        }
    }

    fun   getPhoneNumber(): String {
        return this.phoneNumber
    }

    fun   getImage(): String? {
        return this.image
    }
    fun setImage(image: String?){
            if (image != null) {
                this.image=image
            }

    }
    fun setIsVerified(isVerified:Boolean){
        this.isVerified=isVerified
    }

    fun getIsVerified():Boolean{

        return this.isVerified
    }
}
