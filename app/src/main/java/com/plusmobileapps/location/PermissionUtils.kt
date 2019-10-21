package com.plusmobileapps.location

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build

import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

private const val SHARED_PREFS_ID = "GENERIC_PREFERENCES"

private val Context.sharedPreferences: SharedPreferences
    get() = getSharedPreferences(SHARED_PREFS_ID, Context.MODE_PRIVATE)

private fun Context.editSharedPrefs(block: SharedPreferences.Editor.() -> Unit) {
    sharedPreferences.edit().apply {
        block()
        apply()
    }
}

@RequiresApi(api = Build.VERSION_CODES.M)
fun Activity.neverAskAgainSelected(permission: String): Boolean {
    val prevShouldShowStatus = getRationaleDisplayStatus(permission)
    val currShouldShowStatus = shouldShowRequestPermissionRationale(permission)
    return prevShouldShowStatus != currShouldShowStatus
}

fun Activity.setShouldShowStatus(permission: String) {
    editSharedPrefs {
        putBoolean(permission, true)
    }
}

fun Activity.getRationaleDisplayStatus(permission: String): Boolean {
    return sharedPreferences.getBoolean(permission, false)
}

fun isPermissionGranted(permission: String, context: Context): Boolean {
    return (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)
}

