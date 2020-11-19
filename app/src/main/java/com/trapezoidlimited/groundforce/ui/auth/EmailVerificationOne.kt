package com.trapezoidlimited.groundforce.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentEmailVerificationOneBinding
import com.trapezoidlimited.groundforce.utils.JDErrorConstants
import com.trapezoidlimited.groundforce.utils.JDFormValidator
import com.trapezoidlimited.groundforce.utils.JDataClass
import com.trapezoidlimited.groundforce.utils.jdValidateEmail

class EmailVerificationOne : Fragment() {
    private var _binding: FragmentEmailVerificationOneBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEmailVerificationOneBinding.inflate(layoutInflater, container, false)

        /** setting toolbar text **/
        binding.fragmentEmailVerificationTb.toolbarTitle.text =
            getString(R.string.email_verification_title_bar_str)


        /** set navigation arrow from drawable **/
        binding.fragmentEmailVerificationTb.toolbarTransparentFragment.setNavigationIcon(R.drawable.ic_arrow_back)

        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentEmailVerificationTb.toolbarTransparentFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        validateFields()

        binding.fragmentEmailVerificationSubmitBtn.setOnClickListener {
            findNavController().navigate(R.id.action_emailVerificationOne_to_emailVerificationTwo)
        }

    }

    private fun validateFields() {

        val fields: MutableList<JDataClass> = mutableListOf(
            JDataClass(
                editText = binding.fragmentEmailVerificationEt,
                editTextInputLayout = binding.fragmentEmailVerificationTil,
                errorMessage = JDErrorConstants.INVALID_EMAIL_ERROR,
                validator = { it.jdValidateEmail(it.text.toString()) }
            )
        )

        JDFormValidator.Builder()
            .addFieldsToValidate(fields)
            .removeErrorIcon(true)
            .viewsToEnable(mutableListOf(binding.fragmentEmailVerificationSubmitBtn))
            .watchWhileTyping(true)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}