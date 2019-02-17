package com.fevziomurtekin.custom_mapview.data

import com.fevziomurtekin.custom_mapview.util.PlaceType

data class Place(

    var name :String="",

    var content :String="",

    var latitude :Double=0.0,

    var longitude :Double=0.0,

    var placeType : PlaceType = PlaceType.OTHER,

    var isUrl : Boolean = false,

    var url : String ="",

    var phone : String =""

    )
