package com.japps.taskmanagement.models

import com.google.firebase.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class TaskModel(var id:String, val title:String, val description:String, val status_id:String, val user_id:String,val createdAt:Timestamp)

@Serializable
data class UserModel(val id:String, val name:String, val email:String)

@Serializable
data class StatusModel(val id:String, val name:String,val sequence:Long)
