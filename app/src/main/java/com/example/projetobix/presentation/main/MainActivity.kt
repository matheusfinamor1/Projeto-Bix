package com.example.projetobix.presentation.main

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.projetobix.R
import com.example.projetobix.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), MainActivityListener {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = setupNavigationBar()
        setupNavigationDrawer()

        defineVisibilityBottomNavigation(navController)
    }

    private fun setupNavigationDrawer() {
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            R.string.title_open,
            R.string.title_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_subitem1 -> {
                    Toast.makeText(applicationContext, "subitem1 click", Toast.LENGTH_LONG).show()
                }
                R.id.nav_subitem2 -> {
                    Toast.makeText(applicationContext, "subitem2 click", Toast.LENGTH_LONG).show()
                }
                R.id.logout_item -> {
                    Toast.makeText(applicationContext, "sair click", Toast.LENGTH_LONG).show()
                }
            }
            true
        }
    }

    private fun setupNavigationBar(): NavController {
        val navController = findNavController(R.id.nav_host_fragment)
        setupWithNavController(binding.navigationBarMain.bottomNavigation, navController)
        return navController
    }

    override fun setBottomNavigationViewAlpha(percentage: Float) {
        val alpha = 1 - percentage
        binding.navigationBarMain.bottomNavigation.alpha = alpha.coerceAtLeast(0.5f)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)

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