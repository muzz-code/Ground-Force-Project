package com.trapezoidlimited.groundforce.ui


import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.transition.ChangeBounds
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentLoginBinding
import com.trapezoidlimited.groundforce.viewmodel.LoginAuthViewModel
import com.trapezoidlimited.groundforce.validator.Validation
import com.trapezoidlimited.groundforce.utils.hideStatusBar


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private val validate = Validation()


    private val viewModel: LoginAuthViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /** Determine how shared elements are handled**/
        sharedElementEnterTransition =
            TransitionInflater.from(this.context).inflateTransition(R.transition.change_bounds)
        sharedElementReturnTransition =
            TransitionInflater.from(this.context).inflateTransition(R.transition.change_bounds)
        /**delay transition**/
        sharedElementEnterTransition = ChangeBounds().apply {
            duration = 750
        }


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


        /**Get Test from String Resource**/
        val codeText2 = getText(R.string.forgot_password_str)

        /**Get an instance of SpannableString**/
        val ssText2 = SpannableString(codeText2)

        /**Implement ClickableSpan**/
        val clickableSpan2: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                view.setOnClickListener {
                    findNavController().navigate(R.id.forgetPasswordFragment)
                }
            }

            /**Change color and remove underline**/
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(requireContext(), R.color.colorTextPrimary)
                ds.isUnderlineText = false
            }
        }
        /**Set the span text**/
        ssText2.setSpan(clickableSpan2, 0, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        /**Make the text spannable and clickable**/
        binding.loginForgetPasswordTv.text = ssText2
        binding.loginForgetPasswordTv.movementMethod = LinkMovementMethod.getInstance()

        return binding.root


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        /**move to previous screen**/
        binding.loginArrowBackIv.setOnClickListener {
            findNavController().navigate(R.id.landingFragment)
        }
        requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().navigate(R.id.landingFragment)
        }

        /**move to Home **/
        binding.loginLoginBtn.setOnClickListener {

            val email = binding.editTextTextEmailAddressEt.text.toString()
            val pin = binding.editTextNumberPinEt.text.toString()

            if (!validateEmailAndPin(email, pin)) {
                return@setOnClickListener
            } else {

                /** USE CODE WHEN API IS READY: set the email and pin to the login method in the viewModel to make the post request */

                //viewModel.login(email, pin)

                Toast.makeText(requireContext(), "login successful", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.resetPasswordFragment)

            }
        }

        /** USE CODE WHEN API IS READY: observe the loginResponse to authorize  user to navigate to the next page
         * or handle error */

//        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
//            when(it) {
//                is Resource.Success -> {
//                    findNavController().navigate(R.id.dashBoardFragment)
//                }
//                is Resource.Failure -> {
//                    handleApiError(it)
//                }
//            }
//        })


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun validateEmailAndPin(email: String, pin: String): Boolean {


        if (!validate.validateEmail(email)) {
            binding.editTextTextEmailAddressEt.error = "Invalid email"
            return false
        } else if (!validate.validatePin(pin)) {
            binding.editTextNumberPinEt.error = "Invalid password"
            return false
        }

        return true
    }
}
