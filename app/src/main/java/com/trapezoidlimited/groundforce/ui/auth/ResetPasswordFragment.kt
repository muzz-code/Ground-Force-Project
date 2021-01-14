package com.trapezoidlimited.groundforce.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.api.ApiService
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.databinding.FragmentForgetPasswordBinding
import com.trapezoidlimited.groundforce.databinding.FragmentResetPasswordBinding
import com.trapezoidlimited.groundforce.model.request.ResetPasswordRequest
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.ui.dashboard.DashboardActivity
import com.trapezoidlimited.groundforce.utils.*
import com.trapezoidlimited.groundforce.viewmodel.AuthViewModel
import com.trapezoidlimited.groundforce.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import javax.inject.Inject

@AndroidEntryPoint
class ResetPasswordFragment : Fragment() {

    @Inject
    lateinit var loginApiServiceService: ApiService

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var missionsApi: MissionsApi

    private lateinit var viewModel: AuthViewModel

    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!

//    private val args: ResetPasswordFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentResetPasswordBinding.inflate(layoutInflater, container, false)


        /** setting toolbar text **/
        binding.fragmentResetPasswordToolbar.toolbarTitle.text =
            getString(R.string.password_reset_title_bar_str)

        val repository = AuthRepositoryImpl(loginApiServiceService, missionsApi)
        val factory = ViewModelFactory(repository, requireContext())
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        //Set Toolbar Back Arrow Icon
        binding.fragmentResetPasswordToolbar.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)

        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentResetPasswordToolbar.toolbarFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.forgetPasswordFragment)
        }


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val email = loadFromSharedPreference(requireActivity(), EMAIL)
        val token = DataListener.token
        println(token)
        println(email)


        viewModel.resetPasswordResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    binding.forgetResetConfirmPb.hide(binding.forgetResetConfirmBtn)
                    val message = it.value.data?.message
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.loginFragment)
                }
                is Resource.Failure -> {
                    binding.forgetResetConfirmPb.hide(binding.forgetResetConfirmBtn)
                    handleApiError(it, retrofit, binding.forgetResetConfirmBtn)
                }
            }
        })


        val fields: MutableList<JDataClass> = mutableListOf(
            JDataClass(
                editText = binding.fragmentResetNewPasswordEt,
                editTextInputLayout = binding.fragmentResetNewPasswordTil,
                errorMessage = JDErrorConstants.INVALID_PASSWORD_ERROR,
                validator = { it.jdValidatePin(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.fragmentResetConfirmNewPasswordEt,
                editTextInputLayout = binding.fragmentResetConfirmNewPasswordTil,
                errorMessage = JDErrorConstants.PASSWORD_DOES_NOT_MATCH,
                validator = {
                    it.jdValidateConfirmPassword(
                        binding.fragmentResetNewPasswordEt,
                        binding.fragmentResetConfirmNewPasswordEt
                    )
                }
            )
        )

        JDFormValidator.Builder()
            .addFieldsToValidate(fields)
            .removeErrorIcon(true)
            .viewsToEnable(mutableListOf(binding.forgetResetConfirmBtn))
            .watchWhileTyping(true)
            .build()



        binding.forgetResetConfirmBtn.setOnClickListener {

          val newPassword = binding.fragmentResetNewPasswordEt.text.toString()
          val confirmPassword = binding.fragmentResetConfirmNewPasswordEt.toString()

         binding.forgetResetConfirmPb.show(binding.forgetResetConfirmBtn)

          if (email != null && token != null) {
              val resetPasswordRequest = ResetPasswordRequest(email = email, token = token, newPassword = newPassword )
              viewModel.resetPassword(resetPasswordRequest)
          }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}