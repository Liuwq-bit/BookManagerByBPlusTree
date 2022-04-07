package com.example.bookmanagerbybplustree.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.android.synthetic.main.activity_main.*
import android.util.DisplayMetrics

import androidx.customview.widget.ViewDragHelper

import androidx.drawerlayout.widget.DrawerLayout

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.example.bookmanagerbybplustree.BookManager.Companion.user
import com.example.bookmanagerbybplustree.Fragment.BookListFragment
import com.example.bookmanagerbybplustree.Fragment.SearchFragment
import com.example.bookmanagerbybplustree.R
import kotlinx.android.synthetic.main.nav_header.view.*
import java.lang.Exception
import java.lang.reflect.Field

class MainActivity : AppCompatActivity() {

    val titleList = listOf("图书", "图书检索")
    val fragmentList = ArrayList<Fragment>()    // 存放各个页面的Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
        }


        // 设置侧滑菜单点击事件
        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bookAdd -> {
                    intent = Intent(this, AddBookActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawers()
                }
                R.id.navQuit -> {
                    intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.navMarks -> {
                    intent = Intent(this, BorrowActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawers()
                }
            }
            true
        }

        fragmentList.add(BookListFragment())    // 添加到viewPaper
        fragmentList.add(SearchFragment())
//        fragmentList.add(BookListFragment())

        modelViewPager.adapter = MyAdapter(supportFragmentManager, lifecycle)


        // 设置底部点击事件
        modelBottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_book -> {
                    modelViewPager.currentItem = 0
                }
                R.id.navigation_video -> {
                    modelViewPager.currentItem = 1
                }
//                R.id.navigation_music -> {
//                    modelViewPager.currentItem = 2
//                }
            }
            true
        }

        // 设置viewPager切换选择
        modelViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
//                Toast.makeText(BVMApplication.context, "$position", Toast.LENGTH_SHORT).show()
                when (position) {
                    0 -> modelBottomNavigation.selectedItemId = R.id.navigation_book
                    1 -> modelBottomNavigation.selectedItemId = R.id.navigation_video
//                    2 -> modelBottomNavigation.selectedItemId = R.id.navigation_music
                }
            }
        })

        setDrawerLeftEdgeSize(this, drawerLayout, 0.3f)
//
        if (navView.headerCount > 0) {  // 侧边栏用户名
            val header: View = navView.getHeaderView(0)
//            header.findViewById(R.id.mainUsername).setText(username)
//            header.mainNameText.text = BVMApplication.USER?.user_name ?: ""  // 设置用户名称
            header.mainNameText.text = user
        }

        modelViewPager.offscreenPageLimit = 2    // 预加载3个fragment


    }

    /**
     * viewPaper滑动事件监听
     */
    inner class MyAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun getItemCount() = titleList.size

        override fun createFragment(position: Int) = fragmentList[position]

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.START)
        }
        return true
    }

    /**
     *  设置DrawerLayout侧滑范围
     */
    private fun setDrawerLeftEdgeSize(
        activity: Activity?,
        drawerLayout: DrawerLayout?,
        displayWidthPercentage: Float) {
        if (activity == null || drawerLayout == null) return
        try {
            val leftDraggerField: Field = drawerLayout.javaClass.getDeclaredField("mLeftDragger")
            leftDraggerField.isAccessible = true
            val leftDragger = leftDraggerField.get(drawerLayout) as ViewDragHelper
            val edgeSizeField: Field = leftDragger.javaClass.getDeclaredField("mEdgeSize")
            edgeSizeField.isAccessible = true
            val edgeSize: Int = edgeSizeField.getInt(leftDragger)
            val dm = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(dm)
            edgeSizeField.setInt(
                leftDragger,
                edgeSize.coerceAtLeast((dm.widthPixels * displayWidthPercentage).toInt())
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}