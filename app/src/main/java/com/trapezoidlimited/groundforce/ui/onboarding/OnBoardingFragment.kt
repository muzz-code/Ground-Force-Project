package com.trapezoidlimited.groundforce.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.adapters.GenericViewPagerAdapter
import com.trapezoidlimited.groundforce.databinding.FragmentOnBoardingBinding
import com.trapezoidlimited.groundforce.utils.ONBOARD
import com.trapezoidlimited.groundforce.utils.hideStatusBar
import com.trapezoidlimited.groundforce.utils.loadFromSharedPreference
import com.trapezoidlimited.groundforce.utils.saveToSharedPreference

class OnBoardingFragment : Fragment() {

    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)


        // Hide Status Bar
        hideStatusBar()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Set View PagerAdapter
        val fragmentList: ArrayList<Fragment> = arrayListOf(
            OnBoardingScreen2Fragment(),
            OnBoardingScreen3Fragment(),
            OnBoardingScreen4Fragment()
        )

        //Connect the fragment list to the view pager
        val adapter = activity?.supportFragmentManager?.let {
            GenericViewPagerAdapter(
                fragmentList,
                it,
                lifecycle
            )
        }

        binding.fragmentOnBoardingVp.adapter = adapter

        //Ue TabLayoutMediator to set indicator to the tab layout
        TabLayoutMediator(
            binding.fragmentOnBoardingIndicatorTl,
            binding.fragmentOnBoardingVp
        ) { tab, position ->
        }.attach()


        /*
        Set Onclick listener to get started button and navigate to Sign up pager
        Also, save to shared preference so users don't need to go through again.
         */
        binding.fragmentOnBoardingGetStartedBtn.setOnClickListener {
            saveToSharedPreference(requireActivity(), ONBOARD, "true")
            findNavController().navigate(R.id.landingFragment)
        }

    }


    //If User has already gone through OnBoarding once, Move straight to Landing Fragment
    override fun onStart() {
        super.onStart()
        if (loadFromSharedPreference(requireActivity(), ONBOARD) == "true") {
            findNavController().navigate(R.id.landingFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}