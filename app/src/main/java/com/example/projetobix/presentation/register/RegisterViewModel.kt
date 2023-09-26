package com.example.projetobix.presentation.register

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
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class RegisterViewModel : ViewModel() {
    private lateinit var firebaseAuthServicePattern: FirebaseAuthServicePattern
    private lateinit var firebaseAuthServiceGoogle: FirebaseAuthServiceGoogle


    private val _status = MutableLiveData<Boolean>()
    val status: LiveData<Boolean> = _status

    private val _msg = MutableLiveData<FirebaseException>()
    val msg: LiveData<FirebaseException> = _msg

    private val _statusWithGoogleLogin = MutableLiveData<Boolean>()
    val statusWithGoogleLogin: LiveData<Boolean> = _statusWithGoogleLogin

    private val _msgFailureGoogleLogin = MutableLiveData<FirebaseException>()
    val msgFailureGoogleLogin: LiveData<FirebaseException> = _msgFailureGoogleLogin

    fun signUp(email: String, password: String) {
        firebaseAuthServicePattern = FirebaseAuthServicePatternImpl()

        val task = firebaseAuthServicePattern.createUserWithEmailAndPassword(email, password)
        task.addOnSuccessListener {
            _status.value = true
        }
            .addOnFailureListener {
                _msg.value = try {
                    throw task.exception!!
                } catch (e: FirebaseAuthWeakPasswordException) {
                    e
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    e
                } catch (e: FirebaseAuthUserCollisionException) {
                    e
                }
            }
    }

    fun signInWithGoogle(credential: AuthCredential){
        firebaseAuthServiceGoogle = FirebaseAuthServiceGoogleImpl()
        val task = firebaseAuthServiceGoogle.signIn(credential)

        task
            .addOnSuccessListener {
                _statusWithGoogleLogin.value = true
            }
            .addOnFailureListener {
                _msgFailureGoogleLogin.value = try{
                    throw task.exception!!
                }catch (e: FirebaseException){
                    e
                }
            }

    }


}