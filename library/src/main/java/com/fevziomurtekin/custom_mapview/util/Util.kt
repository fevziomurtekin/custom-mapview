package com.fevziomurtekin.custom_mapview.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object Util{
    fun hideKeyboard(context: Activity, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun hideKeyboard(context: Activity) {
        try {
            hideKeyboard(context, context.currentFocus!!)
        } catch (e: Exception) {
        }
    }
}