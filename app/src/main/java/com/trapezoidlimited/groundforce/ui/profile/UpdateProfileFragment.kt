package com.trapezoidlimited.groundforce.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.api.LoginAuthApi
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.databinding.FragmentUpdateProfileBinding
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.utils.*
import com.trapezoidlimited.groundforce.viewmodel.AuthViewModel
import com.trapezoidlimited.groundforce.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import javax.inject.Inject

@AndroidEntryPoint
class UpdateProfileFragment : Fragment() {

    @Inject
    lateinit var loginApiService: LoginAuthApi

    @Inject
    lateinit var missionsApi: MissionsApi

    @Inject
    lateinit var retrofit: Retrofit

    private var _binding: FragmentUpdateProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AuthViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateProfileBinding.inflate(inflater, container, false)

        /** set navigation arrow from drawable **/
        binding.fragmentUpdateProfileIc.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)

        /** set title of the toolbar **/
        binding.fragmentUpdateProfileIc.toolbarTitle.text = "Additional Information"

        val repository = AuthRepositoryImpl(loginApiService, missionsApi)
        val factory = ViewModelFactory(repository, requireContext())

        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)





        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentUpdateProfileIc.toolbarFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


        val religions = listOf("Christian", "Muslim", "Others")
        val adapterReligion = ArrayAdapter(requireContext(), R.layout.list_item, religions)
        (binding.fragmentUpdateProfileReligionTil.editText as? AutoCompleteTextView)?.setAdapter(
            adapterReligion
        )

        val genders = listOf("Male", "Female", "Others")
        val adapterGender = ArrayAdapter(requireContext(), R.layout.list_item, genders)
        (binding.fragmentUpdateProfileGenderTil.editText as? AutoCompleteTextView)?.setAdapter(
            adapterGender
        )

        binding.fragmentUpdateProfileBtn.setOnClickListener {

            saveToSharedPreference(requireActivity(), COMPLETED_REGISTRATION, "true")

            if (!validateFields()) {
                showSnackBar(binding.fragmentUpdateProfileBtn, "All fields are required and must contain valid inputs.")
                return@setOnClickListener
            } else {
                showSnackBar(binding.fragmentUpdateProfileBtn, "Validated")
            }
        }
    }

    private fun validateFields(): Boolean {

        val fields = mutableListOf(
            JDataClass(
                editText = binding.fragmentUpdateProfileBankCodeEt,
                editTextInputLayout = binding.fragmentUpdateProfileBankCodeTil,
                errorMessage = JDErrorConstants.NAME_FIELD_ERROR,
                validator = { it.jdValidateName(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.fragmentUpdateProfileAccountNumEt,
                editTextInputLayout = binding.fragmentUpdateProfileAccountNumTil,
                errorMessage = JDErrorConstants.BANK_ACCOUNT_NUMBER_ERROR,
                validator = { it.jdValidateAccountNumber(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.fragmentUpdateProfileAdditionalNumEt,
                editTextInputLayout = binding.fragmentUpdateProfileAdditionalNumTil,
                errorMessage = JDErrorConstants.INCOMPLETE_PHONE_NUMBER_ERROR,
                validator = { it.jdValidateAdditionalPhone(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.fragmentUpdateProfileReligionTil.editText!!,
                editTextInputLayout = binding.fragmentUpdateProfileReligionTil,
                errorMessage = JDErrorConstants.NAME_FIELD_ERROR,
                validator = { it.jdValidateName(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.fragmentUpdateProfileGenderTil.editText!!,
                editTextInputLayout = binding.fragmentUpdateProfileGenderTil,
                errorMessage = JDErrorConstants.NAME_FIELD_ERROR,
                validator = { it.jdValidateName(it.text.toString()) }
            )
        )


        val validator = JDFormValidator.Builder()
            .addFieldsToValidate(fields)
            .removeErrorIcon(true)
            .build()

        return validator.areAllFieldsValidated
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}