package com.example.projetobix.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projetobix.infraestructure.service.FirebaseAuthServicePattern
import com.example.projetobix.infraestructure.service.impl.FirebaseAuthServicePatternImpl

class HomeViewModel : ViewModel() {
    private lateinit var firebaseAuthServicePattern: FirebaseAuthServicePattern

    private val _isLogout = MutableLiveData<Boolean>()
    val isLogout: LiveData<Boolean> = _isLogout

    fun logout() {
        _isLogout.value = true

        if (_isLogout.value!!) {
            firebaseAuthServicePattern = FirebaseAuthServicePatternImpl()
            firebaseAuthServicePattern.signOut()
        } else _isLogout.value = false
    }
}