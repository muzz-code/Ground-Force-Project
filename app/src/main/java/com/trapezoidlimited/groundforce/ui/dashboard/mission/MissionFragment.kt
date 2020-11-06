package com.trapezoidlimited.groundforce.ui.dashboard.mission


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.trapezoidlimited.groundforce.adapters.mission.MissionAdapter
import com.trapezoidlimited.groundforce.adapters.mission.OnMissionItemClickListener
import com.trapezoidlimited.groundforce.databinding.FragmentMissionBinding
import com.trapezoidlimited.groundforce.model.mission.MissionItem
import com.trapezoidlimited.groundforce.utils.DataListener
import com.trapezoidlimited.groundforce.utils.DummyData
import com.trapezoidlimited.groundforce.utils.setInVisibility
import com.trapezoidlimited.groundforce.utils.setVisibility


class MissionFragment : Fragment(), OnMissionItemClickListener {

    private var _binding: FragmentMissionBinding? = null
    private val binding get() = _binding!!
    private var locationTitlesList = DummyData.missionLocationData()
    private var adapter: MissionAdapter = MissionAdapter(
        mutableListOf(),
        this
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        _binding= FragmentMissionBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /**
         * Mission location data will be coming from the backend to populate the recyclerview list
         *
         * For now: here includes some dummy data*/


        adapter.setMyList(locationTitlesList)

        binding.fragmentMissionRv.adapter = adapter
        binding.fragmentMissionRv.layoutManager = LinearLayoutManager(this.context)

        if (locationTitlesList.size == 0) {
            setNoMissionViewVisible()
        } else {
            setNoMissionViewInVisible()
        }

    }



    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }


    /**
     * Method handles the actions onclick of the accept btn */

    override fun onAcceptClick(mission: MissionItem, position: Int) {

        /** setting the indicator  on the onGoing tab onclick of the accept btn */

        DataListener.mSetTabIndicator.value = true



        /** Onclick of the accept btn, the location data at that position will be removed from the Mission's list in the backend
         * And then, this location data is added to the ongoing list.
         * For now, this logic here just removes from the dummy list of the locations data */

        locationTitlesList.removeAt(position)

        adapter.notifyItemRemoved(position)

        if (locationTitlesList.size == 0) {
            setNoMissionViewVisible()
        }
    }

    /**
     * Method handles the actions onclick of the delete btn */

    override fun onDeclineClick(mission: MissionItem, position: Int) {

        /** Onclick of the decline btn, the location data is removed from the Mission's list
         * For now, this logic here just removes at that position from the dummy list of the locations data*/

        locationTitlesList.removeAt(position)

        adapter.notifyItemRemoved(position)

        if (locationTitlesList.size == 0) {
            setNoMissionViewVisible()
        }
    }

    private fun setNoMissionViewVisible(){
        setInVisibility(binding.fragmentMissionRv)
        setVisibility(binding.missionShoesIv)
        setVisibility(binding.missionNoMissionTv)
        setVisibility(binding.missionNoMissionTwoTv)
    }

    private fun setNoMissionViewInVisible(){
        setVisibility(binding.fragmentMissionRv)
        setInVisibility(binding.missionShoesIv)
        setInVisibility(binding.missionNoMissionTv)
        setInVisibility(binding.missionNoMissionTwoTv)
    }

}