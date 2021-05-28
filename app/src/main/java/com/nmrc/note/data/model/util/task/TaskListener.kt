package com.nmrc.note.data.model.util.task

import androidx.navigation.NavController
import com.nmrc.note.data.model.Task

interface TaskListener {

    fun onEditTask(task: Task, nav: NavController)

}