package com.example.ui.screens.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.SampurnaDatabase
import com.example.data.local.entity.AdminUserEntity
import com.example.data.local.entity.UserEntity
import com.example.data.repository.AuthResult
import com.example.data.repository.SampurnaRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class CustomerAuthState {
    object Idle : CustomerAuthState()
    object Loading : CustomerAuthState()
    data class Authenticated(val user: UserEntity) : CustomerAuthState()
    data class AdminAuthenticated(val admin: AdminUserEntity) : CustomerAuthState()
    data class Error(val message: String) : CustomerAuthState()
}

sealed class SignupStep {
    object Form : SignupStep()
    data class OtpVerification(val email: String, val name: String, val mobile: String, val secondsRemaining: Int = 60) : SignupStep()
    data class SetPassword(val email: String, val name: String, val mobile: String) : SignupStep()
}

class CustomerAuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SampurnaRepository = SampurnaRepository(SampurnaDatabase.getDatabase(application))

    private val _authState = MutableStateFlow<CustomerAuthState>(CustomerAuthState.Idle)
    val authState: StateFlow<CustomerAuthState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    private val _currentAdmin = MutableStateFlow<AdminUserEntity?>(null)
    val currentAdmin: StateFlow<AdminUserEntity?> = _currentAdmin.asStateFlow()

    private val _signupStep = MutableStateFlow<SignupStep>(SignupStep.Form)
    val signupStep: StateFlow<SignupStep> = _signupStep.asStateFlow()

    private val _generatedDemoOtp = MutableStateFlow<String?>(null)
    val generatedDemoOtp: StateFlow<String?> = _generatedDemoOtp.asStateFlow()

    private var otpTimerJob: Job? = null

    init {
        // By default, no user is auto-logged in. User logs in manually via Login / Signup page.
    }

    fun loginCustomer(identifier: String, passwordAttempt: String) {
        val cleanId = identifier.trim()
        val cleanPass = passwordAttempt.trim()

        if (cleanId.isBlank()) {
            _authState.value = CustomerAuthState.Error("Please enter your Mobile Number or Email.")
            return
        }
        if (cleanPass.isBlank()) {
            _authState.value = CustomerAuthState.Error("Please enter your password.")
            return
        }

        viewModelScope.launch {
            _authState.value = CustomerAuthState.Loading

            // 1. Check if Admin credentials (6370805780 / pranayakhuntia85@gmail.com with Pranaya@1997)
            val admin = repository.authenticateAdmin(cleanId, cleanPass)
            if (admin != null) {
                _currentAdmin.value = admin
                _authState.value = CustomerAuthState.AdminAuthenticated(admin)
                return@launch
            }

            // 2. Check Standard Account Login
            when (val result = repository.loginCustomer(cleanId, cleanPass)) {
                is AuthResult.Success -> {
                    _currentUser.value = result.data
                    _currentAdmin.value = null
                    _authState.value = CustomerAuthState.Authenticated(result.data)
                }
                is AuthResult.Error -> {
                    _authState.value = CustomerAuthState.Error(result.message)
                }
            }
        }
    }

    fun startSignupFlow(name: String, mobile: String, email: String) {
        if (name.isBlank()) {
            _authState.value = CustomerAuthState.Error("Please enter your full name.")
            return
        }
        if (mobile.isBlank() || mobile.length < 10) {
            _authState.value = CustomerAuthState.Error("Valid 10-digit Mobile Number is mandatory.")
            return
        }
        if (email.isBlank() || !email.contains("@")) {
            _authState.value = CustomerAuthState.Error("Valid Email ID is mandatory for verification.")
            return
        }

        // Generate OTP
        val otp = repository.generateEmailOtp(email)
        _generatedDemoOtp.value = otp
        _signupStep.value = SignupStep.OtpVerification(email = email, name = name, mobile = mobile, secondsRemaining = 60)
        startOtpCountdown(email, name, mobile)
    }

    private fun startOtpCountdown(email: String, name: String, mobile: String) {
        otpTimerJob?.cancel()
        otpTimerJob = viewModelScope.launch {
            var timeLeft = 60
            while (timeLeft > 0) {
                delay(1000)
                timeLeft -= 1
                if (_signupStep.value is SignupStep.OtpVerification) {
                    _signupStep.value = SignupStep.OtpVerification(
                        email = email,
                        name = name,
                        mobile = mobile,
                        secondsRemaining = timeLeft
                    )
                }
            }
        }
    }

    fun resendOtp(email: String, name: String, mobile: String) {
        val otp = repository.generateEmailOtp(email)
        _generatedDemoOtp.value = otp
        startOtpCountdown(email, name, mobile)
    }

    fun verifyOtpAndProceed(enteredOtp: String, email: String, name: String, mobile: String): Boolean {
        val isValid = repository.verifyEmailOtp(email, enteredOtp)
        if (isValid) {
            _signupStep.value = SignupStep.SetPassword(email = email, name = name, mobile = mobile)
            return true
        } else {
            _authState.value = CustomerAuthState.Error("Invalid or expired OTP. Please check the code and try again.")
            return false
        }
    }

    fun completeSignup(name: String, mobile: String, email: String, password: String, confirmPass: String) {
        if (password.isBlank() || password.length < 6) {
            _authState.value = CustomerAuthState.Error("Password must be at least 6 characters.")
            return
        }
        if (password != confirmPass) {
            _authState.value = CustomerAuthState.Error("Passwords do not match.")
            return
        }

        viewModelScope.launch {
            _authState.value = CustomerAuthState.Loading
            when (val result = repository.registerCustomer(name, mobile, email, password)) {
                is AuthResult.Success -> {
                    _currentUser.value = result.data
                    _authState.value = CustomerAuthState.Authenticated(result.data)
                    _signupStep.value = SignupStep.Form
                }
                is AuthResult.Error -> {
                    _authState.value = CustomerAuthState.Error(result.message)
                }
            }
        }
    }

    fun resetPassword(identifier: String, newPass: String, confirmPass: String, onComplete: (Boolean, String) -> Unit) {
        if (identifier.isBlank()) {
            onComplete(false, "Please enter your registered Mobile or Email.")
            return
        }
        if (newPass.length < 6) {
            onComplete(false, "Password must be at least 6 characters.")
            return
        }
        if (newPass != confirmPass) {
            onComplete(false, "Passwords do not match.")
            return
        }

        viewModelScope.launch {
            when (val res = repository.resetCustomerPassword(identifier, newPass)) {
                is AuthResult.Success -> onComplete(true, "Password updated successfully. Please login.")
                is AuthResult.Error -> onComplete(false, res.message)
            }
        }
    }

    fun resetSignupStep() {
        _signupStep.value = SignupStep.Form
    }

    fun clearError() {
        if (_authState.value is CustomerAuthState.Error) {
            _authState.value = CustomerAuthState.Idle
        }
    }

    fun logout() {
        _currentUser.value = null
        _currentAdmin.value = null
        _authState.value = CustomerAuthState.Idle
    }
}
