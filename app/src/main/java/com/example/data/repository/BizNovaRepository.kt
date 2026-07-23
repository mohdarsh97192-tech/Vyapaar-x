package com.example.data.repository

import android.content.Context
import com.example.data.local.BizNovaDatabase
import com.example.data.local.entity.CustomerEntity
import com.example.data.local.entity.ExpenseEntity
import com.example.data.local.entity.NotificationEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.SaleEntity
import com.example.data.local.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Calendar

class BizNovaRepository(context: Context) {

    private val db = BizNovaDatabase.getDatabase(context)
    private val userDao = db.userDao()
    private val productDao = db.productDao()
    private val customerDao = db.customerDao()
    private val saleDao = db.saleDao()
    private val expenseDao = db.expenseDao()
    private val notificationDao = db.notificationDao()

    init {
        // Pre-populate sample data if empty in background
        CoroutineScope(Dispatchers.IO).launch {
            seedSampleDataIfRequired()
        }
    }

    // User Profile
    val userFlow: Flow<UserEntity?> = userDao.getUserFlow()
    suspend fun getUser(): UserEntity? = userDao.getUser()
    suspend fun saveUser(user: UserEntity) = userDao.insertOrUpdateUser(user)

    // Products
    val allProducts: Flow<List<ProductEntity>> = productDao.getAllProducts()
    val lowStockProducts: Flow<List<ProductEntity>> = productDao.getLowStockProducts()
    val productCount: Flow<Int> = productDao.getProductCount()

    suspend fun addProduct(product: ProductEntity): Long = productDao.insertProduct(product)
    suspend fun updateProduct(product: ProductEntity) = productDao.updateProduct(product)
    suspend fun deleteProduct(id: Long) = productDao.deleteProductById(id)
    suspend fun getProductByBarcode(code: String): ProductEntity? = productDao.getProductByBarcode(code)

    // Customers
    val allCustomers: Flow<List<CustomerEntity>> = customerDao.getAllCustomers()
    val customersWithCredit: Flow<List<CustomerEntity>> = customerDao.getCustomersWithCredit()
    val totalPendingCredit: Flow<Double?> = customerDao.getTotalPendingCredit()
    val customerCount: Flow<Int> = customerDao.getCustomerCount()

    suspend fun addCustomer(customer: CustomerEntity): Long = customerDao.insertCustomer(customer)
    suspend fun updateCustomer(customer: CustomerEntity) = customerDao.updateCustomer(customer)
    suspend fun deleteCustomer(id: Long) = customerDao.deleteCustomerById(id)

    // Sales & Invoices
    val allSales: Flow<List<SaleEntity>> = saleDao.getAllSales()

    fun getTodayStartTimestamp(): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    fun getMonthStartTimestamp(): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    fun getTodaySales(): Flow<List<SaleEntity>> = saleDao.getSalesSince(getTodayStartTimestamp())
    fun getTodayRevenue(): Flow<Double?> = saleDao.getRevenueSince(getTodayStartTimestamp())
    fun getTodayProfit(): Flow<Double?> = saleDao.getProfitSince(getTodayStartTimestamp())
    fun getTodaySalesCount(): Flow<Int> = saleDao.getSalesCountSince(getTodayStartTimestamp())
    fun getMonthlyRevenue(): Flow<Double?> = saleDao.getRevenueSince(getMonthStartTimestamp())

    suspend fun createInvoice(
        sale: SaleEntity,
        purchasedProducts: List<Pair<ProductEntity, Int>>
    ): Long {
        val saleId = saleDao.insertSale(sale)

        // Deduct inventory quantities
        for ((prod, qty) in purchasedProducts) {
            val newQty = (prod.quantity - qty).coerceAtLeast(0)
            productDao.updateProduct(prod.copy(quantity = newQty, updatedAt = System.currentTimeMillis()))
        }

        // If payment method is Udhar / Credit and customer is selected, update customer credit balance
        if ((sale.paymentMethod == "Udhar" || sale.paymentStatus == "Pending") && sale.customerId != null) {
            val customer = customerDao.getCustomerById(sale.customerId)
            if (customer != null) {
                val newCredit = customer.creditBalance + sale.netAmount
                customerDao.updateCustomer(customer.copy(creditBalance = newCredit))
            }
        }

        return saleId
    }

    // Expenses
    val allExpenses: Flow<List<ExpenseEntity>> = expenseDao.getAllExpenses()
    fun getTodayExpenses(): Flow<Double?> = expenseDao.getTotalExpensesSince(getTodayStartTimestamp())
    fun getMonthlyExpenses(): Flow<Double?> = expenseDao.getTotalExpensesSince(getMonthStartTimestamp())

