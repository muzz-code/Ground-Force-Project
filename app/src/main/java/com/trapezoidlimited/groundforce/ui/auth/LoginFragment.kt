package com.trapezoidlimited.groundforce.ui.auth


import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.transition.ChangeBounds
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import com.trapezoidlimited.groundforce.EntryApplication
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.api.ApiService
import com.trapezoidlimited.groundforce.api.MissionsApi
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.databinding.FragmentLoginBinding
import com.trapezoidlimited.groundforce.model.request.LoginRequest
import com.trapezoidlimited.groundforce.repository.AuthRepositoryImpl
import com.trapezoidlimited.groundforce.ui.dashboard.DashboardActivity
import com.trapezoidlimited.groundforce.utils.*
import com.trapezoidlimited.groundforce.viewmodel.AuthViewModel
import com.trapezoidlimited.groundforce.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    @Inject
    lateinit var loginApiServiceService: ApiService

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var missionsApi: MissionsApi

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var googleAccount: GoogleSignInAccount? = null
    private lateinit var emailAddressEt: EditText
    private lateinit var emailAddressTil: TextInputLayout
    private lateinit var pinEt: EditText
    private lateinit var pinTil: TextInputLayout
    private lateinit var loginButton: Button
    private lateinit var loginProgress: ProgressBar
    private val RC_SIGN_IN: Int = 1
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var viewModel: AuthViewModel
    private var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()
    private var pin = ""


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /** Determine how shared elements are handled**/
        sharedElementEnterTransition =
            TransitionInflater.from(this.context).inflateTransition(R.transition.change_bounds)
        sharedElementReturnTransition =
            TransitionInflater.from(this.context).inflateTransition(R.transition.change_bounds)
        /**delay transition**/
        sharedElementEnterTransition = ChangeBounds().apply {
            duration = 750
        }

        /** Inflate the layout for this fragment**/
        _binding = FragmentLoginBinding.inflate(inflater, container, false)


        /**
         * Initialize Views Here
         */
        emailAddressEt = binding.editTextTextEmailAddressEt
        emailAddressTil = binding.editTextTextEmailAddressTil
        pinEt = binding.editTextNumberPinEt
        pinTil = binding.editTextNumberPinTil
        loginButton = binding.loginLoginBtn
        loginProgress = binding.fragmentLoginProgressBar

        /** setting toolbar text **/
