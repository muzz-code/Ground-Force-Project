package com.trapezoidlimited.groundforce.ui.dashboard.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.adapters.history.HistoryMissionAdapter
import com.trapezoidlimited.groundforce.adapters.history.OnHistoryItemClickListener
import com.trapezoidlimited.groundforce.databinding.FragmentHistoryMissionBinding
import com.trapezoidlimited.groundforce.databinding.FragmentMissionBinding
import com.trapezoidlimited.groundforce.databinding.FragmentOngoingBinding
import com.trapezoidlimited.groundforce.model.mission.MissionItem
import com.trapezoidlimited.groundforce.utils.DummyData

class HistoryMissionFragment : Fragment(), OnHistoryItemClickListener {

    private var _binding: FragmentHistoryMissionBinding? = null
    private val binding get() = _binding!!
    private val historyMissionList = DummyData.historyMissionData()
    private var adapter = HistoryMissionAdapter(
        mutableListOf(),
        this
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentHistoryMissionBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter.setMyList(historyMissionList)

        binding.fragmentHistoryMissionRv.adapter = adapter
        binding.fragmentHistoryMissionRv.layoutManager = LinearLayoutManager(this.context)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onVerifyClick(missionItem: MissionItem, position: Int) {
        Toast.makeText(this.context, "Clicked", Toast.LENGTH_SHORT).show()
    }
}