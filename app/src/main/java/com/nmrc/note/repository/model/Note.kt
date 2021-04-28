package com.nmrc.note.repository.model

data class Note (

        var title: String,
        var date: String,
        var description: String,
        var favorite: Boolean,
        var image: Int
        )