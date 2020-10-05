package com.trapezoidlimited.groundforce.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.navigation.AppNavigator
import com.trapezoidlimited.groundforce.navigation.Screens
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Splash Screen activity
 */
@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {

    @Inject
    lateinit var navigator: AppNavigator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        /**
         * Navigates to the MainActivity
         */
        navigator.navigateTo(Screens.MainActivity)
    }
}