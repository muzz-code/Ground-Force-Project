package com.trapezoidlimited.groundforce.ui.dashboard.mission

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.trapezoidlimited.groundforce.EntryApplication
import com.trapezoidlimited.groundforce.adapters.mission.OngoingAdapter
import com.trapezoidlimited.groundforce.adapters.mission.OngoingItemClickListener
import com.trapezoidlimited.groundforce.api.ApiService
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.databinding.FragmentOngoingBinding
import com.trapezoidlimited.groundforce.model.mission.OngoingItem
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.room.RoomOngoingMission
import com.trapezoidlimited.groundforce.ui.dashboard.MissionReportActivity
import com.trapezoidlimited.groundforce.ui.main.MainActivity
import com.trapezoidlimited.groundforce.utils.*
import com.trapezoidlimited.groundforce.viewmodel.MissionsViewModel
import com.trapezoidlimited.groundforce.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import javax.inject.Inject

@AndroidEntryPoint
class OngoingFragment : Fragment(), OngoingItemClickListener {

    @Inject
    lateinit var loginApiServiceService: ApiService

    @Inject
    lateinit var missionsApi: MissionsApi

    @Inject
    lateinit var retrofit: Retrofit

    private lateinit var viewModel: MissionsViewModel

    private var _binding: FragmentOngoingBinding? = null
    private val binding get() = _binding!!

    //    private var locationTitlesList = mutableListOf<OngoingItem>()
    private var locationTitlesList = DummyData.ongoingLocationData()

    private val roomViewModel by lazy { EntryApplication.roomViewModel(this) }

    private lateinit var token: String
    private lateinit var userId: String

    private lateinit var ongoingMissionList: MutableList<OngoingItem>

    private lateinit var adapter: OngoingAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOngoingBinding.inflate(inflater, container, false)

        val repository = AuthRepositoryImpl(loginApiServiceService, missionsApi)
        val factory = ViewModelFactory(repository, requireContext())

        viewModel = ViewModelProvider(this, factory).get(MissionsViewModel::class.java)

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = OngoingAdapter(mutableListOf(), this)

        /**
         * Network call to get ongoing missions
         */

        userId = loadFromSharedPreference(requireActivity(), USERID)
        token = "Bearer ${loadFromSharedPreference(requireActivity(), TOKEN)}"

        viewModel.getMissions(userId, "accepted", "1")

        ongoingMissionList = mutableListOf()


        viewModel.getMissionResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {

                    val missions = response.value.data?.data

                    /**
                     * onSuccess add ongoing missions to room database
                     */

                    if (missions != null) {
                        for (mission in missions) {

                            val title = mission.title
                            val description = mission.description
                            val id = mission.missionId

                            val roomOngoingMissionItem = RoomOngoingMission(id, title, description)

                            roomViewModel.addOngoingMission(roomOngoingMissionItem)
                        }
                    }

                    /**
                     * Read from room database
                     */

                    readOngoingMissionsFromRoom()
                }

                is Resource.Failure -> {

                    if (response.errorCode == UNAUTHORIZED) {
                        Intent(requireContext(), MainActivity::class.java).also {
                            saveToSharedPreference(requireActivity(), LOG_OUT, "true")
                            startActivity(it)
                            requireActivity().finish()
                        }
                    } else {
                        handleApiError(roomViewModel, requireActivity(), response, retrofit, requireView())
                    }

                    /**
                     * Read from room database
                     */

                    readOngoingMissionsFromRoom()
                }
            }
        })


        binding.fragmentOngoingRv.adapter = adapter
        binding.fragmentOngoingRv.layoutManager = LinearLayoutManager(this.context)

    }

    override fun onResume() {
        super.onResume()

        /**
         * Network call to get ongoing missions onRefresh
         */

        binding.fragmentOngoingSrl.setOnRefreshListener {
            viewModel.getMissions(userId, "accepted", "1")
            adapter.notifyDataSetChanged()

            binding.fragmentOngoingSrl.isRefreshing = false
        }

    }


    /**
     * This method will handle the action onclick of the verify button */

    override fun onVerifyBtnClick(ongoing: OngoingItem, position: Int, id: String) {


        /** navigate to the verification page */
        Intent(requireContext(), MissionReportActivity::class.java).also {
            it.putExtra("POSITION", position)
            it.putExtra("MISSIONID", id)
            startActivity(it)
        }

    }

    private fun setNoOngoingViewVisible() {
        setInVisibility(binding.fragmentOngoingRv)
        setVisibility(binding.fragmentEmptyOngoing.ongoingShoesIv)
        setVisibility(binding.fragmentEmptyOngoing.ongoingNoOngoingTv)
        setVisibility(binding.fragmentEmptyOngoing.ongoingNoOngoingTwoTv)
        setInVisibility(binding.fragmentEmptyOngoing.fragmentOngoingMissionPb)
    }

    private fun setNoOngoingViewInVisible() {
        setVisibility(binding.fragmentOngoingRv)
        setInVisibility(binding.fragmentEmptyOngoing.ongoingShoesIv)
        setInVisibility(binding.fragmentEmptyOngoing.ongoingNoOngoingTv)
        setInVisibility(binding.fragmentEmptyOngoing.ongoingNoOngoingTwoTv)
        setInVisibility(binding.fragmentEmptyOngoing.fragmentOngoingMissionPb)
    }

    private fun readOngoingMissionsFromRoom() {
        roomViewModel.ongoingMission.observe(viewLifecycleOwner, Observer {

            ongoingMissionList.clear()

            /**
             * adding assigned missions to recyclerview list
             */

            for (ongoingMission in it) {
                val title = ongoingMission.locationTitle
                val description = ongoingMission.locationSubTitle
                val ongoingMissionId = ongoingMission.Id

                val ongoingMissionItem = OngoingItem(title, description, id = ongoingMissionId)

                ongoingMissionList.add(ongoingMissionItem)

                adapter.setMyList(ongoingMissionList)
                adapter.notifyDataSetChanged()
            }

            if (ongoingMissionList.size == 0) {
                setNoOngoingViewVisible()
            } else {
                setNoOngoingViewInVisible()
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}