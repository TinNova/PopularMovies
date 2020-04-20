package com.tin.popularmovies.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.tin.popularmovies.*
import com.tin.popularmovies.ui.home.HomeActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory<LoginViewModel>
    private lateinit var viewModel: LoginViewModel

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        AndroidInjection.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        viewModel.onInitView()
        observeViewState()
    }

    private fun observeViewState() {
        viewModel.fBUserLD.observe(this, Observer<LoginState> {
            it?.let {
                when (it.isLoggedIn) {
                    true -> navigateToHomeActivity(it.currentUser)
                    false -> setupListeners()
                }
                when (it.isEmailValid) {
                    true -> email_edit_text.error = null
                    false -> email_edit_text.error = "Required"
                }
                when (it.isPasswordValid) {
                    true -> password_edit_text.error = null
                    false -> password_edit_text.error = "Required"
                }
                when (it.isAuthError) {
                    true -> this.showToast(getString(R.string.auth_error))
                }
            }
        })
    }

    private fun setupListeners() {

        register_btn.setOnClickListener {
            viewModel.onRegisterBtnClicked(
                email_edit_text.text.toString(),
                password_edit_text.text.toString()
            )
        }

        login_btn.setOnClickListener {
            viewModel.onSignInBtnClicked(
                email_edit_text.text.toString(),
                password_edit_text.text.toString()
            )
        }

        login_link.setOnClickListener {
            if (register_btn.visibility == View.VISIBLE) {
                register_btn.gone()
                login_btn.visible()
                login_link.text = getString(R.string.register)
            } else {
                register_btn.visible()
                login_btn.gone()
                login_link.text = getString(R.string.login)
            }
        }

        skip.setOnClickListener {
            navigateToHomeActivity(null)
        }
    }

    private fun navigateToHomeActivity(currentUser: FirebaseUser?) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra(CURRENT_USER, currentUser)
        startActivity(intent)
    }

    companion object {
        const val CURRENT_USER = "current_user"
    }
}
