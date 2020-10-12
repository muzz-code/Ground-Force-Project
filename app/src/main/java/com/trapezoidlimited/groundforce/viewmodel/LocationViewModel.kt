package com.trapezoidlimited.groundforce.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocationViewModel: ViewModel() {


    lateinit var location:MutableLiveData<Location>
    lateinit var isLocationGotten:MutableLiveData<Boolean>
    val _location:LiveData<Location> get() = location

    init {
        isLocationGotten.value=false
    }







}