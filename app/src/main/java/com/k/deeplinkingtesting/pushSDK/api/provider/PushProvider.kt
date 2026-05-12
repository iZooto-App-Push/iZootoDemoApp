package com.k.deeplinkingtesting.pushSDK.api.provider

import com.izooto.Payload
import com.k.deeplinkingtesting.pushSDK.api.model.PushPayload

interface PushProvider {
    val name:String
    fun isSupported(): Boolean
   suspend fun send(payload: PushPayload): Boolean
}
