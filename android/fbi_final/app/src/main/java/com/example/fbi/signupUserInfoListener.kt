package com.example.fbi

interface signupUserInfoListener {

    fun signupUserInfo1(su_name:String, su_id:String, su_pw:String)

    fun signupUserInfo2(su_age:Int, su_gender:String)

    fun get_name():String?
    fun get_id():String?
    fun get_pw():String?
    fun get_age():Int?
    fun get_gender():String?
}