package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Orange500
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate600
import com.example.ui.theme.Violet700

enum class NavigationTab {
    HOME,
    PLAY,
    CATEGORIES,
    ACCOUNT,
    CART
}

@Composable
fun SampurnaBottomBar(
    currentTab: NavigationTab,
    cartItemCount: Int = 2,
    onTabSelected: (NavigationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
                spotColor = Color(0x22000000)
            ),
        shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding() // Proper safe area inset for Android gesture & 3-button system bars
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(
                    label = "Home",
                    icon = if (currentTab == NavigationTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                    isSelected = currentTab == NavigationTab.HOME,
                    onClick = { onTabSelected(NavigationTab.HOME) },
                    testTag = "nav_tab_home"
                )

                BottomNavItem(
                    label = "Play",
                    icon = if (currentTab == NavigationTab.PLAY) Icons.Filled.PlayCircle else Icons.Outlined.PlayCircle,
                    isSelected = currentTab == NavigationTab.PLAY,
                    onClick = { onTabSelected(NavigationTab.PLAY) },
                    testTag = "nav_tab_play"
                )

                BottomNavItem(
                    label = "Categories",
                    icon = if (currentTab == NavigationTab.CATEGORIES) Icons.Filled.Category else Icons.Outlined.Category,
                    isSelected = currentTab == NavigationTab.CATEGORIES,
                    onClick = { onTabSelected(NavigationTab.CATEGORIES) },
                    testTag = "nav_tab_categories"
                )

                BottomNavItem(
                    label = "Account",
                    icon = if (currentTab == NavigationTab.ACCOUNT) Icons.Filled.Person else Icons.Outlined.Person,
                    isSelected = currentTab == NavigationTab.ACCOUNT,
                    onClick = { onTabSelected(NavigationTab.ACCOUNT) },
                    testTag = "nav_tab_account"
                )

                BottomNavItem(
                    label = "Cart",
                    icon = if (currentTab == NavigationTab.CART) Icons.Filled.ShoppingCart else Icons.Outlined.ShoppingCart,
                    isSelected = currentTab == NavigationTab.CART,
                    badgeCount = cartItemCount,
                    onClick = { onTabSelected(NavigationTab.CART) },
                    testTag = "nav_tab_cart"
                )
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    badgeCount: Int = 0,
    onClick: () -> Unit,
    testTag: String
) {
    val tintColor by animateColorAsState(
        targetValue = if (isSelected) Violet700 else Slate400,
        label = "nav_tint"
    )

    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (badgeCount > 0) {
            BadgedBox(
                badge = {
                    Badge(
                        containerColor = Orange500,
                        contentColor = Color.White,
                        modifier = Modifier.size(16.dp)
                    ) {
                        Text(
                            text = badgeCount.toString(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = tintColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        } else {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tintColor,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = tintColor
        )
    }
}
