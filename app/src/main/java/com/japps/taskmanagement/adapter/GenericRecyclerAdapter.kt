package com.japps.taskmanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class GenericRecyclerAdapter<T>(
    private var itemList: List<T>,
    private val layout: Int,
    private val hasRowClick: Boolean,
    private val onBind: (holder: RecyclerView.ViewHolder, position: Int, item: T) -> Unit,
    private val onRowClick: (position: Int, item: T) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return object : RecyclerView.ViewHolder(view) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBind(holder, position, itemList[position])

        if (hasRowClick) {
            holder.itemView.setOnClickListener {
                onRowClick(holder.adapterPosition, itemList[holder.adapterPosition])
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun updateList(updatedList:List<T>){
        this.itemList = updatedList
    }
}
