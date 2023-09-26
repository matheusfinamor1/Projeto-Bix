package com.example.projetobix.infraestructure.service

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult

interface FirebaseAuthServiceGoogle {
    fun signIn(credential: AuthCredential): Task<AuthResult>

}