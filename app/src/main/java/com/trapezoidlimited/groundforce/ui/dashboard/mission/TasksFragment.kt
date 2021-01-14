package com.trapezoidlimited.groundforce.ui.dashboard.mission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.adapters.GenericViewPagerAdapter
import com.trapezoidlimited.groundforce.databinding.FragmentTasksBinding
import com.trapezoidlimited.groundforce.utils.DataListener
import com.trapezoidlimited.groundforce.utils.MISSION
import com.trapezoidlimited.groundforce.utils.ONGOING
import kotlinx.android.synthetic.main.custom_tab_heading.view.*


class TasksFragment : Fragment() {
    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: GenericViewPagerAdapter
    private lateinit var customTabLinearLayout: LinearLayout
    private lateinit var customTabTextView: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)

        /** setting toolbar text **/
        binding.fragmentTasksToolbar.toolbarTitle.text = getString(R.string.tasks_title_str)

        /** set navigation arrow from drawable **/
        binding.fragmentTasksToolbar.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)

        /** Setting up tabs and handling indicator behavior*/
        setUpTabs()

        /** setting the viewPager item to Ongoing and Missions depending on the currentItem value and the button clicked */

        if (DataListener.currentItem == ONGOING) {
            binding.fragmentTasksTabViewPagerVp.currentItem = ONGOING
        } else {
            binding.fragmentTasksTabViewPagerVp.currentItem = MISSION
        }


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /** Inflating the custom tab layout and get a reference to its textView  */
        customTabLinearLayout = LayoutInflater.from(this.context)
            .inflate(R.layout.custom_tab_heading, null) as LinearLayout
        customTabTextView = customTabLinearLayout.customTabTV

        handleTabIndicator()

        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentTasksToolbar.toolbarFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        /** setting the indicator  on the onGoing tab onclick of the accept btn */

        DataListener.setTabIndicator.observe(viewLifecycleOwner, Observer {

            if (it == true) {

                customTabTextView.text = "Ongoing    "
                customTabTextView.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.custom_tab_indicator_active,
                    0
                )

                binding.fragmentTasksTabLayoytTl.getTabAt(1)!!.customView = customTabTextView

                DataListener.mSetTabIndicator.value = false
            }

        })
    }


    /** This method sets up the tabs and fragments in the tablayout and viewpager */

    private fun setUpTabs() {

        val fragmentList = arrayListOf(
            MissionFragment(),
            OngoingFragment()
        )

        adapter = activity?.supportFragmentManager?.let {
            GenericViewPagerAdapter(
                fragmentList,
                it,
                lifecycle
            )
        }!!



        binding.fragmentTasksTabViewPagerVp.adapter = adapter


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

                    customTabTextView.text = "Ongoing"

                    customTabTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

                    binding.fragmentTasksTabLayoytTl.getTabAt(1)!!.customView = customTabTextView

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

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }


}