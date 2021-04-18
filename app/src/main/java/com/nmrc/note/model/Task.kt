package com.nmrc.note.model

data class Task (

        var title: String,
        var description: String,
        var date: String,
        var priority: Int,
        var topic: Int,
        var finalized: Boolean
        )