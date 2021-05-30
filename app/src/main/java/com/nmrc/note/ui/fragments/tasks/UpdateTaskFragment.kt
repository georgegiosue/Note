package com.nmrc.note.ui.fragments.tasks

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.nmrc.note.R
import com.nmrc.note.data.model.Task
import com.nmrc.note.data.model.util.alertDialog
import com.nmrc.note.data.model.util.dateRangePicker
import com.nmrc.note.data.model.util.navigate
import com.nmrc.note.data.model.util.task.TaskData
import com.nmrc.note.databinding.FragmentUpdateTaskBinding
import com.nmrc.note.viewmodel.TaskSharedViewModel
import com.nmrc.note.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class UpdateTaskFragment : Fragment(R.layout.fragment_update_task) {

    private val binding: FragmentUpdateTaskBinding by viewBinding()
    private val svm: TaskSharedViewModel by activityViewModels { ViewModelFactory(requireContext()) }
    private val arg by navArgs<UpdateTaskFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refillData(arg.currentTask)

        updateTaskListener()
        cancelUpdateListener()
        datePickerListener()
    }

    private fun dataR() = TaskData(bindingUT = binding)

    private fun refillData(task: Task) {
        with(binding) {
            with(task) {
                etTitleTaskDialog.setText(title)
                tvTitleTaskDialog.setLines(1)
                tvTitleTaskDialog.text = title
                tvTitleTaskDialog.textSize = 16f
                etDescriptionTaskDialog.setText(description)
                tvDateTaskDialog.text = date
                sliderPriorityTaskDialog.value = when (priority.name) {
                    "LOW" -> 0f
                    "MEDIUM" -> 1f
                    "HIGH" -> 2f
                    else -> throw IllegalArgumentException("Priority no found")
                }
                chipGroupTask.check(when (topic.name){
                    "HOME" -> R.id.chipHomeTaskDialog
                    "WORK" -> R.id.chipWorkTaskDialog
                    "OTHER" -> R.id.chipOtherTaskDialog
                    else -> throw IllegalArgumentException("Topic no found")
                })
                swAutoDelete.isChecked = autoDelete
            }
        }
    }

    private fun updateTaskListener() {
        binding.ivSaveTaskDialog.setOnClickListener {
            if (!updateTask()) {
                alertDialog(
                    R.string.warningMessage,
                    R.string.alertEmptyData,
                    R.drawable.ic_warning
                )
            }
        }
    }

    private fun cancelUpdateListener() {
        binding.ivCancelNewTaskDialog.setOnClickListener {
            navigate(R.id.action_updateTaskFragment_to_itemTaskFragment)
        }
    }

    private fun datePickerListener() {
        binding.ibDateNewTaskDialog.setOnClickListener {
            lifecycleScope.launch {
                dateRangePicker { date ->
                    binding.tvDateTaskDialog.text = date
                }
            }
        }
    }

    private fun updateTask(): Boolean {
        val data = dataR()

        return if (data.isEmpty()) false
        else {
            val task = Task(data).also { task ->
                task.id = arg.currentTask.id
            }
            svm.updateTask(task)
            navigate(R.id.action_updateTaskFragment_to_itemTaskFragment)
            true
        }
    }
}