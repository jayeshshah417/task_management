package com.japps.taskmanagement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView

class GenericSpinnerAdapter<T>(
    private var context: Context,
    private var itemList: List<T>,
    private val layout: Int,
    private val getView: (position: Int,convertView:View? , parent: ViewGroup, item:T) -> Unit,
    private val getDropDownView: (position: Int,convertView:View? , parent: ViewGroup, item:T) -> Unit,
) :ArrayAdapter<T>(context,layout,itemList){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(layout, parent, false)

        getView(position, view, parent, itemList.get(position))
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(layout, parent, false)
        getDropDownView(position, view, parent, itemList.get(position))
        return view
    }
}