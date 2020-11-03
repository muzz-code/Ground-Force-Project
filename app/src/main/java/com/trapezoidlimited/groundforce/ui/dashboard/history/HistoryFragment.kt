package com.trapezoidlimited.groundforce.ui.dashboard.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.trapezoidlimited.groundforce.adapters.GenericViewPagerAdapter
import com.trapezoidlimited.groundforce.databinding.FragmentHistoryBinding
import com.trapezoidlimited.groundforce.ui.dashboard.mission.MissionFragment
import com.trapezoidlimited.groundforce.ui.dashboard.mission.OngoingFragment


class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var adapter: GenericViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.historyBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        setUpTabs()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    /** This method sets up the tabs and fragments in the tablayout and viewpager */

    private fun setUpTabs() {

        val fragmentList = arrayListOf(
           HistoryMissionFragment(),
            HistorySurveyFragment()
        )

        adapter = activity?.supportFragmentManager?.let {
            GenericViewPagerAdapter(
                fragmentList,
                it,
                lifecycle
            )
        }!!


        binding.fragmentHistoryTabViewPagerVp.adapter = adapter



        TabLayoutMediator(
            binding.fragmentHistoryTabLayoutTl,
            binding.fragmentHistoryTabViewPagerVp
        ) { tab, position ->
            tab.text = when (position) {
                0 -> "Missions"
                else -> "Surveys"
            }
        }.attach()

    }




}