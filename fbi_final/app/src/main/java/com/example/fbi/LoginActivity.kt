package com.example.fbi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.api.toJson
import com.apollographql.apollo.exception.ApolloException
import com.google.android.datatransport.runtime.logging.Logging.d
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.gson.Gson
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.view.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.system.exitProcess

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    //    val apolloQueryCreator = ApolloQueryCreator()
    var userinfo : User? = null

    private var auth : FirebaseAuth? = null //파이어베이스 인증 객체
    private var googleSigninClient : GoogleSignInClient? = null //구글 로그인 클라이언트 객체
    private val RC_SIGN_IN = 9001 // 구글로그인 결과 코드

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        Logger.d("test")
        setContentView(R.layout.activity_login)
        Logger.addLogAdapter(AndroidLogAdapter())
        //[START config_signin]
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        //[END config_signin]
        googleSigninClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()

        //구글 로그인 버튼 텍스트 변경
        val textView = btn_login_google.getChildAt(0) as TextView
        textView.text ="구글 계정으로 로그인 "

        //클릭 이벤트 리스너 설정
        btn_sign_up.setOnClickListener(this)
//        btn_find_account.setOnClickListener(this)
        btn_login.setOnClickListener(this)
        btn_login_google.setOnClickListener(this)
    }
    //구글 로그인 인증을 요청했을 때 결과값을 돌려받는 곳
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)  //account 라는 데이터는 구글 로그인 정보를 담고 있음(닉네임, 프로필사진 url, 이메일주소 등)
                if (account != null) {
                    firebaseAuthWithGoogle(account)
                }
            }
            catch (e: ApiException){
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
    //파이어베이스로부터 인증을 받아와 유저의 정보를 다음 액티비티로 전달하는 함수
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth!!.signInWithCredential(credential)
            .addOnCompleteListener(this){
                if(it.isSuccessful){    //인증결과가 성공적인지
                    val user = auth?.currentUser
                    Toast.makeText(this, acct.email, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)

                    intent.putExtra("nickName", acct.displayName)
                    intent.putExtra("email", acct.email)
                    intent.putExtra("photoURL", acct.photoUrl.toString())

                    startActivity(intent)
                }
                else{       //로그인 실패 시
                    Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                }

            }
    }

    //클릭 이벤트 처리
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_sign_up -> {
                Toast.makeText(this, "회원 가입", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            }
//            R.id.btn_find_account -> {
//                Toast.makeText(applicationContext, "계정 찾기", Toast.LENGTH_SHORT).show()
//                //var getbooklist = ApolloQueryCreator.getQuery(Queries.getBookList)
//                //Logger.d(getbooklist)
//            }
            R.id.btn_login -> {
                if (et_login_user_name.text.toString() != "" && et_login_password.text.toString() != "") {
                    ApolloQueryCreator.getQuery(
                        Queries.loginUser,
                        et_login_user_name.text.toString(),
                        et_login_password.text.toString()
                    ) { user ->
//                        Logger.d(user)
                        if (user != null) {
//                            Log.e("전달받은 데이터", "userbook")
                            val _jsonObject = JSONObject(user);
                            val item = _jsonObject.getString("data");
                            val jsonObject2 = JSONObject(item)
                            val item2 = jsonObject2.getString("loginUser")
                            val jsonArray = JSONArray(item2)
                            if (jsonArray.length() != 0) {
                                val jsonObject = jsonArray.getJSONObject(0);
                                val id = jsonObject.getString("user_ID")
                                val name = jsonObject.getString("user_name")
                                val phone = jsonObject.getString("user_phone")
                                val age = jsonObject.getInt("user_age")
                                val gender = jsonObject.getString("user_gender")
                                val mindBook = jsonObject.getInt("user_mindbook")
                                val category = jsonObject.getInt("user_category")

                                if (id != null) {
                                    val intent = Intent(this, MainActivity::class.java)
                                    intent.putExtra("name", name)
                                    intent.putExtra("id", id)
                                    intent.putExtra("phone", phone)
                                    intent.putExtra("age", age)
                                    intent.putExtra("gender", gender)
                                    intent.putExtra("mindBook", mindBook)
                                    intent.putExtra("category", category)
                                    startActivity(intent)
                                } else
                                    Toast.makeText(applicationContext, "등록된 회원 정보가 없거나 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                            } else
                                runOnUiThread {
                                    Toast.makeText(applicationContext, "등록된 회원 정보가 없거나 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                }
                else
                    Toast.makeText(applicationContext, "이메일 혹은 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            R.id.btn_login_google -> {
                //로그인 구현 완성될때까지 주석 처리
                //val signInIntent = googleSigninClient?.getSignInIntent()
                //startActivityForResult(signInIntent, RC_SIGN_IN)
                val intent = Intent(this, MainActivity::class.java)

                startActivity(intent)
            }
        }
    }
}
