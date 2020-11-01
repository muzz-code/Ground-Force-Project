package com.trapezoidlimited.groundforce.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentAgentDashboardBinding

class AgentDashboardFragment : Fragment() {

    private var _binding: FragmentAgentDashboardBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAgentDashboardBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.fragmentAgentDashboardMissionsButtonIb.setOnClickListener {
            findNavController().navigate(R.id.tasksFragment)
        }

        binding.fragmentAgentDashboardSurveysButtonIb.setOnClickListener {
            findNavController().navigate(R.id.surveyListFragment2)
        }

        binding.fragmentAgentDashboardActiveButtonIb.setOnClickListener {
            findNavController().navigate(R.id.tasksFragment)
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}