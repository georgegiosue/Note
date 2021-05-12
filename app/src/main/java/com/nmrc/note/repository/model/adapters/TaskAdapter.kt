package com.nmrc.note.repository.model.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nmrc.note.R
import com.nmrc.note.databinding.ItemTaskBinding
import com.nmrc.note.repository.model.Task
import com.nmrc.note.repository.model.util.TaskDiffUtil
import com.nmrc.note.repository.model.util.TaskListener

class TaskAdapter(val taskListener: TaskListener) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    private var taskList: ArrayList<Task> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).run {
            inflate(R.layout.item_task,parent,false)
        }

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskAdapter.ViewHolder, position: Int) {
       holder.render(taskList[position])
    }

    override fun getItemCount() = taskList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener{

        private var itemTaskBinding = ItemTaskBinding.bind(view)

        init {
            with(itemTaskBinding) {
                llCheckTask.setOnClickListener(this@ViewHolder)
                mcvTask.setOnClickListener(this@ViewHolder)
            }
        }

        fun render(task: Task) {
            with(itemTaskBinding) {
                tvTitleTask.text = task.title
                tvDescriptionTask.text = task.description
                tvTaskDate.text = task.date
                ivTopicTask.setImageResource(task.topic.drawable!!)
                civPriorityTask.setBackgroundResource(task.priority.drawable!!)
            }
        }

        override fun onClick(view: View?) {
            taskListener.onTaskClicked(view!!, bindingAdapterPosition,this@TaskAdapter)
        }
    }

    fun updateTasks(data: ArrayList<Task>) {
        val diffUtil = TaskDiffUtil(taskList,data)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        taskList = data
        diffResults.dispatchUpdatesTo(this)
    }
}