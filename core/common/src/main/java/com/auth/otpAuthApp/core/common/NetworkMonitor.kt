package com.auth.otpAuthApp.core.common

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

interface NetworkMonitor {
    val isOnline: Boolean
}

@Singleton
class ConnectivityManagerNetworkMonitor @Inject constructor(
    @ApplicationContext private val context: Context,
) : NetworkMonitor {
    override val isOnline: Boolean
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        get() = context.isNetworkAvailable()
}
