package com.example.projetobix.infraestructure.service.impl

import com.example.projetobix.infraestructure.service.FirebaseAuthServicePattern
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseAuthServicePatternImpl: FirebaseAuthServicePattern {

    private val firebaseAuth = FirebaseAuth.getInstance()
    override fun signIn(email: String, password: String): Task<AuthResult> {
        return firebaseAuth.signInWithEmailAndPassword(email,password)
    }

    override fun createUserWithEmailAndPassword(email: String, password: String): Task<AuthResult> {
        return firebaseAuth.createUserWithEmailAndPassword(email, password)
    }

    override fun getUser(): FirebaseUser {
        return firebaseAuth.currentUser!!
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    override fun resetPassword(email: String): Task<Void> {
        return firebaseAuth.sendPasswordResetEmail(email)
    }
}