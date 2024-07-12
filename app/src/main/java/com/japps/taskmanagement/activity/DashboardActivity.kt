package com.japps.taskmanagement.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Timestamp
import com.japps.taskmanagement.R
import com.japps.taskmanagement.adapter.GenericRecyclerAdapter
import com.japps.taskmanagement.adapter.GenericSpinnerAdapter
import com.japps.taskmanagement.databinding.ActivityDashboardBinding
import com.japps.taskmanagement.databinding.LayoutAddTaskItemBinding
import com.japps.taskmanagement.models.StatusModel
import com.japps.taskmanagement.models.TaskModel
import com.japps.taskmanagement.models.UserModel
import com.japps.taskmanagement.repository.DBRepository
import com.japps.taskmanagement.viewmodels.StatusViewModel
import com.japps.taskmanagement.viewmodels.TaskViewModel
import com.japps.taskmanagement.viewmodels.UserViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DashboardActivity : AppCompatActivity() {
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private var userList:List<UserModel> = ArrayList<UserModel>()
    private var statusList:List<StatusModel> = ArrayList<StatusModel>()
    private lateinit var dashboardBinding: ActivityDashboardBinding
    private lateinit var adapter: GenericRecyclerAdapter<TaskModel>
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        dashboardBinding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(dashboardBinding.root)
        val iv_add = dashboardBinding.ivAdd
        iv_add.setOnClickListener({
            showAddTaskBottomSheet()
        })
       var  taskList = dashboardBinding.rvList
        adapter = GenericRecyclerAdapter(
            itemList = ArrayList(),
            hasRowClick = true,
            layout = R.layout.layout_task_item,
            onBind = { holder, position, item ->
                val tv_title = holder.itemView.findViewById<TextView>(R.id.tv_title)
                val tv_createdAt = holder.itemView.findViewById<TextView>(R.id.tv_createdat)
                val tv_description = holder.itemView.findViewById<TextView>(R.id.tv_description)
                val sp_status = holder.itemView.findViewById<Spinner>(R.id.sp_status)
                val sp_user = holder.itemView.findViewById<Spinner>(R.id.sp_user)
                val statusAdapter =  GenericSpinnerAdapter(context = this@DashboardActivity, itemList = statusList,
                    layout = R.layout.layout_spinner_item,
                    getView = { position, convertView, parent, item ->
                        val tvView = convertView?.findViewById<TextView>(R.id.tv_title)
                        tvView?.text = item?.name
                        convertView
                    },
                    getDropDownView = { position, convertView, parent, item ->

                        val tvView = convertView?.findViewById<TextView>(R.id.tv_title)
                        tvView?.text = item?.name
                        convertView
                    })

                val userAdapter = GenericSpinnerAdapter(context = this@DashboardActivity, itemList = userList,
                    layout = R.layout.layout_spinner_item,
                    getView = { position, convertView, parent, item ->
                        val tvView = convertView?.findViewById<TextView>(R.id.tv_title)
                        tvView?.text = item?.name
                        convertView
                    },
                    getDropDownView = { position, convertView, parent, item ->

                        val tvView = convertView?.findViewById<TextView>(R.id.tv_title)
                        tvView?.text = item?.name
                        convertView
                    })

                sp_user.adapter = userAdapter
                sp_status.adapter = statusAdapter
                userAdapter.notifyDataSetChanged()
                statusAdapter.notifyDataSetChanged()

                sp_user.onItemSelectedListener=object :
                    OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                        taskViewModel.updateStatus.observe(this@DashboardActivity,{
                            if(!it){
                                adapter?.notifyDataSetChanged()
                            }
                            taskViewModel.updateStatus.removeObservers(this@DashboardActivity)
                        })
                        val task = TaskModel(item.id,item.title,item.description,item.status_id,userList.get(pos).id,item.createdAt)
                        taskViewModel.updateTask(task)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }
                }
                sp_status.onItemSelectedListener=object :
                    OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                        taskViewModel.updateStatus.observe(this@DashboardActivity,{
                            if(!it){
                                adapter?.notifyDataSetChanged()
                            }
                            taskViewModel.updateStatus.removeObservers(this@DashboardActivity)
                        })
                        val task = TaskModel(item.id,item.title,item.description,statusList.get(pos).id,item.user_id,item.createdAt)
                        taskViewModel.updateTask(task)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }
                }

                sp_user?.setSelection(userList.indexOfFirst { it.id==item.user_id })
                sp_status?.setSelection(statusList.indexOfFirst { it.id==item.status_id })
                tv_title.text = item.title
                tv_description.text = item.description
                tv_createdAt.text = SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS", Locale.getDefault()).format(item.createdAt.toDate())


            },
            onRowClick = { position, item ->

            }
        )
        taskList.layoutManager = LinearLayoutManager(this)
        taskList.adapter = adapter
        adapter.notifyDataSetChanged()
        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        taskViewModel.taskList.observe(this,{
            adapter.updateList(it)
            adapter.notifyDataSetChanged()
        })

        val userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        val statusViewModel = ViewModelProvider(this).get(StatusViewModel::class.java)
        userViewModel.getUserList()
        userViewModel.userList.observe(this,{
            userList = it
            statusViewModel.getStatusList()
        })

        statusViewModel.statusList.observe(this,{
            statusList = it
            taskViewModel.getTasks()
        })


    }

    private fun setDefaultStatusSelected(statusId: String, statusList: List<StatusModel>, spStatus: Spinner?) {

    }

    private fun setDefaultUserSelected(userId: String, userList: List<UserModel>, spUser: Spinner?) {
        spUser?.setSelection(userList.indexOfFirst { it.name==userId })
    }

    private fun showAddTaskBottomSheet(){
         bottomSheetDialog = BottomSheetDialog(this@DashboardActivity)
        val layoutAddTaskItemBinding = LayoutAddTaskItemBinding.inflate(layoutInflater)
        val bt_submit = layoutAddTaskItemBinding.btSubmit
        val et_title = layoutAddTaskItemBinding.etTitle
        val et_desc = layoutAddTaskItemBinding.etDescription
        val sp_user = layoutAddTaskItemBinding.spUser
        val sp_status = layoutAddTaskItemBinding.spStatus

// Set up BottomSheetBehavior to expand the dialog fully

        val statusAdapter =  GenericSpinnerAdapter(context = this@DashboardActivity, itemList = statusList,
            layout = R.layout.layout_spinner_item,
            getView = { position, convertView, parent, item ->
                val tvView = convertView?.findViewById<TextView>(R.id.tv_title)
                tvView?.text = item?.name
                convertView
            },
            getDropDownView = { position, convertView, parent, item ->

                val tvView = convertView?.findViewById<TextView>(R.id.tv_title)
                tvView?.text = item?.name
                convertView
            })

        val userAdapter = GenericSpinnerAdapter(context = this@DashboardActivity, itemList = userList,
            layout = R.layout.layout_spinner_item,
            getView = { position, convertView, parent, item ->
                val tvView = convertView?.findViewById<TextView>(R.id.tv_title)
                tvView?.text = item?.name
                convertView
            },
            getDropDownView = { position, convertView, parent, item ->

                val tvView = convertView?.findViewById<TextView>(R.id.tv_title)
                tvView?.text = item?.name
                convertView
            })

        taskViewModel.task.observe(this@DashboardActivity,{
            if(it!=null){
                bottomSheetDialog.dismiss()
                taskViewModel.getTasks()
            }
        })
        sp_user.adapter = userAdapter
        sp_status.adapter = statusAdapter
        userAdapter.notifyDataSetChanged()
        statusAdapter.notifyDataSetChanged()

        sp_user.onItemSelectedListener=object :
            OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
        sp_status.onItemSelectedListener=object :
            OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        bt_submit.setOnClickListener({
            if(validate(et_desc.text.toString(),et_title.text.toString())){
               taskViewModel.addTask(
                   TaskModel(
                       id = "",
                       title = et_title.text.toString(),
                       description = et_desc.text.toString(),
                       status_id = ((sp_status.selectedItem as StatusModel).id),
                       user_id = ((sp_user.selectedItem as UserModel).id),
                       createdAt = Timestamp(Date()))
               )
            }else{
                Toast.makeText(this@DashboardActivity,"Please fillup All Details !!",Toast.LENGTH_LONG).show()
            }
        })
        bottomSheetDialog.setContentView(layoutAddTaskItemBinding.root)
        bottomSheetDialog.setOnDismissListener({
            taskViewModel.task.removeObservers(this@DashboardActivity)
        })
        bottomSheetDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        val behavior = BottomSheetBehavior.from(bottomSheetDialog.window!!.decorView.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet))
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDialog.show()
    }

    private fun validate( et_title:String, et_desc:String): Boolean {
        if(!et_desc.isNullOrEmpty() && !et_title.isNullOrEmpty())
            return true
        else return false
    }
}