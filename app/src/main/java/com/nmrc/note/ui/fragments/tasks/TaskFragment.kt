package com.nmrc.note.ui.fragments.tasks

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nmrc.note.R
import com.nmrc.note.databinding.FragmentTaskBinding
import com.nmrc.note.data.model.adapters.TaskAdapter
import com.nmrc.note.data.model.util.navigate
import com.nmrc.note.viewmodel.TaskSharedViewModel
import com.nmrc.note.viewmodel.ViewModelFactory


class TaskFragment : Fragment(R.layout.fragment_task) {

    private val binding: FragmentTaskBinding by viewBinding()
    private val svm: TaskSharedViewModel by activityViewModels{ ViewModelFactory(requireContext()) }
    private val taskAdapter by lazy { TaskAdapter(svm.getTaskListenerInterface()) }
    private val layoutRV by lazy { LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        svm.setBindingTask(binding)

        setUpRV()
        observerTaskVM()
        newTaskListener()
        clearAllTaskListener()
    }

    private fun setUpRV() {
        with(binding) {
            rvTaskList.apply {
                layoutManager = layoutRV
                adapter = taskAdapter
            }
        }
    }

    private fun observerTaskVM() {
        svm.taskList().observe(viewLifecycleOwner, { list ->
            taskAdapter.update(list)
            svm.setStateEmptyTaskList(list.isNullOrEmpty())

            with(binding.tvPreviewNothingTaskFragment) {
                visibility = if(list.isNotEmpty()) View.INVISIBLE else View.VISIBLE
            }
            svm.withStates(count = true)
        })
    }

    private fun newTaskListener() {
        binding.fabNewTask.setOnClickListener {
            navigate(R.id.action_toNewTask)
        }
    }

    private fun clearAllTaskListener() {
        binding.chipClearAllTasks.setOnClickListener {
            if(svm.clearAllTask(taskAdapter,requireContext()))
                svm.withStates(emptyList = true)
        }
    }
}