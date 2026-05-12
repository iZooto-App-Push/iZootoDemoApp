package com.k.deeplinkingtesting.cart

class CartRepository(private val payment: Payment) {

     val cartItems = mutableListOf<Product>()
    fun addProduct(product: Product)
    {
        cartItems.add(product)
    }
    fun getTotal(): Int {
        return cartItems.sumOf { it.productPrice }
    }

    fun checkout(): String {
        val total = getTotal()
        return payment.pay(total)
    }

    fun getItems(): List<Product> = cartItems
}