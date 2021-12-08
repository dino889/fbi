package com.example.fbi

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_user_info.*
import kotlinx.android.synthetic.main.main.*
import com.bumptech.glide.Glide
import com.example.fbi.MainActivity.UserInfo.setUser
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    var name: String? = null
    var id: String? = null
    var age: Int? = null
    var phone: String?= null
    var gender: String? = null
    var mindBook: Int? = null
    var category: Int? = null

    object UserInfo {

        var userinfo : User ?= null

        fun setUser(id:String, name:String , phone:String, age:Int, gender:String, mindBook:Int, category:Int ){
           userinfo = User(id,name,phone,age,gender,mindBook,category)
        }

        fun getUser() : User? {
            return userinfo
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        getAccountInfoFromLoginActivity() //로그인 오류 해결 전 주석처리

        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.setDisplayShowTitleEnabled(false) //기존 appbar title

        val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // 각 fragemnt들을 최상위 대상으로 설정
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_gps,
                R.id.navigation_camera,
                R.id.navigation_bookshelf,
                R.id.navigation_mypage
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        settingButtonClickEvent() //버튼 클릭 이벤트 처리
        settingPermission() // 카메라 권한 체크 시작

    }

    //로그인 오류 해결 전 주석처리
    //유저의 정보를 loginActivity로부터 받아오는 함수
    private fun getAccountInfoFromLoginActivity() {
        if (intent.hasExtra("name")) {
            name = intent.getStringExtra("name")
        } else {
            Toast.makeText(this, "전달된 이름이 없습니다", Toast.LENGTH_SHORT).show()
        }
        if (intent.hasExtra("id")) {
            id = intent.getStringExtra("id")
        } else {
            Toast.makeText(this, "전달된 아이디 정보가 없습니다", Toast.LENGTH_SHORT).show()
        }
        if (intent.hasExtra("phone")) {
            phone = intent.getStringExtra("phone")
        } else {
            Toast.makeText(this, "전달된 연락처 정보가 없습니다", Toast.LENGTH_SHORT).show()
        }
        if (intent.hasExtra("age")) {
            age = intent.getIntExtra("age", 0)
        } else {
            Toast.makeText(this, "전달된 연령 정보가 없습니다", Toast.LENGTH_SHORT).show()
        }
        if (intent.hasExtra("gender")) {
            gender = intent.getStringExtra("gender")
        } else {
            Toast.makeText(this, "전달된 성별 정보가 없습니다", Toast.LENGTH_SHORT).show()
        }
        if (intent.hasExtra("mindBook")) {
            mindBook = intent.getIntExtra("mindBook", 0)
        } else {
            Toast.makeText(this, "전달된 탐색 도서 정보가 없습니다", Toast.LENGTH_SHORT).show()
        }
        if (intent.hasExtra("category")) {
            category = intent.getIntExtra("category", 0)
        } else {
            Toast.makeText(this, "전달된 카테고리 정보가 없습니다", Toast.LENGTH_SHORT).show()
        }

        setUser(id!!, name!!, phone!!, age!!, gender!!, mindBook!!, category!!)
    }

    //뒤로가기 처리
    override fun onBackPressed() {
        if (main_layout.isDrawerOpen(GravityCompat.START)) { //만약 drawer가 열려있을 때는 drawer닫기
            main_layout.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }

    //권한 설정을 하는 함수
    fun settingPermission() {
        var permis = object : PermissionListener {
            //권한 설정 확인
            override fun onPermissionGranted() {
                Toast.makeText(this@MainActivity, "권한 허가", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(this@MainActivity, "권한 거부", Toast.LENGTH_SHORT)
                    .show()
                ActivityCompat.finishAffinity(this@MainActivity) // 권한 거부시 앱 종료
            }
        }

        //카메라 권한 요청
        TedPermission.with(this)
            .setPermissionListener(permis)
            .setRationaleMessage("카메라 사진 권한 필요")
            .setDeniedMessage("카메라 권한 요청 거부")
            .setPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                //android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            )
            .check()
    }

    //버튼 클릭 이벤트
    fun settingButtonClickEvent() {

        //샌드위치 메뉴 누르면 드로어 레이아웃 열기
        btn_userinfo?.setOnClickListener {
            main_layout.openDrawer(Gravity.LEFT)
            //드로우어가 오픈된 후 drawer의 인자를 login Activity에서 받아온 데이터로 입력
            tv_drawer_nickname.text = name
            tv_drawer_email.text = id


            var user = UserInfo.getUser()
            mindBook = user?.mindBook
            //탐색 도서 데이터 DB에서 가져와서 변경
//            Log.e("마인드북 없나?" ,mindBook.toString())

            if(mindBook !=null) {
                ApolloQueryCreator.getQuery(Queries.getBookInfo, mindBook!!) { getSearchedbook ->
                    runOnUiThread {
                        if (getSearchedbook != null) {
                            val _jsonObject = JSONObject(getSearchedbook);
                            val item = _jsonObject.getString("data");
                            val jsonObject2 = JSONObject(item)
                            val item2 = jsonObject2.getString("getByBookNum")
                            val jsonObject = JSONObject(item2);

                            //val jsonObject = jsonArray.getJSONObject(0)
                            val num = jsonObject.getInt("book_num")
                            val name = jsonObject.getString("book_name")
                            val author = jsonObject.getString("book_author")
                            val publisher = jsonObject.getString("book_publisher")
                            val year = jsonObject.getString("book_year")
                            val picture = jsonObject.getString("book_picture")
                            val category = jsonObject.getString("book_category")

                            tv_drawer_book_title.text = name
                            tv_drawer_book_author.text = "저자명 : " + author
                            tv_drawer_book_publisher.text = "출판사 : " + publisher

                            if (picture != null) {
                                Glide.with(this)
                                    .load(picture)
                                    .into(iv_drawer_book_img)
                            }
                            else {
                                iv_drawer_book_img.setImageResource(R.drawable.img_book2); //도서 이미지 없을 경우 기본 이미지
                            }
                        }
                    }
                }
            }

            //드로어 열린 후 logout 버튼 클릭 처리
            btn_drawer_logout.setOnClickListener {
                Toast.makeText(this, "로그아웃", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        //검색 버튼 클릭시 SearchActivity로 전환
        btn_search?.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }
    }
}
