package com.k.deeplinkingtesting.cart

data class CartState(
    val items: List<Product> = emptyList(),
    val total: Int = 0,
    val paymentStatus: String = ""
)