package com.trapezoidlimited.groundforce.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentPhoneActivationBinding

/**
 * A simple [Fragment] subclass.
 * Use the [PhoneActivationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhoneActivationFragment : Fragment() {
    private var _binding: FragmentPhoneActivationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhoneActivationBinding.inflate(inflater, container, false)
        val view = binding.root



        // Inflate the layout for this fragment
        return view
    }
}