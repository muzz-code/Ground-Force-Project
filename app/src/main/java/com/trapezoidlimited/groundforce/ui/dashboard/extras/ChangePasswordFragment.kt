package com.trapezoidlimited.groundforce.ui.dashboard.extras

import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentChangePasswordBinding
import com.trapezoidlimited.groundforce.ui.auth.PhoneActivationFragmentDirections
import com.trapezoidlimited.groundforce.utils.PASSWORD
import com.trapezoidlimited.groundforce.utils.loadFromSharedPreference
import com.trapezoidlimited.groundforce.utils.showSnackBar


class ChangePasswordFragment : Fragment() {

    private var _binding: FragmentChangePasswordBinding? = null
    private val binding
        get() = _binding!!
    private lateinit var pinText: String
    private lateinit var editable: Editable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.fragmentChangePasswordPushButton0.setOnClickListener { setPin(0) }
        binding.fragmentChangePasswordPushButton1.setOnClickListener { setPin(1) }
        binding.fragmentChangePasswordPushButton2.setOnClickListener { setPin(2) }
        binding.fragmentChangePasswordPushButton3.setOnClickListener { setPin(3) }
        binding.fragmentChangePasswordPushButton4.setOnClickListener { setPin(4) }
        binding.fragmentChangePasswordPushButton5.setOnClickListener { setPin(5) }
        binding.fragmentChangePasswordPushButton6.setOnClickListener { setPin(6) }
        binding.fragmentChangePasswordPushButton7.setOnClickListener { setPin(7) }
        binding.fragmentChangePasswordPushButton8.setOnClickListener { setPin(8) }
        binding.fragmentChangePasswordPushButton9.setOnClickListener { setPin(9) }

        pinText = binding.fragmentChangePasswordPinView.text.toString()

        binding.fragmentChangePasswordDeleteIv.setOnClickListener {

            if (pinText.isNotEmpty()) {
                pinText = pinText.substring(0, pinText.length - 1)
                editable = SpannableStringBuilder(pinText)
                binding.fragmentChangePasswordPinView.text = editable
            }
        }

        binding.fragmentChangePasswordResetBtn.setOnClickListener {

            val currentPassword = loadFromSharedPreference(requireActivity(), PASSWORD)
            val pin = binding.fragmentChangePasswordPinView.text.toString()


            if (currentPassword == pin) {
                val action = ChangePasswordFragmentDirections.actionChangePasswordFragmentToCreateNewPasswordFragment(pin)
                findNavController().navigate(action)
            } else {
                showSnackBar(binding.fragmentChangePasswordResetBtn, "Incorrect Password!")
            }


        }

    }

    private fun setPin(num: Int) {
        pinText = binding.fragmentChangePasswordPinView.text.toString()
        if (pinText.length < 4) {
            pinText += num.toString()
            editable = SpannableStringBuilder(pinText)
            binding.fragmentChangePasswordPinView.text = editable
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}