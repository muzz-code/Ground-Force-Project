package com.trapezoidlimited.groundforce.ui.dashboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.EntryApplication
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.data.AgentObject
import com.trapezoidlimited.groundforce.databinding.FragmentAgentDashboardBinding
import com.trapezoidlimited.groundforce.room.RoomAgent
import com.trapezoidlimited.groundforce.utils.*

class AgentDashboardFragment : Fragment() {

    private var _binding: FragmentAgentDashboardBinding? = null
    private val binding get() = _binding!!
    private val roomViewModel by lazy { EntryApplication.viewModel(this) }
    private lateinit var dashBoardCard: CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (_binding == null) {
            _binding = FragmentAgentDashboardBinding.inflate(inflater, container, false)
            /** setting toolbar text **/
            binding.dashboardToolBarLy.toolbarTitle.text = getString(R.string.home_title_str)
        }

        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dashBoardCard = binding.agentDashboardFragmentSummaryContainerCv

        binding.fragmentAgentDashboardMissionsButtonIb.setOnClickListener {
            DataListener.currentItem = MISSION
            findNavController().navigate(R.id.action_agentDashboardFragment_to_tasksFragment)
        }

        binding.fragmentAgentDashboardSurveysButtonIb.setOnClickListener {
            findNavController().navigate(R.id.action_agentDashboardFragment_to_surveyListFragment2)
        }

        /** setting mutableLiveData value controlling where the tab will start on the mission fragment  */

        binding.fragmentAgentDashboardActiveButtonIb.setOnClickListener {
            DataListener.currentItem = ONGOING
            findNavController().navigate(R.id.action_agentDashboardFragment_to_tasksFragment)
        }

        binding.fragmentAgentDashboardSeeDetailsButton.setOnClickListener {
            findNavController().navigate(R.id.action_agentDashboardFragment_to_paymentHistory)
        }

        binding.fragmentAgentViewDetailsTv.setOnClickListener {
            // Setting currentItem value for mission-survey-history viewpager
            DataListener.msCurrentItem = MISSIONCOMPLETED
            findNavController().navigate(R.id.action_agentDashboardFragment_to_historyFragment)
        }

        binding.fragmentAgentSurveyViewDetailsTv.setOnClickListener {
            // Setting currentItem value for mission-survey-history viewpager
            DataListener.msCurrentItem = SURVEYCOMPLETED
            findNavController().navigate(R.id.action_agentDashboardFragment_to_historyFragment)
        }


        binding.fragmentAgentDashboardCloseIconIv.setOnClickListener {
            binding.fragmentAgentDashboardIncompleteProfileCl.visibility = View.GONE
        }

        // Navigate to Home on Back Press
        requireActivity().onBackPressedDispatcher.addCallback {
            if (findNavController().currentDestination?.id == R.id.agentDashboardFragment) {
                activity?.finish()
            } else {
                findNavController().popBackStack()
            }
        }

        val roomAgent = RoomAgent(
            1,
            "Oladokun",
            "Oladapo",
            "08090930021",
            "m",
            "11/05/1993",
            "ola@gmail.com",
            "1234",
            "Ibadan",
            "Oyo",
            "Ibadan",
            "200201",
            "1993 N",
            "902903"

        )

//        roomViewModel.addAgent(roomAgent)

        roomViewModel.agentObject.observe(requireActivity(), {
            if (it.isNotEmpty()) {
                Toast.makeText(requireActivity(), it[it.lastIndex].firstName, Toast.LENGTH_SHORT)
                    .show()
                Log.i("Agent From Room", it[it.lastIndex].firstName)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}