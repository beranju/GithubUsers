package com.example.githubuser.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.githubuser.R
import com.example.githubuser.ViewModelFactory
import com.example.githubuser.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        val viewModel: SettingViewModel by viewModels { factory }


        viewModel.user.observe(viewLifecycleOwner) {
            binding?.emptyProfile?.visibility = if (it.name.isEmpty()) View.VISIBLE else View.GONE
            binding?.profileData?.visibility = if (it.name.isEmpty()) View.GONE else View.VISIBLE
            it.let {
                binding?.tvName?.text = it.name
            }
        }

        viewModel.getTheme.observe(viewLifecycleOwner) { dark ->
            if (dark) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding?.switchTheme?.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding?.switchTheme?.isChecked = false
            }
        }
        binding?.btnSetProfile?.setOnClickListener {
            findNavController().navigate(R.id.action_setting_navigation_to_profileFragment)
        }
        binding?.btnUpdate?.setOnClickListener {
            findNavController().navigate(R.id.action_setting_navigation_to_profileFragment)
        }

        binding?.switchTheme?.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setTheme(isChecked)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "SettingsFragment"
    }
}