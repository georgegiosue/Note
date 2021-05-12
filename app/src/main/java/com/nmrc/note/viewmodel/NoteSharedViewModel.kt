package com.nmrc.note.viewmodel

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nmrc.note.R
import com.nmrc.note.databinding.FragmentNewNoteBinding
import com.nmrc.note.databinding.FragmentNoteBinding
import com.nmrc.note.repository.model.Note
import com.nmrc.note.repository.model.adapters.NoteAdapter
import com.nmrc.note.repository.model.util.NoteListener
import com.nmrc.note.repository.model.util.createToast
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class NoteSharedViewModel : ViewModel(), NoteListener {

    private lateinit var bindingNewNote: FragmentNewNoteBinding
    private lateinit var bindingNote: FragmentNoteBinding
    private var _noteList = MutableLiveData<ArrayList<Note>>()
    private var noteArray: ArrayList<Note> = ArrayList()
    private var _editStateNote = MutableLiveData<StateEditNote>()
    private var _emptyNoteList = MutableLiveData<Boolean>()
    private var isFavorite = MutableLiveData<Boolean>()

    companion object {
        fun notEditNoteState() = StateEditNote(false, 0)
    }

    init {
        setNoteList(noteArray)
        setStateEditNote(notEditNoteState())
        setStateFavorite(false)
    }

    data class StateEditNote (val state: Boolean, val position: Int)

    fun noteList(): LiveData<ArrayList<Note>> = _noteList

    fun editNote(): LiveData<StateEditNote> = _editStateNote

    fun setStateEditNote(stateEditNote: StateEditNote) {
        _editStateNote.value = stateEditNote
    }

    fun setStateEmptyNoteList(empty: Boolean) {
        _emptyNoteList.value = empty
    }

    private fun setNoteList(noteList: ArrayList<Note>){
        _noteList.value = noteList
    }

    fun setStateFavorite(state: Boolean) {
        this.isFavorite.value = state
    }

    fun getStateFavorite(): LiveData<Boolean> = this.isFavorite

    fun getNoteListenerInterface() : NoteListener = this

    fun setBindingNewNote(binding: FragmentNewNoteBinding) {
        this.bindingNewNote = binding
    }

    fun setBindingNote(binding: FragmentNoteBinding) {
        this.bindingNote = binding
    }

    fun addNote(note: Note) = _noteList.value?.add(note)

    private fun removeNote(position: Int, adapter: NoteAdapter): Boolean { // USO CON EL LONG CLICK LISTENER
        _noteList.value?.removeAt(position)
        adapter.notifyDataSetChanged()
        return adapter.itemCount == 0
    }

    fun clearAllNote(adapter: NoteAdapter, context: Context): Boolean {
        return if (_noteList.value.isNullOrEmpty()) {
            Toast.makeText(context, R.string.noNotesForClear, Toast.LENGTH_SHORT).show()
            false
        }
        else {
            _noteList.value!!.clear()
            adapter.notifyDataSetChanged()
            Toast.makeText(context, R.string.clearAllNotes, Toast.LENGTH_SHORT).show()
            true
        }
    }

    fun refillDataNoteEdit(note: Note) {
        with(bindingNewNote) {
            with(note) {
                etTitleNoteDialog.setText(title)
                etDescriptionNoteDialog.setText(description)
                tvTitleNoteDialog.text = title

                if (favorite)
                    ivFavoriteNoteDialog.setImageResource(R.drawable.ic_favorite)

            }
        }
    }

    fun visibleToolsAndCountNote() {
        if (_emptyNoteList.value!!)
            with(bindingNote) {
                chipCountNotes.visibility = View.INVISIBLE
                llToolsNotes.visibility = View.INVISIBLE
                tvPreviewNothingNotesFragment.visibility = View.VISIBLE
            }
        else
            with(bindingNote) {
                chipCountNotes.visibility = View.VISIBLE
                llToolsNotes.visibility = View.VISIBLE
            }
    }

    fun countNotes() {
        val notesAmount = _noteList.value?.size

        with(bindingNote) {
            chipCountNotes.text = notesAmount.toString()
        }
    }

    override fun onNoteClicked(view: View, position: Int, adapter: NoteAdapter) {
        createToast("NOTE CLICKED") { view.rootView.context }
    }

    override fun onNoteLongClicked(view: View, position: Int, adapter: NoteAdapter) {
        createToast("NOTE LONG CLICKED") { view.rootView.context }
    }

    fun favoriteAction(observer: Boolean = false) {

        if (!observer)
            setStateFavorite(!isFavorite.value!!)

        if (isFavorite.value!!)
            bindingNewNote.ivFavoriteNoteDialog.setImageResource(R.drawable.ic_favorite)
        else
            bindingNewNote.ivFavoriteNoteDialog.setImageResource(R.drawable.ic_favorite_border)
    }


    inner class RecoverNoteData(binding: FragmentNewNoteBinding) {

        var title: String
        var date: String
        var description: String
        var favorite: Boolean
        var image: Int

        init {
            with(binding) {
                title = etTitleNoteDialog.text.toString()
                date = LocalDateTime.now().toString()
                description = etDescriptionNoteDialog.text.toString()
                favorite = isFavorite.value!!
                image = 0
            }
        }

        fun emptyData(): Boolean {
            return when{
                title.isEmpty() -> true
                description.isEmpty() -> true
                else -> false
            }
        }
    }

    fun addImageAction() {
        createToast("Developing") { bindingNewNote.root.context }
    }

    fun todayDate(): String {
        val day = LocalDate.now().dayOfMonth
        val month = LocalDate.now().month.run {
            val first = this.toString().substring(0,1)
            val over = this.toString().substring(1).lowercase(Locale.ROOT)
            first+over
        }
        return "$month $day"
    }

}