package com.trapezoidlimited.groundforce.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trapezoidlimited.groundforce.databinding.FragmentWaitingBinding
import com.trapezoidlimited.groundforce.utils.*

class WaitingFragment : Fragment() {

    private var _binding: FragmentWaitingBinding? = null
    val binding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWaitingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        showWelcomeDialog()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}