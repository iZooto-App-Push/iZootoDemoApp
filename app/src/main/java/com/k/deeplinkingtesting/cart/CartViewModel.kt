package com.k.deeplinkingtesting.cart

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CartViewModel(private val repository: CartRepository): ViewModel() {
    private val _state= MutableStateFlow(CartState())
    val state: StateFlow<CartState> = _state

    fun addProduct(product: Product)
    {
        repository.addProduct(product)
        updateState()
    }
    fun checkout() {
        val result = repository.checkout()
        _state.value = _state.value.copy(paymentStatus = result)
    }
    private fun updateState() {
        _state.value = CartState(
            items = repository.getItems(),
            total = repository.getTotal()
        )
    }
}