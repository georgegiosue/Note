package com.nmrc.note.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nmrc.note.R
import com.nmrc.note.databinding.FragmentTaskBinding
import com.nmrc.note.repository.model.adapters.TaskAdapter
import com.nmrc.note.repository.model.util.navigate
import com.nmrc.note.viewmodel.TaskSharedViewModel


class TaskFragment : Fragment() {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!
    private val taskAdapter by lazy { TaskAdapter(taskSharedViewModel.getTaskListenerInterface()) }
    private val layoutRV by lazy { LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false) }
    private val taskSharedViewModel: TaskSharedViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTaskBinding.inflate(inflater,container,false)

        with(binding) {
            rvTaskList.apply {
                layoutManager = layoutRV
                adapter = taskAdapter
            }
        }

        observerTaskVM()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newTaskListener()
        clearAllTaskListener()
    }

    private fun newTaskListener() {
        binding.fabNewTask.setOnClickListener {
            navigate(R.id.action_toNewTask)
        }
    }

    private fun observerTaskVM() {
        taskSharedViewModel.taskList().observe(viewLifecycleOwner, {
            taskAdapter.updateTasks(it)
            taskSharedViewModel.setStateEmptyTaskList(it.isNullOrEmpty())

            with(binding.tvPreviewNothingTaskFragment) {
                visibility = if(it.isNotEmpty()) View.INVISIBLE else View.VISIBLE
            }

            with(taskSharedViewModel) {
                countTask(binding)
                visibleToolsAndCountTask(binding)
            }
        })

    }

    private fun clearAllTaskListener() {
        binding.chipClearAllTasks.setOnClickListener {
            if(taskSharedViewModel.clearAllTask(taskAdapter,requireContext())){
                with(taskSharedViewModel) {
                    setStateEmptyTaskList(true)
                    visibleToolsAndCountTask(binding)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}