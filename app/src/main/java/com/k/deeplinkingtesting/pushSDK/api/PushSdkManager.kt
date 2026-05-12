package com.k.deeplinkingtesting.pushSDK.api

import com.k.deeplinkingtesting.pushSDK.api.analytics.AnalyticsTracker
import com.k.deeplinkingtesting.pushSDK.api.analytics.ConsoleAnalyticsTracker
import com.k.deeplinkingtesting.pushSDK.api.model.ProviderConfig
import com.k.deeplinkingtesting.pushSDK.api.model.PushPayload
import com.k.deeplinkingtesting.pushSDK.api.provider.FCMProvider
import com.k.deeplinkingtesting.pushSDK.api.provider.HuaweiProvider
import com.k.deeplinkingtesting.pushSDK.api.provider.XiaomiProvider
import com.k.deeplinkingtesting.pushSDK.api.resolver.PushProviderResolver
import com.k.deeplinkingtesting.pushSDK.api.retry.RetryPolicy

class PushSdkManager
    (
            private  val resolver: PushProviderResolver,
            private val analytics: AnalyticsTracker,
            private  val retryPolicy: RetryPolicy
            )

{
    suspend fun send(payload: PushPayload)
    {
       val config = ProviderConfig(listOf("FCM","Xiaomi","Huawei"))
        val provider = resolver.resolve(config)
        val selectedProvider = resolver.resolve(config)
        println("Selected Provider${selectedProvider.name} ")

        analytics.track("Provider Selected ${provider.name}")
        val success  = retryPolicy.execute(provider,payload)
        if(success)
        {
            analytics.track("Push Sent Successfully")
        }
        else{
            analytics.track("Push Failed")

        }


    }
}
suspend fun main()
{
    val providers = listOf(
        FCMProvider(),
        XiaomiProvider(),
        HuaweiProvider()
    )
    val sdk = PushSdkManager(
        resolver = PushProviderResolver(providers),
        analytics = ConsoleAnalyticsTracker(),
        retryPolicy = RetryPolicy()

    )
    sdk.send(
        PushPayload(title = "Welcome to the Notification", message ="Hello user")

    )
}