package com.trapezoidlimited.groundforce.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object DataListener {

    var mSetTabIndicator = MutableLiveData<Boolean>()
    val setTabIndicator : LiveData<Boolean>
        get() = mSetTabIndicator

    var mSetStartTab = MutableLiveData<Boolean>()
    val setStartTab : LiveData<Boolean>
        get() = mSetStartTab

    init {
        mSetTabIndicator.value = false
        mSetStartTab.value = false
    }
}