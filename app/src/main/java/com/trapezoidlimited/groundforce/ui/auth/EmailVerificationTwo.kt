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
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.misterjedu.jdformvalidator.*
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentEmailVerificationTwoBinding

class EmailVerificationTwo : Fragment() {

    private var _binding: FragmentEmailVerificationTwoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEmailVerificationTwoBinding.inflate(layoutInflater, container, false)

        /** setting toolbar text **/
        binding.fragmentEmailVerificationTwoTb.toolbarTitle.text =
            getString(R.string.email_verification_title_bar_str)


        /** set navigation arrow from drawable **/
        binding.fragmentEmailVerificationTwoTb.toolbarTransparentFragment.setNavigationIcon(R.drawable.ic_arrow_back)

        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentEmailVerificationTwoTb.toolbarTransparentFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        validateFields()

        // Get Test from String Resource
        val codeText = getText(R.string.email_verify_didnt_get_code_text_str)
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

        // Set the span text
        ssText.setSpan(clickableSpan, 21, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        // Make the text spannable and clickable
        binding.fragmentEmailVerificationTwoResendTv.text = ssText
        binding.fragmentEmailVerificationTwoResendTv.movementMethod =
            LinkMovementMethod.getInstance()

        binding.fragmentEmailVerificationTwoChangeEmailTv.setOnClickListener {
            Toast.makeText(requireContext(), "Clicked!", Toast.LENGTH_SHORT).show()
        }

        binding.fragmentEmailVerificationTwoConfirmBtn.setOnClickListener {
            findNavController().navigate(R.id.createProfileFragmentOne)
        }

    }

    private fun validateFields() {

        val fields: MutableList<JDataClass> = mutableListOf(
            JDataClass(
                editText = binding.fragmentEmailVerificationTwoPinView,
                editTextInputLayout = null,
                errorMessage = "Error: Code must be 4 digits",
                validator = { it.jdValidateOTP(it.text.toString()) }
            )
        )

        JDFormValidator.Builder()
            .addFieldsToValidate(fields)
            .removeErrorIcon(true)
            .viewsToEnable(mutableListOf(binding.fragmentEmailVerificationTwoConfirmBtn))
            .watchWhileTyping(true)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}