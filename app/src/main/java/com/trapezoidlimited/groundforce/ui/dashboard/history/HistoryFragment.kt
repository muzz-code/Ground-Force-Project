package com.trapezoidlimited.groundforce.ui.dashboard.history

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.adapters.GenericViewPagerAdapter
import com.trapezoidlimited.groundforce.databinding.FragmentHistoryBinding
import com.trapezoidlimited.groundforce.utils.*
import com.trapezoidlimited.groundforce.utils.MISSIONCOMPLETED
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

        /** setting toolbar text **/
        binding.fragmentHistoryToolbar.toolbarTitle.text = getString(R.string.history_title_str)

        /** set navigation arrow from drawable **/
        binding.fragmentHistoryToolbar.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)

        setUpTabs()

        /** setting the viewPager item to Missions History and Surveys History depending on the button clicked  */

        if(DataListener.msCurrentItem == SURVEYCOMPLETED){

            binding.fragmentHistoryTabViewPagerVp.currentItem = SURVEYCOMPLETED
        }
        else{

            binding.fragmentHistoryTabViewPagerVp.currentItem = MISSIONCOMPLETED
        }

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentHistoryToolbar.toolbarFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


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