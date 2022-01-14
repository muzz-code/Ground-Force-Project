package com.trapezoidlimited.groundforce.ui.dashboard.extras

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.adapters.GenericViewPagerAdapter
import com.trapezoidlimited.groundforce.databinding.FragmentPaymentHistoryBinding
import com.trapezoidlimited.groundforce.ui.dashboard.PaymentHistoryMissions
import com.trapezoidlimited.groundforce.ui.dashboard.PaymentHistorySurvey

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

        /** setting toolbar text **/
        binding.fragmentPrimaryHistoryToolbar.toolbarTitle.text = getString(R.string.payment_title_str)

        /** set navigation arrow from drawable **/
        binding.fragmentPrimaryHistoryToolbar.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentPrimaryHistoryToolbar.toolbarFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.agentDashboardFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback{
            if (findNavController().currentDestination?.id == R.id.paymentHistory) {
                findNavController().navigate(R.id.agentDashboardFragment)
            } else {
                findNavController().popBackStack()
            }
        }

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


    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}