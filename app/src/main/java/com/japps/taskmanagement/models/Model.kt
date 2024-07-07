package com.japps.taskmanagement.models

import kotlinx.serialization.Serializable

data class TaskModel(val id:String, val title:String, val description:String,val status_id:String,val user_id:String)

@Serializable
data class UserModel(val id:String, val name:String, val email:String)

data class StatusModel(val id:String, val name:String,val sequence:Int)
