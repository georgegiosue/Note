package com.nmrc.note.ui.fragments.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.nmrc.note.R
import com.nmrc.note.databinding.FragmentSettingsBinding
import com.nmrc.note.viewmodel.SettingsViewModel

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding: FragmentSettingsBinding by viewBinding()
    private val vm: SettingsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.setBinding(binding)

        darkModeListener()
        applicationInfoListener()
        bugsReportsListener()
    }

    private fun darkModeListener() {
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) vm.darkMode(true)

            else vm.darkMode(false)
        }
    }

    private fun applicationInfoListener() {
        binding.llApplicationInfo.setOnClickListener {
            Intent(Intent.ACTION_VIEW, Uri.parse(SettingsViewModel.LINK_REPOSITORY)).also {
                startActivity(it)
            }
        }
    }

    private fun bugsReportsListener() {
        binding.llReportBugs.setOnClickListener {
            vm.bugReports()
        }
    }
}