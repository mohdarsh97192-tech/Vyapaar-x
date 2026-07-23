package com.example.ui.screens.reports

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.StatCard
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SapphireBlue
import com.example.viewmodel.BizNovaViewModel

@Composable
fun ReportsScreen(
    viewModel: BizNovaViewModel,
    onAddExpenseClick: () -> Unit
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Sales & Profit", "Expense Tracker", "Inventory Valuation")

    val todayRev by viewModel.todayRevenue.collectAsState()
    val todayProf by viewModel.todayProfit.collectAsState()
    val monthlyRev by viewModel.monthlyRevenue.collectAsState()
    val todayExp by viewModel.todayExpenses.collectAsState()
    val monthlyExp by viewModel.monthlyExpenses.collectAsState()
    val allExpenses by viewModel.allExpenses.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Assessment, contentDescription = null, tint = PrimaryBlue)
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Reports & Analytics",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Track revenue, margins, expenses & stock value",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        }

        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> SalesAndProfitTab(
                todayRev = todayRev ?: 0.0,
                todayProf = todayProf ?: 0.0,
                monthlyRev = monthlyRev ?: 0.0,
                monthlyExp = monthlyExp ?: 0.0
            )
            1 -> ExpenseTrackerTab(
                expenses = allExpenses,
                todayExp = todayExp ?: 0.0,
                monthlyExp = monthlyExp ?: 0.0,
                onAddExpenseClick = onAddExpenseClick,
                onDeleteExpense = { viewModel.deleteExpense(it) }
            )
            2 -> InventoryValuationTab(products = allProducts)
        }
    }
}

@Composable
fun SalesAndProfitTab(
    todayRev: Double,
    todayProf: Double,
    monthlyRev: Double,
    monthlyExp: Double
) {
    val netMonthlyProfit = (monthlyRev * 0.18) - monthlyExp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        StatCard(
            title = "Today's Gross Sales",
            value = "₹${String.format("%.2f", todayRev)}",
            subtitle = "Estimated Profit: ₹${String.format("%.2f", todayProf)}",
            icon = Icons.Default.PointOfSale,
            iconBgColor = PrimaryBlue
        )

        StatCard(
            title = "Monthly Revenue (MTD)",
            value = "₹${String.format("%.2f", monthlyRev)}",
            subtitle = "Recorded shop sales",
            icon = Icons.Default.TrendingUp,
            iconBgColor = EmeraldGreen
        )

        StatCard(
            title = "Net Est. Monthly Profit",
            value = "₹${String.format("%.2f", netMonthlyProfit.coerceAtLeast(0.0))}",
            subtitle = "After subtracting ₹${String.format("%.2f", monthlyExp)} expenses",
            icon = Icons.Default.Assessment,
            iconBgColor = SapphireBlue
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Business Margin Analysis",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "• Average Gross Margin: 18.5% across Kirana and FMCG inventory.\n" +
                            "• High Margin Categories: Healthcare & Pharmacy (28-35%), Electronics (22%).\n" +
                            "• Volume Drivers: Atta, Cooking Oil, Dairy Products.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun ExpenseTrackerTab(
    expenses: List<com.example.data.local.entity.ExpenseEntity>,
    todayExp: Double,
    monthlyExp: Double,
    onAddExpenseClick: () -> Unit,
    onDeleteExpense: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                title = "Today's Expenses",
                value = "₹${String.format("%.2f", todayExp)}",
                icon = Icons.Default.MoneyOff,
                iconBgColor = Color(0xFFE11D48),
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Monthly Expenses",
                value = "₹${String.format("%.2f", monthlyExp)}",
                icon = Icons.Default.MoneyOff,
                iconBgColor = Color(0xFFF59E0B),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onAddExpenseClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE11D48))
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Add Rent / Power / Salary Expense")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Expense Log (${expenses.size})",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (expenses.isEmpty()) {
            Text("No shop expenses recorded yet.", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(expenses) { exp ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(exp.title, fontWeight = FontWeight.Bold)
                                Text("${exp.category} • ${exp.note}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("₹${exp.amount}", fontWeight = FontWeight.Bold, color = Color(0xFFE11D48))
                                IconButton(onClick = { onDeleteExpense(exp.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InventoryValuationTab(
    products: List<com.example.data.local.entity.ProductEntity>
) {
    var totalBuyValue = 0.0
    var totalSellValue = 0.0

    for (p in products) {
        totalBuyValue += p.purchasePrice * p.quantity
        totalSellValue += p.sellingPrice * p.quantity
    }

    val potentialProfit = (totalSellValue - totalBuyValue).coerceAtLeast(0.0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        StatCard(
            title = "Stock Investment (Buy)",
            value = "₹${String.format("%.2f", totalBuyValue)}",
            subtitle = "Total purchase cost of inventory",
            icon = Icons.Default.PointOfSale,
            iconBgColor = PrimaryBlue
        )

        StatCard(
            title = "Potential Selling Valuation",
            value = "₹${String.format("%.2f", totalSellValue)}",
            subtitle = "Expected revenue at retail price",
            icon = Icons.Default.TrendingUp,
            iconBgColor = EmeraldGreen
        )

        StatCard(
            title = "Locked Potential Profit",
            value = "₹${String.format("%.2f", potentialProfit)}",
            subtitle = "Expected profit upon full stock liquidation",
            icon = Icons.Default.Assessment,
            iconBgColor = SapphireBlue
        )
    }
}
