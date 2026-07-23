package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.CustomerEntity
import com.example.data.local.entity.ExpenseEntity
import com.example.data.local.entity.NotificationEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.SaleEntity
import com.example.data.local.entity.UserEntity
import com.example.data.remote.BizNovaAiService
import com.example.data.repository.BizNovaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BizNovaViewModel(application: Application) : AndroidViewModel(application) {

    val repository = BizNovaRepository(application)

    // User State
    val currentUser: StateFlow<UserEntity?> = repository.userFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    // Products
    val allProducts: StateFlow<List<ProductEntity>> = repository.allProducts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val lowStockProducts: StateFlow<List<ProductEntity>> = repository.lowStockProducts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val productCount: StateFlow<Int> = repository.productCount.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    // Customers
    val allCustomers: StateFlow<List<CustomerEntity>> = repository.allCustomers.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val customersWithCredit: StateFlow<List<CustomerEntity>> = repository.customersWithCredit.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val totalPendingCredit: StateFlow<Double?> = repository.totalPendingCredit.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    val customerCount: StateFlow<Int> = repository.customerCount.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    // Sales
    val allSales: StateFlow<List<SaleEntity>> = repository.allSales.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val todaySales: StateFlow<List<SaleEntity>> = repository.getTodaySales().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val todayRevenue: StateFlow<Double?> = repository.getTodayRevenue().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    val todayProfit: StateFlow<Double?> = repository.getTodayProfit().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    val todaySalesCount: StateFlow<Int> = repository.getTodaySalesCount().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val monthlyRevenue: StateFlow<Double?> = repository.getMonthlyRevenue().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    // Expenses
    val allExpenses: StateFlow<List<ExpenseEntity>> = repository.allExpenses.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val todayExpenses: StateFlow<Double?> = repository.getTodayExpenses().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    val monthlyExpenses: StateFlow<Double?> = repository.getMonthlyExpenses().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    // Notifications
    val allNotifications: StateFlow<List<NotificationEntity>> = repository.allNotifications.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val unreadNotifCount: StateFlow<Int> = repository.unreadNotificationCount.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    // UI Local State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _selectedLanguage = MutableStateFlow("en") // "en" or "hi"
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    // AI Analysis State
    private val _aiSummaryText = MutableStateFlow<String?>(null)
    val aiSummaryText: StateFlow<String?> = _aiSummaryText.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    // Active Cart for Invoice Creation
    private val _cartItems = MutableStateFlow<List<Pair<ProductEntity, Int>>>(emptyList())
    val cartItems: StateFlow<List<Pair<ProductEntity, Int>>> = _cartItems.asStateFlow()

    private val _selectedCustomerForInvoice = MutableStateFlow<CustomerEntity?>(null)
    val selectedCustomerForInvoice: StateFlow<CustomerEntity?> = _selectedCustomerForInvoice.asStateFlow()

    private val _invoiceDiscount = MutableStateFlow(0.0)
    val invoiceDiscount: StateFlow<Double> = _invoiceDiscount.asStateFlow()

    private val _invoiceGstRate = MutableStateFlow(0.0) // 0%, 5%, 12%, 18%
    val invoiceGstRate: StateFlow<Double> = _invoiceGstRate.asStateFlow()

    private val _selectedPaymentMethod = MutableStateFlow("Cash") // Cash, UPI, Card, Udhar
    val selectedPaymentMethod: StateFlow<String> = _selectedPaymentMethod.asStateFlow()

    // Active Detail Selection
    private val _selectedSaleForDetail = MutableStateFlow<SaleEntity?>(null)
    val selectedSaleForDetail: StateFlow<SaleEntity?> = _selectedSaleForDetail.asStateFlow()

    private val _selectedCustomerForDetail = MutableStateFlow<CustomerEntity?>(null)
    val selectedCustomerForDetail: StateFlow<CustomerEntity?> = _selectedCustomerForDetail.asStateFlow()

    fun setSearchQuery(query: String) { _searchQuery.value = query }
    fun setSelectedCategory(cat: String) { _selectedCategory.value = cat }
    fun toggleDarkMode() { _isDarkMode.value = !_isDarkMode.value }
    fun setLanguage(lang: String) {
        _selectedLanguage.value = lang
        currentUser.value?.let { user ->
            viewModelScope.launch {
                repository.saveUser(user.copy(preferredLanguage = lang))
            }
        }
    }

    // User Operations
    fun updateUserProfile(name: String, phone: String, email: String, bName: String, bType: String, gst: String, address: String) {
        viewModelScope.launch {
            val existing = currentUser.value ?: UserEntity()
            val updated = existing.copy(
                name = name,
                phone = phone,
                email = email,
                businessName = bName,
                businessType = bType,
                gstNumber = gst,
                address = address,
                isLoggedIn = true
            )
            repository.saveUser(updated)
        }
    }

    fun logout() {
        viewModelScope.launch {
            currentUser.value?.let { user ->
                repository.saveUser(user.copy(isLoggedIn = false))
            }
        }
    }

    fun loginWithPhoneOrGoogle(name: String, phone: String) {
        viewModelScope.launch {
            val existing = currentUser.value ?: UserEntity()
            repository.saveUser(
                existing.copy(
                    name = name,
                    phone = phone,
                    isLoggedIn = true
                )
            )
        }
    }

    // Cart / Invoice Creation Operations
    fun addToCart(product: ProductEntity, qty: Int = 1) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.first.id == product.id }
        if (index >= 0) {
            val oldPair = current[index]
            val newQty = (oldPair.second + qty).coerceAtMost(product.quantity)
            current[index] = Pair(product, newQty)
        } else {
            current.add(Pair(product, qty.coerceAtMost(product.quantity)))
        }
        _cartItems.value = current
    }

    fun removeFromCart(product: ProductEntity) {
        _cartItems.value = _cartItems.value.filter { it.first.id != product.id }
    }

    fun updateCartQty(product: ProductEntity, newQty: Int) {
        if (newQty <= 0) {
            removeFromCart(product)
        } else {
            val current = _cartItems.value.toMutableList()
            val index = current.indexOfFirst { it.first.id == product.id }
            if (index >= 0) {
                current[index] = Pair(product, newQty.coerceAtMost(product.quantity))
                _cartItems.value = current
            }
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
        _selectedCustomerForInvoice.value = null
        _invoiceDiscount.value = 0.0
        _invoiceGstRate.value = 0.0
        _selectedPaymentMethod.value = "Cash"
    }

    fun setSelectedCustomerForInvoice(customer: CustomerEntity?) {
        _selectedCustomerForInvoice.value = customer
    }

    fun setInvoiceDiscount(discount: Double) { _invoiceDiscount.value = discount }
    fun setInvoiceGstRate(rate: Double) { _invoiceGstRate.value = rate }
    fun setSelectedPaymentMethod(method: String) { _selectedPaymentMethod.value = method }

    fun finalizeAndCreateInvoice(onSuccess: (Long) -> Unit) {
        val items = _cartItems.value
        if (items.isEmpty()) return

        viewModelScope.launch {
            var subtotal = 0.0
            var estProfit = 0.0
            val summaryList = mutableListOf<String>()

            for ((prod, qty) in items) {
                val itemCost = prod.sellingPrice * qty
                val itemProfit = (prod.sellingPrice - prod.purchasePrice) * qty
                subtotal += itemCost
                estProfit += itemProfit
                summaryList.add("${qty}x ${prod.name}")
            }

            val discount = _invoiceDiscount.value
            val taxable = (subtotal - discount).coerceAtLeast(0.0)
            val gstVal = taxable * (_invoiceGstRate.value / 100.0)
            val netTotal = taxable + gstVal

            val cust = _selectedCustomerForInvoice.value
            val invNo = "INV-2026-" + (1000..9999).random()

            val sale = SaleEntity(
                invoiceNumber = invNo,
                customerId = cust?.id,
                customerName = cust?.name ?: "Walk-in Customer",
                customerPhone = cust?.phone ?: "",
                totalAmount = subtotal,
                discountAmount = discount,
                gstAmount = gstVal,
                netAmount = netTotal,
                estimatedProfit = estProfit,
                paymentMethod = _selectedPaymentMethod.value,
                paymentStatus = if (_selectedPaymentMethod.value == "Udhar") "Pending" else "Paid",
                itemsSummary = summaryList.joinToString(", "),
                timestamp = System.currentTimeMillis()
            )

            val saleId = repository.createInvoice(sale, items)
            val createdSale = sale.copy(id = saleId)
            _selectedSaleForDetail.value = createdSale
            clearCart()
            onSuccess(saleId)
        }
    }

    // Product CRUD
    fun saveProduct(
        id: Long = 0,
        name: String,
        sku: String,
        barcode: String,
        category: String,
        pPrice: Double,
        sPrice: Double,
        qty: Int,
        lowLimit: Int,
        unit: String
    ) {
        viewModelScope.launch {
            val prod = ProductEntity(
                id = id,
                name = name,
                sku = sku,
                barcode = barcode,
                category = category,
                purchasePrice = pPrice,
                sellingPrice = sPrice,
                quantity = qty,
                lowStockThreshold = lowLimit,
                unit = unit,
                updatedAt = System.currentTimeMillis()
            )
            if (id == 0L) {
                repository.addProduct(prod)
            } else {
                repository.updateProduct(prod)
            }
        }
    }

    fun deleteProduct(id: Long) {
        viewModelScope.launch { repository.deleteProduct(id) }
    }

    // Customer CRUD
    fun saveCustomer(id: Long = 0, name: String, phone: String, credit: Double, notes: String, address: String) {
        viewModelScope.launch {
            val cust = CustomerEntity(
                id = id,
                name = name,
                phone = phone,
                creditBalance = credit,
                notes = notes,
                address = address,
                createdAt = System.currentTimeMillis()
            )
            if (id == 0L) {
                repository.addCustomer(cust)
            } else {
                repository.updateCustomer(cust)
            }
        }
    }

    fun updateCustomerCreditPayment(customer: CustomerEntity, paidAmount: Double) {
        viewModelScope.launch {
            val newCredit = (customer.creditBalance - paidAmount).coerceAtLeast(0.0)
            repository.updateCustomer(customer.copy(creditBalance = newCredit))
        }
    }

    fun deleteCustomer(id: Long) {
        viewModelScope.launch { repository.deleteCustomer(id) }
    }

    // Expense CRUD
    fun addExpense(title: String, category: String, amount: Double, note: String) {
        viewModelScope.launch {
            repository.addExpense(
                ExpenseEntity(
                    title = title,
                    category = category,
                    amount = amount,
                    note = note,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    fun deleteExpense(id: Long) {
        viewModelScope.launch { repository.deleteExpense(id) }
    }

    // AI Functions
    fun fetchAiDailySummary() {
        viewModelScope.launch {
            _isAiLoading.value = true
            val user = currentUser.value
            val bType = user?.businessType ?: "Kirana Shop"
            val sales = todayRevenue.value ?: 0.0
            val profit = todayProfit.value ?: 0.0
            val count = todaySalesCount.value
            val pending = totalPendingCredit.value ?: 0.0
            val lang = selectedLanguage.value

            val summary = BizNovaAiService.getDailySummary(
                businessType = bType,
                todaySales = sales,
                todayProfit = profit,
                invoiceCount = count,
                pendingUdhar = pending,
                language = lang
            )
            _aiSummaryText.value = summary
            _isAiLoading.value = false
        }
    }

    fun setSelectedSaleForDetail(sale: SaleEntity?) {
        _selectedSaleForDetail.value = sale
    }

    fun setSelectedCustomerForDetail(customer: CustomerEntity?) {
        _selectedCustomerForDetail.value = customer
    }

    // Notifications
    fun markNotificationRead(id: Long) {
        viewModelScope.launch { repository.markNotificationAsRead(id) }
    }

    fun markAllNotificationsRead() {
        viewModelScope.launch { repository.markAllNotificationsRead() }
    }
}
