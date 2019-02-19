package com.fevziomurtekin.custom_mapview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Point
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.fevziomurtekin.custom_mapview.adapter.MarkerAdapter
import com.fevziomurtekin.custom_mapview.adapter.MenuAdapter
import com.fevziomurtekin.custom_mapview.adapter.SearchAdapter
import com.fevziomurtekin.custom_mapview.data.Place
import com.fevziomurtekin.custom_mapview.module.GlideApp
import com.fevziomurtekin.custom_mapview.util.Dialogs
import com.fevziomurtekin.custom_mapview.util.PlaceType
import com.fevziomurtekin.custom_mapview.util.Util
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.view_layout.*

const val LOCATION = 1001
const val PHONE = 1002

open class View : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMapClickListener,
    GoogleMap.OnMarkerClickListener {

    /*Map view*/
    private lateinit var mapView: MapView

    private lateinit var mMap: GoogleMap

    /*Default focused location. (Turkey location) */
    private var focus : LatLng = LatLng(39.92077,32.85411)

    /*Default animation time.*/
    private var searchAnimation_time = 300

    /*Default animation time.*/
    private var menuAnimation_time = 300

    private var defaultSearchError="The location you were looking for was not found on the map."

    /* Phone screen attributes.*/
    private var mheight: Float = 0.toFloat()

    private var mwidth:Float = 0.toFloat()

    private var scale:Float = 0.toFloat()

    private val metrics = DisplayMetrics()

    private var search_counter :Int =0

    private var menu_counter : Int=0

    /*Places list added in Activity*/
    private var placesList : MutableList<Place>? = null

    private var isSearchList : Boolean = false

    private var isMenuList : Boolean = false

    private var markerAdapter:MarkerAdapter ?=null

    private var phone:String =""

    private var currentMarker: MarkerOptions? =null

    private var current_latlng:LatLng = LatLng(0.0,0.0)

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var activity:com.fevziomurtekin.custom_mapview.View=this

    private var builder:LatLngBounds.Builder=LatLngBounds.builder()

    private var markerList:MutableList<MarkerOptions> = mutableListOf()

    private var tempPlaceList:MutableList<Place> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_layout)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocation()
        }
        setDisplaySize()

        initMapView()

        btn_menu.isEnabled=false

        btn_search.setOnClickListener(this)
        btn_target.setOnClickListener(this)
        btn_menu.setOnClickListener(this)

        initRecyclers()

        edt_search.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                Log.e("custom-mapview",s.toString())
                val arrays :MutableList<Place> = mutableListOf()

                if(placesList!=null){
                    if(!placesList!!.isEmpty()){
                        for(place in placesList!!){
                            if(place.name.contains(s.toString())){
                                arrays.add(place)
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

        edt_search.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if(actionId==EditorInfo.IME_ACTION_SEARCH){
                    Util.hideKeyboard(activity)
                    searchPlace()
                    return true
                }
                return false
            }
        })

    }

    override fun onResume() {
        super.onResume()
        getLastLocation()
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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        try{ getLastLocation() }catch (e:java.lang.Exception){}
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkLocation(){
        if(this.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
            && this.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            this.requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION)
        }
    }

    private fun getLastLocation(){
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(this){
            location ->

            if(location!=null){

                current_latlng = LatLng(location.latitude,location.longitude)
                currentMarker = MarkerOptions()
                    .title("")
                    .snippet("Me")
                    .position(current_latlng!!)
                    .draggable(false)

                GlideApp.with(this.applicationContext)
                    .asBitmap()
                    .load(R.drawable.user)
                    .override((scale * 30).toInt(), (scale * 30).toInt())
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            try {
                                currentMarker?.icon(BitmapDescriptorFactory.fromBitmap(resource))
                                mMap.addMarker(currentMarker)
                            }catch (e:java.lang.Exception){}
                        }
                    })
            }
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        /*MapsInitializer.initialize(getApplicationContext())*/
        MapsInitializer.initialize(applicationContext)
        mMap = p0!!
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(focus,6f))
        //mMap.isMyLocationEnabled=true
        mMap.setOnMapClickListener(this)
        mMap.setOnMarkerClickListener(this)


    }

    private fun drawLine(place: Place){

        val mList : MutableList<MarkerOptions> = mutableListOf()
        val poly : PolylineOptions=PolylineOptions()
            .color(this.resources.getColor(R.color.blue))
            .add(current_latlng,LatLng(place.latitude,place.longitude))

        mMap.addPolyline(poly)

        mList.add(MarkerOptions()
            .title("")
            .position(LatLng(place.latitude,place.longitude)))
        mList.add(MarkerOptions()
            .title("")
            .position(current_latlng))

        centerToMap(mList)
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
        var finishX :Float = (mheight/2.25).toFloat()

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

    /*Attributes.*/

    protected fun addPlacesList(places: MutableList<Place>){
        tempPlaceList.clear()
        this.placesList=places

        if(placesList!=null) {

            btn_menu.isEnabled=true

            for (place in placesList!!) {
                val markerOptions : MarkerOptions = MarkerOptions()
                    .snippet(place.name)
                    .position(LatLng(place.latitude,place.longitude))
                    .title("")
                    .draggable(false)
                markerList.add(markerOptions)
                setIconMarker(place,markerOptions)
                tempPlaceList.add(place)
            }
        }

        recycler_place_type.adapter = MenuAdapter(Util.getMenuType(places),this)
        recycler_search.adapter = SearchAdapter(places,this)

    }

    protected fun setDefaultSearchError(error:String){defaultSearchError=error}

    protected fun setFocus (focus:LatLng){this.focus=focus}

    protected fun setSearchAnimation_time(searchAnimation_time:Int){this.searchAnimation_time=searchAnimation_time}

    protected fun setMenuAnimation_time(menuAnimation_time:Int){this.menuAnimation_time=menuAnimation_time}

    private fun setIconMarker(place: Place, markerOptions: MarkerOptions) {
        if (!place.isUrl) {
            var icon: Int = 0
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
                PlaceType.MALL -> {
                    icon = R.drawable.mall
                }
                PlaceType.MOSQUE -> {
                    icon = R.drawable.mosque
                }
                PlaceType.STADIUM -> {
                    icon = R.drawable.stadium
                }
                PlaceType.MUSEUM -> {
                    icon = R.drawable.museum
                }
                PlaceType.HEART -> {
                    icon = R.drawable.like
                }
            }

            GlideApp.with(this.applicationContext)
                .asBitmap()
                .load(icon)
                .override((scale * 30).toInt(), (scale * 30).toInt())
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        try {
                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resource))
                            mMap.addMarker(markerOptions)
                        }catch (e:java.lang.Exception){}
                    }
                })


        } else {
            GlideApp.with(this)
                .asBitmap()
                .load(place.url)
                .override((scale * 30).toInt(), (scale * 30).toInt())
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        try {
                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resource))
                            mMap.addMarker(markerOptions)
                        }catch (e:java.lang.Exception){}
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

            val place:Place= placesList!!.find {
                it.name==marker!!.snippet
            }!!

            markerAdapter!!.onBind(place)
            markerAdapter!!.itemView.alpha = 0f

            (findViewById<RelativeLayout>(R.id.map_parent).addView(markerAdapter!!.itemView))

            val params = markerAdapter!!.itemView.getLayoutParams() as RelativeLayout.LayoutParams
