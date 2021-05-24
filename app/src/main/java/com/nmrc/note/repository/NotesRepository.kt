package com.nmrc.note.repository

import androidx.lifecycle.LiveData
import com.nmrc.note.data.model.Note
import com.nmrc.note.data.model.room.dao.NotesDao

class NotesRepository(private val notesDao: NotesDao) {

    val readAllData: LiveData<MutableList<Note>> = notesDao.readAllData()

    suspend fun addNote(note: Note) = notesDao.addNote(note)

    suspend fun updateNote(note: Note) = notesDao.updateNote(note)

    suspend fun deleteNote(note: Note) = notesDao.deleteNote(note)

    suspend fun deleteAllNotes() = notesDao.deleteAllNotes()
}