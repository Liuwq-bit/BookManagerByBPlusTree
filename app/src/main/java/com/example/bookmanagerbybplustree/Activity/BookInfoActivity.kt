package com.example.bookmanagerbybplustree.Activity

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.bookmanagerbybplustree.Book
import com.example.bookmanagerbybplustree.BookManager
import com.example.bookmanagerbybplustree.BookManager.Companion.borrowBpt
import com.example.bookmanagerbybplustree.BookManager.Companion.context
import com.example.bookmanagerbybplustree.BookManager.Companion.user
import com.example.bookmanagerbybplustree.Borrow
import com.example.bookmanagerbybplustree.Fragment.BookListFragment
import com.example.bookmanagerbybplustree.R
import kotlinx.android.synthetic.main.activity_book_info.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

/**
 * 图书信息展示页面
 */
class BookInfoActivity : AppCompatActivity() {

//    val viewModel by lazy { ViewModelProviders.of(this).get(BookViewModel::class.java) }

    companion object {
        var book_id = "0"
        const val BOOK_ID = "bookId"
        const val BOOK_NAME = "bookName"
//        const val BOOK_LABEL = "bookLabel"
//        const val BOOK_PUBLISH_TIME = "bookPublishTime"
        const val BOOK_INFO = "bookInfo"
        const val BOOK_PIC = "bookPic"
        const val BOOK_AUTHOR = "bookAuthor"
        const val BOOK_AUTHOR_INFO = "bookAuthorInfo"
        const val BOOK_AVAILABLE = "bookAvailable"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_info)

        val bookId = intent.getStringExtra(BOOK_ID) ?: ""
        book_id = bookId
        val bookTitle = intent.getStringExtra(BOOK_NAME) ?: ""
        val bookInfo = intent.getStringExtra(BOOK_INFO) ?: ""
        val bookPic = intent.getStringExtra(BOOK_PIC) ?: ""
        val bookAuthor = intent.getStringExtra(BOOK_AUTHOR) ?: ""
        val bookAuthorInfo = intent.getStringExtra(BOOK_AUTHOR_INFO) ?: ""
        val bookAvailable = intent.getStringExtra(BOOK_AVAILABLE)?.toInt()
//        val bookLabel = intent.getStringExtra(BOOK_LABEL) ?: ""
//        val bookPublishTime = intent.getStringExtra(BOOK_PUBLISH_TIME) ?: ""
        setSupportActionBar(bookInfoToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        bookInfoCollapsingToolbar.title = bookTitle
        Glide.with(this).load(bookPic).into(bookInfoImageView)
        bookInfoContextText.text = bookInfo
        bookAuthorContextText.text = bookAuthor
        bookAuthorInfoContextText.text = bookAuthorInfo

        bookInfoImageView.setOnClickListener {
//            val tmp = bookInfoImageView.drawable as BitmapDrawable
//            val bitmap = tmp.bitmap
//            bigImageLoader(bitmap)
            load(bookPic) { bitmap ->
                bigImageLoader(bitmap)
            }
        }

        bookTotalRatingBar.rating = 4.5F

        bookBorrowBtn.setOnClickListener {
            if (!borrowBpt.findByUserAndBookName(user, bookTitle)) {
                Toast.makeText(context, "不可再次借阅", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (!BookManager.bookBpt.borrowByBookName(bookTitle)) {
                Toast.makeText(context, "本书库存为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                var calendar: Calendar = Calendar.getInstance()
                calendar.add(Calendar.MONTH, 1)
                val returnTime = SimpleDateFormat("yyyy-MM-dd").format(calendar.time)

                var borrow = Borrow(
                    BookManager.user,
                    returnTime,
                    bookTitle,
                    bookInfo,
                    bookAuthor,
                    bookAuthorInfo,
                    bookPic
                )
                BookManager.borrowBpt.insert(borrow)

                Toast.makeText(context, "借阅成功，请与1个月内归还", Toast.LENGTH_SHORT).show()

            }

        }

//        if (BVMApplication.USER?.user_id != 1L)
//            bookInfoChangeFab.isVisible = false

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

    private fun bigImageLoader(bitmap: Bitmap) {
        val dialog = Dialog(this)
        val image = ImageView(context)
        if (bitmap != null) {
            image.setImageBitmap(bitmap)
            dialog.setContentView(image)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.show()
            image.setOnClickListener { dialog.cancel() }
        }
    }

    /**
     * 加载网络地址 [url] 图片返回 Bitmap
     */
    private fun load(url: String, success: (Bitmap) -> Unit) {
        Glide.with(context) // context，可添加到参数中
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    // 成功返回 Bitmap
                    success.invoke(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }
}