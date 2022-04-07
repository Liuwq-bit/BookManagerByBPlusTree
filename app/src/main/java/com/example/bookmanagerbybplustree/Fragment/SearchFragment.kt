package com.example.bookmanagerbybplustree.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookmanagerbybplustree.Adapter.BookAdapter
import com.example.bookmanagerbybplustree.Book
import com.example.bookmanagerbybplustree.BookManager.Companion.bookBpt
import com.example.bookmanagerbybplustree.R
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    private lateinit var adapter: BookAdapter
    private lateinit var bookList: MutableList<Book>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val layoutManager = LinearLayoutManager(activity)
        searchRecyclerView.layoutManager = layoutManager

        bookList = ArrayList()
//        bookList.add(Book(1, 1, "", "", "", "", ""))
        adapter = BookAdapter(this, bookList)
        searchRecyclerView.adapter = adapter

        searchBookEdit.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.isNotEmpty()) {
                var temp = bookBpt.findListByBookName(content)
//                adapter.bookList = bookBpt.findListByBookName(content)
                if (temp.size > 0 && temp[0].bookName == content) {
                    adapter.bookList = temp
                    adapter.notifyDataSetChanged()
                    searchRecyclerView.visibility = View.VISIBLE
                    bgImageView.visibility = View.GONE
                } else {
                    searchRecyclerView.visibility = View.GONE
                    bgImageView.visibility = View.VISIBLE
                    Toast.makeText(context, "未能查询到相关书目", Toast.LENGTH_SHORT).show()
                }
            } else {
                searchRecyclerView.visibility = View.GONE
                bgImageView.visibility = View.VISIBLE
            }
        }
    }
}