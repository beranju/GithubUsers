package com.example.githubuser.ui.setting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.githubuser.R
import com.example.githubuser.ViewModelFactory
import com.example.githubuser.databinding.FragmentProfileBinding
import com.example.githubuser.utils.showSnackBar


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        val viewModel: SettingViewModel by viewModels { factory }

        binding?.btnSave?.setOnClickListener {
            val name = binding?.tvName?.text
            viewModel.saveProfile(name = name.toString())
            Log.d(TAG, "$name")
            it.showSnackBar("Yeay, your profile updated")
            findNavController().navigate(R.id.setting_navigation)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "ProfileFragment"
    }
}