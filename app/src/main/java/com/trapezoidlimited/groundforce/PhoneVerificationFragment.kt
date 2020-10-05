package com.trapezoidlimited.groundforce

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trapezoidlimited.groundforce.databinding.FragmentPhoneVerificationBinding

/**
 * A simple [Fragment] subclass.
 * Use the [PhoneVerificationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhoneVerificationFragment : Fragment() {
    private var _binding: FragmentPhoneVerificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhoneVerificationBinding.inflate(inflater, container, false)
        val view = binding.root
        // Inflate the layout for this fragment
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}