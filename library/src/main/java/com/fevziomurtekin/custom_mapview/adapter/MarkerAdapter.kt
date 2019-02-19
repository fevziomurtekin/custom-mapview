package com.fevziomurtekin.custom_mapview.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.fevziomurtekin.custom_mapview.R
import com.fevziomurtekin.custom_mapview.data.Place
import java.lang.Exception

class MarkerAdapter: RecyclerView.ViewHolder{

    lateinit var txtInfo : TextView
    lateinit var txtContent : TextView
    lateinit var btnPhone : ImageView
    lateinit var btnWay : ImageView

    constructor(itemview: View, act:com.fevziomurtekin.custom_mapview.View) : super(itemview) {
        val width = act.window.decorView.width
        txtInfo  = itemview.findViewById<TextView>(R.id.txt_title)
        txtContent  = itemview.findViewById<TextView>(R.id.txt_content)
        btnPhone  = itemview.findViewById<ImageView>(R.id.btn_phone)
        btnWay  = itemview.findViewById<ImageView>(R.id.btn_way)

        btnPhone.setOnClickListener(act)
        btnWay.setOnClickListener(act)

        itemview.tag=act
    }

    fun onBind(place:Place){
        try {
            txtInfo.text = place.name
            txtContent.text = place.content

            if(place.phone!="")
                btnPhone.tag = place.phone
            else
                btnPhone.isEnabled = false

        }catch (e:Exception){}
    }


}