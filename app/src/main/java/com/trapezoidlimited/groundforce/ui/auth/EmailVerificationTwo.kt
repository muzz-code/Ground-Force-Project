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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.trapezoidlimited.groundforce.EntryApplication
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.api.ApiService
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.databinding.FragmentEmailVerificationTwoBinding
import com.trapezoidlimited.groundforce.model.request.ConfirmEmailAddressRequest
import com.trapezoidlimited.groundforce.model.request.VerifyEmailAddressRequest
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.utils.*
import com.trapezoidlimited.groundforce.viewmodel.AuthViewModel
import com.trapezoidlimited.groundforce.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import javax.inject.Inject

@AndroidEntryPoint
class EmailVerificationTwo : Fragment() {

    @Inject
    lateinit var loginApiServiceService: ApiService

    @Inject
    lateinit var errorUtils: ErrorUtils

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var missionsApi: MissionsApi

    private var _binding: FragmentEmailVerificationTwoBinding? = null
    private val binding get() = _binding!!
    private val args: EmailVerificationTwoArgs by navArgs()
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEmailVerificationTwoBinding.inflate(layoutInflater, container, false)
        val repository = AuthRepositoryImpl(loginApiServiceService, missionsApi)
        val factory = ViewModelFactory(repository, requireContext())
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)


        /** setting toolbar text **/
        binding.fragmentEmailVerificationTwoTb.toolbarTitle.text =
            getString(R.string.email_verification_title_bar_str)


        /** set navigation arrow from drawable **/
        binding.fragmentEmailVerificationTwoTb.toolbarTransparentFragment.setNavigationIcon(R.drawable.ic_arrow_back)

        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentEmailVerificationTwoTb.toolbarTransparentFragment.setNavigationOnClickListener {

//            DataListener.observerChecker = false
            //findNavController().popBackStack()
            findNavController().navigate(R.id.action_emailVerificationTwo_to_emailVerificationOne)
        }



        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        validateFields()

        val verificationCode = args.verificationCode
        binding.fragmentEmailVerificationTwoPinView.setText(verificationCode)

        val email = loadFromSharedPreference(requireActivity(), EMAIL)

        binding.fragmentEmailVerificationTwoInstructionTv.text =
            "Please enter the 4 digit code sent to you at $email"

        /** Get Test from String Resource */
        val codeText = getText(R.string.email_verify_didnt_get_code_text_str)

        /** Get an instance of SpannableString */
        val ssText = SpannableString(codeText)

        /** Implement ClickableSpan */
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {

                val verifyEmailAddressRequest = VerifyEmailAddressRequest(email)

                viewModel.verifyEmail(verifyEmailAddressRequest)

            }

            /** Change color and remove underline */
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(requireContext(), R.color.colorBlue)
                ds.isUnderlineText = false
            }
        }

        /** Set the span text */
        ssText.setSpan(clickableSpan, 21, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        /** Make the text spannable and clickable */
        binding.fragmentEmailVerificationTwoResendTv.text = ssText
        binding.fragmentEmailVerificationTwoResendTv.movementMethod =
            LinkMovementMethod.getInstance()



        viewModel.confirmEmailResponse.observe(viewLifecycleOwner, {

            when (it) {
                is Resource.Success -> {
                    binding.fragmentEmailVerificationTwoPb.hide(binding.fragmentEmailVerificationTwoConfirmBtn)

                    Toast.makeText(
                        requireContext(),
                        "${it.value.data?.message}",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                    findNavController().navigate(R.id.createProfileFragmentOne)
                }
                is Resource.Failure -> {

                    binding.fragmentEmailVerificationTwoPb.hide(binding.fragmentEmailVerificationTwoConfirmBtn)

                    val message = "Email is already confirmed"

                    handleApiError(
                        it,
                        retrofit,
                        requireView(),
                        message,
                        R.id.action_emailVerificationOne_to_createProfileFragmentOne
                    )

                }
            }
        })


        viewModel.verifyEmailResponse.observe(viewLifecycleOwner, {
            when (it) {

                is Resource.Success -> {

                    Toast.makeText(
                        requireContext(),
                        "${it.value.data?.message}",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                }

                is Resource.Failure -> {

                    handleApiError(it, retrofit, requireView())
                }
            }
        })


        binding.fragmentEmailVerificationTwoChangeEmailTv.setOnClickListener {
            findNavController().navigate(R.id.emailVerificationOne)
        }

        binding.fragmentEmailVerificationTwoConfirmBtn.setOnClickListener {

            binding.fragmentEmailVerificationTwoPb.show(binding.fragmentEmailVerificationTwoConfirmBtn)

            val code = binding.fragmentEmailVerificationTwoPinView.text.toString()
//            val email = loadFromSharedPreference(requireActivity(), EMAIL)

            val confirmEmailAddressRequest = ConfirmEmailAddressRequest(email, code)

            println(confirmEmailAddressRequest)

            viewModel.confirmEmail(confirmEmailAddressRequest)

            //findNavController().navigate(R.id.createProfileFragmentOne)

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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}