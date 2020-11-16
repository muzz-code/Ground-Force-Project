package com.trapezoidlimited.groundforce.ui.auth


import android.content.ContentValues
import android.content.Intent
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
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentLoginBinding
import com.trapezoidlimited.groundforce.ui.dashboard.DashboardActivity
import com.trapezoidlimited.groundforce.utils.hideStatusBar
import com.trapezoidlimited.groundforce.validator.*
import com.trapezoidlimited.groundforce.validator.Validation.validateEmail
import com.trapezoidlimited.groundforce.validator.Validation.validatePin
import com.trapezoidlimited.groundforce.viewmodel.LoginAuthViewModel
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var googleAccount: GoogleSignInAccount? = null
    private lateinit var emailAddressEt: EditText
    private lateinit var pinEt: EditText
    private lateinit var loginButton: Button

    private val RC_SIGN_IN: Int = 1
    private lateinit var googleSignInClient: GoogleSignInClient
    var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()

    private val viewModel: LoginAuthViewModel by viewModels()


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
        pinEt = binding.editTextNumberPinEt
        loginButton = binding.loginLoginBtn


        validateFields()


        /** setting toolbar text **/
        binding.fragmentLoginTb.toolbarTitle.text = getString(R.string.login_str)
        binding.fragmentLoginTb.toolbarTitle.setTextColor(resources.getColor(R.color.colorWhite))

        /** set navigation arrow from drawable **/
        binding.fragmentLoginTb.toolbarTransparentFragment.setNavigationIcon(R.drawable.ic_arrow_white_back)

        /**Hide status bar**/
        hideStatusBar()

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

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
                ds.color = ContextCompat.getColor(requireContext(), R.color.colorPrimaryBlack)
                ds.isUnderlineText = false
            }
        }
        /**Set the span text**/
        ssText2.setSpan(clickableSpan2, 0, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        /**Make the text spannable and clickable**/
        binding.loginForgetPasswordTv.text = ssText2
        binding.loginForgetPasswordTv.movementMethod = LinkMovementMethod.getInstance()

        return binding.root
    }

    /** Validate form fields **/
    private fun validateFields() {
        emailAddressEt.watchToValidator(EditFieldType.EMAIL)
        pinEt.watchToValidator(EditFieldType.PIN)

        watchAllMyFields(
            mutableMapOf(
                emailAddressEt to EditFieldType.ADDRESS,
                pinEt to EditFieldType.PIN
            ),
            loginButton
        )
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /** set navigation to go to the previous screen on click of navigation arrow **/
        binding.fragmentLoginTb.toolbarTransparentFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().navigate(R.id.landingFragment)
        }


        //Google Sign Up
        binding.loginSignUpGoogleBtn.setOnClickListener {
            val signInIntent: Intent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        /**move to Home **/
        binding.loginLoginBtn.setOnClickListener {

            val email = binding.editTextTextEmailAddressEt.text.toString()
            val pin = binding.editTextNumberPinEt.text.toString()

            if (!validateEmailAndPin(email, pin)) {
                return@setOnClickListener
            } else {

                /** USE CODE WHEN API IS READY: set the email and pin to the login method in the viewModel to make the post request */

                //viewModel.login(email, pin)

                Toast.makeText(requireContext(), "login successful", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.resetPasswordFragment)

            }
        }

        /** USE CODE WHEN API IS READY: observe the loginResponse to authorize  user to navigate to the next page
         * or handle error */

//        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
//            when(it) {
//                is Resource.Success -> {
//                    findNavController().navigate(R.id.dashBoardFragment)
//                }
//                is Resource.Failure -> {
//                    handleApiError(it)
//                }
//            }
//        })


        /**This code add clickListener to the login button and it move to a new activity **/
        binding.loginLoginBtn.setOnClickListener {

            Intent(requireContext(), DashboardActivity::class.java).also {
                it.putExtra("googleAccount", googleAccount)
                startActivity(it)
                requireActivity().finish()
            }

            requireActivity().finish()
        }

    }


    override fun onStart() {
        super.onStart()

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        googleAccount = GoogleSignIn.getLastSignedInAccount(requireContext())
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
            Intent(requireContext(), DashboardActivity::class.java).also {
                it.putExtra("googleAccount", account)
                startActivity(it)
                requireActivity().finish()
            }

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            Log.w(ContentValues.TAG, "signInResult:failed code=" + e.statusCode)
//            showSnackBar(binding.landingSignUpGoogleBtn, "signInResult:failed code=" + e.statusCode)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun validateEmailAndPin(email: String, pin: String): Boolean {
        if (!validateEmail(email)) {
            binding.editTextTextEmailAddressEt.error = "Invalid email"
            return false
        } else if (!validatePin(pin)) {
            binding.editTextNumberPinEt.error = "Invalid password"
            return false
        }

        return true
    }
}
