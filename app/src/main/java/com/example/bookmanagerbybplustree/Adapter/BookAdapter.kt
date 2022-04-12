package com.example.bookmanagerbybplustree.Adapter

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookmanagerbybplustree.Activity.BookInfoActivity
import com.example.bookmanagerbybplustree.Book
import com.example.bookmanagerbybplustree.BookManager.Companion.bookBpt
import com.example.bookmanagerbybplustree.BookManager.Companion.borrowBpt
import com.example.bookmanagerbybplustree.BookManager.Companion.context
import com.example.bookmanagerbybplustree.BookManager.Companion.user
import com.example.bookmanagerbybplustree.Borrow
import com.example.bookmanagerbybplustree.R
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.*

class BookAdapter(private val fragment: Fragment, private val list: MutableList<Book>) :
    RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    var bookList = list

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bookTitle: TextView = view.findViewById(R.id.itemTitle)
        val bookInfo: TextView = view.findViewById(R.id.itemInfo)
        val bookAuthor: TextView = view.findViewById(R.id.authorText)
        val bookImage: ImageView = view.findViewById(R.id.itemImage)
        val deleteBtn: MaterialButton = view.findViewById(R.id.deleteItemBtn)
        val bookTypeBtn0: MaterialButton = view.findViewById(R.id.typeBtn0)
        val bookTypeBtn1: MaterialButton = view.findViewById(R.id.typeBtn1)
        val borrowBtn: MaterialButton = view.findViewById(R.id.borrowBtn)
    }



    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item,
            parent, false)
        val holder = ViewHolder(view)

        // 设置窗体点击事件
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            val book = bookList[position]
//            Log.d("TestBookId", book.book_id.toString())
            val intent = Intent(context, BookInfoActivity::class.java).apply {
                putExtra(BookInfoActivity.BOOK_NAME, book.bookName)
                putExtra(BookInfoActivity.BOOK_INFO, book.bookInfo)
                putExtra(BookInfoActivity.BOOK_PIC, book.url)
                putExtra(BookInfoActivity.BOOK_AUTHOR, book.author)
                putExtra(BookInfoActivity.BOOK_AUTHOR_INFO, book.authorInfo)
                putExtra(BookInfoActivity.BOOK_ID, book.id.toString())
                putExtra(BookInfoActivity.BOOK_AVAILABLE, book.available.toString())
//                putExtra(BookInfoActivity.BOOK_LABEL, book.label)
//                putExtra(BookInfoActivity.BOOK_PUBLISH_TIME, book.publish_time)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK // 在新的task中开启activity
            }
            context.startActivity(intent)
        }
        return holder
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = bookList[position]
        holder.bookTypeBtn0.text = "可借：" + book.available
        holder.bookTypeBtn1.text = "总库存：" + book.total
        holder.borrowBtn.text = "借阅"

        holder.bookTitle.text = book.bookName
        if (book.bookInfo.length > 60)
            holder.bookInfo.text = book.bookInfo.substring(0, 60)+ "..."
        else
            holder.bookInfo.text = book.bookInfo
        holder.bookAuthor.text = book.author

        holder.deleteBtn.setOnClickListener {
            bookBpt.erase(book)
            bookList.removeAt(position)
            notifyDataSetChanged()

            borrowBpt.eraseAll(book.bookName)
        }

        if (user != "admin")
            holder.deleteBtn.isVisible = false
        else
            holder.borrowBtn.isVisible = false

        Glide.with(context).load(book.url).into(holder.bookImage)   // 加载图片

        holder.borrowBtn.setOnClickListener {

            if (!borrowBpt.findByUserAndBookName(user, bookList[position].bookName)) {
                Toast.makeText(context, "不可再次借阅", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {

                if (bookBpt.borrowByBookName(bookList[position].bookName)) {
                    var temp = bookList[position]
                    temp.available -= 1
                    bookList[position] = temp
//                    bookList[position].available -= 1
                    notifyDataSetChanged()
                    bookBpt.insert(temp)
                } else {
                    Toast.makeText(context, "本书库存为空", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val book = bookList[position]   // todo 不可重复借阅

                var calendar: Calendar = Calendar.getInstance()
                calendar.add(Calendar.MONTH, 1)
                val returnTime = SimpleDateFormat("yyyy-MM-dd").format(calendar.time)

                var borrow = Borrow(
                    user,
                    returnTime,
                    book.bookName,
                    book.bookInfo,
                    book.author,
                    book.authorInfo,
                    book.url
                )

                borrowBpt.insert(borrow)
                Toast.makeText(context, "借阅成功，请与1个月内归还", Toast.LENGTH_SHORT).show()

            }

        }

    }

    override fun getItemCount() = bookList.size


    @JvmName("setBookList1")
    fun setBookList(list: MutableList<Book>) {
        bookList = list
    }
}