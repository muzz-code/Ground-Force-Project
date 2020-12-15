package com.trapezoidlimited.groundforce.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.api.ApiService
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.databinding.FragmentUpdateProfileBinding
import com.trapezoidlimited.groundforce.model.request.VerifyAccountRequest
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
    lateinit var loginApiServiceService: ApiService

    @Inject
    lateinit var missionsApi: MissionsApi

    @Inject
    lateinit var retrofit: Retrofit

    private var _binding: FragmentUpdateProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AuthViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var updateButton: Button
    private lateinit var bankCodeEditText: EditText
    private lateinit var accountNumberEditText: EditText
    private lateinit var religionEditText: EditText
    private lateinit var additionalPhoneNumberEditText: EditText
    private lateinit var genderEditText: EditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateProfileBinding.inflate(inflater, container, false)

        /** set navigation arrow from drawable **/
        binding.fragmentUpdateProfileIc.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)

        /** Initializing views **/
        progressBar = binding.fragmentUpdateProfilePb
        updateButton = binding.fragmentUpdateProfileBtn
        bankCodeEditText = binding.fragmentUpdateProfileBankCodeEt
        accountNumberEditText = binding.fragmentUpdateProfileAccountNumEt
        additionalPhoneNumberEditText = binding.fragmentUpdateProfileAdditionalNumEt
        genderEditText = binding.fragmentUpdateProfileGenderTil.editText!!
        religionEditText = binding.fragmentUpdateProfileReligionTil.editText!!

        /** set title of the toolbar **/
        binding.fragmentUpdateProfileIc.toolbarTitle.text = "Additional Information"

        val repository = AuthRepositoryImpl(loginApiServiceService, missionsApi)
        val factory = ViewModelFactory(repository, requireContext())

        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        /** Observing the results from Verify Account Network Call **/
        viewModel.verifyAccountResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {

                    progressBar.hide(updateButton)
                    saveToSharedPreference(requireActivity(), COMPLETED_REGISTRATION, "true")
                    Toast.makeText(requireContext(), "${it.value.data?.message}", Toast.LENGTH_SHORT).show()

                    findNavController().navigate(R.id.agentDashboardFragment)

                }

                is Resource.Failure -> {

                    progressBar.hide(updateButton)
                    handleApiError(it, retrofit, requireView())

                }

            }
        })


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

        updateButton.setOnClickListener {

            saveToSharedPreference(requireActivity(), COMPLETED_REGISTRATION, "true")

            if (!validateFields()) {
                showSnackBar(requireView(), "All fields are required and must contain valid inputs.")
                return@setOnClickListener
            } else {
                progressBar.show(updateButton)

                val bankCode = bankCodeEditText.text.toString()
                val accountNumber = accountNumberEditText.text.toString()
                val religion = religionEditText.text.toString()
                val additionNumber = additionalPhoneNumberEditText.text.toString()
                val gender = genderEditText.text.toString()
                val agentGender = if (gender == "Male") {
                    "m"
                } else if ( gender == "Female") {
                    "f"
                } else {
                    "o"
                }

                saveToSharedPreference(requireActivity(), BANKCODE, bankCode)
                saveToSharedPreference(requireActivity(), ACCOUNTNUMBER, accountNumber)
                saveToSharedPreference(requireActivity(), RELIGION, religion)
                saveToSharedPreference(requireActivity(), ADDITIONALPHONENUMBER, additionNumber)
                saveToSharedPreference(requireActivity(), GENDER, agentGender)


                val verifyAccountRequest = VerifyAccountRequest(
                    bankCode,
                    accountNumber,
                    religion,
                    additionNumber,
                    agentGender )

                viewModel.verifyAccount(verifyAccountRequest)
            }


        }
    }

    private fun validateFields(): Boolean {

        val fields = mutableListOf(
            JDataClass(
                editText = bankCodeEditText,
                editTextInputLayout = binding.fragmentUpdateProfileBankCodeTil,
                errorMessage = JDErrorConstants.NAME_FIELD_ERROR,
                validator = { it.jdValidateName(it.text.toString()) }
            ),
            JDataClass(
                editText = accountNumberEditText,
                editTextInputLayout = binding.fragmentUpdateProfileAccountNumTil,
                errorMessage = JDErrorConstants.BANK_ACCOUNT_NUMBER_ERROR,
                validator = { it.jdValidateAccountNumber(it.text.toString()) }
            ),
            JDataClass(
                editText = additionalPhoneNumberEditText,
                editTextInputLayout = binding.fragmentUpdateProfileAdditionalNumTil,
                errorMessage = JDErrorConstants.INCOMPLETE_PHONE_NUMBER_ERROR,
                validator = { it.jdValidateAdditionalPhone(it.text.toString()) }
            ),
            JDataClass(
                editText = religionEditText,
                editTextInputLayout = binding.fragmentUpdateProfileReligionTil,
                errorMessage = JDErrorConstants.NAME_FIELD_ERROR,
                validator = { it.jdValidateName(it.text.toString()) }
            ),
            JDataClass(
                editText = genderEditText,
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