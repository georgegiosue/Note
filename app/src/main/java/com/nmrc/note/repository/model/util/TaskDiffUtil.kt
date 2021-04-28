package com.nmrc.note.repository.model.util

import androidx.recyclerview.widget.DiffUtil
import com.nmrc.note.repository.model.Task

class TaskDiffUtil(
        private val oldTaskList: ArrayList<Task>,
        private val newTaskList: ArrayList<Task>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldTaskList.size

    override fun getNewListSize(): Int = newTaskList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTaskList[oldItemPosition].id == newTaskList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldTaskList[oldItemPosition].title != newTaskList[newItemPosition].title -> false
            oldTaskList[oldItemPosition].description != newTaskList[newItemPosition].description -> false
            oldTaskList[oldItemPosition].date != newTaskList[newItemPosition].date -> false
            oldTaskList[oldItemPosition].priority != newTaskList[newItemPosition].priority -> false
            oldTaskList[oldItemPosition].topic != newTaskList[newItemPosition].topic -> false
            oldTaskList[oldItemPosition].id != newTaskList[newItemPosition].id -> false
            else -> true
        }
    }
}