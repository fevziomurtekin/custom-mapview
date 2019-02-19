package com.fevziomurtekin.custom_mapview.adapter

import android.support.v7.widget.RecyclerView
import com.fevziomurtekin.custom_mapview.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.fevziomurtekin.custom_mapview.R


class MenuAdapter : RecyclerView.Adapter<MenuAdapter.ViewHolder>{

    private var list:List<String> = ArrayList()

    private var activity:View ?= null

    private var onClickListener: android.view.View.OnClickListener?=null

    constructor(list:List<String>,onClickListener: android.view.View.OnClickListener) {
        this.list=list
        this.onClickListener=onClickListener

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MenuAdapter.ViewHolder {
        val itemView = LayoutInflater.from(p0.getContext()).inflate(R.layout.menu_item, p0, false)
        return MenuAdapter.ViewHolder(itemView, this.onClickListener!!)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.txt_menu_item.text =  list.get(position)

        holder.btn_menu_item.tag = list.get(position)

    }


    class ViewHolder(
        itemView: android.view.View,
        onClickListener: android.view.View.OnClickListener)
        : RecyclerView.ViewHolder(itemView) {

        var btn_menu_item:RelativeLayout

        var txt_menu_item:TextView

        init {
            btn_menu_item = itemView.findViewById(R.id.btn_menu_item)
            txt_menu_item = itemView.findViewById(R.id.txt_menu_item)

            btn_menu_item .setOnClickListener(onClickListener)
        }
    }

}