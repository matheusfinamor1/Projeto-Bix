package com.example.projetobix.presentation.dialog.modifyPassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projetobix.infraestructure.service.FirebaseAuthServicePattern
import com.example.projetobix.infraestructure.service.impl.FirebaseAuthServicePatternImpl

class ModifyPasswordBottomSheetDialogViewModel : ViewModel() {
    private lateinit var firebaseAuthServicePattern: FirebaseAuthServicePattern

    private val _statusModifyPassword = MutableLiveData<Boolean>()
    val statusModifyPassword: LiveData<Boolean> = _statusModifyPassword

    private val _msgFailureSendEmailModifyPassword = MutableLiveData<Exception>()
    val msgFailureSendEmailModifyPassword: LiveData<Exception> = _msgFailureSendEmailModifyPassword

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

     fun replacePassword(email: String) {
        _isLoading.value = true
        firebaseAuthServicePattern = FirebaseAuthServicePatternImpl()
        val task = firebaseAuthServicePattern.recoverPassword(email)

        task.addOnSuccessListener {
            _statusModifyPassword.value = true
            _isLoading.value = false
        }
            .addOnFailureListener {
                _msgFailureSendEmailModifyPassword.value = try {
                    throw task.exception!!
                }catch (e: Exception){
                    e
                }
                _isLoading.value = false
            }
    }

    fun getEmail(): String {
        firebaseAuthServicePattern = FirebaseAuthServicePatternImpl()
        return firebaseAuthServicePattern.getEmail()
    }
}