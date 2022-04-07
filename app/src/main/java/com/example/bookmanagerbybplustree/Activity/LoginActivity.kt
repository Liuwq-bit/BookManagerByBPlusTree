package com.example.bookmanagerbybplustree.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.example.bookmanagerbybplustree.BookManager
import com.example.bookmanagerbybplustree.BookManager.Companion.context
import com.example.bookmanagerbybplustree.BookManager.Companion.user
import com.example.bookmanagerbybplustree.BookManager.Companion.userBpt
import com.example.bookmanagerbybplustree.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


//        viewModel.userLiveData.observe(this, Observer { result ->   // 动态查询数据
//            val users = result.getOrNull()
//            if (users != null) {
//                val pwd = userPwdText.editText?.text.toString()
//                for (i in 0..users.size-1) {
////                    Toast.makeText(BVMApplication.context, "输入密码：" + pwd + " 目标：" + users[i].user_pwd, Toast.LENGTH_SHORT).show()
//                    if (users[i].user_pwd == pwd) {
////                        Toast.makeText(BVMApplication.context, "找到", Toast.LENGTH_SHORT).show()
//                        BVMApplication.USER = users[i]
//                        user = users[i]
//                        flag = true
//                        break
//                    }
//                }
//                if (flag) {
//                    val intent = Intent(BVMApplication.context, MainActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                } else {
//                    Toast.makeText(BVMApplication.context, "用户名或密码错误，请重新输入", Toast.LENGTH_SHORT).show()
//                }
//            } else {
//                Toast.makeText(BVMApplication.context, "用户名或密码错误，请重新输入", Toast.LENGTH_SHORT).show()
//            }
//        })

//        ButterKnife.inject(this)
        loginBtn.setOnClickListener {
            if (!validate()) {
                Toast.makeText(context, "输入格式错误，请重新输入", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val name = userNameText.editText?.text.toString()
            val pwd = userPwdText.editText?.text.toString()

            val user = userBpt.findByUserName(name)
            if (user.userName == name && user.pwd == pwd) {
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
                Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show()

                BookManager.user = name
                finish()
            } else {
                Toast.makeText(context, "用户名或密码错误，请重新输入", Toast.LENGTH_SHORT).show()
            }


//            viewModel.searchUserByName(name)
//        Toast.makeText(BVMApplication.context, name, Toast.LENGTH_SHORT).show()
        }
        signupLink.setOnClickListener {
            // Start the Signup activity
            val intent = Intent(context, SignupActivity::class.java)
            startActivity(intent)
            // todo 去除已输入信息
        }
    }


    private fun validate(): Boolean {
        // todo 增加格式判断
        var valid = true
        val name = userNameText.editText?.text.toString()
        val pwd = userPwdText.editText?.text.toString()
        if (name.isEmpty()) {
            userNameText.error = "用户名不能为空"
            valid = false
        } else {
            userNameText.error = null
        }
        if (pwd.isEmpty()) {
            userPwdText.error = "密码不能为空"
            valid = false
        } else {
            userPwdText.error = null
        }
        return valid
    }
}