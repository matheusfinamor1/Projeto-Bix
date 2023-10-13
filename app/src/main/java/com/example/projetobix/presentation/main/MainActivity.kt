package com.example.projetobix.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.projetobix.R
import com.example.projetobix.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), MainActivityListener {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment)
        setupWithNavController(binding.navigationBarMain.bottomNavigation, navController)

        defineVisibilityBottomNavigation(navController)
    }

    override fun setBottomNavigationViewAlpha(percentage: Float) {
        val alpha = 1 - percentage
        binding.navigationBarMain.bottomNavigation.alpha = alpha.coerceAtLeast(0.5f)
    }

    override fun onBackPressed() {
        handlerBackPressedBottomNavigation()
        super.onBackPressed()
    }

    private fun handlerBackPressedBottomNavigation() {
        val navController = findNavController(R.id.nav_host_fragment)
        if (navController.currentDestination?.id == R.id.homeFragment ||
            navController.currentDestination?.id == R.id.favoriteFragment
        ) {
            finish()
        }
    }

    private fun defineVisibilityBottomNavigation(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment -> {
                    handlerVisibilityBottomNavigation(false)
                }

                R.id.registerFragment -> {
                    handlerVisibilityBottomNavigation(false)
                }

                R.id.resetPasswordFragment -> {
                    handlerVisibilityBottomNavigation(false)
                }

                R.id.favoriteFragment -> {
                    setBottomNavigationViewAlpha(0f)
                    handlerVisibilityBottomNavigation(true)
                }

                R.id.homeFragment -> {
                    setBottomNavigationViewAlpha(0f)
                    handlerVisibilityBottomNavigation(true)
                }
            }
        }
    }

    private fun handlerVisibilityBottomNavigation(isVisible: Boolean) {
        binding.navigationBarMain.bottomNavigation.isVisible = isVisible
    }
}