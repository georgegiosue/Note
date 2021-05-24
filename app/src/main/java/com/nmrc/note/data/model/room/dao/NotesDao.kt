package com.nmrc.note.data.model.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nmrc.note.data.model.Note

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes()

    @Query("SELECT * FROM notes ORDER BY id ASC")
    fun readAllData(): LiveData<MutableList<Note>>

}