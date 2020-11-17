package com.trapezoidlimited.groundforce.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentCreateProfileTwoBinding


class CreateProfileFragmentTwo : Fragment() {

    private var _binding: FragmentCreateProfileTwoBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateProfileTwoBinding.inflate(inflater, container, false)

        /** set navigation arrow from drawable **/
        binding.fragmentCreateProfileTwoIc.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val zipCodes = listOf("110000", "102010", "100000", "103040")
        val adapterZipCode = ArrayAdapter(requireContext(), R.layout.list_item, zipCodes)
        (binding.fragmentCreateProfileTwoZipCodeTf.editText as? AutoCompleteTextView)?.setAdapter(adapterZipCode)

        val lga = listOf("Alimosho", "Ikorodu", "Oshodi-Isolo", "Lagos-Island")
        val adapterLGA = ArrayAdapter(requireContext(), R.layout.list_item, lga)
        (binding.fragmentCreateProfileTwoLgaTf.editText as? AutoCompleteTextView)?.setAdapter(adapterLGA)

        val states = listOf("Lagos", "Oyo", "Ogun", "Ondo")
        val adapterState = ArrayAdapter(requireContext(), R.layout.list_item, states)
        (binding.fragmentCreateProfileTwoStateTf.editText as? AutoCompleteTextView)?.setAdapter(adapterState)




        /** Navigate to bank detail screen **/
        binding.fragmentCreateProfileTwoBtn.setOnClickListener {

            findNavController().navigate(R.id.createProfileFragmentThree)
        }

        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentCreateProfileTwoIc.toolbarFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}