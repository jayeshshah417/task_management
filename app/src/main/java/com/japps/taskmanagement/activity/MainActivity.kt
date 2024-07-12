package com.japps.taskmanagement.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.japps.taskmanagement.AppApplication
import com.japps.taskmanagement.R
import com.japps.taskmanagement.repository.DBRepository
import com.japps.taskmanagement.viewmodels.UserViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        val userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        Handler(Looper.getMainLooper()).postDelayed({
            val user_id = AppApplication.INSTANCE.getSession().getUserId()
            val user = userViewModel.getUserFromId()

        },5000)

        userViewModel.user.observe(this,{ user ->
            if(user==null){
                startActivity(Intent(this, LoginActivity::class.java))
            }else {
                startActivity(Intent(this, DashboardActivity::class.java))
            }
        })
    }
}