package com.trapezoidlimited.groundforce.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.trapezoidlimited.groundforce.data.GpsState
import com.trapezoidlimited.groundforce.data.LocationModel
import com.trapezoidlimited.groundforce.utils.AppConstants


class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private var fusedLocationClient: FusedLocationProviderClient
    private var request: LocationRequest
    private var locationCallback: LocationCallback

    //for change and updates
    var locationMutable = MutableLiveData(LocationModel(0.0, 0.0))
    var isLocationGotten = MutableLiveData("false")
    var isGpsEnabled = MutableLiveData(GpsState(false))

    //for observation only

    val _location: LiveData<LocationModel> get() = locationMutable
    val _isLocationGotten: LiveData<String> get() = isLocationGotten
    val _isGpsEnabled: LiveData<GpsState> get() = isGpsEnabled


    init {
        request = LocationRequest()
        locationCallback = LocationCallback()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
        requestLocationUpdates()
    }

    //use suppress lint to ignore missing permission request, treat missing permission in fragment
    @SuppressLint("MissingPermission")
    fun requestLocationUpdates() {
        request = LocationRequest()
        request.interval = 60000

        //get user location using high accurate settings
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
                    locationMutable.value = LocationModel(longitude, latitude)
                    isLocationGotten.value = "true"

                }
                //location is null, use fused location client to request location update
                else {
                    fusedLocationClient.requestLocationUpdates(request, locationCallback, null)
                }

            }
        }
        fusedLocationClient.requestLocationUpdates(request, locationCallback, null)
        startLocationUpdates()
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