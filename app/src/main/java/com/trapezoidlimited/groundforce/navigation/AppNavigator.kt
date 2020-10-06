package com.trapezoidlimited.groundforce.navigation

import androidx.appcompat.app.AppCompatActivity

/**
 * This handles the task of navigating through the app
 * */



/**
 * Interfaces that deals with navigation
 **/

  interface AppNavigator {
    //Navigate to specified screen

    fun navigateTo (activityToGo:Class<out AppCompatActivity>)
}
