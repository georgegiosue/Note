package com.nmrc.note.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nmrc.note.viewmodel.TaskSharedViewModel

@Entity(tableName = "tasks")
data class Task (

        @ColumnInfo(name = "title")
        var title: String,

        @ColumnInfo(name = "description")
        var description: String,

        @ColumnInfo(name = "date")
        var date: String,

        @ColumnInfo(name = "priority")
        var priority: Priority,

        @ColumnInfo(name = "topic")
        var topic: Topic,

        @PrimaryKey(autoGenerate = true)
        val id: Int = 0
        )
{
        constructor(data: TaskSharedViewModel.RecoverTaskData) : this(
                data.title,
                data.description,
                data.date,
                data.priority,
                data.topic
        )
}