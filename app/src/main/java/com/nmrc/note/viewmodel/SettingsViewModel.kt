package com.nmrc.note.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.nmrc.note.R
import com.nmrc.note.databinding.FragmentSettingsBinding
import com.nmrc.note.repository.model.util.SettingsListener


class SettingsViewModel : ViewModel(), SettingsListener {

    private lateinit var binding: FragmentSettingsBinding

    fun setBinding(binding: FragmentSettingsBinding) {
        this.binding = binding
    }

    override fun darkMode(state: Boolean): Boolean {
        Log.d("Dark Mode","Developing")
        return true
    }

    override fun notificationsManager(state: Boolean): Boolean {
        Log.d("Notifications","Developing")
        return true
    }

    override fun energySaving(state: Boolean): Boolean {
        Log.d("EnergySaving","Developing")
        return true
    }

    override fun informationApp() {
        Navigation.findNavController(binding.root).navigate(R.id.action_toInfoAppFragment)
    }

    override fun bugReports() {
        Log.d("BugReports","Developing")
    }

}