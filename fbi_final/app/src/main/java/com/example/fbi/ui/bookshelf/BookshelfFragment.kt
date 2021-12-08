package com.example.fbi.ui.bookshelf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.fbi.*
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_bookshelf.*
import org.json.JSONArray
import org.json.JSONObject


class BookshelfFragment : Fragment() {

    private lateinit var bookshelfViewModel: BookshelfViewModel
    private var booklist: ArrayList<Book> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bookshelfViewModel =
            ViewModelProviders.of(this).get(BookshelfViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_bookshelf, container, false)
//        val textView: TextView = root.findViewById(R.id.text_bookshelf)
        bookshelfViewModel.text.observe(viewLifecycleOwner, Observer {
            //            textView.text = it
        })

        onSettingBookItem(root)

        return root
    }
    private fun onSettingBookItem(root: View?) {

        var user = MainActivity.UserInfo.getUser()
        var user_id = user?.id

        if (user_id != null) {
            ApolloQueryCreator.getQuery(Queries.searchedBook, user_id) { getbooklist ->
                activity?.runOnUiThread {
                    Logger.d("내서재" + getbooklist)

                    if (getbooklist != null) {
                        val jsonObject = JSONObject(getbooklist);
                        val item = jsonObject.getString("data");
                        val jsonObject2 = JSONObject(item)
                        val item2 = jsonObject2.getString("searchedBook")
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
                            booklist.add(newBook)
                        }
                    }
                    val gridView = root!!.findViewById(R.id.gridview_bookshelf) as GridView
                    val bookshelfAdapter = BookshelfAdapter(activity!!, booklist)
                    gridView.adapter = bookshelfAdapter

                    gridView.onItemClickListener =
                        OnItemClickListener { parent, view, position, id ->
                            val book: Book = booklist[position]
                            var dialog = BookshelfDialogFragment(book);

                            dialog.show(this.childFragmentManager!!, "tag");
                            bookshelfAdapter.notifyDataSetChanged()
                        }
                }
            }
        }
    }
}
