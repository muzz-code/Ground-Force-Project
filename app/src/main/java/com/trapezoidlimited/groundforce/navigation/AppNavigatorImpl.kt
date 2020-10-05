package com.trapezoidlimited.groundforce.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.trapezoidlimited.groundforce.ui.main.MainActivity
import com.trapezoidlimited.groundforce.ui.main.SplashScreen
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

/**
 * Navigation implementation
 */
class AppNavigatorImpl @Inject constructor( @ActivityContext private var context: Context): AppNavigator {


    override fun navigateTo(screen: Screens) {

        //picks the activity based on the screen enum that is passed
        val activityToGo = when(screen){
            Screens.MainActivity -> MainActivity::class.java
            Screens.SplashScreen -> SplashScreen::class.java
        }


        var intent = Intent(context, activityToGo )

        //starts the necessary activity
        context.startActivity(intent)
    }


}