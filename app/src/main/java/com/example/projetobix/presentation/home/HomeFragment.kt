package com.example.projetobix.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.projetobix.R
import com.example.projetobix.databinding.FragmentHomeBinding
import com.example.projetobix.mock.post

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeAdapter = HomeAdapter(posts = post)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvPostList.bind()
        binding.navigationBarHome.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_1 -> {
                    Log.d("Response", "onViewCreated: Vai para perfil")
                    true
                }

                R.id.item_2 -> {
                    Log.d("Response", "onViewCreated: Vai para favoritos")
                    true
                }
                else-> false
            }
        }
    }

    private fun RecyclerView.bind() {
        adapter = homeAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}