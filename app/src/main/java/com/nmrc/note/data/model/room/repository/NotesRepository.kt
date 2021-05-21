package com.nmrc.note.data.model.room.repository

import androidx.lifecycle.LiveData
import com.nmrc.note.data.model.Note
import com.nmrc.note.data.model.room.dao.NotesDao

class NotesRepository(private val notesDao: NotesDao) {

    val readAllData: LiveData<List<Note>> = notesDao.readAllData()

    suspend fun addNote(note: Note) {
        notesDao.addNote(note)
    }
}