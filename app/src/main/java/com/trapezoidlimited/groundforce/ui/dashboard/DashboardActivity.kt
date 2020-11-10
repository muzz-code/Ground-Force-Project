package com.trapezoidlimited.groundforce.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import com.bumptech.glide.RequestManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.ActivityDashboardBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import javax.inject.Inject


@AndroidEntryPoint
class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var requestManager: RequestManager

    lateinit var profileImage: ImageView

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var drawerLayout: DrawerLayout
    private var googleAccount: GoogleSignInAccount? = null


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

    }

    /**navigating between different fragment in the bottom navigation**/

    private var navListener: BottomNavigationView.OnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener {
            var fragment = 0
            when (it.itemId) {
                R.id.agentDashboard_more_bn -> {
                    drawerLayout.openDrawer(binding.agentDashboardNavigationView)
                }
                R.id.agentDashboard_notification -> {
                    Toast.makeText(this, "Notification", Toast.LENGTH_SHORT).show()
                    fragment = R.id.notificationsFragment
                }
                R.id.agentDashboard_home -> {
                    fragment = R.id.agentDashboardFragment
                    Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_payment -> {
                Toast.makeText(this, "Make Payment", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_security -> {
                Toast.makeText(this, "Protect Your App", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_help -> {
                Toast.makeText(this, "Get Help", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_contact -> {
                Toast.makeText(this, "Contact us", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_logout -> {
                Toast.makeText(this, "Log Out", Toast.LENGTH_SHORT).show()
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }


    //If Drawer is open, close it on back button pressed
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }



    //OnClick Listener on View Profile
    fun openProfile(view: View) {
//        var action = Da
        drawer.closeDrawer(GravityCompat.START)
        Navigation.findNavController(this, R.id.dashboard_activity_nhf)
            .navigate(R.id.userProfileFragment)
    }

}