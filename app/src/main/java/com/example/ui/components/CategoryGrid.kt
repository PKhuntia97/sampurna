package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.CategoryEntity
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate800
import com.example.ui.theme.Violet100
import com.example.ui.theme.Violet600
import com.example.ui.theme.Violet900

@Composable
fun CategoryGrid(
    categories: List<CategoryEntity>,
    selectedCategoryId: Long?,
    onCategorySelected: (CategoryEntity) -> Unit,
    onMoreCategoriesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories, key = { it.id }) { category ->
                val isSelected = selectedCategoryId == category.id
                CategoryItemCard(
                    category = category,
                    isSelected = isSelected,
                    onClick = {
                        if (category.iconType.lowercase() == "more") {
                            onMoreCategoriesClick()
                        } else {
                            onCategorySelected(category)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun CategoryItemCard(
    category: CategoryEntity,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) Violet600 else Color.Transparent,
        animationSpec = spring(),
        label = "category_border"
    )

    val bgColor by animateColorAsState(
        targetValue = if (isSelected) Violet100 else Color.White,
        animationSpec = spring(),
        label = "category_bg"
    )

    Column(
        modifier = modifier
            .width(68.dp)
            .clickable { onClick() }
            .testTag("category_item_${category.id}"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icon Container Box
        Box(
            modifier = Modifier
                .size(56.dp)
                .shadow(
                    elevation = if (isSelected) 6.dp else 2.dp,
                    shape = RoundedCornerShape(18.dp),
                    spotColor = if (isSelected) Violet600 else Slate200
                )
                .clip(RoundedCornerShape(18.dp))
                .background(bgColor)
                .border(width = if (isSelected) 2.dp else 1.dp, color = if (isSelected) Violet600 else Slate200.copy(alpha = 0.6f), shape = RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.Center
        ) {
            CategoryIconBadge(
                iconType = category.iconType,
                imageUrl = category.iconUrl,
                size = 46.dp,
                isSelected = isSelected
            )
        }

        Text(
            text = category.name,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Violet900 else Slate600,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 12.sp,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}
