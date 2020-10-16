package com.trapezoidlimited.groundforce.utils

import android.app.Application
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.IntentSender.SendIntentException
import android.location.LocationManager
import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.trapezoidlimited.groundforce.data.GpsState
import com.trapezoidlimited.groundforce.ui.LocationVerificationFragment

//load by lazy

class GpsUtils(context: Context) {
    var gpsModel=GpsState(false)
    lateinit var context:Context
    var fragment=LocationVerificationFragment::class.java
    private val mSettingsClient: SettingsClient = LocationServices.getSettingsClient(context)
    private val mLocationSettingsRequest: LocationSettingsRequest
    private val locationManager: LocationManager by lazy{ context.getSystemService(Context.LOCATION_SERVICE) as LocationManager}
    private val locationRequest: LocationRequest by lazy { LocationRequest.create() }

    // method for turn on GPS
    fun turnGPSOn(){
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gpsModel.gpsGotten=true
        } else {

            mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener { //  GPS is already enable, callback GPS status through listener
                    //save the result using data class
                    gpsModel.gpsGotten=true
                }
                .addOnFailureListener{ e ->
                    when ((e as ApiException).statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {

                            gpsModel.gpsGotten = false
                        } catch (sie: SendIntentException) {
                            gpsModel.gpsGotten = false
                            Log.i("PendingIntentGPS", "PendingIntent unable to execute request.")
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            val errorMessage = "Location settings are inadequate, and cannot be " +
                                    "fixed here. Fix in Settings."
                            Log.e("GPSSettingsInadequate", errorMessage)
                            gpsModel.gpsGotten = false

                        }
                    }
                }
        }
    }

    //return the result of the data i.e true
    fun returnGpsState(): GpsState {
        return gpsModel
    }



    init {
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10 * 1000.toLong()
        locationRequest.fastestInterval = 2 * 1000.toLong()
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        mLocationSettingsRequest = builder.build()

        //**************************
        builder.setAlwaysShow(true) //this is the key ingredient
        //**************************
    }
}