package com.trapezoidlimited.groundforce.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentCreateProfileOneBinding


class CreateProfileFragmentOne : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding : FragmentCreateProfileOneBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_create_profile_one, container, false)
        _binding = FragmentCreateProfileOneBinding.inflate(inflater, container, false)

       ArrayAdapter.createFromResource(
           requireContext(),
           R.array.sex,
           android.R.layout.simple_spinner_item
       ).also {adapter ->
           // Specify the layout to use when the list of choices appears
           adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
           // Apply the adapter to the spinner
           binding.fragmentCreateProfileOneGenderSp.adapter = adapter

           binding.fragmentCreateProfileOneReligionSp.adapter = adapter
       }

        binding.fragmentCreateProfileOneGenderSp.onItemSelectedListener = this

        binding.fragmentCreateProfileOneReligionSp.onItemSelectedListener = this

        binding.fragmentCreateProfileOneBtn.setOnClickListener {
            findNavController().navigate(R.id.createProfileFragmentTwo)
        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // For spinner
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        parent?.getItemAtPosition(position)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


}