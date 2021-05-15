package com.nmrc.note.viewmodel

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.nmrc.note.R
import com.nmrc.note.databinding.FragmentNewTaskBinding
import com.nmrc.note.databinding.FragmentTaskBinding
import com.nmrc.note.repository.model.Priority
import com.nmrc.note.repository.model.Task
import com.nmrc.note.repository.model.TopicTaks
import com.nmrc.note.repository.model.adapters.TaskAdapter
import com.nmrc.note.repository.model.util.TaskListener
import com.nmrc.note.repository.model.util.navigate
import com.nmrc.note.repository.model.util.newSnackB
import com.nmrc.note.repository.model.util.newToast
import java.util.*

class TaskSharedViewModel : ViewModel(), TaskListener {

    private var _taskList = MutableLiveData<ArrayList<Task>>()
    private var tasksArray: ArrayList<Task> = ArrayList()
    private var _editStateTask = MutableLiveData<StateEditTask>()
    private var _emptyTaskList = MutableLiveData<Boolean>()

    companion object {
        const val LOW = "LOW"
        const val MEDIUM = "MEDIUM"
        const val HOME = "HOME"
        const val WORK = "WORK"
        const val UTC = "UTC"
        const val DATE = "DATE"

        fun notEditTaskState() = StateEditTask(false,0)
    }

    init {
        setTasksList(tasksArray)
        setStateEditTask(StateEditTask(false,0))
    }

    data class StateEditTask (val state: Boolean, val position: Int)

    fun taskList(): LiveData<ArrayList<Task>> = _taskList

    fun editTask(): LiveData<StateEditTask> = _editStateTask

    fun setStateEditTask(stateEditTask: StateEditTask) {
        _editStateTask.value = stateEditTask
    }

    fun setStateEmptyTaskList(empty: Boolean) {
        _emptyTaskList.value = empty
    }

    private fun setTasksList(taskList: ArrayList<Task>){
        _taskList.value = taskList
    }

    fun getTaskListenerInterface() : TaskListener = this

    fun addTask(task: Task) = _taskList.value?.add(task)

    private fun removeTask(position: Int, adapter: TaskAdapter): Boolean {
        _taskList.value!!.removeAt(position)
        adapter.notifyDataSetChanged()
        return adapter.itemCount == 0
    }

     fun clearAllTask(adapter: TaskAdapter, context: Context): Boolean {
        return if (_taskList.value.isNullOrEmpty()) {
            newToast(R.string.noTasksForClear) {context}
            false
        }
        else {
            _taskList.value!!.clear()
            adapter.notifyDataSetChanged()
            newToast(R.string.clearAllTasks) {context}
            true
        }
    }

    fun refillDataTaskEdit(binding: FragmentNewTaskBinding, task: Task) {
        with(binding) {
            with(task) {
                etTitleTaskDialog.setText(title)
                tvTitleTaskDialog.setLines(1)
                tvTitleTaskDialog.text = title
                tvTitleTaskDialog.textSize = 16f
                etDescriptionTaskDialog.setText(description)
                tvDateTaskDialog.text = date
                sliderPriorityTaskDialog.value = when (priority.name) {
                    LOW -> 0f
                    MEDIUM -> 1f
                    else -> 2f
                }
                chipGroupTask.check(when (topic.name){
                    HOME -> R.id.chipHomeTaskDialog
                    WORK -> R.id.chipWorkTaskDialog
                    else -> R.id.chipOtherTaskDialog
                })
            }
        }
    }

    fun visibleToolsAndCountTask(binding: FragmentTaskBinding) {
        if (_emptyTaskList.value!!)
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
        val highAmount = _taskList.value!!.count { it.priority == Priority.HIGHT }
        val mediumAmount = _taskList.value!!.count { it.priority == Priority.MEDIUM }
        val lowAmount = _taskList.value!!.count { it.priority == Priority.LOW }

        with(binding) {
            chipPriorityTaskLow.text = lowAmount.toString()
            chipPriorityTaskMedium.text = mediumAmount.toString()
            chipPriorityTaskHigh.text = highAmount.toString()
        }
    }

    override fun onTaskClicked(view: View, position: Int, adapter: TaskAdapter) {
        when(view.id) {
            R.id.llCheckTask -> {
                val binding = FragmentTaskBinding.bind(view.rootView)
                if(removeTask(position,adapter)){
                    binding.tvPreviewNothingTaskFragment.visibility = View.VISIBLE
                    setStateEmptyTaskList(true)
                    visibleToolsAndCountTask(binding)
                }
                countTask(binding)
                newSnackB(R.string.doneTask,view)
            }
            R.id.mcvTask -> {
                setStateEditTask(StateEditTask(true,position))
                navigate(view,R.id.action_toNewTask)
            }
        }
    }

    class RecoverTaskData(binding: FragmentNewTaskBinding) {

        var title: String
        var description: String
        var date: String
        var priority: Priority
        var topic: TopicTaks

        private var mbinding = binding

        init {
            with(binding) {
                title = etTitleTaskDialog.text.toString()
                description = etDescriptionTaskDialog.text.toString()
                date = tvDateTaskDialog.text.toString()
                priority = dataPriority()
                topic = dataTopic()
            }
        }

        fun emptyData(): Boolean {
            return when {
                mbinding.etTitleTaskDialog.text.isNullOrEmpty() -> true
                mbinding.etDescriptionTaskDialog.text.isNullOrEmpty() -> true
                else -> false
            }
        }

        private fun dataPriority(): Priority {
            return when (mbinding.sliderPriorityTaskDialog.value) {
                0f -> Priority.LOW
                1f -> Priority.MEDIUM
                else -> Priority.HIGHT
            }
        }

        private fun dataTopic(): TopicTaks {
            return when(mbinding.chipGroupTask.checkedChipId) {
                mbinding.chipHomeTaskDialog.id -> TopicTaks.HOME
                mbinding.chipWorkTaskDialog.id -> TopicTaks.WORK
                else -> TopicTaks.OTHER
            }
        }
    }

    fun dateRangePicker(binding: FragmentNewTaskBinding,sfm: FragmentManager) {

        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone(UTC))

        calendar.timeInMillis = today

        val january = calendar.run {
            set(Calendar.MONTH,Calendar.JANUARY)
            timeInMillis
        }.also { calendar.timeInMillis = today }

        val december = calendar.run {
            set(Calendar.MONTH,Calendar.DECEMBER)
            timeInMillis
        }

        val constraintsBuilder = CalendarConstraints.Builder().apply {
            setStart(january)
            setEnd(december)
            setValidator(DateValidatorPointForward.now())
        }

        val picker = MaterialDatePicker.Builder.dateRangePicker().apply {
            setTitleText(R.string.selectARange)
            setCalendarConstraints(constraintsBuilder.build())
        }.build()

        picker.apply {
            show(sfm, DATE)
            addOnPositiveButtonClickListener { binding.tvDateTaskDialog.text = this.headerText }
        }
    }
}