package com.nmrc.note.ui.fragments.tasks

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.nmrc.note.R
import com.nmrc.note.databinding.FragmentNewTaskBinding
import com.nmrc.note.data.model.Task
import com.nmrc.note.data.model.util.DATE_ONLY_MONTH_DAY
import com.nmrc.note.data.model.util.alertDialog
import com.nmrc.note.data.model.util.asFormat
import com.nmrc.note.data.model.util.navigate
import com.nmrc.note.viewmodel.TaskSharedViewModel
import com.nmrc.note.viewmodel.TaskSharedViewModel.RecoverTaskData
import com.nmrc.note.viewmodel.ViewModelFactory
import java.time.LocalDateTime

class NewTaskFragment : Fragment(R.layout.fragment_new_task) {

    private val binding: FragmentNewTaskBinding by viewBinding()
    private val svm: TaskSharedViewModel by activityViewModels{ ViewModelFactory(requireContext()) }
    private val date by lazy { LocalDateTime.now() asFormat DATE_ONLY_MONTH_DAY }
    private val editState by lazy { svm.editTask().value!! }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        svm.setBindingNewTask(binding)

        onEditListener()
        createOrEditTaskListener()
        cancelTaskListener()
        datePickerListener()
        todayDate()
    }

    private fun dataR() = RecoverTaskData(binding)

    private fun backTaskFragment() = navigate(R.id.action_backTaskFragment)

    private fun onEditListener() {
        if (editState.editable){
            val task = svm.withTasks { list -> list[editState.position] }
            svm.refillDataTaskEdit(task)
        }
    }

    private fun createOrEditTaskListener() {
        binding.ivSaveTaskDialog.setOnClickListener{
            createOrEditTaskAction(editState.editable)
        }
    }

    private fun cancelTaskListener() {
        binding.ivCancelNewTaskDialog.setOnClickListener {
            backTaskFragment()
            svm.withStates(noEdit = true)
        }
    }

    private fun datePickerListener() {
        binding.ibDateNewTaskDialog.setOnClickListener {
            svm.dateRangePicker(parentFragmentManager)
        }
    }

    private fun todayDate() {
        with(binding) {
            tvDateTaskDialog.text = date
        }
    }

    private fun createOrEditTaskAction(onEdit: Boolean) {
        if(onEdit){
            if (!editTask(dataR()))
                alertDialog(
                    R.string.warningMessage,
                    R.string.alertEmptyData,
                    R.drawable.ic_warning
                )
        }else{
            if(!createTask(dataR()))
                alertDialog(
                    R.string.warningMessage,
                    R.string.alertEmptyData,
                    R.drawable.ic_warning
                )
        }
    }

    private fun editTask(data: RecoverTaskData): Boolean {
        return if (data.emptyData()) false
        else{
            svm.apply {
                withTasks { list, ePosition ->
                    list.apply {
                        removeAt(ePosition)
                        add(ePosition, Task(dataR()))
                    }
                }
                withStates(noEdit = true)
            }
            backTaskFragment()
            true
        }
    }

    private fun createTask(data: RecoverTaskData) : Boolean {
        return if (data.emptyData()) false
        else{
            svm.apply {
                addTask(Task(data))
                withStates(noEdit = true)
            }
            backTaskFragment()
            true
        }
    }
}