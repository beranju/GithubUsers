package com.example.githubuser.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.ViewModelFactory
import com.example.githubuser.core.common.Resource
import com.example.githubuser.core.remote.response.UserResponseItem
import com.example.githubuser.databinding.FragmentTabFollowBinding
import com.example.githubuser.ui.adapter.UserAdapter


class TabFollowFragment : Fragment() {
    private var _binding: FragmentTabFollowBinding? = null
    private val binding get() = _binding
    private val followAdapter = UserAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        val viewModel: DetailViewModel by viewModels { factory }

        val tabName = arguments?.getString(TAB_SECTION_NUMBER)
        val login = arguments?.getString(LOGIN)
        when (tabName) {
            TAB_FOLLOWING -> {
                binding?.tvDescription?.text = getString(R.string.following_text_desc, login)
                viewModel.fetchFollow(login.toString(), getString(R.string.tab_following_text))
                    .observe(viewLifecycleOwner) { result ->
                        when (result) {
                            is Resource.Loading -> handleLoading(true)
                            is Resource.Empty -> handleEmpty()
                            is Resource.Error -> handleError()
                            is Resource.Success -> handleSuccess(result.data)
                        }
                    }

            }
            TAB_FOLLOWER -> {
                binding?.tvDescription?.text = getString(R.string.follower_text_desc, login)
                viewModel.fetchFollow(login.toString(), getString(R.string.tab_followers_text))
                    .observe(viewLifecycleOwner) { result ->
                        when (result) {
                            is Resource.Loading -> handleLoading(true)
                            is Resource.Empty -> handleEmpty()
                            is Resource.Error -> handleError()
                            is Resource.Success -> handleSuccess(result.data)
                        }
                    }
            }
        }

        binding?.rvFollow?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = followAdapter
        }
    }

    private fun handleError() {
        handleLoading(false)
        binding?.error?.visibility = View.VISIBLE
        binding?.content?.visibility = View.GONE
        binding?.tvError?.text = getString(R.string.something_wrong)
    }

    private fun handleEmpty() {
        handleLoading(false)
        binding?.empty?.visibility = View.VISIBLE
        binding?.content?.visibility = View.GONE
        binding?.tvText?.text = getString(R.string.empty_follow)
    }

    private fun handleLoading(loading: Boolean) {
        binding?.loading?.visibility = if (loading) View.VISIBLE else View.GONE
        if (loading) {
            followAdapter.submitList(null)
            binding?.loading?.startShimmer()
        } else {
            binding?.loading?.stopShimmer()
        }
    }

    private fun handleSuccess(data: List<UserResponseItem>) {
        handleLoading(false)
        followAdapter.submitList(data)
        followAdapter.onCLick = { selected ->
            val bundle = Bundle()
            bundle.putString(DetailFragment.LOGIN, selected.login)
            val options = NavOptions.Builder().setPopUpTo(R.id.home_navigation, false).build()
            findNavController().navigate(R.id.detailFragment, bundle, options)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTabFollowBinding.inflate(layoutInflater)
        return binding?.root
    }

    companion object {
        private const val TAG = "TabFollowFragment"
        const val TAB_SECTION_NUMBER = "1"
        const val TAB_FOLLOWER = "Follower"
        const val TAB_FOLLOWING = "Following"
        const val LOGIN = "login"
    }
}