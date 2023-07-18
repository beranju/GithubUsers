package com.example.githubuser.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.ViewModelFactory
import com.example.githubuser.core.common.Resource
import com.example.githubuser.core.remote.response.UserResponseItem
import com.example.githubuser.databinding.FragmentHomeBinding
import com.example.githubuser.ui.adapter.UserAdapter
import com.example.githubuser.ui.detail.DetailFragment
import com.example.githubuser.utils.showSnackBar


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private lateinit var userAdapter: UserAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        val viewModel: HomeViewModel by viewModels { factory }

        binding?.toolbar?.elevation = 1f

        userAdapter = UserAdapter()
        setupRv()

        binding?.settings?.setOnClickListener {
            it.showSnackBar("Coming soon!")
        }

        viewModel.result.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> handleLoading(true)
                is Resource.Empty -> handleEmpty(true)
                is Resource.Success -> handleSuccess(result.data)
                is Resource.Error -> handleError(result.message)
            }
        }


        binding?.svSearch?.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding?.searchresultdesc?.apply {
                    visibility = View.VISIBLE
                    text = context.getString(R.string.search_result_desc, v.text)
                }
                viewModel.fetchUserByLogin(v.text.toString())
                    .observe(viewLifecycleOwner) { result ->
                        when (result) {
                            is Resource.Loading -> handleLoading(true)
                            is Resource.Empty -> handleEmpty(true)
                            is Resource.Success -> handleSuccess(result.data)
                            is Resource.Error -> handleError(result.message)
                        }
                    }
                true
            }
            false
        }

    }

    private fun handleSuccess(data: List<UserResponseItem>) {
        handleLoading(false)
        handleEmpty(data.isEmpty())
        userAdapter.submitList(data)
        userAdapter.onCLick = { selected ->
            val bundle = Bundle()
            bundle.putString(DetailFragment.LOGIN, selected.login)
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
        }
    }


    private fun handleLoading(loading: Boolean) {
        binding?.shimmerLoading?.visibility = if (loading) View.VISIBLE else View.GONE
        if (loading){
            binding?.shimmerLoading?.startShimmer()
        }else{
            binding?.shimmerLoading?.stopShimmer()
            binding?.svSearch?.clearFocus()
            binding?.svSearch?.setText("")
        }
        userAdapter.submitList(null)
        Log.d(TAG, "Loading")
    }

    private fun handleEmpty(empty: Boolean) {
        handleLoading(false)
        binding?.empty?.visibility = if (empty) View.VISIBLE else View.GONE
        binding?.tvText?.text = getString(R.string.nothing_found)
        userAdapter.submitList(null)
    }

    private fun handleError(message: String) {
        handleLoading(false)
        binding?.error?.visibility = View.VISIBLE
        binding?.tvError?.text = getString(R.string.something_wrong)
        userAdapter.submitList(null)
        Log.e(TAG, message)
    }


    private fun setupRv() {
        binding?.rvUser?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = userAdapter
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "HomeFragment"
    }
}