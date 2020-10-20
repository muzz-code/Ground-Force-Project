package com.trapezoidlimited.groundforce.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentResetPasswordBinding
import com.trapezoidlimited.groundforce.validator.ResetPasswordValidator

class ResetPasswordFragment : Fragment() {

    private var _binding:FragmentResetPasswordBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       _binding= FragmentResetPasswordBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.resetpasswordConfirmBtn.setOnClickListener {
            var newPassword=binding.newpasswordEt.text.toString()
            var confirmPassword=binding.confirmpasswordEt.text.toString()
            if(ResetPasswordValidator.isNotEmpty(newPassword) && ResetPasswordValidator.isNotEmpty(confirmPassword)){
                if(!ResetPasswordValidator.isEqual(newPassword,confirmPassword)) {
                    //send to implementing class for use
                }
                else{
                    Toast.makeText(requireContext(),"New password and Confirm password must be equal",Toast.LENGTH_LONG).show()
                }
            }
            else{
                Toast.makeText(requireContext(),"Both New Password and Confirm Password must be entered",Toast.LENGTH_LONG).show()
            }
        }

        binding.resetpasswordArrowBackIv.setOnClickListener {


        }
    }



}