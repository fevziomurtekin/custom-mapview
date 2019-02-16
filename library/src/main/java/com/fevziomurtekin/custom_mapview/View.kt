package com.fevziomurtekin.custom_mapview

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng

const val LOCATION = 1001

open class View : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {

    /*Map view*/
    private lateinit var mapView: MapView

    private lateinit var mMap: GoogleMap

    /*Default focused location. (Turkey location) */
    private val focus : LatLng = LatLng(39.92077,32.85411)

    private var mheight: Float = 0.toFloat()

    private var mweight:Float = 0.toFloat()

    private var scale:Float = 0.toFloat()

    private val metrics = DisplayMetrics()

    private lateinit var btn_search: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_layout)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocation()
        }

        setDisplaySize()

        initMapView()


        btn_search = findViewById<ImageView>(R.id.btn_search)
        btn_search.setOnClickListener(this)


        /*TODO
        * location izni alındıktan sonra onReadMapkey yapılacak.
        * Search işleminin ve popup işleminin animasyonu yapılacak.
        * arama kısmında ve menu kısmındaki açılır liste animasyonu yapılacak.*/
    }

    private fun setDisplaySize() {
        windowManager.defaultDisplay.getMetrics(metrics)
        mheight = metrics.heightPixels.toFloat()
        mweight = metrics.widthPixels.toFloat()
        scale = metrics.density
    }

    private fun initMapView() {
        mapView = findViewById(R.id.mapview) as MapView
        mapView.onCreate(null)
        mapView.onResume()
        mapView.getMapAsync(this)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkLocation(){
        if(this.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
            && this.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            this.requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION)
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0!!
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(focus,6f))
    }

    private fun searchAnimation(){



    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.btn_search -> {
                searchAnimation()
            }
        }
    }


}