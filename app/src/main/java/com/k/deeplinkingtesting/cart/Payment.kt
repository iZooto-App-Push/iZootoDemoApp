package com.k.deeplinkingtesting.cart

interface Payment {
    fun pay(amount:Int):String
}
class UPI : Payment
{
    override fun pay(amount: Int): String {
        return "Payment via UPI $amount"
    }
}
class Card : Payment
{
    override fun pay(amount: Int): String {
        return "Payment via Card $amount"
    }

}