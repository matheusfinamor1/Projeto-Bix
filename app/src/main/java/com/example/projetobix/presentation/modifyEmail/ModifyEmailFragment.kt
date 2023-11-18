package com.example.projetobix.presentation.modifyEmail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.projetobix.R
import com.example.projetobix.databinding.FragmentModifyEmailBinding
import com.example.projetobix.databinding.LoaderAnimationBinding
import com.example.projetobix.extensions.handlerAlertDialog
import com.example.projetobix.extensions.updateLoaderAnimationVisibility
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException

class ModifyEmailFragment : Fragment() {
    private var _binding: FragmentModifyEmailBinding? = null
    private val binding get() = _binding!!

    private var _loaderAnimation: LoaderAnimationBinding? = null
    private val loaderAnimation get() = _loaderAnimation!!

    private val viewModel: ModifyEmailViewModel by viewModels()

    private var email = ""
    private var password = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModifyEmailBinding.inflate(inflater, container, false)
        _loaderAnimation = LoaderAnimationBinding.bind(binding.includeLoaderAnimation.root)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observers()
        handlerButton()
    }

    private fun handlerButton() {
        binding.apply {
            btnModifyEmail.setOnClickListener {
                val currentEmailLogged = viewModel.getEmail()
                email = binding.etModifyEmail.text.toString()
                password = binding.etReqPasswordModifyEmail.text.toString()
                val newEmail = binding.etModifyNewEmail.text.toString()

                if (currentEmailLogged == email) {
                    viewModel.modifyEmail(newEmail)
                } else {
                    val alertDialog = handlerAlertDialog(
                        context = requireContext(),
                        title = getString(R.string.title_error_modify_email),
                        icon = R.drawable.ic_error_login,
                        message = getString(R.string.text_error_modify_email),
                        textPositiveButton = getString(R.string.text_button_ok)
                    )
                    alertDialog.show()
                }
            }
        }
    }

    private fun observers() {
        viewModel.apply {
            msgFailureModifyEmail.observe(viewLifecycleOwner) {
                when (it) {
                    is FirebaseAuthRecentLoginRequiredException -> {
                        reauthenticationUser(email, password)
                    }

                    else -> {}
                }
                val alertDialog = handlerAlertDialog(
                    requireContext(),
                    title = getString(R.string.title_failure),
                    icon = R.drawable.ic_error,
                    message = getString(R.string.text_error_modify_email),
                    textPositiveButton = getString(R.string.text_button_ok)
                )
                alertDialog.show()
            }
            statusModifyEmail.observe(viewLifecycleOwner) {
                if (it) {
                    val alertDialog = handlerAlertDialog(
                        requireContext(),
                        title = getString(R.string.title_confir_modify_email),
                        icon = R.drawable.ic_confirmed,
                        message = getString(R.string.text_confirm_modify_email),
                        textPositiveButton = getString(R.string.text_button_ok)
                    )
                    alertDialog.show()
                    val directions =
                        ModifyEmailFragmentDirections.modifyEmailFragmentToLoginFragment()
                    findNavController().navigate(directions)
                }
            }
            isLoading.observe(viewLifecycleOwner) {
                if (it) {
                    loaderAnimation.updateLoaderAnimationVisibility(true)
                } else {
                    loaderAnimation.updateLoaderAnimationVisibility(false)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        _loaderAnimation = null
    }

}