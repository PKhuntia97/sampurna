package com.example.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.SampurnaDatabase
import com.example.data.local.entity.CategoryBannerEntity
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.SubCategoryEntity
import com.example.data.repository.SampurnaRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SampurnaRepository(SampurnaDatabase.getDatabase(application))

    private val _selectedCategory = MutableStateFlow<CategoryEntity?>(null)
    val selectedCategory: StateFlow<CategoryEntity?> = _selectedCategory.asStateFlow()

    private val _selectedSubCategory = MutableStateFlow<SubCategoryEntity?>(null)
    val selectedSubCategory: StateFlow<SubCategoryEntity?> = _selectedSubCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _activeProductTab = MutableStateFlow("trending")
    val activeProductTab: StateFlow<String> = _activeProductTab.asStateFlow()

    private val _currentLocation = MutableStateFlow("Keonjhar, Odisha")
    val currentLocation: StateFlow<String> = _currentLocation.asStateFlow()

    private val _cartItemCount = MutableStateFlow(2)
    val cartItemCount: StateFlow<Int> = _cartItemCount.asStateFlow()

    private val _snackBarMessage = MutableStateFlow<String?>(null)
    val snackBarMessage: StateFlow<String?> = _snackBarMessage.asStateFlow()

    // Dynamic Categories from DB
    val activeCategories: StateFlow<List<CategoryEntity>> = repository.getActiveCategories()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // All active subcategories (for Home browse & explore)
    val allActiveSubCategories: StateFlow<List<SubCategoryEntity>> = repository.getAllActiveSubCategories()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // SubCategories for selected category
    val subCategories: StateFlow<List<SubCategoryEntity>> = _selectedCategory
        .flatMapLatest { category ->
            if (category != null) {
                repository.getActiveSubCategoriesForCategory(category.id)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // General Banners (Home default slider)
    val generalBanners: StateFlow<List<CategoryBannerEntity>> = repository.getActiveGeneralBanners()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Category Specific Mega Sale Banner (Always dynamically matches clicked category)
    val categoryBanner: StateFlow<CategoryBannerEntity?> = _selectedCategory
        .flatMapLatest { category ->
            if (category != null) {
                repository.getActiveBannerForCategory(category.id).map { dbBanner ->
                    dbBanner ?: CategoryBannerEntity(
                        categoryId = category.id,
                        title = "${category.name.uppercase()} SPECIAL SALE",
                        subtitle = if (category.description.isNotBlank()) category.description else "Explore Top Deals, Authentic Brands & Superfast Delivery",
                        tag = "${category.name.uppercase()} FEST",
                        discountText = "UP TO 60% OFF",
                        bannerType = category.iconType.ifBlank { "mega_sale" },
                        imageUrl = category.iconUrl,
                        displayOrder = 1
                    )
                }
            } else {
                flowOf(null)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    // Dynamic Products matching Category / SubCategory / Search / Tab
    val products: StateFlow<List<ProductEntity>> = combine(
        _selectedCategory,
        _selectedSubCategory,
        _searchQuery,
        _activeProductTab,
        repository.getActiveProducts()
    ) { category, subCategory, query, tab, allActiveProducts ->
        var list = allActiveProducts

        if (query.isNotBlank()) {
            list = list.filter { it.name.contains(query, ignoreCase = true) }
        } else if (subCategory != null) {
            list = list.filter { it.subCategoryId == subCategory.id }
        } else if (category != null) {
            list = list.filter { it.categoryId == category.id }
        } else {
            // Tab filtering for default state
            list = list.filter { it.tag.equals(tab, ignoreCase = true) }
            if (list.isEmpty()) {
                list = allActiveProducts.take(6)
            }
        }
        list
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun selectCategory(category: CategoryEntity) {
        if (_selectedCategory.value?.id == category.id) {
            // Toggle off to default overview state
            _selectedCategory.value = null
            _selectedSubCategory.value = null
        } else {
            _selectedCategory.value = category
            _selectedSubCategory.value = null // reset subcategory on category change
        }
    }

    fun selectSubCategory(subCategory: SubCategoryEntity) {
        if (_selectedSubCategory.value?.id == subCategory.id) {
            _selectedSubCategory.value = null
        } else {
            _selectedSubCategory.value = subCategory
        }
    }

    fun selectCategoryAndSubCategory(category: CategoryEntity, subCategory: SubCategoryEntity?) {
        _selectedCategory.value = category
        _selectedSubCategory.value = subCategory
    }

    fun setProductTab(tabId: String) {
        _activeProductTab.value = tabId
        // Also clear active category when customer chooses explicit home tab
        _selectedCategory.value = null
        _selectedSubCategory.value = null
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setLocation(location: String) {
        _currentLocation.value = location
    }

    fun addToCart(product: ProductEntity) {
        viewModelScope.launch {
            repository.addToCart(userId = 1L, productId = product.id, quantity = 1)
            _cartItemCount.value += 1
            _snackBarMessage.value = "Added \"${product.name}\" to Cart!"
        }
    }

    fun clearSnackBarMessage() {
        _snackBarMessage.value = null
    }
}
