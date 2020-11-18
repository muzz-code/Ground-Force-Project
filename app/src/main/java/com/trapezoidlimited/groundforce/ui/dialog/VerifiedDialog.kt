package com.trapezoidlimited.groundforce.ui.dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.VerificationResultPageBinding
import com.trapezoidlimited.groundforce.ui.auth.LocationsVerificationFragment
import com.trapezoidlimited.groundforce.ui.dashboard.DashboardActivity


class VerifiedDialog : DialogFragment() {
    private var _binding: VerificationResultPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner);
        _binding = VerificationResultPageBinding.inflate(inflater, container, false)


        binding.locationVerifiedDialogTv.setOnClickListener {
            Intent(requireContext(), DashboardActivity::class.java).also {
                startActivity(it)
                requireActivity().finish()
            }

            dismiss()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.55).toInt()
        dialog!!.window?.setLayout(width, height)
    }

}