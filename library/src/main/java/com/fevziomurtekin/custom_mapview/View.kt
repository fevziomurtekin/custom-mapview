package com.fevziomurtekin.custom_mapview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.fevziomurtekin.custom_mapview.Adapter.MarkerAdapter
import com.fevziomurtekin.custom_mapview.Adapter.MenuAdapter
import com.fevziomurtekin.custom_mapview.Adapter.SearchAdapter
import com.fevziomurtekin.custom_mapview.data.Place
import com.fevziomurtekin.custom_mapview.module.GlideApp
import com.fevziomurtekin.custom_mapview.util.PlaceType
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.view_layout.*

const val LOCATION = 1001

open class View : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMapClickListener,
    GoogleMap.OnMarkerClickListener {

    /*Map view*/
    private lateinit var mapView: MapView

    private lateinit var mMap: GoogleMap

    /*Default focused location. (Turkey location) */
    private val focus : LatLng = LatLng(39.92077,32.85411)

    private var mheight: Float = 0.toFloat()

    private var mwidth:Float = 0.toFloat()

    private var scale:Float = 0.toFloat()

    private val metrics = DisplayMetrics()

    private var search_counter :Int =0

    private var menu_counter : Int=0

    /*Default animation time.*/
    private var searchAnimation_time = 300

    /*Default animation time.*/
    private var menuAnimation_time = 300

    /*Places list added in Activity*/
    private var placesList : List<Place>? = null

    private var isSearchList : Boolean = false

    private var isMenuList : Boolean = false

    private var markerAdapter:MarkerAdapter ?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_layout)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocation()
        }
        setDisplaySize()

        initMapView()

        btn_search.setOnClickListener(this)

        btn_menu.setOnClickListener(this)

        initRecyclers()

        edt_search.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                Log.e("custom-mapview",s.toString())
                val arrays :MutableList<String> = mutableListOf()

                if(placesList!=null){
                    if(!placesList!!.isEmpty()){
                        for(i in 0..placesList!!.size-1){
                            if(placesList!![i].name==s.toString()){
                                arrays.add(placesList!![i].name)
                                val adapter : SearchAdapter = recycler_search.adapter as SearchAdapter
                                adapter.updateSearch(arrays)
                            }
                        }
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        /*TODO
        * location izni alındıktan sonra onReadMapkey yapılacak.
        * Search işleminin ve popup işleminin animasyonu yapılacak.
        * arama kısmında ve menu kısmındaki açılır liste animasyonu yapılacak.*/
    }

    private fun initRecyclers() {
        recycler_search.layoutManager = LinearLayoutManager(this,LinearLayout.VERTICAL,false)
        recycler_place_type.layoutManager = LinearLayoutManager(this,LinearLayout.VERTICAL,false)
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
        mMap.setOnMapClickListener(this)
        mMap.setOnMarkerClickListener(this)
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
                    if(placesList!=null) {
                        if(!placesList!!.isEmpty()) {
                            recycler_search.layoutParams.height = (mheight / 4).toInt()
                            recycler_search.parent.requestLayout()
                        }
                    }
                    isSearchList=true
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
                recycler_search.layoutParams.height = 0
                recycler_search.parent.requestLayout()
            }

            scaleX.addListener(object :Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    rl_search.layoutParams.width = startX.toInt()
                    rl_search.parent.requestLayout()
                    isSearchList=false
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

    private fun menuAnimation(){
        var startX :Float = 0F
        var finishX :Float = (mheight/1.85).toFloat()

        if(menu_counter%2==0){ //show animation.

            val scaleX = ValueAnimator.ofFloat(startX,finishX)

            scaleX.addUpdateListener {
                recycler_place_type.layoutParams.height = (it.animatedValue as Float).toInt()
                recycler_place_type.parent.requestLayout()
            }

            scaleX.addListener(object :Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    recycler_place_type.layoutParams.height = finishX.toInt()
                    recycler_place_type.parent.requestLayout()
                    btn_menu.isSelected=true
                    isMenuList=true
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })
            scaleX.interpolator = LinearInterpolator()
            scaleX.duration = menuAnimation_time.toLong()
            scaleX.start()



        }else{ //dismiss animation.

            val scaleX = ValueAnimator.ofFloat(finishX,startX)
            scaleX.addUpdateListener {
                recycler_place_type.layoutParams.height = (it.animatedValue as Float).toInt()
                recycler_place_type.parent.requestLayout()
            }

            scaleX.addListener(object :Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    recycler_place_type.layoutParams.height = startX.toInt()
                    recycler_place_type.parent.requestLayout()
                    btn_menu.isSelected=false
                    isMenuList=false
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })
            scaleX.interpolator = LinearInterpolator()
            scaleX.duration = menuAnimation_time.toLong()
            scaleX.start()

        }

        menu_counter++
    }

    fun addPlacesList(places: List<Place>){
        this.placesList=places

        if(placesList!=null) {
            for (i in 0..placesList!!.size - 1) {
                val markerOptions : MarkerOptions = MarkerOptions()
                    .snippet(placesList!![i].name)
                    .position(LatLng(placesList!![i].latitude,placesList!![i].longitude))
                    .title("")
                    .draggable(false)

                setIconMarker(placesList!![i],markerOptions)
            }
        }

        recycler_place_type.adapter = MenuAdapter(getMenuType(places),this)

        recycler_search.adapter = SearchAdapter(getSearchType(places),this)

    }

    private fun getSearchType(places: List<Place>): List<String> {
        val arrays :MutableList<String> = mutableListOf()

        for(i in 0..places.size-1) {
            arrays.add(places[i].name)
        }

        return arrays
    }

    private fun getMenuType(places: List<Place>): List<String> {
        val arrays :MutableList<String> = mutableListOf()

        for(i in 0..places.size-1) {
            if (!arrays.isEmpty()) {
                var isFound = false
                for (y in 0..arrays.size - 1) {
                    if (arrays[y] == places[i].placeType.name) isFound = true
                }
                if (!isFound) arrays.add(places[i].placeType.name)
            }else arrays.add(places[i].placeType.name)
        }

        return arrays
    }

    private fun setIconMarker(place: Place, markerOptions: MarkerOptions) {
        if (!place.isUrl) {
            val icon: Int
            when (place.placeType) {
                PlaceType.OTHER -> {
                    icon = R.drawable.other
                }
                PlaceType.BANK -> {
                    icon = R.drawable.bank
                }
                PlaceType.COFFEE -> {
                    icon = R.drawable.coffee

                }
                PlaceType.GAS_STATION -> {
                    icon = R.drawable.gas

                }
                PlaceType.HOSPITAL -> {
                    icon = R.drawable.hospital

                }
                PlaceType.SCHOOL -> {
                    icon = R.drawable.scholl

                }
                PlaceType.HOTEL -> {
                    icon = R.drawable.hotel
                }
                PlaceType.BAR -> {
                    icon = R.drawable.bar
                }
                PlaceType.STORE -> {
                    icon = R.drawable.store
                }
                PlaceType.PARKING_AREA -> {
                    icon = R.drawable.park
                }
                PlaceType.RESTUARANT -> {
                    icon = R.drawable.rest
                }
            }

            GlideApp.with(this)
                .asBitmap()
                .load(icon)
                .override((scale * 40).toInt(), (scale * 40).toInt())
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resource))
                        mMap.addMarker(markerOptions)
                    }
                })


        } else {
            GlideApp.with(this)
                .asBitmap()
                .load(place.url)
                .override((scale * 40).toInt(), (scale * 40).toInt())
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resource))
                        mMap.addMarker(markerOptions)
                    }
                })
        }
    }

    private fun showPopup(marker: Marker?){
        try {
            if (markerAdapter != null)
                markerAdapter!!.itemView.visibility = View.GONE

            val projection: Projection = mMap.projection
            val point: Point = projection.toScreenLocation(mMap.cameraPosition.target)

            markerAdapter = MarkerAdapter(
                this.layoutInflater.inflate(
                    R.layout.popup,
                    findViewById(R.id.map_parent) as RelativeLayout,
                    false
                ), this
            )
            markerAdapter!!.onBind(marker!!.snippet)
            markerAdapter!!.itemView.alpha = 0f

            (findViewById<RelativeLayout>(R.id.map_parent).addView(markerAdapter!!.itemView))

            val params = markerAdapter!!.itemView.getLayoutParams() as RelativeLayout.LayoutParams
//                params.addRule(RelativeLayout.ALIGN_TOP,R.id.center_image);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            val mar = (10 * scale.toInt())
            params.setMargins(
                2 * mar,
                0,
                2 * mar,
                (findViewById<RelativeLayout>(R.id.map_parent) as RelativeLayout).measuredHeight - point.y + mar + (37.5 * scale).toInt()
            )

            markerAdapter!!.itemView.animate().setListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    markerAdapter!!.itemView.visibility = View.VISIBLE
                }

                override fun onAnimationStart(animation: Animator?) {
                    super.onAnimationStart(animation)
                    markerAdapter!!.itemView.alpha = 1f
                }


            }).setStartDelay(250)
                .alpha(1f)
                .setDuration(150)
                .start()
        }catch (e:Exception){ }

    }

    override fun onMapClick(p0: LatLng?) {
        if(isMenuList) {
            menuAnimation()
        }

        if(isSearchList){
            searchAnimation()
        }

        try{
            markerAdapter!!.itemView.visibility=View.GONE
        }catch (ignored:java.lang.Exception){}
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.btn_search -> {
                searchAnimation()
            }
            R.id.btn_menu ->{
                if(placesList!=null) {
                    if (!placesList!!.isEmpty())
                        menuAnimation()
                }
            }
        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        showPopup(p0)
        return false
    }




}