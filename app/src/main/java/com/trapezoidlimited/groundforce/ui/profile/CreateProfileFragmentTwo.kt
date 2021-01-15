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
import com.trapezoidlimited.groundforce.databinding.FragmentCreateProfileTwoBinding
import com.trapezoidlimited.groundforce.model.LocationJson
import com.trapezoidlimited.groundforce.model.request.AgentDataRequest
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.utils.*
import com.trapezoidlimited.groundforce.viewmodel.AuthViewModel
import com.trapezoidlimited.groundforce.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_locations_verification.*
import retrofit2.Retrofit
import java.io.InputStream
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class CreateProfileFragmentTwo : Fragment() {

    @Inject
    lateinit var loginApiServiceService: ApiService

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var missionsApi: MissionsApi

    private lateinit var viewModel: AuthViewModel

    private var _binding: FragmentCreateProfileTwoBinding? = null

    private val binding get() = _binding!!

    private val gson by lazy { EntryApplication.gson }
    private lateinit var locations: LocationJson
    private var statePicked = ""
    private lateinit var agentData: AgentDataRequest
    private var street = ""
    private var localGovtArea = ""
    private var state = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateProfileTwoBinding.inflate(inflater, container, false)

        val repository = AuthRepositoryImpl(loginApiServiceService, missionsApi)
        val factory = ViewModelFactory(repository, requireContext())
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        /** set navigation arrow from drawable **/
        binding.fragmentCreateProfileTwoIc.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        locations = gson.fromJson(readJson(requireActivity()), LocationJson::class.java)

        street = loadFromSharedPreference(requireActivity(), ADDRESS)
        localGovtArea = loadFromSharedPreference(requireActivity(), LGA)
        state = loadFromSharedPreference(requireActivity(), STATE)


        binding.fragmentCreateProfileTwoStreetEt.text = SpannableStringBuilder(street)
        binding.fragmentCreateProfileTwoLgaTf.editText?.text = SpannableStringBuilder(localGovtArea)
        binding.fragmentCreateProfileTwoStateTf.editText?.text = SpannableStringBuilder(state)



        viewModel.agentCreationResponse.observe(viewLifecycleOwner, {

            when (it) {
                is Resource.Success -> {

                    val userId = it.value.data?.loginToken?.id
                    val userToken = it.value.data?.loginToken?.token

                    /**Saving user's id to sharedPreference */
                    saveToSharedPreference(requireActivity(), USERID, userId!!)

                    /** Saving token to sharedPreference */

                    SessionManager.save(requireContext(), TOKEN, userToken!!)

                    binding.fragmentCreateProfileTwoPb.hide(binding.fragmentCreateProfileTwoBtn)
                    binding.fragmentCreateProfileTwoBtn.isEnabled = true

                    findNavController().navigate(R.id.waitingFragment)

                }
                is Resource.Failure -> {

                    binding.fragmentCreateProfileTwoPb.hide(binding.fragmentCreateProfileTwoBtn)
                    binding.fragmentCreateProfileTwoBtn.isEnabled = true

                    handleApiError(it, retrofit, binding.fragmentCreateProfileTwoBtn)
                }
            }

        })


        //States
        val states: MutableList<String> = mutableListOf()
        for (data in locations.data) {
            states.add(data.state)
        }

        val stateAutoCompleteTextView: AutoCompleteTextView? =
            (binding.fragmentCreateProfileTwoStateTf.editText as? AutoCompleteTextView)

        val adapterState = ArrayAdapter(requireContext(), R.layout.list_item, states)
        stateAutoCompleteTextView?.setAdapter(adapterState)

        //LGAS
        val lga: MutableList<String> = mutableListOf()
        val adapterLGA = ArrayAdapter(requireContext(), R.layout.list_item, lga)
        val lgaAutoCompleteTextView =
            (binding.fragmentCreateProfileTwoLgaTf.editText as? AutoCompleteTextView)
        lgaAutoCompleteTextView?.setAdapter(
            adapterLGA
        )

        //Get State Picked
        val adapterStateObject =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                statePicked = parent.getItemAtPosition(position).toString()
                for (state in locations.data) {
                    if (state.state == statePicked) {
//                        lgaAutoCompleteTextView?.text = SpannableStringBuilder(state.lgas[0])
                        lga.clear()
                        for (data in state.lgas) {
                            lga.add(data)
                        }
                    }
                }
            }


        stateAutoCompleteTextView?.onItemClickListener = adapterStateObject


        /** Navigate to bank detail screen **/
        binding.fragmentCreateProfileTwoBtn.setOnClickListener {


            val residentialAddress = binding.fragmentCreateProfileTwoStreetEt.text.toString()
            val zipCode = binding.fragmentCreateProfileTwoZipCodeTf.editText?.text.toString()
            val lgaSelected = binding.fragmentCreateProfileTwoLgaTf.editText?.text.toString()
            val stateText = binding.fragmentCreateProfileTwoStateTf.editText?.text.toString()

            if (!validateFields()) {
                showSnackBar(binding.fragmentCreateProfileTwoBtn, "Field(s) should not be empty")
                return@setOnClickListener
            } else {

                binding.fragmentCreateProfileTwoBtn.isEnabled = false
                binding.fragmentCreateProfileTwoPb.show(binding.fragmentCreateProfileTwoBtn)

                /** Saving USER PROFILE DETAILS in sharedPreference*/

                saveToSharedPreference(requireActivity(), ZIPCODE, zipCode)
                saveToSharedPreference(requireActivity(), BANKNAME, "Nil")
                saveToSharedPreference(requireActivity(), BANKCODE, "Nil")
                saveToSharedPreference(requireActivity(), ACCOUNTNUMBER, "0000000000")
                saveToSharedPreference(requireActivity(), RELIGION, "Nil")
                saveToSharedPreference(requireActivity(), ADDITIONALPHONENUMBER, "Nil")
                saveToSharedPreference(requireActivity(), GENDER, "m")

                //saveToSharedPreference(requireActivity(), LOCATION_VERIFICATION, "false")

                val lastName = loadFromSharedPreference(requireActivity(), LASTNAME)
                val firstName = loadFromSharedPreference(requireActivity(), FIRSTNAME)
                val phoneNumber = loadFromSharedPreference(requireActivity(), PHONE)
                val gender = loadFromSharedPreference(requireActivity(), GENDER)
                val dob = loadFromSharedPreference(requireActivity(), DOB)
                val email = loadFromSharedPreference(requireActivity(), EMAIL)
                val password = loadFromSharedPreference(requireActivity(), PASSWORD)
                val zipCode = loadFromSharedPreference(requireActivity(), ZIPCODE)
                val longitude = loadFromSharedPreference(requireActivity(), LONGITUDE)
                val latitude = loadFromSharedPreference(requireActivity(), LATITUDE)

                agentData = AgentDataRequest(
                    lastName = lastName,
                    firstName = firstName,
                    phoneNumber = phoneNumber,
                    gender = gender,
                    dob = dob,
                    email = email,
                    password = password,
                    residentialAddress = street,
                    state = state,
                    lga = localGovtArea,
                    zipCode = zipCode,
                    longitude = longitude,
                    latitude = latitude,
                    roles = listOf("agent")
                )

                viewModel.registerAgent(agentData)

            }
        }

        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentCreateProfileTwoIc.toolbarFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.locationsVerificationFragment)
        }
    }

    private fun readJson(context: Context): String? {
        var inputStream: InputStream? = null

        val jsonString: String

        try {
            inputStream = context.assets.open("location.json")

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

    private fun validateFields(): Boolean {

        val fields = mutableListOf(
            JDataClass(
                editText = binding.fragmentCreateProfileTwoStreetEt,
                editTextInputLayout = binding.fragmentCreateProfileTwoStreetTil,
                errorMessage = JDErrorConstants.NAME_FIELD_ERROR,
                validator = { it.jdValidateName(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.fragmentCreateProfileTwoStateTf.editText!!,
                editTextInputLayout = binding.fragmentCreateProfileTwoStateTf,
                errorMessage = JDErrorConstants.NAME_FIELD_ERROR,
                validator = { it.jdValidateName(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.fragmentCreateProfileTwoLgaTf.editText!!,
                editTextInputLayout = binding.fragmentCreateProfileTwoLgaTf,
                errorMessage = JDErrorConstants.NAME_FIELD_ERROR,
                validator = { it.jdValidateName(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.fragmentCreateProfileTwoZipCodeEt,
                editTextInputLayout = binding.fragmentCreateProfileTwoZipCodeTf,
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}