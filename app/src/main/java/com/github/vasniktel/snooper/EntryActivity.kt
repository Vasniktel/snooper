package com.github.vasniktel.snooper

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

class EntryActivity: AppCompatActivity() {
    companion object {
        private const val TAG = "EntryActivity"
        private const val AUTH_REQUEST = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (FirebaseAuth.getInstance().currentUser != null) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
            return
        }

        val providers = listOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            AUTH_REQUEST
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != AUTH_REQUEST) {
            return
        }

        val response = IdpResponse.fromResultIntent(data)

        when {
            resultCode == Activity.RESULT_OK -> {
                val user = FirebaseAuth.getInstance().currentUser
                Log.d(TAG, "Successful login with user: $user")
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
            response == null -> {
                // User has cancelled sign in
                Log.d(TAG, "Login form has been cancelled")
                finish()
            }
            else -> {
                Log.e(TAG, "An error occurred: ${response.error?.localizedMessage ?: ""}")
                finish()
            }
        }
    }
}
