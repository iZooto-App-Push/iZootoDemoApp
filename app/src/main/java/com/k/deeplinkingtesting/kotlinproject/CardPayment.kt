package com.k.deeplinkingtesting.kotlinproject

import android.util.Log
import javax.inject.Inject

class CardPayment @Inject constructor(): Payment {
    override fun pay(money: Double) {
       Log.e("Payment","Card payment $money")
    }
}