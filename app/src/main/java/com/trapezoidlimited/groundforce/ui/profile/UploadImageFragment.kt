package com.trapezoidlimited.groundforce.ui.profile

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentUploadImageBinding
import com.trapezoidlimited.groundforce.utils.showSnackBar


class UploadImageFragment : Fragment() {

    private var _binding: FragmentUploadImageBinding? = null
    private val binding
        get() = _binding!!
    private val REQUEST_IMAGE_CAPTURE = 1
    private val PERMISSION_REQUEST_CODE: Int = 101
    private lateinit var addPhotoImageView: ImageView
    private lateinit var pictureImageView: ImageView

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

        binding.fragmentUploadImageProfileOneIc.toolbarFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.agentDashboardFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback{

            if (findNavController().currentDestination?.id == R.id.uploadImageFragment) {
                findNavController().navigate(R.id.agentDashboardFragment)
            } else {
                findNavController().popBackStack()
            }

        }

        addPhotoImageView.setOnClickListener {
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
            e.message?.let { showSnackBar(requireView(), it) }
        }
    }


    /** onActivityResult function place the captured image on the image view place holder **/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            pictureImageView.setImageBitmap(imageBitmap)
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
                Manifest.permission.CAMERA
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


    override fun onDestroy() {
        super.onDestroy()
        _binding = null!!
    }

}