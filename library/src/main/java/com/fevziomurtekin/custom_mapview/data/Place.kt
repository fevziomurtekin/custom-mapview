package com.fevziomurtekin.custom_mapview.data

import com.fevziomurtekin.custom_mapview.util.PlaceType

data class Place(

    val name :String="",

    val content :String="",

    val latitude :Double=0.0,

    val longitude :Double=0.0,

    val placeType : PlaceType = PlaceType.OTHER,

    val isUrl : Boolean = false,

    val url : String =""

    )
