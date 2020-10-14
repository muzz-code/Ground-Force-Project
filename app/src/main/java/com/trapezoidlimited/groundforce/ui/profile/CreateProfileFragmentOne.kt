package com.trapezoidlimited.groundforce.ui.profile

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentCreateProfileOneBinding
import kotlinx.android.synthetic.main.fragment_create_profile_one.*
import java.util.*


class CreateProfileFragmentOne : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding : FragmentCreateProfileOneBinding? = null

    private val binding get() = _binding!!

    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    private lateinit var date : String

    private val PERMISSION_REQUEST_CODE: Int = 101

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_create_profile_one, container, false)
        _binding = FragmentCreateProfileOneBinding.inflate(inflater, container, false)

       ArrayAdapter.createFromResource(
           requireContext(),
           R.array.sex,
           android.R.layout.simple_spinner_item
       ).also {sexAdapter ->
           // Specify the layout to use when the list of choices appears
           sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
           // Apply the adapter to the spinner
           binding.fragmentCreateProfileOneGenderSp.adapter = sexAdapter


       }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.religion,
            android.R.layout.simple_spinner_item
        ).also {religionAdapter ->
            // Specify the layout to use when the list of choices appears
            religionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner

            binding.fragmentCreateProfileOneReligionSp.adapter = religionAdapter
        }



        binding.fragmentCreateProfileOneGenderSp.onItemSelectedListener = this

        binding.fragmentCreateProfileOneReligionSp.onItemSelectedListener = this

        binding.fragmentCreateProfileOneBtn.setOnClickListener {
            findNavController().navigate(R.id.createProfileFragmentTwo)
        }






        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // For spinner
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        parent?.getItemAtPosition(position)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dateButton = binding.fragmentCreateProfileOneDateBirthEt

        /** Show the date button on click of date button **/
        dateButton.setOnClickListener {
            showDatePickerDialog(requireView())
        }


        dateSetListener = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
            date = "${month+1}/$day/$year"
            dateButton.setText(date)
        }

        val cameraButton = binding.fragmentCreateProfileOneIb

        /** On click on camera button, check if permission is granted, if granted take picture or request other wise **/
        cameraButton.setOnClickListener {
            if (checkPermission()) takePicture() else requestPermission()
        }

    }


    /** Take picture function **/
    private fun takePicture() {
        try {
            val intent = Intent()
            intent.action = MediaStore.ACTION_IMAGE_CAPTURE
            startActivity(intent)
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    private fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(requireContext(),
            android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(READ_EXTERNAL_STORAGE, CAMERA),
            PERMISSION_REQUEST_CODE)
    }


    /** On request permission result grant user permission or show a permission denied message **/
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {

                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    takePicture()

                } else {
                    Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }

            else -> {

            }
        }
    }



    /** Show Date picker Dialog Function **/
    @RequiresApi(Build.VERSION_CODES.M)
    private fun showDatePickerDialog(v: View) {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(requireContext(),
            android.R.style.ThemeOverlay_Material_Dialog_Alert,
            dateSetListener,year, month,day
        )
        dialog.show()

    }

}