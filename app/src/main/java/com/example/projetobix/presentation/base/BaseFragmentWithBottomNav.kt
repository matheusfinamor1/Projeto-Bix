package com.example.projetobix.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.projetobix.R
import com.google.android.material.bottomnavigation.BottomNavigationView

abstract class BaseFragmentWithBottomNav<T: ViewBinding?> : Fragment() {

    abstract val binding: T

    abstract val menuResID: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_base_with_bottom_nav, container,false)

        val bottomNavView = rootView.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavView.inflateMenu(menuResID)


        return rootView
    }

}