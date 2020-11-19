package com.trapezoidlimited.groundforce.ui.dashboard

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentUserProfileBinding
import com.trapezoidlimited.groundforce.utils.*
import java.util.*


class UserProfileFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private var _binding: FragmentUserProfileBinding? = null

    val binding get() = _binding!!
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    private lateinit var date: String

    private var googleAccount: GoogleSignInAccount? = null

    /** onCreateView over ride function **/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)

        /** setting toolbar text **/
        binding.fragmentUserProfileTb.toolbarTitle.text = getString(R.string.profile_str)
        binding.fragmentUserProfileTb.toolbarTitle.setTextColor(resources.getColor(R.color.colorWhite))

        /** set navigation arrow from drawable **/
        binding.fragmentUserProfileTb.toolbarTransparentFragment.setNavigationIcon(R.drawable.ic_arrow_white_back)


        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentUserProfileTb.toolbarTransparentFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        /** Array adapter for spinner drop down for sex **/
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sex,
            android.R.layout.simple_spinner_item
        ).also { sexAdapter ->
            // Specify the layout to use when the list of choices appears
            sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.fragmentUserProfileGenderSp.adapter = sexAdapter

        }

        /** Array adapter for spinner drop down for religion **/
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.religion,
            android.R.layout.simple_spinner_item
        ).also { religionAdapter ->
            // Specify the layout to use when the list of choices appears
            religionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner

            binding.fragmentUserProfileReligiousSp.adapter = religionAdapter
        }

        /** listener for sex option **/
        binding.fragmentUserProfileGenderSp.onItemSelectedListener = this

        /** listener for religion option **/
        binding.fragmentUserProfileReligiousSp.onItemSelectedListener = this

        binding.fragmentUserProfileFirstNameEt.isEnabled = false
        binding.fragmentUserProfileLastNameEt.isEnabled = false
        binding.fragmentUserProfileAccountNumberEt.isEnabled = false
        binding.fragmentUserProfileResidentialAddressEt.isEnabled = false


        return binding.root
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        parent?.getItemAtPosition(position)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


    /** onActivityCreated **/
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        validateFields()

        val dateButton = binding.fragmentUserProfileDateBirthEt

        /** Show the date button on click of date button **/
        dateButton.setOnClickListener {
            showDatePickerDialog(requireView())
        }

        /** Date set listener **/
        dateSetListener = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
            date = "${month + 1}/$day/$year"
            dateButton.setText(date)
        }

    }

    private fun validateFields() {
        val fields: MutableList<JDataClass> = mutableListOf(
            JDataClass(
                editText = binding.fragmentUserProfileFirstNameEt,
                editTextInputLayout = binding.fragmentUserProfileFirstNameTil,
                errorMessage = JDErrorConstants.NAME_FIELD_ERROR,
                validator = { it.jdValidateName(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.fragmentUserProfileLastNameEt,
                editTextInputLayout = binding.fragmentUserProfileLastNameTil,
                errorMessage = JDErrorConstants.NAME_FIELD_ERROR,
                validator = { it.jdValidateName(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.fragmentUserProfileDateBirthEt,
                editTextInputLayout = binding.fragmentUserProfileDateBirthTil,
                errorMessage = JDErrorConstants.NAME_FIELD_ERROR,
                validator = { it.jdValidateName(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.fragmentUserProfileEmailAddressEt,
                editTextInputLayout = binding.fragmentUserProfileEmailAddressTil,
                errorMessage = JDErrorConstants.INVALID_EMAIL_ERROR,
                validator = { it.jdValidateEmail(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.fragmentUserProfileAdditionalNumberEt,
                editTextInputLayout = binding.fragmentUserProfileAdditionalNumberTil,
                errorMessage = JDErrorConstants.INVALID_PHONE_NUMBER_ERROR,
                validator = { it.jdValidateAdditionalPhone(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.fragmentUserProfileResidentialAddressEt,
                editTextInputLayout = binding.fragmentUserProfileResidentialAddressTil,
                errorMessage = JDErrorConstants.NAME_FIELD_ERROR,
                validator = { it.jdValidateName(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.fragmentUserProfileAccountNumberEt,
                editTextInputLayout = binding.fragmentUserProfileAccountNumberTil,
                errorMessage = JDErrorConstants.BANK_ACCOUNT_NUMBER_ERROR,
                validator = { it.jdValidateAccountNumber(it.text.toString()) }
            )
        )

        JDFormValidator.Builder()
            .addFieldsToValidate(fields)
            .removeErrorIcon(true)
            .watchWhileTyping(true)
            .build()
    }


    /** Show Date picker Dialog Function **/
    @RequiresApi(Build.VERSION_CODES.M)
    private fun showDatePickerDialog(v: View) {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        /** Date dialog picker style **/
        val dialog = DatePickerDialog(
            requireContext(),
            android.R.style.ThemeOverlay_Material_Dialog_Alert,
            dateSetListener, year, month, day
        )
        dialog.show()

    }


    override fun onStart() {
        super.onStart()
        googleAccount = GoogleSignIn.getLastSignedInAccount(requireContext())

    }


}