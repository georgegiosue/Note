package com.nmrc.note.ui.fragments.notes

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.nmrc.note.R
import com.nmrc.note.data.model.Note
import com.nmrc.note.data.model.util.alertDialog
import com.nmrc.note.data.model.util.navigate
import com.nmrc.note.data.model.util.note.NoteData
import com.nmrc.note.databinding.FragmentUpdateNoteBinding
import com.nmrc.note.viewmodel.NoteSharedViewModel
import com.nmrc.note.viewmodel.ViewModelFactory

class UpdateNoteFragment : Fragment(R.layout.fragment_update_note) {

    private val binding: FragmentUpdateNoteBinding by viewBinding()
    private val svm: NoteSharedViewModel by activityViewModels { ViewModelFactory(requireContext()) }
    private val arg by navArgs<UpdateNoteFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        svm.isFavorite.value = false

        refillData(arg.currentNote)
        updateNoteListener()
        cancelUpdateListener()
        favoriteListener()
    }

    private fun dataR() = NoteData(bindingUN = binding,
                                   isFavorite = svm.isFavorite.value!! )

    private fun refillData(note: Note) {
        with(binding) {
            with(note) {
                etTitleNoteDialog.setText(title)
                etDescriptionNoteDialog.setText(description)
                tvTitleNoteDialog.text = title

                if (note.favorite) {
                    ivFavoriteNoteDialog.setImageResource(R.drawable.ic_favorite)
                }
            }
        }
    }

    private fun updateNoteListener() {
        binding.ivSaveNoteDialog.setOnClickListener {
            if (!updateNote()) {
                alertDialog(
                    R.string.warningMessage,
                    R.string.alertEmptyData,
                    R.drawable.ic_warning
                )
            }
        }
    }

    private fun cancelUpdateListener() {
        binding.ivCancelNewNoteDialog.setOnClickListener {
            navigate(R.id.action_updateNoteFragment_to_itemNoteFragment)
        }
    }

    private fun favoriteListener() {
        binding.ivFavoriteNoteDialog.setOnClickListener {
            svm.favoriteAction(binding)
        }
    }

    private fun updateNote(): Boolean {
        val data = dataR()

        return if (data.isEmpty()) false
        else {
            val note = Note(data).also { note ->
                note.id = arg.currentNote.id
            }
            svm.updateNote(note)
            navigate(R.id.action_updateNoteFragment_to_itemNoteFragment)
            true
        }
    }
}