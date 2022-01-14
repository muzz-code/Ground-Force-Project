package com.trapezoidlimited.groundforce.ui.auth

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.api.ApiService
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.databinding.FragmentLocationsVerificationBinding
import com.trapezoidlimited.groundforce.model.request.AgentDataRequest
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.utils.*
import com.trapezoidlimited.groundforce.viewmodel.AuthViewModel
import com.trapezoidlimited.groundforce.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_locations_verification.*
import retrofit2.Retrofit
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@AndroidEntryPoint
class LocationsVerificationFragment : Fragment() {

    @Inject
    lateinit var loginApiServiceService: ApiService

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var missionsApi: MissionsApi

    private lateinit var viewModel: AuthViewModel

    private var _binding: FragmentLocationsVerificationBinding? = null
    private val binding get() = _binding!!
    private val LOCATION_PERMISSION_REQUEST = 1
    private val LOCATION_REQUEST_CODE = 1
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var shortAnimationDuration: Int = 0
    private lateinit var skipProgressBar: ProgressBar
    private lateinit var skipButton: Button
    private lateinit var agentData: AgentDataRequest
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var geoPointLat: Double = 0.0
    private var geoPointLong: Double = 0.0


    private val args: LocationsVerificationFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLocationsVerificationBinding.inflate(inflater, container, false)

        val repository = AuthRepositoryImpl(loginApiServiceService, missionsApi)
        val factory = ViewModelFactory(repository, requireContext())
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        /** initializing views */
        skipProgressBar = binding.fragmentLocationSkipPg
        skipButton = binding.fragmentLocationVerificationSkipBtn

        /** setting toolbar text **/
        binding.fragmentLocationVerificationTb.toolbarTitle.text =
            getString(R.string.location_verification_title_str)

        binding.fragmentLocationVerificationTb.toolbarTitle.setTextColor(resources.getColor(R.color.colorWhite))

        /** set navigation arrow from drawable **/

        binding.fragmentLocationVerificationTb.toolbarTransparentFragment.setNavigationIcon(R.drawable.ic_arrow_white_back)

        /**
         * Get the duration of the animation
         */
        shortAnimationDuration = resources.getInteger(android.R.integer.config_longAnimTime)

        /**
         * Starts the animation for the textview
         */
        binding.verifyingLocationStatusTv.crossShow(shortAnimationDuration.toLong())

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        DataListener.currentScreen = LOCATION_VERIFICATION_SCREEN


        val geoPoints = args.geopoints

        geoPointLat = geoPoints?.latitude!!
        geoPointLong = geoPoints.longitude!!


        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentLocationVerificationTb.toolbarTransparentFragment.setNavigationOnClickListener {
            //findNavController().navigate(R.id.createProfileFragmentOne)
            findNavController().popBackStack()
        }

        /** Observing results from agent creation network call**/

        viewModel.agentCreationResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {

                    val userId = it.value.data?.loginToken?.id
                    val userToken = it.value.data?.loginToken?.token

                    /**Saving user's id to sharedPreference */
                    SessionManager.save(requireContext(), USERID, userId!!)

                    /** Saving token to sharedPreference */

                    SessionManager.save(requireContext(), TOKEN, userToken!!)

                    /** Saving LAT and LONG in sharedPreference*/
                    saveToSharedPreference(requireActivity(), LATITUDE, latitude.toString())
                    saveToSharedPreference(requireActivity(), LONGITUDE, longitude.toString())
//                    saveToSharedPreference(requireActivity(), LOCATION_VERIFICATION, "true")
                    saveToSharedPreference(requireActivity(), IS_LOCATION_VERIFIED, "false")


                    setInVisibility(skipProgressBar)
                    findNavController().navigate(R.id.waitingFragment)

                }
                is Resource.Failure -> {

                    setInVisibility(skipProgressBar)
                    skipButton.isEnabled = true
                    handleApiError(it, retrofit, skipButton)
                }
            }
        })


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

                try {

                    if (locationResult?.lastLocation != null) {
                        val lat = locationResult.lastLocation.latitude.toString()
                        val long = locationResult.lastLocation.longitude.toString()
                        latitude = locationResult.lastLocation.latitude
                        longitude = locationResult.lastLocation.longitude


                        binding.verifyingLocationStatusTv.text = "Latitude: $lat, Longitude: $long"


                        if ((geoPointLat - latitude).absoluteValue < 0.1 && (geoPointLong - longitude).absoluteValue < 0.1) {

                            SessionManager.save(requireContext(), LATITUDE, latitude.toString())
                            SessionManager.save(requireContext(), LONGITUDE, longitude.toString())

                            setSuccessDialog()
                        } else {

                            Toast.makeText(
                                requireContext(),
                                "Address Mismatch. Check address input.",
                                Toast.LENGTH_SHORT
                            ).show()

                            showFailedDialog()
                        }


                    } else {
                        Log.d("LOCATION", "Location missing in callback.")
                        unsubscribeToLocationUpdates()
                        showFailedDialog()
                    }

                    unsubscribeToLocationUpdates()

                } catch (e: Exception) {
                    unsubscribeToLocationUpdates()
                    showFailedDialog()
                }
            }

            override fun onLocationAvailability(p0: LocationAvailability?) {
//                Log.i("GPSSTATUSCHANGE", "GPS is ${p0?.isLocationAvailable ?: false}")
                Toast.makeText(
                    requireContext(),
                    "location service available",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        /** requesting location permission **/
        requestLocationPermission()


        /** setting the welcome dialog when user clicks skip for now **/

        skipButton.setOnClickListener {


            saveToSharedPreference(requireActivity(), LATITUDE, "")
            saveToSharedPreference(requireActivity(), LONGITUDE, "")
            saveToSharedPreference(requireActivity(), LGA, "Nil")
            saveToSharedPreference(requireActivity(), STATE, "Nil")
            saveToSharedPreference(requireActivity(), ADDRESS, "Nil")

            //findNavController().navigate(R.id.createProfileFragmentTwo)


            val lastName = loadFromSharedPreference(requireActivity(), LASTNAME)
            val firstName = loadFromSharedPreference(requireActivity(), FIRSTNAME)
            val phoneNumber = loadFromSharedPreference(requireActivity(), PHONE)
            val gender = loadFromSharedPreference(requireActivity(), GENDER)
            val dob = loadFromSharedPreference(requireActivity(), DOB)
            val email = loadFromSharedPreference(requireActivity(), EMAIL)
            val password = loadFromSharedPreference(requireActivity(), PASSWORD)
            val residentialAddress = loadFromSharedPreference(requireActivity(), ADDRESS)
            val state = loadFromSharedPreference(requireActivity(), STATE)
            val lga = loadFromSharedPreference(requireActivity(), LGA)
            val zipCode = loadFromSharedPreference(requireActivity(), ZIPCODE)
            val longitude = loadFromSharedPreference(requireActivity(), LONGITUDE)
            val latitude = loadFromSharedPreference(requireActivity(), LATITUDE)


            agentData = AgentDataRequest(
                lastName = lastName,
                firstName = firstName,
                phoneNumber = phoneNumber,
                gender = gender,
                dob = dob,
                email = email,
                password = password,
                residentialAddress = residentialAddress,
                state = state,
                lga = lga,
                zipCode = zipCode,
                longitude = longitude,
                latitude = latitude,
                roles = listOf("agent")
            )


            setVisibility(skipProgressBar)

            skipButton.isEnabled = false

            viewModel.registerAgent(agentData)

        }

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


