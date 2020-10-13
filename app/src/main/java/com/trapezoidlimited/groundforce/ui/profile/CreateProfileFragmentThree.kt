package com.trapezoidlimited.groundforce.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentCreateProfileThreeBinding


class CreateProfileFragmentThree : Fragment() {

    private var _binding : FragmentCreateProfileThreeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_create_profile_three, container, false)
        _binding = FragmentCreateProfileThreeBinding.inflate(inflater, container, false)

        binding.fragmentCreateProfileThreeIc.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)

        binding.fragmentCreateProfileThreeIc.toolbarFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}