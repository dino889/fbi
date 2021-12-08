package com.example.fbi.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fbi.Book
import com.example.fbi.R

class Best5ViewPagerAdapter(
    private val context: Context,
    private val booklist: ArrayList<Book>) :
    RecyclerView.Adapter<Best5ViewPagerAdapter.Holder>() {

    //클릭 이벤트
    interface ItemClick
    {
        fun onClick(view: View, position: Int)
    }
    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewpager_best5, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if(booklist.size != 0) {
            val index = position % booklist.size
            holder.bind(booklist[index], context)

            holder.itemView.setOnClickListener(View.OnClickListener {
                itemClick?.onClick(it, position) //SearchActivity의 클릭이벤트 호출
            })
        }
    }

    override fun getItemCount(): Int {
        return booklist.size
    }

    inner class Holder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val book_img = itemView?.findViewById<ImageView>(R.id.best5_bookImg)

        fun bind(booklist: Book, context: Context) {

            if (booklist.img != "") {
                if (book_img != null) {
                    Glide.with(context)
                        .load(booklist.img)
                        .into(book_img)
                }
            } else {
//                book_img?.setImageResource(R.mipmap.ic_launcher) //이미지 없을 시 기본 이미지 설정
            }
        }
    }

}
