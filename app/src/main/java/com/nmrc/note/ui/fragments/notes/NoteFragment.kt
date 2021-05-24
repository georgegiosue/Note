package com.nmrc.note.ui.fragments.notes

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.nmrc.note.R
import com.nmrc.note.databinding.FragmentNoteBinding
import com.nmrc.note.data.model.adapters.NoteAdapter
import com.nmrc.note.data.model.util.navigate
import com.nmrc.note.data.model.util.newToast
import com.nmrc.note.viewmodel.NoteSharedViewModel
import com.nmrc.note.viewmodel.ViewModelFactory

class NoteFragment : Fragment(R.layout.fragment_note) {

    private val binding: FragmentNoteBinding by viewBinding()
    private val svm: NoteSharedViewModel by activityViewModels{ ViewModelFactory(requireContext()) }
    private val noteAdapter: NoteAdapter by lazy { NoteAdapter(svm.getNoteListenerInterface(), findNavController()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRV()
        observerNoteVM()
        newNoteListener()
        deleteAllNotesListener()
    }

    private fun setUpRV() {
        with(binding) {
            rvNotesList.apply {
                layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
                adapter = noteAdapter
            }
        }
    }

    private fun observerNoteVM() {
        svm.noteList().observe(viewLifecycleOwner) { list ->
            noteAdapter.update(list)

            with(binding.tvPreviewNothingNotesFragment) {
                visibility = if(list.isNotEmpty()) View.INVISIBLE else View.VISIBLE
            }
            svm.apply {
                countNotes(binding)
                visibleTools(binding)
            }
        }
    }

    private fun newNoteListener() {
        binding.fabNewNote.setOnClickListener {
            navigate(R.id.action_toNewNote)
        }
    }

    private fun deleteAllNotesListener() {
        binding.chipClearAllNotes.setOnClickListener {
            svm.deleteAllNotes()
            newToast(R.string.deleteAllNotes, requireContext())
        }
    }
}