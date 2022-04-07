package com.example.bookmanagerbybplustree

import androidx.core.text.isDigitsOnly

data class Book(
    var available: Int,         // 可借数量
    var total: Int,             // 总数量
    var bookName: String,       // 书名
    var bookInfo: String,       // 图书信息
    var author: String,         // 作者
    var authorInfo: String,     // 作者信息
    var url: String             // 图书封面url
) {
    var id = bookName.hashCode()    // 书号
}


class BookBPlusTree {
    private val nativeBPlusTree: Long
    lateinit var set: MutableSet<Int>

    /**
     * 返回B+树中的所有元素
     */
    fun showAll(): MutableList<Book> {
        var bookList = mutableSetOf<Book>()

        var iterator = set.sorted().iterator()
        while (iterator.hasNext()) {
            bookList.add(findById(iterator.next()))
        }

        return bookList.toTypedArray().toMutableList()
    }

    /**
     * 向B+树中插入Book
     */
    fun insert(book: Book) {
        insertBook(nativeBPlusTree, book.id, book.id, book.available, book.total, book.bookName, book.bookInfo, book.author, book.authorInfo, book.url)
        set.add(book.id)
    }

    /**
     * 使用书名借阅
     */
    fun borrowByBookName(bookName: String) : Boolean {
        var book = findByBookName(bookName)
        return if (book.available != 0) {
            book.available -= 1
            insert(book)
            true
        } else {
            false
        }
    }


    /**
     * 根据索引查找Book
     */
    fun findById(index: Int): Book {
        val id = findId(nativeBPlusTree, index)
        val available = findAvailable(nativeBPlusTree, index)
        val total = findTotal(nativeBPlusTree, index)
        val bookName = findBookName(nativeBPlusTree, index)
        val bookInfo = findBookInfo(nativeBPlusTree, index)
        val author = findAuthor(nativeBPlusTree, index)
        val authorInfo = findAuthorInfo(nativeBPlusTree, index)
        val url = findUrl(nativeBPlusTree, index)

        return Book(available, total, bookName, bookInfo, author, authorInfo, url)
    }

    /**
     * 根据书名查找Book
     */
    fun findByBookName(bookName: String): Book {
        val index = bookName.hashCode()
        return findById(index)
    }

    /**
     * 根据书名查找图书列表
     */
    fun findListByBookName(bookName: String): MutableList<Book> {
        val bookList = showAll().filter {
            it.bookName == bookName
        }
        return bookList.toMutableList()
    }

    /**
     * 从B+树中删除Book
     */
    fun erase(book: Book) {
        eraseBook(nativeBPlusTree, book.id)
        set.remove(book.id)
    }

    private external fun createNativeObject(): Long
    private external fun insertBook(addr: Long, index: Int, id: Int, available: Int, total: Int, bookName: String, bookInfo: String, author: String, authorInfo: String, url: String)
    private external fun findId(addr: Long, index: Int): Int
    private external fun findAvailable(addr: Long, index: Int): Int
    private external fun findTotal(addr: Long, index: Int): Int
    private external fun findBookName(addr: Long, index: Int): String
    private external fun findBookInfo(addr: Long, index: Int): String
    private external fun findAuthor(addr: Long, index: Int): String
    private external fun findAuthorInfo(addr: Long, index: Int): String
    private external fun findUrl(addr: Long, index: Int): String
    private external fun eraseBook(addr: Long, index: Int)

    init {
        nativeBPlusTree = createNativeObject()
        set = mutableSetOf()
    }
}

data class Borrow(
    var userName: String,       // 用户名
    var returnTime: String,     // 归还时间
    var bookName: String,       // 书名
    var bookInfo: String,       // 图书信息
    var author: String,         // 作者
    var authorInfo: String,     // 作者信息
    var url: String             // 图书封面url
) {
    var id = (userName+bookName).hashCode()    // 书号
}

class BorrowBPlusTree {
    private val nativeBPlusTree: Long
    lateinit var set: MutableSet<Int>

    /**
     * 返回B+树中的所有元素
     */
    fun showAll(): MutableList<Borrow> {
        var borrowList = mutableSetOf<Borrow>()

        var iterator = set.sorted().iterator()
        while (iterator.hasNext()) {
            borrowList.add(findById(iterator.next()))
        }

        return borrowList.toTypedArray().toMutableList()
    }

    /**
     * 向B+树中插入借阅信息
     */
    fun insert(borrow: Borrow) {
        insertBorrow(nativeBPlusTree, borrow.id, borrow.id, borrow.userName, borrow.returnTime, borrow.bookName, borrow.bookInfo, borrow.author, borrow.authorInfo, borrow.url)
        set.add(borrow.id)
    }

