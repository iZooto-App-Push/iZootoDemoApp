package com.k.deeplinkingtesting.pushSDK.api.provider

import com.k.deeplinkingtesting.pushSDK.api.model.PushPayload

class AmazonPushProvider: PushProvider {
    override val name: String= "Amazon"


    override fun isSupported(): Boolean {
       return true
    }

    override suspend fun send(payload: PushPayload): Boolean {
       return true
    }
}