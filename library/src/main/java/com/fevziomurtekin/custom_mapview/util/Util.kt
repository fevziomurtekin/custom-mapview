package com.fevziomurtekin.custom_mapview.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.fevziomurtekin.custom_mapview.data.Place

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

    fun getMenuType(places: List<Place>): List<String> {
        val arrays :MutableList<String> = mutableListOf()

        for(place in places) {
            if (!arrays.isEmpty()) {
                var isFound = false
                for (y in 0..arrays.size - 1) {
                    if (arrays[y] ==place.placeType.name) isFound = true
                }
                if (!isFound) arrays.add(escapeString(place.placeType.name))
            }else arrays.add(escapeString(place.placeType.name))
        }

        return arrays
    }

    fun escapeString(string:String): String {
        return string.replace("_"," ")
    }

    fun getMenuList(menu:String,btn_menu: String, list: MutableList<String>): List<String>? {
        list.remove(menu)
        list.add(0,btn_menu)
        return list
    }

    fun getPlaces(
        menu: String,
        placesList: List<Place>?
    ): List<Place> {

        val places : MutableList<Place> = mutableListOf()

        for(place in placesList!!){
            if(escapeString(place.placeType.name).trim().toLowerCase()==menu.trim().toLowerCase())
                places.add(place)
        }

        return places
    }
}