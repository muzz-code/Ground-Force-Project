package com.trapezoidlimited.groundforce.ui.dialog

import android.content.Intent
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
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.data.AgentObject
import com.trapezoidlimited.groundforce.databinding.WelcomeDialogBinding
import com.trapezoidlimited.groundforce.model.request.AgentDataRequest
import com.trapezoidlimited.groundforce.model.request.LoginRequest
import com.trapezoidlimited.groundforce.model.request.UserRequest
import com.trapezoidlimited.groundforce.model.response.LoginResponse
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.ui.dashboard.DashboardActivity
import com.trapezoidlimited.groundforce.utils.*
import com.trapezoidlimited.groundforce.viewmodel.LoginAuthViewModel
import com.trapezoidlimited.groundforce.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeDialog : DialogFragment() {

    @Inject
    lateinit var loginApiService: LoginAuthApi

    @Inject
    lateinit var retrofit: Retrofit

    private var _binding: WelcomeDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LoginAuthViewModel
    private lateinit var progressBar : ProgressBar
    private lateinit var oKTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner)
        _binding = WelcomeDialogBinding.inflate(layoutInflater, container, false)



        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        progressBar = binding.welcomeDialogPb
        oKTextView = binding.welcomeDialogOkTv

        val repository = AuthRepositoryImpl(loginApiService)
        val factory = ViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory).get(LoginAuthViewModel::class.java)


        viewModel.getUserResponse.observe(viewLifecycleOwner, Observer {

            when (it) {
                is Resource.Success -> {

                    Log.i("USER", it.value.data?.firstName!!)
                    Log.i("USER", it.value.data?.lastName!!)

                    setInVisibility(progressBar)
                    setVisibility(oKTextView)
                    goToDashboard()
                    dismiss()
                }
                is Resource.Failure -> {
                    setInVisibility(progressBar)
                    setVisibility(oKTextView)
                    handleApiError(it, retrofit, requireView())
                }
            }
        })


        binding.welcomeDialogOkTv.setOnClickListener {

            val userId = loadFromSharedPreference(requireActivity(), USERID)
            viewModel.getUser(userId)

            setVisibility(progressBar)
            setInVisibility(oKTextView)
        }

    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.25).toInt()
        dialog!!.window?.setLayout(width, height)
    }

    private fun goToDashboard() {
        Intent(requireContext(), DashboardActivity::class.java).also { intent ->
            startActivity(intent)
            requireActivity().finish()
        }
        requireActivity().finish()
    }
}