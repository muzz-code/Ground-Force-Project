package com.trapezoidlimited.groundforce.ui

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentLandingBinding
import com.trapezoidlimited.groundforce.utils.hideStatusBar
import com.trapezoidlimited.groundforce.utils.showStatusBar



class LandingFragment : Fragment() {

    private var _binding: FragmentLandingBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentLandingBinding.inflate(inflater, container, false)
        val view = binding.root
        // Hide status bar
        hideStatusBar()

        // Inflate the layout for this fragment
        return view
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.landingCreateAccBtn.setOnClickListener {
            it.findNavController().navigate(R.id.phoneActivationFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback{
            requireActivity().finish()
        }

        /**move to login fragment**/
        binding.landingSignUpBtn.setOnClickListener {
            val extra = FragmentNavigatorExtras(
                binding.landingWelcomeTv to binding.landingWelcomeTv.transitionName,
                binding.landingSubTitleTv to binding.landingSubTitleTv.transitionName,
                binding.landingCreateAccBtn to binding.landingCreateAccBtn.transitionName,
                binding.landingSignUpGoogleBtn to binding.landingSignUpGoogleBtn.transitionName
            )
            findNavController().navigate(R.id.loginFragment,null, null,  extra)
        }

        binding.landingSignUpGoogleBtn.setOnClickListener {
            findNavController().navigate(R.id.beginSurveyFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}