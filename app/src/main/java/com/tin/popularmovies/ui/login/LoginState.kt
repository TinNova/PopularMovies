package com.tin.popularmovies.ui.login

import com.google.firebase.auth.FirebaseUser

data class LoginState(
    val currentUser: FirebaseUser? = null,
    val isLoggedIn: Boolean = false,
    val isEmailValid: Boolean = true, // if true, show no errors, else show errors
    val isPasswordValid: Boolean = true,
    val isAuthError: Boolean = false
)