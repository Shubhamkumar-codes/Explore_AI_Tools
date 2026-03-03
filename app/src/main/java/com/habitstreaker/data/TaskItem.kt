package com.habitstreaker.data

data class TaskItem(
    val id: Long,
    val title: String,
    val reminderHour: Int,
    val reminderMinute: Int,
    val streakDays: Int,
    val lastCompletedEpochDay: Long?
)
