package com.example.projetobix.presentation.resetPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.projetobix.R
import com.example.projetobix.databinding.FragmentResetPasswordBinding
import com.example.projetobix.databinding.LoaderAnimationBinding
import com.example.projetobix.extensions.handlerAlertDialog
import com.example.projetobix.extensions.updateLoaderAnimationVisibility

class ResetPasswordFragment : Fragment() {
    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!

    private var _loaderBinding: LoaderAnimationBinding? = null
    private val loaderBinding get() = _loaderBinding

    private var msgToast: String? = null

    private lateinit var viewModel: ResetPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        _loaderBinding = LoaderAnimationBinding.bind(binding.includeLoaderAnimation.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ResetPasswordViewModel()
        observers()
        handlerButtons()
    }

    private fun observers() {
        viewModel.apply {
            msgFailureSendResetPassword.observe(viewLifecycleOwner) {
                msgToast = when (it) {
                    is Exception -> getString(R.string.message_failure_reset_password)
                    else -> getString(R.string.message_failure_reset_password_generic)
                }
                val alertDialog = handlerAlertDialog(
                    requireContext(),
                    title = getString(R.string.title_failure),
                    icon = R.drawable.ic_error,
                    message = msgToast,
                    textPositiveButton = getString(R.string.text_button_ok)
                )
                alertDialog.show()
            }
            statusResetPassword.observe(viewLifecycleOwner) {
                if (it) {
                    val alertDialog = handlerAlertDialog(
                        requireContext(),
                        title = getString(R.string.title_email_send),
                        icon = R.drawable.ic_confirmed,
                        message = getString(R.string.message_email_send),
                        textPositiveButton = getString(R.string.text_button_ok)
                    )
                    alertDialog.show()
                }
            }
            isLoading.observe(viewLifecycleOwner){
                if(it){
                    loaderBinding?.updateLoaderAnimationVisibility(true)
                }else{
                    loaderBinding?.updateLoaderAnimationVisibility(false)
                }
            }
        }
    }

    private fun handlerButtons() {
        binding.apply {
            btnRecoverPassword.setOnClickListener {
                val email = etResetEmail.text.toString()
                val confirmedEmail = etResetConfirmedEmail.text.toString()
                if (!email.isNullOrEmpty() && !confirmedEmail.isNullOrEmpty()) {
                    if (email == confirmedEmail) {
                        viewModel.resetPassword(email)
                        cleanEditText()
                    } else {
                        cleanEditText()
                        val alertDialog = handlerAlertDialog(
                            context = requireContext(),
                            title = getString(R.string.title_email_different),
                            icon = R.drawable.ic_error_login,
                            message = getString(R.string.message_email_different),
                            textPositiveButton = getString(R.string.text_button_ok)
                        )
                        alertDialog.show()
                    }
                } else {
                    cleanEditText()
                    val alertDialog = handlerAlertDialog(
                        context = requireContext(),
                        title = getString(R.string.title_empty_fields),
                        icon = R.drawable.ic_error,
                        message = getString(R.string.message_empty_fields),
                        textPositiveButton = getString(R.string.text_button_ok)
                    )
                    alertDialog.show()
                }

            }
        }
    }

    private fun FragmentResetPasswordBinding.cleanEditText() {
        etResetEmail.let {
            it.text!!.clear()
        }
        etResetConfirmedEmail.let {
            it.text!!.clear()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}