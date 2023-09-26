package com.example.projetobix.presentation.resetPassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projetobix.infraestructure.service.FirebaseAuthServicePattern
import com.example.projetobix.infraestructure.service.impl.FirebaseAuthServicePatternImpl

class ResetPasswordViewModel: ViewModel() {
    private lateinit var firebaseAuthServicePattern: FirebaseAuthServicePattern

    private val _statusResetPassword = MutableLiveData<Boolean>()
    val statusResetPassword: LiveData<Boolean> = _statusResetPassword

    private val _msgFailureSendResetPassword = MutableLiveData<Exception>()
    val msgFailureSendResetPassword: LiveData<Exception> = _msgFailureSendResetPassword

    fun resetPassword(email: String){
        firebaseAuthServicePattern = FirebaseAuthServicePatternImpl()
        val task = firebaseAuthServicePattern.resetPassword(email)
        task
            .addOnSuccessListener {
                _statusResetPassword.value = true
            }
            .addOnFailureListener {
                _msgFailureSendResetPassword.value = try{
                    throw task.exception!!
                }catch (e: Exception){
                    e
                }
            }
    }
}