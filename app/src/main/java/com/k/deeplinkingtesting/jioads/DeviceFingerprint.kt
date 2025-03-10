package com.k.deeplinkingtesting.jioads

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings

class DeviceFingerprint(private val context: Context) {
    
    fun getDeviceFingerprint(): Map<String, String> {
        return mapOf(
            "Android_ID" to getAndroidId(),
            "Build_ID" to Build.ID,
            "Display_ID" to Build.DISPLAY,
            "Model" to Build.MODEL,
            "Manufacturer" to Build.MANUFACTURER,
            "Product" to Build.PRODUCT,
            "Device" to Build.DEVICE,
            "Brand" to Build.BRAND
        )
    }

    @SuppressLint("HardwareIds")
    private fun getAndroidId(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: "Unknown"
    }
}
