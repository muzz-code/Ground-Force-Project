package com.trapezoidlimited.groundforce.ui.dashboard.history

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.trapezoidlimited.groundforce.EntryApplication
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.adapters.history.HistoryMissionAdapter
import com.trapezoidlimited.groundforce.adapters.history.OnHistoryItemClickListener
import com.trapezoidlimited.groundforce.api.ApiService
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.databinding.FragmentHistoryMissionBinding
import com.trapezoidlimited.groundforce.databinding.FragmentMissionBinding
import com.trapezoidlimited.groundforce.databinding.FragmentOngoingBinding
import com.trapezoidlimited.groundforce.model.mission.MissionItem
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.room.RoomHistoryMission
import com.trapezoidlimited.groundforce.room.RoomMission
import com.trapezoidlimited.groundforce.utils.*
import com.trapezoidlimited.groundforce.viewmodel.MissionsViewModel
import com.trapezoidlimited.groundforce.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import java.lang.StringBuilder
import javax.inject.Inject


@AndroidEntryPoint
class HistoryMissionFragment : Fragment(), OnHistoryItemClickListener {

    @Inject
    lateinit var loginApiServiceService: ApiService

    @Inject
    lateinit var missionsApi: MissionsApi

    @Inject
    lateinit var retrofit: Retrofit
    private lateinit var viewModel: MissionsViewModel

    private var _binding: FragmentHistoryMissionBinding? = null
    private val binding get() = _binding!!

    private lateinit var historyMissionList: MutableList<MissionItem>
    private var adapter = HistoryMissionAdapter(
        mutableListOf(),
        this
    )
    private lateinit var userId: String

    private val roomViewModel by lazy { EntryApplication.roomViewModel(this) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val repository = AuthRepositoryImpl(loginApiServiceService, missionsApi)
        val factory = ViewModelFactory(repository, requireContext())

        viewModel = ViewModelProvider(this, factory).get(MissionsViewModel::class.java)

        _binding = FragmentHistoryMissionBinding.inflate(inflater, container, false)

        /**
         * Clear all history mission from room
         */

        roomViewModel.deleteAllHistoryMission()

        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        /**
         * Network call to get assigned missions
         */

        userId = loadFromSharedPreference(requireActivity(), USERID)

        viewModel.getMissions(userId, "verified", "1")

        historyMissionList = mutableListOf()


        viewModel.getMissionResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {

                    val historyMissions = response.value.data?.data

                    /**
                     * onSuccess add assigned missions to room database
                     */

                    if (historyMissions != null) {
                        for (historyMission in historyMissions) {

                            val title = historyMission.title
                            val description = historyMission.description
                            val id = historyMission.missionId
                            val date = historyMission.createdAt

                            val roomHistoryMissionItem =
                                RoomHistoryMission(id, title, description, date)

                            roomViewModel.addHistoryMission(roomHistoryMissionItem)

                            readHistoryMissionsFromRoom()

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
                    readHistoryMissionsFromRoom()
                }
            }
        })


        binding.fragmentHistoryMissionRv.adapter = adapter
        binding.fragmentHistoryMissionRv.layoutManager = LinearLayoutManager(this.context)


    }

    private fun readHistoryMissionsFromRoom() {
        Log.i("READHISTORY", "RUNS")

        roomViewModel.historyMission.observe(viewLifecycleOwner, Observer {

            Log.i("READHISTORYVIEWMODEL", "RUNS")

            historyMissionList.clear()

            /**
             * adding assigned history missions to recyclerview list
             */

            /**
             * adding assigned history missions to recyclerview list
             */

            for (historyMission in it) {
                val title = historyMission.locationTitle
                val description = historyMission.locationSubTitle
                val historyMissionId = historyMission.Id
                val date = historyMission.date
                var dateInWord = ""

                if (date != null) {
                    dateInWord = splitDate(date)
                }

                val historyMissionItem =
                    MissionItem(title, description, historyMissionId, dateInWord)


                historyMissionList.add(historyMissionItem)

                Log.i("TITLE", title)

                adapter.setMyList(historyMissionList)
                adapter.notifyDataSetChanged()
            }

        }

        )
    }


    override fun onVerifyClick(missionItem: MissionItem, position: Int) {
        println("Clicked")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}