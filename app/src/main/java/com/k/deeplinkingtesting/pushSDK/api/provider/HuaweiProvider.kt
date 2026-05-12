package com.k.deeplinkingtesting.pushSDK.api.provider

import com.izooto.Payload
import com.k.deeplinkingtesting.pushSDK.api.model.PushPayload
import kotlinx.coroutines.delay

class HuaweiProvider: PushProvider {
    override val name: String = "Huawei"

    override fun isSupported(): Boolean {
       return false
    }

    override suspend fun send(payload: PushPayload): Boolean {
        delay(500)
       println("Notification Send via Huawei")
        return true
    }
}