package com.example.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import com.example.data.local.entity.SaleEntity
import com.example.ui.screens.customers.CustomersScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.inventory.InventoryScreen
import com.example.ui.screens.reports.ReportsScreen
import com.example.ui.screens.sales.SalesScreen
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.PrimaryBlue
import com.example.viewmodel.BizNovaViewModel

@Composable
fun MainContainerScreen(
    viewModel: BizNovaViewModel,
    onCreateInvoiceClick: () -> Unit,
    onAddProductClick: () -> Unit,
    onAddCustomerClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    onAiAssistantClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSaleDetailClick: (SaleEntity) -> Unit,
    onCustomerDetailClick: (com.example.data.local.entity.CustomerEntity) -> Unit
) {
    var selectedTab by remember { mutableStateOf(BottomTab.HOME) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                NavigationBarItem(
                    selected = selectedTab == BottomTab.HOME,
                    onClick = { selectedTab = BottomTab.HOME },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryBlue,
                        selectedTextColor = PrimaryBlue,
                        indicatorColor = PrimaryBlue.copy(alpha = 0.15f)
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == BottomTab.INVENTORY,
                    onClick = { selectedTab = BottomTab.INVENTORY },
                    icon = { Icon(Icons.Default.Inventory, contentDescription = "Inventory") },
                    label = { Text("Inventory") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryBlue,
                        selectedTextColor = PrimaryBlue,
                        indicatorColor = PrimaryBlue.copy(alpha = 0.15f)
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == BottomTab.SALES,
                    onClick = { selectedTab = BottomTab.SALES },
                    icon = { Icon(Icons.Default.PointOfSale, contentDescription = "Sales") },
                    label = { Text("Sales") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryBlue,
                        selectedTextColor = PrimaryBlue,
                        indicatorColor = PrimaryBlue.copy(alpha = 0.15f)
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == BottomTab.CUSTOMERS,
                    onClick = { selectedTab = BottomTab.CUSTOMERS },
                    icon = { Icon(Icons.Default.Group, contentDescription = "Customers") },
                    label = { Text("Khata") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryBlue,
                        selectedTextColor = PrimaryBlue,
                        indicatorColor = PrimaryBlue.copy(alpha = 0.15f)
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == BottomTab.REPORTS,
                    onClick = { selectedTab = BottomTab.REPORTS },
                    icon = { Icon(Icons.Default.Assessment, contentDescription = "Reports") },
                    label = { Text("Reports") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryBlue,
                        selectedTextColor = PrimaryBlue,
                        indicatorColor = PrimaryBlue.copy(alpha = 0.15f)
                    )
                )
            }
        },
        floatingActionButton = {
            if (selectedTab == BottomTab.HOME) {
                FloatingActionButton(
                    onClick = onCreateInvoiceClick,
                    shape = CircleShape,
                    containerColor = EmeraldGreen,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Quick POS Invoice")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                BottomTab.HOME -> HomeScreen(
                    viewModel = viewModel,
                    onCreateInvoiceClick = onCreateInvoiceClick,
                    onAddProductClick = onAddProductClick,
                    onAddCustomerClick = onAddCustomerClick,
                    onAddExpenseClick = onAddExpenseClick,
                    onAiAssistantClick = onAiAssistantClick,
                    onNotificationClick = onNotificationClick,
                    onProfileClick = onProfileClick,
                    onSaleClick = onSaleDetailClick,
                    onInventoryClick = { selectedTab = BottomTab.INVENTORY },
                    onCustomersClick = { selectedTab = BottomTab.CUSTOMERS }
                )

                BottomTab.INVENTORY -> InventoryScreen(viewModel = viewModel)

                BottomTab.SALES -> SalesScreen(
                    viewModel = viewModel,
                    onCreateInvoiceClick = onCreateInvoiceClick,
                    onSaleDetailClick = onSaleDetailClick
                )

                BottomTab.CUSTOMERS -> CustomersScreen(
                    viewModel = viewModel,
                    onCustomerClick = onCustomerDetailClick
                )

                BottomTab.REPORTS -> ReportsScreen(
                    viewModel = viewModel,
                    onAddExpenseClick = onAddExpenseClick
                )
            }
        }
    }
}
