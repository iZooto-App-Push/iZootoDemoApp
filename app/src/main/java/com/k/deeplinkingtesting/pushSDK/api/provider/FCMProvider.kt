package com.k.deeplinkingtesting.pushSDK.api.provider

import com.izooto.Payload
import com.k.deeplinkingtesting.pushSDK.api.model.PushPayload

class FCMProvider : PushProvider {
    override val name: String = "FCM"


    override fun isSupported(): Boolean {
       return true;
    }

    override suspend fun send(payload: PushPayload): Boolean {
        println("Sending Notification via FCM")
        return true

    }
}