package com.trapezoidlimited.groundforce.ui.dashboard.extras

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentSecurityBinding


class SecurityFragment : Fragment() {

    private var _binding: FragmentSecurityBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSecurityBinding.inflate(inflater, container, false)

        binding.fragmentSecurityIct.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.fragmentSecurityIct.toolbarTitle.text = getString(R.string.security_fragment_title_str)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.fragmentSecurityIct.toolbarFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.agentDashboardFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback{
            if (findNavController().currentDestination?.id == R.id.securityFragment) {
                findNavController().navigate(R.id.agentDashboardFragment)
            } else {
                findNavController().popBackStack()
            }
        }

        binding.fragmentSecurityChangePwdLl.setOnClickListener {
            findNavController().navigate(R.id.changePasswordFragment)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}