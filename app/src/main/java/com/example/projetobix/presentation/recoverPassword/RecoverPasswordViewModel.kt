package com.example.projetobix.presentation.recoverPassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projetobix.infraestructure.service.FirebaseAuthServicePattern
import com.example.projetobix.infraestructure.service.impl.FirebaseAuthServicePatternImpl

class RecoverPasswordViewModel: ViewModel() {
    private lateinit var firebaseAuthServicePattern: FirebaseAuthServicePattern

    private val _statusRecoverPassword = MutableLiveData<Boolean>()
    val statusRecoverPassword: LiveData<Boolean> = _statusRecoverPassword

    private val _msgFailureSendRecoverPassword = MutableLiveData<Exception>()
    val msgFailureSendRecoverPassword: LiveData<Exception> = _msgFailureSendRecoverPassword

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun recoverPassword(email: String){
        _isLoading.value = true
        firebaseAuthServicePattern = FirebaseAuthServicePatternImpl()
        val task = firebaseAuthServicePattern.recoverPassword(email)
        task
            .addOnSuccessListener {
                _statusRecoverPassword.value = true
                _isLoading.value = false
            }
            .addOnFailureListener {
                _msgFailureSendRecoverPassword.value = try{
                    throw task.exception!!
                }catch (e: Exception){
                    e
                }
                _isLoading.value = false
            }
    }
}