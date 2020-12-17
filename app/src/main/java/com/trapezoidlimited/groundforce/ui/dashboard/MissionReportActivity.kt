package com.trapezoidlimited.groundforce.ui.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
    private lateinit var latitude: String
    private lateinit var longitude: String

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

        viewModel.getBuildingTypeResponse.observe(this, {
            when (it) {
                is Resource.Success ->{
                    val buildingTypeObjectList = it.value.data?.data

                    if (buildingTypeObjectList != null) {


                        for (buildingTypeObject in buildingTypeObjectList) {

                            val buildingTypeName = buildingTypeObject.typeName

                            buildingTypeList.add(buildingTypeName)

                        }

                    }
                }
                is Resource.Failure -> {
                   handleApiError(it, retrofit, view)
                }
            }
        })




        val addressExists = binding.activityMissionReportNoOneRb.isChecked || binding.activityMissionReportYesOneRb.isChecked


        viewModel.updateMissionStatusResponse.observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    Toast.makeText(this, "Verified", Toast.LENGTH_SHORT).show()

                    val submitMissionRequest = SubmitMissionRequest(
                        missionId,
                        "7facf1a8-dd39-4c56-967d-a24786e31301",
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

                    Log.i("MISSIONID", missionId)

                    Log.i("POSITION", "$missionPosition")

                }
                is Resource.Failure -> {
                    binding.activityMissionReportPb.visibility = View.GONE
                    handleApiError(it, retrofit, view)
                }
            }

        })



        viewModel.submitMissionResponse.observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.activityMissionReportPb.visibility = View.GONE
                    binding.activityMissionReportSubmitBtn.isEnabled = true

                    Toast.makeText(this, "Submit", Toast.LENGTH_SHORT).show()

                    /*** Making network call to submit mission **/
                    roomViewModel.deleteByOngoingMissionId(missionId)

                    Intent(this, DashboardActivity::class.java).apply {
                        startActivity(this)
                        finish()
                    }

                }
                is Resource.Failure -> {
                    binding.activityMissionReportPb.visibility = View.GONE
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



            /*** Making network call to verified mission **/

            viewModel.updateMissionStatus(missionId, "verified")



            /*** creating submit mission object **/

            nearestLandmark = binding.activityMissionReportLandmarkTil.editText?.text.toString()
            nearestBusStop = binding.activityMissionReportBusstopTil.editText?.text.toString()
            typeOfStructure = binding.activityMissionReportStructureTil.editText?.text.toString()
            additionalComments =
                binding.activityMissionReportCommentsTil.editText?.text.toString()

            latitude = loadFromSharedPreference(this, LATITUDE)
            longitude = loadFromSharedPreference(this, LONGITUDE)



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