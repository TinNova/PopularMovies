package com.tin.popularmovies.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.tin.popularmovies.R
import com.tin.popularmovies.gone
import com.tin.popularmovies.ui.home.HomeActivity
import com.tin.popularmovies.visible
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        AndroidInjection.inject(this)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            navigateToHomeActivity(auth.currentUser)
        } else {

            register_btn.setOnClickListener {
                createAccount(
                    email_edit_text.text.toString(), password_edit_text.text.toString()
                )
            }

            login_btn.setOnClickListener {
                signIn(
                    email_edit_text.text.toString(), password_edit_text.text.toString()
                )
            }

            login_link.setOnClickListener {
                if (register_btn.visibility == View.VISIBLE) {
                    register_btn.gone()
                    login_btn.visible()
                    login_link.text = "Register"
                } else {
                    register_btn.visible()
                    login_btn.gone()
                    login_link.text = "Login"
                }
            }

            skip.setOnClickListener {
                navigateToHomeActivity(null)
            }
        }
    }

    /** Register New Users When Create Account Btn Clicked */
    private fun createAccount(email: String, password: String) {
        if (validateForm(email, password)) {

//            showProgressBar()

            // Register new users
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Register & Sign-in Successful
                        navigateToHomeActivity(auth.currentUser)
                    } else {
                        Log.w("$this", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        // It can error because of a weak password (under 6 characters)
                    }

//                    hideProgressBar()
                }
        }
    }

    /** Sign-In Existing Users, when SignIn Btn Clicked */
    private fun signIn(email: String, password: String) {
        if (validateForm(email, password)) {

//            showProgressBar()

            // Sign-in existing users
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign-in Successful
                        navigateToHomeActivity(auth.currentUser)
                    } else {
                        Log.w("$this", "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }

//                    hideProgressBar()
                }
        }
    }

    private fun navigateToHomeActivity(currentUser: FirebaseUser?) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra(CURRENT_USER, currentUser)
        startActivity(intent)
    }


    private fun validateForm(email: String, password: String): Boolean {
        var valid = true

        if (TextUtils.isEmpty(email)) {
            email_edit_text.error = "Required."
            valid = false
        } else {
            email_edit_text.error = null
        }

        if (TextUtils.isEmpty(password)) {
            password_edit_text.error = "Required."
            valid = false
        } else {
            password_edit_text.error = null
        }

        return valid
    }

    companion object {
        const val CURRENT_USER = "current_user"
    }
}