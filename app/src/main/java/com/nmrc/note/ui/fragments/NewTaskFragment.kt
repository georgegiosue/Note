package com.nmrc.note.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nmrc.note.R
import com.nmrc.note.databinding.FragmentNewTaskBinding
import com.nmrc.note.repository.model.Task
import com.nmrc.note.viewmodel.TaskSharedViewModel
import com.nmrc.note.viewmodel.TaskSharedViewModel.RecoverTaskData

class NewTaskFragment : Fragment() {

    private var _binding: FragmentNewTaskBinding? = null
    private val binding get() = _binding!!
    private var navController: NavController? = null
    private val taskSharedViewModel: TaskSharedViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNewTaskBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        binding.ivSaveTaskDialog.setOnClickListener{createOrEditTask()}

        todayDate()
        cancelTaskListener()
        datePickerListener()
        editTaskVM()
    }

    private fun todayDate() {
        binding.tvDateTaskDialog.text = taskSharedViewModel.todayDate()
    }

    private fun editTaskVM() {
        if (taskSharedViewModel.editTask().value!!.state){
            val task: Task = taskSharedViewModel.taskList().value!![taskSharedViewModel.editTask().value!!.position]
            taskSharedViewModel.refillDataTaskEdit(binding, task)
        }
    }

    private fun createOrEditTask() {

        if(taskSharedViewModel.editTask().value!!.state){

            val taskList = taskSharedViewModel.taskList().value
            val position = taskSharedViewModel.editTask().value!!.position
            val taskEdit = Task(RecoverTaskData(binding))

            taskList?.apply {
                removeAt(position)
                add(position,taskEdit)
            }

            taskSharedViewModel.setStateEditTask(TaskSharedViewModel.notEditTaskState())
            backTaskFragment()
        }else{
            val data = RecoverTaskData(binding)
            if(!createTask(data))
                alertDialog()
            taskSharedViewModel.setStateEditTask(TaskSharedViewModel.notEditTaskState())
        }
    }

    private fun alertDialog() {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(R.string.warningMessage)
            setMessage(R.string.alertEmptyDataTask)
            setIcon(R.drawable.ic_warning)
            show()
        }
    }

    private fun createTask(data: RecoverTaskData) : Boolean {

        return if (data.emptyData()) false

        else{
            val newTask = Task(data)
            taskSharedViewModel.addTask(newTask)
            backTaskFragment()
            true
        }
    }

    private fun backTaskFragment() {
        findNavController().navigate(R.id.action_backTaskFragment)
    }

    private fun cancelTaskListener() {
        binding.ivCancelNewTaskDialog.setOnClickListener {
            navController!!.navigate(R.id.action_backTaskFragment)
            taskSharedViewModel.setStateEditTask(TaskSharedViewModel.notEditTaskState())
        }
    }

    private fun datePickerListener() {
        binding.ibDateNewTaskDialog.setOnClickListener {
            taskSharedViewModel.dateRangePicker(binding,parentFragmentManager)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}