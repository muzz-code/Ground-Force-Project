package com.trapezoidlimited.groundforce.ui.dashboard.survey

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.trapezoidlimited.groundforce.EntryApplication
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.adapters.SurveyRecyclerAdapter
import com.trapezoidlimited.groundforce.api.ApiService
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.data.SurveyData
import com.trapezoidlimited.groundforce.databinding.FragmentSurveyListBinding
import com.trapezoidlimited.groundforce.model.mission.MissionItem
import com.trapezoidlimited.groundforce.model.mission.SurveyItem
import com.trapezoidlimited.groundforce.model.request.UpdateSurveyStatusRequest
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.room.RoomSurvey
import com.trapezoidlimited.groundforce.utils.*
import com.trapezoidlimited.groundforce.viewmodel.AuthViewModel
import com.trapezoidlimited.groundforce.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import javax.inject.Inject

@AndroidEntryPoint
class SurveyListFragment : Fragment(), SurveyRecyclerAdapter.OnSurveyClickListener {

    @Inject
    lateinit var loginApiServiceService: ApiService

    @Inject
    lateinit var missionsApi: MissionsApi

    @Inject
    lateinit var retrofit: Retrofit

    private lateinit var viewModel: AuthViewModel

    private var _binding: FragmentSurveyListBinding? = null

    private val binding get() = _binding!!

    private lateinit var surveyTopicList: MutableList<SurveyData>

    private lateinit var adapter: SurveyRecyclerAdapter

    private val roomViewModel by lazy { EntryApplication.roomViewModel(this) }

    private lateinit var surveyId: String
    private var surveyPosition: Int = 0

    private lateinit var agentID: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSurveyListBinding.inflate(inflater, container, false)
        /** setting toolbar text **/
        binding.fragmentSurveyListToolbar.toolbarTitle.text = getString(R.string.surveys_title_str)

        /** set navigation arrow from drawable **/
        binding.fragmentSurveyListToolbar.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)


        val repository = AuthRepositoryImpl(loginApiServiceService, missionsApi)
        val factory = ViewModelFactory(repository, requireContext())
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        agentID = loadFromSharedPreference(requireActivity(), USERID)

        viewModel.getSurvey(agentID, "pending", 1)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = SurveyRecyclerAdapter(mutableListOf(), this)

        surveyTopicList = mutableListOf()


        viewModel.getSurveyResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {

                    setInVisibility(binding.fragmentSurveyListPg)

                    val userSurveyToReturnListOfObjects = it.value.data?.userSurveyToReturn

                    if (userSurveyToReturnListOfObjects != null) {

                        for (userSurveyToReturnListOfObject in userSurveyToReturnListOfObjects) {

                            val title = userSurveyToReturnListOfObject.surveyType
                            val topic = userSurveyToReturnListOfObject.topic
                            val surveyId = userSurveyToReturnListOfObject.surveyId

                            val roomSurvey = RoomSurvey(surveyId, title, topic)

                            roomViewModel.addSurvey(roomSurvey)

                        }

                    }

                    readSurveysFromRoom()
                }
                is Resource.Failure -> {

                    setInVisibility(binding.fragmentSurveyListPg)

                    handleApiError(roomViewModel, requireActivity(), it, retrofit, requireView())

                    readSurveysFromRoom()
                }
            }
        })


        viewModel.updateSurveyStatusResponse.observe(viewLifecycleOwner, {
            when (it) {

                is Resource.Success -> {

                    surveyTopicList.removeAt(surveyPosition)
                    roomViewModel.deleteBySurveyId(surveyId)


                    Toast.makeText(requireContext(), "${it.value.data?.message}", Toast.LENGTH_SHORT)
                        .show()

                    val action = SurveyListFragmentDirections.actionSurveyListFragment2ToBeginSurveyFragment2(surveyId)

                    findNavController().navigate(action)
                }

                is Resource.Failure -> {
                    handleApiError(roomViewModel, requireActivity(), it, retrofit, requireView())
                }
            }
        })




        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentSurveyListToolbar.toolbarFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        //initialize survey recycler view adapter and  Layout Manager
        binding.fragmentSurveyListRecyclerView.adapter = adapter
        binding.fragmentSurveyListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onAcceptClick(survey: SurveyData, position: Int, id: String) {

        surveyId = id
        surveyPosition = position

        //val updateSurveyStatusRequest = UpdateSurveyStatusRequest(agentID, id, "declined")

        //viewModel.updateSurveyStatus(updateSurveyStatusRequest)

        surveyTopicList.removeAt(surveyPosition)
        roomViewModel.deleteBySurveyId(surveyId)

        val action = SurveyListFragmentDirections.actionSurveyListFragment2ToBeginSurveyFragment2(surveyId)

        findNavController().navigate(action)


    }

    override fun onDeleteClick(survey: SurveyData, position: Int, id: String) {


        val updateSurveyStatusRequest = UpdateSurveyStatusRequest(agentID, id, "declined")

        viewModel.updateSurveyStatus(updateSurveyStatusRequest)

        adapter.notifyItemRemoved(position)
    }


    private fun readSurveysFromRoom() {
        roomViewModel.survey.observe(viewLifecycleOwner, {

            surveyTopicList.clear()

            /**
             * adding assigned survey to recyclerview list
             */

            for (survey in it) {
                val title = survey.title
                val body = survey.body
                val progress = 40
                val id = survey.Id

                val surveyData = SurveyData(title, body, progress, id)

                val gson = Gson()
                val strObj = gson.toJson(surveyData)

                println(strObj)

                surveyTopicList.add(surveyData)

                Log.i("TITLE", title)

                adapter.setMyList(surveyTopicList)
                adapter.notifyDataSetChanged()
            }


        }

        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}