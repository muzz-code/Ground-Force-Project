package com.trapezoidlimited.groundforce.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.data.LocationModel
import com.trapezoidlimited.groundforce.databinding.FragmentLocationVerificationBinding
import com.trapezoidlimited.groundforce.utils.CustomAlert
import com.trapezoidlimited.groundforce.viewmodel.LocationViewModel
import kotlinx.android.synthetic.main.fragment_location_verification.*
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin


class LocationVerificationFragment : Fragment() {
    private var _binding: FragmentLocationVerificationBinding? = null
    private val binding get() = _binding!!

    //set location request code to 101
    var LOCATION_REQUEST=101

    //sample user latitude and longitude which is intended to be used to test that address input by user is near proximity of
    //location gotten by google location service or vice versa
    var userLat=3.630
    var userLong=6.474

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
            locationViewModel.requestLocationUpdates()
            locationViewModel._isLocationGotten.observe(viewLifecycleOwner, Observer {
                if (it == "false") {
                    binding.layoutRipplepulse.startRippleAnimation()
                    locationViewModel.requestLocationUpdates()
                } else {
                    //if location has been gotten, make animation or its visibility gone; while trigger compareLocationResultWithUsersInputAddress
                    binding.layoutRipplepulse.stopRippleAnimation()
                    getLocationLatLng()
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
    private fun getLocationLatLng(){

       locationViewModel._location.value.apply{
            verifying_location_status_tv.text =
                getString(R.string.latLong, this?.longitude, this?.latitude)
            this?.let { compareLocationResultWithUsersInputAddress(it) }

        }
    }

    private fun compareLocationResultWithUsersInputAddress(locationModel:LocationModel){
        var distanceBetweenUserAddressAndGoogleAddress=distanceBetweenUsersEnteredAddressAndGoogleLatLng(
            userLat,
            locationModel.latitude,
            userLong,
            locationModel.longitude
        )
        if(distanceBetweenUserAddressAndGoogleAddress < 1000.0){
            var alertDialog=CustomAlert()
            alertDialog.showDialog(requireContext(), "Congratulations", "Success")

        }
        else{
            Toast.makeText(
                requireContext(),
                "You should be near the address your registered with",
                Toast.LENGTH_LONG
            ).show()
        }

    }




    private fun distanceBetweenUsersEnteredAddressAndGoogleLatLng(
        userLat: Double,
        userLong: Double,
        GoogleLat: Double,
        GoogleLng: Double
    ): Double {
        //calculate the difference
        var diffInLong=userLong-GoogleLng

        var distance= sin(deg2Rad(userLat)) *
                sin(deg2Rad(GoogleLat)) +
                cos(deg2Rad(userLat)) *
                cos(deg2Rad(GoogleLat)) *
                cos(deg2Rad(diffInLong))
        distance= acos(distance)

        //convert distance radian to degree
        distance=rad2deg(distance)

        //distance in miles
        distance *= 60 * 1.1515

        //distance in metres
        distance *= 1.609344

        //distance in 2decimal place

        return String.format("%.2f", distance).toDouble()


    }

    private fun deg2Rad(userLat: Double): Double {
        return (userLat * Math.PI/180)
    }

    private fun rad2deg(distance: Double): Double{
        return (distance * 180/ Math.PI)
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