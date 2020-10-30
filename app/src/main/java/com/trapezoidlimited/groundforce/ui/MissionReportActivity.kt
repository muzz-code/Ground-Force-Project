package com.trapezoidlimited.groundforce.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.ActivityMissionReportBinding
import com.trapezoidlimited.groundforce.utils.MissionReportValidation

class MissionReportActivity : AppCompatActivity() {
    private val validate = MissionReportValidation()

    /** instantiate values**/
    lateinit var  nearestLandmark:String
    lateinit var  nearestBusStop:String
    lateinit var  typeOfStructure:String
    lateinit var additionalComments:String

    private lateinit var binding: ActivityMissionReportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMissionReportBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        /** set navigation arrow from drawable **/
        binding.activityMissionReportIc.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)

        binding.activityMissionReportGroup1Rg.checkedRadioButtonId

        /***save mission report**/
        binding.activityMissionReportSubmitBtn.setOnClickListener {
            if(!validateDetails()){
                return@setOnClickListener
            }
            else{
                Toast.makeText(this, R.string.report_saved_str, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateDetails(): Boolean{

        nearestLandmark = binding.activityMissionReportLandmarkTil.editText?.text.toString().trim()
        nearestBusStop = binding.activityMissionReportBusstopTil.editText?.text.toString().trim()
        typeOfStructure = binding.activityMissionReportStructureTil.editText?.text.toString().trim()
        additionalComments=binding.activityMissionReportCommentsTil.editText?.text.toString().trim()

        if(!validate.validateTextInput(nearestLandmark)){
            binding.activityMissionReportLandmarkTil.error ="Field cannot be empty"
            return false
        }else{
            binding.activityMissionReportLandmarkTil.error =null

        }
        if(!validate.validateTextInput(nearestBusStop)){
            binding.activityMissionReportBusstopTil.error = "Field cannot be empty"
            return false
        }
        else{
            binding.activityMissionReportBusstopTil.error = null
        }
        if(!validate.validateTextInput(typeOfStructure)){
            binding.activityMissionReportStructureTil.error = "Field cannot be empty"
            return false
        }
        else{
            binding.activityMissionReportStructureTil.error = null
        }
        if(!validate.validateTextInput(additionalComments)){
            binding.activityMissionReportCommentsTil.error ="Field cannot be empty"
            return false
        }else{
            binding.activityMissionReportCommentsTil.error =null
        }


        return true


    }
}