package com.nmrc.note.repository.model.util

import android.view.View
import com.nmrc.note.repository.model.adapters.NoteAdapter


interface NoteListener {

    fun onNoteClicked(view: View, position: Int, adapter: NoteAdapter)

    fun onNoteLongClicked(view: View, position: Int, adapter: NoteAdapter)
}