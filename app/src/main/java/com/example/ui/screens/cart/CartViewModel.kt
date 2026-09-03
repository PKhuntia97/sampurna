package com.example.ui.screens.cart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.SampurnaDatabase
import com.example.data.local.entity.OfferEntity
import com.example.data.local.entity.UserEntity
import com.example.data.repository.CartItemWithProduct
import com.example.data.repository.SampurnaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SampurnaRepository(SampurnaDatabase.getDatabase(application))

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItemWithProduct>>(emptyList())
    val cartItems: StateFlow<List<CartItemWithProduct>> = _cartItems.asStateFlow()

    private val _appliedCoupon = MutableStateFlow<OfferEntity?>(null)
    val appliedCoupon: StateFlow<OfferEntity?> = _appliedCoupon.asStateFlow()

    private val _couponInput = MutableStateFlow("")
    val couponInput: StateFlow<String> = _couponInput.asStateFlow()

    private val _couponError = MutableStateFlow<String?>(null)
    val couponError: StateFlow<String?> = _couponError.asStateFlow()

    private val _actionMessage = MutableStateFlow<String?>(null)
    val actionMessage: StateFlow<String?> = _actionMessage.asStateFlow()

    val totalItemCount: StateFlow<Int> = repository.getCartCount(1L)
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val availableOffers: StateFlow<List<OfferEntity>> = repository.getActiveOffers()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        loadCurrentUserAndCart()
    }

    fun loadCurrentUserAndCart() {
        viewModelScope.launch {
            val user = repository.getUserByMobileOrEmail("9876543210")
            _currentUser.value = user
            if (user != null) {
                repository.getCartItems(user.id).collect { items ->
                    _cartItems.value = items
                }
            }
        }
    }

    fun updateQuantity(cartItemId: Long, newQuantity: Int) {
        viewModelScope.launch {
            repository.updateCartQuantity(cartItemId, newQuantity)
        }
    }

    fun removeItem(cartItemId: Long) {
        viewModelScope.launch {
            repository.removeFromCart(cartItemId)
            _actionMessage.value = "Item removed from cart"
        }
    }

    fun clearCart() {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.clearCart(user.id)
            _actionMessage.value = "Cart cleared"
        }
    }

    fun saveToWishlist(cartItemId: Long, productId: Long) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.saveToWishlistFromCart(cartItemId, user.id, productId)
            _actionMessage.value = "Moved to Wishlist"
        }
    }

    fun setCouponInput(code: String) {
        _couponInput.value = code.uppercase()
        _couponError.value = null
    }

    fun applyCoupon(code: String) {
        val trimmed = code.trim().uppercase()
        val offer = availableOffers.value.find { it.code.equals(trimmed, ignoreCase = true) }
        val subtotal = calculateSubtotal()

        if (offer == null) {
            _couponError.value = "Invalid coupon code \"$trimmed\""
            _appliedCoupon.value = null
            return
        }

        if (subtotal < offer.minOrderValue) {
            _couponError.value = "Min order value for ${offer.code} is ₹${offer.minOrderValue.toInt()}"
            _appliedCoupon.value = null
            return
        }

        _appliedCoupon.value = offer
        _couponError.value = null
        _actionMessage.value = "Coupon \"${offer.code}\" applied successfully!"
    }

    fun removeCoupon() {
        _appliedCoupon.value = null
        _couponInput.value = ""
        _couponError.value = null
    }

    fun clearMessage() {
        _actionMessage.value = null
    }

    fun calculateSubtotal(): Double {
        return _cartItems.value.sumOf { (it.product?.price ?: it.cartItem.unitPrice) * it.cartItem.quantity }
    }

    fun calculateMrpTotal(): Double {
        return _cartItems.value.sumOf { (it.product?.mrp ?: it.cartItem.unitMrp) * it.cartItem.quantity }
    }

    fun calculateDiscount(): Double {
        val coupon = _appliedCoupon.value ?: return 0.0
        val subtotal = calculateSubtotal()
        val discount = (subtotal * (coupon.discountPercent / 100.0))
        return Math.min(discount, coupon.maxDiscount)
    }

    fun calculateFinalTotal(): Double {
        val subtotal = calculateSubtotal()
        val discount = calculateDiscount()
        return Math.max(0.0, subtotal - discount)
    }
}
