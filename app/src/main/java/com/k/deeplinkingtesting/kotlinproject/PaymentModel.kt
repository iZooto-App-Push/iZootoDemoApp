package com.k.deeplinkingtesting.kotlinproject

import com.google.android.datatransport.runtime.dagger.Module
import com.google.android.datatransport.runtime.dagger.Provides

@Module
class PaymentModel {
    @Provides
    fun provideUpi(): Payment = UPIPayment()

    @Provides
    fun provideCard(): Payment = CardPayment()
}