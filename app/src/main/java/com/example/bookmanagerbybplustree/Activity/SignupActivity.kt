package com.example.bookmanagerbybplustree.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.example.bookmanagerbybplustree.BookManager.Companion.context
import com.example.bookmanagerbybplustree.BookManager.Companion.userBpt
import com.example.bookmanagerbybplustree.R
import com.example.bookmanagerbybplustree.User
import kotlinx.android.synthetic.main.activity_signup.*
import java.text.SimpleDateFormat
import java.util.*

class SignupActivity : AppCompatActivity() {

//    val viewModel by lazy { ViewModelProviders.of(this).get(CommonViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        signupBtn.setOnClickListener {
            signup()
        }
        loginLink.setOnClickListener {
            finish()
        }
    }

    fun signup() {
//        Log.d(TAG, "Signup")
        if (!validate()) {
            Toast.makeText(context, "注册失败，请检查格式", Toast.LENGTH_SHORT).show()
            return
        }

        val name = signupNameText.editText?.text.toString()
        val pwd = signupPwdText1.editText?.text.toString()

        val temp = userBpt.findByUserName(name)
        if (temp.userName == name) {
            Toast.makeText(context, "该用户名已注册", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        userBpt.insert(User(name, pwd))

        Toast.makeText(context, "注册成功，请登录", Toast.LENGTH_SHORT).show()
        finish()
    }


    private fun validate(): Boolean {
        // todo 输入格式提示
        var valid = true
        val name = signupNameText.editText?.text.toString()
        val pwd1 = signupPwdText1.editText?.text.toString()
        val pwd2 = signupPwdText2.editText?.text.toString()
        if (name.isEmpty() || name.length < 3) {
            valid = false
        } else {
            signupNameText.error = null
        }
        if (pwd1.isEmpty()) {
            signupPwdText2.error = "密码格式错误"
            valid = false
        } else {
            signupPwdText2.error = null
        }
        if (pwd1 != pwd2) {
            signupPwdText1.error = "两次输入的密码不一致"
            valid = false
        } else {
            signupPwdText1.error = null
        }
        return valid
    }
}