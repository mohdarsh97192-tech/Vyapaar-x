package com.example.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.entity.SaleEntity
import com.example.ui.components.BizNovaHeader
import com.example.ui.components.StatCard
import com.example.ui.theme.BorderLight
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.EmeraldGreenLight
import com.example.ui.theme.IndigoGradientStart
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.TextMuted
import com.example.ui.theme.SapphireBlue
import com.example.viewmodel.BizNovaViewModel

@Composable
fun HomeScreen(
    viewModel: BizNovaViewModel,
    onCreateInvoiceClick: () -> Unit,
    onAddProductClick: () -> Unit,
    onAddCustomerClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    onAiAssistantClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSaleClick: (SaleEntity) -> Unit,
    onInventoryClick: () -> Unit,
    onCustomersClick: () -> Unit
) {
    val user by viewModel.currentUser.collectAsState()
    val todayRev by viewModel.todayRevenue.collectAsState()
    val todayProf by viewModel.todayProfit.collectAsState()
    val pendingCredit by viewModel.totalPendingCredit.collectAsState()
    val custCount by viewModel.customerCount.collectAsState()
    val prodCount by viewModel.productCount.collectAsState()
    val monthlyRev by viewModel.monthlyRevenue.collectAsState()
    val recentSales by viewModel.todaySales.collectAsState()
    val lowStockList by viewModel.lowStockProducts.collectAsState()
    val unreadNotifs by viewModel.unreadNotifCount.collectAsState()
    val aiSummary by viewModel.aiSummaryText.collectAsState()
    val isAiLoading by viewModel.isAiLoading.collectAsState()

    LaunchedEffect(Unit) {
        if (aiSummary == null) {
            viewModel.fetchAiDailySummary()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            BizNovaHeader(
                shopName = user?.businessName ?: "Sharma Kirana Store",
                businessType = user?.businessType ?: "Kirana & General Store",
                unreadNotifs = unreadNotifs,
                onAiClick = onAiAssistantClick,
                onNotifClick = onNotificationClick,
                onProfileClick = onProfileClick
            )
        }

        // Today's Revenue Hero Card (Professional Polish Style)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .border(
                        1.dp,
                        BorderLight,
                        RoundedCornerShape(24.dp)
                    ),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TODAY'S REVENUE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = com.example.ui.theme.TextMuted
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(com.example.ui.theme.EmeraldGreenLight)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.TrendingUp,
                                    contentDescription = null,
                                    tint = com.example.ui.theme.EmeraldGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "+12.5%",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = com.example.ui.theme.EmeraldGreen
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "₹${String.format("%.2f", todayRev ?: 0.0)}",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 32.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    androidx.compose.material3.HorizontalDivider(
                        color = com.example.ui.theme.BorderLight.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Est. Profit",
                                style = MaterialTheme.typography.labelMedium,
                                color = com.example.ui.theme.TextMuted
                            )
                            Text(
                                text = "₹${String.format("%.2f", todayProf ?: 0.0)}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = com.example.ui.theme.EmeraldGreen
                            )
                        }

                        Column {
                            Text(
                                text = "Today's Invoices",
                                style = MaterialTheme.typography.labelMedium,
                                color = com.example.ui.theme.TextMuted
                            )
                            Text(
                                text = "${recentSales.size} Invoices",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = PrimaryBlue
                            )
                        }

                        Button(
                            onClick = onCreateInvoiceClick,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("New Sale", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
        }

        // Quick Action Bar
        item {
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    QuickActionChip(
                        label = "+ Invoice",
                        icon = Icons.Default.PointOfSale,
                        color = PrimaryBlue,
                        onClick = onCreateInvoiceClick
                    )
                }
                item {
                    QuickActionChip(
                        label = "+ Product",
                        icon = Icons.Default.AddShoppingCart,
                        color = EmeraldGreen,
                        onClick = onAddProductClick
                    )
                }
                item {
                    QuickActionChip(
                        label = "+ Customer",
                        icon = Icons.Default.Group,
                        color = SapphireBlue,
                        onClick = onAddCustomerClick
                    )
                }
                item {
                    QuickActionChip(
                        label = "+ Expense",
                        icon = Icons.Default.MoneyOff,
                        color = Color(0xFFE11D48),
                        onClick = onAddExpenseClick
                    )
                }
            }
        }

        // Low Stock Alert Banner
        if (lowStockList.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .clickable { onInventoryClick() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEF4444)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Low Stock Alert (${lowStockList.size} Items)",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF991B1B)
                            )
                            Text(
                                text = lowStockList.take(2).joinToString { it.name },
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF7F1D1D),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Text(
                            text = "Reorder >",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFFDC2626)
                        )
                    }
                }
            }
        }

        // Dashboard Metrics Grid
        item {
            Text(
                text = "Today's Business Summary",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 8.dp)
            )

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    StatCard(
                        title = "Today's Sales",
                        value = "₹${String.format("%.2f", todayRev ?: 0.0)}",
                        subtitle = "Recorded Sales",
                        icon = Icons.Default.CurrencyRupee,
                        iconBgColor = PrimaryBlue,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Today's Profit",
                        value = "₹${String.format("%.2f", todayProf ?: 0.0)}",
                        subtitle = "Est. Margin",
                        icon = Icons.Default.TrendingUp,
                        iconBgColor = EmeraldGreen,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    StatCard(
                        title = "Pending Udhar",
                        value = "₹${String.format("%.2f", pendingCredit ?: 0.0)}",
                        subtitle = "Uncollected Credit",
                        icon = Icons.Default.MoneyOff,
                        iconBgColor = Color(0xFFF59E0B),
                        modifier = Modifier.weight(1f),
                        onClick = onCustomersClick
                    )
                    StatCard(
                        title = "Monthly Revenue",
                        value = "₹${String.format("%.2f", monthlyRev ?: 0.0)}",
                        subtitle = "This Month",
                        icon = Icons.Default.PointOfSale,
                        iconBgColor = SapphireBlue,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    StatCard(
                        title = "Total Customers",
                        value = "$custCount",
                        subtitle = "Khata Accounts",
                        icon = Icons.Default.Group,
                        iconBgColor = Color(0xFF8B5CF6),
                        modifier = Modifier.weight(1f),
                        onClick = onCustomersClick
                    )
                    StatCard(
                        title = "Total Products",
                        value = "$prodCount",
                        subtitle = "Active Catalog",
                        icon = Icons.Default.Inventory,
                        iconBgColor = Color(0xFF06B6D4),
                        modifier = Modifier.weight(1f),
                        onClick = onInventoryClick
                    )
                }
            }
        }

        // AI Business Advisor Card (Professional Polish Indigo Banner)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    com.example.ui.theme.IndigoGradientStart,
                                    com.example.ui.theme.PrimaryBlue
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White.copy(alpha = 0.2f))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "AI INSIGHTS",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp
                                        ),
                                        color = Color.White
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            if (isAiLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp,
                                    color = Color.White
                                )
                            } else {
                                IconButton(
                                    onClick = { viewModel.fetchAiDailySummary() },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        Icons.Default.AutoAwesome,
                                        contentDescription = "Refresh",
                                        tint = Color.White
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = if (!aiSummary.isNullOrBlank()) aiSummary!! else "Analyzing store sales, stock velocity, and customer payment trends...",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                lineHeight = 20.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = onAiAssistantClick,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = com.example.ui.theme.PrimaryBlue
                            )
                        ) {
                            Text(
                                "Ask BizNova AI Assistant",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }

        // Recent Transactions Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Transactions",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "View All",
                    style = MaterialTheme.typography.labelLarge,
                    color = PrimaryBlue,
                    modifier = Modifier.clickable { onCreateInvoiceClick() }
                )
            }
        }

        // Recent Transactions List
        if (recentSales.isEmpty()) {
            item {
                Text(
                    text = "No invoices generated today yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        } else {
            items(recentSales.take(5)) { sale ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clickable { onSaleClick(sale) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(PrimaryBlue.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Receipt, contentDescription = null, tint = PrimaryBlue)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = sale.customerName,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "${sale.invoiceNumber} • ${sale.paymentMethod}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "₹${String.format("%.2f", sale.netAmount)}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = EmeraldGreen
                            )
                            Text(
                                text = sale.paymentStatus,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (sale.paymentStatus == "Paid") EmeraldGreen else Color(0xFFDC2626)
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
fun QuickActionChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = label, color = Color.White, style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
        }
    }
}
