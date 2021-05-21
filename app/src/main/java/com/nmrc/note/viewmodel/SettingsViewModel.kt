package com.nmrc.note.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.nmrc.note.databinding.FragmentSettingsBinding
import com.nmrc.note.data.model.util.SettingsListener


class SettingsViewModel : ViewModel(), SettingsListener {

    companion object {
        const val LINK_REPOSITORY = "https://github.com/16george/note"
    }

    private lateinit var binding: FragmentSettingsBinding

    fun setBinding(binding: FragmentSettingsBinding) {
        this.binding = binding
    }

    override fun darkMode(state: Boolean): Boolean {
        Log.d("Dark Mode","Developing")
        return true
    }

    override fun bugReports() {
        Log.d("BugReports","Developing")
    }
}