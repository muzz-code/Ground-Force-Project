package com.trapezoidlimited.groundforce.ui.dashboard

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.ActivityDashboardBinding
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navigationView: NavigationView = findViewById(R.id.agentDashboard_navigation_view)
        navigationView.setNavigationItemSelectedListener(this)

        drawerLayout = binding.drawer

        //Bottom Navigation with Menu
        val bottomNavigator: BottomNavigationView = binding.dashboardActivityBnv
        bottomNavigator.setOnNavigationItemSelectedListener(navListener)
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
        drawer.closeDrawer(GravityCompat.START)
        Navigation.findNavController(this, R.id.dashboard_activity_nhf)
            .navigate(R.id.userProfileFragment)
    }

}