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
import com.example.bookmanagerbybplustree.Activity.BorrowActivity
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

class BorrowAdapter(private val fragment: BorrowActivity, private val list: MutableList<Borrow>) :
    RecyclerView.Adapter<BorrowAdapter.ViewHolder>() {

//    val viewModel by lazy { ViewModelProviders.of(fragment).get(BookViewModel::class.java) }

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

//        // 设置窗体点击事件
//        holder.itemView.setOnClickListener {
//            val position = holder.adapterPosition
//            val book = bookList[position]
////            Log.d("TestBookId", book.book_id.toString())
//            val intent = Intent(context, BookInfoActivity::class.java).apply {
//                putExtra(BookInfoActivity.BOOK_NAME, book.bookName)
//                putExtra(BookInfoActivity.BOOK_INFO, book.bookInfo)
//                putExtra(BookInfoActivity.BOOK_PIC, book.url)
//                putExtra(BookInfoActivity.BOOK_AUTHOR, book.author)
//                putExtra(BookInfoActivity.BOOK_AUTHOR_INFO, book.authorInfo)
//                putExtra(BookInfoActivity.BOOK_ID, book.id.toString())
//                putExtra(BookInfoActivity.BOOK_AVAILABLE, book.available.toString())
////                putExtra(BookInfoActivity.BOOK_LABEL, book.label)
////                putExtra(BookInfoActivity.BOOK_PUBLISH_TIME, book.publish_time)
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK // 在新的task中开启activity
//            }
//            context.startActivity(intent)
//        }
        return holder
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = bookList[position]
        holder.bookTypeBtn0.text = "归还时间：" + book.returnTime
//        holder.bookTypeBtn1.text = book.returnTime
        holder.bookTypeBtn1.isVisible = false
        holder.borrowBtn.text = "归还"

        holder.bookTitle.text = book.bookName
        if (book.bookInfo.length > 60)
            holder.bookInfo.text = book.bookInfo.substring(0, 60)+ "..."
        else
            holder.bookInfo.text = book.bookInfo
        holder.bookAuthor.text = book.author

//        holder.deleteBtn.setOnClickListener {
//            borrowBpt.erase(user, book.bookName)
//            bookList.removeAt(position)
//            notifyDataSetChanged()
//
//            borrowBpt.eraseAll(book.bookName)
//        }
        holder.deleteBtn.isVisible = false

        Glide.with(context).load(book.url).into(holder.bookImage)   // 加载图片
//        holder.bookImage.setImageResource(Glide.with(context).load(book.url).into(holder.bookImage))

        // 设置标记按钮监听事件
//        holder.bookTypeBtn0.setOnClickListener {
////            val position = holder.adapterPosition
//            val book = bookList[position]
//            val date = Date()
//            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//            viewModel.insertBookMark(BookMark(userId, book.book_id ?: 0, 0, dateFormat.format(date)))
//            if (holder.bookTypeBtn0.text == "已想读")
//                holder.bookTypeBtn0.text = "想读"
//            else
//                holder.bookTypeBtn0.text = "已想读"
//            holder.bookTypeBtn1.text = "在读"
//            holder.bookTypeBtn2.text = "读过"
//        }
//        holder.bookTypeBtn1.setOnClickListener {
////            val position = holder.adapterPosition
//            val book = bookList[position]
//            val date = Date()
//            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//            viewModel.insertBookMark(BookMark(userId, book.book_id ?: 0, 1, dateFormat.format(date)))
//            holder.bookTypeBtn0.text = "想读"
//            if (holder.bookTypeBtn1.text == "已在读")
//                holder.bookTypeBtn1.text = "在读"
//            else
//                holder.bookTypeBtn1.text = "已在读"
//            holder.bookTypeBtn2.text = "读过"
//        }
//        holder.bookTypeBtn2.setOnClickListener {
////            val position = holder.adapterPosition
//            val book = bookList[position]
//            val date = Date()
//            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//            viewModel.insertBookMark(BookMark(userId, book.book_id ?: 0, 2, dateFormat.format(date)))
//            holder.bookTypeBtn0.text = "想读"
//            holder.bookTypeBtn1.text = "在读"
//            if (holder.bookTypeBtn2.text == "已读过")
//                holder.bookTypeBtn2.text = "读过"
//            else
//                holder.bookTypeBtn2.text = "已读过"
//        }

        holder.borrowBtn.setOnClickListener {
            borrowBpt.erase(user, bookList[position].bookName)
            bookList.removeAt(position)
            notifyDataSetChanged()

            var book = bookBpt.findByBookName(book.bookName)
            book.available += 1
            bookBpt.insert(book)
        }

    }

    override fun getItemCount() = bookList.size


    @JvmName("setBookList1")
    fun setBookList(list: MutableList<Borrow>) {
        bookList = list
    }
}