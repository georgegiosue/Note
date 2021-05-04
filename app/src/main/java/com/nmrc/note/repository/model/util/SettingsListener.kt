package com.nmrc.note.repository.model.util

import android.content.Context

interface SettingsListener {

    fun darkMode(state: Boolean): Boolean

    fun notificationsManager(state: Boolean): Boolean

    fun energySaving(state: Boolean): Boolean

    fun informationApp()

    fun bugReports()

}