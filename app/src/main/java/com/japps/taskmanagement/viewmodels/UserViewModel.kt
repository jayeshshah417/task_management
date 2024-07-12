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
    private val _userList = MutableLiveData<List<UserModel>>()
    val userList: LiveData<List<UserModel>> = _userList
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
                for(it in result.documents) { if(it.data?.get("email")!!.equals(email)){
                    data = UserModel(it.id,it.data?.get("name").toString(),it.data?.get("email").toString())

                    break
                } }
                updateUser(data)
            }

    }

    fun getUserFromId() {
        var data:UserModel? = null
        userTable.get()
            .addOnSuccessListener { result ->
                for(it in result.documents) { if(it.id.equals(AppApplication.INSTANCE.getSession().getUserId())){
                    data = UserModel(it.id,it.data?.get("name").toString(),it.data?.get("email").toString())

                    break
                } }
                updateUser(data)
            }

    }

    fun getUserList() {
        var data:ArrayList<UserModel> = ArrayList()
        userTable.get()
            .addOnSuccessListener { result ->
                for(it in result.documents) {
                    data.add(UserModel(it.id,it.data?.get("name").toString(),it.data?.get("email").toString()))
                 }
                _userList.value = data
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