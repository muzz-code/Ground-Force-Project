package com.trapezoidlimited.groundforce.ui.profile

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.trapezoidlimited.groundforce.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentCreateProfileOneBinding
import com.trapezoidlimited.groundforce.validator.AllFormValidator
import com.trapezoidlimited.groundforce.validator.EditFieldType
import com.trapezoidlimited.groundforce.validator.FormFieldValidator
import com.trapezoidlimited.groundforce.validator.FormFieldValidator.watchToValidator
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class CreateProfileFragmentOne : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentCreateProfileOneBinding? = null
    private val binding get() = _binding!!

    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    private lateinit var date: String

    @Inject
    lateinit var requestManager: RequestManager

    //    private val args: Arg
    private var googleAccount: GoogleSignInAccount? = null
    private val args: CreateProfileFragmentOneArgs by navArgs()


    private val PERMISSION_REQUEST_CODE: Int = 101
    private val REQUEST_IMAGE_CAPTURE = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Get Google Account Details from Landing Fragment via navigation argument
        googleAccount = args.googleAccount

        // Inflate the layout for this fragment
        _binding = FragmentCreateProfileOneBinding.inflate(inflater, container, false)


        if (googleAccount != null) {
            setProfileDetailsFromGoogle()
        }

        return binding.root
    }

    private fun setProfileDetailsFromGoogle() {

        //Injected requestManager returns Glide Instance
        if (googleAccount!!.photoUrl != null) {
            requestManager
                .load(googleAccount!!.photoUrl)
                .into(binding.fragmentCreateProfileOneProfileImageIv)
        }
    }


    /** onActivityCreated **/
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        /** Array adapter for spinner drop down for sex **/
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sex,
            android.R.layout.simple_spinner_item
        ).also { sexAdapter ->
            // Specify the layout to use when the list of choices appears
            sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.fragmentCreateProfileOneGenderSp.adapter = sexAdapter
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

            binding.fragmentCreateProfileOneReligionSp.adapter = religionAdapter
        }

        /** listener for sex option **/
        binding.fragmentCreateProfileOneGenderSp.onItemSelectedListener = this

        /** listener for religion option **/
        binding.fragmentCreateProfileOneReligionSp.onItemSelectedListener = this


        /**Validating the Name fields*/
        validateNameFields()

        val genderField = binding.fragmentCreateProfileOneGenderSp
        val religionField = binding.fragmentCreateProfileOneReligionSp
        val dateOfBirth = binding.fragmentCreateProfileOneDateBirthEt

        /** Navigate to contact details page **/
        binding.fragmentCreateProfileOneBtn.setOnClickListener {

            if (!FormFieldValidator.validateDateOfBirth(dateOfBirth.text.toString())) {
                dateOfBirth.error = "Please specify a date of birth"
            } else if (!FormFieldValidator.validateGender(genderField.selectedItem.toString())) {

                showSnackBar(binding.fragmentCreateProfileOneBtn, "Gender field is required.")

            } else if (!FormFieldValidator.validateReligion(religionField.selectedItem.toString())) {

                showSnackBar(binding.fragmentCreateProfileOneBtn, "Religion field is required.")

            } else {
                findNavController().navigate(R.id.createProfileFragmentTwo)
                AllFormValidator.clearFieldsArray()
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


        val cameraButton = binding.fragmentCreateProfileOneIb

        /** On click on camera button, check if permission is granted, if granted take picture or request other wise **/
        cameraButton.setOnClickListener {
            if (checkPermission()) dispatchTakePictureIntent() else requestPermission()
        }
    }


    /** Take picture function **/

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    /** onActivityResult function place the captured image on the image view place holder **/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            binding.fragmentCreateProfileOneProfileImageIv

            val imagePlaceHolder = binding.fragmentCreateProfileOneProfileImageIv
            val imageBitmap = data?.extras?.get("data") as Bitmap
            //imageView.setImageBitmap(imageBitmap)
            imagePlaceHolder.setImageBitmap(imageBitmap)
        }
    }


    /** Check for user permission to access phone camera **/
    private fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.CAMERA
        ) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED)
    }

    /** requestPermission for user permission to access phone camera **/
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(READ_EXTERNAL_STORAGE, CAMERA),
            PERMISSION_REQUEST_CODE
        )
    }


    /** On request permission result grant user permission or show a permission denied message **/
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {

                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {

                    dispatchTakePictureIntent()

                } else {
                    Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }

            else -> {

            }
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


    /** on spinner item selected get item position override method**/
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        parent?.getItemAtPosition(position)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }


    private fun validateNameFields() {
        val firstNameEditText = binding.fragmentCreateProfileFirstNamePlaceholderEt
        val lastNameEditText = binding.fragmentCreateProfileOneLastNameEt


        firstNameEditText.watchToValidator(EditFieldType.NAME)
        lastNameEditText.watchToValidator(EditFieldType.NAME)

        AllFormValidator.watchAllMyFields(
            mutableMapOf(
                firstNameEditText to EditFieldType.NAME,
                lastNameEditText to EditFieldType.NAME,
            ),
            binding.fragmentCreateProfileOneBtn
        )

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}