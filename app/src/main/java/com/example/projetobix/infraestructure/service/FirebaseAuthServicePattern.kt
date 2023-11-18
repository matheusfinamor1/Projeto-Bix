package com.example.projetobix.infraestructure.service

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

interface FirebaseAuthServicePattern {
    fun signIn(email: String, password: String): Task<AuthResult>

    fun createUserWithEmailAndPassword(email: String, password: String): Task<AuthResult>

    fun getUser(): FirebaseUser?

    fun signOut()

    fun recoverPassword(email: String): Task<Void>

    fun modifyEmail(email: String): Task<Void>

    fun getEmail(): String

    fun reauthentication(credential: AuthCredential): Task<Void>
}