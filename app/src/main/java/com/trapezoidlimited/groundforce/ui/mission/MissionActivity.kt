package com.trapezoidlimited.groundforce.ui.mission

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import com.google.android.material.tabs.TabLayout
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.adapters.mission.MissionViewPagerAdapter
import com.trapezoidlimited.groundforce.databinding.ActivityMissionBinding
import kotlinx.android.synthetic.main.activity_mission.*

class MissionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMissionBinding
    private lateinit var adapter: MissionViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMissionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        /** Setting up tabs and handling indicator behavior*/
        setUpTabs()
        handleTabIndicator()


        /** Setting back arrow to return to the agent's dashboard */

        binding.missionBackArrow.setOnClickListener {
//            Intent(this, DashBoardActivity::class.java).also {
//                startActivity(it)
//            }
        }

    }

    /** This method sets up the tabs and fragments in the tablayout and viewpager */

    private fun setUpTabs() {


        adapter = MissionViewPagerAdapter(supportFragmentManager)

        val firstTabTitle = SpannableString("Missions")
        adapter.addFragment(MissionFragment(), firstTabTitle)


        val secondTabTitle = SpannableString("Ongoing")
        adapter.addFragment(OngoingFragment(), secondTabTitle)



        binding.missionOngoingVp.adapter = adapter
        binding.missionOngoingTl.setupWithViewPager(mission_ongoing_vp)

    }

    /** This method listens to the onSelect of the tabs and handles actions */

    private fun handleTabIndicator() {

        binding.missionOngoingTl.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                /** Here, the icon is faded from visibility */

                if (tab?.position == 1) {

                    binding.missionOngoingTl.getTabAt(1)?.text = "Ongoing"

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