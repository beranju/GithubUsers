package com.example.githubuser.ui.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.githubuser.R
import com.example.githubuser.ViewModelFactory
import com.example.githubuser.core.common.Resource
import com.example.githubuser.core.remote.response.DetailUserResponse
import com.example.githubuser.databinding.FragmentDetailBinding
import com.example.githubuser.ui.adapter.SectionPagerAdapter
import com.example.githubuser.utils.loadImage
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        val viewModel: DetailViewModel by viewModels { factory }

        val login = arguments?.getString(LOGIN)
        viewModel.isExists(login.toString())

        login?.let {
            setupToolbar(it)
            setupTabLayout(it)
            viewModel.fetchDetailUser(login).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Loading -> handleLoading(true)
                    is Resource.Empty -> handleError(getString(R.string.something_wrong))
                    is Resource.Error -> handleError(result.message)
                    is Resource.Success -> {
                        handleSuccess(result.data)
                        binding?.favorite?.setOnClickListener {
                            viewModel.setFavoriteUser(result.data)
                        }
                    }
                }
            }
        }


        viewModel.isFavorite.observe(viewLifecycleOwner) { favorite ->
            if (favorite) {
                binding?.favorite?.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.baseline_star_24
                    )
                )
            } else {
                binding?.favorite?.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.baseline_star_outline_24
                    )
                )
            }
        }

    }

    private fun setupTabLayout(login: String) {
        val sectionPagerAdapter =
            SectionPagerAdapter((activity as AppCompatActivity), login)
        val viewPager = binding?.viewpager as ViewPager2
        viewPager.adapter = sectionPagerAdapter
        val tabs = binding?.tabLayout as TabLayout
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TABTITLE[position])
        }.attach()
    }

    private fun setupToolbar(login: String) {
        binding?.toolbar?.apply {
            title = getString(R.string.detail_toolbar_title, login)
            elevation = 1f
            setNavigationOnClickListener { findNavController().popBackStack() }
        }

    }

    private fun handleSuccess(data: DetailUserResponse) {
        binding?.apply {
            tvUsername.text = data.login
            tvName.text = data.name
            tvFollower.text = data.followers.toString()
            tvFollowing.text = data.following.toString()
            avatar.loadImage(data.avatarUrl)
        }
        binding?.share?.setOnClickListener {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_SUBJECT, "Halo i'm ${data.login}, Let's explore about me")
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Visit ${data.login}'s Github by clicking here ${data.htmlUrl}"
                )
                type = "text/plain"
            }
            val share = Intent.createChooser(intent, null)
            startActivity(share)
        }
        handleLoading(false)
    }

    private fun handleError(message: String) {
        binding?.error?.visibility = View.VISIBLE
        binding?.detailView?.visibility = View.GONE
        binding?.favorite?.isEnabled = false
        handleLoading(false)
        Log.e(TAG, message)

    }

    private fun handleLoading(loading: Boolean) {
        binding?.loading?.visibility = if (loading) View.VISIBLE else View.GONE
        binding?.detailView?.visibility = if (loading) View.GONE else View.VISIBLE
        binding?.favorite?.isEnabled = !loading
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "DetailFragment"
        const val LOGIN = "login"
        private val TABTITLE = intArrayOf(
            R.string.tab1,
            R.string.tab2,
        )
    }
}