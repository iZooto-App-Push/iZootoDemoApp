package com.k.deeplinkingtesting.pushSDK.api.retry

import com.k.deeplinkingtesting.pushSDK.api.model.PushPayload
import com.k.deeplinkingtesting.pushSDK.api.provider.PushProvider
import kotlinx.coroutines.delay

class RetryPolicy (
){
    suspend fun execute(
        provider: PushProvider,
        payload: PushPayload
    ): Boolean
    {
        var delayMs =1000L
        repeat(3){
            if (provider.send(payload)) return true

            delay(delayMs)
            delayMs *= 2
        }

        return  false

    }
}