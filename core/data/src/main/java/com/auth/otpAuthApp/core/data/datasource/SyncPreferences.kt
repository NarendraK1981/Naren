package com.auth.otpAuthApp.core.data.datasource

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class SyncPreferences @Inject constructor(
    @ApplicationContext context: Context
) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("sync_prefs", Context.MODE_PRIVATE)

    var lastSyncTimestamp: Long
        get() = sharedPreferences.getLong(KEY_LAST_SYNC_TIME, 0L)
        set(value) = sharedPreferences.edit { putLong(KEY_LAST_SYNC_TIME, value) }

    companion object {
        private const val KEY_LAST_SYNC_TIME = "last_sync_time"
    }
}
