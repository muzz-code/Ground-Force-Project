package com.trapezoidlimited.groundforce.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.viewmodel.LocationViewModel
import kotlinx.android.synthetic.main.fragment_location_verification.*

class LocationVerificationFragment : Fragment() {

    private lateinit var locationViewModel: LocationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)
        return inflater.inflate(R.layout.fragment_location_verification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        locationViewModel.requestLocationUpdates()
        locationViewModel.isLocationGotten.observe(viewLifecycleOwner, Observer {
            if(!it){
                progressBar.visibility=VISIBLE
            }
            else{
                progressBar.visibility=GONE
                displayLocationResult()
            }
        })
        super.onViewCreated(view, savedInstanceState)
    }

    private fun displayLocationResult(){
        locationViewModel._location.observe(viewLifecycleOwner, Observer {
            verifying_location_status_tv.text=getString(R.string.latLong, it.longitude, it.latitude)
        })

    }






}