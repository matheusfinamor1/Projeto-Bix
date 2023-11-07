package com.example.projetobix.presentation.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.projetobix.R
import com.example.projetobix.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observers()
    }


    private fun observers() {
        viewModel.apply {
            isLogout.observe(this@MainActivity) {
                if (it) {
                    val navHostFragment =
                        supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                    val navController = navHostFragment.navController
                    navController.navigate(R.id.loginFragment)
                }
            }
        }
    }

    private fun setupNavigationDrawer() {
//        toggle = ActionBarDrawerToggle(
//            this,
//            binding.drawerLayout,
//            R.string.title_open,
//            R.string.title_close
//        )
//        binding.drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()
//
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        binding.navView.setNavigationItemSelectedListener {
//            when (it.itemId) {
//                R.id.nav_subitem1 -> {
//                    Toast.makeText(applicationContext, "subitem1 click", Toast.LENGTH_LONG).show()
//                }
//
//                R.id.nav_subitem2 -> {
//                    Toast.makeText(applicationContext, "subitem2 click", Toast.LENGTH_LONG).show()
//                }
//
//                R.id.logout_item -> {
//                    viewModel.logout()
//                }
//            }
//            true
//        }
    }
}