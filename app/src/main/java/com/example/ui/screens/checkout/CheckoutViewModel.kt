package com.example.ui.screens.checkout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.SampurnaDatabase
import com.example.data.local.entity.AddressEntity
import com.example.data.local.entity.OfferEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.UserEntity
import com.example.data.repository.CartItemWithProduct
import com.example.data.repository.DeliveryFeeQuote
import com.example.data.repository.PaymentGatewayConfig
import com.example.data.repository.ProductCancellationAnalysis
import com.example.data.repository.SampurnaRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class CheckoutStep {
    object Address : CheckoutStep()
    object DeliveryAndReview : CheckoutStep()
    object Payment : CheckoutStep()
    data class Success(val order: OrderEntity) : CheckoutStep()
}

class CheckoutViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SampurnaRepository(SampurnaDatabase.getDatabase(application))

    private val _currentStep = MutableStateFlow<CheckoutStep>(CheckoutStep.Address)
    val currentStep: StateFlow<CheckoutStep> = _currentStep.asStateFlow()

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    private val _savedAddresses = MutableStateFlow<List<AddressEntity>>(emptyList())
    val savedAddresses: StateFlow<List<AddressEntity>> = _savedAddresses.asStateFlow()

    private val _selectedAddress = MutableStateFlow<AddressEntity?>(null)
    val selectedAddress: StateFlow<AddressEntity?> = _selectedAddress.asStateFlow()

    private val _checkoutItems = MutableStateFlow<List<CartItemWithProduct>>(emptyList())
    val checkoutItems: StateFlow<List<CartItemWithProduct>> = _checkoutItems.asStateFlow()

    private val _deliveryQuote = MutableStateFlow<DeliveryFeeQuote?>(null)
    val deliveryQuote: StateFlow<DeliveryFeeQuote?> = _deliveryQuote.asStateFlow()

    private val _cancellationAnalyses = MutableStateFlow<List<ProductCancellationAnalysis>>(emptyList())
    val cancellationAnalyses: StateFlow<List<ProductCancellationAnalysis>> = _cancellationAnalyses.asStateFlow()

    private val _gatewayConfig = MutableStateFlow<PaymentGatewayConfig?>(null)
    val gatewayConfig: StateFlow<PaymentGatewayConfig?> = _gatewayConfig.asStateFlow()

    // Payment state
    private val _selectedPaymentMethod = MutableStateFlow("UPI") // UPI, DEBIT_CARD, CREDIT_CARD, COD
    val selectedPaymentMethod: StateFlow<String> = _selectedPaymentMethod.asStateFlow()

    private val _selectedUpiApp = MutableStateFlow("Google Pay") // Google Pay, PhonePe, Paytm, Other UPI
    val selectedUpiApp: StateFlow<String> = _selectedUpiApp.asStateFlow()

    private val _upiIdInput = MutableStateFlow("")
    val upiIdInput: StateFlow<String> = _upiIdInput.asStateFlow()

    // Card Details
    private val _cardNumber = MutableStateFlow("")
    val cardNumber: StateFlow<String> = _cardNumber.asStateFlow()

    private val _cardExpiry = MutableStateFlow("")
    val cardExpiry: StateFlow<String> = _cardExpiry.asStateFlow()

    private val _cardCvv = MutableStateFlow("")
    val cardCvv: StateFlow<String> = _cardCvv.asStateFlow()

    private val _cardHolderName = MutableStateFlow("")
    val cardHolderName: StateFlow<String> = _cardHolderName.asStateFlow()

    // Coupon
    private val _appliedCoupon = MutableStateFlow<OfferEntity?>(null)
    val appliedCoupon: StateFlow<OfferEntity?> = _appliedCoupon.asStateFlow()

    // Processing & Dialog state
    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _showOtpDialog = MutableStateFlow(false)
    val showOtpDialog: StateFlow<Boolean> = _showOtpDialog.asStateFlow()

    private val _otpInput = MutableStateFlow("")
    val otpInput: StateFlow<String> = _otpInput.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        initializeCheckout()
    }

    fun initializeCheckout(directProduct: ProductEntity? = null, directQty: Int = 1) {
        viewModelScope.launch {
            val user = repository.getUserByMobileOrEmail("9876543210")
            _currentUser.value = user

            // Load gateway configs
            val config = repository.getPaymentGatewayConfig()
            _gatewayConfig.value = config

            if (user != null) {
                // Load Addresses
                repository.getAddressesForUser(user.id).collect { addresses ->
                    _savedAddresses.value = addresses
                    if (_selectedAddress.value == null) {
                        _selectedAddress.value = addresses.find { it.isDefault } ?: addresses.firstOrNull()
                    }
                    updateDeliveryAndCancellationQuotes()
                }
            }

            // If direct product specified, use single product item, else load from Cart
            if (directProduct != null) {
                val seller = if (directProduct.sellerId != null) repository.getSellerById(directProduct.sellerId) else null
                val item = CartItemWithProduct(
                    cartItem = com.example.data.local.entity.CartItemEntity(
                        userId = user?.id ?: 1L,
                        productId = directProduct.id,
                        sellerId = directProduct.sellerId ?: 1L,
                        quantity = directQty,
                        unitPrice = directProduct.price,
                        unitMrp = directProduct.mrp
                    ),
                    product = directProduct,
                    seller = seller
                )
                _checkoutItems.value = listOf(item)
                updateDeliveryAndCancellationQuotes()
            } else if (user != null) {
                repository.getCartItems(user.id).collect { items ->
                    _checkoutItems.value = items
                    updateDeliveryAndCancellationQuotes()
                }
            }
        }
    }

    fun selectAddress(address: AddressEntity) {
        _selectedAddress.value = address
        updateDeliveryAndCancellationQuotes()
    }

    fun setStep(step: CheckoutStep) {
        _currentStep.value = step
    }

    fun setPaymentMethod(method: String) {
        _selectedPaymentMethod.value = method
    }

    fun setUpiApp(app: String) {
        _selectedUpiApp.value = app
    }

    fun setUpiId(upiId: String) {
        _upiIdInput.value = upiId
    }

    fun setCardDetails(number: String, expiry: String, cvv: String, name: String) {
        _cardNumber.value = number
        _cardExpiry.value = expiry
        _cardCvv.value = cvv
        _cardHolderName.value = name
    }

    fun setOtp(otp: String) {
        _otpInput.value = otp
    }

    fun dismissOtpDialog() {
        _showOtpDialog.value = false
        _isProcessing.value = false
    }

    private fun updateDeliveryAndCancellationQuotes() {
        val address = _selectedAddress.value ?: return
        val items = _checkoutItems.value
        val user = _currentUser.value ?: return

        viewModelScope.launch {
            val firstSeller = items.firstOrNull()?.seller
            val quote = repository.getDeliveryQuote(firstSeller, address)
            _deliveryQuote.value = quote

            // Check repeated cancellation for all items
            val analyses = items.mapNotNull { item ->
                val p = item.product ?: return@mapNotNull null
                repository.checkRepeatedCancellation(user.id, p.id, p.price)
            }
            _cancellationAnalyses.value = analyses
        }
    }

    fun calculateItemsSubtotal(): Double {
        return _checkoutItems.value.sumOf { (it.product?.price ?: it.cartItem.unitPrice) * it.cartItem.quantity }
    }

    fun calculateDeliveryCharge(): Double {
        return _deliveryQuote.value?.totalDeliveryCharge ?: 10.0
    }

    fun calculateCodFee(): Double {
        val isCod = _selectedPaymentMethod.value == "COD"
        val config = _gatewayConfig.value
        return if (isCod && (config?.codEnabled != false)) (config?.codFee ?: 10.0) else 0.0
    }

    fun calculateCancellationAdjustment(): Double {
        return _cancellationAnalyses.value.filter { it.hasPenalty }.sumOf { it.penaltyAmount }
    }

    fun calculateGrandTotal(): Double {
        val subtotal = calculateItemsSubtotal()
        val delivery = calculateDeliveryCharge()
        val cod = calculateCodFee()
        val penalty = calculateCancellationAdjustment()
        return subtotal + delivery + cod + penalty
    }

    // Submit Order flow
    fun processPaymentAndPlaceOrder() {
        val address = _selectedAddress.value
        val user = _currentUser.value
        val items = _checkoutItems.value

        if (address == null) {
            _errorMessage.value = "Please select a delivery address."
            return
        }

        if (items.isEmpty()) {
            _errorMessage.value = "Your checkout items are empty."
            return
        }

        val method = _selectedPaymentMethod.value

        // Card validation
        if (method == "DEBIT_CARD" || method == "CREDIT_CARD") {
            if (_cardNumber.value.length < 12 || _cardExpiry.value.length < 4 || _cardCvv.value.length < 3) {
                _errorMessage.value = "Please enter valid Card details (Number, Expiry & CVV)"
                return
            }
            // Show Card 3D Secure OTP verification simulation
            _showOtpDialog.value = true
            return
        }

        // UPI or COD execution
        executeOrderCreation()
    }

    fun verifyCardOtpAndPlaceOrder() {
        if (_otpInput.value.length < 4) {
            _errorMessage.value = "Please enter valid 4 or 6 digit Bank OTP"
            return
        }
        _showOtpDialog.value = false
        executeOrderCreation()
    }

    private fun executeOrderCreation() {
        val address = _selectedAddress.value ?: return
        val user = _currentUser.value ?: return
        val items = _checkoutItems.value
        val method = _selectedPaymentMethod.value

        viewModelScope.launch {
            _isProcessing.value = true
            _errorMessage.value = null

            // Simulate secure backend payment verification gateway call
            delay(1500)

            try {
                val createdOrder = repository.placeOrder(
                    customer = user,
                    deliveryAddress = address,
                    items = items,
                    paymentMethod = method,
                    paymentUpiApp = if (method == "UPI") _selectedUpiApp.value else null,
                    paymentTransactionId = if (method == "COD") null else "TXN-SMP-${System.currentTimeMillis()}"
                )

                _isProcessing.value = false
                _currentStep.value = CheckoutStep.Success(createdOrder)
            } catch (e: Exception) {
                _isProcessing.value = false
                _errorMessage.value = "Order placement failed: ${e.localizedMessage}"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
