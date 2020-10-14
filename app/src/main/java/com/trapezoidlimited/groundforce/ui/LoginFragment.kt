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
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentLoginBinding
import com.trapezoidlimited.groundforce.utils.Validation
import com.trapezoidlimited.groundforce.utils.hideStatusBar
import com.trapezoidlimited.groundforce.utils.showStatusBar


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private val validate = Validation()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /** Inflate the layout for this fragment**/
        _binding = FragmentLoginBinding.inflate(inflater, container, false)


        /**Hide status bar**/
        hideStatusBar()

        /**Get Test from String Resource**/
        val codeText = getText(R.string.new_user_register_here_str)
        /**Get an instance of SpannableString**/
        val ssText = SpannableString(codeText)
        /**Implement ClickableSpan**/
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
               view.setOnClickListener {
                   findNavController().navigate(R.id.phoneActivationFragment)
               }
            }

            /**Change color and remove underline**/
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(requireContext(), R.color.colorTextResend)
                ds.isUnderlineText = false
            }
        }
        /**Set the span text**/
        ssText.setSpan(clickableSpan, 10, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        /**Make the text spannable and clickable**/
        binding.loginNewUserTv.text = ssText
        binding.loginNewUserTv.movementMethod = LinkMovementMethod.getInstance()

        return binding.root


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /**move to previous screen**/
        binding.loginArrowBackIv.setOnClickListener {
            findNavController().popBackStack()
        }
        requireActivity().onBackPressedDispatcher.addCallback{
            findNavController().navigate(R.id.landingFragment)
        }

        /**move to Home **/
        binding.loginLoginBtn.setOnClickListener {
            if(!validateEmailAndPin()){
                return@setOnClickListener
            }
            else{
                Toast.makeText(requireContext(), "login successful", Toast.LENGTH_SHORT).show()
                //findNavController().navigate(R.id.dashBoardFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun validateEmailAndPin(): Boolean{
        var email = binding.editTextTextEmailAddressEt.text.toString()
        var pin = binding.editTextNumberPinEt.text.toString()

        if(!validate.validateEmail(email)){
            binding.editTextTextEmailAddressEt.error ="Invalid email"
            return false
        }
        else if(!validate.validatePin(pin)){
            binding.editTextNumberPinEt.error="Invalid password"
            return false
        }
        return true
    }


}