package com.trapezoidlimited.groundforce.ui.profile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.EntryApplication
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentCreateProfileTwoBinding
import com.trapezoidlimited.groundforce.model.LocationJson
import com.trapezoidlimited.groundforce.utils.*
import java.io.InputStream
import java.lang.Exception

class CreateProfileFragmentTwo : Fragment() {

    private var _binding: FragmentCreateProfileTwoBinding? = null

    private val binding get() = _binding!!

    private val gson by lazy { EntryApplication.gson }
    private lateinit var locations: LocationJson
    private var statePicked = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateProfileTwoBinding.inflate(inflater, container, false)

        /** set navigation arrow from drawable **/
        binding.fragmentCreateProfileTwoIc.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        locations = gson.fromJson(readJson(requireActivity()), LocationJson::class.java)


        //ZipCodes
        val zipCodes = listOf("110000", "102010", "100000", "103040")
        val adapterZipCode = ArrayAdapter(requireContext(), R.layout.list_item, zipCodes)
        (binding.fragmentCreateProfileTwoZipCodeTf.editText as? AutoCompleteTextView)?.setAdapter(
            adapterZipCode
        )


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
            if (!validateFields()) {
                showSnackBar(binding.fragmentCreateProfileTwoBtn, "Field(s) should not be empty")
                return@setOnClickListener
            } else {

                val residentialAddress = binding.fragmentCreateProfileTwoStreetEt.text.toString()
                val zipCode = binding.fragmentCreateProfileTwoZipCodeTf.editText?.text.toString()
                val lga = binding.fragmentCreateProfileTwoLgaTf.editText?.text.toString()
                val state = binding.fragmentCreateProfileTwoStateTf.editText?.text.toString()

                /** Saving USER PROFILE DETAILS in sharedPreference*/

                saveToSharedPreference(requireActivity(), ADDRESS, residentialAddress)
                saveToSharedPreference(requireActivity(), ZIPCODE, zipCode)
                saveToSharedPreference(requireActivity(), LGA, lga)
                saveToSharedPreference(requireActivity(), STATE, state)
                saveToSharedPreference(requireActivity(), GENDER, "m")

                findNavController().navigate(R.id.action_createProfileFragmentTwo_to_locationsVerificationFragment)
            }
        }

        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentCreateProfileTwoIc.toolbarFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
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
                editText = binding.fragmentCreateProfileTwoZipCodeTf.editText!!,
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


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}