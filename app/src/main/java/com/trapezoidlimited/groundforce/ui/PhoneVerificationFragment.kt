package com.trapezoidlimited.groundforce.ui

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat.getColor
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentPhoneVerificationBinding
import com.trapezoidlimited.groundforce.utils.showStatusBar

class PhoneVerificationFragment : Fragment() {
    private var _binding: FragmentPhoneVerificationBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhoneVerificationBinding.inflate(inflater, container, false)

        /**show status bar**/
        showStatusBar()
        val view = binding.root

        // Get Test from String Resource
        val codeText = getText(R.string.phone_verif_didnt_get_code_text_str)
        // Get an instance of SpannableString
        val ssText = SpannableString(codeText)
        // Implement ClickableSpan
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                Toast.makeText(requireContext(), "Clicked!", Toast.LENGTH_LONG).show()
            }

            // Change color and remove underline
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = getColor(requireContext(), R.color.colorTextResend)
                ds.isUnderlineText = false
            }
        }
        // Set the span text
        ssText.setSpan(clickableSpan, 21, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        // Make the text spannable and clickable
        binding.phoneVerifResendTv.text = ssText
        binding.phoneVerifResendTv.movementMethod = LinkMovementMethod.getInstance()

        // Inflate the layout for this fragment
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Go to previous screen
        binding.phoneActivArrowBackIv.setOnClickListener {
            findNavController().popBackStack()
        }


        //Text location verification fragment
        //click on phone verification to text
        binding.phoneVerifConfirmBtn.setOnClickListener {
            findNavController().navigate(R.id.locationVerificationFragment)

        }


        requireActivity().onBackPressedDispatcher.addCallback{
            findNavController().popBackStack()
        }

        binding.phoneVerifConfirmBtn.setOnClickListener {
            findNavController().navigate(R.id.createProfileFragmentOne)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}