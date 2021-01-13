package com.trapezoidlimited.groundforce.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

const val MISSION = 0
const val ONGOING = 1

const val MISSIONCOMPLETED = 0
const val SURVEYCOMPLETED = 1

const val LOCATION_VERIFICATION_SCREEN = 0
const val VERIFY_LOCATION_SCREEN = 1
const val CREATE_NEW_PASSWORD_SCREEN = 2


object DataListener {

    var currentItem = MISSION
    var msCurrentItem = MISSIONCOMPLETED

    var token = "token"

    var currentScreen = LOCATION_VERIFICATION_SCREEN

    var mSetTabIndicator = MutableLiveData<Boolean>()
    val setTabIndicator: LiveData<Boolean>
        get() = mSetTabIndicator


    init {
        mSetTabIndicator.value = false
    }

}