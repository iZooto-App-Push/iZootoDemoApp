package com.k.deeplinkingtesting.pushSDK.api.analytics

class ConsoleAnalyticsTracker : AnalyticsTracker {
    override fun track(events: String) {
        println("[Analytics] $events")
    }
}

