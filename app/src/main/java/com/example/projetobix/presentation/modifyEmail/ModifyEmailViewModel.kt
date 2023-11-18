package com.example.projetobix.presentation.modifyEmail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projetobix.infraestructure.service.FirebaseAuthServicePattern
import com.example.projetobix.infraestructure.service.impl.FirebaseAuthServicePatternImpl
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException

class ModifyEmailViewModel : ViewModel() {
    private lateinit var firebaseAuthServicePattern: FirebaseAuthServicePattern

    private val _statusModifyEmail = MutableLiveData<Boolean>()
    val statusModifyEmail: LiveData<Boolean> = _statusModifyEmail

    private val _msgFailureModifyEmail = MutableLiveData<Exception>()
    val msgFailureModifyEmail: LiveData<Exception> = _msgFailureModifyEmail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun reauthenticationUser(email: String, password: String) {
        firebaseAuthServicePattern = FirebaseAuthServicePatternImpl()

        val credential = EmailAuthProvider.getCredential(email, password)
        firebaseAuthServicePattern.reauthentication(credential)
    }

    fun modifyEmail(email: String) {
        _isLoading.value = true
        firebaseAuthServicePattern = FirebaseAuthServicePatternImpl()
        val task = firebaseAuthServicePattern.modifyEmail(email)

        task.addOnSuccessListener {
            _statusModifyEmail.value = true
            _isLoading.value = false
            firebaseAuthServicePattern.signOut()
        }
            .addOnFailureListener {
                _msgFailureModifyEmail.value = try {
                    throw task.exception!!
                } catch (auth: FirebaseAuthRecentLoginRequiredException) {
                    auth
                }
                _isLoading.value = false
            }
    }

    fun getEmail(): String {
        firebaseAuthServicePattern = FirebaseAuthServicePatternImpl()
        return firebaseAuthServicePattern.getEmail()
    }
}