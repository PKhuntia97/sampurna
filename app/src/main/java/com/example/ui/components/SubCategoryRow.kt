package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.local.entity.SubCategoryEntity
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate900
import com.example.ui.theme.Violet100
import com.example.ui.theme.Violet600
import com.example.ui.theme.Violet700
import com.example.ui.theme.Violet900

@Composable
fun SubCategoryRow(
    categoryName: String,
    subCategories: List<SubCategoryEntity>,
    selectedSubCategoryId: Long?,
    onSubCategoryClick: (SubCategoryEntity) -> Unit,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = subCategories.isNotEmpty(),
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 4.dp)
        ) {
            // Header: Sub Categories (CategoryName) + View All >
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sub Categories ($categoryName)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )

                Row(
                    modifier = Modifier
                        .clickable { onViewAllClick() }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "View All",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Violet700
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "View all sub categories",
                        tint = Violet700,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Horizontal subcategory items
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(subCategories, key = { it.id }) { subCategory ->
                    val isSelected = selectedSubCategoryId == subCategory.id
                    SubCategoryItemCard(
                        subCategory = subCategory,
                        isSelected = isSelected,
                        onClick = { onSubCategoryClick(subCategory) }
                    )
                }
            }
        }
    }
}

@Composable
fun SubCategoryItemCard(
    subCategory: SubCategoryEntity,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(68.dp)
            .clickable { onClick() }
            .testTag("sub_category_item_${subCategory.id}"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .shadow(
                    elevation = if (isSelected) 4.dp else 2.dp,
                    shape = RoundedCornerShape(18.dp),
                    spotColor = if (isSelected) Violet600 else Slate200
                )
                .clip(RoundedCornerShape(18.dp))
                .background(if (isSelected) Violet100 else Color.White)
                .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) Violet600 else Slate200.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(18.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            SubCategoryVisualBadge(
                iconType = subCategory.iconType,
                imageUrl = subCategory.imageUrl,
                size = 46.dp
            )
        }

        Text(
            text = subCategory.name,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Violet900 else Slate600,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
