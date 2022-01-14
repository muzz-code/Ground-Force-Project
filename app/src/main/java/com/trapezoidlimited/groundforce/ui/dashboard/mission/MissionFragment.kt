package com.trapezoidlimited.groundforce.ui.dashboard.mission


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.trapezoidlimited.groundforce.EntryApplication
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.adapters.mission.MissionAdapter
import com.trapezoidlimited.groundforce.adapters.mission.OnMissionItemClickListener
import com.trapezoidlimited.groundforce.api.ApiService
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.databinding.FragmentMissionBinding
import com.trapezoidlimited.groundforce.model.mission.MissionItem
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.room.RoomMission
import com.trapezoidlimited.groundforce.ui.main.MainActivity
import com.trapezoidlimited.groundforce.utils.*
import com.trapezoidlimited.groundforce.viewmodel.MissionsViewModel
import com.trapezoidlimited.groundforce.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import javax.inject.Inject

@AndroidEntryPoint
class MissionFragment : Fragment(), OnMissionItemClickListener {

    @Inject
    lateinit var loginApiServiceService: ApiService

    @Inject
    lateinit var missionsApi: MissionsApi

    @Inject
    lateinit var retrofit: Retrofit

    private lateinit var viewModel: MissionsViewModel

    private var _binding: FragmentMissionBinding? = null
    private val binding get() = _binding!!
    private lateinit var missionList: MutableList<MissionItem>

    private lateinit var adapter: MissionAdapter

    private val roomViewModel by lazy { EntryApplication.roomViewModel(this) }

    private lateinit var userId: String
    private lateinit var missionId: String
    private var missionPosition: Int = 0

    private lateinit var missionBadgeTextView: TextView

    private var indicatorChecker: Boolean = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val repository = AuthRepositoryImpl(loginApiServiceService, missionsApi)
        val factory = ViewModelFactory(repository, requireContext())

        viewModel = ViewModelProvider(this, factory).get(MissionsViewModel::class.java)


        _binding = FragmentMissionBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = MissionAdapter(mutableListOf(), this)

        val agentDashboardFragment = layoutInflater.inflate(R.layout.fragment_agent_dashboard, null)

       missionBadgeTextView = agentDashboardFragment.findViewById(R.id.fragment_missions_missions_notif_icon_tv)

        /**
         * Network call to get assigned missions
         */

        userId = loadFromSharedPreference(requireActivity(), USERID)

        viewModel.getMissions(userId, "pending", "1")

        missionList = mutableListOf()


        viewModel.getMissionResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {

                    val missions = response.value.data?.data

                    /**
                     * onSuccess add assigned missions to room database
                     */

                    if (missions != null) {
                        for (mission in missions) {

                            val title = mission.title
                            val description = mission.description
                            val id = mission.missionId

                            val roomMissionItem = RoomMission(id, title, description)

                            roomViewModel.addMission(roomMissionItem)

                        }

                    }

                    /**
                     * Read from room database
                     */

                    readMissionsFromRoom()
                }

                is Resource.Failure -> {

                    Log.i("FAILED", "${response.errorCode}")

                        handleApiError(roomViewModel, requireActivity(), response, retrofit, requireView())

                    /**
                     * Read from room database
                     */

                    readMissionsFromRoom()
                }
            }
        })


        binding.fragmentMissionRv.adapter = adapter
        binding.fragmentMissionRv.layoutManager = LinearLayoutManager(this.context)


        viewModel.updateMissionStatusResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {

                    /** setting the indicator  on the onGoing tab onclick of the accept btn */


                    if (indicatorChecker == true) {
                        DataListener.mSetTabIndicator.value = true
                    }

                    /** mission data is removed from the missions list
                    * And then, this mission data is removed from the missions table in Room.
                     */

                    missionList.removeAt(missionPosition)
                    adapter.notifyItemRemoved(missionPosition)

                    roomViewModel.deleteByMissionId(missionId)

                    it.value.data?.message?.let { it1 -> showSnackBar(requireView(), it1) }
                }

                is Resource.Failure -> {

                    handleApiError(roomViewModel, requireActivity() ,it, retrofit, binding.fragmentMissionRv)

                }
            }
        })


    }

    override fun onResume() {
        super.onResume()

        binding.fragmentMissionSrl.setOnRefreshListener {

            /**
             * Network call to get assigned missions
             */

            viewModel.getMissions(userId, "pending", "1")
            adapter.notifyDataSetChanged()

            binding.fragmentMissionSrl.isRefreshing = false
        }
    }




    /**
     * Method handles the actions onclick of the accept btn */

    override fun onAcceptClick(mission: MissionItem, position: Int, id: String) {

        indicatorChecker = true
        missionId = id
        missionPosition = position

        /** Onclick of the accept btn, a network call is made to update the status of the mission
         */

        viewModel.updateMissionStatus(id, "accepted")


        adapter.notifyItemRemoved(position)

        if (missionList.size == 0) {
            setNoMissionViewVisible()
        }
    }

    /**
     * Method handles the actions onclick of the delete btn */

    override fun onDeclineClick(mission: MissionItem, position: Int, id: String) {

        /** Onclick of the decline btn, a network call is made to update the status of the mission
         * this mission data is removed from the missions list
         * And then, this mission data is removed from the missions table in Room.
         **/


        indicatorChecker = false

        missionId = id
        missionPosition = position

        viewModel.updateMissionStatus(id, "declined")


        adapter.notifyItemRemoved(position)

        if (missionList.size == 0) {
            setNoMissionViewVisible()
        }
    }

    private fun setNoMissionViewVisible() {
        setInVisibility(binding.fragmentMissionRv)
        setVisibility(binding.fragmentEmptyMission.missionShoesIv)
        setVisibility(binding.fragmentEmptyMission.missionNoMissionTv)
        setVisibility(binding.fragmentEmptyMission.missionNoMissionTwoTv)
        setInVisibility(binding.fragmentEmptyMission.fragmentMissionPb)
    }

    private fun setNoMissionViewInVisible() {
        setVisibility(binding.fragmentMissionRv)
        setInVisibility(binding.fragmentEmptyMission.missionShoesIv)
        setInVisibility(binding.fragmentEmptyMission.missionNoMissionTv)
        setInVisibility(binding.fragmentEmptyMission.missionNoMissionTwoTv)
        setInVisibility(binding.fragmentEmptyMission.fragmentMissionPb)
    }

    private fun readMissionsFromRoom() {
        roomViewModel.mission.observe(viewLifecycleOwner, Observer {

            missionList.clear()

            /**
             * adding assigned missions to recyclerview list
             */

            for (mission in it) {
                val title = mission.locationTitle
                val description = mission.locationSubTitle
                val missionId = mission.Id

                val missionItem = MissionItem(title, description, id = missionId)

                missionList.add(missionItem)

                Log.i("TITLE", title)

                adapter.setMyList(missionList)
                adapter.notifyDataSetChanged()
            }

            if (missionList.size == 0) {
                setNoMissionViewVisible()
                binding.fragmentMissionSrl.isRefreshing = false
            } else {
                setNoMissionViewInVisible()
            }

            //missionBadgeTextView.text = missionList.size.toString()
        }

        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}