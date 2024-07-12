package com.japps.taskmanagement.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.japps.taskmanagement.AppApplication
import com.japps.taskmanagement.databinding.ActivityLoginBinding
import com.japps.taskmanagement.repository.DBRepository
import com.japps.taskmanagement.viewmodels.UserViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var activityLoginBinding: ActivityLoginBinding
    private lateinit var signInLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(activityLoginBinding.root)

        activityLoginBinding.btSignin.setOnClickListener({
            userViewModel.getUser(activityLoginBinding?.etEmail?.text.toString())

        })


        userViewModel.user.observe(this,{ user ->

            if(user==null) {
                Log.d("Login","Createing user")
                userViewModel.createUser(
                    activityLoginBinding.etEmail.text.toString()
                )
            }else{
                Log.d("Login","User found")
            AppApplication.INSTANCE.getSession().setUserId(user.id)
            startActivity(Intent(this, DashboardActivity::class.java))
        }
            })
    }
}