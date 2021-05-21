package com.nmrc.note.data.model.util

interface SettingsListener {

    fun darkMode(state: Boolean): Boolean

    fun bugReports()
}