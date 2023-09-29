package com.example.projetobix.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projetobix.infraestructure.service.FirebaseAuthServiceGoogle
import com.example.projetobix.infraestructure.service.FirebaseAuthServicePattern
import com.example.projetobix.infraestructure.service.impl.FirebaseAuthServiceGoogleImpl
import com.example.projetobix.infraestructure.service.impl.FirebaseAuthServicePatternImpl
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginViewModel() : ViewModel() {
    private lateinit var firebaseAuthServicePattern: FirebaseAuthServicePattern
    private lateinit var firebaseAuthServiceGoogle: FirebaseAuthServiceGoogle

    private val _statusEmailAndPassword = MutableLiveData<Boolean>()
    val statusEmailAndPassword: LiveData<Boolean> = _statusEmailAndPassword

    private val _msgFailureEmailAndPasswordLogin = MutableLiveData<FirebaseException>()
    val msgFailureEmailAndPasswordLogin: LiveData<FirebaseException> =
        _msgFailureEmailAndPasswordLogin

    private val _statusWithGoogleLogin = MutableLiveData<Boolean>()
    val statusWithGoogleLogin: LiveData<Boolean> = _statusWithGoogleLogin

    private val _msgFailureGoogleLogin = MutableLiveData<FirebaseException>()
    val msgFailureGoogleLogin: LiveData<FirebaseException> = _msgFailureGoogleLogin

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean> = _isConnected

    fun signIn(email: String, password: String) {
        _isLoading.value = true
        firebaseAuthServicePattern = FirebaseAuthServicePatternImpl()
        val task = firebaseAuthServicePattern.signIn(email, password)

        task
            .addOnSuccessListener {
                _statusEmailAndPassword.value = true
                _isLoading.value = false
            }
            .addOnFailureListener {
                _msgFailureEmailAndPasswordLogin.value = try {
                    throw task.exception!!
                } catch (e: FirebaseAuthInvalidUserException) {
                    e
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    e
                } catch (e: FirebaseException) {
                    e
                }
                _isLoading.value = false
            }
    }

    fun signInWithGoogle(credential: AuthCredential) {
        _isLoading.value = true
        firebaseAuthServiceGoogle = FirebaseAuthServiceGoogleImpl()
        val task = firebaseAuthServiceGoogle.signIn(credential)

        task
            .addOnSuccessListener {
                _isLoading.value = true
                _statusWithGoogleLogin.value = true
            }
            .addOnFailureListener {
                _msgFailureGoogleLogin.value = try {
                    throw task.exception!!
                } catch (e: FirebaseException) {
                    e
                }
                _isLoading.value = false
            }

    }

    fun verifyLogin() {
        firebaseAuthServicePattern = FirebaseAuthServicePatternImpl()
        val user = firebaseAuthServicePattern.getUser()

        _isConnected.value = user != null
    }
}
