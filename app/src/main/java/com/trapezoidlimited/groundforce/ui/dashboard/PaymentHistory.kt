package com.trapezoidlimited.groundforce.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.adapters.GenericViewPagerAdapter
import com.trapezoidlimited.groundforce.databinding.FragmentOnBoardingBinding
import com.trapezoidlimited.groundforce.databinding.FragmentPaymentHistoryBinding
import com.trapezoidlimited.groundforce.ui.dashboard.mission.MissionFragment
import com.trapezoidlimited.groundforce.ui.dashboard.mission.OngoingFragment

class PaymentHistory : Fragment() {

    private var _binding: FragmentPaymentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var paymentHistoryTabLayout: TabLayout
    private lateinit var paymentHistoryViewPager: ViewPager2


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPaymentHistoryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        paymentHistoryTabLayout = binding.fragmentPaymentHistoryTabLayout
        paymentHistoryViewPager = binding.fragmentPaymentHistoryViewPager


        val fragmentList = arrayListOf(
            PaymentHistoryMissions(),
            PaymentHistorySurvey()
        )

        val adapter = activity?.supportFragmentManager?.let {
            GenericViewPagerAdapter(
                fragmentList,
                it,
                lifecycle
            )
        }!!

        paymentHistoryViewPager.adapter = adapter

        TabLayoutMediator(
            paymentHistoryTabLayout,
            paymentHistoryViewPager
        ) { tab, position ->
            tab.text = when (position) {
                0 -> "Missions"
                else -> "Surveys"
            }
        }.attach()

        binding.fragmentPaymentHistoryBackArrowIv.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}