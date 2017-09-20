package com.kotlintesting

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 *@author Amanpal Singh.
 */
class ItemsAdapter(val list: ArrayList<String>,val dashboard: Dashboard) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is ViewHolder) {
            val dataHolder: ViewHolder = holder
            dataHolder.bindItems(list[position],dashboard)

        }
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(name: String, dashboard: Dashboard) {
            val tvName = itemView.findViewById<TextView>(R.id.tvName)
            tvName.text = name
            tvName.setOnClickListener {

                dashboard.onItemClicked(adapterPosition)

            }
        }

    }

}