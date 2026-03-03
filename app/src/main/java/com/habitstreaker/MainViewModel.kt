package com.habitstreaker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.habitstreaker.data.TaskItem
import com.habitstreaker.data.TaskRepository
import com.habitstreaker.notification.ReminderScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.math.max

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = TaskRepository(app)
    private val _tasks = MutableStateFlow<List<TaskItem>>(emptyList())
    val tasks: StateFlow<List<TaskItem>> = _tasks.asStateFlow()

    init {
        viewModelScope.launch {
            repository.tasksFlow.collect { _tasks.value = it }
        }
    }

    fun addTask(title: String, hour: Int, minute: Int) {
        val cleanTitle = title.trim()
        if (cleanTitle.isBlank()) return
        val task = TaskItem(
            id = System.currentTimeMillis(),
            title = cleanTitle,
            reminderHour = hour,
            reminderMinute = minute,
            streakDays = 0,
            lastCompletedEpochDay = null
        )
        persistAndReschedule(_tasks.value + task)
    }

    fun completeTask(taskId: Long) {
        val today = LocalDate.now().toEpochDay()
        val updated = _tasks.value.map { task ->
            if (task.id != taskId) return@map task
            val previous = task.lastCompletedEpochDay
            val newStreak = when {
                previous == today -> task.streakDays
                previous == today - 1 -> task.streakDays + 1
                else -> max(1, task.streakDays)
            }
            task.copy(streakDays = newStreak, lastCompletedEpochDay = today)
        }
        persistAndReschedule(updated)
    }

    fun removeTask(taskId: Long) {
        ReminderScheduler.cancel(getApplication(), taskId)
        persistAndReschedule(_tasks.value.filterNot { it.id == taskId })
    }

    fun moveTaskTime(taskId: Long, hour: Int, minute: Int) {
        val updated = _tasks.value.map { task ->
            if (task.id == taskId) task.copy(reminderHour = hour, reminderMinute = minute) else task
        }
        persistAndReschedule(updated)
    }

    private fun persistAndReschedule(tasks: List<TaskItem>) {
        _tasks.update { tasks }
        viewModelScope.launch {
            repository.saveTasks(tasks)
            tasks.forEach { ReminderScheduler.schedule(getApplication(), it) }
        }
    }
}
