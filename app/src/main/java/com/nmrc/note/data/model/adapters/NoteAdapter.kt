package com.nmrc.note.data.model.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nmrc.note.R
import com.nmrc.note.data.model.Note
import com.nmrc.note.data.model.util.DATE_COMPLETED
import com.nmrc.note.data.model.util.asFormat
import com.nmrc.note.data.model.util.note.NoteDiffUtil
import com.nmrc.note.data.model.util.note.NoteListener
import com.nmrc.note.databinding.ItemNoteBinding
import java.util.*

class NoteAdapter(private val listener: NoteListener,
                  private val nav: NavController ) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private var noteList: MutableList<Note> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).run {
            inflate(R.layout.item_note,parent,false)
        }

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentNote = noteList[position]

        holder.itemNoteBinding.cvNote.apply {
            setOnClickListener {
                listener.onEditNote(currentNote, nav)
            }

            setOnLongClickListener { card ->
                return@setOnLongClickListener if (card.isLongClickable) {
                    listener.onLongClicked(currentNote)
                    true
                } else false
            }
        }

        holder.render(currentNote)
    }

    override fun getItemCount(): Int = noteList.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var itemNoteBinding = ItemNoteBinding.bind(view)

        fun render(note: Note) {
            with(itemNoteBinding) {
                tvTitleNote.text = note.title
                tvDateNote.text = note.date asFormat DATE_COMPLETED
                tvDescriptionNote.text = note.description

                if(note.favorite)
                    ivIconFavoriteNote.setImageResource(R.drawable.ic_favorite)

                /*if (!note.image.isNullOrEmpty())
                    ivImageNote.setImageResource(note.image)*/
            }
        }
    }

    fun update(data: MutableList<Note>) {
        val diffUtil = NoteDiffUtil(noteList,data)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        noteList = data
        diffResults.dispatchUpdatesTo(this)
    }
}