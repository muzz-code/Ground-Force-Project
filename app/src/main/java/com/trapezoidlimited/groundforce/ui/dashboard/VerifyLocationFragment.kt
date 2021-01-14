package com.trapezoidlimited.groundforce.ui.dashboard

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.api.ApiService
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.databinding.FragmentVerifyLocationBinding
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.utils.*
import com.trapezoidlimited.groundforce.viewmodel.AuthViewModel
import com.trapezoidlimited.groundforce.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class VerifyLocationFragment : Fragment() {

    @Inject
    lateinit var loginApiServiceService: ApiService

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var missionsApi: MissionsApi

    private lateinit var viewModel: AuthViewModel

    private var _binding: FragmentVerifyLocationBinding? = null
    private val binding
        get() = _binding!!

    private val LOCATION_PERMISSION_REQUEST = 1
    private val LOCATION_REQUEST_CODE = 1


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: Location? = null
    private var shortAnimationDuration: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentVerifyLocationBinding.inflate(inflater, container, false)
        val repository = AuthRepositoryImpl(loginApiServiceService, missionsApi)
        val factory = ViewModelFactory(repository, requireContext())
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)



        /** setting toolbar text **/
        binding.fragmentVerifyLocationTb.toolbarTitle.text =
            getString(R.string.location_verification_title_str)

        binding.fragmentVerifyLocationTb.toolbarTitle.setTextColor(resources.getColor(R.color.colorWhite))

        /** set navigation arrow from drawable **/

        binding.fragmentVerifyLocationTb.toolbarTransparentFragment.setNavigationIcon(R.drawable.ic_arrow_white_back)

        /**
         * Get the duration of the animation
         */
        shortAnimationDuration = resources.getInteger(android.R.integer.config_longAnimTime)

        /**
         * Starts the animation for the textview
         */
        binding.verifyLocationStatusTv.crossShow(shortAnimationDuration.toLong())

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        DataListener.currentScreen = VERIFY_LOCATION_SCREEN

        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentVerifyLocationTb.toolbarTransparentFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        /** Getting the current location **/
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        locationRequest = LocationRequest().apply {
            interval = TimeUnit.SECONDS.toMillis(1000)
            fastestInterval = TimeUnit.SECONDS.toMillis(2000)
            maxWaitTime = TimeUnit.MINUTES.toMillis(1)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)

                if (locationResult?.lastLocation != null) {
                    var lat = locationResult.lastLocation.latitude.toString()
                    var long = locationResult.lastLocation.longitude.toString()
                    currentLocation = locationResult.lastLocation

                    binding.verifyLocationStatusTv.text = "Latitude: $lat, Longitude: $long"


                    /** Saving LAT and LONG in sharedPreference*/

                    saveToSharedPreference(requireActivity(), LATITUDE, lat)
                    saveToSharedPreference(requireActivity(), LONGITUDE, long)

                    /** TO DO */
                    setSuccessDialog()


                } else {
                    Log.d("LOCATION", "Location missing in callback.")
                    showFailedDialog()
                }
            }

            override fun onLocationAvailability(p0: LocationAvailability?) {
                Log.i("GPSSTATUSCHANGE", "GPS is ${p0?.isLocationAvailable ?: false}")
            }
        }

        /** requesting location permission **/
        requestLocationPermission()

    }


    /** method to subscribe to location updates **/
    private fun subscribeToLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.getMainLooper()
        )
    }

    /** method to unsubscribe to location updates **/
    fun unsubscribeToLocationUpdates() {
        val removeTask = fusedLocationProviderClient.removeLocationUpdates(locationCallback)

        removeTask.addOnCompleteListener {
            if (it.isSuccessful) {
                onStop()
            } else {
                Log.d("UNSUBSCRIBE", "Unable to unsubscribe location updates")
            }
        }
    }

    /** method to request location permission **/
    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST
        )
    }

    /** method to check if GPS is enabled and request user to turn on gps **/

    private fun checkGPSEnabled() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)


        val client: SettingsClient = LocationServices.getSettingsClient(requireActivity())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { locationSettingsResponse ->
            subscribeToLocationUpdates()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    /** Make alert dialog to request user to turn on GPS**/
                    AlertDialog.Builder(requireContext())
                        .setTitle("GPS Settings")
                        .setMessage("GPS is off. App requires location turned on for verification.")
                        .setPositiveButton(
                            "SETTINGS"
                        ) { dialogInterface, i ->
                            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            startActivityForResult(intent, LOCATION_REQUEST_CODE)
                        }
                        .setNegativeButton(
                            "Cancel"
                        ) { dialogInterface, i ->
                            dialogInterface.cancel()
                        }
                        .create()
                        .show()

                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }

    }

    /** if the GPS is turned on, location update is subscribed to **/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == LOCATION_REQUEST_CODE) {
            subscribeToLocationUpdates()

        } else {
            showSnackBar(requireView(), "Something is wrong! GPS is off.")
        }
    }


    /** method to observe the result request location permission **/

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when (requestCode) {
            LOCATION_PERMISSION_REQUEST -> when {
                grantResults.isEmpty() -> Toast.makeText(
                    requireContext(),
                    "Access not granted",
                    Toast.LENGTH_LONG
                ).show()
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    checkGPSEnabled()
                }
                else -> {

                    AlertDialog.Builder(requireContext())
                        .setTitle("Permission For Location")
                        .setMessage("App requires permission to use the device's location to verify location.")
                        .setPositiveButton(
                            "OK",
                            DialogInterface.OnClickListener { dialogInterface, i ->
                                requestPermissions(
                                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                    LOCATION_PERMISSION_REQUEST
                                )
                            }).create()
                        .show()
                }
            }
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}