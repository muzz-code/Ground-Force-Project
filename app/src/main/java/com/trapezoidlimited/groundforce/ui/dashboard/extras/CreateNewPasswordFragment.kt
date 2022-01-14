package com.trapezoidlimited.groundforce.ui.dashboard.extras

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.trapezoidlimited.groundforce.EntryApplication
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.api.ApiService
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.databinding.FragmentCreateNewPasswordBinding
import com.trapezoidlimited.groundforce.model.request.ChangePasswordRequest
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.utils.*
import com.trapezoidlimited.groundforce.viewmodel.AuthViewModel
import com.trapezoidlimited.groundforce.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import javax.inject.Inject

@AndroidEntryPoint
class CreateNewPasswordFragment : Fragment() {

    @Inject
    lateinit var loginApiServiceService: ApiService

    @Inject
    lateinit var missionsApi: MissionsApi

    @Inject
    lateinit var retrofit: Retrofit

    private var _binding: FragmentCreateNewPasswordBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var viewModel: AuthViewModel

    private val navArgs: CreateNewPasswordFragmentArgs by navArgs()
    private lateinit var pin: String

    private val roomViewModel by lazy { EntryApplication.viewModel(this) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateNewPasswordBinding.inflate(inflater, container, false)

        binding.fragmentCreateNewPasswordIct.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.fragmentCreateNewPasswordIct.toolbarTitle.text = getString(R.string.create_new_password_title_str)

        val repository = AuthRepositoryImpl(loginApiServiceService, missionsApi)
        val factory = ViewModelFactory(repository, requireContext())

        //Instantiate View Model
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        DataListener.currentScreen = CREATE_NEW_PASSWORD_SCREEN


        pin = navArgs.pin.toString()

        /** set navigation to go to the home screen **/

        binding.fragmentCreateNewPasswordIct.toolbarFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


        binding.fragmentCreateRepeatNewPasswordConfirmBtn.setOnClickListener {

            val newPin = binding.fragmentCreateNewPasswordEt.text.toString()
            val userId = loadFromSharedPreference(requireActivity(), USERID)

            Log.i("NEWPIN", newPin)
            Log.i("PIN", pin)
            Log.i("USERID", userId)

            val changePasswordRequest = ChangePasswordRequest(userId, pin, newPin)

            binding.fragmentCreateRepeatNewPasswordPb.show(binding.fragmentCreateRepeatNewPasswordConfirmBtn)

            viewModel.changePassword(changePasswordRequest)

        }


        viewModel.changePasswordResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    binding.fragmentCreateRepeatNewPasswordPb.hide(binding.fragmentCreateRepeatNewPasswordConfirmBtn)
                    setSuccessDialog()
                }

                is Resource.Failure -> {
                    binding.fragmentCreateRepeatNewPasswordPb.hide(binding.fragmentCreateRepeatNewPasswordConfirmBtn)
                    handleApiError(roomViewModel, requireActivity(), it, retrofit, requireView())
                }
            }
        })



        val fields: MutableList<JDataClass> = mutableListOf(
            JDataClass(
                editText = binding.fragmentCreateNewPasswordEt,
                editTextInputLayout = binding.fragmentCreateNewPasswordTil,
                errorMessage = JDErrorConstants.INVALID_PASSWORD_ERROR,
                validator = { it.jdValidatePin(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.fragmentCreateRepeatNewPasswordEt,
                editTextInputLayout = binding.fragmentCreateRepeatNewPasswordTil,
                errorMessage = JDErrorConstants.PASSWORD_DOES_NOT_MATCH,
                validator = {
                    it.jdValidateConfirmPassword(
                        binding.fragmentCreateNewPasswordEt,
                        binding.fragmentCreateRepeatNewPasswordEt
                    )
                }
            )
        )

        JDFormValidator.Builder()
            .addFieldsToValidate(fields)
            .removeErrorIcon(true)
            .viewsToEnable(mutableListOf(binding.fragmentCreateRepeatNewPasswordConfirmBtn))
            .watchWhileTyping(true)
            .build()


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}