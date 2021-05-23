package com.nmrc.note.ui.fragments.tasks

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.nmrc.note.R
import com.nmrc.note.data.model.Task
import com.nmrc.note.data.model.util.*
import com.nmrc.note.data.model.util.task.TaskData
import com.nmrc.note.databinding.FragmentNewTaskBinding
import com.nmrc.note.viewmodel.TaskSharedViewModel
import com.nmrc.note.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class NewTaskFragment : Fragment(R.layout.fragment_new_task) {

    private val binding: FragmentNewTaskBinding by viewBinding()
    private val svm: TaskSharedViewModel by activityViewModels{ ViewModelFactory(requireContext()) }
    private val date by lazy { LocalDateTime.now() asFormat DATE_ONLY_MONTH_DAY }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createTaskListener()
        cancelTaskListener()
        datePickerListener()
        todayDate()
    }

    private fun dataR() = TaskData(bindingNT = binding)

    private fun createTaskListener() {
        binding.ivSaveTaskDialog.setOnClickListener{
            if(!createTask()) {
                alertDialog(
                    R.string.warningMessage,
                    R.string.alertEmptyData,
                    R.drawable.ic_warning
                )
            }
        }
    }

    private fun cancelTaskListener() {
        binding.ivCancelNewTaskDialog.setOnClickListener {
            navigate(R.id.action_backTaskFragment)
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

    private fun todayDate() {
        with(binding) {
            tvDateTaskDialog.text = date
        }
    }

    private fun createTask() : Boolean{
        val data = dataR()

        return if (data.isEmpty()) false
        else {
            svm.addTask(Task(data))
            navigate(R.id.action_backTaskFragment)
            true
        }
    }
}