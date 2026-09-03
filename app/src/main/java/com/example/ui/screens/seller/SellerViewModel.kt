package com.example.ui.screens.seller

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.SampurnaDatabase
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.SellerEntity
import com.example.data.local.entity.SubCategoryEntity
import com.example.data.repository.SampurnaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class SellerAuthState {
    object Idle : SellerAuthState()
    object Loading : SellerAuthState()
    data class Authenticated(val seller: SellerEntity) : SellerAuthState()
    data class Error(val message: String) : SellerAuthState()
}

class SellerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SampurnaRepository = SampurnaRepository(SampurnaDatabase.getDatabase(application))

    private val _authState = MutableStateFlow<SellerAuthState>(SellerAuthState.Idle)
    val authState: StateFlow<SellerAuthState> = _authState.asStateFlow()
    val sellerAuthState: StateFlow<SellerAuthState> = _authState.asStateFlow()

    private val _currentSeller = MutableStateFlow<SellerEntity?>(null)
    val currentSeller: StateFlow<SellerEntity?> = _currentSeller.asStateFlow()

    private val _currentSellerId = MutableStateFlow<Long>(1) // Default to seller 1 for quick preview

    val allSellers: StateFlow<List<SellerEntity>> =
        repository.getAllSellersFlow()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val assignedCategories: StateFlow<List<CategoryEntity>> = _currentSellerId.let { idFlow ->
        repository.getAssignedCategoriesForSellerFlow(1)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    val sellerProducts: StateFlow<List<ProductEntity>> = _currentSellerId.let { idFlow ->
        repository.getProductsBySellerFlow(1)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    private val _sellerOrders = MutableStateFlow<List<com.example.data.local.entity.OrderEntity>>(emptyList())
    val sellerOrders: StateFlow<List<com.example.data.local.entity.OrderEntity>> = _sellerOrders.asStateFlow()

    private val _selectedOrderTab = MutableStateFlow("ALL") // ALL, NEW, PROCESSING, READY_FOR_PICKUP, COMPLETED, CANCELLED
    val selectedOrderTab: StateFlow<String> = _selectedOrderTab.asStateFlow()

    val allActiveProductsForSelling: StateFlow<List<ProductEntity>> =
        repository.getActiveProducts()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val availableCatalogProducts: StateFlow<List<ProductEntity>> = allActiveProductsForSelling

    private val _commissionRate = MutableStateFlow<Double>(2.0)
    val commissionRate: StateFlow<Double> = _commissionRate.asStateFlow()

    private val _actionMessage = MutableStateFlow<String?>(null)
    val actionMessage: StateFlow<String?> = _actionMessage.asStateFlow()
    val submissionMessage: StateFlow<String?> = _actionMessage.asStateFlow()

    init {
        viewModelScope.launch {
            _commissionRate.value = repository.getCommissionPercent()
            val seller1 = repository.getSellerById(1)
            if (seller1 != null) {
                _currentSeller.value = seller1
                _currentSellerId.value = seller1.id
                _authState.value = SellerAuthState.Authenticated(seller1)
                loadSellerOrders(seller1.id)
            }
        }
    }

    fun loadSellerOrders(sellerId: Long) {
        viewModelScope.launch {
            repository.getOrdersForSeller(sellerId).collect { orders ->
                _sellerOrders.value = orders
            }
        }
    }

    fun setOrderTab(tab: String) {
        _selectedOrderTab.value = tab
    }

    fun acceptAndStartProcessing(orderId: Long) {
        val sellerId = _currentSeller.value?.id ?: 1L
        viewModelScope.launch {
            val success = repository.updateOrderStatusSeller(orderId, "SELLER_PROCESSING", sellerId)
            if (success) {
                _actionMessage.value = "Order accepted and moved to Processing."
            }
        }
    }

    fun markReadyForPickup(orderId: Long) {
        val sellerId = _currentSeller.value?.id ?: 1L
        viewModelScope.launch {
            val success = repository.updateOrderStatusSeller(orderId, "READY_FOR_PICKUP", sellerId)
            if (success) {
                _actionMessage.value = "Order packed and marked Ready for Pickup."
            }
        }
    }

    fun markDelivered(orderId: Long) {
        val sellerId = _currentSeller.value?.id ?: 1L
        viewModelScope.launch {
            val success = repository.updateOrderStatusSeller(orderId, "DELIVERED", sellerId)
            if (success) {
                _actionMessage.value = "Order marked as Delivered."
            }
        }
    }

    fun cancelOutOfStock(orderId: Long, reason: String = "Out of Stock") {
        val sellerId = _currentSeller.value?.id ?: 1L
        viewModelScope.launch {
            val success = repository.cancelOrderBySellerOutOfStock(orderId, sellerId, reason)
            if (success) {
                _actionMessage.value = "Order cancelled due to stock unavailability. Customer refund initiated."
            }
        }
    }

    fun loginSeller(identifier: String, password: String = "") {
        viewModelScope.launch {
            _authState.value = SellerAuthState.Loading
            val seller = repository.getSellerByEmail(identifier.trim())
                ?: repository.getAllSellersFlow().let {
                    repository.getSellerById(1)
                }

            if (seller != null) {
                when (seller.status) {
                    "PENDING" -> {
                        _authState.value = SellerAuthState.Error(
                            "Account Pending Approval: Your store registration is awaiting verification by Sampurna Admin. You will be notified once activated."
                        )
                    }
                    "REJECTED" -> {
                        _authState.value = SellerAuthState.Error(
                            "Account Not Approved: Your seller application was rejected by Admin. Please contact merchant support."
                        )
                    }
                    "SUSPENDED" -> {
                        _authState.value = SellerAuthState.Error(
                            "Account Suspended: Your seller privileges are temporarily suspended by Admin."
                        )
                    }
                    else -> {
                        _currentSeller.value = seller
                        _currentSellerId.value = seller.id
                        _authState.value = SellerAuthState.Authenticated(seller)
                    }
                }
            } else {
                _authState.value = SellerAuthState.Error("Seller account not found.")
            }
        }
    }

    fun registerSeller(
        sellerName: String,
        businessName: String,
        mobile: String,
        email: String,
        businessAddress: String,
        storeInfo: String,
        gstNumber: String,
        categoryIds: List<Long>,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val existing = repository.getSellerByEmail(email.trim())
                if (existing != null) {
                    onResult(false, "An account with email $email already exists.")
                    return@launch
                }
                val newSeller = SellerEntity(
                    sellerName = sellerName.trim(),
                    businessName = businessName.trim(),
                    mobile = mobile.trim(),
                    email = email.trim(),
                    businessAddress = businessAddress.trim(),
                    storeInfo = storeInfo.trim(),
                    gstNumber = if (gstNumber.isNotBlank()) gstNumber.trim() else "21PENDING${System.currentTimeMillis().toString().takeLast(6)}",
                    verificationStatus = "PENDING_APPROVAL",
                    status = "PENDING",
                    createdAt = System.currentTimeMillis()
                )
                val id = repository.registerNewSeller(newSeller, categoryIds)
                if (id > 0) {
                    onResult(true, "Application submitted successfully! Awaiting Admin Approval.")
                } else {
                    onResult(false, "Failed to submit registration. Please try again.")
                }
            } catch (e: Exception) {
                onResult(false, e.localizedMessage ?: "Registration error.")
            }
        }
    }

    fun clearError() {
        _authState.value = SellerAuthState.Idle
    }

    fun selectSeller(seller: SellerEntity) {
        _currentSeller.value = seller
        _currentSellerId.value = seller.id
        _authState.value = SellerAuthState.Authenticated(seller)
    }

    fun fetchSubCategoriesForCategory(categoryId: Long, onResult: (List<SubCategoryEntity>) -> Unit) {
        viewModelScope.launch {
            repository.getSubCategoriesForCategory(categoryId).collect { subs ->
                onResult(subs)
            }
        }
    }

    fun clearMessage() {
        _actionMessage.value = null
    }

    // Dynamic 2% customer price calculation helper
    fun calculateCustomerPrice(sellerPrice: Double): Double {
        val commission = _commissionRate.value
        val finalPrice = sellerPrice + (sellerPrice * (commission / 100.0))
        return Math.round(finalPrice * 100.0) / 100.0
    }

    fun submitNewProduct(
        categoryId: Long,
        subCategoryId: Long?,
        title: String,
        brand: String,
        mrp: Double,
        sellerPrice: Double,
        stock: Int,
        sku: String,
        specifications: String,
        description: String,
        iconType: String = "general",
        onComplete: (Boolean, String) -> Unit
    ) {
        addSellerProduct(
            categoryId = categoryId,
            subCategoryId = subCategoryId,
            brand = brand,
            name = title,
            description = description,
            mrp = mrp,
            sellerPrice = sellerPrice,
            stock = stock,
            sku = sku,
            warranty = "1 Year Brand Warranty",
            specifications = specifications,
            iconType = iconType,
            asDraft = false,
            onComplete = onComplete
        )
    }

    fun sellExistingCatalogProduct(
        catalogProduct: ProductEntity,
        sellerPrice: Double,
        stock: Int,
        onComplete: (Boolean, String) -> Unit
    ) {
        sellExistingProduct(
            baseProductId = catalogProduct.id,
            sellerPrice = sellerPrice,
            mrp = catalogProduct.mrp,
            stock = stock,
            sku = "SEL-${(1000..9999).random()}",
            customOffer = "Special Seller Offer",
            onComplete = onComplete
        )
    }

    fun addSellerProduct(
        categoryId: Long,
        subCategoryId: Long?,
        brand: String,
        name: String,
        description: String,
        mrp: Double,
        sellerPrice: Double,
        stock: Int,
        sku: String,
        warranty: String,
        specifications: String,
        iconType: String,
        asDraft: Boolean,
        onComplete: (Boolean, String) -> Unit
    ) {
        val sellerId = _currentSeller.value?.id ?: 1
        viewModelScope.launch {
            try {
                repository.createSellerProductSubmission(
                    sellerId = sellerId,
                    categoryId = categoryId,
                    subCategoryId = subCategoryId,
                    brand = brand.trim(),
                    name = name.trim(),
                    description = description.trim(),
                    mrp = mrp,
                    sellerPrice = sellerPrice,
                    stock = stock,
                    sku = sku.trim(),
                    warranty = warranty.trim(),
                    specifications = specifications.trim(),
                    iconType = iconType,
                    asDraft = asDraft
                )
                val msg = if (asDraft) "Product saved as Draft." else "Product submitted for Admin Review."
                _actionMessage.value = msg
                onComplete(true, msg)
            } catch (e: Exception) {
                onComplete(false, e.message ?: "Failed to add product.")
            }
        }
    }

    fun sellExistingProduct(
        baseProductId: Long,
        sellerPrice: Double,
        mrp: Double,
        stock: Int,
        sku: String,
        customOffer: String,
        onComplete: (Boolean, String) -> Unit
    ) {
        val sellerId = _currentSeller.value?.id ?: 1
        viewModelScope.launch {
            try {
                repository.sellExistingProduct(
                    sellerId = sellerId,
                    baseProductId = baseProductId,
                    sellerPrice = sellerPrice,
                    mrp = mrp,
                    stock = stock,
                    sku = sku.trim(),
                    customOffer = customOffer.trim()
                )
                val msg = "Product offer listed successfully!"
                _actionMessage.value = msg
                onComplete(true, msg)
            } catch (e: Exception) {
                onComplete(false, e.message ?: "Failed to list existing product.")
            }
        }
    }

    fun clearActionMessage() {
        _actionMessage.value = null
    }

    fun logout() {
        _currentSeller.value = null
        _authState.value = SellerAuthState.Idle
    }
}
