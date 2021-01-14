package com.trapezoidlimited.groundforce.ui.dashboard.survey

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.api.ApiService
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.databinding.FragmentBeginSurveyBinding
import com.trapezoidlimited.groundforce.databinding.FragmentSurveyListBinding
import com.trapezoidlimited.groundforce.model.request.UpdateSurveyStatusRequest
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.ui.auth.PhoneVerificationFragmentArgs
import com.trapezoidlimited.groundforce.utils.*
import com.trapezoidlimited.groundforce.viewmodel.AuthViewModel
import com.trapezoidlimited.groundforce.viewmodel.MissionsViewModel
import com.trapezoidlimited.groundforce.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import javax.inject.Inject


/**
 * BeginSurvey Fragment
 */

@AndroidEntryPoint
class BeginSurveyFragment : Fragment() {

    @Inject
    lateinit var loginApiServiceService: ApiService

    @Inject
    lateinit var missionsApi: MissionsApi

    @Inject
    lateinit var retrofit: Retrofit

    private lateinit var viewModel: AuthViewModel

    private var _binding: FragmentBeginSurveyBinding? = null
    private val binding get() = _binding!!

    private val args: BeginSurveyFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentBeginSurveyBinding.inflate(inflater, container, false)

        val repository = AuthRepositoryImpl(loginApiServiceService, missionsApi)
        val factory = ViewModelFactory(repository, requireContext())

        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val surveyId = args.surveyId!!

        viewModel.getSurveyByIDResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {

                    binding.beginSurveyPb.hide(binding.beginSurveyFragmentBtn)

                    val questionsIdList = it.value.data?.questions?.toTypedArray()

                    println(questionsIdList)

                    val action =
                        BeginSurveyFragmentDirections.actionBeginSurveyFragment2ToSurveyQuestionsFragment2(questionsIdList)

                    findNavController().navigate(action)
                }
                is Resource.Failure -> {
                    binding.beginSurveyPb.hide(binding.beginSurveyFragmentBtn)
                    handleApiError(it, retrofit, requireView())
                }
            }
        })

        binding.beginSurveyFragmentBtn.setOnClickListener {

            binding.beginSurveyPb.show(binding.beginSurveyFragmentBtn)

            Log.i("SURVEY_ID", surveyId)

            viewModel.getSurveyById(surveyId)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}