package com.trapezoidlimited.groundforce.ui.profile

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
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

        dateButton.setOnClickListener {
            showDatePickerDialog(requireView())
        }

        dateSetListener = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
            date = "${month+1}/$day/$year"
            dateButton.setText(date)
        }

        val cameraButton = binding.fragmentCreateProfileOneIb

        cameraButton.setOnClickListener {
            try {
                val intent = Intent()
                intent.action = MediaStore.ACTION_IMAGE_CAPTURE
                startActivity(intent)
            } catch (e : Exception) {
                e.printStackTrace()
            }


        }

    }




    //Show Date Picker Dialog Function
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