package com.trapezoidlimited.groundforce.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import com.trapezoidlimited.groundforce.data.GpsState
import com.trapezoidlimited.groundforce.data.LocationModel
import com.trapezoidlimited.groundforce.utils.AppConstants
import com.trapezoidlimited.groundforce.utils.GpsUtils


class LocationViewModel(application: Application): AndroidViewModel(application) {

    private val PERMISSION_REQUEST = 100

    private var fusedLocationClient: FusedLocationProviderClient
    private var request: LocationRequest
    var gpsUtil=GpsUtils(getApplication())
    private  var locationCallback: LocationCallback

    //for change and updates
    var locationMutable=MutableLiveData(LocationModel(0.0, 0.0))
    var isLocationGotten = MutableLiveData("false")
    var isGpsEnabled = MutableLiveData(GpsState(false))

    //for observation only

    val _location: LiveData<LocationModel> get() = locationMutable
    val _isLocationGotten:LiveData<String> get() = isLocationGotten
    val _isGpsEnabled:LiveData<GpsState> get() = isGpsEnabled



    init {
        request= LocationRequest()
        locationCallback= LocationCallback()
        fusedLocationClient= LocationServices.getFusedLocationProviderClient(application)
        requestLocationUpdates()
    }


    //recieve current user location with intervals and display on the map
    //use suppress lint to ignore missing permission request, treat missing permission in fragment
    @SuppressLint("MissingPermission")
    fun requestLocationUpdates() {
        request = LocationRequest()
        //fusedLocationClient.lastLocation
        /**interval for receiving location updates**/
        //request.interval = 10000
        /**shortest interval for receiving location callBack**/
        //request.fastestInterval = 5000

        //et user location using high accurate settings
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        //set location call back to receive updates to location change
        locationCallback = object : LocationCallback() {

            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                val latitude = location.latitude
                val longitude = location.longitude
                if (location != null) {
                    //if location is not null, update the islocationgotten to be true and locationMutable value with
                    //latitude and longitude gotten from location
                    locationMutable.value=LocationModel(longitude, latitude)
                    isLocationGotten.value="true"

                    //one time location must have been gotten at the call back and mutable data updated, remove listening for location update
                    //if (fusedLocationClient != null) {
                      //  fusedLocationClient.removeLocationUpdates(locationCallback);
                    //}
                }
                //location is null, use fused location client to request location update
                else {
                    //maybe gps is turned off, trigger to turn on gps if it is not turned on
                    triggerGps()
                    fusedLocationClient.requestLocationUpdates(request, locationCallback, null)
                }

            }
        }
        fusedLocationClient.requestLocationUpdates(request, locationCallback, null)
        startLocationUpdates()
    }

    fun triggerGps(){
        gpsUtil.turnGPSOn()
        //when the gps is turned on, the state by now would be true, return it
        isGpsEnabled.value=gpsUtil.returnGpsState()
    }










//    //update the location
//    //use suppress lint to ignore missing permission error, treat missing permission in fragment
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            request,
            locationCallback,
            null
        )
    }




}