    suspend fun addExpense(expense: ExpenseEntity): Long = expenseDao.insertExpense(expense)
    suspend fun deleteExpense(id: Long) = expenseDao.deleteExpenseById(id)

    // Notifications
    val allNotifications: Flow<List<NotificationEntity>> = notificationDao.getAllNotifications()
    val unreadNotificationCount: Flow<Int> = notificationDao.getUnreadCount()

    suspend fun addNotification(notification: NotificationEntity): Long = notificationDao.insertNotification(notification)
    suspend fun markNotificationAsRead(id: Long) = notificationDao.markAsRead(id)
    suspend fun markAllNotificationsRead() = notificationDao.markAllAsRead()

    // Database Seeding
    private suspend fun seedSampleDataIfRequired() {
        if (userDao.getUser() == null) {
            userDao.insertOrUpdateUser(
                UserEntity(
                    id = "default_user",
                    name = "Rajesh Sharma",
                    phone = "+91 98765 43210",
                    email = "sharma.kirana@biznova.in",
                    businessType = "Kirana & General Store",
                    businessName = "Sharma Kirana & General Store",
                    gstNumber = "07AAAAA0000A1Z5",
                    address = "Shop No. 12, Sector 15 Market, New Delhi",
                    isLoggedIn = true,
                    preferredLanguage = "en"
                )
            )
        }

        val currentProdCount = productDao.getProductCount().firstOrNull() ?: 0
        if (currentProdCount == 0) {
            val sampleProducts = listOf(
                ProductEntity(name = "Aashirvaad Whole Wheat Atta 10kg", sku = "ATT-10", barcode = "8901058852312", category = "Groceries", purchasePrice = 380.0, sellingPrice = 440.0, quantity = 22, lowStockThreshold = 5, unit = "pkt"),
                ProductEntity(name = "Fortune Refined Sunflower Oil 1L", sku = "OIL-01", barcode = "8906007280015", category = "Groceries", purchasePrice = 112.0, sellingPrice = 135.0, quantity = 3, lowStockThreshold = 10, unit = "btl"),
                ProductEntity(name = "Amul Butter Pasteurized 500g", sku = "BTR-05", barcode = "8901262010052", category = "Dairy", purchasePrice = 232.0, sellingPrice = 275.0, quantity = 2, lowStockThreshold = 5, unit = "pcs"),
                ProductEntity(name = "Tata Salt Iodized 1kg", sku = "SLT-01", barcode = "8901058000010", category = "Groceries", purchasePrice = 20.0, sellingPrice = 28.0, quantity = 48, lowStockThreshold = 10, unit = "pkt"),
                ProductEntity(name = "Dolo 650 Paracetamol 15 Tablets", sku = "MED-01", barcode = "8901112233441", category = "Medicines", purchasePrice = 22.0, sellingPrice = 33.5, quantity = 65, lowStockThreshold = 15, unit = "strip"),
                ProductEntity(name = "Cadbury Dairy Milk Silk 150g", sku = "CHK-01", barcode = "8901233009988", category = "Snacks", purchasePrice = 142.0, sellingPrice = 175.0, quantity = 18, lowStockThreshold = 5, unit = "pcs"),
                ProductEntity(name = "boAt Type-C Fast Charge Cable 1.5m", sku = "CAB-01", barcode = "8904321100223", category = "Electronics", purchasePrice = 95.0, sellingPrice = 199.0, quantity = 4, lowStockThreshold = 6, unit = "pcs"),
                ProductEntity(name = "Surf Excel Easy Wash Powder 1kg", sku = "DET-01", barcode = "8901030012345", category = "Cleaning", purchasePrice = 115.0, sellingPrice = 142.0, quantity = 16, lowStockThreshold = 5, unit = "pkt")
            )
            for (p in sampleProducts) {
                productDao.insertProduct(p)
            }
        }

        val currentCustCount = customerDao.getCustomerCount().firstOrNull() ?: 0
        if (currentCustCount == 0) {
            val sampleCustomers = listOf(
                CustomerEntity(name = "Ramesh Verma", phone = "+91 98112 34567", creditBalance = 1450.0, notes = "Regular monthly household khata customer", address = "House 45, Sector 15"),
                CustomerEntity(name = "Priya Gupta", phone = "+91 98711 88234", creditBalance = 820.0, notes = "Main Street resident", address = "Flat 202, Green Valley Apts"),
                CustomerEntity(name = "Anil Kumar (Contractor)", phone = "+91 99532 11099", creditBalance = 2100.0, notes = "Contractor - promised payment on Friday", address = "Site 4 B, Sector 18"),
                CustomerEntity(name = "Sunita Devi", phone = "+91 97110 55432", creditBalance = 350.0, notes = "Daily milk & groceries", address = "House 102, Sector 15")
            )
            for (c in sampleCustomers) {
                customerDao.insertCustomer(c)
            }
        }

        val currentSales = saleDao.getAllSales().firstOrNull() ?: emptyList()
        if (currentSales.isEmpty()) {
            val now = System.currentTimeMillis()
            val hourMs = 3600 * 1000L
            val sampleSalesList = listOf(
                SaleEntity(invoiceNumber = "INV-2026-0841", customerName = "Ramesh Verma", totalAmount = 980.0, discountAmount = 50.0, gstAmount = 0.0, netAmount = 930.0, estimatedProfit = 160.0, paymentMethod = "UPI", paymentStatus = "Paid", itemsSummary = "1x Aashirvaad Atta 10kg, 2x Tata Salt 1kg", timestamp = now - (2 * hourMs)),
                SaleEntity(invoiceNumber = "INV-2026-0842", customerName = "Walk-in Customer", totalAmount = 375.0, discountAmount = 0.0, gstAmount = 0.0, netAmount = 375.0, estimatedProfit = 65.0, paymentMethod = "Cash", paymentStatus = "Paid", itemsSummary = "1x Fortune Sunflower Oil 1L, 1x Surf Excel 1kg", timestamp = now - (4 * hourMs)),
                SaleEntity(invoiceNumber = "INV-2026-0843", customerName = "Priya Gupta", totalAmount = 820.0, discountAmount = 0.0, gstAmount = 0.0, netAmount = 820.0, estimatedProfit = 140.0, paymentMethod = "Udhar", paymentStatus = "Pending", itemsSummary = "2x Amul Butter 500g, 1x Cadbury Silk 150g", timestamp = now - (6 * hourMs)),
                SaleEntity(invoiceNumber = "INV-2026-0844", customerName = "Walk-in Customer", totalAmount = 632.0, discountAmount = 20.0, gstAmount = 0.0, netAmount = 612.0, estimatedProfit = 110.0, paymentMethod = "Card", paymentStatus = "Paid", itemsSummary = "1x boAt Type-C Cable, 4x Dolo 650 Strip", timestamp = now - (8 * hourMs))
            )
            for (s in sampleSalesList) {
                saleDao.insertSale(s)
            }
        }

        val currentExpenses = expenseDao.getAllExpenses().firstOrNull() ?: emptyList()
        if (currentExpenses.isEmpty()) {
            val now = System.currentTimeMillis()
            val dayMs = 86400 * 1000L
            val sampleExpenses = listOf(
                ExpenseEntity(title = "Shop Rent (Sector 15)", category = "Rent", amount = 12000.0, note = "Paid to Shop Landlord", timestamp = now - (2 * dayMs)),
                ExpenseEntity(title = "Electricity Power Bill", category = "Electricity", amount = 3450.0, note = "BSES Rajdhani Bill", timestamp = now - (5 * dayMs)),
                ExpenseEntity(title = "Staff Helper Salary (Amit)", category = "Salary", amount = 9000.0, note = "Monthly helper wages", timestamp = now - (10 * dayMs)),
                ExpenseEntity(title = "Wholesale Mandi Auto Transport", category = "Transport", amount = 650.0, note = "Auto fare for loading stock", timestamp = now - (1 * dayMs))
            )
            for (e in sampleExpenses) {
                expenseDao.insertExpense(e)
            }
        }

        val currentNotifs = notificationDao.getAllNotifications().firstOrNull() ?: emptyList()
        if (currentNotifs.isEmpty()) {
            val now = System.currentTimeMillis()
            val sampleNotifs = listOf(
                NotificationEntity(title = "Low Stock Alert ⚠️", message = "Fortune Sunflower Oil 1L (3 left) and Amul Butter (2 left) are below safe stock levels.", type = "LOW_STOCK", timestamp = now - (10 * 60 * 1000L)),
                NotificationEntity(title = "Udhar Due Reminder 💬", message = "Anil Kumar (Contractor) owes ₹2,100. Tap to send a polite WhatsApp reminder.", type = "PAYMENT_DUE", timestamp = now - (2 * 3600 * 1000L)),
                NotificationEntity(title = "Daily Sales Record 🎉", message = "Today's sales reached ₹2,737! Gross profit estimated at ₹475.", type = "DAILY_SUMMARY", timestamp = now - (5 * 3600 * 1000L))
            )
            for (n in sampleNotifs) {
                notificationDao.insertNotification(n)
            }
        }
    }
}
