package com.trapezoidlimited.groundforce.ui.auth

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.data.GpsState
import com.trapezoidlimited.groundforce.databinding.FragmentLocationVerificationBinding
import com.trapezoidlimited.groundforce.utils.AppConstants
import com.trapezoidlimited.groundforce.utils.showDialog
import com.trapezoidlimited.groundforce.viewmodel.LocationViewModel
import kotlinx.android.synthetic.main.fragment_location_verification.*


class LocationVerificationFragment : Fragment(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {


    private var _binding: FragmentLocationVerificationBinding? = null
    private val binding get() = _binding!!

    //declare location request and google api client
    lateinit var mLocationRequest: LocationRequest
    lateinit var mGoogleApiClient: GoogleApiClient

    //declare locaiton view model
    private lateinit var locationViewModel: LocationViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //initialize location view model using view model providers
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)

        // Inflate the layout for this fragment
        _binding = FragmentLocationVerificationBinding.inflate(inflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initialize location request
        mLocationRequest = LocationRequest()
        //initialize google api client which will be used to trigger the turn on gps dialog
        mGoogleApiClient = GoogleApiClient.Builder(requireContext())
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        //onview created, trigger getLocation
        getLocation()
    }

    //while permission is granted, observe gps and check that it is turned on, else trigger gps
    @RequiresApi(Build.VERSION_CODES.M)
    fun getLocation() {
        if (isPermissionsGranted()) {
            //observe that gps is enabled, if not enables, trigger gps
            locationViewModel._isGpsEnabled.observe(viewLifecycleOwner) {
                if (!it.gpsGotten) {
                    triggerGPS()
                }
            }
            //request location of user if permission is granted
            locationViewModel.requestLocationUpdates()
            //observe the state wether it has been gotten, if it is false continue to show ripple else get the location latlng
            locationViewModel._isLocationGotten.observe(
                viewLifecycleOwner,
                Observer { locationM ->
                    if (locationM == "false") {
                        binding.layoutRipplepulse.startRippleAnimation()
                    } else {
                        //if location has been gotten, make animation or its visibility gone; while get users latlng
                        binding.layoutRipplepulse.stopRippleAnimation()
                        getLocationLatLng()
                    }
                })
        } else {
            //if permission not granted, request for permission
            requestPermission()
        }

        if (shouldShowRequestPermissionRationale()) {
            Toast.makeText(requireContext(), "permission not granted", Toast.LENGTH_LONG).show()
        }
    }

    //trigger gps
    private fun triggerGPS() {
        //connect and load the location service builder to the fragment; attach a callback listener and connection failed listener
        activity?.let {
            GoogleApiClient.Builder(it)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build()
        }?.connect()
    }

    //function to check wether permission has been granted by the user
    private fun isPermissionsGranted() =
        requireContext()?.applicationContext?.let {
            ActivityCompat.checkSelfPermission(
                it,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireContext().applicationContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED


    //request for permission
    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestPermission() {
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            AppConstants.LOCATION_REQUEST
        )
    }


    //if the permission has been granted, and the code is equal , then trigger get location
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            AppConstants.LOCATION_REQUEST -> {
                getLocation()
            }
        }
    }

    //when the user selects a response after the dialog for gps is displayed
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //if the user selects ok, then update the isGpsEnabled to be true
            if (requestCode == AppConstants.GPS_REQUEST) {
                //reset the state of gps enabled to true
                locationViewModel.isGpsEnabled.value = GpsState(true)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Location is turned off, Please turn on your location",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    //if permission is not granted, consider to show the permission not granted rationale
    @RequiresApi(Build.VERSION_CODES.M)
    private fun shouldShowRequestPermissionRationale() =
        shouldShowRequestPermissionRationale(

            Manifest.permission.ACCESS_FINE_LOCATION
        ) && ActivityCompat.shouldShowRequestPermissionRationale(
            requireContext().applicationContext as Activity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )


    //permission is granted, gps is turned on, now we can get the users lat long
    private fun getLocationLatLng() {
        locationViewModel._location.value.apply {
            verifying_location_status_tv.text =
                getString(R.string.latLong, this?.longitude, this?.latitude)
            this?.let {
                //get the custom alert dialog to display success since location is gotten
                showDialog(requireContext(), "Success!", "Congratulations")

            }
        }
    }


    // the function that actaullay shows the turn on gps dialog builder when its connected
    override fun onConnected(p0: Bundle?) {
        mLocationRequest = LocationRequest.create()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 30 * 1000
        mLocationRequest.fastestInterval = 5 * 1000

        //always show the builder if location service is requested
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest)
        builder.setAlwaysShow(true)

        //get result wether the builder was displayed
        var result =
            LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build())


        result.setResultCallback { result ->
            val status: Status = result.status
            when (status.statusCode) {
                //if the builder was displayed,
                LocationSettingsStatusCodes.SUCCESS -> {

                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                    // Location settings are not satisfied. But could be fixed by showing the user // a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(
                            activity,
                            AppConstants.LOCATION_REQUEST
                        )
                    } catch (e: SendIntentException) {
                        // Ignore the error.
                    }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                }
            }
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("Not yet implemented")

    }

    //on start, check if google api client has been created, if it has then connect the location service builder to the fragment activity
    override fun onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("Not yet implemented")
    }


    //on activity created, handle navigation
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        //Go to previous screen
        binding.locationVerificationBackNavButtonIv.setOnClickListener {
            findNavController().popBackStack()
        }
        super.onActivityCreated(savedInstanceState)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

//    override fun pageToNavigate(page: Int) {
//        Toast.makeText(requireContext(), page.toString(), Toast.LENGTH_SHORT).show()
////        findNavController().navigate(R.id.createPinFragment)
//    }


}