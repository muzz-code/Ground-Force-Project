package com.trapezoidlimited.groundforce.ui.auth

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentPhoneActivationBinding


import com.trapezoidlimited.groundforce.validator.ValidationPhoneNumber
import com.trapezoidlimited.groundforce.validator.ValidationPhoneNumber.Companion.validatePhoneNumber

import com.trapezoidlimited.groundforce.utils.showStatusBar


class PhoneActivationFragment : Fragment() {
    private var _binding: FragmentPhoneActivationBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhoneActivationBinding.inflate(inflater, container, false)

        /** setting toolbar text **/
        binding.fragmentPhoneActivationTb.toolbarTitle.text = getString(R.string.phone_activation_title_str)

        /** set navigation arrow from drawable **/
        binding.fragmentPhoneActivationTb.toolbarTransparentFragment.setNavigationIcon(R.drawable.ic_arrow_back)


        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentPhoneActivationTb.toolbarTransparentFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        /**show status bar**/
        showStatusBar()
        val view = binding.root

        // Get Test from String Resource
        val codeText = getText(R.string.phone_activ_t_and_c_str)
        // Get an instance of SpannableString
        val ssText = SpannableString(codeText)
        // Implement ClickableSpan
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                Toast.makeText(requireContext(), "Clicked!", Toast.LENGTH_LONG).show()
            }
            // Change color and remove underline
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(requireContext(), R.color.colorTextResend)
                ds.isUnderlineText = false
            }
        }

        val clickableSpan1: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                Toast.makeText(requireContext(), "Clicked!", Toast.LENGTH_LONG).show()
            }
            // Change color and remove underline
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(requireContext(), R.color.colorTextResend)
                ds.isUnderlineText = false
            }
        }
        // Set the span text
        ssText.setSpan(clickableSpan, 46, 64, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ssText.setSpan(clickableSpan1, 79, 94, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        // Make the text spannable and clickable
        binding.phoneActivTermsConditionTv.text = ssText
        binding.phoneActivTermsConditionTv.movementMethod = LinkMovementMethod.getInstance()



        // Inflate the layout for this fragment
        return view
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val phoneEditText = binding.phoneActivPhoneNoEt
        val userPhoneNumber = phoneEditText.text.toString()

        requireActivity().onBackPressedDispatcher.addCallback{
            findNavController().navigate(R.id.landingFragment)
        }

        /**Verification button to verify user phone number as nigeria phone number**/
        binding.phoneActivContinueBtn.setOnClickListener {
            // using vararg to validate input
            val verifyPhoneNumberInput = ValidationPhoneNumber(phoneEditText)


            if (verifyPhoneNumberInput != null) {

                /**Error message for blank space**/
                verifyPhoneNumberInput.error = "Field required"
                Snackbar.make(requireActivity().findViewById(android.R.id.content),
                        getString(R.string.blank_phone_number_input_str),
                        Snackbar.LENGTH_LONG).show()

            } else if(!validate()){

                /**Error message for invalidate phone number**/
               binding.phoneActivPhoneNoEt.error = "Invalid phone number"
                Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    getString(R.string.invalid_phone_number_input_str),
                    Snackbar.LENGTH_LONG).show()
               return@setOnClickListener

           } else{

                /**Navigate to activation screen after user verification **/
              findNavController().navigate(R.id.phoneVerificationFragment)

           }
        }
    }

    /**Get user input for validation**/
    private fun validate(): Boolean{
        val field = "+234"+binding.phoneActivPhoneNoEt.text.toString().trim()
       return validatePhoneNumber(field)
    }
}