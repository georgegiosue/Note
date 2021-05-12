package com.nmrc.note.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.nmrc.note.R
import com.nmrc.note.databinding.FragmentTaskBinding
import com.nmrc.note.repository.model.adapters.TaskAdapter
import com.nmrc.note.viewmodel.TaskSharedViewModel


class TaskFragment : Fragment() {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!
    private var navController: NavController? = null
    private lateinit var taskAdapter: TaskAdapter
    private val taskSharedViewModel: TaskSharedViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTaskBinding.inflate(inflater,container,false)
        taskAdapter = TaskAdapter(taskSharedViewModel.getTaskListenerInterface())

        with(binding) {
            rvTaskList.apply {
                layoutManager =LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                adapter = taskAdapter
            }
        }

        observerTaskVM()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        newTaskListener()
        clearAllTaskListener()
    }

    private fun newTaskListener() {
        binding.fabNewTask.setOnClickListener {
        navController!!.navigate(R.id.action_toNewTask)
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
                taskSharedViewModel.setStateEmptyTaskList(true)
                taskSharedViewModel.visibleToolsAndCountTask(binding)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}