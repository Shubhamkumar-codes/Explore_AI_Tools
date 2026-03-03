package com.habitstreaker.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.habitstreaker.data.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action != Intent.ACTION_BOOT_COMPLETED) return
        CoroutineScope(Dispatchers.IO).launch {
            val tasks = TaskRepository(context).tasksFlow.first()
            tasks.forEach { ReminderScheduler.schedule(context, it) }
        }
    }
}
