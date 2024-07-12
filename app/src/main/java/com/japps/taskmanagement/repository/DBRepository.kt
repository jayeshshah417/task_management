package com.japps.taskmanagement.repository

import android.util.Log
import com.japps.taskmanagement.AppApplication
import com.japps.taskmanagement.models.TaskModel
import com.japps.taskmanagement.models.UserModel
import com.japps.taskmanagement.viewmodels.UserViewModel

class DBRepository {
    final val userTable = AppApplication.INSTANCE.getDatabase().collection("user")
    final val statusTable = AppApplication.INSTANCE.getDatabase().collection("status")
    final val taskTable = AppApplication.INSTANCE.getDatabase().collection("task")



}