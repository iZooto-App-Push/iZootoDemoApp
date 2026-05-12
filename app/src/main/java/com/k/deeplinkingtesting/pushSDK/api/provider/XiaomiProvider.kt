package com.k.deeplinkingtesting.pushSDK.api.provider

import com.izooto.Payload
import com.k.deeplinkingtesting.pushSDK.api.model.PushPayload
import kotlinx.coroutines.delay

class XiaomiProvider : PushProvider {
    override val name: String = "Xiaomi"


    override fun isSupported(): Boolean {
         return  true
    }

    override suspend fun send(payload: PushPayload): Boolean {
        delay(500)
        println("Send Notification Via Xiaomi")
        return true
    }
}
