package com.trapezoidlimited.groundforce.ui

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentPhoneVerificationBinding

/**
 * A simple [Fragment] subclass.
 * Use the [PhoneVerificationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhoneVerificationFragment : Fragment() {
    private var _binding: FragmentPhoneVerificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhoneVerificationBinding.inflate(inflater, container, false)
        val view = binding.root

        // Get Test from String Resource
        val codeText = getText(R.string.phone_verif_didnt_get_code_text)
        // Get an instance of SpannableString
        val ssText = SpannableString(codeText)
        // Implement ClickableSpan
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                Toast.makeText(requireContext(), "Clicked!", Toast.LENGTH_LONG).show()
            }
        }
        // Set the span text
        ssText.setSpan(clickableSpan, 21, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        // Make the text spannable and clickable
        binding.phoneVerifTvResend.text = ssText
        binding.phoneVerifTvResend.movementMethod = LinkMovementMethod.getInstance()

        // Inflate the layout for this fragment
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}