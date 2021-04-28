package com.nmrc.note.repository.model.util

import android.view.View
import com.nmrc.note.repository.model.adapters.TaskAdapter

interface TaskListener {

    fun onTaskClicked(view: View, position: Int, adapter: TaskAdapter)
}