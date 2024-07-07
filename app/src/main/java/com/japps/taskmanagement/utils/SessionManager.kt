package com.japps.taskmanagement.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefName = "SessionPref"
    private val keyUserId = "user_id"

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    // Save user session details
    fun setUserId(userId:String){
        editor.putString(keyUserId,userId)
        editor.commit()
    }

    // Retrieve session details
    fun getUserId(): String {
        return sharedPreferences.getString(keyUserId, "")!!
    }

    // Clear session details
    fun clearSession() {
        editor.clear().apply()
    }
}
