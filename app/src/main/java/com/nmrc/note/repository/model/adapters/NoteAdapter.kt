package com.nmrc.note.repository.model.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nmrc.note.R
import com.nmrc.note.databinding.ItemNoteBinding
import com.nmrc.note.repository.model.Note
import com.nmrc.note.repository.model.util.NoteDiffUtil
import com.nmrc.note.repository.model.util.NoteListener
import java.util.*

class NoteAdapter(val noteListener: NoteListener) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private var noteList: ArrayList<Note> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).run {
            inflate(R.layout.item_note,parent,false)
        }

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(noteList[position])
    }

    override fun getItemCount(): Int = noteList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {

        private var itemNoteBinding = ItemNoteBinding.bind(view)

        init {
            with(itemNoteBinding) {
                cvNote.setOnClickListener(this@ViewHolder)
                cvNote.setOnLongClickListener(this@ViewHolder)
            }
        }

        fun render(note: Note) {
            with(itemNoteBinding) {
                tvTitleNote.text = note.title
                tvDateNote.text = note.date
                tvDescriptionNote.text = note.description

                if(note.favorite)
                    ivIconFavoriteNote.setImageResource(R.drawable.ic_favorite)

                if (note.image != 0)
                    ivImageNote.setImageResource(note.image)
            }
        }

        override fun onClick(view: View?) {
            noteListener.onNoteClicked(view!!,bindingAdapterPosition,this@NoteAdapter)
            
        }

        override fun onLongClick(view: View?): Boolean {
            return if (view!!.isLongClickable) {
                noteListener.onNoteLongClicked(view,bindingAdapterPosition,this@NoteAdapter)
                true
            }else false

        }

    }

    fun updateNotes(data: ArrayList<Note>) {
        val diffUtil = NoteDiffUtil(noteList,data)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        noteList = data
        diffResults.dispatchUpdatesTo(this)
    }
}