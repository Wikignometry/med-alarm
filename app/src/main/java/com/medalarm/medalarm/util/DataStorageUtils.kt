package com.medalarm.medalarm.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.sql.Time


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val ALARM_COUNT = intPreferencesKey("alarm_count")
val END_TIME = longPreferencesKey("end_time")

suspend fun loadAlarmCount(context: Context): Int {
    return context.dataStore.data
        .map { preferences ->
            preferences[ALARM_COUNT] ?: 3
        }.first()
}

suspend fun saveAlarmCount(context: Context, newAlarmCount: Int) {
    context.dataStore.edit { settings ->
        settings[ALARM_COUNT] = newAlarmCount
    }
}

suspend fun loadEndTimeLong(context: Context): Long {
    return context.dataStore.data
        .map { preferences ->
            preferences[END_TIME] ?: Time(System.currentTimeMillis()).time
        }.first()
}

suspend fun loadEndTime(context: Context): Time {
    return Time(loadEndTimeLong(context))
}

suspend fun saveEndTime(context: Context, endTime: Time) {
    saveEndTime(context, endTime.time)
}

suspend fun saveEndTime(context: Context, endTime: Long) {
    context.dataStore.edit { settings ->
        settings[com.medalarm.medalarm.util.END_TIME] = endTime
    }
}