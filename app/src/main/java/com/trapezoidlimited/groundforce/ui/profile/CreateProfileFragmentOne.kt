package com.trapezoidlimited.groundforce.ui.profile


import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.AndroidEntryPoint
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.data.AgentObject
import com.trapezoidlimited.groundforce.databinding.FragmentCreateProfileOneBinding
import com.trapezoidlimited.groundforce.utils.*
import com.trapezoidlimited.groundforce.utils.jdValidateName
import com.trapezoidlimited.groundforce.validator.*
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class CreateProfileFragmentOne : Fragment() {

    private var _binding: FragmentCreateProfileOneBinding? = null
    private val binding get() = _binding!!
    private var yearPicked = 0
    private var currentYear = 0
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    private lateinit var date: String

    @Inject
    lateinit var requestManager: RequestManager

    //    private val args: Arg
    private var googleAccount: GoogleSignInAccount? = null
    private val args: CreateProfileFragmentOneArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Get Google Account Details from Landing Fragment via navigation argument
        googleAccount = args.googleAccount

        // Inflate the layout for this fragment
        _binding = FragmentCreateProfileOneBinding.inflate(inflater, container, false)

        binding.fragmentCreateProfileOneIc.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)

        binding.fragmentCreateProfileOneIc.toolbarFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.emailVerificationTwo)
        }


        return binding.root
    }


    /** onActivityCreated **/
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        /**Validating the Name fields*/
        validateFields()


        val dateOfBirth = binding.fragmentCreateProfileOneDateBirthEt

        /** Navigate to contact details page **/
        binding.fragmentCreateProfileOneBtn.setOnClickListener {

            if (!Validation.validateDateOfBirth(dateOfBirth.text.toString())) {
                dateOfBirth.error = "Please specify a date of birth"
            } else if (currentYear - yearPicked !in 18..120) {
                showSnackBar(requireView(), "Age must be between 18 - 120")
            } else {
                val firstName = binding.fragmentCreateProfileFirstNamePlaceholderEt.text.toString()
                val lastName = binding.fragmentCreateProfileOneLastNameEt.text.toString()
                val dob = binding.fragmentCreateProfileOneDateBirthEt.text.toString()
                val password = binding.fragmentCreateProfileOnePasswordEt.text.toString()

                /** Saving USER PROFILE Details in sharedPreference*/

                saveToSharedPreference(requireActivity(), FIRSTNAME, firstName)
                saveToSharedPreference(requireActivity(), LASTNAME, lastName)
                saveToSharedPreference(requireActivity(), DOB, dob)
                saveToSharedPreference(requireActivity(), PASSWORD, password)


                Log.i("DOB", loadFromSharedPreference(requireActivity(), DOB))

                //findNavController().navigate(R.id.createProfileFragmentTwo)
                findNavController().navigate(R.id.action_createProfileFragmentOne_to_locationsVerificationFragment)
            }

        }


        val dateButton = binding.fragmentCreateProfileOneDateBirthEt

        /** Show the date button on click of date button **/
        dateButton.setOnClickListener {
            showDatePickerDialog(requireView())
        }

        /** Date set listener **/
        dateSetListener = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
            date = "${month + 1}/$day/$year"
            yearPicked = year
            dateButton.setText(date)
            dateOfBirth.error = null
        }


    }


    /** Show Date picker Dialog Function **/
    @RequiresApi(Build.VERSION_CODES.M)
    private fun showDatePickerDialog(v: View) {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        currentYear = c.get(Calendar.YEAR)
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


    private fun validateFields() {

        val fields: MutableList<JDataClass> = mutableListOf(
            JDataClass(
                editText = binding.fragmentCreateProfileFirstNamePlaceholderEt,
                editTextInputLayout = binding.fragmentCreateProfileFirstNamePlaceholderTil,
                errorMessage = JDErrorConstants.NAME_FIELD_ERROR,
                validator = { it.jdValidateName(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.fragmentCreateProfileOneLastNameEt,
                editTextInputLayout = binding.fragmentCreateProfileOneLastNameTil,
                errorMessage = JDErrorConstants.NAME_FIELD_ERROR,
                validator = { it.jdValidateName(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.fragmentCreateProfileOnePasswordEt,
                editTextInputLayout = binding.fragmentCreateProfileOnePasswordTil,
                errorMessage = JDErrorConstants.INVALID_PASSWORD_ERROR,
                validator = { it.jdValidatePin(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.fragmentCreateProfileOneConfirmPasswordEt,
                editTextInputLayout = binding.fragmentCreateProfileOneConfirmPasswordTil,
                errorMessage = JDErrorConstants.PASSWORD_DOES_NOT_MATCH,
                validator = { it.jdValidateConfirmPassword(binding.fragmentCreateProfileOnePasswordEt,
                    binding.fragmentCreateProfileOneConfirmPasswordEt) }
            )
        )

        JDFormValidator.Builder()
            .addFieldsToValidate(fields)
            .removeErrorIcon(true)
            .viewsToEnable(mutableListOf(binding.fragmentCreateProfileOneBtn))
            .watchWhileTyping(true)
            .build()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

