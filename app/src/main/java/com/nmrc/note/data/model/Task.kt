package com.nmrc.note.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nmrc.note.data.model.util.task.TaskData
import kotlinx.parcelize.Parcelize

@Parcelize
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

        @ColumnInfo(name = "autoDelete")
        var autoDelete: Boolean = false,

        @PrimaryKey(autoGenerate = true)
        var id: Int = 0
        ) : Parcelable
{
        constructor(data: TaskData) : this(
                data.title,
                data.description,
                data.date,
                data.priority,
                data.topic,
                data.autoDelete
        )
}