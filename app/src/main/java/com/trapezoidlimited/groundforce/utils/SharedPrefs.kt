package com.trapezoidlimited.groundforce.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

//Load details From Shared Preferences
fun loadFromSharedPreference(activity: Activity, prefType: String): String {
    val sharedPreferences: SharedPreferences =
        activity.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
    return sharedPreferences.getString(prefType, "").toString()
}

//Save details to Shared Preferences
fun saveToSharedPreference(activity: Activity, prefType: String, prefValue: String) {
    val sharedPreferences: SharedPreferences =
        activity.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = sharedPreferences.edit()
    editor.putString(prefType, prefValue)
    editor.apply()
}