    /**
     * 根据索引查找借阅信息
     */
    fun findById(index: Int): Borrow {
        val id = findId(nativeBPlusTree, index)
//        val available = findAvailable(nativeBPlusTree, index)
//        val total = findTotal(nativeBPlusTree, index)
        val userName = findUserName(nativeBPlusTree, index)
        val returnTime = findReturnTime(nativeBPlusTree, index)
        val bookName = findBookName(nativeBPlusTree, index)
        val bookInfo = findBookInfo(nativeBPlusTree, index)
        val author = findAuthor(nativeBPlusTree, index)
        val authorInfo = findAuthorInfo(nativeBPlusTree, index)
        val url = findUrl(nativeBPlusTree, index)

//        return Book(bookName, bookInfo, author, authorInfo, url)
        return Borrow(userName, returnTime, bookName, bookInfo, author, authorInfo, url)
    }

    /**
     * 根据用户查找借阅信息
     */
    fun findByUserName(userName: String): MutableList<Borrow> {
        val borrowList = showAll().filter {
            it.userName == userName
        }
        return borrowList.toMutableList()
    }

    /**
     * 使用书名借阅
     */
    fun findByUserAndBookName(userName: String, bookName: String) : Boolean {
        var borrow = findById((userName+bookName).hashCode())
        return !(borrow.userName == userName && borrow.bookName == bookName) // 可再次借阅
    }


    /**
     * 从B+树中删除Book
     */
    fun erase(userName: String, bookName: String) {
        var id = (userName + bookName).hashCode()
        eraseBorrow(nativeBPlusTree, id)
        set.remove(id)
    }

    /**
     * 从B+树中移除所有借阅信息
     */
    fun eraseAll(bookName: String) {
        val borrowList = showAll().filter {
            it.bookName == bookName
        }
        for (it in borrowList) {
            eraseBorrow(nativeBPlusTree, it.id)
            set.remove(it.id)
        }
    }

    private external fun createNativeObject(): Long
    private external fun insertBorrow(addr: Long, index: Int, id: Int, userName: String, returnTime: String, bookName: String, bookInfo: String, author: String, authorInfo: String, url: String)
    private external fun findId(addr: Long, index: Int): Int
//    private external fun findAvailable(addr: Long, index: Int): Int
//    private external fun findTotal(addr: Long, index: Int): Int
    private external fun findUserName(addr: Long, index: Int): String
    private external fun findReturnTime(addr: Long, index: Int): String
    private external fun findBookName(addr: Long, index: Int): String
    private external fun findBookInfo(addr: Long, index: Int): String
    private external fun findAuthor(addr: Long, index: Int): String
    private external fun findAuthorInfo(addr: Long, index: Int): String
    private external fun findUrl(addr: Long, index: Int): String
    private external fun eraseBorrow(addr: Long, index: Int)

    init {
        nativeBPlusTree = createNativeObject()
        set = mutableSetOf()
    }
}

/**
 * 存储用户信息
 */
data class User(
    var userName: String,
    var pwd: String
) {
    var id = userName.hashCode()
}

class UserBPlusTree {

    private val nativeBPlusTree: Long
    lateinit var set: MutableSet<Int>

    /**
     * 返回B+树中的所有元素
     */
    fun showAll(): MutableList<User> {
        var borrowList = mutableSetOf<User>()

        var iterator = set.sorted().iterator()
        while (iterator.hasNext()) {
            borrowList.add(findById(iterator.next()))
        }

        return borrowList.toTypedArray().toMutableList()
    }

    /**
     * 插入用户信息
     */
    fun insert(user: User) {
        insertUser(nativeBPlusTree, user.id, user.userName, user.pwd)
        set.add(user.id)
    }

    /**
     * 根据id查找用户
     */
    fun findById(index: Int): User {
//        val id = findId(nativeBPlusTree, index)
        val userName = findUserName(nativeBPlusTree, index)
        val pwd = findPwd(nativeBPlusTree, index)

        return User(userName, pwd)
    }

    /**
     * 根据用户名查找用户
     */
    fun findByUserName(userName: String): User {
        return findById(userName.hashCode())
    }


    private external fun createNativeObject(): Long
    private external fun insertUser(addr: Long, index: Int, userName: String, pwd: String)
    private external fun findId(addr: Long, index: Int): Int
    private external fun findUserName(addr: Long, index: Int): String
    private external fun findPwd(addr: Long, index: Int): String

    init {
        nativeBPlusTree = createNativeObject()
        set = mutableSetOf()
    }
}