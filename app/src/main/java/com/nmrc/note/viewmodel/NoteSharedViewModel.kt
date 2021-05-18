package com.nmrc.note.viewmodel

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nmrc.note.R
import com.nmrc.note.databinding.FragmentNewNoteBinding
import com.nmrc.note.databinding.FragmentNoteBinding
import com.nmrc.note.repository.model.Note
import com.nmrc.note.repository.model.adapters.NoteAdapter
import com.nmrc.note.repository.model.util.NoteListener
import com.nmrc.note.repository.model.util.navigate
import com.nmrc.note.repository.model.util.newToast
import com.nmrc.note.repository.model.util.setImg
import java.time.LocalDateTime
import java.util.*

class NoteSharedViewModel : ViewModel(), NoteListener {

    private var _editStateNote = MutableLiveData<StateEditNote>()
    private var _emptyNoteList = MutableLiveData<Boolean>()
    private var isFavorite = MutableLiveData<Boolean>()
    private var _noteList = MutableLiveData<ArrayList<Note>>()
    private lateinit var bindingNewNote: FragmentNewNoteBinding
    private lateinit var bindingNote: FragmentNoteBinding
    private var noteArray: ArrayList<Note> = ArrayList()


    init {
        setNoteList(noteArray)
        withStates(noEdit = true)
    }

    private fun setStateEditNote(stateEditNote: StateEditNote) {
        _editStateNote.value = stateEditNote
    }

    private fun removeNote(position: Int, adapter: NoteAdapter): Boolean {
        _noteList.value?.removeAt(position)
        adapter.notifyDataSetChanged()
        return adapter.itemCount == 0
    }

    private fun setNoteList(noteList: ArrayList<Note>){
        _noteList.value = noteList
    }

    private fun setStateFavorite(state: Boolean) {
        this.isFavorite.value = state
    }

    private fun visibleToolsAndCountNote() {
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

    private fun countNotes() {
        with(bindingNote) {
            withNotes { list, _ ->
                chipCountNotes.text = list.size.toString()
            }
        }
    }

    fun noteList(): LiveData<ArrayList<Note>> = _noteList

    fun editNote(): LiveData<StateEditNote> = _editStateNote

    fun withNotes(list: (ArrayList<Note>) -> Note) = list(_noteList.value!!)

    fun withNotes(arg: (list: ArrayList<Note>, ePosition: Int ) -> Unit) {
        with(_editStateNote.value!!) {
            arg(_noteList.value!!, position)
        }
    }

    fun withStates(emptyList: Boolean? = null,
                   noEdit: Boolean = false,
                   fav: Boolean? = null,
                   count: Boolean = false ) {

        if (noEdit){
            setStateEditNote(StateEditNote(false, 0))
            setStateFavorite(false)
        }

        emptyList?.let { empty ->
            setStateEmptyNoteList(empty)
            if (empty)
                visibleToolsAndCountNote()
        }

        fav?.let { setStateFavorite(it) }

        if (count) {
            countNotes()
            visibleToolsAndCountNote()
        }
    }
    fun setBindingNote(binding: FragmentNoteBinding) {
        this.bindingNote = binding
    }

    fun setBindingNewNote(binding: FragmentNewNoteBinding) {
        this.bindingNewNote = binding
    }

    fun setStateEmptyNoteList(empty: Boolean) {
        _emptyNoteList.value = empty
    }

    fun getStateFavorite(): LiveData<Boolean> = this.isFavorite

    fun getNoteListenerInterface() : NoteListener = this

    fun clearAllNote(adapter: NoteAdapter, context: Context): Boolean {
        return if (_noteList.value.isNullOrEmpty()) {
            newToast(R.string.noNotesForClear, context)
            false
        }
        else {
            _noteList.value!!.clear()
            adapter.notifyDataSetChanged()
            newToast(R.string.clearAllNotes, context)
            true
        }
    }

    fun refillDataNoteEdit(note: Note) {
        with(bindingNewNote) {
            with(note) {
                etTitleNoteDialog.setText(title)
                etDescriptionNoteDialog.setText(description)
                tvTitleNoteDialog.text = title
                this@NoteSharedViewModel.setStateFavorite(favorite)
            }
        }
    }

    fun favoriteAction(observer: Boolean = false) {
        if (!observer)
            setStateFavorite(!isFavorite.value!!)

        with(bindingNewNote) {
            if (isFavorite.value!!)
                ivFavoriteNoteDialog.setImg(R.drawable.ic_favorite)
            else
                ivFavoriteNoteDialog.setImg(R.drawable.ic_favorite_border)
        }
    }

    fun addImageAction() {
        newToast(R.string.developing, bindingNewNote.root.context)
    }

    override fun onNoteClicked(view: View, position: Int, adapter: NoteAdapter) {
        setStateEditNote(StateEditNote(true, position))
        navigate(view,R.id.action_toNewNote)
    }

    override fun onNoteLongClicked(view: View, position: Int, adapter: NoteAdapter) {
        newToast(R.string.developing, view.rootView.context)
    }

    data class StateEditNote (val editable: Boolean, val position: Int)

    class RecoverNoteData(binding: FragmentNewNoteBinding, favoriteState: Boolean) {

        var title: String
        var date: LocalDateTime
        var description: String
        var favorite: Boolean
        var image: Int

        init {
            with(binding) {
                title = etTitleNoteDialog.text.toString()
                date = LocalDateTime.now()
                description = etDescriptionNoteDialog.text.toString()
                favorite = favoriteState
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
}