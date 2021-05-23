package com.nmrc.note.data.model.room.repository

import androidx.lifecycle.LiveData
import com.nmrc.note.data.model.Note
import com.nmrc.note.data.model.room.dao.NotesDao

class NotesRepository(private val notesDao: NotesDao) {

    val readAllData: LiveData<List<Note>> = notesDao.readAllData()

    suspend fun addNote(note: Note) = notesDao.addNote(note)

    suspend fun updateNote(note: Note) = notesDao.updateNote(note)

    suspend fun deleteNote(note: Note) = notesDao.deleteNote(note)

    suspend fun deleteAllNotes(note: Note) = notesDao.deleteAllNotes()
}