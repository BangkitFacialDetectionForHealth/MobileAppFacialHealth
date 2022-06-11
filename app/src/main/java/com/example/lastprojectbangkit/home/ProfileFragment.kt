package com.example.lastprojectbangkit.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.lastprojectbangkit.R
import com.example.lastprojectbangkit.databinding.ProfileFragmentBinding
import com.example.lastprojectbangkit.setting.SettingViewModel
import com.example.lastprojectbangkit.view.ViewModelFactory

class ProfileFragment : Fragment() {
    private lateinit var factory: ViewModelFactory
    private val viewModel: SettingViewModel by activityViewModels { factory }
    private var _profilebinding: ProfileFragmentBinding? = null
    private val binding get() = _profilebinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _profilebinding = ProfileFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factory = ViewModelFactory.getInstance(requireActivity())

        initObserve()
        initAction()
    }

    private fun initObserve() {

        viewModel.getUserName().observe(viewLifecycleOwner) { username ->
            binding.tvNameProfile.text = username
        }
        viewModel.getUserEmail().observe(viewLifecycleOwner) { email ->
            binding.tvEmailProfile.text = email
        }

    }

    private fun initAction() {
        binding.toolbar.apply {

            inflateMenu(R.menu.nav_setting)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.navigation_setting -> {
                        findNavController().navigate(R.id.action_navigation_profile_to_settingFragment)
                        true

                    }
                    else -> false
                }
            }
        }
    }
}