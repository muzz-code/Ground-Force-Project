package com.trapezoidlimited.groundforce.ui.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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

    /** instantiate values**/
    private lateinit var nearestLandmark: String
    private lateinit var nearestBusStop: String
    private lateinit var typeOfStructure: String
    private lateinit var additionalComments: String
    private lateinit var buildingTypeId: String
    private lateinit var latitude: String
    private lateinit var longitude: String
    private  var position: Int = 0

    private lateinit var binding: ActivityMissionReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMissionReportBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        /** initializing missionViewModel **/

        val repository = AuthRepositoryImpl(loginApiServiceService, missionsApi)
        val factory = ViewModelFactory(repository, this)

        viewModel = ViewModelProvider(this, factory).get(MissionsViewModel::class.java)

        latitude = loadFromSharedPreference(this, LATITUDE)
        longitude = loadFromSharedPreference(this, LONGITUDE)

        val coordinate = "$latitude and $longitude"
        val coordinateEt = SpannableStringBuilder(coordinate)

        binding.activityMissionReportCoordTil.editText?.text = coordinateEt


        val bundle: Bundle? = intent.extras

        if (bundle != null) {
            missionId = bundle.getString("MISSIONID")!!
            missionPosition = bundle.getInt("POSITION")
        }


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

            binding.activityMissionReportPb.visibility = View.VISIBLE

            binding.activityMissionReportSubmitBtn.isEnabled = false


            /*** creating submit mission object **/

            nearestLandmark = binding.activityMissionReportLandmarkTil.editText?.text.toString()
            nearestBusStop = binding.activityMissionReportBusstopTil.editText?.text.toString()
            typeOfStructure = binding.activityMissionReportStructureTil.editText?.text.toString()
            additionalComments =
                binding.activityMissionReportCommentsTil.editText?.text.toString()


            position = buildingTypeList.indexOf(typeOfStructure)

            buildingTypeId = buildingTypeIDList[position]


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



        binding.activityMissionReportCancelBtn.setOnClickListener {

            Intent(this, DashboardActivity::class.java).apply {
                startActivity(this)
                finish()
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