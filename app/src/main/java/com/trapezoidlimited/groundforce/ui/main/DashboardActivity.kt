package com.trapezoidlimited.groundforce.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.ActivityDashboardBinding
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_dashboard)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navigationView: NavigationView = findViewById(R.id.agentDashboard_navigation_view)
        navigationView.setNavigationItemSelectedListener(this)

        drawerLayout = binding.drawer

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.agentDashboardNavigationView, navController)

    /**navigating between different fragment in the bottom navigation**/
        binding.dashboardActivityBnv.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.agentDashboard_more_bn -> {
                    drawerLayout.openDrawer(binding.agentDashboardNavigationView)
                    true
                }
                R.id.agentDashboard_notification ->{
                    Toast.makeText(this, "Notification", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.agentDashboard_home ->{
                    Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> {
                    false
                }
            }
        }

        //        Navigate to Driver home on Back Press
        onBackPressedDispatcher.addCallback {
           finish()
        }
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


}