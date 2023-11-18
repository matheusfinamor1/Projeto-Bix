package com.example.projetobix.presentation.dialog.modifyPassword

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.projetobix.R
import com.example.projetobix.databinding.LoaderAnimationBinding
import com.example.projetobix.databinding.ModifyPasswordBottomSheetDialogFragmentBinding
import com.example.projetobix.extensions.handlerAlertDialog
import com.example.projetobix.extensions.updateLoaderAnimationVisibility
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ModifyPasswordBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private var _binding: ModifyPasswordBottomSheetDialogFragmentBinding? = null
    private val binding get() = _binding!!

    private var _loaderBinding: LoaderAnimationBinding? = null
    private val loaderBinding get() = _loaderBinding!!

    private val viewModel: ModifyPasswordBottomSheetDialogViewModel by viewModels()

    private var msgToast: String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setDimAmount(0.4f)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            ModifyPasswordBottomSheetDialogFragmentBinding.inflate(inflater, container, false)
        _loaderBinding = LoaderAnimationBinding.bind(binding.includeLoaderAnimation.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observers()

        binding.apply {
            btnConfReplPassword.setOnClickListener {
                viewModel.apply {
                    replacePassword(getEmail())
                }
            }
            btnCancReplPassword.setOnClickListener {
                dialog?.dismiss()
            }
        }
    }

    private fun observers() {
        viewModel.apply {
            msgFailureSendEmailModifyPassword.observe(viewLifecycleOwner) {
                msgToast = when (it) {
                    is Exception -> getString(R.string.message_failure_reset_password)
                    else -> getString(R.string.message_failure_generic)
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
            statusModifyPassword.observe(viewLifecycleOwner) {
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
            isLoading.observe(viewLifecycleOwner) {
                if (it) {
                    loaderBinding.updateLoaderAnimationVisibility(true)
                } else {
                    loaderBinding.updateLoaderAnimationVisibility(false)
                    dialog?.dismiss()
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        _loaderBinding = null
    }
}