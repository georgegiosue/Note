package com.nmrc.note.data.model.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nmrc.note.data.model.Task

@Dao
interface TasksDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM tasks")
    suspend fun deleteAllTask()

    @Query("SELECT * FROM tasks ORDER BY id ASC")
    fun readAllData(): LiveData<MutableList<Task>>
}