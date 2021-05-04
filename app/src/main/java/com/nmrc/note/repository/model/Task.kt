package com.nmrc.note.repository.model

import com.nmrc.note.viewmodel.TaskSharedViewModel

data class Task (

        var title: String,
        var description: String,
        var date: String,
        var priority: Priority,
        var topic: TopicTaks,
        var id: Int = (Math.random()*(10E6)).toInt()
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