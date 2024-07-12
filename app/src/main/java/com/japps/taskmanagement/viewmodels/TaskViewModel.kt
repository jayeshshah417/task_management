package com.japps.taskmanagement.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.japps.taskmanagement.AppApplication
import com.japps.taskmanagement.models.TaskModel
import com.japps.taskmanagement.models.UserModel
import com.japps.taskmanagement.repository.DBRepository
import java.util.Date

class TaskViewModel:ViewModel() {
    final val taskTable = DBRepository().taskTable
    private val _task = MutableLiveData<TaskModel>()
    val task: LiveData<TaskModel> = _task

    private val _updateStatus = MutableLiveData<Boolean>()
    val updateStatus: LiveData<Boolean> = _updateStatus

    private val _taskList = MutableLiveData<List<TaskModel>>()
    val taskList: LiveData<List<TaskModel>> = _taskList

    fun getTasks() {
        var data:ArrayList<TaskModel> = ArrayList()
        taskTable.whereEqualTo("user_id",AppApplication.INSTANCE.getDatabase().document("user/"+AppApplication.INSTANCE.getSession().getUserId())).get()
            .addOnSuccessListener { result ->
                for(it in result.documents) {
                    Log.d("id",it.id)
                    data.add(TaskModel(id = it.id, description = it.data?.get("description").toString(), status_id = (it.data?.get("status_id") as DocumentReference).id, title = it.data?.get("title").toString(), user_id = (it.data?.get("user_id") as DocumentReference).id, createdAt = it.data?.get("createdAt") as Timestamp))
                }
                _taskList.setValue(data)
            }


    }

    fun updateTask(task:TaskModel){
        taskTable.document(task.id).update(hashMapOf(
            "title" to task.title,
            "description" to task.description,
            "status_id" to DBRepository().statusTable.document(task.status_id),
            "user_id" to DBRepository().userTable.document(task.user_id),
            "createdAt" to task.createdAt
        )).addOnCompleteListener({
            if(it.isSuccessful){
                _updateStatus.value = true
            }else{
                _updateStatus.value = false
            }
        })
    }

    fun addTask(task:TaskModel) {
        taskTable.add(hashMapOf(
            "title" to task.title,
            "description" to task.description,
            "status_id" to DBRepository().statusTable.document(task.status_id),
            "user_id" to DBRepository().userTable.document(task.user_id),
            "createdAt" to task.createdAt
        ))
            .addOnSuccessListener { documentReference ->
                task.id = documentReference.id;
                _task.value =task
            }
            .addOnFailureListener{e ->
                _task.value = null
                Log.w("ADd user", "Error adding document", e)
            }

    }
}