//        binding.fragmentLoginTb.toolbarTitle.text = getString(R.string.login_str)
        binding.fragmentLoginTb.toolbarTitle.setTextColor(resources.getColor(R.color.colorWhite))

        /** set navigation arrow from drawable **/
        binding.fragmentLoginTb.toolbarTransparentFragment.setNavigationIcon(R.drawable.ic_arrow_white_back)

        /** Hide status bar**/
        hideStatusBar()

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        handleSpannable()
        validateFields()

        val repository = AuthRepositoryImpl(loginApiServiceService, missionsApi)
        val factory = ViewModelFactory(repository, requireContext())

        //Instantiate View Model
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        //Observe View Model
        viewModel.loginResponse.observe(viewLifecycleOwner, {

            when (it) {
                is Resource.Success -> {
                    binding.fragmentLoginProgressBar.hide(binding.loginLoginBtn)
                    val successResponse = it.value.data?.loginToken!!

                    //On Login Success, Save token to sharedPref and go to dashboard
                    SessionManager.save(requireContext(), TOKEN, successResponse.token)
                    SessionManager.save(requireContext(), USERID, successResponse.id)
                    SessionManager.save(requireContext(), PASSWORD, pin)
                    //saveToSharedPreference(requireActivity(), TOKEN, successResponse.token)
                    // Log.i("Login Response", successResponse.token)
                    goToDashboard()
                }
                is Resource.Failure -> {
                    binding.fragmentLoginProgressBar.hide(binding.loginLoginBtn)
                    handleApiError(it, retrofit, requireView())
                }
            }
        })


        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentLoginTb.toolbarTransparentFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.landingFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().navigate(R.id.landingFragment)
        }

        //Google Sign Up
        binding.loginSignUpGoogleBtn.setOnClickListener {
//            val signInIntent: Intent = googleSignInClient.signInIntent
//            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        /**This code add clickListener to the login button and it move to a new activity **/
        binding.loginLoginBtn.setOnClickListener {

            pin = pinEt.text.toString()

            val loginRequest = LoginRequest(emailAddressEt.text.toString(), pinEt.text.toString())
            binding.fragmentLoginProgressBar.show(it as Button?)
            viewModel.login(loginRequest)
            //goToDashboard()
        }

    }

    /** Validate form fields **/
    private fun validateFields() {

        val fields: MutableList<JDataClass> = mutableListOf(
            JDataClass(
                editText = binding.editTextTextEmailAddressEt,
                editTextInputLayout = binding.editTextTextEmailAddressTil,
                errorMessage = JDErrorConstants.INVALID_EMAIL_ERROR,
                validator = { it.jdValidateEmail(it.text.toString()) }
            ),
            JDataClass(
                editText = binding.editTextNumberPinEt,
                editTextInputLayout = binding.editTextNumberPinTil,
                errorMessage = JDErrorConstants.INVALID_PASSWORD_ERROR,
                validator = { it.jdValidatePin(it.text.toString()) }
            )
        )

        JDFormValidator.Builder()
            .addFieldsToValidate(fields)
            .removeErrorIcon(true)
            .viewsToEnable(mutableListOf(binding.loginLoginBtn))
            .watchWhileTyping(true)
            .build()
    }


    override fun onStart() {
        super.onStart()

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        googleAccount = GoogleSignIn.getLastSignedInAccount(requireContext())
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
            requireActivity().finish()
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            Intent(requireContext(), DashboardActivity::class.java).also {
                it.putExtra("googleAccount", account)
                startActivity(it)
                requireActivity().finish()
            }

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            Log.w(ContentValues.TAG, "signInResult:failed code = " + e.statusCode)
            showSnackBar(requireView(), "signInResult:failed code=" + e.statusCode)
        }
    }


    private fun handleSpannable() {
        /**Get Test from String Resource**/
        val codeText = getText(R.string.new_user_register_here_str)

        /**Get an instance of SpannableString**/
        val ssText = SpannableString(codeText)

        /**Implement ClickableSpan**/
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                view.setOnClickListener {
                    findNavController().navigate(R.id.phoneActivationFragment)
                }
            }

            /**Change color and remove underline**/
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(requireContext(), R.color.colorBlue)
                ds.isUnderlineText = false
            }
        }
        /**Set the span text**/
        ssText.setSpan(clickableSpan, 10, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        /**Make the text spannable and clickable**/
        binding.loginNewUserTv.text = ssText
        binding.loginNewUserTv.movementMethod = LinkMovementMethod.getInstance()


        /**Get Test from String Resource**/
        val codeText2 = getText(R.string.forgot_password_str)

        /**Get an instance of SpannableString**/
        val ssText2 = SpannableString(codeText2)

        /**Implement ClickableSpan**/
        val clickableSpan2: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                view.setOnClickListener {
                    findNavController().navigate(R.id.forgetPasswordFragment)
                }
            }

            /**Change color and remove underline**/
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(requireContext(), R.color.colorBlue)
                ds.isUnderlineText = false
            }
        }
        /**Set the span text**/
        ssText2.setSpan(clickableSpan2, 0, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        /**Make the text spannable and clickable**/
        binding.loginForgetPasswordTv.text = ssText2
        binding.loginForgetPasswordTv.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun goToDashboard() {
        Intent(requireContext(), DashboardActivity::class.java).also { intent ->
            intent.putExtra("googleAccount", googleAccount)
            startActivity(intent)
            requireActivity().finish()
        }
        requireActivity().finish()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
