package com.trapezoidlimited.groundforce.ui.dashboard.extras

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentHelpBinding

class HelpFragment : Fragment() {

    private var _binding: FragmentHelpBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentHelpBinding.inflate(inflater, container, false)
        binding.fragmentHelpIct.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.fragmentHelpIct.toolbarTitle.text = getString(R.string.help_fragment_title_str)


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        binding.fragmentHelpEmailLl.setOnClickListener {

            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:abc@gmail.com")
            }
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            }
        }

        binding.fragmentHelpCallLl.setOnClickListener {
            findNavController().navigate(R.id.action_helpFragment_to_callCentreFragment)
        }

        binding.fragmentHelpFaqLl.setOnClickListener {
            findNavController().navigate(R.id.action_helpFragment_to_FAQFragment)
        }

        binding.fragmentHelpIct.toolbarFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.agentDashboardFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback{
            if (findNavController().currentDestination?.id == R.id.helpFragment) {
                findNavController().navigate(R.id.agentDashboardFragment)
            } else {
                findNavController().popBackStack()
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}