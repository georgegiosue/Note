package com.nmrc.note.ui.fragments.notes

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.nmrc.note.R
import com.nmrc.note.data.model.Note
import com.nmrc.note.data.model.util.alertDialog
import com.nmrc.note.data.model.util.navigate
import com.nmrc.note.data.model.util.note.NoteData
import com.nmrc.note.databinding.FragmentNewNoteBinding
import com.nmrc.note.viewmodel.NoteSharedViewModel
import com.nmrc.note.viewmodel.ViewModelFactory

class NewNoteFragment : Fragment(R.layout.fragment_new_note) {

    private val binding: FragmentNewNoteBinding by viewBinding()
    private val svm: NoteSharedViewModel by activityViewModels{ ViewModelFactory(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        svm.isFavorite.value = false

        createNoteListener()
        cancelNoteListener()
        favoriteListener()
    }

    private fun dataR() = NoteData(bindingNN = binding,
                                   isFavorite = svm.isFavorite.value!! )

    private fun createNoteListener() {
        binding.ivSaveNoteDialog.setOnClickListener {
            if (!createNote()) {
                alertDialog(
                    R.string.warningMessage,
                    R.string.alertEmptyData,
                    R.drawable.ic_warning
                )
            }
        }
    }

    private fun cancelNoteListener() {
        binding.ivCancelNewNoteDialog.setOnClickListener {
            navigate(R.id.action_backNoteFragment)
        }
    }

    private fun favoriteListener() {
        binding.ivFavoriteNoteDialog.setOnClickListener {
            svm.favoriteAction(binding)
        }
    }

    private fun createNote(): Boolean {
        val data = dataR()

        return if (data.isEmpty()) false
        else{
            svm.addNote(Note(data))
            navigate(R.id.action_backNoteFragment)
            true
        }
    }
}