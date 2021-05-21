package com.nmrc.note.data.model.util

import android.view.View
import com.nmrc.note.data.model.adapters.TaskAdapter

interface TaskListener {

    fun onTaskClicked(view: View, position: Int, adapter: TaskAdapter)
}