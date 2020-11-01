package com.trapezoidlimited.groundforce.ui.dashboard.mission

import android.os.Bundle
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.trapezoidlimited.groundforce.adapters.GenericViewPagerAdapter
import com.trapezoidlimited.groundforce.databinding.FragmentTasksBinding

class TasksFragment : Fragment() {
    private lateinit var binding: FragmentTasksBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTasksBinding.inflate(inflater, container, false)

        /** Setting up tabs and handling indicator behavior*/
        setUpTabs()
        handleTabIndicator()

        /** Setting back arrow to return to the agent's dashboard */
        binding.missionBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }


    /** This method sets up the tabs and fragments in the tablayout and viewpager */

    private fun setUpTabs() {

        val fragmentList = arrayListOf(
            MissionFragment(),
            OngoingFragment()
        )

        val adapter = activity?.supportFragmentManager?.let {
            GenericViewPagerAdapter(
                fragmentList,
                it,
                lifecycle
            )
        }!!

//
//        adapter = MissionViewPagerAdapter(adapter)
//
//        val firstTabTitle = SpannableString("Missions")
//        adapter.addFragment(MissionFragment(), firstTabTitle)
//
//
//        val secondTabTitle = SpannableString("Ongoing")
//        adapter.addFragment(OngoingFragment(), secondTabTitle)


        binding.fragmentTasksTabViewPagerVp.adapter = adapter

//        binding.fragmentTasksTabLayoytTl.setupWithViewPager(fragment_tasks_tab_view_pager_vp)
        //Ue TabLayoutMediator to set indicator to the tab layout

        TabLayoutMediator(
            binding.fragmentTasksTabLayoytTl,
            binding.fragmentTasksTabViewPagerVp
        ) { tab, position ->
            tab.text = when (position) {
                0 -> "Missions"
                else -> "Ongoing"
            }
        }.attach()


    }

    /** This method listens to the onSelect of the tabs and handles actions */

    private fun handleTabIndicator() {

        binding.fragmentTasksTabLayoytTl.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                /** Here, the icon is faded from visibility */

                if (tab?.position == 1) {

//                    binding.missionOngoingTl.getTabAt(1)?.text = "Ongoing"

                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                /// do nothing
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                /// do nothing
            }

        })
    }

}