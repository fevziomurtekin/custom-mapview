package com.fevziomurtekin.custom_mapview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng

open class View : AppCompatActivity(), OnMapReadyCallback {


    /*Map view*/
    private var mapview : MapView=null

    private lateinit var mMap: GoogleMap

    /*Default focused location. (Turkey location) */
    private val focus : LatLng = LatLng(44.5742, 25.90902)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_layout)

        checkLocation()

        initMapView()



        /*TODO
        * location izni alınmış o kontrol edilip izin alınacak.
        * location izni alındıktan sonra onReadMapkey yapılacak.
        * Search işleminin ve popup işleminin animasyonu yapılacak.
        * arama kısmında ve menu kısmındaki açılır liste animasyonu yapılacak.*/
    }

    private fun initMapView() {
        mapview = findViewById<MapView>(R.id.mapview) as MapView
        if (mapview != null) {
            mapview.onCreate(null)
            mapview.onResume()
            mapview.getMapAsync(this)
        }
    }

    private fun checkLocation(){

    }

    override fun onMapReady(p0: GoogleMap?) {

    }
}