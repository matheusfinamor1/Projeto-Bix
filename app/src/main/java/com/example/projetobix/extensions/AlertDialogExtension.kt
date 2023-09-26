package com.example.projetobix.extensions

import android.content.Context
import com.example.projetobix.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun handlerAlertDialog(
    context: Context,
    title: String? = null,
    icon: Int? = R.drawable.ic_priority,
    message: String? = null,
    textPositiveButton: String? = null
): MaterialAlertDialogBuilder {
    return MaterialAlertDialogBuilder(context)
        .setTitle(title)
        .setIcon(icon!!)
        .setMessage(message)
        .setPositiveButton(textPositiveButton) { dialog, wich -> }
}