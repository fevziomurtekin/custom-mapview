package com.fevziomurtekin.custom_mapview.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.fevziomurtekin.custom_mapview.R
import com.fevziomurtekin.custom_mapview.View


class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private var list: List<String> = ArrayList()

    private var activity: View? = null

    private var onClickListener: android.view.View.OnClickListener? = null

    constructor(list: List<String>, onClickListener: android.view.View.OnClickListener) {
        this.list = list
        this.onClickListener = onClickListener

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SearchAdapter.ViewHolder {
        val itemView = LayoutInflater.from(p0.getContext()).inflate(R.layout.search_item, p0, false)
        return SearchAdapter.ViewHolder(itemView, this.onClickListener!!)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.txt_search_item.text = list.get(position)

        holder.btn_search_item.tag = list.get(position)

    }

    fun updateSearch(arrays: MutableList<String>) {
        this.list = arrays
        notifyDataSetChanged()
    }


    class ViewHolder(
        itemView: android.view.View,
        onClickListener: android.view.View.OnClickListener
    ) : RecyclerView.ViewHolder(itemView) {

        var btn_search_item: RelativeLayout

        var txt_search_item: TextView

        init {
            btn_search_item = itemView.findViewById(R.id.btn_search_item)
            txt_search_item = itemView.findViewById(R.id.txt_search_item)

            btn_search_item.setOnClickListener(onClickListener)
        }
    }

}