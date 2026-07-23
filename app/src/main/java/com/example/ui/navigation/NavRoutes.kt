package com.example.ui.navigation

sealed class NavRoutes(val route: String) {
    object Splash : NavRoutes("splash")
    object Auth : NavRoutes("auth")
    object BusinessSetup : NavRoutes("business_setup")
    object MainContainer : NavRoutes("main_container")
    object CreateInvoice : NavRoutes("create_invoice")
    object InvoiceDetail : NavRoutes("invoice_detail")
    object CustomerDetail : NavRoutes("customer_detail")
    object AiAssistant : NavRoutes("ai_assistant")
    object Settings : NavRoutes("settings")
    object AddExpense : NavRoutes("add_expense")
    object Notifications : NavRoutes("notifications")
}

enum class BottomTab(val route: String, val label: String) {
    HOME("tab_home", "Home"),
    INVENTORY("tab_inventory", "Inventory"),
    SALES("tab_sales", "Sales"),
    CUSTOMERS("tab_customers", "Customers"),
    REPORTS("tab_reports", "Reports")
}
