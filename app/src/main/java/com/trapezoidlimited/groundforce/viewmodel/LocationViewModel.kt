package com.trapezoidlimited.groundforce.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.*
import com.trapezoidlimited.groundforce.data.LocationModel
import dagger.hilt.android.qualifiers.ApplicationContext

class LocationViewModel(application: Application): AndroidViewModel(application) {

    private val PERMISSION_REQUEST = 100
    private var fusedLocationClient: FusedLocationProviderClient
    private var request: LocationRequest
    private  var locationCallback: LocationCallback

    var locationMutable=MutableLiveData(LocationModel(0.0,0.0))
    var isLocationGotten = MutableLiveData("false")

    val _location: LiveData<LocationModel> get() = locationMutable
    val _isLocationGotten:LiveData<String> get() = isLocationGotten



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
                    locationMutable.value=LocationModel(longitude,latitude)
                    isLocationGotten.value="true"
                }

            }
        }
        fusedLocationClient.requestLocationUpdates(request,locationCallback, Looper.myLooper())
        startLocationUpdates()
    }




    //update the location
    //use suppress lint to ignore missing permission error, treat missing permission in fragment
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            request,
            locationCallback,
            null
        )
    }


}