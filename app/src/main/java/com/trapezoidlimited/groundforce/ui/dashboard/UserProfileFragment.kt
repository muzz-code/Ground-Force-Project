package com.trapezoidlimited.groundforce.ui.dashboard

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.trapezoidlimited.groundforce.EntryApplication
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.api.ApiService
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.databinding.FragmentUserProfileBinding
import com.trapezoidlimited.groundforce.images.BitMapConverter
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.utils.*
import com.trapezoidlimited.groundforce.viewmodel.AuthViewModel
import com.trapezoidlimited.groundforce.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Retrofit
import java.io.File
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class UserProfileFragment : Fragment(), AdapterView.OnItemSelectedListener {

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var missionsApi: MissionsApi

    @Inject
    lateinit var retrofit: Retrofit

    private lateinit var viewModel: AuthViewModel

//    private val viewModel by lazy {
//        EntryApplication.authViewModel(this)
//    }

    private var _binding: FragmentUserProfileBinding? = null
    val binding get() = _binding!!
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    private lateinit var date: String
    private val PERMISSION_REQUEST_CODE: Int = 101
    private val REQUEST_IMAGE_CAPTURE = 1

    private val genericRepository by lazy { EntryApplication.groundForceRepository }

    private var googleAccount: GoogleSignInAccount? = null

    private lateinit var profileImageView: ImageView
    private lateinit var addProfileImageView: ImageView

    private lateinit var verifyLocationTextView: TextView

    private val roomViewModel by lazy { EntryApplication.roomViewModel(this) }

    private lateinit var userNameTextView: TextView
    private lateinit var userEmailAddressTextView: TextView
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var dateOfBirthEditText: EditText
    private lateinit var emailAddressEditText: EditText
    private lateinit var additionalPhoneEditText: EditText
    private lateinit var residenceAddressEditText: EditText
    private lateinit var bankDetailsEditText: EditText
    private lateinit var accountNumberEditText: EditText
    private lateinit var isLocationVerified: String


    /** onCreateView over ride function **/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)

        /** Initializing views */

        userNameTextView = binding.fragmentUserProfileUserNameTv
        userEmailAddressTextView = binding.fragmentUserProfileUserEmailTv
        firstNameEditText = binding.fragmentUserProfileFirstNameEt
        lastNameEditText = binding.fragmentUserProfileLastNameEt
        dateOfBirthEditText = binding.fragmentUserProfileDateBirthEt
        emailAddressEditText = binding.fragmentUserProfileEmailAddressEt
        additionalPhoneEditText = binding.fragmentUserProfileAdditionalNumberEt
        residenceAddressEditText = binding.fragmentUserProfileResidentialAddressEt
        verifyLocationTextView = binding.fragmentUserProfileVerifyLocationTv
        accountNumberEditText = binding.fragmentUserProfileAccountNumberEt

        isLocationVerified = loadFromSharedPreference(requireActivity(), LOCATION_VERIFICATION)

        if (COMPLETED_REGISTRATION == "true") {
            binding.fragmentUserProfileHeaderBackgroundCl.visibility = View.GONE
        }


        if (isLocationVerified == "true") {
            setInVisibility(verifyLocationTextView)
        } else {
            setVisibility(verifyLocationTextView)
        }

        /** setting toolbar text **/
        binding.fragmentUserProfileTb.toolbarTitle.text = getString(R.string.profile_str)
        binding.fragmentUserProfileTb.toolbarTitle.setTextColor(resources.getColor(R.color.colorWhite))

        /** set navigation arrow from drawable **/
        binding.fragmentUserProfileTb.toolbarTransparentFragment.setNavigationIcon(R.drawable.ic_arrow_white_back)

        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentUserProfileTb.toolbarTransparentFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.agentDashboardFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            if (findNavController().currentDestination?.id == R.id.userProfileFragment) {
                findNavController().navigate(R.id.agentDashboardFragment)
            } else {
                findNavController().popBackStack()
            }
        }

        roomViewModel.agentObject.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {

                val firstName = it[it.lastIndex].firstName
                val lastName = it[it.lastIndex].lastName
                val name = "$firstName $lastName"
                val email = it[it.lastIndex].email
                val dob = it[it.lastIndex].dob
                val gender = it[it.lastIndex].gender
                val residentialAddress = it[it.lastIndex].residentialAddress

                val firstNameEt = SpannableStringBuilder(firstName)
                val lastNameEt = SpannableStringBuilder(lastName)
                val emailEt = SpannableStringBuilder(email)
                val dobEt = SpannableStringBuilder(dob)
                val residentAddressEt = SpannableStringBuilder(residentialAddress)

                userNameTextView.text = name
                userEmailAddressTextView.text = email
                firstNameEditText.text = firstNameEt
                lastNameEditText.text = lastNameEt
                emailAddressEditText.text = emailEt
                dateOfBirthEditText.text = dobEt
                residenceAddressEditText.text = residentAddressEt


                when (gender) {
                    "m" -> binding.fragmentUserProfileGenderSp.setSelection(1)
                    "f" -> binding.fragmentUserProfileGenderSp.setSelection(2)
                    "o" -> binding.fragmentUserProfileGenderSp.setSelection(3)
                    else -> binding.fragmentUserProfileGenderSp.setSelection(0)
                }


            }
        })

        roomViewModel.additionalDetail.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()){
                val accountNumber = it[it.lastIndex].accountNumber
                val accountNumberEt = SpannableStringBuilder(accountNumber)
                accountNumberEditText.text = accountNumberEt
            }
        })

        setArrayAdapters()

        return binding.root
    }

    /** onActivityCreated **/
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        profileImageView = binding.fragmentCreateProfileOneProfileImageIv
        addProfileImageView = binding.fragmentCreateProfileOneProfileAddPhotoIv


        validateFields()

        val repository = AuthRepositoryImpl(apiService, missionsApi)
        val factory = ViewModelFactory(repository, requireContext())
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        //Save Image Url in Shared Preference on Success
        viewModel.imageUrl.observe(viewLifecycleOwner, {

            val avatarUrl = "http://res.cloudinary.com/da5sxhdqg/image/upload/v1608286961/n6nlpstuc6ypgqhdlqxs.jpg"

            when (it) {
                is Resource.Success -> {
                    it.value.data?.avatarUrl?.let { urlString ->
                        saveToSharedPreference(
                            requireActivity(), IMAGE_URL,
                            urlString
                        )
                    }
                    genericRepository.saveImageFromServer(
                        avatarUrl,
                        profileImageView,
                        requireActivity()
                    )
                }
                is Resource.Failure -> {
                    handleApiError(it, retrofit, requireView())
                }
            }
        })

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

        //Open Camera and capture Image
        addProfileImageView.setOnClickListener {
            if (checkPermission()) dispatchTakePictureIntent() else requestPermission()
        }

        //load the profile image from internal storage if present, else pull from api
        if (!agentImageIsSaved()) {
            val imageUrl = loadFromSharedPreference(requireActivity(), AVATAR_URL)
            if (imageUrl.isNotEmpty()) {
                genericRepository.saveImageFromServer(
                    imageUrl,
                    profileImageView,
                    requireActivity()
                )
            }
        } else {
            genericRepository.getImageFromStorage(requireActivity(), profileImageView)
        }

        verifyLocationTextView.setOnClickListener {
            findNavController().navigate(R.id.verifyLocationFragment)
        }
    }


    private fun agentImageIsSaved(): Boolean {
        val path = File(requireActivity().filesDir, "GroundForce${File.separator}Images")

        val file = File(path, GROUND_FORCE_IMAGE_NAME)
        return file.exists()
    }


    private fun setArrayAdapters() {
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
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        parent?.getItemAtPosition(position)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    /** Take picture function **/
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
            e.message?.let { showSnackBar(requireView(), it) }
        }
    }

    /** onActivityResult function place the captured image on the image view place holder **/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
//            profileImageView.setImageBitmap(imageBitmap)
            val dialogInterface = DialogInterface.OnClickListener { dialog, _ ->
                Toast.makeText(requireActivity(), "Uploading", Toast.LENGTH_SHORT).show()

                GlobalScope.launch {
                    val file = BitMapConverter.toJpg(imageBitmap)

                    val requestFile: RequestBody =
                        file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

                    val requestImage =
                        MultipartBody.Part.createFormData(
                            "ground_force",
                            "profile_image.jpg",
                            requestFile
                        )

//                    viewModel.uploadImage(
//                        requestImage, loadFromSharedPreference(
//                            requireActivity(),
//                            USERID
//                        )
//                    )
                }

                dialog.cancel()
            }
            showAlertDialog("Update your Profile Image?", "Upload Image", dialogInterface)
        }
    }

    /** Check for user permission to access phone camera **/
    private fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireContext(), CAMERA
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            requireContext(), READ_EXTERNAL_STORAGE
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


    override fun onStart() {
        super.onStart()
        googleAccount = GoogleSignIn.getLastSignedInAccount(requireContext())
    }
}