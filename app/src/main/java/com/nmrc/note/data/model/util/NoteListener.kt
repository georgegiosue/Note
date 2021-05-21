package com.nmrc.note.data.model.util

import android.view.View
import com.nmrc.note.data.model.adapters.NoteAdapter


interface NoteListener {

    fun onNoteClicked(view: View, position: Int, adapter: NoteAdapter)

    fun onNoteLongClicked(view: View, position: Int, adapter: NoteAdapter)
}