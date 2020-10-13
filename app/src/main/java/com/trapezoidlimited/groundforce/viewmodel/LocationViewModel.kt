package com.trapezoidlimited.groundforce.viewmodel

import android.Manifest
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

class LocationViewModel(val context: Application): AndroidViewModel(context) {

    private val PERMISSION_REQUEST = 100

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var request: LocationRequest
    private lateinit var locationCallback: LocationCallback

    lateinit var locationMutable:MutableLiveData<LocationModel>
    lateinit var isLocationGotten:MutableLiveData<Boolean>
    val _location:LiveData<LocationModel> get() = locationMutable

    init {
        isLocationGotten.value=false
        locationCallback= LocationCallback()
        fusedLocationClient=LocationServices.getFusedLocationProviderClient(context)
    }


    //1a1 recieve current user location with intervals and display on the map
    fun requestLocationUpdates() {
        isLocationGotten.postValue(false)
        request = LocationRequest()
        /**interval for receiving location updates**/
        request.interval = 1000
        /**shortest interval for receiving location callBack**/
        request.fastestInterval = 5000

        //et user location using high accurate settings
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        //set location call back to receive updates to location change
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {

                val location = locationResult.lastLocation
                val latitude = location.latitude
                val longitude = location.longitude
                if (location != null) {

                    locationMutable.postValue(LocationModel(longitude,latitude))
                    isLocationGotten.postValue(true)

//                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUserLoc, 20f))
                }
            }
        }
        if (ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission()
        }

        //save the settings using fusedLocationClient, specifying the unit process to take place in the System
        fusedLocationClient.requestLocationUpdates(request,locationCallback, Looper.myLooper())
    }

    //1a. request permission from the user, in other to access location
    private fun requestPermission(){
        //Check whether this app has access to the location permission//
        val permission = ContextCompat.checkSelfPermission(
            getApplication(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        //If the location permission has been granted, then start receiving location updates //
        if (permission == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplication(),"Permission granted", Toast.LENGTH_LONG).show()
            //a1.
            requestLocationUpdates()
            //a2.
            startLocationUpdates()
        } else {

            //If the app doesn’t currently have access to the user’s location, then request access//
            ActivityCompat.requestPermissions(
                context.applicationContext as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST
            );
        }
    }

    //1a2 update the location
    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationClient.requestLocationUpdates(
            request,
            locationCallback,
            null
        )
    }











}