package com.trapezoidlimited.groundforce.ui.dashboard.mission

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.adapters.GenericViewPagerAdapter
import com.trapezoidlimited.groundforce.databinding.FragmentTasksBinding
import com.trapezoidlimited.groundforce.utils.DataListener
import com.trapezoidlimited.groundforce.utils.MISSION
import com.trapezoidlimited.groundforce.utils.ONGOING


class TasksFragment : Fragment() {
    private lateinit var binding: FragmentTasksBinding
    private lateinit var adapter: GenericViewPagerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTasksBinding.inflate(inflater, container, false)

        /** setting toolbar text **/
        binding.fragmentTasksToolbar.toolbarTitle.text = getString(R.string.tasks_title_str)

        /** set navigation arrow from drawable **/
        binding.fragmentTasksToolbar.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)

        /** Setting up tabs and handling indicator behavior*/
        setUpTabs()
        handleTabIndicator()


        /** setting the viewPager item to Ongoing and Missions depending on the currentItem value and the button clicked */

        if(DataListener.currentItem == ONGOING){
            binding.fragmentTasksTabViewPagerVp.currentItem = ONGOING
        }
        else{
            binding.fragmentTasksTabViewPagerVp.currentItem = MISSION
        }


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentTasksToolbar.toolbarFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        /** setting the indicator  on the onGoing tab onclick of the accept btn */

        DataListener.setTabIndicator.observe(viewLifecycleOwner, Observer {

            if (it == true) {

                val image: Drawable? = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.custom_tab_indicator_active,
                    null
                )

                image?.setBounds(0, 0, image.intrinsicWidth, image.intrinsicHeight)

                val sb = SpannableString("Ongoing" + "     ")

                val imageSpan = ImageSpan(image!!, ImageSpan.ALIGN_CENTER)


                sb.setSpan(
                    imageSpan,
                    sb.length - 2,
                    sb.length - 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                binding.fragmentTasksTabLayoytTl.getTabAt(1)?.text = SpannableString(sb)

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

                    binding.fragmentTasksTabLayoytTl.getTabAt(1)?.text = "Ongoing"

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