package com.github.vasniktel.snooper.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.github.vasniktel.snooper.R
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val AUTH_REQUEST = 123
private val TAG = LoginFragment::class.simpleName

class LoginFragment : Fragment(), LoginViewStateCallback {
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        loginButton.setOnClickListener { onNotAuthorized() }

        viewModel.viewState.observe(viewLifecycleOwner) {
            Log.d(TAG, "Got state: $it")
            it.applyCallback(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != AUTH_REQUEST) return

        viewModel.onEvent(AuthorizationComplete(IdpResponse.fromResultIntent(data)))
    }

    override fun onNotAuthorized() {
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

    override fun onAuthorized() {
        findNavController().navigate(
            LoginFragmentDirections.actionLoginFragmentToFeedFragment()
        )
    }

    override fun onAuthorizationError(message: String, throwable: Throwable?) {
        // do nothing
    }
}