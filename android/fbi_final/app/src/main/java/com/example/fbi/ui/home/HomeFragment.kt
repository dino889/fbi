package com.example.fbi.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.fbi.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.viewpager_best5.view.*
import me.relex.circleindicator.CircleIndicator3
import org.json.JSONArray
import org.json.JSONObject


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    var booklist_best5 = arrayListOf<Book>()
    var booklist_recommend = arrayListOf<Book>()
    var booklist_user = arrayListOf<Book>()

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {

        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        onSettingBest5BookItem()
        onSettingRecommendBookItem()
        onSettingUserBookItem()

        return root
    }

    private fun onSettingBest5BookItem() {

//        Log.e("요청시작", "best5")
        ApolloQueryCreator.getQuery(Queries.mostSearchBook){ getbooklist ->
            activity?.runOnUiThread{
//                Log.e("전달받은 데이터", "best5")
                if(getbooklist != null) {
                    val jsonObject = JSONObject(getbooklist);
                    val item = jsonObject.getString("data");
                    val jsonObject2 = JSONObject(item)
                    val item2 = jsonObject2.getString("mostSearchbook")
                    val jsonArray = JSONArray(item2);

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val num = jsonObject.getInt("book_num")
                        val name = jsonObject.getString("book_name")
                        val author = jsonObject.getString("book_author")
                        val publisher = jsonObject.getString("book_publisher")
                        val year = jsonObject.getString("book_year")
                        val picture = jsonObject.getString("book_picture")
                        val category = jsonObject.getInt("book_category")
                        val newBook = Book(num, name, author, publisher, year, picture, category)
                        booklist_best5.add(newBook)
                    }

                    val adapter = Best5ViewPagerAdapter(activity!!, booklist_best5)
                    var viewPager2: ViewPager2? = activity!!.findViewById(R.id.viewPager2)
                    adapter.notifyDataSetChanged()
                    viewPager2?.orientation = ViewPager2.ORIENTATION_HORIZONTAL

                    viewPager2?.offscreenPageLimit = 2 //양쪽에 2개씩 총 5개의 페이지 생김

                    viewPager2?.adapter = adapter

                    val indicator: CircleIndicator3? = activity!!.findViewById(R.id.indicator)
                    indicator?.setViewPager(viewPager2)
                    // adapter.registerAdapterDataObserver(indicator.getAdapterDataObserver());
                    indicator?.createIndicators(5,2);

//                  viewPager2?.currentItem = 2 //처음에 중앙에 올 아이템 position 현재 얘때문에 간혹가다 뷰페이저 안나옴

//        val pageMargin = resources.getDimensionPixelOffset(R.dimen.pageMargin).toFloat()
//        val pageOffset = resources.getDimensionPixelOffset(R.dimen.offset).toFloat()

                    val width = resources.displayMetrics.widthPixels

                    val pageMargin = 20 //viewpager2 들어있는 LinearLayout margin
                    val img_width = 70
                    viewPager2?.setPageTransformer { page, position ->
                        //            val myOffset = position * -(2 * pageOffset + pageMargin)
                        val x = (width-(pageMargin*2))/4*3
                        val myOffset = position * - x
                        //Log.e("width값 $width x값", x.toString())

                        if (position <= -2) {
                            val scaleFactor = Math.max(0.7f, 1 - Math.abs(position - 0.14285715f))
                            page.translationX = myOffset // 기본 위치에서 n만큼 이동
                            //Log.e("위치-2", myOffset.toString())
                            page.scaleX = 1f
                            page.scaleY = 1f
                            page.best5_bookImg.alpha = scaleFactor -0.3f // alpha : 투명도
                            page.elevation = 6.0f; //elevation: 그림자 효과
                        } else if (position <= -1) {
                            val scaleFactor = Math.max(0.7f, 1 - Math.abs(position - 0.14285715f))
                            page.translationX = myOffset
                            page.scaleX = 1f
                            page.scaleY = 1f
                            page.best5_bookImg.alpha = 0.6f//fade in/out
                            page.elevation = 7.0f;
                            //Log.e("위치-1", myOffset.toString())
                        } else if (position <= 0) {
                            //Log.e("초기위치 $position $width", page.x.toString())
                            val scaleFactor = Math.max(0.9f, 1 - Math.abs(position - 0.14285715f))
                            page.translationX = myOffset
                            page.scaleX = 1.3f
                            page.scaleY = 1.3f
                            page.best5_bookImg.alpha = 1f
                            page.elevation = 2f;
                            // Log.e("위치0", myOffset.toString())
                        } else if (position <= 1) {
                            val scaleFactor = Math.max(0.7f, 1 - Math.abs(position - 0.14285715f))
                            page.translationX = myOffset
                            page.scaleX = 1f
                            page.scaleY = 1f
                            page.best5_bookImg.alpha = 0.6f
                            page.elevation = 7.0f;
                            // Log.e("위치 1", myOffset.toString())
                        } else {
                            val scaleFactor = Math.max(0.7f, 1 - Math.abs(position - 0.14285715f))
                            page.translationX = myOffset
                            page.scaleX = 1f
                            page.scaleY = 1f
                            page.best5_bookImg.alpha = 0.3f
                            page.elevation = 6.0f;
                            // Log.e("위치 2", page.x.toString())
                        }
                    }

                    //아이템 클릭 이벤트
                    adapter.itemClick = object: Best5ViewPagerAdapter.ItemClick {
                        override fun onClick(view: View, position: Int) {
//                            Toast.makeText(activity, booklist_best5[position-1].title, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun onSettingRecommendBookItem(){
//        Log.e("요청시작", "recommend")
        ApolloQueryCreator.getQuery(Queries.getBookList){getbooklist ->
            activity?.runOnUiThread {
//                Log.e("전달받은 데이터", "recommend")
                if (getbooklist != null) {
                    val jsonObject = JSONObject(getbooklist);
                    val item = jsonObject.getString("data");
                    val jsonObject2 = JSONObject(item)
                    val item2 = jsonObject2.getString("getBooklist")
                    val jsonArray = JSONArray(item2);

                    for (i in 0 until 3) {
                        val jsonObject = jsonArray.getJSONObject(i);
                        val num = jsonObject.getInt("book_num")
                        val name = jsonObject.getString("book_name")
                        val author = jsonObject.getString("book_author")
                        val publisher = jsonObject.getString("book_publisher")
                        val year = jsonObject.getString("book_year")
                        val picture = jsonObject.getString("book_picture")
                        val category = jsonObject.getInt("book_category")
                        val newBook = Book(num, name, author, publisher, year, picture, category)
                        booklist_recommend.add(newBook)
                    }

                    val recommendBookListAdapter = RecommendBookListAdapter(activity!!,booklist_recommend)
                    val recyclerview = activity!!.findViewById(R.id.rv_home_recommend_book_list) as RecyclerView

                    recyclerview.layoutManager = LinearLayoutManager(activity)
                    recyclerview.adapter = recommendBookListAdapter

                    //아이템 클릭 이벤트
                    recommendBookListAdapter.itemClick = object: RecommendBookListAdapter.ItemClick {
                        override fun onClick(view: View, position: Int) {
//                            Toast.makeText(activity, position.toString(), Toast.LENGTH_SHORT).show()
//                if(view.isSelected)
//                    btn_store_book.visibility = View.GONE
//                else
//                    btn_store_book.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun onSettingUserBookItem(){
//        Log.e("요청시작", "user")

        ApolloQueryCreator.getQuery(Queries.getBookList){ getbooklist ->
            activity?.runOnUiThread {
                if (getbooklist != null) {
//                    Log.e("전달받은 데이터", "userbook")
                    val jsonObject = JSONObject(getbooklist);
                    val item = jsonObject.getString("data");
                    val jsonObject2 = JSONObject(item)
                    val item2 = jsonObject2.getString("getBooklist")
                    val jsonArray = JSONArray(item2);

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i);
                        val num = jsonObject.getInt("book_num")
                        val name = jsonObject.getString("book_name")
                        val author = jsonObject.getString("book_author")
                        val publisher = jsonObject.getString("book_publisher")
                        val year = jsonObject.getString("book_year")
                        val picture = jsonObject.getString("book_picture")
                        val category = jsonObject.getInt("book_category")
                        val newBook = Book(num, name, author, publisher, year, picture, category)
                        booklist_user.add(newBook)
                    }

                    var userBookListAdapter = UserBookListAdapter(activity!!, booklist_user)
                    var recyclerView = activity!!.findViewById<RecyclerView>(R.id.rv_home_user_book_list)
                    recyclerView.layoutManager = LinearLayoutManager(activity!!)
                    recyclerView.adapter = userBookListAdapter

                    //아이템 클릭 이벤트
                    userBookListAdapter.itemClick = object: UserBookListAdapter.ItemClick {
                        override fun onClick(view: View, position: Int) {
//                            Toast.makeText(activity, position.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var name : String?= (MainActivity.UserInfo.getUser())?.name
        tv_home_recommend_book?.text =  name+ "님 맞춤 추천 도서"
        tv_home_user_book?.text = name + "님의 관심도서"
    }
}
