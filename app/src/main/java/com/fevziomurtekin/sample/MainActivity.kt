package com.fevziomurtekin.sample

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.fevziomurtekin.custom_mapview.View
import com.fevziomurtekin.custom_mapview.data.Place
import com.fevziomurtekin.custom_mapview.util.PlaceType

class MainActivity : View() {

    private var lists:MutableList<Place> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*Default place list adding*/
        val school:Place = Place()
        school.name="Uludag University"
        school.placeType=PlaceType.SCHOOL
        school.isUrl=false
        school.content=""
        /*40.234583,28.8812533*/
        school.latitude=40.234583
        school.longitude=28.8812533

        val uludag:Place = Place()
        uludag.name="Uludağ National Park"
        uludag.placeType=PlaceType.PARKING_AREA
        uludag.isUrl=true
        uludag.url="https://cdn4.vectorstock.com/i/1000x1000/22/63/mountain-icons-on-white-background-vector-21032263.jpg"
        uludag.content=""
        /*40.112148,29.0715413*/
        uludag.latitude=40.112148
        uludag.longitude=29.0715413

        lists.add(school)
        lists.add(uludag)

        this.addPlacesList(lists)

    }
}
