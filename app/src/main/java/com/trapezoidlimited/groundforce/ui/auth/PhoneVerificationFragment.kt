package com.trapezoidlimited.groundforce.ui.auth

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.api.LoginAuthApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.databinding.FragmentPhoneVerificationBinding
import com.trapezoidlimited.groundforce.model.ConfirmPhone
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
class PhoneVerificationFragment : Fragment() {
    private var _binding: FragmentPhoneVerificationBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var loginApiService: LoginAuthApi

    @Inject
    lateinit var errorUtils: ErrorUtils
    private lateinit var viewModel: LoginAuthViewModel

    private val args: PhoneVerificationFragmentArgs by navArgs()
    private lateinit var phoneNumber: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhoneVerificationBinding.inflate(inflater, container, false)

        phoneNumber = args.phoneNumber

        val repository = AuthRepositoryImpl(loginApiService)
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(LoginAuthViewModel::class.java)

        /** setting toolbar text **/
        binding.fragmentPhoneVerificationTb.toolbarTitle.text =
            getString(R.string.phone_verification_title_str)

        /** set navigation arrow from drawable **/
        binding.fragmentPhoneVerificationTb.toolbarTransparentFragment.setNavigationIcon(R.drawable.ic_arrow_back)


        /**show status bar**/
        showStatusBar()
        val view = binding.root

        // Get Test from String Resource
        val codeText = getText(R.string.phone_verif_didnt_get_code_text_str)
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
                ds.color = getColor(requireContext(), R.color.colorBlue)
                ds.isUnderlineText = false
            }
        }
        // Set the span text
        ssText.setSpan(clickableSpan, 21, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        // Make the text spannable and clickable
        binding.phoneVerifResendTv.text = ssText
        binding.phoneVerifResendTv.movementMethod = LinkMovementMethod.getInstance()

        // Inflate the layout for this fragment
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentPhoneVerificationTb.toolbarTransparentFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        val otpField = binding.phoneVerifPinView as EditText


        otpField.watchToValidator(EditFieldType.OTP)
        watchAllMyFields(
            mutableMapOf(
                otpField to EditFieldType.OTP
            ),
            binding.phoneVerifConfirmBtn
        )


        viewModel.confirmPhoneResponse.observe(viewLifecycleOwner, Observer {
            when (it) {

                is Resource.Success -> {
                    Toast.makeText(this.context, it.value.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Failure -> {

                    val error = it.errorBody?.let { it1 -> errorUtils.parseError(it1) }

                    Toast.makeText(requireContext(), "${error?.message}", Toast.LENGTH_SHORT).show()

                    //handleApiError(it)
                }
            }
        })

        //navigate to create profile fragment
        binding.phoneVerifConfirmBtn.setOnClickListener {

            val otp = otpField.text.toString()
            val confirmPhone = ConfirmPhone(phoneNumber, otp)
            viewModel.confirmPhone(confirmPhone)

            //findNavController().navigate(R.id.createProfileFragmentOne)
            // otpField.text.clear()
            //clearFieldsArray()
        }


        requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().popBackStack()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}