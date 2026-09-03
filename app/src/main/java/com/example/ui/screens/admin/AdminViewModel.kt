package com.example.ui.screens.admin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.SampurnaDatabase
import com.example.data.local.entity.AdminUserEntity
import com.example.data.local.entity.CategoryBannerEntity
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.DeliveryPartnerEntity
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

sealed interface AdminAuthState {
    object LoggedOut : AdminAuthState
    object Loading : AdminAuthState
    data class Authenticated(val admin: AdminUserEntity) : AdminAuthState
    data class Error(val message: String) : AdminAuthState
}

enum class AdminTab {
    DASHBOARD,
    CATEGORIES,
    SUB_CATEGORIES,
    BANNERS,
    PRODUCTS,
    SELLERS,
    DELIVERY_BOYS,
    APPROVALS,
    COMMISSION,
    ORDERS,
    SETTINGS
}

class AdminViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SampurnaRepository(SampurnaDatabase.getDatabase(application))

    private val _authState = MutableStateFlow<AdminAuthState>(AdminAuthState.LoggedOut)
    val authState: StateFlow<AdminAuthState> = _authState.asStateFlow()

    private val _selectedTab = MutableStateFlow(AdminTab.DASHBOARD)
    val selectedTab: StateFlow<AdminTab> = _selectedTab.asStateFlow()

    private val _adminToast = MutableStateFlow<String?>(null)
    val adminToast: StateFlow<String?> = _adminToast.asStateFlow()

    private val _commissionRate = MutableStateFlow<Double>(2.0)
    val commissionRate: StateFlow<Double> = _commissionRate.asStateFlow()

    // Statistics Flows
    val totalCategoriesCount: StateFlow<Int> = repository.getCategoriesCount()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val totalSubCategoriesCount: StateFlow<Int> = repository.getSubCategoriesCount()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val totalBannersCount: StateFlow<Int> = repository.getTotalBannersCount()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val activeBannersCount: StateFlow<Int> = repository.getActiveBannersCount()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val totalProductsCount: StateFlow<Int> = repository.getTotalProductsCount()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val activeProductsCount: StateFlow<Int> = repository.getActiveProductsCount()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    // Data lists for admin management
    val allCategories: StateFlow<List<CategoryEntity>> = repository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val allSubCategories: StateFlow<List<SubCategoryEntity>> = repository.getAllSubCategories()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val allBanners: StateFlow<List<CategoryBannerEntity>> = repository.getAllBanners()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val allProducts: StateFlow<List<ProductEntity>> = repository.getAllProducts()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val allSellers: StateFlow<List<SellerEntity>> = repository.getAllSellersFlow()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val allDeliveryBoys: StateFlow<List<DeliveryPartnerEntity>> = repository.getAllDeliveryBoysFlow()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val pendingSellers: StateFlow<List<SellerEntity>> = repository.getPendingSellersFlow()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val pendingDeliveryBoys: StateFlow<List<DeliveryPartnerEntity>> = repository.getPendingDeliveryBoysFlow()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val pendingApprovalProducts: StateFlow<List<ProductEntity>> = repository.getPendingApprovalProductsFlow()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val sellerSignupLink: String = "https://sampurna.in/seller/signup"
    val deliveryBoySignupLink: String = "https://sampurna.in/delivery/signup"

    init {
        viewModelScope.launch {
            _commissionRate.value = repository.getCommissionPercent()
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AdminAuthState.Error("Please enter email and password.")
            return
        }

        viewModelScope.launch {
            _authState.value = AdminAuthState.Loading
            val admin = repository.authenticateAdmin(email.trim(), password.trim())
            if (admin != null) {
                _authState.value = AdminAuthState.Authenticated(admin)
                _adminToast.value = "Welcome, ${admin.name}!"
            } else {
                _authState.value = AdminAuthState.Error("Invalid admin credentials.")
            }
        }
    }

    fun logout() {
        _authState.value = AdminAuthState.LoggedOut
        _selectedTab.value = AdminTab.DASHBOARD
    }

    fun selectTab(tab: AdminTab) {
        _selectedTab.value = tab
    }

    fun clearToast() {
        _adminToast.value = null
    }

    // Category Operations
    fun addCategory(name: String, iconType: String, description: String, displayOrder: Int, iconUrl: String? = null) {
        viewModelScope.launch {
            repository.insertCategory(
                CategoryEntity(
                    name = name.trim(),
                    iconType = iconType.trim(),
                    iconUrl = iconUrl?.trim()?.ifBlank { null },
                    description = description.trim(),
                    displayOrder = displayOrder
                )
            )
            _adminToast.value = "Category \"$name\" added successfully!"
        }
    }

    fun updateCategory(category: CategoryEntity) {
        viewModelScope.launch {
            repository.updateCategory(category.copy(updatedAt = System.currentTimeMillis()))
            _adminToast.value = "Category \"${category.name}\" updated!"
        }
    }

    fun deleteCategory(category: CategoryEntity) {
        viewModelScope.launch {
            repository.deleteCategory(category)
            _adminToast.value = "Category deleted!"
        }
    }

    fun toggleCategoryActive(category: CategoryEntity) {
        viewModelScope.launch {
            repository.toggleCategoryActive(category.id, !category.isActive)
            _adminToast.value = "Category status updated!"
        }
    }

    // SubCategory Operations
    fun addSubCategory(categoryId: Long, name: String, iconType: String, displayOrder: Int, imageUrl: String? = null) {
        viewModelScope.launch {
            repository.insertSubCategory(
                SubCategoryEntity(
                    categoryId = categoryId,
                    name = name.trim(),
                    iconType = iconType.trim(),
                    imageUrl = imageUrl?.trim()?.ifBlank { null },
                    displayOrder = displayOrder
                )
            )
            _adminToast.value = "Sub-Category \"$name\" added!"
        }
    }

    fun updateSubCategory(subCategory: SubCategoryEntity) {
        viewModelScope.launch {
            repository.updateSubCategory(subCategory.copy(updatedAt = System.currentTimeMillis()))
            _adminToast.value = "Sub-Category \"${subCategory.name}\" updated!"
        }
    }

    fun deleteSubCategory(subCategory: SubCategoryEntity) {
        viewModelScope.launch {
            repository.deleteSubCategory(subCategory)
            _adminToast.value = "Sub-Category deleted!"
        }
    }

    fun toggleSubCategoryActive(subCategory: SubCategoryEntity) {
        viewModelScope.launch {
            repository.toggleSubCategoryActive(subCategory.id, !subCategory.isActive)
            _adminToast.value = "Sub-Category status updated!"
        }
    }

    // Banner Operations
    fun addBanner(
        categoryId: Long?,
        title: String,
        subtitle: String?,
        tag: String?,
        discountText: String?,
        bannerType: String,
        displayOrder: Int
    ) {
        viewModelScope.launch {
            repository.insertBanner(
                CategoryBannerEntity(
                    categoryId = categoryId,
                    title = title.trim(),
                    subtitle = subtitle?.trim(),
                    tag = tag?.trim() ?: "SPECIAL OFFER",
                    discountText = discountText?.trim() ?: "UP TO 50% OFF",
                    bannerType = bannerType.trim(),
                    displayOrder = displayOrder
                )
            )
            _adminToast.value = "Banner \"$title\" added!"
        }
    }

    fun updateBanner(banner: CategoryBannerEntity) {
        viewModelScope.launch {
            repository.updateBanner(banner.copy(updatedAt = System.currentTimeMillis()))
            _adminToast.value = "Banner \"${banner.title}\" updated!"
        }
    }

    fun deleteBanner(banner: CategoryBannerEntity) {
        viewModelScope.launch {
            repository.deleteBanner(banner)
            _adminToast.value = "Banner deleted!"
        }
    }

    fun toggleBannerActive(banner: CategoryBannerEntity) {
        viewModelScope.launch {
            repository.toggleBannerActive(banner.id, !banner.isActive)
            _adminToast.value = "Banner status updated!"
        }
    }

    // Product Operations
    fun addProduct(
        categoryId: Long,
        subCategoryId: Long?,
        name: String,
        iconType: String,
        price: Double,
        mrp: Double,
        discount: Int,
        rating: Float,
        tag: String
    ) {
        viewModelScope.launch {
            repository.insertProduct(
                ProductEntity(
                    categoryId = categoryId,
                    subCategoryId = subCategoryId,
                    name = name.trim(),
                    iconType = iconType.trim(),
                    price = price,
                    mrp = mrp,
                    discount = discount,
                    rating = rating,
                    tag = tag.trim()
                )
            )
            _adminToast.value = "Product \"$name\" added!"
        }
    }

    fun toggleProductActive(product: ProductEntity) {
        viewModelScope.launch {
            repository.toggleProductActive(product.id, !product.isActive)
            _adminToast.value = "Product status updated!"
        }
    }

    fun deleteProduct(product: ProductEntity) {
        viewModelScope.launch {
            repository.deleteProduct(product)
            _adminToast.value = "Product deleted!"
        }
    }

    // Seller Operations
    fun addSeller(
        sellerName: String,
        businessName: String,
        mobile: String,
        email: String,
        businessAddress: String,
        storeInfo: String,
        assignedCategoryIds: List<Long>,
        assignedSubCategoryIds: List<Long>
    ) {
        viewModelScope.launch {
            val seller = SellerEntity(
                sellerName = sellerName.trim(),
                businessName = businessName.trim(),
                mobile = mobile.trim(),
                email = email.trim(),
                businessAddress = businessAddress.trim(),
                storeInfo = storeInfo.trim(),
                status = "ACTIVE"
            )
            repository.createSellerWithAssignments(seller, assignedCategoryIds, assignedSubCategoryIds)
            _adminToast.value = "Seller \"$businessName\" created and categories assigned!"
        }
    }

    // Review & Approval Operations
    fun approveSeller(sellerId: Long) {
        viewModelScope.launch {
            repository.approveSeller(sellerId)
            _adminToast.value = "Merchant account approved & verified for business!"
        }
    }

    fun rejectSeller(sellerId: Long, reason: String = "Registration rejected by Admin") {
        viewModelScope.launch {
            repository.rejectSeller(sellerId, reason)
            _adminToast.value = "Merchant registration rejected."
        }
    }

    fun approveDeliveryBoy(deliveryBoyId: Long) {
        viewModelScope.launch {
            repository.approveDeliveryBoy(deliveryBoyId)
            _adminToast.value = "Delivery Boy verified and activated for Keonjhar fleet!"
        }
    }

    fun rejectDeliveryBoy(deliveryBoyId: Long) {
        viewModelScope.launch {
            repository.rejectDeliveryBoy(deliveryBoyId)
            _adminToast.value = "Delivery Partner registration rejected/removed."
        }
    }

    fun approveProduct(productId: Long) {
        viewModelScope.launch {
            repository.reviewProductByAdmin(productId, approved = true)
            _adminToast.value = "Product approved and published Live!"
        }
    }

    fun rejectProduct(productId: Long, reason: String) {
        viewModelScope.launch {
            repository.reviewProductByAdmin(productId, approved = false, rejectionReason = reason)
            _adminToast.value = "Product rejected. Feedback sent to seller."
        }
    }

    // Commission Configuration
    fun setCommissionPercent(percent: Double) {
        viewModelScope.launch {
            repository.setCommissionPercent(percent)
            _commissionRate.value = percent
            _adminToast.value = "Sampurna commission rate updated to $percent%."
        }
    }

    // Part 3: Orders & System Settings
    val allOrders: StateFlow<List<com.example.data.local.entity.OrderEntity>> = repository.getAllOrdersFlow()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val allCancellationAudits: StateFlow<List<com.example.data.local.entity.CustomerProductCancellationEntity>> =
        repository.getAllCancellationAuditsFlow()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun updateDeliveryConfig(baseKm: Double, baseFee: Double, extraPerKm: Double) {
        viewModelScope.launch {
            repository.saveDeliveryConfig(baseKm, baseFee, extraPerKm)
            _adminToast.value = "Delivery rate formula updated successfully."
        }
    }

    fun updateCodConfig(enabled: Boolean, codFee: Double) {
        viewModelScope.launch {
            repository.saveCodConfig(enabled, codFee)
            _adminToast.value = "COD settings saved."
        }
    }

    fun updatePaymentGatewayConfig(
        gateway: String,
        keyId: String,
        keySecret: String,
        webhookSecret: String,
        isTestMode: Boolean
    ) {
        viewModelScope.launch {
            repository.savePaymentGatewayConfig(
                gateway = gateway,
                keyId = keyId,
                keySecret = keySecret,
                webhookSecret = webhookSecret,
                isTestMode = isTestMode
            )
            _adminToast.value = "Payment Gateway ($gateway) credentials updated."
        }
    }

    fun resetCustomerCancellationPenalty(customerId: Long, productId: Long) {
        viewModelScope.launch {
            repository.resetCancellationAudit(customerId, productId)
            _adminToast.value = "Customer cancellation count reset. 1% penalty removed."
        }
    }

    // ==========================================
    // DELIVERY BOY FLEET MANAGEMENT ACTIONS
    // ==========================================
    fun createDeliveryBoy(
        name: String,
        mobile: String,
        email: String,
        password: String,
        vehicleType: String,
        vehicleNumber: String,
        licenseNumber: String,
        assignedHub: String,
        emergencyContact: String?
    ) {
        if (name.isBlank() || mobile.isBlank()) {
            _adminToast.value = "Please enter delivery partner name and mobile."
            return
        }

        viewModelScope.launch {
            val deliveryBoy = DeliveryPartnerEntity(
                name = name.trim(),
                mobile = mobile.trim(),
                email = email.trim(),
                passwordHash = if (password.isNotBlank()) password.trim() else "delivery123",
                vehicleType = vehicleType.trim(),
                vehicleNumber = vehicleNumber.trim().uppercase(),
                licenseNumber = licenseNumber.trim().uppercase(),
                assignedHub = if (assignedHub.isNotBlank()) assignedHub.trim() else "Keonjhar Central Hub",
                emergencyContact = emergencyContact?.trim(),
                isActive = true,
                isOnDuty = true,
                totalDeliveries = 0,
                rating = 5.0f
            )
            val id = repository.createDeliveryBoy(deliveryBoy)
            if (id > 0) {
                _adminToast.value = "Delivery boy account created for ${deliveryBoy.name}."
            } else {
                _adminToast.value = "Failed to create delivery boy account. Mobile or Email may already exist."
            }
        }
    }

    fun updateDeliveryBoy(deliveryBoy: DeliveryPartnerEntity) {
        viewModelScope.launch {
            repository.updateDeliveryBoy(deliveryBoy.copy(updatedAt = System.currentTimeMillis()))
            _adminToast.value = "Delivery boy details updated for ${deliveryBoy.name}."
        }
    }

    fun toggleDeliveryBoyActive(deliveryBoy: DeliveryPartnerEntity) {
        val newStatus = !deliveryBoy.isActive
        viewModelScope.launch {
            repository.toggleDeliveryBoyActive(deliveryBoy.id, newStatus)
            _adminToast.value = "${deliveryBoy.name} is now ${if (newStatus) "Active & Approved" else "Suspended / Inactive"}."
        }
    }

    fun toggleDeliveryBoyDuty(deliveryBoy: DeliveryPartnerEntity) {
        val newDuty = !deliveryBoy.isOnDuty
        viewModelScope.launch {
            repository.toggleDeliveryBoyDuty(deliveryBoy.id, newDuty)
            _adminToast.value = "${deliveryBoy.name} is now ${if (newDuty) "ON DUTY (Online)" else "OFF DUTY (Offline)"}."
        }
    }

    fun deleteDeliveryBoy(deliveryBoy: DeliveryPartnerEntity) {
        viewModelScope.launch {
            repository.deleteDeliveryBoy(deliveryBoy)
            _adminToast.value = "Delivery boy account removed."
        }
    }
}
