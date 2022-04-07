//package com.example.bookmanagerbybplustree.Fragment
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.bookmanagerbybplustree.Adapter.BookAdapter
//import com.example.bookmanagerbybplustree.Book
//import com.example.bookmanagerbybplustree.BookBPlusTree
//import com.example.bookmanagerbybplustree.BookManager.Companion.bookBpt
//import com.example.bookmanagerbybplustree.R
//import kotlinx.android.synthetic.main.book_list.*
//import kotlin.concurrent.thread
//
///**
// * 书籍信息展示列表
// */
//class BookSearchFragment: Fragment() {
//
//    private lateinit var adapter: BookAdapter
//    private lateinit var bookList: List<Book>
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.book_list, container, false)
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//
//
//        val layoutManager = LinearLayoutManager(activity)
////        val layoutManager = GridLayoutManager(activity, 2)
//        bookRecyclerView.layoutManager = layoutManager
//
//        initBookBpt()
//
////        bookList = bookBpt.findByBookName()
//        adapter = BookAdapter(this, bookList)
//        bookRecyclerView.adapter = adapter
//
//        bookListSwipeRefresh.setColorSchemeResources(androidx.appcompat.R.color.abc_btn_colored_borderless_text_material)
//        bookListSwipeRefresh.setOnRefreshListener {
//            reFreshBooks(adapter)
//        }
//
////        viewModel.bookLiveData.observe(viewLifecycleOwner, Observer { result -> // 动态查询数据
////            val books = result.getOrNull()
////            if (books != null) {
//////                bookRecyclerView.visibility = View.VISIBLE
////                viewModel.bookList.clear()
////                viewModel.bookList.addAll(books)
////                if (tag1) { // 仅执行一次
////                    adapter.notifyDataSetChanged()
////                    tag1 = false
////                }
////            } else {
////                Toast.makeText(activity, "未能查询到任何书籍", Toast.LENGTH_SHORT).show()
////                result.exceptionOrNull()?.printStackTrace()
////            }
////        })
//
////        viewModel.markLiveData.observe(viewLifecycleOwner, Observer { result ->
////            val marks = result.getOrNull()
////            if (marks != null) {
////                viewModel.markList.clear()
////                viewModel.markList.addAll(marks)
////                if (tag2) {
////                    adapter.notifyDataSetChanged()
////                    tag2 = false
////                }
////            }
////        })
////
////        viewModel.searchAllBooks()  // 显示所有书籍
////        viewModel.searchAllMarkById(BVMApplication.USER?.user_id.toString())
//
//    }
//
//
//    private fun reFreshBooks(adapter: BookAdapter) {
//        thread {
//            activity?.runOnUiThread {
////                viewModel.searchAllBooks()  // 显示所有书籍
////                viewModel.searchAllMarkById(BVMApplication.USER?.user_id.toString())
//                bookList = bookBpt.showAll()
//            }
//            Thread.sleep(2000)
//            activity?.runOnUiThread {
////                viewModel.bookList.shuffle()    // 洗牌
//                adapter.notifyDataSetChanged()
//                bookListSwipeRefresh.isRefreshing = false
//            }
//        }
//
//    }
//
//    fun initBookBpt() {
//    }
//
//
//}