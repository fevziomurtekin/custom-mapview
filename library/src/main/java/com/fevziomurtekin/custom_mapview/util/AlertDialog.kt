package com.fevziomurtekin.custom_mapview.util

import android.app.Dialog
import android.content.Context
import com.fevziomurtekin.custom_mapview.R

class AlertDialog{

    fun gpsCreate(context: Context){

        val dialog=Dialog(context)
        dialog.setContentView(R.layout.gpspopup)
    }
}