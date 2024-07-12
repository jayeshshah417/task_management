package com.japps.taskmanagement.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.japps.taskmanagement.AppApplication
import com.japps.taskmanagement.models.StatusModel

class StatusViewModel : ViewModel() {
    final val statusTable = AppApplication.INSTANCE.getDatabase().collection("status")
    private val _statusList = MutableLiveData<List<StatusModel>>()
    val statusList: LiveData<List<StatusModel>> = _statusList



    fun getStatusList()  {
        var data:ArrayList<StatusModel> = ArrayList()
        statusTable.get()
            .addOnSuccessListener { result ->
                for(it in result.documents) {
                    data.add(StatusModel(id = it.id,name=it.data?.get("name").toString(), sequence = it.data?.get("sequence") as Long))

                    //cml copy client master list
                    //dis delivery instruction slip
                 }
                _statusList.value = data
            }
    }

}