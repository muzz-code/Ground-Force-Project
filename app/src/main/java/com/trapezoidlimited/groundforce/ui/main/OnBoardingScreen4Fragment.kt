package com.trapezoidlimited.groundforce.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trapezoidlimited.groundforce.R

/**
 * The Fragment for OnBoarding Screen No. 4
 * where there is the image of female holding a phone
 */
class OnBoardingScreen4Fragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for Onboarding Screen 4.
        return inflater.inflate(R.layout.fragment_on_boarding_screen_4, container, false)
    }
}