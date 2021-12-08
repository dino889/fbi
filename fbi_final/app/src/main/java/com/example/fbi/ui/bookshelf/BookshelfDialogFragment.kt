package com.example.fbi.ui.bookshelf

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.fbi.Book
import com.example.fbi.R


//사진에서 도서 찾은 후 찾는 도서가 맞는지 확인하는 팝업
class BookshelfDialogFragment(book: Book) : DialogFragment() {

    private var myListener: MyDialogListener? = null
    private var book : Book = book

    interface MyDialogListener {
        fun myCallback(cityName: String?)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.dialog_check_book, container)

        var msg : TextView = view.findViewById(R.id.tv_title_msg)
        var cancel : Button = view.findViewById(R.id.btn_check_book_cancel)
        var ok : Button = view.findViewById(R.id.btn_check_book_ok)
        var book_img : ImageView = view.findViewById(R.id.iv_check_book)
        var book_title : TextView = view.findViewById(R.id.tv_check_book_popup_title)
        var book_author : TextView = view.findViewById(R.id.tv_check_book_popup_author)
        var book_publisher : TextView = view.findViewById(R.id.tv_check_book_popup_publisher)
        var book_year : TextView = view.findViewById(R.id.tv_check_book_popup_year)

        msg.text = "도서 정보"
        cancel.text = "삭제"
        ok.text = "닫기"
        book_title.setText("도서명 : " + book.title)
        book_author.setText("저자명 : " + book.author)
        book_publisher.setText("출판사명 : " + book.publisher)
        book_year.setText("출판연도 : " + book.year)

        if (book.img != "") {
            if (book_img != null) {
                Glide.with(this)
                    .load(book.img)
                    .into(book_img)
            }
        } else {
//                book_img?.setImageResource(R.mipmap.ic_launcher) //이미지 없을 시 기본 이미지 설정
        }

        cancel?.setOnClickListener(View.OnClickListener {
                Toast.makeText(activity, "삭제", Toast.LENGTH_SHORT)
                    .show()
                dialog!!.dismiss()
        })

        ok?.setOnClickListener(View.OnClickListener {
            Toast.makeText(activity, "닫기", Toast.LENGTH_SHORT)
                    .show()
            dialog!!.dismiss()
        })
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myListener = try {
            targetFragment as MyDialogListener?
        } catch (e: ClassCastException) {
            throw ClassCastException()
        }
    }
}