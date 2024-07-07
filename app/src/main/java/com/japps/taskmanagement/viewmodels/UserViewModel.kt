package com.japps.taskmanagement.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.japps.taskmanagement.AppApplication
import com.japps.taskmanagement.models.UserModel

class UserViewModel : ViewModel() {
    final val userTable = AppApplication.INSTANCE.getDatabase().collection("user")
    private val _user = MutableLiveData<UserModel>()
    val user: LiveData<UserModel> = _user
    init {
        //_user.value = null
    }
    fun updateUser(userModel:UserModel?){
        _user.value = userModel
    }


    fun getUser(email:String) {
        var data:UserModel? = null
        userTable.get()
            .addOnSuccessListener { result ->
                for(it in result) { if(it.id.equals(email)){
                    data = UserModel(it.id,it.data.get("name").toString(),it.data.get("email").toString())

                    break
                } }
                updateUser(data)
            }

    }

    fun createUser(email: String) {
        AppApplication.INSTANCE.getDatabase().collection("user").add(hashMapOf(
            "email" to email,
            "name" to email,
        ))
            .addOnSuccessListener { documentReference ->
                updateUser(UserModel(documentReference.id,"",""))
                Log.d("ADd user", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener{e ->
                Log.w("ADd user", "Error adding document", e)
            }
    }
}