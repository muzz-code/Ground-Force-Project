package com.trapezoidlimited.groundforce.ui.dashboard.history

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.trapezoidlimited.groundforce.EntryApplication
import com.trapezoidlimited.groundforce.adapters.history.HistorySurveyAdapter
import com.trapezoidlimited.groundforce.adapters.history.OnHistorySurveyItemClickListener
import com.trapezoidlimited.groundforce.api.ApiService
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.data.SurveyData
import com.trapezoidlimited.groundforce.databinding.FragmentHistorySurveyBinding
import com.trapezoidlimited.groundforce.model.mission.SurveyItem
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.room.RoomHistoryMission
import com.trapezoidlimited.groundforce.room.RoomHistorySurvey
import com.trapezoidlimited.groundforce.utils.DummyData
import com.trapezoidlimited.groundforce.utils.USERID
import com.trapezoidlimited.groundforce.utils.handleApiError
import com.trapezoidlimited.groundforce.utils.loadFromSharedPreference
import com.trapezoidlimited.groundforce.viewmodel.AuthViewModel
import com.trapezoidlimited.groundforce.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import javax.inject.Inject

@AndroidEntryPoint
class HistorySurveyFragment : Fragment(), OnHistorySurveyItemClickListener {

    @Inject
    lateinit var loginApiServiceService: ApiService

    @Inject
    lateinit var missionsApi: MissionsApi

    @Inject
    lateinit var retrofit: Retrofit

    private var _binding: FragmentHistorySurveyBinding? = null
    private val binding get() = _binding!!

    private lateinit var historySurveyList : MutableList<SurveyItem>
    private var adapter = HistorySurveyAdapter(
        mutableListOf(),
        this
    )
    private lateinit var userId: String
    private lateinit var viewModel: AuthViewModel

    private val roomViewModel by lazy { EntryApplication.roomViewModel(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val repository = AuthRepositoryImpl(loginApiServiceService, missionsApi)
        val factory = ViewModelFactory(repository, requireContext())
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        _binding = FragmentHistorySurveyBinding.inflate(inflater, container, false)

        /**
         * Clear all history Survey from room
         */

        roomViewModel.deleteAllHistorySurvey()

        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        userId = loadFromSharedPreference(requireActivity(), USERID)

        viewModel.getSurvey(userId, "completed", 1)

        historySurveyList = mutableListOf()

        viewModel.getSurveyResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {

                    val historySurveys = response.value.data?.userSurveyToReturn

                    /**
                     * onSuccess add assigned missions to room database
                     */

                    if (historySurveys != null) {
                        for (historySurvey in historySurveys) {

                            val title = "Human Resource Survey, 2020"
                            val topic = historySurvey.topic
                            val id = historySurvey.surveyId

                            val roomHistorySurveyItem =
                                RoomHistorySurvey(id, title, topic)

                            roomViewModel.addHistorySurvey(roomHistorySurveyItem)

                            readHistorySurveysFromRoom()

                        }

                    }

                }
                is Resource.Failure -> {
                    Log.i("FAILED", "${response.errorCode}")

                    handleApiError(
                        roomViewModel,
                        requireActivity(),
                        response,
                        retrofit,
                        requireView()
                    )
                    readHistorySurveysFromRoom()
                }
            }
        })


        binding.fragmentHistoryMissionRv.adapter = adapter
        binding.fragmentHistoryMissionRv.layoutManager = LinearLayoutManager(this.context)
    }

    private fun readHistorySurveysFromRoom() {
        roomViewModel.historySurvey.observe(viewLifecycleOwner, {

            historySurveyList.clear()

            /**
             * adding assigned survey to recyclerview list
             */

            for (survey in it) {
                val title = survey.title
                val body = survey.body
                val id = survey.Id

                val historySurvey = SurveyItem(title, body, id)

                historySurveyList.add(historySurvey)

                Log.i("TITLE", title)

                adapter.setMyList(historySurveyList)
                adapter.notifyDataSetChanged()
            }

        }

        )
    }

    override fun onCompletedClick(surveyItem: SurveyItem, position: Int) {
        println("Clicked")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}