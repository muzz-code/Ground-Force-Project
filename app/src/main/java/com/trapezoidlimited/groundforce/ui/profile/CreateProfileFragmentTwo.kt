package com.trapezoidlimited.groundforce.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentCreateProfileTwoBinding
import com.trapezoidlimited.groundforce.utils.JDErrorConstants
import com.trapezoidlimited.groundforce.utils.JDFormValidator
import com.trapezoidlimited.groundforce.utils.JDataClass
import com.trapezoidlimited.groundforce.utils.jdValidateName


class CreateProfileFragmentTwo : Fragment() {

    private var _binding: FragmentCreateProfileTwoBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateProfileTwoBinding.inflate(inflater, container, false)

        /** set navigation arrow from drawable **/
        binding.fragmentCreateProfileTwoIc.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val zipCodes = listOf("110000", "102010", "100000", "103040")
        val adapterZipCode = ArrayAdapter(requireContext(), R.layout.list_item, zipCodes)
        (binding.fragmentCreateProfileTwoZipCodeTf.editText as? AutoCompleteTextView)?.setAdapter(
            adapterZipCode
        )

        val lga = listOf("Alimosho", "Ikorodu", "Oshodi-Isolo", "Lagos-Island")
        val adapterLGA = ArrayAdapter(requireContext(), R.layout.list_item, lga)
        (binding.fragmentCreateProfileTwoLgaTf.editText as? AutoCompleteTextView)?.setAdapter(
            adapterLGA
        )

        val states = listOf("Lagos", "Oyo", "Ogun", "Ondo")
        val adapterState = ArrayAdapter(requireContext(), R.layout.list_item, states)
        (binding.fragmentCreateProfileTwoStateTf.editText as? AutoCompleteTextView)?.setAdapter(
            adapterState
        )


        /** Navigate to bank detail screen **/
        binding.fragmentCreateProfileTwoBtn.setOnClickListener {
            validateFields()
            if (!validateFields()) {
                return@setOnClickListener
            } else {
                findNavController().navigate(R.id.action_createProfileFragmentTwo_to_locationsVerificationFragment)
            }
        }

        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentCreateProfileTwoIc.toolbarFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun validateFields(): Boolean {

        val fields = mutableListOf(
            JDataClass(
                editText = binding.fragmentCreateProfileTwoStreetEt,
                editTextInputLayout = binding.fragmentCreateProfileTwoStreetTil,
                errorMessage = JDErrorConstants.NAME_FIELD_ERROR,
                validator = { it.jdValidateName(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.fragmentCreateProfileTwoStateTf.editText!!,
                editTextInputLayout = binding.fragmentCreateProfileTwoStateTf,
                errorMessage = JDErrorConstants.NAME_FIELD_ERROR,
                validator = { it.jdValidateName(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.fragmentCreateProfileTwoLgaTf.editText!!,
                editTextInputLayout = binding.fragmentCreateProfileTwoLgaTf,
                errorMessage = JDErrorConstants.NAME_FIELD_ERROR,
                validator = { it.jdValidateName(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.fragmentCreateProfileTwoZipCodeTf.editText!!,
                editTextInputLayout = binding.fragmentCreateProfileTwoZipCodeTf,
                errorMessage = JDErrorConstants.NAME_FIELD_ERROR,
                validator = { it.jdValidateName(it.text.toString()) }
            )
        )


        val validator = JDFormValidator.Builder()
            .addFieldsToValidate(fields)
            .removeErrorIcon(true)
            .build()

        return validator.areAllFieldsValidated
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}