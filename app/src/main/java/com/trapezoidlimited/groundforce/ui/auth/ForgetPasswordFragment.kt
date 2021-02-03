package com.trapezoidlimited.groundforce.ui.auth

import android.os.Bundle
import android.text.SpannableString
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.api.ApiService
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.databinding.FragmentForgetPasswordBinding
import com.trapezoidlimited.groundforce.model.request.ForgotPasswordRequest
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.utils.*
import com.trapezoidlimited.groundforce.viewmodel.AuthViewModel
import com.trapezoidlimited.groundforce.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_locations_verification.*
import retrofit2.Retrofit
import javax.inject.Inject

@AndroidEntryPoint
class ForgetPasswordFragment : Fragment() {

    @Inject
    lateinit var loginApiServiceService: ApiService

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var missionsApi: MissionsApi

    private lateinit var viewModel: AuthViewModel

    private var _binding: FragmentForgetPasswordBinding? = null
    private val binding get() = _binding!!
    private var email = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentForgetPasswordBinding.inflate(layoutInflater, container, false)

        /** setting toolbar text **/
        binding.fragmentForgetPasswordToolbar.toolbarTitle.text =
            getString(R.string.password_reset_title_bar_str)

        val repository = AuthRepositoryImpl(loginApiServiceService, missionsApi)
        val factory = ViewModelFactory(repository, requireContext())
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        //Set Toolbar Back Arrow Icon
        binding.fragmentForgetPasswordToolbar.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)

        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentForgetPasswordToolbar.toolbarFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }


        showStatusBar()

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        viewModel.forgotPasswordResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    val message = it.value.data?.message
                    binding.forgetPasswordResetPb.hide(binding.forgetPasswordResetBtn)
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    saveToSharedPreference(requireActivity(), EMAIL, email)


//                    findNavController().navigate(R.id.action_forgetPasswordFragment_to_resetPasswordFragment)
                }
                is Resource.Failure -> {
                    binding.forgetPasswordResetPb.hide(binding.forgetPasswordResetBtn)
                    handleApiError(it, retrofit, binding.forgetPasswordResetBtn)
                }
            }
        })


        val fields: MutableList<JDataClass> = mutableListOf(
            JDataClass(
                editText = binding.fragmentForgetPasswordEmailEt,
                editTextInputLayout = binding.fragmentForgetPasswordEmailTil,
                errorMessage = JDErrorConstants.INVALID_EMAIL_ERROR,
                validator = { it.jdValidateEmail(it.text.toString()) }
            )
        )

        JDFormValidator.Builder()
            .addFieldsToValidate(fields)
            .removeErrorIcon(true)
            .viewsToEnable(mutableListOf(binding.forgetPasswordResetBtn))
            .watchWhileTyping(true)
            .build()


        binding.forgetPasswordResetBtn.setOnClickListener {

            binding.forgetPasswordResetPb.show(binding.forgetPasswordResetBtn)

            email =  binding.fragmentForgetPasswordEmailEt.text.toString()


            val forgotPasswordRequest = ForgotPasswordRequest(email)

            viewModel.forgotPassword(forgotPasswordRequest)

        }

    }

    override fun onResume() {
        super.onResume()

        val emailFromSharedPref = loadFromSharedPreference(requireActivity(), EMAIL)

        if (emailFromSharedPref.isNotEmpty()){
            binding.fragmentForgetPasswordEmailEt.setText(SpannableString(emailFromSharedPref))
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}