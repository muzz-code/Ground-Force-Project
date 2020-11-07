package com.trapezoidlimited.groundforce.ui.profile

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentUserProfileBinding
import java.util.*


class UserProfileFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private var _binding: FragmentUserProfileBinding? = null

    val binding get() = _binding!!

    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    private lateinit var date: String

    /** onCreateView over ride function **/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_user_profile, container, false)
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)

        /** setting toolbar text **/
        binding.fragmentUserProfileTb.toolbarTitle.text = getString(R.string.profile_str)
        binding.fragmentUserProfileTb.toolbarTitle.setTextColor(resources.getColor(R.color.colorWhite))

        /** set navigation arrow from drawable **/
        binding.fragmentUserProfileTb.toolbarTransparentFragment.setNavigationIcon(R.drawable.ic_arrow_white_back)


        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentUserProfileTb.toolbarTransparentFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        /** Array adapter for spinner drop down for sex **/
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sex,
            android.R.layout.simple_spinner_item
        ).also { sexAdapter ->
            // Specify the layout to use when the list of choices appears
            sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.fragmentUserProfileGenderSp.adapter = sexAdapter

        }

        /** Array adapter for spinner drop down for religion **/
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.religion,
            android.R.layout.simple_spinner_item
        ).also { religionAdapter ->
            // Specify the layout to use when the list of choices appears
            religionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner

            binding.fragmentUserProfileReligiousSp.adapter = religionAdapter
        }

        /** listener for sex option **/
        binding.fragmentUserProfileGenderSp.onItemSelectedListener = this

        /** listener for religion option **/
        binding.fragmentUserProfileReligiousSp.onItemSelectedListener = this


        binding.fragmentUserProfileFirstNamePlaceholderEt.isEnabled = false
        binding.fragmentUserProfileLastNamePlaceholderEt.isEnabled = false
        binding.fragmentUserProfileAccountNumberEt.isEnabled =false
        binding.fragmentUserProfileResidentialAddressEt.isEnabled = false


        return binding.root
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        parent?.getItemAtPosition(position)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


    /** onActivityCreated **/
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val dateButton = binding.fragmentUserProfileDateBirthEt

        /** Show the date button on click of date button **/
        dateButton.setOnClickListener {
            showDatePickerDialog(requireView())
        }

        /** Date set listener **/
        dateSetListener = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
            date = "${month + 1}/$day/$year"
            dateButton.setText(date)
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

        /** Date dialog picker style **/
        val dialog = DatePickerDialog(
            requireContext(),
            android.R.style.ThemeOverlay_Material_Dialog_Alert,
            dateSetListener, year, month, day
        )
        dialog.show()

    }


}