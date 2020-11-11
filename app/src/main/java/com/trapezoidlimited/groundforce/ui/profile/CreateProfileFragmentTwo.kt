package com.trapezoidlimited.groundforce.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.data.AgentObject
import com.trapezoidlimited.groundforce.databinding.FragmentCreateProfileTwoBinding
import com.trapezoidlimited.groundforce.validator.EditFieldType
import com.trapezoidlimited.groundforce.validator.watchAllMyFields
import com.trapezoidlimited.groundforce.validator.watchToValidator

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val emailEditText = binding.fragmentCreateProfileTwoEmailAddressPlaceholderEt
        val addressEditText = binding.fragmentCreateProfileTwoAddressPlaceholderEt
        val additionalPhoneEditText = binding.fragmentCreateProfileTwoPhoneNumberEt

        /** validating email and residential address fields **/
        emailEditText.watchToValidator(EditFieldType.EMAIL)
        addressEditText.watchToValidator(EditFieldType.ADDRESS)
        additionalPhoneEditText.watchToValidator(EditFieldType.ADDITIONALPHONE)

        watchAllMyFields(
            mutableMapOf(
                emailEditText to EditFieldType.EMAIL,
                addressEditText to EditFieldType.ADDRESS,
                additionalPhoneEditText to EditFieldType.ADDITIONALPHONE
            ),
            binding.fragmentCreateProfileTwoBtn
        )


        /** Navigate to bank detail screen **/
        binding.fragmentCreateProfileTwoBtn.setOnClickListener {

            AgentObject.additionalPhoneNumber =
                binding.fragmentCreateProfileTwoPhoneNumberEt.text.toString()
            AgentObject.email =
                binding.fragmentCreateProfileTwoEmailAddressPlaceholderEt.text.toString()
            AgentObject.homeAddress =
                binding.fragmentCreateProfileTwoAddressPlaceholderEt.text.toString()


            findNavController().navigate(R.id.createProfileFragmentThree)
        }

        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentCreateProfileTwoIc.toolbarFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}