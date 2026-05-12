package com.k.deeplinkingtesting.kotlinproject

object PaymentFactory {
    fun create(type: PaymentType): Payment
    {
        return  when(type)
        {
            PaymentType.UPI-> UPIPayment()
            PaymentType.CARD-> CardPayment()
            PaymentType.WALLET-> WalletPayment()
        }
    }
}
fun main()
{
    val payment = PaymentFactory.create(PaymentType.UPI)
    payment.pay(500.00)
}
