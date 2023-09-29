package com.example.projetobix.presentation.login

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projetobix.R
import com.example.projetobix.databinding.FragmentLoginBinding
import com.example.projetobix.databinding.LoaderAnimationBinding
import com.example.projetobix.extensions.handlerAlertDialog
import com.example.projetobix.extensions.updateLoaderAnimationVisibility
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var _loaderBinding: LoaderAnimationBinding? = null
    private val loaderBinding get() = _loaderBinding

    private var msgToast: String? = null
    private var showOneTapUI = true
    private val REQ_ONE_TAP = 2
    private lateinit var viewModel: LoginViewModel
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        _loaderBinding = LoaderAnimationBinding.bind(binding.includeLoaderAnimation.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = LoginViewModel()
        observers()
        handlerButtons()
        verifyLogin()
    }

    private fun observers() {
        viewModel.apply {
            msgFailureEmailAndPasswordLogin.observe(viewLifecycleOwner) {
                msgToast = when (it) {
                    is FirebaseAuthInvalidUserException -> getString(R.string.text_user_do_not_registered)
                    is FirebaseAuthInvalidCredentialsException -> getString(R.string.text_email_or_password_do_not_match)
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
            statusEmailAndPassword.observe(viewLifecycleOwner) {
                if (it) {
                    val directions = LoginFragmentDirections.loginFragmentToHomeFragment()
                    findNavController().navigate(directions)
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
                    val directions = LoginFragmentDirections.loginFragmentToHomeFragment()
                    findNavController().navigate(directions)
                }
            }
            isLoading.observe(viewLifecycleOwner) {
                if (it) {
                    loaderBinding?.updateLoaderAnimationVisibility(true)
                } else {
                    loaderBinding?.updateLoaderAnimationVisibility(false)
                }
            }
            isConnected.observe(viewLifecycleOwner) {
                if (it) {
                    val directions = LoginFragmentDirections.loginFragmentToHomeFragment()
                    findNavController().navigate(directions)
                }
            }
        }
    }

    private fun handlerButtons() {

        binding.apply {
            btnLoginScreenLoginGoogle.setOnClickListener {
                handlerClientLogin()
            }
            btnEnter.setOnClickListener {
                val email = binding.etLoginEmail.text.toString()
                val password = binding.etLoginPassword.text.toString()
                if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
                    val alertDialog = handlerAlertDialog(
                        context = requireContext(),
                        title = getString(R.string.text_error_login),
                        icon = R.drawable.ic_error_login,
                        message = getString(R.string.message_failure_login),
                        textPositiveButton = getString(R.string.text_button_ok)
                    )
                    alertDialog.show()
                } else {
                    viewModel.signIn(email, password)
                    cleanEditText()
                }
            }
            btnRememberPassword.setOnClickListener {
                val directions = LoginFragmentDirections.loginFragmentToResetPasswordFragment()
                findNavController().navigate(directions)
            }
            tvRegister.setOnClickListener {
                val directions = LoginFragmentDirections.loginFragmentToRegisterFragment()
                findNavController().navigate(directions)
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

    private fun verifyLogin() {
        viewModel.verifyLogin()
    }

    private fun FragmentLoginBinding.cleanEditText() {
        etLoginEmail.let {
            it.text!!.clear()
        }
        etLoginPassword.let {
            it.text!!.clear()
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