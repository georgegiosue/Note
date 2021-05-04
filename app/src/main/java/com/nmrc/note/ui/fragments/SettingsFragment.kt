package com.nmrc.note.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.nmrc.note.databinding.FragmentSettingsBinding
import com.nmrc.note.viewmodel.SettingsViewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel: SettingsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingsBinding.inflate(inflater,container,false)
        settingsViewModel.setBinding(binding)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        darkModeListener()
        notificationsListener()
        energySavingListener()
        applicationInfoListener()
        bugsReportsListener()
    }

    private fun darkModeListener() {
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                settingsViewModel.darkMode(true)
            else
                settingsViewModel.darkMode(false)
        }
    }

    private fun notificationsListener() {
        binding.switchPauseNotifications.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                settingsViewModel.notificationsManager(true)
            else
                settingsViewModel.notificationsManager(false)
        }
    }

    private fun energySavingListener() {
        binding.switchEnergySaving.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                settingsViewModel.energySaving(true)
            else
                settingsViewModel.energySaving(false)
        }
    }

    private fun applicationInfoListener() {
        binding.llApplicationInfo.setOnClickListener {
            settingsViewModel.informationApp()
        }
    }

    private fun bugsReportsListener() {
        binding.llReportBugs.setOnClickListener {
            settingsViewModel.bugReports()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }

}