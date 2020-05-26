package com.github.vasniktel.snooper.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.github.vasniktel.snooper.R
import com.google.android.libraries.maps.MapsInitializer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MapsInitializer.initialize(applicationContext)

        val host = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment

        host.navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNavigation.isVisible = destination.id != R.id.loginFragment
        }

        with(bottomNavigation) {
            setupWithNavController(host.navController)
            setOnNavigationItemReselectedListener {
                // ignore reselected menu item
            }
        }
    }
}