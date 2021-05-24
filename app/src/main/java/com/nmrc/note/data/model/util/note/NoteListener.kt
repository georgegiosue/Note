package com.nmrc.note.data.model.util.note

import androidx.navigation.NavController
import com.nmrc.note.data.model.Note

interface NoteListener {

    fun onEditNote(note: Note, nav: NavController)

    fun onLongClicked(note: Note)

}