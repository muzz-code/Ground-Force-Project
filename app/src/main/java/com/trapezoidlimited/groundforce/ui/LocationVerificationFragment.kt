package com.trapezoidlimited.groundforce.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
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
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.data.GpsState
import com.trapezoidlimited.groundforce.databinding.FragmentLocationVerificationBinding
import com.trapezoidlimited.groundforce.utils.AppConstants
import com.trapezoidlimited.groundforce.utils.CustomAlert
import com.trapezoidlimited.groundforce.viewmodel.LocationViewModel
import kotlinx.android.synthetic.main.fragment_location_verification.*


class LocationVerificationFragment : Fragment() {
    private var _binding: FragmentLocationVerificationBinding? = null
    private val binding get() = _binding!!

    //set location request code to 101
    var LOCATION_REQUEST=101


    //check if Gps is enabled
    private var isGpsEnabled=false
    //check if check of location should continue
    private var isContinue: Boolean = false

    private lateinit var locationViewModel: LocationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //initialize location view model using view model providers
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)
        // Inflate the layout for this fragment

        _binding= FragmentLocationVerificationBinding.inflate(inflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //onview created, trigger getLocation
        getLocation()
        super.onViewCreated(view, savedInstanceState)
    }

    //if permission is granted, begin request for location updates
    //observe that location is gotten
    @RequiresApi(Build.VERSION_CODES.M)
    fun getLocation(){
        if(isPermissionsGranted()) {
            //request location of user
            locationViewModel.requestLocationUpdates()

            locationViewModel._isLocationGotten.observe(viewLifecycleOwner, Observer { locationM ->
                if (locationM == "false") {
                    binding.layoutRipplepulse.startRippleAnimation()
                    //check that the gps was turned on while it is still searching for the users location
                    locationViewModel._isGpsEnabled.observe(viewLifecycleOwner) {
                        if(!it.gpsGotten)
                        Toast.makeText(
                            requireContext(),
                            "Please turn on GPS",
                            Toast.LENGTH_SHORT
                        ).show();
                    }
                    //still continue to listen to location of user expecting the user to turn on gps
                    locationViewModel.requestLocationUpdates()
                } else {
                    //if location has been gotten, make animation or its visibility gone; while trigger compareLocationResultWithUsersInputAddress
                    binding.layoutRipplepulse.stopRippleAnimation()
                    getLocationLatLng()
                }
            })
        }
        else{
            //if permission not granted, request for permission
            requestPermission()
        }
        if(shouldShowRequestPermissionRationale()){
            Toast.makeText(requireContext(), "permission not granted", Toast.LENGTH_LONG).show()
        }
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




    //if the permission has been granted, compare location result with user's input location


    //request for permission  and then trigger get location
    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestPermission(){
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_REQUEST
        )
    }


    //if the permission has been granted, and the code is equal , the trigger get location
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST -> {
                getLocation()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == AppConstants.GPS_REQUEST) {
                    locationViewModel.isGpsEnabled.value = GpsState(true) // flag maintain before get location
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
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


    //begin compareLocationResultWithUsersInputAddress and display custom alert
    private fun getLocationLatLng(){
       locationViewModel._location.value.apply{
            verifying_location_status_tv.text =
                getString(R.string.latLong, this?.longitude, this?.latitude)
            this?.let {
                var alertDialog=CustomAlert()
                alertDialog.showDialog(requireContext(), "Congratulations", "Success")
            }
        }
    }


    //on activity created, handle navigation

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        //Go to previous screen
        binding.locationVerificationBackNavButtonIv.setOnClickListener {
            findNavController().popBackStack()
        }
        super.onActivityCreated(savedInstanceState)
    }



}