package com.japps.taskmanagement

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.japps.taskmanagement.utils.SessionManager

class AppApplication : Application() {
    private lateinit var db: FirebaseFirestore
    private lateinit var session: SessionManager

    companion object {
        lateinit var INSTANCE: AppApplication

    }
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        session = SessionManager(this)
        db = Firebase.firestore

    }

    fun getSession():SessionManager{
        return this.session
    }
    fun getDatabase():FirebaseFirestore{
        return this.db
    }
}