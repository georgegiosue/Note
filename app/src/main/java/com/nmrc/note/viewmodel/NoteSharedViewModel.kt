package com.nmrc.note.viewmodel

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.nmrc.note.R
import com.nmrc.note.data.model.Note
import com.nmrc.note.data.model.room.database.AppDatabase
import com.nmrc.note.data.model.util.note.NoteListener
import com.nmrc.note.databinding.FragmentNewNoteBinding
import com.nmrc.note.databinding.FragmentNoteBinding
import com.nmrc.note.databinding.FragmentUpdateNoteBinding
import com.nmrc.note.repository.NotesRepository
import com.nmrc.note.ui.fragments.notes.NoteFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteSharedViewModel(context: Context) : ViewModel(), NoteListener {

    private val _noteList: LiveData<MutableList<Note>>
    private val repository: NotesRepository
    val isFavorite = MutableLiveData<Boolean>()

    init {
        val noteDao = AppDatabase.getDatabase(context).notesDao()
        repository = NotesRepository(noteDao)
        _noteList = repository.readAllData
        isFavorite.value = false
    }

    fun noteList(): LiveData<MutableList<Note>> = _noteList

    fun addNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNote(note)
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllNotes()
        }
    }

    fun visibleTools(binding: FragmentNoteBinding) {
        if (_noteList.value!!.isEmpty())
            with(binding) {
                chipCountNotes.visibility = View.INVISIBLE
                svSearchNotes.visibility = View.INVISIBLE
                tvPreviewNote.visibility = View.VISIBLE
                ivPreviewNote.visibility = View.VISIBLE
            }
        else
            with(binding) {
                chipCountNotes.visibility = View.VISIBLE
                svSearchNotes.visibility = View.VISIBLE
                tvPreviewNote.visibility = View.INVISIBLE
                ivPreviewNote.visibility = View.INVISIBLE
            }
    }

    fun countNotes(binding: FragmentNoteBinding) {
        with(binding) {
            chipCountNotes.text = _noteList.value!!.size.toString()
        }
    }

    fun getNoteListenerInterface() : NoteListener = this

    fun favoriteAction(binding: FragmentNewNoteBinding) {
        isFavorite.value = !isFavorite.value!!

        with(binding) {
            if (isFavorite.value!!)
                ivFavoriteNoteDialog.setImageResource(R.drawable.ic_favorite)
            else
                ivFavoriteNoteDialog.setImageResource(R.drawable.ic_favorite_border)
        }
    }

    fun favoriteAction(binding: FragmentUpdateNoteBinding) {
        isFavorite.value = !isFavorite.value!!

        with(binding) {
            if (isFavorite.value!!)
                ivFavoriteNoteDialog.setImageResource(R.drawable.ic_favorite)
            else
                ivFavoriteNoteDialog.setImageResource(R.drawable.ic_favorite_border)
        }
    }

    override fun onEditNote(note: Note, nav: NavController) {
        NoteFragmentDirections.actionItemNoteFragmentToUpdateNoteFragment(note).also { action ->
            nav.navigate(action)
        }
    }

    override fun onLongClicked(note: Note) {
        //
    }
}