package com.github.vasniktel.snooper.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.github.vasniktel.snooper.R
import com.github.vasniktel.snooper.util.changeVisibility
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val host = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment

        bottomNavigation.setupWithNavController(host.navController)

        host.navController.addOnDestinationChangedListener { _, destination, _ ->
            changeVisibility(bottomNavigation, destination.id != R.id.loginFragment)
        }
    }
}