package com.trapezoidlimited.groundforce.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.api.LoginAuthApi
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.model.request.AgentDataRequest
import com.trapezoidlimited.groundforce.databinding.VerificationResultPageBinding
import com.trapezoidlimited.groundforce.model.request.VerifyLocationRequest
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.utils.*
import com.trapezoidlimited.groundforce.viewmodel.AuthViewModel
import com.trapezoidlimited.groundforce.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_locations_verification.*
import retrofit2.Retrofit
import javax.inject.Inject


@AndroidEntryPoint
class VerifiedDialog : DialogFragment() {
    @Inject
    lateinit var loginApiService: LoginAuthApi

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var missionsApi: MissionsApi

    private var _binding: VerificationResultPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AuthViewModel
    private lateinit var agentData: AgentDataRequest
    private lateinit var progressBar: ProgressBar
    private lateinit var okTextView: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner);
        _binding = VerificationResultPageBinding.inflate(inflater, container, false)

        val repository = AuthRepositoryImpl(loginApiService, missionsApi)
        val factory = ViewModelFactory(repository, requireContext())

        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        progressBar = binding.locationVerifiedDialogPb
        okTextView = binding.locationVerifiedDialogTv


        viewModel.agentCreationResponse.observe(viewLifecycleOwner, Observer {

            when (it) {
                is Resource.Success -> {

                    val userId = it.value.data?.loginToken?.id
                    val userToken = it.value.data?.loginToken?.token

                    /**Saving user's id to sharedPreference */
                    saveToSharedPreference(requireActivity(), USERID, userId!!)

                    /** Saving token to sharedPreference */

                    SessionManager.save(requireContext(), TOKEN, userToken!!)

                    //saveToSharedPreference(requireActivity(), TOKEN, userToken!! )

                    setInVisibility(progressBar)
                    findNavController().navigate(R.id.waitingFragment)

                    dismiss()
                }
                is Resource.Failure -> {
                    setInVisibility(progressBar)
                    //setVisibility(okTextView)
                    handleApiError(it, retrofit, requireActivity().view)
                    dismiss()
                }
            }

        })

        viewModel.verifyLocationResponse.observe(viewLifecycleOwner, {

            when (it) {
                is Resource.Success -> {
                    setInVisibility(progressBar)


                    Toast.makeText(
                        requireContext(),
                        "${it.value.data?.message}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    findNavController().navigate(R.id.userProfileFragment)
                    dismiss()
                }
                is Resource.Failure -> {
                    setInVisibility(progressBar)
                    //setVisibility(okTextView)
                    handleApiError(it, retrofit, requireView())
                    dismiss()
                }
            }

        })



        if (DataListener.currentScreen == LOCATION_VERIFICATION_SCREEN) {

            Log.i("LOCATION", "LOCATION_VERIFICATION_SCREEN")

            okTextView.setOnClickListener {

                val lastName = loadFromSharedPreference(requireActivity(), LASTNAME)
                val firstName = loadFromSharedPreference(requireActivity(), FIRSTNAME)
                val phoneNumber = loadFromSharedPreference(requireActivity(), PHONE)
                val gender = loadFromSharedPreference(requireActivity(), GENDER)
                val dob = loadFromSharedPreference(requireActivity(), DOB)
                val email = loadFromSharedPreference(requireActivity(), EMAIL)
                val password = loadFromSharedPreference(requireActivity(), PASSWORD)
                val residentialAddress = loadFromSharedPreference(requireActivity(), ADDRESS)
                val state = loadFromSharedPreference(requireActivity(), STATE)
                val lga = loadFromSharedPreference(requireActivity(), LGA)
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
                    residentialAddress = residentialAddress,
                    state = state,
                    lga = lga,
                    zipCode = zipCode,
                    longitude = longitude,
                    latitude = latitude,
                    roles = listOf("agent")
                )


                setVisibility(progressBar)
                //setInVisibility(okTextView)

                viewModel.registerAgent(agentData)

            }

        } else if (DataListener.currentScreen == VERIFY_LOCATION_SCREEN) {

            okTextView.setOnClickListener {

                Log.i("LOCATION", "VERIFY_LOCATION_SCREEN")

                saveToSharedPreference(requireActivity(), LOCATION_VERIFICATION, "false")

                setVisibility(progressBar)

                val longitude = loadFromSharedPreference(requireActivity(), LONGITUDE)
                val latitude = loadFromSharedPreference(requireActivity(), LATITUDE)

                val verifyLocationRequest = VerifyLocationRequest(longitude, latitude)

                viewModel.verifyLocation(verifyLocationRequest)
            }

        }

    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.55).toInt()
        dialog!!.window?.setLayout(width, height)
    }

}