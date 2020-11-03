package com.trapezoidlimited.groundforce.ui.dashboard.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.trapezoidlimited.groundforce.adapters.history.HistorySurveyAdapter
import com.trapezoidlimited.groundforce.adapters.history.OnHistorySurveyItemClickListener
import com.trapezoidlimited.groundforce.databinding.FragmentHistorySurveyBinding
import com.trapezoidlimited.groundforce.model.mission.SurveyItem
import com.trapezoidlimited.groundforce.utils.DummyData


class HistorySurveyFragment : Fragment(), OnHistorySurveyItemClickListener {

    private var _binding: FragmentHistorySurveyBinding? = null
    private val binding get() = _binding!!

    private val historySurveyList = DummyData.historySurveyData()
    private var adapter = HistorySurveyAdapter(
        mutableListOf(),
        this
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentHistorySurveyBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter.setMyList(historySurveyList)

        binding.fragmentHistoryMissionRv.adapter = adapter
        binding.fragmentHistoryMissionRv.layoutManager = LinearLayoutManager(this.context)
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    override fun onCompletedClick(surveyItem: SurveyItem, position: Int) {
        TODO("Not yet implemented")
    }
}