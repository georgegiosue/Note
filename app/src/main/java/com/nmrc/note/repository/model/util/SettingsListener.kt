package com.nmrc.note.repository.model.util

interface SettingsListener {

    fun darkMode(state: Boolean): Boolean

    fun bugReports()
}