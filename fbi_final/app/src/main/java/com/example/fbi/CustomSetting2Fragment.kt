package com.example.fbi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_custom_setting2.*

class CustomSetting2Fragment: Fragment(), View.OnClickListener {

    var white :Int = 0
    var black :Int = 0

    private var su_name : String? = null
    private var su_id: String? = null
    private var su_pw: String? = null
    private var su_age: Int? = null
    private var su_gender: String? = null
    private var su_category: Int? = null

    private var su_userinfo3: signupUserInfoListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_custom_setting2, container, false)

        white = ContextCompat.getColor(activity!!, R.color.white)
        black = ContextCompat.getColor(activity!!, R.color.black)

        //버튼 클릭 이벤트 리스너 설정
        var btn_category_history : Button = root.findViewById(R.id.btn_category_history)
        btn_category_history.setOnClickListener(this)
        var btn_category_psychology : Button = root.findViewById(R.id.btn_category_psychology)
        btn_category_psychology.setOnClickListener(this)
        var btn_category_essay : Button = root.findViewById(R.id.btn_category_essay)
        btn_category_essay.setOnClickListener(this)
        var btn_category_humanities : Button = root.findViewById(R.id.btn_category_humanities)
        btn_category_humanities.setOnClickListener(this)
        var btn_category_science : Button = root.findViewById(R.id.btn_category_science)
        btn_category_science.setOnClickListener(this)
        var btn_category_detective : Button = root.findViewById(R.id.btn_category_detective)
        btn_category_detective.setOnClickListener(this)
        var btn_customSet_category_end : Button = root.findViewById(R.id.btn_customSet_category_end)
        btn_customSet_category_end.setOnClickListener(this)
        var btn_customSet_category_finish : Button = root.findViewById(R.id.btn_customSet_category_finish)
        btn_customSet_category_finish.setOnClickListener(this)
        var btn_category_history_cover : Button = root.findViewById(R.id.btn_category_history_cover)
        btn_category_history_cover.setOnClickListener(this)
        var btn_category_psychology_cover : Button = root.findViewById(R.id.btn_category_psychology_cover)
        btn_category_psychology_cover.setOnClickListener(this)
        var btn_category_essay_cover : Button = root.findViewById(R.id.btn_category_essay_cover)
        btn_category_essay_cover.setOnClickListener(this)
        var btn_category_humanities_cover : Button = root.findViewById(R.id.btn_category_humanities_cover)
        btn_category_humanities_cover.setOnClickListener(this)
        var btn_category_science_cover : Button = root.findViewById(R.id.btn_category_science_cover)
        btn_category_science_cover.setOnClickListener(this)
        var btn_category_detective_cover : Button = root.findViewById(R.id.btn_category_detective_cover)
        btn_category_detective_cover.setOnClickListener(this)

        return root
    }


    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity is signupUserInfoListener) {
            this.su_userinfo3 = activity as signupUserInfoListener?
        }
    }

    override fun onClick(v: View?) {

        var categorySelected:Boolean = false
        var coverSelected:Boolean = true

        when (v?.id) {
            R.id.btn_category_history -> {
                if (!categorySelected) {

                    btn_category_history.isSelected = true
                    btn_category_history_cover.visibility = View.VISIBLE
                    su_category = 1
                    categorySelected = true
                }

            }
            R.id.btn_category_psychology -> {
                if (!categorySelected) {
                    btn_category_psychology.isSelected = true
                    btn_category_psychology_cover.visibility = View.VISIBLE
                    su_category = 2
                    categorySelected = true
                }
            }
            R.id.btn_category_essay -> {
                if (!categorySelected) {

                    btn_category_essay.isSelected = true
                    btn_category_essay_cover.visibility = View.VISIBLE
                    su_category = 3
                    categorySelected = true

                }
            }
            R.id.btn_category_humanities -> {
                if (!categorySelected) {

                    btn_category_humanities.isSelected = true
                    btn_category_humanities_cover.visibility = View.VISIBLE
                    su_category = 4
                    categorySelected = true

                }
            }
            R.id.btn_category_science -> {
                if (!categorySelected) {

                    btn_category_science.isSelected = true
                    btn_category_science_cover.visibility = View.VISIBLE
                    su_category = 5
                    categorySelected = true

                }


            }
            R.id.btn_category_detective -> {
                if (!categorySelected) {

                    btn_category_detective.isSelected = true
                    btn_category_detective_cover.visibility = View.VISIBLE
                    su_category = 6
                    categorySelected = true

                }
            }

            //cover
            //---------------------------------------------------------------------------------------
            R.id.btn_category_history_cover -> {
                if (!coverSelected) {

                    btn_category_history.isSelected = false
                    btn_category_history_cover.visibility = View.INVISIBLE
                    coverSelected = false
                }
            }
            R.id.btn_category_psychology_cover -> {
                if (!coverSelected) {

                    btn_category_psychology.isSelected = false
                    btn_category_psychology_cover.visibility = View.INVISIBLE
                    coverSelected = false
                }
            }
            R.id.btn_category_essay_cover -> {
                if (!coverSelected) {
                    btn_category_essay.isSelected = false
                    btn_category_essay_cover.visibility = View.INVISIBLE
                    coverSelected = false
                }
            }
            R.id.btn_category_humanities_cover -> {
                if (!coverSelected) {
                    btn_category_humanities.isSelected = false
                    btn_category_humanities_cover.visibility = View.INVISIBLE
                    coverSelected = false
                }
            }
            R.id.btn_category_science_cover -> {
                if (!coverSelected) {
                    btn_category_science.isSelected = false
                    btn_category_science_cover.visibility = View.INVISIBLE
                    coverSelected = false
                }
            }
            R.id.btn_category_detective_cover -> {
                if (!coverSelected) {
                    btn_category_detective.isSelected = false
                    btn_category_detective_cover.visibility = View.INVISIBLE
                    coverSelected = false
                }
            }
            R.id.btn_customSet_category_end -> { //다음에 할게용
                su_name = su_userinfo3?.get_name()
                su_id = su_userinfo3?.get_id()
                su_pw = su_userinfo3?.get_pw()
                su_age = su_userinfo3?.get_age()
                su_gender = su_userinfo3?.get_gender()

                Log.e(" 정보", su_id.toString() + su_pw.toString() + su_age.toString() + su_gender.toString())
                ApolloQueryCreator.sendMutation( Queries.signUp, su_id!!, su_pw!!, su_name!!, su_age!!, su_gender!!, 0)
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
            }

            R.id.btn_customSet_category_finish -> { //확인
                su_name = su_userinfo3?.get_name()
                su_id = su_userinfo3?.get_id()
                su_pw = su_userinfo3?.get_pw()
                su_age = su_userinfo3?.get_age()
                su_gender = su_userinfo3?.get_gender()

                ApolloQueryCreator.sendMutation(Queries.signUp, su_id!!, su_pw!!, su_name!!, su_age!!, su_gender!!, su_category!!)
                Toast.makeText(activity?.applicationContext, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }



    
    
}