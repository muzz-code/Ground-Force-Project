package com.trapezoidlimited.groundforce.ui.dashboard

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.trapezoidlimited.groundforce.EntryApplication
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.ActivityDashboardBinding
import com.trapezoidlimited.groundforce.ui.main.MainActivity
import com.trapezoidlimited.groundforce.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var requestManager: RequestManager

    lateinit var profileImage: ImageView

    private lateinit var profileNameTextView: TextView
    private val genericRepository by lazy { EntryApplication.groundForceRepository }

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var drawerLayout: DrawerLayout
    private var googleAccount: GoogleSignInAccount? = null

    private val roomViewModel by lazy { EntryApplication.viewModel(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.extras != null) {
            googleAccount = intent.extras?.getParcelable("googleAccount")
        }


        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        //Inflate the Nav Header
        val navHeaderView = LayoutInflater.from(this)
            .inflate(R.layout.nav_header_main, agentDashboard_navigation_view, true)


        profileImage = navHeaderView.nav_header_agent_icon

        /** Initializing user's profile name **/

        profileNameTextView = navHeaderView.agentDashboardframent_user_name_tv

        //Load Image from file if already saved
//        if (agentImageIsSaved(this)) {
//            genericRepository.getImageFromStorage(this, profileImage)
//        }

        /** Setting user's profile image from network */
        loadImageFromNetwork()

        //Initialize Drawer Menu Listener
        val navigationView: NavigationView = findViewById(R.id.agentDashboard_navigation_view)
        navigationView.setNavigationItemSelectedListener(this)

        drawerLayout = binding.drawer

        //Bottom Navigation with Menu
        val bottomNavigator: BottomNavigationView = binding.dashboardActivityBnv
        bottomNavigator.setOnNavigationItemSelectedListener(navListener)


        //Load Profile Image to Nav Drawer Header
        if (googleAccount != null) {
            requestManager
                .load(googleAccount?.photoUrl)
                .into(profileImage)
        }

        /** Setting user's profile name from room database */

        roomViewModel.agentObject.observe(this, {
            if (it.isNotEmpty()) {

                val firstName = it[it.lastIndex].firstName
                val lastName = it[it.lastIndex].lastName
                val name = "$firstName $lastName"

                profileNameTextView.text = name
            }
        })

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.dashboard_activity_nhf) as NavHostFragment
        val navController = navHostFragment.navController


        navController.addOnDestinationChangedListener { controller, destination, arguments ->

//            if (destination.id == R.id.verifyLocationFragment || destination.id == R.id.userProfileFragment) {
//                /** hiding bottom navigation bar **/
//                binding.dashboardActivityBnv.visibility = View.GONE
//
//            } else if(destination.id == R.id.uploadImageFragment){
//                binding.dashboardActivityBnv.visibility = View.GONE
//            }else {
//                binding.dashboardActivityBnv.visibility = View.VISIBLE
//            }

            when (destination.id) {
                R.id.verifyLocationFragment -> {
                    binding.dashboardActivityBnv.visibility = View.GONE
                }
                R.id.userProfileFragment -> {
                    binding.dashboardActivityBnv.visibility = View.GONE
                }
                R.id.uploadImageFragment -> {
                    binding.dashboardActivityBnv.visibility = View.GONE
                }
                else -> {
                    binding.dashboardActivityBnv.visibility = View.VISIBLE
                }
            }

        }


    }


    /**navigating between different fragment in the bottom navigation**/

    private var navListener: BottomNavigationView.OnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener {
            var fragment = 0
            when (it.itemId) {
                R.id.agentDashboard_more_bn -> {

                    drawerLayout.openDrawer(binding.agentDashboardNavigationView)

                    loadImageFromNetwork()

                }
                R.id.agentDashboard_notification -> {
                    fragment = R.id.notificationsFragment
                }
                R.id.agentDashboard_home -> {
                    fragment = R.id.agentDashboardFragment
                }
                else -> {
                    fragment = R.id.agentDashboardFragment
                }
            }

            if (fragment != 0) {
                Navigation.findNavController(this, R.id.dashboard_activity_nhf).navigate(fragment)
            }
            true
        }


    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_home -> {
                findNavController(R.id.dashboard_activity_nhf).navigate(R.id.agentDashboardFragment)
                selectActiveIcon()
            }
            R.id.nav_payment -> {
                findNavController(R.id.dashboard_activity_nhf).navigate(R.id.paymentHistory)
            }
            R.id.nav_security -> {
                findNavController(R.id.dashboard_activity_nhf).navigate(R.id.securityFragment)
            }
            R.id.nav_help -> {

                findNavController(R.id.dashboard_activity_nhf).navigate(R.id.helpFragment)

//                if (findNavController(R.id.dashboard_activity_nhf).currentDestination?.id == R.id.securityFragment){
//
//                } else {
//                    findNavController(R.id.dashboard_activity_nhf).navigate(R.id.action_agentDashboardFragment_to_helpFragment)
//                }

            }


            R.id.nav_logout -> {

                val dialogInterface = DialogInterface.OnClickListener { dialog, _ ->

                    SessionManager.save(this, TOKEN, "")
                    SessionManager.save(this, AVATAR_URL, "")
                    Intent(this, MainActivity::class.java).also {
                        saveToSharedPreference(this, LOG_OUT, "true")
                        roomViewModel.deleteAllMission()
                        roomViewModel.deleteAllOngoingMission()
                        roomViewModel.deleteAllAgentDetails()
                        roomViewModel.deleteAllHistorySurvey()
                        roomViewModel.deleteAllHistoryMission()
                        startActivity(it)
                        finish()
                    }

                    dialog.cancel()
                }
                showAlertDialog("Are you sure you want to log out?", "Log Out", dialogInterface)

            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }


    //If Drawer is open, close it on back button pressed
    //
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
            selectActiveIcon()
        } else {
            super.onBackPressed()
        }
    }

    /**
     * Change the Selected Menu Icon back to the current fragment
     */
    private fun selectActiveIcon() {
        val currentDestination =
            findNavController(R.id.dashboard_activity_nhf).currentDestination?.id
        if (currentDestination == R.id.agentDashboardFragment) {
            dashboardActivity_bnv.checkItem(R.id.agentDashboard_home)
        } else if (currentDestination == R.id.notificationsFragment) {
            dashboardActivity_bnv.checkItem(R.id.agentDashboard_notification)
        }
    }

    //OnClick Listener on View Profile
    fun openProfile(view: View) {
        drawer.closeDrawer(GravityCompat.START)
        Navigation.findNavController(this, R.id.dashboard_activity_nhf)
            .navigate(R.id.userProfileFragment)
    }

    private fun loadImageFromNetwork() {
        val photoUrl = loadFromSharedPreference(this, AVATAR_URL)

        Glide.with(this)
            .load(photoUrl)
            .placeholder(R.drawable.agent_icon)
            .into(profileImage)
    }

}