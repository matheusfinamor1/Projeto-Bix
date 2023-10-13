package com.example.projetobix.presentation.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projetobix.databinding.FragmentHomeBinding
import com.example.projetobix.mock.post
import com.example.projetobix.presentation.main.MainActivityListener

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var mainActivityListener: MainActivityListener? = null

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
        mainActivityListener?.setBottomNavigationViewAlpha(percentage)
    }

    private fun verifyLastItemRecyclerView() {
        val layoutManager = binding.rvPostList.layoutManager as LinearLayoutManager
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && totalItemCount > 0) {
            binding.loaderAnimationFinishList.layoutParams.width = 200
            binding.loaderAnimationFinishList.layoutParams.height = 200
            binding.loaderAnimationFinishList.isVisible = true

        } else binding.loaderAnimationFinishList.isVisible = false
    }

    private fun RecyclerView.bind() {
        adapter = homeAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivityListener) {
            mainActivityListener = context
        } else {
            throw RuntimeException("$context must implement MainActivityListener")
        }
    }
}