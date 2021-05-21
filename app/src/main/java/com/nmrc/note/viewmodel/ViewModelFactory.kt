package com.nmrc.note.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return when {
            modelClass.isAssignableFrom(NoteSharedViewModel::class.java) -> {
                NoteSharedViewModel(context) as T
            }
            modelClass.isAssignableFrom(TaskSharedViewModel::class.java) -> {
                TaskSharedViewModel(context) as T
            }
            else -> throw IllegalArgumentException("ViewModel class no found")
        }
    }

}