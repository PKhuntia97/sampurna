package com.example.ui.screens.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import kotlinx.coroutines.delay

@Composable
fun SampurnaSplashScreen(
    onSplashFinished: () -> Unit
) {
    var progress by remember { mutableFloatStateOf(0f) }
    var startAnimation by remember { androidx.compose.runtime.mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "splash_infinite")
    
    // Rotating gradient glow ring
    val ringRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ring_rotation"
    )

    // Gentle pulsing scale
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    // Entrance scale animation
    val entryScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.4f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "entry_scale"
    )

    // Alpha entrance
    val entryAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 700),
        label = "entry_alpha"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        // Animate progress smoothly over ~2.6 seconds
        val steps = 50
        val delayTime = 2600L / steps
        for (i in 1..steps) {
            progress = i / steps.toFloat()
            delay(delayTime)
        }
        delay(200)
        onSplashFinished()
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onSplashFinished()
            }
            .testTag("sampurna_splash_screen"),
        color = Color(0xFF0F172A) // Dark premium backdrop with radiant highlights
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Ambient background radial gradients
            Box(
                modifier = Modifier
                    .size(450.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0x334F46E5),
                                Color(0x22F97316),
                                Color(0x000F172A)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .scale(entryScale * pulseScale),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Circular Brand Emblem with Rotating Radiant Ring
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .shadow(elevation = 24.dp, shape = CircleShape, spotColor = Color(0xFFF97316)),
                    contentAlignment = Alignment.Center
                ) {
                    // Outer rotating vibrant gradient ring
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .rotate(ringRotation)
                            .clip(CircleShape)
                            .background(
                                Brush.sweepGradient(
                                    colors = listOf(
                                        Color(0xFFF97316), // Orange
                                        Color(0xFFFBBF24), // Gold
                                        Color(0xFF10B981), // Green
                                        Color(0xFF3B82F6), // Blue
                                        Color(0xFF8B5CF6), // Purple
                                        Color(0xFFF97316)  // Orange
                                    )
                                )
                            )
                    )

                    // Inner container for logo
                    Box(
                        modifier = Modifier
                            .size(228.dp)
                            .clip(RoundedCornerShape(32.dp))
                            .background(Color.White)
                            .border(3.dp, Color(0xFFFFFFFF), RoundedCornerShape(32.dp))
                            .padding(6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_sampurna_logo),
                            contentDescription = "Sampurna Logo",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(26.dp)),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Brand Titles & Odia Script
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Odia Script Name
                    Text(
                        text = "ସମ୍ପୂର୍ଣ୍ଣ",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFB923C),
                        letterSpacing = 2.sp
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    // English Brand Name
                    Text(
                        text = "SAMPURNA",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = 3.sp
                    )

                    // Ribbon subtitle
                    Box(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF1E3A8A), Color(0xFF3B82F6))
                                )
                            )
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "E-Commerce Application",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "— Shop Smart, Live Better —",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF94A3B8),
                        letterSpacing = 0.5.sp
                    )

                    Text(
                        text = "Complete Shopping & Delivery Solution",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Value Highlights Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SplashFeaturePill(icon = Icons.Default.LocalShipping, text = "Fast Delivery")
                    SplashFeaturePill(icon = Icons.Default.VerifiedUser, text = "100% Genuine")
                    SplashFeaturePill(icon = Icons.Default.ElectricBolt, text = "Best Offers")
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Progress Bar
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.65f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = Color(0xFFF97316),
                        trackColor = Color(0xFF334155),
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Loading your marketplace...",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun SplashFeaturePill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0x1AFFFFFF))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF38BDF8),
            modifier = Modifier.size(13.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFFE2E8F0)
        )
    }
}
