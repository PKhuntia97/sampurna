package com.example.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.example.R
import com.example.ui.theme.SampurnaDarkPurple
import com.example.ui.theme.SampurnaOrange
import com.example.ui.theme.SampurnaPrimaryPurple

@Composable
fun CustomerLoginScreen(
    viewModel: CustomerAuthViewModel,
    onLoginSuccess: () -> Unit,
    onAdminLoginSuccess: () -> Unit = {},
    onNavigateToSignup: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var identifier by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }

    val authState by viewModel.authState.collectAsState()

    // Handle authentication success
    LaunchedEffect(authState) {
        if (authState is CustomerAuthState.AdminAuthenticated) {
            onAdminLoginSuccess()
        } else if (authState is CustomerAuthState.Authenticated) {
            onLoginSuccess()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF9FAFB)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar with Back Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.testTag("login_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = SampurnaDarkPurple
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Branding Logo & Name
            Surface(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(20.dp)),
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_sampurna_logo),
                    contentDescription = "Sampurna Official Logo",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "SAMPURNA",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.5.sp,
                color = SampurnaDarkPurple
            )

            Text(
                text = "Your Trusted Shopping Destination",
                fontSize = 13.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Card Form
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Account Login",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "Enter your Mobile Number or Email to continue",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 20.dp)
                    )

                    // Error Message
                    if (authState is CustomerAuthState.Error) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Error",
                                    tint = Color(0xFFDC2626),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = (authState as CustomerAuthState.Error).message,
                                    fontSize = 12.sp,
                                    color = Color(0xFFDC2626)
                                )
                            }
                        }
                    }

                    // Mobile / Email input
                    OutlinedTextField(
                        value = identifier,
                        onValueChange = {
                            identifier = it
                            viewModel.clearError()
                        },
                        label = { Text("Mobile Number or Email") },
                        placeholder = { Text("e.g. 6370805780 or name@mail.com") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = "User",
                                tint = SampurnaPrimaryPurple
                            )
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("login_identifier_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SampurnaPrimaryPurple,
                            unfocusedBorderColor = Color(0xFFE2E8F0)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password input
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            viewModel.clearError()
                        },
                        label = { Text("Password") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password",
                                tint = SampurnaPrimaryPurple
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle password visibility"
                                )
                            }
                        },
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("login_password_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SampurnaPrimaryPurple,
                            unfocusedBorderColor = Color(0xFFE2E8F0)
                        )
                    )

                    // Forgot Password link
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = { showForgotPasswordDialog = true },
                            modifier = Modifier.testTag("login_forgot_password_btn")
                        ) {
                            Text(
                                text = "Forgot Password?",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SampurnaOrange
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Login Button
                    Button(
                        onClick = {
                            viewModel.loginCustomer(identifier, password)
                        },
                        enabled = authState !is CustomerAuthState.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("login_submit_btn"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SampurnaPrimaryPurple
                        )
                    ) {
                        if (authState is CustomerAuthState.Loading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Login",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(16.dp))

                    // Create Account CTA
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "New user? ",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "Sign Up / Create Account",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SampurnaPrimaryPurple,
                            modifier = Modifier
                                .clickable { onNavigateToSignup() }
                                .testTag("login_goto_signup_link")
                        )
                    }
                }
            }
        }
    }

    // Forgot Password Dialog
    if (showForgotPasswordDialog) {
        var resetIdentifier by remember { mutableStateOf("") }
        var newPass by remember { mutableStateOf("") }
        var confirmNewPass by remember { mutableStateOf("") }
        var resetMsg by remember { mutableStateOf<String?>(null) }
        var isSuccess by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showForgotPasswordDialog = false },
            title = {
                Text(
                    text = "Reset Password",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = SampurnaDarkPurple
                )
            },
            text = {
                Column {
                    Text(
                        text = "Enter your registered Mobile Number or Email to reset your password.",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (resetMsg != null) {
                        Text(
                            text = resetMsg!!,
                            fontSize = 12.sp,
                            color = if (isSuccess) Color(0xFF16A34A) else Color(0xFFDC2626),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    OutlinedTextField(
                        value = resetIdentifier,
                        onValueChange = { resetIdentifier = it },
                        label = { Text("Mobile or Email") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newPass,
                        onValueChange = { newPass = it },
                        label = { Text("New Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = confirmNewPass,
                        onValueChange = { confirmNewPass = it },
                        label = { Text("Confirm New Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetPassword(resetIdentifier, newPass, confirmNewPass) { success, msg ->
                            isSuccess = success
                            resetMsg = msg
                            if (success) {
                                identifier = resetIdentifier
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SampurnaPrimaryPurple)
                ) {
                    Text("Reset Password")
                }
            },
            dismissButton = {
                TextButton(onClick = { showForgotPasswordDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun CustomerSignupScreen(
    viewModel: CustomerAuthViewModel,
    onSignupSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var otpInput by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val authState by viewModel.authState.collectAsState()
    val signupStep by viewModel.signupStep.collectAsState()
    val demoOtp by viewModel.generatedDemoOtp.collectAsState()

    if (authState is CustomerAuthState.Authenticated) {
        onSignupSuccess()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF9FAFB)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (signupStep !is SignupStep.Form) {
                            viewModel.resetSignupStep()
                        } else {
                            onNavigateBack()
                        }
                    },
                    modifier = Modifier.testTag("signup_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = SampurnaDarkPurple
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                modifier = Modifier
                    .size(68.dp)
                    .clip(RoundedCornerShape(18.dp)),
                color = Color.White,
                shadowElevation = 3.dp
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_sampurna_logo),
                    contentDescription = "Sampurna Official Logo",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(3.dp)
                        .clip(RoundedCornerShape(15.dp)),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Create Account",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = SampurnaDarkPurple
            )
            Text(
                text = "Sign up to start shopping on Sampurna",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
            )

            // Step Progress Indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StepIndicator(step = 1, title = "Details", isActive = true, isCompleted = signupStep !is SignupStep.Form)
                Spacer(modifier = Modifier.width(8.dp))
                HorizontalDivider(modifier = Modifier.width(32.dp), color = if (signupStep !is SignupStep.Form) SampurnaPrimaryPurple else Color(0xFFE2E8F0))
                Spacer(modifier = Modifier.width(8.dp))
                StepIndicator(step = 2, title = "Email OTP", isActive = signupStep !is SignupStep.Form, isCompleted = signupStep is SignupStep.SetPassword)
                Spacer(modifier = Modifier.width(8.dp))
                HorizontalDivider(modifier = Modifier.width(32.dp), color = if (signupStep is SignupStep.SetPassword) SampurnaPrimaryPurple else Color(0xFFE2E8F0))
                Spacer(modifier = Modifier.width(8.dp))
                StepIndicator(step = 3, title = "Password", isActive = signupStep is SignupStep.SetPassword, isCompleted = false)
            }

            // Error Display
            if (authState is CustomerAuthState.Error) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Error",
                            tint = Color(0xFFDC2626),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = (authState as CustomerAuthState.Error).message,
                            fontSize = 12.sp,
                            color = Color(0xFFDC2626)
                        )
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (val currentStep = signupStep) {
                        is SignupStep.Form -> {
                            // Step 1: Input Details
                            OutlinedTextField(
                                value = name,
                                onValueChange = {
                                    name = it
                                    viewModel.clearError()
                                },
                                label = { Text("Full Name *") },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Person, contentDescription = "Name", tint = SampurnaPrimaryPurple)
                                },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("signup_name_input"),
                                shape = RoundedCornerShape(10.dp)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            OutlinedTextField(
                                value = mobile,
                                onValueChange = {
                                    if (it.length <= 10 && it.all { char -> char.isDigit() }) {
                                        mobile = it
                                        viewModel.clearError()
                                    }
                                },
                                label = { Text("Mobile Number (Mandatory) *") },
                                placeholder = { Text("10-digit number") },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Phone, contentDescription = "Phone", tint = SampurnaPrimaryPurple)
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("signup_mobile_input"),
                                shape = RoundedCornerShape(10.dp)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            OutlinedTextField(
                                value = email,
                                onValueChange = {
                                    email = it
                                    viewModel.clearError()
                                },
                                label = { Text("Email ID (For OTP Verification) *") },
                                placeholder = { Text("e.g. yourname@gmail.com") },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Email, contentDescription = "Email", tint = SampurnaPrimaryPurple)
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("signup_email_input"),
                                shape = RoundedCornerShape(10.dp)
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    viewModel.startSignupFlow(name, mobile, email)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("signup_send_otp_btn"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SampurnaPrimaryPurple)
                            ) {
                                Text("Send Verification OTP", fontWeight = FontWeight.Bold)
                            }
                        }

                        is SignupStep.OtpVerification -> {
                            // Step 2: Email OTP Entry
                            Text(
                                text = "Verify Your Email Address",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = SampurnaDarkPurple
                            )
                            Text(
                                text = "We have sent a 6-digit OTP to\n${currentStep.email}",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 6.dp, bottom = 14.dp)
                            )

                            // OTP Simulation Helper Card for easy testing in emulator
                            if (demoOtp != null) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp)
                                        .clickable { otpInput = demoOtp!! },
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                                    border = CardDefaults.outlinedCardBorder()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Email,
                                            contentDescription = "OTP simulation",
                                            tint = Color(0xFF2563EB),
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = "Verification Code: $demoOtp",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = Color(0xFF1D4ED8)
                                            )
                                            Text(
                                                text = "Tap here to auto-fill OTP",
                                                fontSize = 11.sp,
                                                color = Color(0xFF3B82F6)
                                            )
                                        }
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = otpInput,
                                onValueChange = {
                                    if (it.length <= 6) {
                                        otpInput = it
                                        viewModel.clearError()
                                    }
                                },
                                label = { Text("Enter 6-Digit OTP") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("signup_otp_input"),
                                shape = RoundedCornerShape(10.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (currentStep.secondsRemaining > 0) {
                                    Text(
                                        text = "Resend OTP in ${currentStep.secondsRemaining}s",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                } else {
                                    TextButton(
                                        onClick = {
                                            viewModel.resendOtp(currentStep.email, currentStep.name, currentStep.mobile)
                                        }
                                    ) {
                                        Text(
                                            text = "Resend OTP",
                                            fontWeight = FontWeight.Bold,
                                            color = SampurnaOrange,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    viewModel.verifyOtpAndProceed(
                                        enteredOtp = otpInput,
                                        email = currentStep.email,
                                        name = currentStep.name,
                                        mobile = currentStep.mobile
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("signup_verify_otp_btn"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SampurnaPrimaryPurple)
                            ) {
                                Text("Verify & Proceed", fontWeight = FontWeight.Bold)
                            }
                        }

                        is SignupStep.SetPassword -> {
                            // Step 3: Set Password
                            Text(
                                text = "Set Account Password",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = SampurnaDarkPurple
                            )
                            Text(
                                text = "Create a secure password to protect your account.",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                            )

                            OutlinedTextField(
                                value = password,
                                onValueChange = {
                                    password = it
                                    viewModel.clearError()
                                },
                                label = { Text("Password (Min 6 characters)") },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Lock, contentDescription = "Password", tint = SampurnaPrimaryPurple)
                                },
                                trailingIcon = {
                                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                        Icon(
                                            imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = "Toggle visibility"
                                        )
                                    }
                                },
                                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("signup_password_input"),
                                shape = RoundedCornerShape(10.dp)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            OutlinedTextField(
                                value = confirmPassword,
                                onValueChange = {
                                    confirmPassword = it
                                    viewModel.clearError()
                                },
                                label = { Text("Confirm Password") },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Lock, contentDescription = "Confirm Password", tint = SampurnaPrimaryPurple)
                                },
                                visualTransformation = PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("signup_confirm_password_input"),
                                shape = RoundedCornerShape(10.dp)
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    viewModel.completeSignup(
                                        name = currentStep.name,
                                        mobile = currentStep.mobile,
                                        email = currentStep.email,
                                        password = password,
                                        confirmPass = confirmPassword
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("signup_submit_btn"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SampurnaPrimaryPurple)
                            ) {
                                Text("Create My Account", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Already registered? ",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "Login here",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SampurnaPrimaryPurple,
                            modifier = Modifier
                                .clickable { onNavigateToLogin() }
                                .testTag("signup_goto_login_link")
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StepIndicator(step: Int, title: String, isActive: Boolean, isCompleted: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(
                    if (isCompleted) Color(0xFF10B981)
                    else if (isActive) SampurnaPrimaryPurple
                    else Color(0xFFE2E8F0)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Completed",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Text(
                    text = "$step",
                    color = if (isActive) Color.White else Color.Gray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 10.sp,
            color = if (isActive || isCompleted) SampurnaDarkPurple else Color.Gray,
            fontWeight = if (isActive || isCompleted) FontWeight.Bold else FontWeight.Normal
        )
    }
}
