package com.trapezoidlimited.groundforce.ui.dashboard

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.trapezoidlimited.groundforce.EntryApplication
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.api.ApiService
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.databinding.ActivityMissionReportBinding
import com.trapezoidlimited.groundforce.model.request.SubmitMissionRequest
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.utils.*
import com.trapezoidlimited.groundforce.viewmodel.MissionsViewModel
import com.trapezoidlimited.groundforce.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MissionReportActivity : AppCompatActivity() {
    private val validate = MissionReportValidation()

    @Inject
    lateinit var loginApiServiceService: ApiService

    @Inject
    lateinit var missionsApi: MissionsApi

    @Inject
    lateinit var retrofit: Retrofit

    private lateinit var viewModel: MissionsViewModel

    private val roomViewModel by lazy { EntryApplication.viewModel(this) }

    private lateinit var missionId: String
    private var missionPosition: Int = 0
    private val LOCATION_PERMISSION_REQUEST = 1
    private val LOCATION_REQUEST_CODE = 1
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    /** instantiate values**/
    private lateinit var nearestLandmark: String
    private lateinit var nearestBusStop: String
    private lateinit var typeOfStructure: String
    private lateinit var additionalComments: String
    private lateinit var buildingTypeId: String
    private var latitude: String = ""
    private var longitude: String = ""
    private  var position: Int = 0

    private lateinit var binding: ActivityMissionReportBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMissionReportBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        /** initializing missionViewModel **/

        val repository = AuthRepositoryImpl(loginApiServiceService, missionsApi)
        val factory = ViewModelFactory(repository, this)

        viewModel = ViewModelProvider(this, factory).get(MissionsViewModel::class.java)


        val bundle: Bundle? = intent.extras

        if (bundle != null) {
            missionId = bundle.getString("MISSIONID")!!
            missionPosition = bundle.getInt("POSITION")
        }


        /** Getting the current location **/
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest().apply {
            interval = TimeUnit.SECONDS.toMillis(1000)
            fastestInterval = TimeUnit.SECONDS.toMillis(2000)
            maxWaitTime = TimeUnit.MINUTES.toMillis(1)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }


        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)

                try{

                    if (locationResult?.lastLocation != null) {
                        latitude = locationResult.lastLocation.latitude.toString()
                        longitude = locationResult.lastLocation.longitude.toString()

                        val coordinate = "$latitude and $longitude"
                        val coordinateEt = SpannableStringBuilder(coordinate)
                        binding.activityMissionReportCoordTil.editText?.text = coordinateEt


                    } else {
                        Log.d("LOCATION", "Location missing in callback.")
                        unsubscribeToLocationUpdates()

                    }
                    unsubscribeToLocationUpdates()

                } catch (e: Exception) {
                    unsubscribeToLocationUpdates()
                }

            }

            override fun onLocationAvailability(p0: LocationAvailability?) {
                Toast.makeText(
                    this@MissionReportActivity.applicationContext,
                    "location service available",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        /** requesting location permission **/
        requestLocationPermission()



        /** Network call to get building type **/

        viewModel.getBuildingType(1)


        /** validating fields **/

        validateFields()


        binding.activityMissionReportGroup1Rg.checkedRadioButtonId

        /** setting toolbar text **/
        binding.fragmentMissionReportToolBarLl.toolbarTitle.text = getString(R.string.mission_report_title_str)

        /** set navigation arrow from drawable **/
        binding.fragmentMissionReportToolBarLl.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)

        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentMissionReportToolBarLl.toolbarFragment.setNavigationOnClickListener {
            finish()
        }


        val buildingTypeList = mutableListOf<String>()
        val buildingTypeIDList = mutableListOf<String>()

        viewModel.getBuildingTypeResponse.observe(this, {
            when (it) {
                is Resource.Success ->{
                    val buildingTypeObjectList = it.value.data?.data

                    if (buildingTypeObjectList != null) {


                        for (buildingTypeObject in buildingTypeObjectList) {

                            val buildingTypeName = buildingTypeObject.typeName
                            val buildingTypeID = buildingTypeObject.typeId

                            buildingTypeList.add(buildingTypeName)
                            buildingTypeIDList.add(buildingTypeID)

                        }

                    }
                }
                is Resource.Failure -> {
                   handleApiError(it, retrofit, view)
                }
            }
        })


        val addressExists = binding.activityMissionReportNoOneRb.isChecked || binding.activityMissionReportYesOneRb.isChecked


        viewModel.submitMissionResponse.observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.activityMissionReportPb.visibility = View.GONE
                    binding.activityMissionReportSubmitBtn.isEnabled = true

                    Toast.makeText(this, "Submitted", Toast.LENGTH_SHORT).show()

                    /*** Making network call to submit mission **/
                    roomViewModel.deleteByOngoingMissionId(missionId)

                    Intent(this, DashboardActivity::class.java).apply {
                        startActivity(this)
                        finish()
                    }

                }
                is Resource.Failure -> {

                    Toast.makeText(this, "Not Submitted", Toast.LENGTH_SHORT).show()

                    binding.activityMissionReportPb.visibility = View.GONE
                    binding.activityMissionReportSubmitBtn.isEnabled = true
                    handleApiError(it, retrofit, view)
                }
            }
        })


        /*** Populating the dropdown with list of building type **/

        val adapterBuildingType = ArrayAdapter(this, R.layout.list_item, buildingTypeList)

        (binding.activityMissionReportStructureTil.editText as? AutoCompleteTextView)?.setAdapter(
            adapterBuildingType
        )



        binding.activityMissionReportSubmitBtn.setOnClickListener {


            /*** creating submit mission object **/

            nearestLandmark = binding.activityMissionReportLandmarkTil.editText?.text.toString()
            nearestBusStop = binding.activityMissionReportBusstopTil.editText?.text.toString()
            typeOfStructure = binding.activityMissionReportStructureTil.editText?.text.toString()
            additionalComments =
                binding.activityMissionReportCommentsTil.editText?.text.toString()


            position = buildingTypeList.indexOf(typeOfStructure)

            buildingTypeId = buildingTypeIDList[position]


            if (latitude.trim().isEmpty() && longitude.trim().isEmpty()) {

                showSnackBar(binding.activityMissionReportSubmitBtn, "Unable to obtain coordinates. Ensure location is on.")

            } else {

                binding.activityMissionReportPb.visibility = View.VISIBLE

                binding.activityMissionReportSubmitBtn.isEnabled = false

                val submitMissionRequest = SubmitMissionRequest(
                    missionId,
                    buildingTypeId,
                    nearestLandmark,
                    nearestBusStop,
                    "Dark Green",
                    addressExists,
                    typeOfStructure,
                    longitude,
                    latitude,
                    additionalComments
                )

                /*** Making network call to submit mission **/

                viewModel.submitMission(submitMissionRequest)
            }

        }


        binding.activityMissionReportCancelBtn.setOnClickListener {

            Intent(this, DashboardActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }


    }

    /** method to request location permission **/
    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST
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



    /** method to subscribe to location updates **/
    private fun subscribeToLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.getMainLooper()
        )

    }

    /** method to check if GPS is enabled and request user to turn on gps **/

    private fun checkGPSEnabled() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)


        val client: SettingsClient = LocationServices.getSettingsClient(this)
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
                    AlertDialog.Builder(this)
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
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LOCATION_REQUEST_CODE) {
            subscribeToLocationUpdates()

        } else {
            showSnackBar(binding.activityMissionReportFirstHintTv, "Something is wrong! GPS is off.")
        }
    }


    /** method to observe the result request location permission **/

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when (requestCode) {
            LOCATION_PERMISSION_REQUEST -> when {
                grantResults.isEmpty() -> Toast.makeText(
                    this,
                    "Access not granted",
                    Toast.LENGTH_LONG
                ).show()
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    checkGPSEnabled()
                }
                else -> {

                    AlertDialog.Builder(this)
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





    private fun validateFields() {
        val fields: MutableList<JDataClass> = mutableListOf(
            JDataClass(
                editText = binding.activityMissionReportLandmarkEt,
                editTextInputLayout = binding.activityMissionReportLandmarkTil,
                errorMessage = JDErrorConstants.NAME_FIELD_ERROR,
                validator = { it.jdValidateLongText(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.activityMissionReportBusstopEt,
                editTextInputLayout = binding.activityMissionReportBusstopTil,
                errorMessage = JDErrorConstants.NAME_FIELD_ERROR,
                validator = { it.jdValidateLongText(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.activityMissionReportStructureEt,
                editTextInputLayout = binding.activityMissionReportStructureTil,
                errorMessage = JDErrorConstants.NAME_FIELD_ERROR,
                validator = { it.jdValidateLongText(it.text.toString()) }
            )
        )

        JDFormValidator.Builder()
            .addFieldsToValidate(fields)
            .removeErrorIcon(true)
            .viewsToEnable(mutableListOf(binding.activityMissionReportSubmitBtn))
            .watchWhileTyping(true)
            .build()
    }



}