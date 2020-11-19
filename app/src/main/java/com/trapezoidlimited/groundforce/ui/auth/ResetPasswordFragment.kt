package com.trapezoidlimited.groundforce.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentForgetPasswordBinding
import com.trapezoidlimited.groundforce.databinding.FragmentResetPasswordBinding
import com.trapezoidlimited.groundforce.ui.dashboard.DashboardActivity
import com.trapezoidlimited.groundforce.utils.*

class ResetPasswordFragment : Fragment() {

    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentResetPasswordBinding.inflate(layoutInflater, container, false)


        /** setting toolbar text **/
        binding.fragmentResetPasswordToolbar.toolbarTitle.text =
            getString(R.string.password_reset_title_bar_str)

        //Set Toolbar Back Arrow Icon
        binding.fragmentResetPasswordToolbar.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)

        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentResetPasswordToolbar.toolbarFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val fields: MutableList<JDataClass> = mutableListOf(
            JDataClass(
                editText = binding.fragmentResetNewPasswordEt,
                editTextInputLayout = binding.fragmentResetNewPasswordTil,
                errorMessage = JDErrorConstants.INVALID_PASSWORD_ERROR,
                validator = { it.jdValidatePin(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.fragmentResetConfirmNewPasswordEt,
                editTextInputLayout = binding.fragmentResetConfirmNewPasswordTil,
                errorMessage = JDErrorConstants.PASSWORD_DOES_NOT_MATCH,
                validator = {
                    it.jdValidateConfirmPassword(
                        binding.fragmentResetNewPasswordEt,
                        it.text.toString()
                    )
                }
            )
        )

        JDFormValidator.Builder()
            .addFieldsToValidate(fields)
            .removeErrorIcon(true)
            .viewsToEnable(mutableListOf(binding.forgetResetConfirmBtn))
            .watchWhileTyping(true)
            .build()



        binding.forgetResetConfirmBtn.setOnClickListener {
            Intent(requireContext(), DashboardActivity::class.java).also {
                startActivity(it)
                requireActivity().finish()
            }
        }
    }
}