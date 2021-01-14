package com.trapezoidlimited.groundforce.ui.auth

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentLandingBinding
import com.trapezoidlimited.groundforce.utils.*

class LandingFragment : Fragment() {

    private val RC_SIGN_IN: Int = 1
    private var _binding: FragmentLandingBinding? = null
    private val binding get() = _binding!!
    private lateinit var googleSignInClient: GoogleSignInClient

    // Configure sign-in to request the user's ID, email address, and basic
    // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentLandingBinding.inflate(inflater, container, false)
        val view = binding.root
        // Hide status bar
        hideStatusBar()

        // Inflate the layout for this fragment
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.landingCreateAccBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_landingFragment_to_phoneActivationFragment2)
            //findNavController().navigate(R.id.createProfileFragmentOne)
        }

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        requireActivity().onBackPressedDispatcher.addCallback {
            requireActivity().finish()
        }

        /**move to login fragment**/
        binding.landingSignUpBtn.setOnClickListener {
            val extra = FragmentNavigatorExtras(
                binding.landingWelcomeTv to binding.landingWelcomeTv.transitionName,
                binding.landingSubTitleTv to binding.landingSubTitleTv.transitionName,
                binding.landingCreateAccBtn to binding.landingCreateAccBtn.transitionName,
                binding.landingSignUpGoogleBtn to binding.landingSignUpGoogleBtn.transitionName
            )

            findNavController().navigate(
                R.id.action_landingFragment_to_loginFragment,
                null,
                null,
                extra
            )
        }


        //Google Sign Up
        binding.landingSignUpGoogleBtn.setOnClickListener {
            val signInIntent: Intent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onStart() {
        super.onStart()

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        //     updateUI(account)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

            handleSignInResult(task)
        }
    }

    //Google Sign Up result
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {

            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)

            saveToSharedPreference(requireActivity(), EMAIL_FROM_GOOGLE, account?.email!!)
            saveToSharedPreference(requireActivity(), SIGN_UP_WITH_GGOGLE, "true")

            findNavController().navigate(R.id.action_landingFragment_to_phoneActivationFragment2)

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            showSnackBar(binding.landingSignUpGoogleBtn, "signInResult:failed code=" + e.statusCode)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}