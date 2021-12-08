package com.example.fbi.ui.home

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fbi.Book
import com.example.fbi.R


class UserBookListAdapter(
    val context: Context,
    val bookList: ArrayList<Book>
) :
    RecyclerView.Adapter<UserBookListAdapter.Holder>() {

    var index : Int = -1 //아이템 배경색 변경 위한 인덱스 저장

    interface ItemClick
    {
        fun onClick(view: View, position: Int)
    }
    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_book_list_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return bookList!!.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = bookList!![position]
        holder?.bind(bookList!![position], context)

        //아이템 클릭 이벤트
        holder.itemView.setOnClickListener(View.OnClickListener {
            index = position
            notifyDataSetChanged() //배경 색 변경된 데이터 리스트에 적용
            itemClick?.onClick(it, position) //SearchActivity의 클릭이벤트 호출
        })

        var color :Int = ContextCompat.getColor(context, R.color.colorSelected);
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
                book_img?.setImageResource(R.mipmap.ic_launcher)
            }
            book_title?.text = booklist.title
            book_author?.text = booklist.author
            book_publisher?.text = booklist.publisher
            book_year?.text = booklist.year
        }
    }
}