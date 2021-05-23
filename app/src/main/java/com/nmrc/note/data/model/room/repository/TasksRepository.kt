package com.nmrc.note.data.model.room.repository

import androidx.lifecycle.LiveData
import com.nmrc.note.data.model.Task
import com.nmrc.note.data.model.room.dao.TasksDao

class TasksRepository(private val tasksDao: TasksDao) {

    val readAllData: LiveData<MutableList<Task>> = tasksDao.readAllData()

    suspend fun addTask(task: Task) = tasksDao.addTask(task)

    suspend fun updateTask(task: Task) = tasksDao.updateTask(task)

    suspend fun deleteTask(task: Task) = tasksDao.deleteTask(task)

    suspend fun deleteAllTask() = tasksDao.deleteAllTask()

}