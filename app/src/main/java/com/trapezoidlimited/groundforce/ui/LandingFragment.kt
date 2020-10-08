package com.trapezoidlimited.groundforce.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentLandingBinding
import com.trapezoidlimited.groundforce.utils.hideStatusBar
import com.trapezoidlimited.groundforce.utils.showStatusBar

/**
 * A simple [Fragment] subclass.
 * Use the [LandingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

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

    override fun onPause() {
        // Show status bar
        showStatusBar()
        super.onPause()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.landingCreateAccBtn.setOnClickListener {
            it.findNavController().navigate(R.id.phoneActivationFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback{
            requireActivity().finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}