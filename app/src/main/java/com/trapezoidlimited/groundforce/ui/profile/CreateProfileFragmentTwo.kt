package com.trapezoidlimited.groundforce.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentCreateProfileTwoBinding

class CreateProfileFragmentTwo : Fragment() {

    private var _binding : FragmentCreateProfileTwoBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_create_profile_two, container, false)
        _binding = FragmentCreateProfileTwoBinding.inflate(inflater, container, false)

        binding.fragmentCreateProfileTwoIc.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)

        binding.fragmentCreateProfileTwoIc.toolbarFragment.setNavigationOnClickListener {

        }

        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}