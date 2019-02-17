package com.fevziomurtekin.custom_mapview

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.view_layout.*

const val LOCATION = 1001

open class View : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {

    /*Map view*/
    private lateinit var mapView: MapView

    private lateinit var mMap: GoogleMap

    /*Default focused location. (Turkey location) */
    private val focus : LatLng = LatLng(39.92077,32.85411)

    private var mheight: Float = 0.toFloat()

    private var mwidth:Float = 0.toFloat()

    private var scale:Float = 0.toFloat()

    private val metrics = DisplayMetrics()

    private lateinit var btn_search: ImageView

    private var search_counter :Int =0

    /*Default animation time.*/
    private var searchAnimation_time = 300


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
        mwidth = metrics.widthPixels.toFloat()
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

        var startX :Float = 0F
        var finishX :Float = 0F

        if(search_counter%2==0){ //show animation.

            finishX = ((mwidth/1.15).toFloat())

            val scaleX = ValueAnimator.ofFloat(startX,finishX)

            scaleX.addUpdateListener {
                rl_search.layoutParams.width = (it.animatedValue as Float).toInt()
                rl_search.parent.requestLayout()
            }

            scaleX.addListener(object :Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    rl_search.layoutParams.width = finishX.toInt()
                    rl_search.parent.requestLayout()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })
            scaleX.interpolator = LinearInterpolator()
            scaleX.duration = searchAnimation_time.toLong()
            scaleX.start()



        }else{ //dismiss animation.

            val positions = IntArray(2)
            rl_search.getLocationInWindow(positions)

            finishX = rl_search.width.toFloat()


            val scaleX = ValueAnimator.ofFloat(finishX,startX)
            scaleX.addUpdateListener {
                rl_search.layoutParams.width = (it.animatedValue as Float).toInt()
                rl_search.parent.requestLayout()
            }

            scaleX.addListener(object :Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    rl_search.layoutParams.width = startX.toInt()
                    rl_search.parent.requestLayout()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })
            scaleX.interpolator = LinearInterpolator()
            scaleX.duration = searchAnimation_time.toLong()
            scaleX.start()

        }

        search_counter++
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.btn_search -> {
                searchAnimation()
            }
        }
    }


}