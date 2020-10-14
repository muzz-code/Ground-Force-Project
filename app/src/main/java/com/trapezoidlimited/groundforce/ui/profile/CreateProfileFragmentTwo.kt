package com.trapezoidlimited.groundforce.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
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
        _binding = FragmentCreateProfileTwoBinding.inflate(inflater, container, false)

        /** set navigation arrow from drawable **/
        binding.fragmentCreateProfileTwoIc.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)

        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentCreateProfileTwoIc.toolbarFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        /** Navigate to bank detail screen **/
        binding.fragmentCreateProfileTwoBtn.setOnClickListener {
            findNavController().navigate(R.id.createProfileFragmentThree)
        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}