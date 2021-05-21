package com.nmrc.note.ui.fragments.notes

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.nmrc.note.R
import com.nmrc.note.databinding.FragmentNewNoteBinding
import com.nmrc.note.data.model.Note
import com.nmrc.note.data.model.util.alertDialog
import com.nmrc.note.data.model.util.loadAnim
import com.nmrc.note.data.model.util.navigate
import com.nmrc.note.viewmodel.NoteSharedViewModel
import com.nmrc.note.viewmodel.NoteSharedViewModel.RecoverNoteData
import com.nmrc.note.viewmodel.ViewModelFactory

class NewNoteFragment : Fragment(R.layout.fragment_new_note) {

    private val binding: FragmentNewNoteBinding by viewBinding()
    private val svm: NoteSharedViewModel by activityViewModels{ ViewModelFactory(requireContext()) }
    private val rotateOpenAnimation by lazy { loadAnim(R.anim.rotate_open_anim) }
    private val rotateCloseAnimation by lazy { loadAnim(R.anim.rotate_close_anim) }
    private val fromBottom by lazy { loadAnim(R.anim.from_bottom_anim) }
    private val toBottom by lazy { loadAnim(R.anim.to_bottom_anim) }
    private val editState by lazy { svm.editNote().value!! }
    private var clicked = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        svm.setBindingNewNote(binding)

        fabListener()
        onEditListener()
        createOrEditNoteListener()
        cancelNewNoteListener()
        addImageListener()
        favoriteListener()
        favoriteObserver()
    }

    private fun dataR() = RecoverNoteData(binding,svm.getStateFavorite().value!!)

    private fun fabListener() {
        with(binding){
            fabAddInfoNote.setOnClickListener {
                onAddBottomInfoClicked()
            }
        }
    }

    private fun onEditListener() {
        if (editState.editable) {
            val note = svm.withNotes { list -> list[editState.position]}
            svm.refillDataNoteEdit(note)
        }
    }

    private fun createOrEditNoteListener() {
        binding.ivSaveNoteDialog.setOnClickListener {
            createOrEditNoteAction(editState.editable) // bug
        }
    }

    private fun cancelNewNoteListener() {
        binding.ivCancelNewNoteDialog.setOnClickListener {
            navigate(R.id.action_backNoteFragment)
            svm.withStates(noEdit = true)
        }
    }

    private fun addImageListener() {
        binding.fabAddInfoNoteImage.setOnClickListener {
            svm.addImageAction()
        }
    }

    private fun favoriteListener() {
        binding.ivFavoriteNoteDialog.setOnClickListener {
            svm.favoriteAction()
        }
    }

    private fun favoriteObserver() {
        svm.getStateFavorite().observe(viewLifecycleOwner) {
            svm.favoriteAction(true)
        }
    }

    private fun createOrEditNoteAction(onEdit: Boolean) {
        if(onEdit){
            if (!editNote(dataR()))
                alertDialog(
                    R.string.warningMessage,
                    R.string.alertEmptyData,
                    R.drawable.ic_warning
                )
        }else{
            if (!createNote(dataR()))
                alertDialog(
                    R.string.warningMessage,
                    R.string.alertEmptyData,
                    R.drawable.ic_warning
                )
        }
    }

    private fun createNote(data: RecoverNoteData): Boolean {
        return if (data.emptyData()) false
        else{
            svm.apply {
                withNotes { list, _ -> list.add(Note(data)) }
                withStates(noEdit = true)
            }
            backNoteFragment()
            true
        }
    }

    private fun editNote(data: RecoverNoteData): Boolean {
        return if (data.emptyData()) false
        else{
            svm.apply {
                withNotes { list, ePosition ->
                    list.apply {
                        removeAt(ePosition)
                        add(ePosition, Note(dataR()))
                    }
                }
                withStates(noEdit = true)
            }
            backNoteFragment()
            true
        }
    }

    private fun backNoteFragment() {
        navigate(R.id.action_backNoteFragment)
        svm.withStates(fav = false)
    }

    private fun onAddBottomInfoClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setAnimation(clicked: Boolean) {
        if(!clicked) with(binding) {
            fabAddInfoNoteImage.startAnimation(fromBottom)
            fabAddInfoNote.startAnimation(rotateOpenAnimation)
        } else with(binding) {
            fabAddInfoNoteImage.startAnimation(toBottom)
            fabAddInfoNote.startAnimation(rotateCloseAnimation)
        }
    }

    private fun setVisibility(clicked: Boolean) {
        if(!clicked) with(binding) { fabAddInfoNoteImage.visibility = View.VISIBLE }
        else with(binding) { fabAddInfoNoteImage.visibility = View.INVISIBLE }
    }
}