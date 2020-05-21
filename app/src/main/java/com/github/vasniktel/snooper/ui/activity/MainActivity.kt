package com.github.vasniktel.snooper.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.github.vasniktel.snooper.R
import com.github.vasniktel.snooper.logic.model.toUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val AUTH_REQUEST = 123
private val TAG = MainActivity::class.simpleName

class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        setContentView(R.layout.activity_main)

        loginButton.setOnClickListener { login() }

        if (Firebase.auth.currentUser == null) {
            Log.d(TAG, "Calling login")
            login()
        } else {
            Log.d(TAG, "Already logged in")
            showMainAppLayout()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != AUTH_REQUEST) return

        val response = IdpResponse.fromResultIntent(data)

        if (response == null || response.error != null) {
            Log.w(TAG, "Login is unsuccessful: response: $response")
            showLoginRequestLayout()
            return
        }

        if (response.isNewUser) {
            Log.d(TAG, "new user")
            viewModel.signUp(Firebase.auth.currentUser!!.toUser())
        }

        showMainAppLayout()
    }

    private fun login() {
        val providers = listOf(
            AuthUI.IdpConfig.EmailBuilder().setRequireName(true).build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setAvailableProviders(providers)
                .build(),
            AUTH_REQUEST
        )
    }

    private fun showLoginRequestLayout() {
        mainAppLayout.visibility = View.GONE
        loginRequestLayout.visibility = View.VISIBLE
    }

    private fun showMainAppLayout() {
        loginRequestLayout.visibility = View.GONE
        mainAppLayout.visibility = View.VISIBLE

        findNavController(R.id.navHostFragment).apply {
            setGraph(R.navigation.nav_graph)
            NavigationUI.setupWithNavController(bottomNavigation, this)
        }
    }
}