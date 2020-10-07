package com.trapezoidlimited.groundforce.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trapezoidlimited.groundforce.databinding.FragmentLandingBinding
import com.trapezoidlimited.groundforce.utils.hideStatusBar
import com.trapezoidlimited.groundforce.utils.showStatusBar

/**
 * A simple [Fragment] subclass.
 * Use the [LandingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LandingFragment : Fragment() {

    private var _bindind: FragmentLandingBinding? = null

    private val binding get() = _bindind!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindind = FragmentLandingBinding.inflate(inflater, container, false)
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


    override fun onDestroy() {
        super.onDestroy()
        _bindind = null
    }
}