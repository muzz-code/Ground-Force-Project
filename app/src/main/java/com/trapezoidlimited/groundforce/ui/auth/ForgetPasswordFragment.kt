package com.trapezoidlimited.groundforce.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.misterjedu.jdformvalidator.*
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentEmailVerificationOneBinding
import com.trapezoidlimited.groundforce.databinding.FragmentForgetPasswordBinding
import com.trapezoidlimited.groundforce.utils.showStatusBar


class ForgetPasswordFragment : Fragment() {

    private var _binding: FragmentForgetPasswordBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentForgetPasswordBinding.inflate(layoutInflater, container, false)

        /** setting toolbar text **/
        binding.fragmentForgetPasswordToolbar.toolbarTitle.text =
            getString(R.string.password_reset_title_bar_str)

        //Set Toolbar Back Arrow Icon
        binding.fragmentForgetPasswordToolbar.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)

        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentForgetPasswordToolbar.toolbarFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


        showStatusBar()

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val fields: MutableList<JDataClass> = mutableListOf(
            JDataClass(
                editText = binding.fragmentForgetPasswordEmailEt,
                editTextInputLayout = binding.fragmentForgetPasswordEmailTil,
                errorMessage = JDErrorConstants.INVALID_PASSWORD_ERROR,
                validator = { it.jdValidatePin(it.text.toString()) }
            )
        )

        JDFormValidator.Builder()
            .addFieldsToValidate(fields)
            .removeErrorIcon(true)
            .viewsToEnable(mutableListOf(binding.forgetPasswordResetBtn))
            .watchWhileTyping(true)
            .build()


        binding.forgetPasswordResetBtn.setOnClickListener {
            findNavController().navigate(R.id.action_forgetPasswordFragment_to_resetPasswordFragment)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}