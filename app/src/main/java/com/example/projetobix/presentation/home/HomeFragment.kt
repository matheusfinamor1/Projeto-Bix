package com.example.projetobix.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projetobix.R
import com.example.projetobix.databinding.FragmentHomeBinding
import com.example.projetobix.mock.post
import com.example.projetobix.presentation.base.BaseFragmentWithBottomNav
import com.example.projetobix.presentation.base.BottomNavigationViewAlphaListener
import com.example.projetobix.presentation.dialog.ModifyPasswordBottomSheetDialogFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : BaseFragmentWithBottomNav<FragmentHomeBinding>(),
    BottomNavigationViewAlphaListener {

    private var _binding: FragmentHomeBinding? = null
    override val binding: FragmentHomeBinding
        get() {
            return _binding ?: throw IllegalStateException(NOT_BINDING)
        }

    override val menuResID: Int
        get() = R.menu.bottom_navigation_menu

    private val viewModel: HomeViewModel by viewModels()
    private val homeAdapter = HomeAdapter(posts = post)

    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var dialog: ModifyPasswordBottomSheetDialogFragment

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
        bottomNavView = binding.bottomNav.bottomNavigationView
        binding.rvPostList.bind()
        observers()
        setupDestinationNavigationBar()
        setupNavDrawer()
        handlerScrollForBottomNavigation()
        setupOpenDrawer()
    }

    private fun observers() {
        viewModel.apply {
            isLogout.observe(viewLifecycleOwner) {
                if (it) {
                    val directions = HomeFragmentDirections.homeFragmentToLoginFragment()
                    findNavController().navigate(directions)
                }
            }
        }
    }

    private fun setupNavDrawer() {
        binding.navHome.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_alt_email -> {
                    Toast.makeText(context, "Alterar email click", Toast.LENGTH_LONG).show()
                    true
                }

                R.id.nav_alt_pass -> {
                    dialog = ModifyPasswordBottomSheetDialogFragment()
                    dialog.show(this.parentFragmentManager, dialog.tag)
                    true
                }

                R.id.nav_logout -> {
                    viewModel.logout()
                    true
                }

                else -> false
            }
        }
    }

    private fun setupOpenDrawer() {
        binding.toolbarHome.setNavigationIcon(R.drawable.ic_menu)
        binding.toolbarHome.setNavigationOnClickListener {
            binding.drawerLayoutHome.openDrawer(binding.navHome)
        }
    }


    private fun setupDestinationNavigationBar() {
        bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.favoriteFragment -> {
                    Toast.makeText(requireContext(), "Favorite click", Toast.LENGTH_LONG).show()
                    true
                }

                R.id.homeFragment -> {
                    Toast.makeText(requireContext(), "Home click", Toast.LENGTH_LONG).show()
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

    override fun setBottomNavigationViewAlpha(percentage: Float) {
        val alpha = 1 - percentage
        bottomNavView.alpha = alpha.coerceAtLeast(0.5f)
    }

    companion object {
        private const val NOT_BINDING = "Binding is not initialized"
    }
}