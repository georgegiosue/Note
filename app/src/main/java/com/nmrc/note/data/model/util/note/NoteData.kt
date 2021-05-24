package com.nmrc.note.data.model.util.note

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.nmrc.note.R
import com.nmrc.note.databinding.FragmentNewNoteBinding
import com.nmrc.note.databinding.FragmentUpdateNoteBinding
import java.time.LocalDateTime

class NoteData(bindingNN: FragmentNewNoteBinding? = null,
               bindingUN: FragmentUpdateNoteBinding? = null,
               isFavorite: Boolean ) {

    lateinit var title: String
    lateinit var date: LocalDateTime
    lateinit var description: String
    var favorite: Boolean = true
    var image: String? = null

    init {
        bindingNN?.let { binding ->
            with(binding) {
                title = etTitleNoteDialog.text.toString()
                date = LocalDateTime.now()
                description = etDescriptionNoteDialog.text.toString()
                favorite = isFavorite
                image = null
            }
        }

        bindingUN?.let { binding ->
            with(binding) {
                title = etTitleNoteDialog.text.toString()
                date = LocalDateTime.now()
                description = etDescriptionNoteDialog.text.toString()
                favorite = isFavorite
                image = null
            }
        }
    }

    fun isEmpty(): Boolean {
        return when {
            title.isEmpty() -> true
            description.isEmpty() -> true
            else -> false
        }
    }
}