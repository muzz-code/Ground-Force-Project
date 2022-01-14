package com.trapezoidlimited.groundforce.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.trapezoidlimited.groundforce.ui.main.MainActivity
import com.trapezoidlimited.groundforce.ui.main.SplashScreen
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

/**
 * Navigation implementation
 */
class AppNavigatorImpl @Inject constructor( @ActivityContext private var context: Context): AppNavigator {

    override fun navigateTo(activityToGo:Class<out AppCompatActivity>) {

        var intent = Intent(context, activityToGo )

        //starts the necessary activity
        context.startActivity(intent)
    }


}