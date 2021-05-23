package com.nmrc.note.data.model.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nmrc.note.R
import com.nmrc.note.databinding.ItemTaskBinding
import com.nmrc.note.data.model.Task
import com.nmrc.note.data.model.util.task.TaskDiffUtil
import com.nmrc.note.data.model.util.task.TaskListener

class TaskAdapter(private val listener: TaskListener) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    private var taskList: MutableList<Task> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).run {
            inflate(R.layout.item_task,parent,false)
        }

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

       val currentTask = taskList[position]

       with(holder.itemTaskBinding) {
           llDoneTask.setOnClickListener { view ->
               listener.onDoneTask(currentTask,view)
           }
           rootViewTask.setOnClickListener {
                listener.onEditTask(currentTask, holder.itemView.findNavController())
           }
       }

       holder.render(currentTask)
    }

    override fun getItemCount() = taskList.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var itemTaskBinding = ItemTaskBinding.bind(view)

        fun render(task: Task) {
            with(itemTaskBinding) {
                tvTitleTask.text = task.title
                tvDescriptionTask.text = task.description
                tvTaskDate.text = task.date
                ivTopicTask.setImageResource(task.topic.drawable!!)
                civPriorityTask.setBackgroundResource(task.priority.drawable!!)
            }
        }
    }

    fun update(data: MutableList<Task>) {
        val diffUtil = TaskDiffUtil(taskList,data)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        taskList = data
        diffResults.dispatchUpdatesTo(this)
    }
}