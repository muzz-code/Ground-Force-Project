package com.trapezoidlimited.groundforce.ui.dashboard.extras

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentCreateNewPasswordBinding
import com.trapezoidlimited.groundforce.utils.*


class CreateNewPasswordFragment : Fragment() {

    private var _binding: FragmentCreateNewPasswordBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateNewPasswordBinding.inflate(inflater, container, false)

        binding.fragmentCreateNewPasswordIct.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.fragmentCreateNewPasswordIct.toolbarTitle.text = getString(R.string.create_new_password_title_str)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /** set navigation to go to the home screen **/

        binding.fragmentCreateNewPasswordIct.toolbarFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


        val fields: MutableList<JDataClass> = mutableListOf(
            JDataClass(
                editText = binding.fragmentCreateNewPasswordEt,
                editTextInputLayout = binding.fragmentCreateNewPasswordTil,
                errorMessage = JDErrorConstants.INVALID_PASSWORD_ERROR,
                validator = { it.jdValidatePin(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.fragmentCreateRepeatNewPasswordEt,
                editTextInputLayout = binding.fragmentCreateRepeatNewPasswordTil,
                errorMessage = JDErrorConstants.PASSWORD_DOES_NOT_MATCH,
                validator = {
                    it.jdValidateConfirmPassword(
                        binding.fragmentCreateNewPasswordEt,
                        binding.fragmentCreateRepeatNewPasswordEt
                    )
                }
            )
        )

        JDFormValidator.Builder()
            .addFieldsToValidate(fields)
            .removeErrorIcon(true)
            .viewsToEnable(mutableListOf(binding.fragmentCreateRepeatNewPasswordConfirmBtn))
            .watchWhileTyping(true)
            .build()


    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }


}