//                params.addRule(RelativeLayout.ALIGN_TOP,R.id.center_image);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            val mar = (10 * scale.toInt())
            params.setMargins(
                2 * mar,
                15,
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

    private fun searchPlace(){

        val search = edt_search.text.toString()
        var isFound = false

        if(isMenuList) {
            menuAnimation()
        }

        if(isSearchList){
            searchAnimation()
        }

        if(tempPlaceList!=null){
            for(place in tempPlaceList!!){
                if(place.name.trim().toLowerCase()==search.trim().toLowerCase()) {
                    isFound=true
                    moveMap(LatLng(place.latitude, place.longitude))
                }
            }
            if(!isFound) Toast.makeText(activity,defaultSearchError,Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateMarker(
        places: List<Place>,
        menu: String
    ) {
        mMap.clear()
        markerList.clear()

        getLastLocation()

        if(menu==getString(R.string.default_menu)){
            for(place in tempPlaceList){
                val marker : MarkerOptions = MarkerOptions()
                    .position(LatLng(place.latitude,place.longitude))
                    .title("")
                    .snippet(place.name)
                    .draggable(false)
                markerList.add(marker)
                setIconMarker(place,marker)
            }
        }else{
            for(place in places){
                val marker : MarkerOptions = MarkerOptions()
                    .position(LatLng(place.latitude,place.longitude))
                    .title("")
                    .snippet(place.name)
                    .draggable(false)
                markerList.add(marker)
                setIconMarker(place,marker)
            }
        }
        centerToMap(markerList)
    }

    private fun centerToMap(markerList: MutableList<MarkerOptions>){
        for(marker in markerList){
            builder.include(marker.position)
        }

        val bounds :LatLngBounds = builder.build()
        val padding = 10
        val cameraUpdate :CameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds,padding)
        mMap.animateCamera(cameraUpdate)
    }

    private fun moveMap(location:LatLng){
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(location,15f))
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

    private fun callPhone(){
        val intent = Intent(Intent.ACTION_CALL)
        intent.setData(Uri.parse("tel:"+phone))
        startActivity(intent)
    }

    private fun checkGps(){

        val locationManager:LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Dialogs.gpsCreate(this,View.OnClickListener{
                val dialog: Dialog
                when(it.id){
                    R.id.btn_deny->{
                        dialog = it.tag as Dialog
                        dialog.dismiss()
                    }
                    R.id.btn_allow->{
                        dialog = it.tag as Dialog
                        dialog.dismiss()
                        val callGPSSettingIntent
                                = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(callGPSSettingIntent)
                    }
                }

            })
        }


    }

    override fun onClick(v: View?) {
        try{Util.hideKeyboard(activity)}catch (e:Exception){}
        when(v!!.id) {
            R.id.btn_search -> {
                searchAnimation()
            }
            R.id.btn_menu ->{
                try{markerAdapter?.itemView!!.visibility=View.GONE}catch (e:java.lang.Exception){}
                if(placesList!=null) {
                    if (!placesList!!.isEmpty())
                        menuAnimation()
                }
            }
            R.id.btn_phone->{
                try{markerAdapter?.itemView!!.visibility=View.GONE}catch (e:java.lang.Exception){}
                if(isMenuList) {
                    menuAnimation()
                }

                if(isSearchList){
                    searchAnimation()
                }

                phone = v.getTag() as String

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (this.checkSelfPermission(android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        this.requestPermissions(
                            arrayOf(
                                android.Manifest.permission.CALL_PHONE
                            ), PHONE
                        )
                    }else{
                        callPhone()
                    }
                }
            }
            R.id.btn_way->{
                try{markerAdapter?.itemView!!.visibility=View.GONE}catch (e:java.lang.Exception){}
                if(isMenuList) {
                    menuAnimation()
                }

                if(isSearchList){
                    searchAnimation()
                }

                getLastLocation()
                val place = v.tag as Place
                drawLine(place)
            }

            R.id.btn_target->{
                try{markerAdapter?.itemView!!.visibility=View.GONE}catch (e:java.lang.Exception){}
                if(isMenuList) {
                    menuAnimation()
                }

                if(isSearchList){
                    searchAnimation()
                }

                if(current_latlng.latitude!=0.toDouble() && current_latlng.longitude!=0.toDouble()){
                    moveMap(current_latlng)
                }
            }

            R.id.btn_search_item->{
                if(isMenuList) {
                    menuAnimation()
                }

                if(isSearchList){
                    searchAnimation()
                }
                try{markerAdapter?.itemView!!.visibility=View.GONE}catch (e:java.lang.Exception){}
                val place = v.tag as Place
                updateMarker(tempPlaceList,getString(R.string.default_menu))
                moveMap(LatLng(place.latitude,place.longitude))

            }

            R.id.btn_menu_item->{
                if(isMenuList) {
                    menuAnimation()
                }

                if(isSearchList){
                    searchAnimation()
                }
                try{markerAdapter?.itemView!!.visibility=View.GONE}catch (e:java.lang.Exception){}
                val menu = v.tag as String
                val places:List<Place> = Util.getPlaces(menu,placesList)
                val adapter : MenuAdapter = recycler_place_type.adapter as MenuAdapter
                adapter.updateMenu(Util.getMenuList(menu,btn_menu.text.toString(),
                    adapter.getList() as MutableList<String>
                )!!)
                btn_menu.text=menu

                updateMarker(places,menu)
            }

        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        if(isMenuList) {
            menuAnimation()
        }

        if(isSearchList){
            searchAnimation()
        }
        showPopup(p0)
        return false
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            LOCATION->{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    checkGps()
                    getLastLocation()

                }

            }
            PHONE->{
                if (grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                    callPhone()
                }
            }
        }
    }

}