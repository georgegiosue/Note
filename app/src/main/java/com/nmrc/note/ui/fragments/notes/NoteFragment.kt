package com.nmrc.note.ui.fragments.notes

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.nmrc.note.R
import com.nmrc.note.databinding.FragmentNoteBinding
import com.nmrc.note.repository.model.adapters.NoteAdapter
import com.nmrc.note.repository.model.util.navigate
import com.nmrc.note.viewmodel.NoteSharedViewModel

class NoteFragment : Fragment(R.layout.fragment_note) {

    private val binding: FragmentNoteBinding by viewBinding()
    private val svm: NoteSharedViewModel by activityViewModels()
    private val noteAdapter: NoteAdapter by lazy { NoteAdapter(svm.getNoteListenerInterface()) }
    private val layoutRV by lazy { StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        svm.setBindingNote(binding)

        setUpRV()
        observerNoteVM()
        newNoteListener()
        clearAllNotesListener()
    }

    private fun setUpRV() {
        with(binding) {
            rvNotesList.apply {
                layoutManager = layoutRV
                adapter = noteAdapter
            }
        }
    }

    private fun observerNoteVM() {
        svm.noteList().observe(viewLifecycleOwner) { list ->
            noteAdapter.update(list)
            svm.setStateEmptyNoteList(list.isNullOrEmpty())

            with(binding.tvPreviewNothingNotesFragment) {
                visibility = if(list.isNotEmpty()) View.INVISIBLE else View.VISIBLE
            }
            svm.withStates(count = true)
        }
    }

    private fun newNoteListener() {
        binding.fabNewNote.setOnClickListener {
            navigate(R.id.action_toNewNote)
        }
    }

    private fun clearAllNotesListener() {
        binding.chipClearAllNotes.setOnClickListener {
            if(svm.clearAllNote(noteAdapter,requireContext()))
                svm.withStates(emptyList = true)
        }
    }
}