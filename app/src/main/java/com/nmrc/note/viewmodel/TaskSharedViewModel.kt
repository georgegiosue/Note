package com.nmrc.note.viewmodel

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.nmrc.note.R
import com.nmrc.note.data.model.Priority
import com.nmrc.note.data.model.Task
import com.nmrc.note.data.model.Topic
import com.nmrc.note.data.model.adapters.TaskAdapter
import com.nmrc.note.data.model.room.database.AppDatabase
import com.nmrc.note.data.model.room.repository.TasksRepository
import com.nmrc.note.data.model.util.TaskListener
import com.nmrc.note.data.model.util.navigate
import com.nmrc.note.data.model.util.newSnackB
import com.nmrc.note.data.model.util.newToast
import com.nmrc.note.databinding.FragmentNewTaskBinding
import com.nmrc.note.databinding.FragmentTaskBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class TaskSharedViewModel(context: Context) : ViewModel(), TaskListener {

    private val _taskList: LiveData<MutableList<Task>>
    private var _editStateTask = MutableLiveData<StateEditTask>()
    private var _emptyTaskList = MutableLiveData<Boolean>()
    private lateinit var bindingNewTask: FragmentNewTaskBinding
    private lateinit var bindingTask: FragmentTaskBinding
    private val repository: TasksRepository

    init {
        val taskDao = AppDatabase.getDatabase(context).tasksDao()
        repository = TasksRepository(taskDao)
        _taskList = repository.readAllData
        withStates(noEdit = true)
    }

    companion object {
        const val LOW = "LOW"
        const val MEDIUM = "MEDIUM"
        const val HOME = "HOME"
        const val WORK = "WORK"
        const val UTC = "UTC"
        const val DATE = "DATE"
    }

    private fun setStateEditTask(stateEditTask: StateEditTask) {
        _editStateTask.value = stateEditTask
    }

    private fun removeTask(position: Int, adapter: TaskAdapter): Boolean {
        _taskList.value!!.removeAt(position)
        adapter.notifyDataSetChanged()
        return adapter.itemCount == 0
    }

    private fun visibleToolsAndCountTask() {
        if (_emptyTaskList.value!!)
            with(bindingTask) {
                chipGroupTaskPriorities.visibility = View.INVISIBLE
                llToolsTasks.visibility = View.INVISIBLE
                tvPreviewNothingTaskFragment.visibility = View.VISIBLE
            }
        else
            with(bindingTask) {
                chipGroupTaskPriorities.visibility = View.VISIBLE
                llToolsTasks.visibility = View.VISIBLE
            }
    }

    private fun countTask() {
        val highAmount = _taskList.value!!.count { it.priority == Priority.HIGH }
        val mediumAmount = _taskList.value!!.count { it.priority == Priority.MEDIUM }
        val lowAmount = _taskList.value!!.count { it.priority == Priority.LOW }

        with(bindingTask) {
            chipPriorityTaskLow.text = lowAmount.toString()
            chipPriorityTaskMedium.text = mediumAmount.toString()
            chipPriorityTaskHigh.text = highAmount.toString()
        }
    }

    fun taskList(): LiveData<MutableList<Task>> = _taskList

    fun editTask(): LiveData<StateEditTask> = _editStateTask

    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTask(task)
        }
    }


    fun withTasks(list: (MutableList<Task>) -> Task) = list(_taskList.value!!)

    fun withTasks(arg: (list: MutableList<Task>, ePosition: Int ) -> Unit) {
        with(_editStateTask.value!!) {
            arg(_taskList.value!!, position)
        }
    }

    fun withStates(emptyList: Boolean? = null,
                   noEdit: Boolean = false,
                   count: Boolean = false ) {

        if (noEdit)
            setStateEditTask(StateEditTask(false,0))

        emptyList?.let { empty ->
            setStateEmptyTaskList(empty)
            if (empty)
                visibleToolsAndCountTask()
        }

        if (count) {
            countTask()
            visibleToolsAndCountTask()
        }
    }

    fun setBindingTask(binding: FragmentTaskBinding) {
        this.bindingTask = binding
    }

    fun setBindingNewTask(binding: FragmentNewTaskBinding) {
        this.bindingNewTask = binding
    }

    fun setStateEmptyTaskList(empty: Boolean) {
        _emptyTaskList.value = empty
    }

    fun getTaskListenerInterface() : TaskListener = this

    fun clearAllTask(adapter: TaskAdapter, context: Context): Boolean {
        return if (_taskList.value.isNullOrEmpty()) {
            newToast(R.string.noTasksForClear, context)
            false
        }
        else {
            _taskList.value!!.clear()
            adapter.notifyDataSetChanged()
            newToast(R.string.clearAllTasks, context)
            true
        }
    }

    fun refillDataTaskEdit(task: Task) {
        with(bindingNewTask) {
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

    fun dateRangePicker(sfm: FragmentManager) {

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
            addOnPositiveButtonClickListener {
                bindingNewTask.tvDateTaskDialog.text = this.headerText
            }
        }
    }

    override fun onTaskClicked(view: View, position: Int, adapter: TaskAdapter) {
        when(view.id) {
            R.id.llCheckTask -> {
                if(removeTask(position,adapter)){
                    with(bindingTask) {
                        tvPreviewNothingTaskFragment.visibility = View.VISIBLE
                    }
                    withStates(emptyList = true)
                }
                countTask()
                newSnackB(R.string.doneTask,view)
            }
            R.id.mcvTask -> {
                setStateEditTask(StateEditTask(true,position))
                navigate(view,R.id.action_toNewTask)
            }
        }
    }

    data class StateEditTask (val editable: Boolean, val position: Int)

    class RecoverTaskData(binding: FragmentNewTaskBinding) {

        var title: String
        var description: String
        var date: String
        var priority: Priority
        var topic: Topic

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

        private fun dataPriority(): Priority {
            return when (mbinding.sliderPriorityTaskDialog.value) {
                0f -> Priority.LOW
                1f -> Priority.MEDIUM
                else -> Priority.HIGH
            }
        }

        private fun dataTopic(): Topic {
            return when(mbinding.chipGroupTask.checkedChipId) {
                mbinding.chipHomeTaskDialog.id -> Topic.HOME
                mbinding.chipWorkTaskDialog.id -> Topic.WORK
                else -> Topic.OTHER
            }
        }

        fun emptyData(): Boolean {
            return when {
                mbinding.etTitleTaskDialog.text.isNullOrEmpty() -> true
                mbinding.etDescriptionTaskDialog.text.isNullOrEmpty() -> true
                else -> false
            }
        }
    }
}