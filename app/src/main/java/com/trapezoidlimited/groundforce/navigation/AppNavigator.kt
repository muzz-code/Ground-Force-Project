package com.trapezoidlimited.groundforce.navigation

/**
 * This handles the task of navigating through the app
 * */

/**
 * Each Activity/Fragment is represented in this enum class .. Available Screens
 */
   enum class Screens {

    MainActivity,
    SplashScreen
   }


/**
 * Interfaces that deals with navigation
 **/

  interface AppNavigator {
    //Navigate to specified screen

    fun navigateTo (screen:Screens)
}
