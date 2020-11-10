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
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentPhoneActivationBinding
import com.trapezoidlimited.groundforce.utils.showStatusBar
import com.trapezoidlimited.groundforce.validator.EditFieldType
import com.trapezoidlimited.groundforce.validator.clearFieldsArray
import com.trapezoidlimited.groundforce.validator.watchAllMyFields
import com.trapezoidlimited.groundforce.validator.watchToValidator


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
                ds.color = ContextCompat.getColor(requireContext(), R.color.colorBlue)
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
                ds.color = ContextCompat.getColor(requireContext(), R.color.colorBlue)
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

        /** Validating the phone number field**/

        val phoneEditText = binding.phoneActivPhoneNoEt

        phoneEditText.watchToValidator(EditFieldType.PHONE)

        watchAllMyFields(
            mutableMapOf(
                phoneEditText to EditFieldType.PHONE
            ),
            binding.phoneActivContinueBtn
        )


        requireActivity().onBackPressedDispatcher.addCallback{
            findNavController().navigate(R.id.landingFragment)
        }

        /**Verification button to verify user phone number as nigeria phone number**/
        binding.phoneActivContinueBtn.setOnClickListener {
            findNavController().navigate(R.id.phoneVerificationFragment)
            phoneEditText.text.clear()
            clearFieldsArray()
        }
    }

}