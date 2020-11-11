package com.trapezoidlimited.groundforce.ui.auth

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.api.LoginAuthApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.api.VerifyPhone
import com.trapezoidlimited.groundforce.databinding.FragmentPhoneActivationBinding
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.utils.ErrorUtils
import com.trapezoidlimited.groundforce.utils.showStatusBar
import com.trapezoidlimited.groundforce.validator.EditFieldType
import com.trapezoidlimited.groundforce.validator.clearFieldsArray
import com.trapezoidlimited.groundforce.validator.watchAllMyFields
import com.trapezoidlimited.groundforce.validator.watchToValidator
import com.trapezoidlimited.groundforce.viewmodel.LoginAuthViewModel
import com.trapezoidlimited.groundforce.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class PhoneActivationFragment : Fragment() {

    @Inject
    lateinit var loginApiService: LoginAuthApi

    @Inject
    lateinit var errorUtils: ErrorUtils

    private lateinit var viewModel: LoginAuthViewModel

    private var _binding: FragmentPhoneActivationBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhoneActivationBinding.inflate(inflater, container, false)

        /** setting toolbar text **/
        binding.fragmentPhoneActivationTb.toolbarTitle.text =
            getString(R.string.phone_activation_title_str)

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


        val repository: AuthRepositoryImpl = AuthRepositoryImpl(loginApiService)

        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(LoginAuthViewModel::class.java)

        // Inflate the layout for this fragment
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        viewModel.phoneVerifyResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    val message = it.value.message
                    if (message != null) {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                        Log.i("Response", message)
                    }

                }
                is Resource.Failure -> {
                    val error = it.errorBody?.let { it1 -> errorUtils.parseError(it1) }
                    Toast.makeText(requireContext(), error?.message, Toast.LENGTH_SHORT).show()
                    error?.message?.let { it1 -> Log.i("Response", it1) }
                }
            }
        })

        /** Validating the phone number field**/

        val phoneEditText = binding.phoneActivPhoneNoEt

        phoneEditText.watchToValidator(EditFieldType.PHONE)

        watchAllMyFields(
            mutableMapOf(
                phoneEditText to EditFieldType.PHONE
            ),
            binding.phoneActivContinueBtn
        )


        requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().navigate(R.id.landingFragment)
        }

        /**Verification button to verify user phone number as nigeria phone number**/
        binding.phoneActivContinueBtn.setOnClickListener {

            val phoneVerify = VerifyPhone("+234" + phoneEditText.text.toString())
            viewModel.verifyPhone(phoneVerify)
//            findNavController().navigate(R.id.phoneVerificationFragment)
//            phoneEditText.text.clear()
//            clearFieldsArray()
        }
    }

}