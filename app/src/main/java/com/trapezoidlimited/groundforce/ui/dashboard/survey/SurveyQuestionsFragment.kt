package com.trapezoidlimited.groundforce.ui.dashboard.survey

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.adapters.OnQuestionItemClickListener
import com.trapezoidlimited.groundforce.adapters.ViewPagerAdapter
import com.trapezoidlimited.groundforce.api.ApiService
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.databinding.FragmentSurveyQuestionsBinding
import com.trapezoidlimited.groundforce.model.mission.QuestionItem
import com.trapezoidlimited.groundforce.model.request.Question
import com.trapezoidlimited.groundforce.model.request.SubmitSurveyRequest
import com.trapezoidlimited.groundforce.model.request.UpdateSurveyStatusRequest
import com.trapezoidlimited.groundforce.model.response.GetQuestionByIDResponse
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.utils.USERID
import com.trapezoidlimited.groundforce.utils.handleApiError
import com.trapezoidlimited.groundforce.utils.loadFromSharedPreference
import com.trapezoidlimited.groundforce.viewmodel.AuthViewModel
import com.trapezoidlimited.groundforce.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import javax.inject.Inject


/**
 * Survey questions fragment
 */

@AndroidEntryPoint
class SurveyQuestionsFragment : Fragment(), OnQuestionItemClickListener {

    @Inject
    lateinit var loginApiServiceService: ApiService

    @Inject
    lateinit var missionsApi: MissionsApi

    @Inject
    lateinit var retrofit: Retrofit

    private lateinit var viewModel: AuthViewModel

    private var _binding: FragmentSurveyQuestionsBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var adapter: ViewPagerAdapter
    private lateinit var questionItemList: MutableList<QuestionItem>
    private val args: SurveyQuestionsFragmentArgs by navArgs()

    private var questionIdPosition: Int = 0
    private lateinit var questionIdList: Array<String>

    private lateinit var selectedOptionsList: MutableList<Question>

    private lateinit var surveyId: String

    private lateinit var question: GetQuestionByIDResponse

    private lateinit var answers: MutableList<Question>

    private lateinit var selectedOption: Question

    private lateinit var agentId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSurveyQuestionsBinding.inflate(inflater, container, false)

        val repository = AuthRepositoryImpl(loginApiServiceService, missionsApi)
        val factory = ViewModelFactory(repository, requireContext())

        questionIdList = if (args.questionId != null) {
            args.questionId!! //list of questionIds
        } else {
            arrayOf()
        }

        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        adapter = ViewPagerAdapter(mutableListOf(), this)

        /** setting toolbar text **/
        binding.fragmentSurveyQuestToolbar.toolbarTitle.text =
            "${questionIdPosition + 1} of ${questionIdList.size}"

        /** set navigation arrow from drawable **/
        binding.fragmentSurveyQuestToolbar.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val firstQuestionId = questionIdList[questionIdPosition]

        agentId = loadFromSharedPreference(requireActivity(), USERID)

        viewModel.getQuestionById(firstQuestionId)

        questionItemList = mutableListOf()

        answers = mutableListOf()



        viewModel.getQuestionByIDResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    val response = it.value.data

                    surveyId = it.value.data?.surveyId.toString()


                    question = response!!

