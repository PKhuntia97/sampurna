package com.example.ui.screens.orders

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.SampurnaDatabase
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.UserEntity
import com.example.data.repository.OrderWithDetails
import com.example.data.repository.SampurnaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CustomerOrdersViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SampurnaRepository(SampurnaDatabase.getDatabase(application))

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    private val _orders = MutableStateFlow<List<OrderEntity>>(emptyList())
    val orders: StateFlow<List<OrderEntity>> = _orders.asStateFlow()

    private val _selectedFilter = MutableStateFlow("ALL") // ALL, ACTIVE, DELIVERED, CANCELLED
    val selectedFilter: StateFlow<String> = _selectedFilter.asStateFlow()

    private val _selectedOrderDetails = MutableStateFlow<OrderWithDetails?>(null)
    val selectedOrderDetails: StateFlow<OrderWithDetails?> = _selectedOrderDetails.asStateFlow()

    private val _showCancelDialog = MutableStateFlow(false)
    val showCancelDialog: StateFlow<Boolean> = _showCancelDialog.asStateFlow()

    private val _cancelReason = MutableStateFlow("Ordered by mistake")
    val cancelReason: StateFlow<String> = _cancelReason.asStateFlow()

    private val _isCancelling = MutableStateFlow(false)
    val isCancelling: StateFlow<Boolean> = _isCancelling.asStateFlow()

    private val _actionMessage = MutableStateFlow<String?>(null)
    val actionMessage: StateFlow<String?> = _actionMessage.asStateFlow()

    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            val user = repository.getUserByMobileOrEmail("9876543210")
            _currentUser.value = user
            if (user != null) {
                repository.getOrdersForCustomer(user.id).collect { list ->
                    _orders.value = list
                }
            }
        }
    }

    fun selectFilter(filter: String) {
        _selectedFilter.value = filter
    }

    fun loadOrderDetails(orderId: Long) {
        viewModelScope.launch {
            val details = repository.getOrderDetails(orderId)
            _selectedOrderDetails.value = details
        }
    }

    fun openCancelDialog() {
        _showCancelDialog.value = true
    }

    fun closeCancelDialog() {
        _showCancelDialog.value = false
    }

    fun setCancelReason(reason: String) {
        _cancelReason.value = reason
    }

    fun cancelCurrentOrder() {
        val details = _selectedOrderDetails.value ?: return
        val user = _currentUser.value ?: return

        viewModelScope.launch {
            _isCancelling.value = true
            val success = repository.cancelOrderByCustomer(
                orderId = details.order.id,
                customerId = user.id,
                reason = _cancelReason.value
            )

            _isCancelling.value = false
            _showCancelDialog.value = false

            if (success) {
                _actionMessage.value = "Order cancelled successfully. Refund initiated if paid."
                // Refresh details
                loadOrderDetails(details.order.id)
            } else {
                _actionMessage.value = "Cannot cancel order in current state."
            }
        }
    }

    fun clearMessage() {
        _actionMessage.value = null
    }
}
