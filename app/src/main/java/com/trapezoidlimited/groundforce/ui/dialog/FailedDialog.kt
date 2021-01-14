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
import com.trapezoidlimited.groundforce.databinding.FailedDialogBinding
import com.trapezoidlimited.groundforce.ui.dashboard.DashboardActivity


class FailedDialog: DialogFragment() {
    private var _binding: FailedDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner)
        _binding = FailedDialogBinding.inflate(layoutInflater, container, false)


        binding.failedDialogOkTv.setOnClickListener {
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

        /** Setting the dimensions of the dialog */

        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, height)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}