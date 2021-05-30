package com.nmrc.note.data.model.util.task

import com.nmrc.note.data.model.Priority
import com.nmrc.note.data.model.Topic
import com.nmrc.note.databinding.FragmentNewTaskBinding
import com.nmrc.note.databinding.FragmentUpdateTaskBinding

class TaskData(bindingNT: FragmentNewTaskBinding? = null,
               bindingUT: FragmentUpdateTaskBinding? = null) {

    lateinit var title: String
    lateinit var description: String
    lateinit var date: String
    lateinit var priority: Priority
    lateinit var topic: Topic
    var autoDelete: Boolean = false

    init {
        bindingNT?.let { binding ->
            with(binding) {
                title = etTitleTaskDialog.text.toString()
                description = etDescriptionTaskDialog.text.toString()
                date = tvDateTaskDialog.text.toString()
                priority = dataPriority(binding)
                topic = dataTopic(binding)
                autoDelete = swAutoDelete.isChecked
            }
        }

        bindingUT?.let { binding ->
            with(binding) {
                title = etTitleTaskDialog.text.toString()
                description = etDescriptionTaskDialog.text.toString()
                date = tvDateTaskDialog.text.toString()
                priority = dataPriority(binding)
                topic = dataTopic(binding)
                autoDelete = swAutoDelete.isChecked
            }
        }
    }

    private fun dataPriority(binding: FragmentNewTaskBinding): Priority {
        return when (binding.sliderPriorityTaskDialog.value) {
            0f -> Priority.LOW
            1f -> Priority.MEDIUM
            2f -> Priority.HIGH
            else -> throw IllegalArgumentException("Priority no found")
        }
    }

    private fun dataPriority(binding: FragmentUpdateTaskBinding): Priority {
        return when (binding.sliderPriorityTaskDialog.value) {
            0f -> Priority.LOW
            1f -> Priority.MEDIUM
            2f -> Priority.HIGH
            else -> throw IllegalArgumentException("Priority no found")
        }
    }

    private fun dataTopic(binding: FragmentNewTaskBinding): Topic {
        return when(binding.chipGroupTask.checkedChipId) {
            binding.chipHomeTaskDialog.id -> Topic.HOME
            binding.chipWorkTaskDialog.id -> Topic.WORK
            binding.chipOtherTaskDialog.id -> Topic.OTHER
            else -> throw IllegalArgumentException("Topic no found")
        }
    }

    private fun dataTopic(binding: FragmentUpdateTaskBinding): Topic {
        return when(binding.chipGroupTask.checkedChipId) {
            binding.chipHomeTaskDialog.id -> Topic.HOME
            binding.chipWorkTaskDialog.id -> Topic.WORK
            binding.chipOtherTaskDialog.id -> Topic.OTHER
            else -> throw IllegalArgumentException("Topic no found")
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