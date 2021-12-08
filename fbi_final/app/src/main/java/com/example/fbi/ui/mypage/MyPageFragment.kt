package com.example.fbi.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.fbi.LoginActivity
import com.example.fbi.MainActivity
import com.example.fbi.R
import com.example.fbi.User
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_mypage.*

class MyPageFragment : Fragment(), View.OnClickListener {

    private lateinit var mypageViewModel: MyPageViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mypageViewModel =
                ViewModelProviders.of(this).get(MyPageViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_mypage, container, false)
        val textView: TextView = root.findViewById(R.id.text_mypage)
        mypageViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })


        //버튼 클릭 처리
        val acco_chPW : TextView = root.findViewById(R.id.acco_chPW)
        acco_chPW.setOnClickListener(this)
        val acco_set : TextView = root.findViewById(R.id.acco_set)
        acco_set.setOnClickListener(this)
        val acco_logout : TextView = root.findViewById(R.id.acco_logout)
        acco_logout.setOnClickListener(this)
        val guide_help : TextView = root.findViewById(R.id.guide_help)
        guide_help.setOnClickListener(this)
        val guide_faq : TextView = root.findViewById(R.id.guide_faq)
        guide_faq.setOnClickListener(this)
        val guide_ques : TextView = root.findViewById(R.id.guide_ques)
        guide_ques.setOnClickListener(this)
        val info_tou : TextView = root.findViewById(R.id.info_tou)
        info_tou.setOnClickListener(this)
        val info_privacy : TextView = root.findViewById(R.id.info_privacy)
        info_privacy.setOnClickListener(this)
        val sece_confirm : TextView = root.findViewById(R.id.sece_confirm)
        sece_confirm.setOnClickListener(this)
        return root
    }


    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.acco_chPW -> {
                Toast.makeText(activity?.applicationContext, "비밀번호 변경", Toast.LENGTH_SHORT).show()
            }
            R.id.acco_set -> {
                Toast.makeText(activity?.applicationContext, "맞춤 설정", Toast.LENGTH_SHORT).show()
            }
            R.id.acco_logout -> {
                val intent = Intent(activity!!, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.guide_help -> {
                Toast.makeText(activity?.applicationContext, "도움말", Toast.LENGTH_SHORT).show()
            }
            R.id.guide_faq -> {
                Toast.makeText(activity?.applicationContext, "자주묻는질문", Toast.LENGTH_SHORT).show()
            }
            R.id.guide_ques -> {
                Toast.makeText(activity?.applicationContext, "문의하기", Toast.LENGTH_SHORT).show()
            }
            R.id.info_tou -> {
                Toast.makeText(activity?.applicationContext, "FBI 이용약관", Toast.LENGTH_SHORT).show()
            }
            R.id.info_privacy -> {
                Toast.makeText(activity?.applicationContext, "개인정보 활용원칙", Toast.LENGTH_SHORT).show()
            }
            R.id.sece_confirm -> {
                Toast.makeText(activity?.applicationContext, "탈퇴하기", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var user : User? =  MainActivity.UserInfo.getUser()
        mp_username?.text = user?.name
        mp_useremail?.text = user?.id
    }
}
