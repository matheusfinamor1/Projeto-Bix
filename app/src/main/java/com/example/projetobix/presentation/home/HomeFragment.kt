package com.example.projetobix.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
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
        handlerScrollForBottomNavigation()

        handlerHomeBottomNavigation()
    }

    private fun handlerHomeBottomNavigation() {
        binding.navigationBarHome.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_1 -> {
                    Log.d("Response", "onViewCreated: Vai para feed")
                    true
                }

                R.id.item_2 -> {
                    Log.d("Response", "onViewCreated: Vai para favoritos")
                    true
                }

                else -> false
            }
        }
    }

    private fun handlerScrollForBottomNavigation() {
        binding.rvPostList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                verifyScrollForHandlerBottomNavigation()
                verifyLastItemRecyclerView()
            }
        })
    }

    private fun verifyScrollForHandlerBottomNavigation() {
        val maxScrollY =
            binding.rvPostList.computeVerticalScrollRange() - binding.rvPostList.height
        val percentage =
            binding.rvPostList.computeVerticalScrollOffset().toFloat() / maxScrollY
        setBottomNavigationViewAlpha(percentage)
    }

    private fun verifyLastItemRecyclerView() {
        val layoutManager = binding.rvPostList.layoutManager as LinearLayoutManager
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        binding.loadedData.isVisible =
            visibleItemCount + firstVisibleItemPosition >= totalItemCount && totalItemCount > 0
    }

    private fun setBottomNavigationViewAlpha(percentage: Float) {
        val alpha = 1 - percentage
        binding.navigationBarHome.bottomNavigation.alpha = alpha.coerceAtLeast(0.5f)
    }

    private fun RecyclerView.bind() {
        adapter = homeAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}