package com.nmrc.note.data.model.util

import androidx.recyclerview.widget.DiffUtil
import com.nmrc.note.data.model.Note

class NoteDiffUtil(
        private val oldNoteList: ArrayList<Note>,
        private val newNoteList: ArrayList<Note>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldNoteList.size

    override fun getNewListSize(): Int = newNoteList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return  oldNoteList[oldItemPosition].id == newNoteList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldNoteList[oldItemPosition].title == newNoteList[newItemPosition].title -> false
            oldNoteList[oldItemPosition].date == newNoteList[newItemPosition].date -> false
            oldNoteList[oldItemPosition].description == newNoteList[newItemPosition].description -> false
            oldNoteList[oldItemPosition].favorite == newNoteList[newItemPosition].favorite -> false
            oldNoteList[oldItemPosition].image == newNoteList[newItemPosition].image -> false
            oldNoteList[oldItemPosition].id == newNoteList[newItemPosition].id -> false
            else -> true
        }
    }
}