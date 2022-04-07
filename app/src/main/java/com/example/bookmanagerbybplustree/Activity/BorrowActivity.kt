package com.example.bookmanagerbybplustree.Activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bookmanagerbybplustree.R
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookmanagerbybplustree.Adapter.BorrowAdapter
import com.example.bookmanagerbybplustree.BookManager.Companion.borrowBpt
import com.example.bookmanagerbybplustree.BookManager.Companion.user
import com.example.bookmanagerbybplustree.Borrow
import kotlinx.android.synthetic.main.activity_borrow.*
import kotlinx.android.synthetic.main.book_list.*
import kotlin.concurrent.thread

class BorrowActivity : AppCompatActivity() {

    private lateinit var adapter: BorrowAdapter
    private lateinit var borrowList: MutableList<Borrow>

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_borrow)
        setSupportActionBar(toolbar2)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        val layoutManager = LinearLayoutManager(this)
        borrowRecyclerView.layoutManager = layoutManager

        borrowList = borrowBpt.findByUserName(user)
        adapter = BorrowAdapter(this, borrowList)
        borrowRecyclerView.adapter = adapter

        borrowListSwipeRefresh.setColorSchemeColors(androidx.appcompat.R.color.abc_btn_colored_borderless_text_material)
        borrowListSwipeRefresh.setOnClickListener {
            reFreshBorrows(adapter)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
        }
        return true
    }

    private fun reFreshBorrows(adapter: BorrowAdapter) {
        thread {
            runOnUiThread {
//                viewModel.searchAllBooks()  // 显示所有书籍
//                viewModel.searchAllMarkById(BVMApplication.USER?.user_id.toString())
//                bookList = bookBpt.showAll()
                adapter.setBookList(borrowBpt.findByUserName(user))  // 更新Adapter中的list

            }
            Thread.sleep(2000)
            runOnUiThread {
//                viewModel.bookList.shuffle()    // 洗牌
                adapter.notifyDataSetChanged()
                bookListSwipeRefresh.isRefreshing = false
            }
        }
    }

}