package com.aman.tomtomimplementation


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.aman.tomtomimplementation.databinding.ActivityMainBinding
import com.aman.tomtomimplementation.retro.PlacesApiResponse
import com.aman.tomtomimplementation.retro.Result
import com.aman.tomtomimplementation.retro.RetrofitClass
import com.aman.tomtomimplementation.room.RestaurantEntity
import com.aman.tomtomimplementation.room.RestaurantViewModel
import com.tomtom.sdk.location.GeoLocation
import com.tomtom.sdk.location.GeoPoint
import com.tomtom.sdk.location.LocationProvider
import com.tomtom.sdk.location.OnLocationUpdateListener
import com.tomtom.sdk.location.android.AndroidLocationProvider
import com.tomtom.sdk.map.display.MapOptions
import com.tomtom.sdk.map.display.TomTomMap
import com.tomtom.sdk.map.display.camera.CameraOptions
import com.tomtom.sdk.map.display.image.ImageFactory
import com.tomtom.sdk.map.display.location.LocationMarkerOptions
import com.tomtom.sdk.map.display.marker.Label
import com.tomtom.sdk.map.display.marker.Marker
import com.tomtom.sdk.map.display.marker.MarkerClickListener
import com.tomtom.sdk.map.display.marker.MarkerOptions
import com.tomtom.sdk.map.display.route.RouteOptions
import com.tomtom.sdk.map.display.ui.MapFragment
import com.tomtom.sdk.routing.RoutePlanner
import com.tomtom.sdk.routing.RoutePlanningCallback
import com.tomtom.sdk.routing.RoutePlanningResponse
import com.tomtom.sdk.routing.RoutingFailure
import com.tomtom.sdk.routing.online.OnlineRoutePlanner
import com.tomtom.sdk.routing.options.Itinerary
import com.tomtom.sdk.routing.options.RoutePlanningOptions
import com.tomtom.sdk.routing.options.guidance.ExtendedSections
import com.tomtom.sdk.routing.options.guidance.GuidanceOptions
import com.tomtom.sdk.routing.options.guidance.InstructionPhoneticsType
import com.tomtom.sdk.routing.route.Route
import com.tomtom.sdk.vehicle.Vehicle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), MarkerClickListener {
    private val apiKey = BuildConfig.TOMTOM_API_KEY
    private lateinit var mapFragment: MapFragment
    private lateinit var tomTomMap: TomTomMap
    private lateinit var locationProvider: LocationProvider
    private lateinit var onLocationUpdateListener: OnLocationUpdateListener
    private val TAG = "MainActivity"
    lateinit var userLocation : GeoLocation
    var restaurantsArray = arrayListOf<Result>()
    lateinit var restaurantViewModel: RestaurantViewModel
    var roomRestaurants = arrayListOf<RestaurantEntity>()
    var selectedMarker : Marker ?= null

    //for route
    private lateinit var routePlanner: RoutePlanner
    private var route: Route? = null
    private lateinit var routePlanningOptions: RoutePlanningOptions

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    /**
     * Method to verify permissions:
     * - [Manifest.permission.ACCESS_FINE_LOCATION]
     * - [Manifest.permission.ACCESS_COARSE_LOCATION]
     */
    private fun areLocationPermissionsGranted() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION,
    ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    ) == PackageManager.PERMISSION_GRANTED

    private val locationPermissionRequest =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            ) {
                 showUserLocation()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.location_permission_denied),
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        restaurantViewModel = ViewModelProvider(this)[RestaurantViewModel::class.java]
        initMap()
        initRouting()

        binding?.btnGetRestaurants?.setOnClickListener{
            restaurantViewModel.deleteRestaurant()
            getRestaurants()
        }

        binding?.btnNext?.setOnClickListener {
            startActivity(Intent(this,RestaurantListActivity::class.java ))
        }
    }

    /**
     * Plans the route by initializing by using the online route planner and default route replanner.
     */
    private fun initRouting() {
        routePlanner = OnlineRoutePlanner.create(context = this, apiKey = apiKey)
    }

    private fun requestLocationPermissions() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )
    }

    /**
     * In order to show the user’s location, the application must use the device’s location services,
     * which requires the appropriate permissions.
     */
    private fun enableUserLocation() {
        if (areLocationPermissionsGranted()) {
             showUserLocation()
        } else {
            requestLocationPermissions()
        }
    }

    private fun initMap() {
        val mapOptions = MapOptions(mapKey = apiKey)
        mapFragment = MapFragment.newInstance(mapOptions)
        supportFragmentManager.beginTransaction()
            .replace(R.id.map_container, mapFragment)
            .commit()
        mapFragment.getMapAsync { map ->
            tomTomMap = map
            enableUserLocation()
            tomTomMap.addMarkerClickListener(this)
        }
    }

    private fun getRestaurants(){
        if(!this::userLocation.isInitialized){
            showUserLocation()
            return
        }
        var map = HashMap<String, Any>()
        map["key"] = apiKey
        map["lat"] = userLocation.position.latitude
        map["lon"] = userLocation.position.longitude
//        map["lat"] = "31.3223978"
//        map["lon"] = "75.5734192"
        map["radius"] = binding.seekBar.value
        map["categoryset"] = 7315

        val call: Call<PlacesApiResponse> = RetrofitClass().getApiClient().getRestaurants(map)
        call.enqueue(object: Callback<PlacesApiResponse> {
            override fun onResponse(
                call: Call<PlacesApiResponse>,
                response: Response<PlacesApiResponse>
            ) {
                Log.e(TAG, "response ${response.body()} $response")
                if(response.body() != null) {
                    var apiResponse = response.body() as PlacesApiResponse
                    if(apiResponse.results?.isNotEmpty() == true) {
                        restaurantsArray.addAll(apiResponse.results as ArrayList<Result>)
                        addMarkers()
                    }
                }
                //adapter.notifyDataSetChanged()
                Log.e(TAG, " in success")
            }

            override fun onFailure(call: Call<PlacesApiResponse>, t: Throwable) {
                Log.e(TAG, " in failure")
            }
        })

    }

    private fun showUserLocation() {
        initLocationProvider()
        locationProvider.enable()
        // zoom to current location at city level
        onLocationUpdateListener =
            OnLocationUpdateListener { location ->
                userLocation = location
                tomTomMap.moveCamera(CameraOptions(location.position, zoom = 8.0))
                locationProvider.removeOnLocationUpdateListener(onLocationUpdateListener)
            }
        locationProvider.addOnLocationUpdateListener(onLocationUpdateListener)
        tomTomMap.setLocationProvider(locationProvider)
        val locationMarker = LocationMarkerOptions(type = LocationMarkerOptions.Type.Pointer)
        tomTomMap.enableLocationMarker(locationMarker)
    }

    private fun initLocationProvider() {
        locationProvider = AndroidLocationProvider(context = this)
        locationProvider.enable()
    }


    fun addMarkers(){
        this.tomTomMap.removeMarkers()
        showUserLocation()
        for(results in restaurantsArray){
            val location = GeoPoint(results.position?.lat?:0.0, results.position?.lon?:0.0)
            val markerOptions =
                MarkerOptions(
                    coordinate = location,
                    pinImage = ImageFactory.fromResource(R.drawable.ic_location),
                    label = Label(results?.poi?.name?:""),
                    tag = results.poi?.name?:"" + " address "+results?.address
                )
            tomTomMap.addMarker(markerOptions)
            roomRestaurants.add(RestaurantEntity(name = results.poi?.name))
        }
        restaurantViewModel.insertRestaurants(roomRestaurants)
    }

    override fun onMarkerClicked(marker: Marker) {
        calculateRouteTo(marker.coordinate)
        selectedMarker = marker

    }

    private fun calculateRouteTo(destination: GeoPoint) {
        val userLocation =
            tomTomMap.currentLocation?.position ?: return
        val itinerary = Itinerary(origin = userLocation, destination = destination)
        routePlanningOptions = RoutePlanningOptions(
            itinerary = itinerary,
            guidanceOptions = GuidanceOptions(
                phoneticsType = InstructionPhoneticsType.Ipa,
                extendedSections = ExtendedSections.All
            ),
            vehicle = Vehicle.Car()
        )
        routePlanner.planRoute(routePlanningOptions, routePlanningCallback)
    }
    private val routePlanningCallback = object : RoutePlanningCallback {
        override fun onSuccess(result: RoutePlanningResponse) {
            route = result.routes.first()
            drawRoute(route!!)
            tomTomMap.zoomToRoutes(100)
        }

        override fun onFailure(failure: RoutingFailure) {
            Toast.makeText(this@MainActivity, failure.message, Toast.LENGTH_SHORT).show()
        }

        override fun onRoutePlanned(route: Route) = Unit
    }

    /**
     * Used to draw route on the map
     */
    private fun drawRoute(route: Route) {
       // val instructions = route.mapInstructions()
        val routeOptions = RouteOptions(
            geometry = route.geometry,
            destinationMarkerVisible = true,
            departureMarkerVisible = true,
          //  instructions = instructions,
            routeOffset = route.routePoints.map { it.routeOffset }
        )

        AlertDialog.Builder(this).apply{
            setTitle("Route Information")
            setMessage("Route Information \nDistance between you and restaurant is ${route.guidanceProgressOffset}\n  name of restaurant is ${selectedMarker?.tag}")
            show()
        }
        tomTomMap.addRoute(routeOptions)
    }

}