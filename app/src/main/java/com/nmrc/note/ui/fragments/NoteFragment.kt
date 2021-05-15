package com.nmrc.note.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.nmrc.note.R
import com.nmrc.note.databinding.FragmentNoteBinding
import com.nmrc.note.repository.model.adapters.NoteAdapter
import com.nmrc.note.viewmodel.NoteSharedViewModel

class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private var navController: NavController? = null
    private lateinit var noteAdapter: NoteAdapter
    private val noteSharedViewModel: NoteSharedViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNoteBinding.inflate(inflater,container,false)
        noteAdapter = NoteAdapter(noteSharedViewModel.getNoteListenerInterface())

        with(binding) {
            rvNotesList.apply {
                layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
                adapter = noteAdapter
            }
        }

        observerNoteVM()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        noteSharedViewModel.setBindingNote(binding)

        newNoteListener()
        clearAllNotesListener()
    }

    private fun observerNoteVM() {
        noteSharedViewModel.noteList().observe(viewLifecycleOwner) {
            noteAdapter.updateNotes(it)
            noteSharedViewModel.setStateEmptyNoteList(it.isNullOrEmpty())

            with(binding.tvPreviewNothingNotesFragment) {
                visibility = if(it.isNotEmpty()) View.INVISIBLE else View.VISIBLE
            }

            with(noteSharedViewModel) {
                countNotes()
                visibleToolsAndCountNote()
            }
        }
    }

    private fun newNoteListener() {
        binding.fabNewNote.setOnClickListener {
            navController!!.navigate(R.id.action_toNewNote)
        }
    }

    private fun clearAllNotesListener() {
        binding.chipClearAllNotes.setOnClickListener {
            if(noteSharedViewModel.clearAllNote(noteAdapter,requireContext())){
                noteSharedViewModel.setStateEmptyNoteList(true)
                noteSharedViewModel.visibleToolsAndCountNote()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}