package com.habitstreaker.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.habitstreaker.data.TaskItem
import java.time.LocalDateTime
import java.time.ZoneId

object ReminderScheduler {
    fun schedule(context: Context, task: TaskItem) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val triggerTime = nextTriggerMillis(task.reminderHour, task.reminderMinute)
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            createPendingIntent(context, task)
        )
    }

    fun cancel(context: Context, taskId: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(createPendingIntent(context, taskId))
    }

    private fun nextTriggerMillis(hour: Int, minute: Int): Long {
        val now = LocalDateTime.now()
        var reminder = now.withHour(hour).withMinute(minute).withSecond(0).withNano(0)
        if (!reminder.isAfter(now)) reminder = reminder.plusDays(1)
        return reminder.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    private fun createPendingIntent(context: Context, task: TaskItem): PendingIntent =
        createPendingIntent(context, task.id, task.title)

    private fun createPendingIntent(context: Context, taskId: Long): PendingIntent =
        createPendingIntent(context, taskId, "")

    private fun createPendingIntent(context: Context, taskId: Long, taskTitle: String): PendingIntent {
        val intent = Intent(context, ReminderReceiver::class.java)
            .putExtra(ReminderReceiver.EXTRA_TASK_ID, taskId)
            .putExtra(ReminderReceiver.EXTRA_TASK_TITLE, taskTitle)
        return PendingIntent.getBroadcast(
            context,
            taskId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
