package com.trapezoidlimited.groundforce.utils

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
const val MISSION = 0
const val ONGOING = 1

const val MISSIONCOMPLETED = 0
const val SURVEYCOMPLETED = 1


object DataListener {

    var currentItem = MISSION
    var msCurrentItem = MISSIONCOMPLETED

    var mSetTabIndicator = MutableLiveData<Boolean>()
    val setTabIndicator : LiveData<Boolean>
        get() = mSetTabIndicator


    init {
        mSetTabIndicator.value = false
    }

}