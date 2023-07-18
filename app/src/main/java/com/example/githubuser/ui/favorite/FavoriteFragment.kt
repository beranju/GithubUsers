package com.example.githubuser.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.ViewModelFactory
import com.example.githubuser.databinding.FragmentFavoriteBinding
import com.example.githubuser.ui.adapter.UserAdapter
import com.example.githubuser.ui.detail.DetailFragment
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        val viewModel: FavoriteViewModel by viewModels { factory }
        val userAdapter = UserAdapter()

        binding?.rvUser?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = userAdapter
        }

        lifecycleScope.launch {
            viewModel.fetchUser().observe(viewLifecycleOwner) {
                userAdapter.submitList(it)
                userAdapter.onCLick = { selectedUser ->
                    val bundle = Bundle()
                    bundle.putString(DetailFragment.LOGIN, selectedUser.login)
                    val options =
                        NavOptions.Builder().setPopUpTo(R.id.home_navigation, false).build()
                    findNavController().navigate(
                        R.id.action_favoriteFragment_to_detailFragment,
                        bundle,
                        options
                    )
                }
            }
        }

        binding?.btnFindMore?.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "FavoriteFragment"
    }
}