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
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.trapezoidlimited.groundforce.EntryApplication
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.api.ApiService
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.databinding.FragmentPhoneVerificationBinding
import com.trapezoidlimited.groundforce.model.request.ConfirmPhoneRequest
import com.trapezoidlimited.groundforce.model.request.VerifyPhoneRequest
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.utils.*
import com.trapezoidlimited.groundforce.viewmodel.AuthViewModel
import com.trapezoidlimited.groundforce.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import javax.inject.Inject

@AndroidEntryPoint
class PhoneVerificationFragment : Fragment() {
    private var _binding: FragmentPhoneVerificationBinding? = null
    private val binding get() = _binding!!
    private var sign_up_with_google = ""

    @Inject
    lateinit var loginApiServiceService: ApiService

    @Inject
    lateinit var errorUtils: ErrorUtils

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var missionsApi: MissionsApi

    private lateinit var viewModel: AuthViewModel


    private val args: PhoneVerificationFragmentArgs by navArgs()
    private lateinit var phoneNumber: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhoneVerificationBinding.inflate(inflater, container, false)

        phoneNumber = args.phoneNumber

        val repository = AuthRepositoryImpl(loginApiServiceService, missionsApi)
        val factory = ViewModelFactory(repository, requireContext())
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        /** setting toolbar text **/
        binding.fragmentPhoneVerificationTb.toolbarTitle.text =
            getString(R.string.phone_verification_title_str)

        /** set navigation arrow from drawable **/
        binding.fragmentPhoneVerificationTb.toolbarTransparentFragment.setNavigationIcon(R.drawable.ic_arrow_back)


        /**show status bar**/
        showStatusBar()
        val view = binding.root

        /** Get Test from String Resource **/
        val codeText = getText(R.string.phone_verif_didnt_get_code_text_str)

        /** Get an instance of SpannableString **/
        val ssText = SpannableString(codeText)

        /** Implement ClickableSpan **/
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {

                /** Network call to activate phone number **/

                val phoneNo = VerifyPhoneRequest(phoneNumber)
                viewModel.verifyPhone(phoneNo)

                setVisibility(binding.phoneVerificationResendPb)

            }

            /** Change color and remove underline **/
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = getColor(requireContext(), R.color.colorBlue)
                ds.isUnderlineText = false
            }
        }

        /** encrypting the phone number to set the phone verification text **/

        val numberFirstString = phoneNumber.substring(4, 7)
        val numberSecondString = phoneNumber.substring(10, phoneNumber.length)
        val phoneVerificationText =
            "Please enter the 4 digit code sent to you at 0$numberFirstString****$numberSecondString"

        binding.phoneVerifVerifyTv.text = phoneVerificationText

        /** Set the span text **/

        ssText.setSpan(clickableSpan, 21, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        /** Make the text spannable and clickable **/
        binding.phoneVerifResendTv.text = ssText
        binding.phoneVerifResendTv.movementMethod = LinkMovementMethod.getInstance()

        /** Load Email from google shared preference **/
        sign_up_with_google = loadFromSharedPreference(requireActivity(), SIGN_UP_WITH_GGOGLE)

        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentPhoneVerificationTb.toolbarTransparentFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        val otpField = binding.phoneVerifPinView as EditText

        validateFields()

        viewModel.verifyPhoneResponse.observe(viewLifecycleOwner, {

            when (it) {
                is Resource.Success -> {

                    setInVisibility(binding.phoneVerificationResendPb)

                    Toast.makeText(requireContext(), "OTP Sent!", Toast.LENGTH_SHORT).show()
                }
                is Resource.Failure -> {

                    setInVisibility(binding.phoneVerificationResendPb)

                    val message = "Number is already confirmed"


                    handleApiError( it, retrofit, requireView(), message, R.id.emailVerificationOne )

                }
            }

        })


        viewModel.confirmPhoneResponse.observe(viewLifecycleOwner, Observer {
            when (it) {

                is Resource.Success -> {

                    findNavController().navigate(
                        R.id.action_phoneVerificationFragment_to_emailVerificationOne
                    )

//                    /** Navigating to create profile fragment onSuccess*/
//                    if (sign_up_with_google != "true") {
//                        findNavController().navigate(
//                            R.id.action_phoneVerificationFragment_to_emailVerificationOne
//                        )
//                    } else {
//                        findNavController().navigate(
//                            R.id.action_phoneVerificationFragment_to_createProfileFragmentOne
//                        )
//                        saveToSharedPreference(requireActivity(), SIGN_UP_WITH_GGOGLE, "false")
//                    }
                }
                is Resource.Failure -> {
                    /** Hiding progressbar and enabling button */
                    binding.phoneVerificationPb.hide(binding.phoneVerifConfirmBtn)
                    handleApiError( it, retrofit, requireView())
                }
            }
        })

        /** navigate to create profile fragment */
        binding.phoneVerifConfirmBtn.setOnClickListener {

//            findNavController().navigate(
//                R.id.action_phoneVerificationFragment_to_emailVerificationOne
//            )

            val otp = otpField.text.toString()
            val confirmPhone = ConfirmPhoneRequest(phoneNumber, otp)


            /** Making network call*/
            viewModel.confirmPhone(confirmPhone)

            /** Setting Progress bar to visible and disabling button*/
            binding.phoneVerificationPb.show(it as Button)
        }


        requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().popBackStack()
        }

    }

    private fun validateFields() {

        val fields: MutableList<JDataClass> = mutableListOf(
            JDataClass(
                editText = binding.phoneVerifPinView,
                editTextInputLayout = null,
                errorMessage = JDErrorConstants.OTP_ERROR,
                validator = { it.jdValidateOTP(it.text.toString()) }
            )
        )

        JDFormValidator.Builder()
            .addFieldsToValidate(fields)
            .removeErrorIcon(true)
            .viewsToEnable(mutableListOf(binding.phoneVerifConfirmBtn))
            .watchWhileTyping(true)
            .build()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel._confirmPhoneResponse.value = null
        _binding = null
    }

}