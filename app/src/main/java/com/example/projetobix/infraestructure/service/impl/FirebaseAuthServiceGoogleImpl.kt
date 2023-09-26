package com.example.projetobix.infraestructure.service.impl

import com.example.projetobix.infraestructure.service.FirebaseAuthServiceGoogle
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthServiceGoogleImpl(): FirebaseAuthServiceGoogle {
    private val auth = FirebaseAuth.getInstance()
    override fun signIn(credential: AuthCredential): Task<AuthResult> {
        return auth.signInWithCredential(credential)
    }


}