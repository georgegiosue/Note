package com.nmrc.note.viewmodel

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.nmrc.note.R
import com.nmrc.note.data.model.Priority
import com.nmrc.note.data.model.Task
import com.nmrc.note.data.model.room.database.AppDatabase
import com.nmrc.note.data.model.room.repository.TasksRepository
import com.nmrc.note.data.model.util.newToast
import com.nmrc.note.data.model.util.task.TaskListener
import com.nmrc.note.databinding.FragmentTaskBinding
import com.nmrc.note.ui.fragments.tasks.TaskFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskSharedViewModel(context: Context) : ViewModel(), TaskListener {

    private val _taskList: LiveData<MutableList<Task>>
    private val repository: TasksRepository

    init {
        val taskDao = AppDatabase.getDatabase(context).tasksDao()
        repository = TasksRepository(taskDao)
        _taskList = repository.readAllData
    }

    fun taskList(): LiveData<MutableList<Task>> = _taskList

    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(task)
        }
    }

    private fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(task)
        }
    }

    fun deleteAllTask() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTask()
        }
    }

    fun visibleTools(binding: FragmentTaskBinding) {
        if (_taskList.value!!.isEmpty())
            with(binding) {
                chipGroupTaskPriorities.visibility = View.INVISIBLE
                llToolsTasks.visibility = View.INVISIBLE
                tvPreviewNothingTaskFragment.visibility = View.VISIBLE
            }
        else
            with(binding) {
                chipGroupTaskPriorities.visibility = View.VISIBLE
                llToolsTasks.visibility = View.VISIBLE
            }
    }

    fun countTask(binding: FragmentTaskBinding) {
        val highAmount = _taskList.value!!.count { it.priority == Priority.HIGH }
        val mediumAmount = _taskList.value!!.count { it.priority == Priority.MEDIUM }
        val lowAmount = _taskList.value!!.count { it.priority == Priority.LOW }

        with(binding) {
            chipPriorityTaskLow.text = lowAmount.toString()
            chipPriorityTaskMedium.text = mediumAmount.toString()
            chipPriorityTaskHigh.text = highAmount.toString()
        }
    }

    fun getTaskListenerInterface() : TaskListener = this

    override fun onEditTask(task: Task, nav: NavController) {
        TaskFragmentDirections.actionToUpdateTaskFragment(task).also { action ->
            nav.navigate(action)
        }
    }

    override fun onDoneTask(task: Task, view: View) {
        deleteTask(task)
        newToast(R.string.doneTask, view.rootView.context)
    }
}