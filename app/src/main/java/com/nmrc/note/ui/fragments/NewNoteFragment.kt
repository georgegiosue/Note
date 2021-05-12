package com.nmrc.note.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.nmrc.note.R
import com.nmrc.note.databinding.FragmentNewNoteBinding
import com.nmrc.note.repository.model.Note
import com.nmrc.note.repository.model.Task
import com.nmrc.note.repository.model.util.alertDialog
import com.nmrc.note.viewmodel.NoteSharedViewModel


class NewNoteFragment : Fragment() {

    private var _binding: FragmentNewNoteBinding? = null
    private val binding get() = _binding!!
    private var clicked = false
    private var navController: NavController? = null
    private val rotateOpenAnimation: Animation by lazy { AnimationUtils.loadAnimation(context,R.anim.rotate_open_anim) }
    private val rotateCloseAnimation: Animation by lazy { AnimationUtils.loadAnimation(context,R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(context,R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(context,R.anim.to_bottom_anim) }
    private val noteSharedViewModel: NoteSharedViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNewNoteBinding.inflate(inflater,container,false)

        with(binding){
            fabAddInfoNote.setOnClickListener {
                onAddBottomInfoClicked()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        noteSharedViewModel.setBindingNewNote(binding)

        createOrEditNoteListener()
        addImageListener()
        cancelNewNoteListener()
        favoriteListener()
        onEditNoteRefill()
        favoriteObserver()

    }

    private fun onEditNoteRefill() {
        if (noteSharedViewModel.editNote().value!!.state){
            val note: Note = noteSharedViewModel.noteList().value!![noteSharedViewModel.editNote().value!!.position]
            noteSharedViewModel.refillDataNoteEdit(note)
        }
    }

    private fun createOrEditNoteListener() {
        binding.ivSaveNoteDialog.setOnClickListener {
            createOrEditNoteAction()
        }
    }

    private fun cancelNewNoteListener() {
        binding.ivCancelNewNoteDialog.setOnClickListener {
            navController!!.navigate(R.id.action_backNoteFragment)
            noteSharedViewModel.setStateEditNote(NoteSharedViewModel.notEditNoteState())
        }
    }

    private fun favoriteListener() {
        binding.ivFavoriteNoteDialog.setOnClickListener {
            noteSharedViewModel.favoriteAction()
        }
    }

    private fun addImageListener() {
        binding.fabAddInfoNoteImage.setOnClickListener {
            noteSharedViewModel.addImageAction()
        }
    }

    private fun createOrEditNoteAction() {

        if(noteSharedViewModel.editNote().value!!.state){
            val data = NoteSharedViewModel().RecoverNoteData(binding)
            if (!editNote(data))
                alertDialog(
                    R.string.warningMessage,
                    R.string.alertEmptyData,
                    R.drawable.ic_warning
                )
        }else{
            val data = NoteSharedViewModel().RecoverNoteData(binding)
            if (!createNote(data))
                alertDialog(
                    R.string.warningMessage,
                    R.string.alertEmptyData,
                    R.drawable.ic_warning
                )
            else
                noteSharedViewModel.setStateEditNote(NoteSharedViewModel.notEditNoteState())
        }
    }

    private fun createNote(data: NoteSharedViewModel.RecoverNoteData): Boolean {
        return if (data.emptyData()) false
        else{
            val newNote = Note(data)
            noteSharedViewModel.addNote(newNote)
            backNoteFragment()
            true
        }

    }

    private fun editNote(data: NoteSharedViewModel.RecoverNoteData): Boolean {
        return if (data.emptyData()) false

        else{
            val noteList = noteSharedViewModel.noteList().value
            val position = noteSharedViewModel.editNote().value!!.position
            val noteEdit = Note(NoteSharedViewModel().RecoverNoteData(binding))

            noteList?.apply {
                removeAt(position)
                add(position,noteEdit)
            }

            noteSharedViewModel.setStateEditNote(NoteSharedViewModel.notEditNoteState())
            backNoteFragment()
            true
        }
    }

    private fun backNoteFragment() {
        navController!!.navigate(R.id.action_backNoteFragment)
        noteSharedViewModel.setStateFavorite(false)
    }

    private fun favoriteObserver() {
        noteSharedViewModel.getStateFavorite().observe(viewLifecycleOwner) {
            noteSharedViewModel.favoriteAction(true)
        }
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}