package com.trapezoidlimited.groundforce.ui.profile


import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.misterjedu.jdformvalidator.JDErrorConstants
import com.misterjedu.jdformvalidator.JDFormValidator
import com.misterjedu.jdformvalidator.JDataClass
import com.misterjedu.jdformvalidator.jdValidateName
import dagger.hilt.android.AndroidEntryPoint
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.data.AgentObject
import com.trapezoidlimited.groundforce.databinding.FragmentCreateProfileOneBinding
import com.trapezoidlimited.groundforce.validator.*
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class CreateProfileFragmentOne : Fragment() {

    private var _binding: FragmentCreateProfileOneBinding? = null
    private val binding get() = _binding!!

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
            } else {
                AgentObject.firstName =
                    binding.fragmentCreateProfileFirstNamePlaceholderEt.text.toString()
                AgentObject.lastName = binding.fragmentCreateProfileOneLastNameEt.text.toString()
                AgentObject.dob = binding.fragmentCreateProfileOneDateBirthEt.text.toString()

                findNavController().navigate(R.id.createProfileFragmentTwo)
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
            dateButton.setText(date)
            dateOfBirth.error = null
        }


        binding.fragmentCreateProfileOneBtn.setOnClickListener {
            findNavController().navigate(R.id.createProfileFragmentTwo)
        }

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


    private fun validateFields() {

        val fields: MutableList<JDataClass> = mutableListOf(
            JDataClass(
                editText = binding.fragmentCreateProfileFirstNamePlaceholderEt,
                editTextInputLayout = binding.fragmentCreateProfileFirstNamePlaceholderTil,
                errorMessage = JDErrorConstants.EMPTYFIELD_ERROR,
                validator = { it.jdValidateName(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.fragmentCreateProfileOneLastNameEt,
                editTextInputLayout = binding.fragmentCreateProfileOneLastNameTil,
                errorMessage = JDErrorConstants.EMPTYFIELD_ERROR,
                validator = { it.jdValidateName(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.fragmentCreateProfileOnePasswordEt,
                editTextInputLayout = binding.fragmentCreateProfileOnePasswordTil,
                errorMessage = JDErrorConstants.EMPTYFIELD_ERROR,
                validator = { it.jdValidateName(it.text.toString()) }
            )
        )

        JDFormValidator.Builder()
            .addFieldsToValidate(fields)
            .removeErrorIcon(true)
            .viewsToEnable(mutableListOf(binding.fragmentCreateProfileOneBtn))
            .watchWhileTyping(true)
            .build()

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}

