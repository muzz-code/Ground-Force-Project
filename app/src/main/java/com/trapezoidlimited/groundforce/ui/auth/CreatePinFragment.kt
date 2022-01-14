package com.trapezoidlimited.groundforce.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentCreatePinBinding
import com.trapezoidlimited.groundforce.ui.dashboard.DashboardActivity
import kotlinx.android.synthetic.main.fragment_create_pin.*


class CreatePinFragment : Fragment() {


    var pinText = ""
    private lateinit var editable: Editable
    private var _binding: FragmentCreatePinBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCreatePinBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fragment_password_reset_btn.setOnClickListener {
            Intent(requireContext(), DashboardActivity::class.java).also {
                startActivity(it)
//                requireActivity().finish()
            }
        }

        binding.forgotPasswordPushButton0.setOnClickListener { setPin(0) }
        binding.forgotPasswordPushButton1.setOnClickListener { setPin(1) }
        binding.forgotPasswordPushButton2.setOnClickListener { setPin(2) }
        binding.forgotPasswordPushButton3.setOnClickListener { setPin(3) }
        binding.forgotPasswordPushButton4.setOnClickListener { setPin(4) }
        binding.forgotPasswordPushButton5.setOnClickListener { setPin(5) }
        binding.forgotPasswordPushButton6.setOnClickListener { setPin(6) }
        binding.forgotPasswordPushButton7.setOnClickListener { setPin(7) }
        binding.forgotPasswordPushButton8.setOnClickListener { setPin(8) }
        binding.forgotPasswordPushButton9.setOnClickListener { setPin(9) }


        //Cancel or delete string on press
        binding.forgotPasswordDeleteIv.setOnClickListener {
            pinText = binding.fragmentResetPasswordPinView.text.toString()
            if (pinText.isNotEmpty()) {
                pinText = pinText.substring(0, pinText.length - 1)
                editable = SpannableStringBuilder(pinText)
                binding.fragmentResetPasswordPinView.text = editable
            }
        }
    }

    // On button press, Concatenate with previous string and set pin view.
    private fun setPin(num: Int) {
        pinText = binding.fragmentResetPasswordPinView.text.toString()
        if (pinText.length < 4) {
            pinText += num.toString()
            editable = SpannableStringBuilder(pinText)
            binding.fragmentResetPasswordPinView.text = editable
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
