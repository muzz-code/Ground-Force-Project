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
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.trapezoidlimited.groundforce.EntryApplication
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.api.ApiService
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.databinding.FragmentPhoneActivationBinding
import com.trapezoidlimited.groundforce.model.request.VerifyPhoneRequest
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.utils.*
import com.trapezoidlimited.groundforce.viewmodel.AuthViewModel
import com.trapezoidlimited.groundforce.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import javax.inject.Inject


@AndroidEntryPoint
class PhoneActivationFragment : Fragment() {
    @Inject
    lateinit var loginApiServiceService: ApiService

    @Inject
    lateinit var errorUtils: ErrorUtils

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var missionsApi: MissionsApi

    private var _binding: FragmentPhoneActivationBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AuthViewModel
    private lateinit var phoneNumberTil: TextInputLayout
    private lateinit var number: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhoneActivationBinding.inflate(inflater, container, false)

        val repository = AuthRepositoryImpl(loginApiServiceService, missionsApi)
        val factory = ViewModelFactory(repository, requireContext())
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)


        /** setting toolbar text **/
        binding.fragmentPhoneActivationTb.toolbarTitle.text =
            getString(R.string.phone_activation_title_str)

        /** set navigation arrow from drawable **/
        binding.fragmentPhoneActivationTb.toolbarTransparentFragment.setNavigationIcon(R.drawable.ic_arrow_back)


        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentPhoneActivationTb.toolbarTransparentFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.landingFragment)
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
        phoneNumberTil = binding.phoneActivPhoneNoTil

        validateFields()

        viewModel.verifyPhoneResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    /** Navigating to phone verification fragment onSuccess*/
                    val action = PhoneActivationFragmentDirections
                        .actionPhoneActivationFragmentToPhoneVerificationFragment(number)
                    findNavController().navigate(action)
                }
                is Resource.Failure -> {
                    /** Hiding progressbar and enabling button */
                    binding.phoneActivationPb.hide(binding.phoneActivContinueBtn)

                    val message = "Number is already confirmed"

                    handleApiError( it, retrofit, requireView(), message, R.id.emailVerificationOne )

                }
            }
        })

        requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().navigate(R.id.landingFragment)
        }

        /**Verification button to verify user phone number as nigeria phone number**/
        binding.phoneActivContinueBtn.setOnClickListener {

            number = "+234" + phoneEditText.text.toString()

//            val action = PhoneActivationFragmentDirections
//                .actionPhoneActivationFragmentToPhoneVerificationFragment(number)
//            findNavController().navigate(action)

//                        val action = PhoneActivationFragmentDirections
//                          .actionPhoneActivationFragmentToPhoneVerificationFragment(number)
//                        findNavController().navigate(action)


            val phoneNumber = VerifyPhoneRequest(number)

            /** Saving phone in sharedPreference*/
            saveToSharedPreference(requireActivity(), PHONE, number)

            /** Making network call*/
            viewModel.verifyPhone(phoneNumber)

            /** Setting Progress bar to visible and disabling button*/
            binding.phoneActivationPb.show(it as Button?)

        }

    }


    private fun validateFields() {
        val fields: MutableList<JDataClass> = mutableListOf(
            JDataClass(
                editText = binding.phoneActivPhoneNoEt,
                editTextInputLayout = binding.phoneActivPhoneNoTil,
                errorMessage = JDErrorConstants.INVALID_PHONE_NUMBER_ERROR,
                validator = { it.jdValidatePhoneNumber(it.text.toString()) }
            )
        )

        JDFormValidator.Builder()
            .addFieldsToValidate(fields)
            .removeErrorIcon(true)
            .viewsToEnable(mutableListOf(binding.phoneActivContinueBtn))
            .watchWhileTyping(true)
            .build()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        /** Code under construction: Beware!*/
        viewModel._verifyPhoneResponse.value = null
        _binding = null
    }

}