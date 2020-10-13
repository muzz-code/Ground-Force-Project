package com.trapezoidlimited.groundforce.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.utils.CustomAlert
import com.trapezoidlimited.groundforce.viewmodel.LocationViewModel
import kotlinx.android.synthetic.main.fragment_location_verification.*

class LocationVerificationFragment : Fragment() {

    var LOCATION_REQUEST=101
    var userLat=3.630
    var userLong=6.474


    private lateinit var locationViewModel: LocationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)
        return inflater.inflate(R.layout.fragment_location_verification, container, false)
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
            locationViewModel.requestLocationUpdates()
            locationViewModel._isLocationGotten.observe(viewLifecycleOwner, Observer {
                if (it == "false") {
                    progressBar.visibility = View.VISIBLE
                    locationViewModel.requestLocationUpdates()
                } else {
                    //if location has been gotten, make animation or its visibility gone; while trigger compareLocationResultWithUsersInputAddress
                    progressBar.visibility = View.GONE
                    compareLocationResultWithUsersInputAddress()
                }
            })
        }
        else{
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
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_REQUEST
        )
        getLocation()
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


    //if permission is not granted, consider to show the permission not granted rationale
    @RequiresApi(Build.VERSION_CODES.M)
    private fun shouldShowRequestPermissionRationale() =
        requireActivity().shouldShowRequestPermissionRationale(

            Manifest.permission.ACCESS_FINE_LOCATION
        ) && ActivityCompat.shouldShowRequestPermissionRationale(
            requireContext().applicationContext as Activity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )


    //begin compareLocationResultWithUsersInputAddress and display custom alert
    private fun compareLocationResultWithUsersInputAddress(){
        locationViewModel._location.observe(viewLifecycleOwner) {
            verifying_location_status_tv.text =
                getString(R.string.latLong, it.longitude, it.latitude)
            CustomAlert.dialog(requireActivity(), "Congratulations", "Success")
        }
    }






}