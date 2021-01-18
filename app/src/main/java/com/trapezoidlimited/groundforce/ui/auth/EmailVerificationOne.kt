package com.trapezoidlimited.groundforce.ui.auth

import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.EntryApplication
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.api.ApiService
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.databinding.FragmentEmailVerificationOneBinding
import com.trapezoidlimited.groundforce.model.request.VerifyEmailAddressRequest
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.utils.*
import com.trapezoidlimited.groundforce.viewmodel.AuthViewModel
import com.trapezoidlimited.groundforce.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import javax.inject.Inject

@AndroidEntryPoint
class EmailVerificationOne : Fragment() {

    @Inject
    lateinit var loginApiServiceService: ApiService

    @Inject
    lateinit var errorUtils: ErrorUtils

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var missionsApi: MissionsApi

    private var _binding: FragmentEmailVerificationOneBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AuthViewModel
    private lateinit var email: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEmailVerificationOneBinding.inflate(layoutInflater, container, false)

        val repository = AuthRepositoryImpl(loginApiServiceService, missionsApi)
        val factory = ViewModelFactory(repository, requireContext())
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)


        /** setting toolbar text **/
        binding.fragmentEmailVerificationTb.toolbarTitle.text =
            getString(R.string.email_verification_title_bar_str)


        /** set navigation arrow from drawable **/
        binding.fragmentEmailVerificationTb.toolbarTransparentFragment.setNavigationIcon(R.drawable.ic_arrow_back)



        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentEmailVerificationTb.toolbarTransparentFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.phoneActivationFragment)
        }

        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        validateFields()

        val signInWithGoogleChecker = loadFromSharedPreference(requireActivity(), SIGN_UP_WITH_GGOGLE)

        if (signInWithGoogleChecker == "true") {
            val emailFromGoogle = loadFromSharedPreference(requireActivity(), EMAIL)

            binding.fragmentEmailVerificationEt.text = SpannableStringBuilder(emailFromGoogle)
        }

        viewModel.verifyEmailResponse.observe(viewLifecycleOwner, {
            when (it) {

                is Resource.Success -> {

                    /** Saving EMAIL in sharedPreference*/
                    saveToSharedPreference(requireActivity(), EMAIL, email)

                    binding.fragmentEmailVerificationSubmitPb.hide(binding.fragmentEmailVerificationSubmitBtn)

                    Toast.makeText(requireContext(), "${it.value.data?.message}", Toast.LENGTH_SHORT)
                        .show()

                    findNavController().navigate(R.id.action_emailVerificationOne_to_emailVerificationTwo)

                }

                is Resource.Failure -> {

                    setInVisibility(binding.fragmentEmailVerificationSubmitPb)

                    binding.fragmentEmailVerificationSubmitPb.hide(binding.fragmentEmailVerificationSubmitBtn)

                    val message = "User is already verified"

                    handleApiError(
                        it, retrofit, requireView(),
                        message, R.id.createProfileFragmentOne
                    )
                }
            }
        })

        binding.fragmentEmailVerificationSubmitBtn.setOnClickListener {

           email = binding.fragmentEmailVerificationEt.text.toString()

//            /** Saving EMAIL in sharedPreference*/
//            saveToSharedPreference(requireActivity(), EMAIL, email)

            binding.fragmentEmailVerificationSubmitPb.show(binding.fragmentEmailVerificationSubmitBtn)

            val verifyEmailAddressRequest = VerifyEmailAddressRequest(email)

            viewModel.verifyEmail(verifyEmailAddressRequest)

            //findNavController().navigate(R.id.action_emailVerificationOne_to_emailVerificationTwo)

        }

    }

    private fun validateFields() {

        val fields: MutableList<JDataClass> = mutableListOf(
            JDataClass(
                editText = binding.fragmentEmailVerificationEt,
                editTextInputLayout = binding.fragmentEmailVerificationTil,
                errorMessage = JDErrorConstants.INVALID_EMAIL_ERROR,
                validator = { it.jdValidateEmail(it.text.toString()) }
            )
        )

        JDFormValidator.Builder()
            .addFieldsToValidate(fields)
            .removeErrorIcon(true)
            .viewsToEnable(mutableListOf(binding.fragmentEmailVerificationSubmitBtn))
            .watchWhileTyping(true)
            .build()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}