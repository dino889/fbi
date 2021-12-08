package com.example.fbi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : Fragment(), View.OnClickListener {

    private var su_userinfo1: signupUserInfoListener? = null

    private var su_name : String? = null
    private var su_id: String? = null
    private var su_pw: String? = null
    private var su_pwChk: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_sign_up, container, false)

        //버튼 클릭 처리
//        val btn_check_email : TextView = root.findViewById(R.id.btn_check_email)
//        btn_check_email.setOnClickListener(this)
        val btn_signUp_end : TextView = root.findViewById(R.id.btn_signUp_end)
        btn_signUp_end.setOnClickListener(this)

        return root
    }

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity is signupUserInfoListener) {
            this.su_userinfo1 = activity as signupUserInfoListener?
        }
    }

    override fun onClick(v: View?) {

        when(v?.id) {
//            R.id.btn_check_email -> {
//                Toast.makeText(activity?.applicationContext, "중복 확인", Toast.LENGTH_SHORT).show()
//            }
            R.id.btn_signUp_end -> {

                su_name = et_signUp_user_name.text.toString()
                su_id = et_signUp_id.text.toString()
                su_pw = et_signUp_password.text.toString()
                su_pwChk = et_signUp_password_check.text.toString()

//                Log.e("su_name", su_name)

                if((su_name == "") || (su_id == "") || (su_pw == "") || (su_pwChk == "")) {
                    Toast.makeText(activity?.applicationContext, "입력되지 않은 정보가 있습니다.\n 다시 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
                else {
                    if(su_pw == su_pwChk) {

                        su_userinfo1?.signupUserInfo1(su_name!!, su_id!!, su_pw!!)
//                        Log.e("유저인포", su_userinfo1.toString())


                        val transaction = activity?.supportFragmentManager?.beginTransaction()
                        transaction?.replace(R.id.frame_sign_up, CustomSetting1Fragment())
                        transaction?.addToBackStack(null)
                        transaction?.commit()
                    }
                    else {
                        Toast.makeText(activity?.applicationContext, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}