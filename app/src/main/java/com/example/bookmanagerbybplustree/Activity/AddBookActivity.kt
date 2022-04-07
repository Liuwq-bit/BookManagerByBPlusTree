package com.example.bookmanagerbybplustree.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bookmanagerbybplustree.Book
import com.example.bookmanagerbybplustree.BookManager.Companion.bookBpt
import com.example.bookmanagerbybplustree.BookManager.Companion.context
import com.example.bookmanagerbybplustree.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_add_book.*
import java.lang.NumberFormatException

/**
 * 采编入库界面
 */
class AddBookActivity : AppCompatActivity() {

//    val titleList = listOf("图书", "影视", "音乐")
//    val fragmentList = ArrayList<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        setSupportActionBar(bookInputToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        bookInputToolbar.title = "采编入库"


        bookCommitFab.setOnClickListener {
            val bookName = bookNameText.editText?.text.toString()
            val bookInfo = bookInfoText.editText?.text.toString()
            val bookAuthor = bookAuthorText.editText?.text.toString()
            val bookAuthorInfo = bookAuthorInfoText.editText?.text.toString()
            val bookUrl = bookUrlText.editText?.text.toString()
            var bookTotal = 1
            try {
                bookTotal = bookTotalText.editText?.text.toString().toInt()
            } catch (e: NumberFormatException) {
              Toast.makeText(context, "采购量请输入数字", Toast.LENGTH_SHORT).show()
              return@setOnClickListener
            }

            val book = Book(bookTotal, bookTotal, bookName, bookInfo, bookAuthor, bookAuthorInfo, bookUrl)

            bookBpt.insert(book)
            Toast.makeText(context, "录入成功", Toast.LENGTH_SHORT).show()
            finish()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}

