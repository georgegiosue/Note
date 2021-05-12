package com.nmrc.note.repository.model

import com.nmrc.note.viewmodel.NoteSharedViewModel

data class Note (

        var title: String,
        var date: String,
        var description: String,
        var favorite: Boolean,
        var image: Int = 0,
        var id: Int = (Math.random()*(10E6)).toInt()
        )
{
        constructor(data: NoteSharedViewModel.RecoverNoteData) : this(
                data.title,
                data.date,
                data.description,
                data.favorite,
                data.image,
        )
}