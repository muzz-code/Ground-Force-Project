package com.trapezoidlimited.groundforce.ui.profile

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.api.ApiService
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.databinding.FragmentUploadImageBinding
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.utils.*
import com.trapezoidlimited.groundforce.viewmodel.AuthViewModel
import com.trapezoidlimited.groundforce.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import javax.inject.Inject


@AndroidEntryPoint
class UploadImageFragment : Fragment() {

    @Inject
    lateinit var loginApiServiceService: ApiService

    @Inject
    lateinit var missionsApi: MissionsApi

    @Inject
    lateinit var retrofit: Retrofit

    private lateinit var viewModel: AuthViewModel

    private var _binding: FragmentUploadImageBinding? = null
    private val binding
        get() = _binding!!
    private val REQUEST_IMAGE_CAPTURE = 1
    private val PERMISSION_REQUEST_CODE: Int = 101
    private lateinit var addPhotoImageView: ImageView
    private lateinit var pictureImageView: ImageView
    private lateinit var nextProfileButton: Button

    private var photoPath: Uri = Uri.parse("")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUploadImageBinding.inflate(inflater, container, false)

        binding.fragmentUploadImageProfileOneIc.toolbarTitle.text = "Update Profile"

        /** set navigation arrow from drawable **/
        binding.fragmentUploadImageProfileOneIc.toolbarFragment.setNavigationIcon(R.drawable.ic_arrow_back)


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        addPhotoImageView = binding.fragmentUploadImageProfileAddPhotoIv
        pictureImageView = binding.fragmentUploadImagePictureIv
        nextProfileButton = binding.fragmentUploadImageProfileNextBtn

        val repository = AuthRepositoryImpl(loginApiServiceService, missionsApi)
        val factory = ViewModelFactory(repository, requireContext())

        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        binding.fragmentUploadImageProfileOneIc.toolbarFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.agentDashboardFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            if (findNavController().currentDestination?.id == R.id.uploadImageFragment) {
                findNavController().navigate(R.id.agentDashboardFragment)
            } else {
                findNavController().popBackStack()
            }
        }

        addPhotoImageView.setOnClickListener {

            if (checkPermission()) {
                dispatchTakePictureIntent()
            } else {
                requestPermission()
            }
        }





        viewModel.imageUrl.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    binding.fragmentUploadImageProfilePb.hide(nextProfileButton)

                    val avatarUrl = response.value.data?.avatarUrl
                    val publicId = response.value.data?.publicId


                    if (avatarUrl != null) {
                        saveToSharedPreference(requireActivity(), AVATAR_URL, avatarUrl)
                        Toast.makeText(requireContext(), avatarUrl, Toast.LENGTH_SHORT).show()
                    }

                    if (publicId != null) {
                        saveToSharedPreference(requireActivity(), PUBLIC_ID, publicId)
                    }

                    findNavController().navigate(R.id.updateProfileFragment)

                }
                is Resource.Failure -> {

                    binding.fragmentUploadImageProfilePb.hide(nextProfileButton)

                    handleApiError(response, retrofit, requireView())
                }
            }
        })


        nextProfileButton.setOnClickListener {

            processUri(photoPath).observe(viewLifecycleOwner, {
                binding.fragmentUploadImageProfilePb.show(nextProfileButton)
                viewModel.uploadImage(it, requireActivity())
            })

        }

    }

    private fun processUri(uri: Uri): LiveData<Uri> {
        val mBitmap = uriToBitmap(uri)
        val mFile = saveBitmap(mBitmap)!!
        val uriMutableLiveData: MutableLiveData<Uri> = MutableLiveData()
        val uriLiveData: LiveData<Uri> = uriMutableLiveData

        lifecycleScope.launch {
            val compressedImageFile = Compressor.compress(requireContext(), mFile)
            val mUri = compressedImageFile.toUri()
            uriMutableLiveData.value = mUri
        }
        return uriLiveData
    }


    /** Take picture function **/
    private fun dispatchTakePictureIntent() {
        val fileName = "ground_force_name.jpg"
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, fileName)
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera")
        photoPath = requireActivity().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )!!
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoPath)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }


    /** onActivityResult function place the captured image on the image view place holder **/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

            Glide.with(this)
                .load(photoPath)
                .into(pictureImageView)

            nextProfileButton.isEnabled = true
        }
    }


    /** Check for user permission to access phone camera **/
    private fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED)
    }

    /** requestPermission for user permission to access phone camera **/
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}