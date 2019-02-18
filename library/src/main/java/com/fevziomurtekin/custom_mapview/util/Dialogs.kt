package com.fevziomurtekin.custom_mapview.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.fevziomurtekin.custom_mapview.R

 object Dialogs {

      fun gpsCreate(context: Context,onClickListener:View.OnClickListener){

        val dialog=Dialog(context)
        dialog.setContentView(R.layout.gpspopup)
        dialog.setTitle("")
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)

        val btnAllow = dialog.findViewById<Button>(R.id.btn_allow)
        val btnDeny = dialog.findViewById<Button>(R.id.btn_deny)
        val txtTitle = dialog.findViewById<TextView>(R.id.txt_title)
        val txtInfo = dialog.findViewById<Button>(R.id.txt_info)

        btnAllow.setOnClickListener(onClickListener)
        btnAllow.tag = dialog
        btnDeny.setOnClickListener(onClickListener)
        btnDeny.tag = dialog

        dialog.show()
    }
}