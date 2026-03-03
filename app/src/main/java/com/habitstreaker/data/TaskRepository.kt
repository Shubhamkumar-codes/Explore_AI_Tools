package com.habitstreaker.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

private val Context.dataStore by preferencesDataStore(name = "habitstreaker_store")

class TaskRepository(private val context: Context) {
    private val tasksKey = stringPreferencesKey("tasks_json")

    val tasksFlow: Flow<List<TaskItem>> = context.dataStore.data.map { prefs ->
        decodeTasks(prefs[tasksKey].orEmpty())
    }

    suspend fun saveTasks(tasks: List<TaskItem>) {
        context.dataStore.edit { prefs ->
            prefs[tasksKey] = encodeTasks(tasks)
        }
    }

    private fun encodeTasks(tasks: List<TaskItem>): String {
        val arr = JSONArray()
        tasks.forEach { task ->
            arr.put(
                JSONObject()
                    .put("id", task.id)
                    .put("title", task.title)
                    .put("hour", task.reminderHour)
                    .put("minute", task.reminderMinute)
                    .put("streak", task.streakDays)
                    .put("last", task.lastCompletedEpochDay ?: JSONObject.NULL)
            )
        }
        return arr.toString()
    }

    private fun decodeTasks(raw: String): List<TaskItem> {
        if (raw.isBlank()) return emptyList()
        return runCatching {
            val arr = JSONArray(raw)
            buildList {
                for (index in 0 until arr.length()) {
                    val obj = arr.getJSONObject(index)
                    add(
                        TaskItem(
                            id = obj.getLong("id"),
                            title = obj.getString("title"),
                            reminderHour = obj.getInt("hour"),
                            reminderMinute = obj.getInt("minute"),
                            streakDays = obj.getInt("streak"),
                            lastCompletedEpochDay = if (obj.isNull("last")) null else obj.getLong("last")
                        )
                    )
                }
            }
        }.getOrDefault(emptyList())
    }
}
