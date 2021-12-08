package com.example.fbi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class SignUpActivity : AppCompatActivity(), View.OnClickListener, signupUserInfoListener {

    private var set_su_name : String? = null
    private var set_su_id: String? = null
    private var set_su_pw: String? = null
    private var set_su_age: Int? = null
    private var set_su_gender: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_main)


        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_sign_up, SignUpFragment())
            .commit()

    }

    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Override
    override fun signupUserInfo1(su_name:String, su_id:String, su_pw:String) {

        set_su_name = su_name
        set_su_id = su_id
        set_su_pw = su_pw

    }

    @Override
    override fun signupUserInfo2(su_age:Int, su_gender:String) {

        set_su_age = su_age
        set_su_gender = su_gender
    }

    @Override
    override fun get_name():String?{
        return set_su_name
    }

    @Override
    override fun get_id():String?{
        return set_su_id
    }

    @Override
    override fun get_pw():String?{
        return set_su_pw
    }

    @Override
    override fun get_age():Int?{
        return set_su_age
    }

    @Override
    override fun get_gender():String?{
        return set_su_gender
    }


}