                    mapQuestionToView()
                    resetOptions()


                }
                is Resource.Failure -> {
                    handleApiError(it, retrofit, requireView())
                }
            }
        })


        viewModel.submitSurveyResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {

                    Toast.makeText(requireContext(), "${it.value.data?.message}", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigate(R.id.agentDashboardFragment)
                }
                is Resource.Failure -> {
                    handleApiError(it, retrofit, requireView())
                }
            }
        })

        viewModel.updateSurveyStatusResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {

                    Toast.makeText(requireContext(), "${it.value.data?.message}", Toast.LENGTH_SHORT)
                        .show()

                    val submitSurveyRequest = SubmitSurveyRequest(agentId, surveyId, answers)
                    viewModel.submitSurvey(submitSurveyRequest)
                }
                is Resource.Failure -> {
                    handleApiError(it, retrofit, requireView())
                }
            }
        })


        //binding.fragmentSurveyQuestVp.adapter = adapter

        Log.d("QUESTIONIDLIST", "${questionIdList!!.lastIndex}")

        if (questionIdPosition == questionIdList!!.lastIndex) {
            // change button text to submit
            binding.surveyQuestionsNextBtn.text = "Submit"
        }


        binding.surveyQuestionsNextBtn.setOnClickListener {

            Log.d("QUESTIONIDPOSITION", "${questionIdPosition}")

            Log.d("QUESTIONIDLIST", "${questionIdList!!.lastIndex}")

            if (binding.surveyQuestionsNextBtn.text == "Submit") {
                // make api call
                Toast.makeText(requireContext(), "Submitted", Toast.LENGTH_SHORT).show()

                val updateSurveyStatusRequest = UpdateSurveyStatusRequest(agentId, surveyId, "accepted")

                viewModel.updateSurveyStatus(updateSurveyStatusRequest)

                return@setOnClickListener
            }

            // Save option
            answers.add(selectedOption)

            //get list of ids
            questionIdPosition++

            if (questionIdPosition == questionIdList!!.lastIndex) {
                // change button text to submit
                binding.surveyQuestionsNextBtn.text = "Submit"
            }


            /** setting toolbar text **/
            binding.fragmentSurveyQuestToolbar.toolbarTitle.text =
                "${questionIdPosition + 1} of ${questionIdList.size}"

            if (questionIdPosition <= questionIdList.lastIndex) {

                viewModel.getQuestionById(questionIdList?.get(questionIdPosition)!!)

                binding.surveyQuestionsNextBtn.isEnabled = false
            }

            println(answers)

        }

        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentSurveyQuestToolbar.toolbarFragment.setNavigationOnClickListener {
            callBackPreviousQuestion()
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            callBackPreviousQuestion()
        }

        binding.surveyQuestionsOptionOneBtn.setOnClickListener {
            optionSelected(it as Button)
        }

        binding.surveyQuestionsOptionTwoBtn.setOnClickListener {
            optionSelected(it as Button)
        }

        binding.surveyQuestionsOptionThreeBtn.setOnClickListener {
            optionSelected(it as Button)
        }


    }

    private fun resetOptions(){
        binding.surveyQuestionsOptionOneBtn.setBackgroundColor(resources.getColor(R.color.colorWhite))
        binding.surveyQuestionsOptionTwoBtn.setBackgroundColor(resources.getColor(R.color.colorWhite))
        binding.surveyQuestionsOptionThreeBtn.setBackgroundColor(resources.getColor(R.color.colorWhite))
    }

    private fun optionSelected(view: Button) {
        binding.surveyQuestionsNextBtn.isEnabled = true
        resetOptions()
        view.setBackgroundColor(resources.getColor(R.color.colorPrimary))

        val options = question.options

        for (opt in options){
            if (opt.option == view.text){
                selectedOption = Question(question.surveyQuestionId, opt.questionOptionId)
                return
            }
        }

    }




    private fun callBackPreviousQuestion() {

        questionIdPosition--

        if (questionIdPosition < 0) {

            findNavController().navigate(R.id.surveyListFragment2)

        } else {

            viewModel.getQuestionById(questionIdList?.get(questionIdPosition)!!)
            /** setting toolbar text **/
            binding.fragmentSurveyQuestToolbar.toolbarTitle.text =
                "${questionIdPosition + 1} of ${questionIdList.size}"

            binding.surveyQuestionsNextBtn.text = "Next"

            //binding.surveyQuestionsNextBtn.isEnabled = false

            answers.removeAt(answers.lastIndex)

        }
    }

    private fun mapQuestionToView() {
        binding.surveyQuestionsQuestTitleTv.text = question.question
        binding.surveyQuestionsOptionOneBtn.text = question.options[0].option
        binding.surveyQuestionsOptionTwoBtn.text = question.options[1].option
        binding.surveyQuestionsOptionThreeBtn.text = question.options[2].option
    }


    override fun onOption(option: String, position: Int) {
        Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}