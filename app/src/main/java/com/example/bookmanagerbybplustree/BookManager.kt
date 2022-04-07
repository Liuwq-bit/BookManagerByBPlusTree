package com.example.bookmanagerbybplustree

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * 全局Context
 */
class BookManager : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var bookBpt: BookBPlusTree
        lateinit var borrowBpt: BorrowBPlusTree
        lateinit var userBpt: UserBPlusTree
        lateinit var user: String

        init {
            System.loadLibrary("bookmanagerbybplustree")
        }

    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        bookBpt = BookBPlusTree()
        borrowBpt = BorrowBPlusTree()
        borrowBpt.insert(Borrow("", "", "", "", "", "", ""))
        userBpt = UserBPlusTree()
        userBpt.insert(User("admin", "123"))
        user = "admin"
    }
}