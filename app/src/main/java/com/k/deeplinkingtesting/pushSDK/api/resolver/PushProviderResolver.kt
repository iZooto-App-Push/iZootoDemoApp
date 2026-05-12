package com.k.deeplinkingtesting.pushSDK.api.resolver

import com.k.deeplinkingtesting.pushSDK.api.model.ProviderConfig
import com.k.deeplinkingtesting.pushSDK.api.provider.PushProvider
import okhttp3.internal.notify

class PushProviderResolver(
    private val providers: List<PushProvider>
) {

    fun resolve(config: ProviderConfig): PushProvider {
        return config.priority
            .mapNotNull { providerName ->
                providers.find {
                    it.name == providerName && it.isSupported()
                }
            }
            .firstOrNull()
            ?: throw IllegalStateException("No supported provider found")
    }
}