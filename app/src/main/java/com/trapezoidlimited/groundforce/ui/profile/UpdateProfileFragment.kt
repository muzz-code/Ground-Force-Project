package com.trapezoidlimited.groundforce.ui.profile

import android.content.Context
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.EntryApplication
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.api.ApiService
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.databinding.FragmentUpdateProfileBinding
import com.trapezoidlimited.groundforce.model.BankJson
import com.trapezoidlimited.groundforce.model.request.VerifyAccountRequest
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.room.RoomAdditionalDetail
import com.trapezoidlimited.groundforce.utils.*
import com.trapezoidlimited.groundforce.viewmodel.AuthViewModel
import com.trapezoidlimited.groundforce.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import java.io.InputStream
import java.lang.Exception
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
    private lateinit var bankNameEditText: EditText
    private lateinit var accountNumberEditText: EditText
    private lateinit var religionEditText: EditText
    private lateinit var additionalPhoneNumberEditText: EditText
    private lateinit var genderEditText: EditText
    private val roomViewModel by lazy { EntryApplication.viewModel(this) }
    private val gson by lazy { EntryApplication.gson }
    private lateinit var banks: BankJson
    private var bankPicked = ""
    private lateinit var bankCodeEditText: EditText


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
        bankNameEditText = binding.fragmentUpdateProfileBankNameTil.editText!!
        accountNumberEditText = binding.fragmentUpdateProfileAccountNumEt
        additionalPhoneNumberEditText = binding.fragmentUpdateProfileAdditionalNumEt
        genderEditText = binding.fragmentUpdateProfileGenderTil.editText!!
        religionEditText = binding.fragmentUpdateProfileReligionTil.editText!!
        bankCodeEditText = binding.fragmentUpdateProfileBankCodeEt

        /** set title of the toolbar **/
        binding.fragmentUpdateProfileIc.toolbarTitle.text = "Additional Information"

        val repository = AuthRepositoryImpl(loginApiServiceService, missionsApi)
        val factory = ViewModelFactory(repository, requireContext())

        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        banks = gson.fromJson(readJson(requireActivity()), BankJson::class.java)


        /** Observing the results from Verify Account Network Call **/
        viewModel.verifyAccountResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {

                    progressBar.hide(updateButton)
                    saveToSharedPreference(requireActivity(), COMPLETED_REGISTRATION, "true")
                    Toast.makeText(
                        requireContext(),
                        "${it.value.data?.message}",
                        Toast.LENGTH_SHORT
                    ).show()

                    /** Saving to shared preference that user is verified **/

                    saveToSharedPreference(requireActivity(), IS_VERIFIED, "true")

                    findNavController().navigate(R.id.agentDashboardFragment)

                }

                is Resource.Failure -> {

                    progressBar.hide(updateButton)
                    handleApiError(roomViewModel, requireActivity(), it, retrofit, requireView())

                }

            }
        })


        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentUpdateProfileIc.toolbarFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.agentDashboardFragment)
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

        //BANKS
        val bankList: MutableList<String> = mutableListOf()

        for (data in banks.data) {
            bankList.add(data.name)
        }


        val bankAutoCompleteTextView =
            (binding.fragmentUpdateProfileBankNameTil.editText as? AutoCompleteTextView)

        val adapterBanks = ArrayAdapter(requireContext(), R.layout.list_item, bankList)

        bankAutoCompleteTextView?.setAdapter(adapterBanks)


        //Get bank Picked
        val adapterBankObject =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                bankPicked = parent.getItemAtPosition(position).toString()
                for (bank in banks.data) {
                    if (bank.name == bankPicked) {
                        bankCodeEditText.text = SpannableStringBuilder(bank.code)
                    }
                }
            }

        bankAutoCompleteTextView?.onItemClickListener = adapterBankObject



        updateButton.setOnClickListener {

            saveToSharedPreference(requireActivity(), COMPLETED_REGISTRATION, "true")

            if (!validateFields()) {
                showSnackBar(
                    requireView(),
                    "All fields are required and must contain valid inputs."
                )
                return@setOnClickListener
            } else {
                progressBar.show(updateButton)

                val bankCode = bankCodeEditText.text.toString()
                val accountNumber = accountNumberEditText.text.toString()
                val religion = religionEditText.text.toString()
                var additionNumber = additionalPhoneNumberEditText.text.toString()
                val gender = genderEditText.text.toString()
                val agentGender = if (gender == "Male") {
                    "m"
                } else if (gender == "Female") {
                    "f"
                } else {
                    "o"
                }

                val bankName = bankNameEditText.text.toString()

                val avatarUrl = loadFromSharedPreference(requireActivity(), AVATAR_URL)
                val publicId = loadFromSharedPreference(requireActivity(), PUBLIC_ID)

                if (additionNumber.trim().isEmpty()) {
                    additionNumber = ""
                }
                println(bankName)

                saveToSharedPreference(requireActivity(), BANKNAME, bankName)
                saveToSharedPreference(requireActivity(), BANKCODE, bankCode)
                saveToSharedPreference(requireActivity(), ACCOUNTNUMBER, accountNumber)
                saveToSharedPreference(requireActivity(), RELIGION, religion)
                saveToSharedPreference(requireActivity(), ADDITIONALPHONENUMBER, additionNumber)
                saveToSharedPreference(requireActivity(), GENDER, agentGender)


                val roomAdditionalDetail = RoomAdditionalDetail(
                    agentId = 1,
                    bankCode = bankCode,
                    accountNumber = accountNumber,
                    religion = religion,
                    additionalPhoneNumber = additionNumber,
                    gender = agentGender,
                    avatarUrl = avatarUrl,
                    publicId = publicId
                )

                roomViewModel.addAdditionalDetail(roomAdditionalDetail)


                val verifyAccountRequest = VerifyAccountRequest(
                    bankCode,
                    accountNumber,
                    religion,
                    additionNumber,
                    agentGender
                )

               viewModel.verifyAccount(verifyAccountRequest)
            }


        }
    }

    private fun validateFields(): Boolean {

        val fields = mutableListOf(
            JDataClass(
                editText = bankNameEditText,
                editTextInputLayout = binding.fragmentUpdateProfileBankNameTil,
                errorMessage = JDErrorConstants.NAME_FIELD_ERROR,
                validator = { it.jdValidateName(it.text.toString()) }
            ),
            JDataClass(
                editText = accountNumberEditText,
                editTextInputLayout = binding.fragmentUpdateProfileAccountNumTil,
                errorMessage = JDErrorConstants.BANK_ACCOUNT_NUMBER_ERROR,
                validator = { it.jdValidateAccountNumber(it.text.toString()) }
            ),
//            JDataClass(
//                editText = additionalPhoneNumberEditText,
//                editTextInputLayout = binding.fragmentUpdateProfileAdditionalNumTil,
//                errorMessage = JDErrorConstants.INCOMPLETE_PHONE_NUMBER_ERROR,
//                validator = { it.jdValidateAdditionalPhone(it.text.toString()) }
//            ),
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

    private fun readJson(context: Context): String? {
        var inputStream: InputStream? = null

        val jsonString: String

        try {
            inputStream = context.assets.open("bank.json")

            val size = inputStream.available()

            val buffer = ByteArray(size)

            inputStream.read(buffer)

            jsonString = String(buffer)

            return jsonString
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }

        return null
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}