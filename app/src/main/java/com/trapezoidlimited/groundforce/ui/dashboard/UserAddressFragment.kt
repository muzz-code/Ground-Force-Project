package com.trapezoidlimited.groundforce.ui.dashboard

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationRequest
import com.trapezoidlimited.groundforce.EntryApplication
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentUserAddressBinding
import com.trapezoidlimited.groundforce.model.GeoPoints
import com.trapezoidlimited.groundforce.model.LocationJson
import com.trapezoidlimited.groundforce.model.request.AgentDataRequest
import com.trapezoidlimited.groundforce.ui.profile.CreateProfileFragmentTwoDirections
import com.trapezoidlimited.groundforce.utils.*
import java.io.InputStream
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit


class UserAddressFragment : Fragment() {

    private var _binding: FragmentUserAddressBinding? = null
    private val binding
        get() = _binding!!

    private val gson by lazy { EntryApplication.gson }
    private lateinit var locations: LocationJson
    private var statePicked = ""
    private lateinit var geoPoints: GeoPoints
    private lateinit var geocoder: Geocoder
    private lateinit var addresses: List<Address>
    private var locationLat: Double = 0.0
    private var locationLong: Double = 0.0
    private lateinit var locationRequest: LocationRequest
    private val LOCATION_REQUEST_CODE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentUserAddressBinding.inflate(inflater, container, false)

        /** set navigation arrow from drawable **/
        binding.fragmentUserAddressIc.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)

        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentUserAddressIc.toolbarFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.userProfileFragment)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        locations = gson.fromJson(readJson(requireActivity()), LocationJson::class.java)
        geocoder = Geocoder(requireContext(), Locale.getDefault())

        locationRequest = LocationRequest().apply {
            interval = TimeUnit.SECONDS.toMillis(1000)
            fastestInterval = TimeUnit.SECONDS.toMillis(2000)
            maxWaitTime = TimeUnit.MINUTES.toMillis(1)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        checkGPSEnabled(LOCATION_REQUEST_CODE, locationRequest)


        //States
        val states: MutableList<String> = mutableListOf()
        for (data in locations.data) {
            states.add(data.state)
        }

        val stateAutoCompleteTextView: AutoCompleteTextView? =
            (binding.fragmentUserAddressStateTf.editText as? AutoCompleteTextView)

        val adapterState = ArrayAdapter(requireContext(), R.layout.list_item, states)
        stateAutoCompleteTextView?.setAdapter(adapterState)

        //LGAS
        val lga: MutableList<String> = mutableListOf()
        val adapterLGA = ArrayAdapter(requireContext(), R.layout.list_item, lga)
        val lgaAutoCompleteTextView =
            (binding.fragmentUserAddressLgaTf.editText as? AutoCompleteTextView)
        lgaAutoCompleteTextView?.setAdapter(
            adapterLGA
        )

        //Get State Picked
        val adapterStateObject =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                statePicked = parent.getItemAtPosition(position).toString()
                for (state in locations.data) {
                    if (state.state == statePicked) {
                        //lgaAutoCompleteTextView?.text = SpannableStringBuilder(state.lgas[0])
                        lga.clear()
                        for (data in state.lgas) {
                            lga.add(data)
                        }
                    }
                }
            }


        stateAutoCompleteTextView?.onItemClickListener = adapterStateObject


        binding.fragmentUserAddressBtn.setOnClickListener {
            val residentialAddress = binding.fragmentUserAddressStreetEt.text.toString()
            val zipCode = binding.fragmentUserAddressZipCodeTf.editText?.text.toString()
            val lgaSelected = binding.fragmentUserAddressLgaTf.editText?.text.toString()
            val stateText = binding.fragmentUserAddressStateTf.editText?.text.toString()


            if (!validateFields()) {
                showSnackBar(binding.fragmentUserAddressBtn, "Field(s) should not be empty")
                return@setOnClickListener
            } else {

                val fullAddress = "$residentialAddress, $lgaSelected, $stateText, Nigeria"

                println(fullAddress)

                geoPoints = getLocationFromAddress(fullAddress)

                /** Saving USER PROFILE DETAILS in sharedPreference*/
                saveToSharedPreference(requireActivity(), ADDRESS, residentialAddress)
                saveToSharedPreference(requireActivity(), ZIPCODE, zipCode)
                saveToSharedPreference(requireActivity(), STATE, stateText)
                saveToSharedPreference(requireActivity(), LGA, lgaSelected)

                val action = UserAddressFragmentDirections.actionUserAddressFragmentToVerifyLocationFragment(geoPoints)

                findNavController().navigate(action)
            }
        }

    }

    private fun getLocationFromAddress(address: String): GeoPoints {

        try {
            addresses = geocoder.getFromLocationName(address, 5)

            val location = addresses[0]
            locationLat = location.latitude
            locationLong = location.longitude

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Address does not exist.", Toast.LENGTH_SHORT).show()
        }

        return GeoPoints(latitude = locationLat, longitude = locationLong)

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
                editText = binding.fragmentUserAddressStreetEt,
                editTextInputLayout = binding.fragmentUserAddressStreetTil,
                errorMessage = JDErrorConstants.NAME_FIELD_ERROR,
                validator = { it.jdValidateName(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.fragmentUserAddressStateTf.editText!!,
                editTextInputLayout = binding.fragmentUserAddressStateTf,
                errorMessage = JDErrorConstants.NAME_FIELD_ERROR,
                validator = { it.jdValidateName(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.fragmentUserAddressLgaTf.editText!!,
                editTextInputLayout = binding.fragmentUserAddressLgaTf,
                errorMessage = JDErrorConstants.NAME_FIELD_ERROR,
                validator = { it.jdValidateName(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.fragmentUserAddressZipCodeEt,
                editTextInputLayout = binding.fragmentUserAddressZipCodeTf,
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