package com.tin.popularmovies.ui.login

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.tin.popularmovies.api.FireCloud
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val fireCloud: FireCloud

) : ViewModel(), LifecycleObserver {

    val fBUserLD = MutableLiveData<LoginState>()

    fun onInitView() {
        login(false)
    }

    private fun login(isAuthError: Boolean) {
        if (fireCloud.isUserLoggedIn()) {
            fBUserLD.value = LoginState(isLoggedIn = true)
        } else {
            fBUserLD.value = LoginState(isLoggedIn = false, isAuthError = isAuthError)
        }
    }

    /** Register New Users When Create Account Btn Clicked */
    fun onRegisterBtnClicked(email: String, password: String) {
        if (isValidateForm(email, password)) {

            fireCloud.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    login(false)

                } else {
                    login(true)
                    Log.w("$this", "createUserWithEmail:failure", task.exception)
                }
            }
        }
    }

    fun onSignInBtnClicked(email: String, password: String) {
        /** Sign-In Existing Users, when SignIn Btn Clicked */
        if (isValidateForm(email, password)) {

            fireCloud.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    login(false)

                } else {
                    login(true)
                    Log.w("$this", "signInWithEmail:failure", task.exception)
                }
            }
        }

    }

    private fun isValidateForm(email: String, password: String): Boolean {
        var isValidEmail = true
        var isValuedPassword = true

        if (TextUtils.isEmpty(email)) {
            isValidEmail = false
        }

        if (TextUtils.isEmpty(password)) {
            isValuedPassword = false
        }

        fBUserLD.value = LoginState(isEmailValid = isValidEmail, isPasswordValid = isValuedPassword)

        return isValidEmail && isValuedPassword
    }

}
