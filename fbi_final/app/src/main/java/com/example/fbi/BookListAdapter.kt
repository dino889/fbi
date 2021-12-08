package com.example.fbi

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class BookListAdapter(
    val context: Context,
    val bookList: ArrayList<Book>
) :
    RecyclerView.Adapter<BookListAdapter.Holder>(), Filterable {

    var filteredList: ArrayList<Book>? = null //필터링된 아이템 리스트
    var index : Int = -1 //아이템 배경색 변경 위한 인덱스 저장
    var filter_option : Int = 0

    interface ItemClick
    {
        fun onClick(view: View, position: Int)
    }
    var itemClick: ItemClick? = null


    init{
        filteredList = bookList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_book_list_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return filteredList!!.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = filteredList!![position]
        holder?.bind(filteredList!![position], context)

        //아이템 클릭 이벤트
        holder.itemView.setOnClickListener(View.OnClickListener {
            index = position
            notifyDataSetChanged() //배경 색 변경된 데이터 리스트에 적용
            var user = MainActivity.UserInfo.getUser()
            var user_id = user?.id

            if (user_id != null) {
                ApolloQueryCreator.sendMutation(Queries.addLog, filteredList!![index].num, user_id!!, filteredList!![index].category)
            }
            itemClick?.onClick(it, position) //SearchActivity의 클릭이벤트 호출
        })
        var color :Int = ContextCompat.getColor(context, R.color.colorSelected)
        //선택된 아이템 처리
        if (index === position) {

            if(holder.itemView.isSelected) {
                holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"))
                holder.itemView.isSelected = false
            }
            else{
                holder.itemView.setBackgroundColor(color)
                holder.itemView.isSelected = true
            }

        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"))
            holder.itemView.isSelected = false
        }
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val book_img = itemView?.findViewById<ImageView>(R.id.iv_rv_book_img)
        val book_title = itemView?.findViewById<TextView>(R.id.tv_rv_book_title)
        val book_author = itemView?.findViewById<TextView>(R.id.tv_rv_book_author)
        val book_publisher = itemView?.findViewById<TextView>(R.id.tv_rv_book_publisher)
        val book_year = itemView?.findViewById<TextView>(R.id.tv_rv_book_year)

        fun bind (booklist: Book, context: Context) {
            if (booklist.img != "") {
                if (book_img != null) {
                    Glide.with(context)
                        .load(booklist.img)
                        .into(book_img)
                }
            } else {
//                book_img?.setImageResource(R.mipmap.ic_launcher) 이미지 없을 경우 기본 이미지 설정
            }
            book_title?.text = booklist.title
            book_author?.text = booklist.author
            book_publisher?.text = booklist.publisher
            book_year?.text = booklist.year
        }
    }

    override fun getFilter(): Filter? {
            return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString() //입력된 string
                if (charString.isEmpty()) {//입력된 문자 없을 경우
                    filteredList = bookList //필터링 되지 않은 리스트를 필터링된 리스트로 사용
                } else {
                    val filteringList = ArrayList<Book>()
                    //일치하는 케이스 검색해서 필터링 중인 리스트(filteringList)에 넣음
                    for (row in bookList) {
                        when (filter_option) {
                            0 -> { //전체 검색
                                if (row.title.toLowerCase().contains(charString.toLowerCase()) || row.author.toLowerCase().contains(charString.toLowerCase())
                                    || row.publisher.toLowerCase().contains(charString.toLowerCase())) {
//                                    Log.e("필터링",row.title)
                                    filteringList.add(row)
                                }
                            }
                            1 -> { //도서명으로 검색
                                if (row.title.toLowerCase().contains(charString.toLowerCase())) {
//                                    Log.e("필터링",row.title)
                                    filteringList.add(row)
                                }
                            }
                            2 -> { //저자명으로 검색
                                if (row.author.toLowerCase().contains(charString.toLowerCase())) {
//                                    Log.e("필터링",row.title)
                                    filteringList.add(row)
                                }
                            }
                            3 -> { //출판사명으로 검색
                                if (row.publisher.toLowerCase().contains(charString.toLowerCase())) {
//                                    Log.e("필터링",row.title)
                                    filteringList.add(row)
                                }
                            }
                        }
                    }
                    filteredList = filteringList //필터링 중인 리스트를 필터링 된 리스트로 사용
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList //필터링 된 리스트 넘겨줌
                return filterResults
            }
            //필터링 완료 후 recyclerview 업데이트해줌
            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredList = filterResults.values as ArrayList<Book>
                notifyDataSetChanged()
            }
        }
    }

    fun setOption(option : Int){
        filter_option = option
    }
}