package com.nmrc.note.ui.fragments.tasks

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.nmrc.note.R
import com.nmrc.note.data.model.adapters.SwipeHelper
import com.nmrc.note.databinding.FragmentTaskBinding
import com.nmrc.note.data.model.adapters.TaskAdapter
import com.nmrc.note.data.model.util.navigate
import com.nmrc.note.data.model.util.newToast
import com.nmrc.note.data.model.util.showMenu
import com.nmrc.note.viewmodel.TaskSharedViewModel
import com.nmrc.note.viewmodel.ViewModelFactory


class TaskFragment : Fragment(R.layout.fragment_task) {

    private val binding: FragmentTaskBinding by viewBinding()
    private val svm: TaskSharedViewModel by activityViewModels{ ViewModelFactory(requireContext()) }
    private val taskAdapter by lazy { TaskAdapter(svm.getTaskListenerInterface(), findNavController()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRV()
        observerTaskVM()
        newTaskListener()
        menuListener()
        swipeListener()
    }

    private fun setUpRV() {
        with(binding) {
            rvTaskList.apply {
                layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                adapter = taskAdapter
            }
        }
    }

    private fun observerTaskVM() {
        svm.taskList().observe(viewLifecycleOwner, { list ->
            taskAdapter.update(list)

            svm.apply {
                countTask(binding)
                visibleTools(binding)
            }
        })
    }

    private fun newTaskListener() {
        binding.fabNewTask.setOnClickListener {
            navigate(R.id.action_toNewTask)
        }
    }

    private fun menuListener() {
        binding.ivOptions.setOnClickListener {
            showMenu(it,R.menu.tasks_menu) { menuItem ->
                when(menuItem) {
                    R.id.itemClearAllTasks -> {
                        svm.deleteAllTask()
                        newToast(R.string.deleteAllTasks, requireContext())
                    }
                }
            }
        }
    }

    private fun swipeListener() {
        ItemTouchHelper( SwipeHelper{ position ->
            val task = svm.taskList().value!![position]
            svm.deleteTask(task)
            newToast(R.string.doneTask, requireContext())
        } ).attachToRecyclerView(binding.rvTaskList)
    }
}