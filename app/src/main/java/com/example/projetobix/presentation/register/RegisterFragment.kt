package com.example.projetobix.presentation.register

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projetobix.R
import com.example.projetobix.databinding.FragmentRegisterBinding
import com.example.projetobix.extensions.handlerAlertDialog
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.GoogleAuthProvider

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val REQ_ONE_TAP = 2
    private var msgToast: String? = null
    private var showOneTapUI = true

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = RegisterViewModel()
        observers()
        handlerButtons()

    }

    private fun handlerButtons() {

        binding.apply {
            btnCreateAccount.setOnClickListener {
                val email = binding.etRegisterEmail.text.toString()
                val password = binding.etRegisterPassword.text.toString()
                val confirmPassword = binding.etRegisterConfirmPassword.text.toString()

                if (email.isNullOrEmpty() || password.isNullOrEmpty() || confirmPassword.isNullOrEmpty()) {
                    val alertDialog = handlerAlertDialog(
                        context = requireContext(),
                        title = getString(R.string.title_failure_registered),
                        icon = R.drawable.ic_error_login,
                        message = getString(R.string.text_fill_in_all_fields),
                        textPositiveButton = getString(R.string.text_button_ok)
                    )
                    alertDialog.show()
                } else
                    viewModel.signUp(email, password)
            }

            btnRegisterScreenLoginGoogle.setOnClickListener {
                handlerClientLogin()
            }
        }
    }

    private fun observers() {
        viewModel.apply {
            msg.observe(viewLifecycleOwner) {

                msgToast = when (it) {
                    is FirebaseAuthWeakPasswordException -> getString(R.string.text_strong_password)
                    is FirebaseAuthInvalidCredentialsException -> getString(R.string.text_valid_email)
                    is FirebaseAuthUserCollisionException -> getString(R.string.text_registered_email)
                    else -> getString(R.string.text_error_registered)
                }
                val alertDialog = handlerAlertDialog(
                    requireContext(),
                    title = getString(R.string.title_failure),
                    icon = R.drawable.ic_error_login,
                    message = msgToast,
                    textPositiveButton = getString(R.string.text_button_ok)
                )
                alertDialog.show()
            }

            status.observe(viewLifecycleOwner) {
                if (it) {
                    val directions = RegisterFragmentDirections.registerFragmentToHomeFragment()
                    findNavController().navigate(directions)
                    val alertDialog = handlerAlertDialog(
                        requireContext(),
                        title = getString(R.string.title_success),
                        icon = R.drawable.ic_confirmed,
                        message = getString(R.string.message_account_create_success),
                        textPositiveButton = getString(R.string.text_button_ok)
                    )
                    alertDialog.show()
                }
            }
            msgFailureGoogleLogin.observe(viewLifecycleOwner) {
                msgToast = when (it) {
                    is FirebaseException -> getString(R.string.text_error_login)
                    else -> getString(R.string.text_error)
                }
                val alertDialog = handlerAlertDialog(
                    requireContext(),
                    title = getString(R.string.title_failure),
                    icon = R.drawable.ic_google,
                    message = msgToast,
                    textPositiveButton = getString(R.string.text_button_ok)
                )
                alertDialog.show()
            }
            statusWithGoogleLogin.observe(viewLifecycleOwner) {
                if (it) {
                    val directions = RegisterFragmentDirections.registerFragmentToHomeFragment()
                    findNavController().navigate(directions)
                }
            }
            isLoading.observe(viewLifecycleOwner){
                if(it){
                    binding.includeLoaderAnimation.loaderAnimation.isVisible = true
                    binding.includeLoaderAnimation.loaderAnimation.playAnimation()
                }else{
                    binding.includeLoaderAnimation.loaderAnimation.isVisible = false
                    binding.includeLoaderAnimation.loaderAnimation.cancelAnimation()
                }
            }
        }
    }

    private fun handlerClientLogin() {
        oneTapClient = Identity.getSignInClient(requireContext())
        handlerFiltersLogin()
        handlerOneTap()
    }

    private fun handlerOneTap() {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                try {
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender, REQ_ONE_TAP,
                        null, 0, 0, 0, null
                    )
                } catch (e: IntentSender.SendIntentException) {

                    val alertDialog = handlerAlertDialog(
                        requireContext(),
                        title = getString(R.string.title_failure_login),
                        icon = R.drawable.ic_google,
                        message = getString(R.string.message_unable_login),
                        textPositiveButton = getString(R.string.text_button_ok)
                    )
                    alertDialog.show()
                }
            }
            .addOnFailureListener {
                val alertDialog = handlerAlertDialog(
                    requireContext(),
                    title = getString(R.string.title_failure_registered),
                    icon = R.drawable.ic_google,
                    message = getString(R.string.message_unable_login),
                    textPositiveButton = getString(R.string.text_button_ok)
                )
                alertDialog.show()
            }
    }

    private fun handlerFiltersLogin() {
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build()
            )
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    private fun handleResultOneTap(requestCode: Int, data: Intent?) {
        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    when {
                        idToken != null -> {
                            val firebaseCredential =
                                GoogleAuthProvider.getCredential(idToken, null)
                            viewModel.signInWithGoogle(firebaseCredential)
                        }
                    }

                } catch (e: ApiException) {
                    handleExceptionOneTap(e)
                }
            }
        }
    }

    private fun handleExceptionOneTap(e: ApiException) {
        when (e.statusCode) {
            CommonStatusCodes.CANCELED -> {
                showOneTapUI = false
            }

            CommonStatusCodes.NETWORK_ERROR -> {
                val alertDialog = handlerAlertDialog(
                    requireContext(),
                    title = getString(R.string.title_failure),
                    icon = R.drawable.ic_google,
                    message = getString(R.string.message_failure_connection),
                    textPositiveButton = getString(R.string.text_button_ok)
                )
                alertDialog.show()
            }

            else -> {
                val alertDialog = handlerAlertDialog(
                    requireContext(),
                    title = getString(R.string.title_failure_registered),
                    icon = R.drawable.ic_google,
                    message = getString(R.string.message_failure_registered),
                    textPositiveButton = getString(R.string.text_button_ok)
                )
                alertDialog.show()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        handleResultOneTap(requestCode, data)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}