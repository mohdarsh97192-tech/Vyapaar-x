package com.example.ui.screens.inventory

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.ProductEntity
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.PrimaryBlue
import com.example.viewmodel.BizNovaViewModel

@Composable
fun InventoryScreen(
    viewModel: BizNovaViewModel
) {
    val products by viewModel.allProducts.collectAsState()
    val lowStockList by viewModel.lowStockProducts.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var editingProduct by remember { mutableStateOf<ProductEntity?>(null) }

    val categories = listOf("All", "Groceries", "Medicines", "Dairy", "Snacks", "Electronics", "Cleaning", "Other")

    val filteredProducts = products.filter { p ->
        val matchesSearch = p.name.contains(searchQuery, ignoreCase = true) ||
                p.sku.contains(searchQuery, ignoreCase = true) ||
                p.barcode.contains(searchQuery, ignoreCase = true)
        val matchesCategory = selectedCategory == "All" || p.category == selectedCategory
        matchesSearch && matchesCategory
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingProduct = null
                    showAddDialog = true
                },
                containerColor = PrimaryBlue,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Product")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Inventory, contentDescription = null, tint = PrimaryBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Inventory Catalog",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "${products.size} Products listed • ${lowStockList.size} Low Stock",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }

            // Search Bar & Barcode Scanner Simulation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = { Text("Search product, SKU or Barcode...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        // Barcode Scan Simulation: autofill demo barcode
                        viewModel.setSearchQuery("8901058852312")
                    },
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(PrimaryBlue.copy(alpha = 0.15f))
                ) {
                    Icon(Icons.Default.QrCodeScanner, contentDescription = "Scan Barcode", tint = PrimaryBlue)
                }
            }

            // Categories Filter Bar
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    Card(
                        modifier = Modifier.clickable { viewModel.setSelectedCategory(cat) },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) PrimaryBlue else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text(
                            text = cat,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }

            // Low Stock Warning Banner
            if (lowStockList.isNotEmpty() && selectedCategory == "All" && searchQuery.isBlank()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFDC2626))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Low Stock Alert: ${lowStockList.size} items require reordering immediately.",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = Color(0xFF991B1B)
                        )
                    }
                }
            }

            // Product List
            if (filteredProducts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No products found matching criteria.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    items(filteredProducts) { product ->
                        val isLowStock = product.quantity <= product.lowStockThreshold
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isLowStock) Color(0xFFFEE2E2) else PrimaryBlue.copy(alpha = 0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Inventory,
                                        contentDescription = null,
                                        tint = if (isLowStock) Color(0xFFDC2626) else PrimaryBlue
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = product.name,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "SKU: ${product.sku.ifBlank { "N/A" }} • Category: ${product.category}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Sell: ₹${product.sellingPrice}",
                                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                            color = EmeraldGreen
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "(Buy: ₹${product.purchasePrice})",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                        )
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(if (isLowStock) Color(0xFFEF4444) else EmeraldGreen)
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "${product.quantity} ${product.unit}",
                                            color = Color.White,
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                        )
                                    }
                                    Row {
                                        IconButton(onClick = {
                                            editingProduct = product
                                            showAddDialog = true
                                        }) {
                                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = PrimaryBlue, modifier = Modifier.size(20.dp))
                                        }
                                        IconButton(onClick = { viewModel.deleteProduct(product.id) }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFE11D48), modifier = Modifier.size(20.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }

        if (showAddDialog) {
            AddOrEditProductDialog(
                product = editingProduct,
                onDismiss = { showAddDialog = false },
                onSave = { name, sku, barcode, category, purchase, selling, qty, lowLimit, unit ->
                    viewModel.saveProduct(
                        id = editingProduct?.id ?: 0L,
                        name = name,
                        sku = sku,
                        barcode = barcode,
                        category = category,
                        pPrice = purchase,
                        sPrice = selling,
                        qty = qty,
                        lowLimit = lowLimit,
                        unit = unit
                    )
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun AddOrEditProductDialog(
    product: ProductEntity?,
    onDismiss: () -> Unit,
    onSave: (
        name: String,
        sku: String,
        barcode: String,
        category: String,
        purchasePrice: Double,
        sellingPrice: Double,
        quantity: Int,
        lowLimit: Int,
        unit: String
    ) -> Unit
) {
    var name by remember { mutableStateOf(product?.name ?: "") }
    var sku by remember { mutableStateOf(product?.sku ?: "") }
    var barcode by remember { mutableStateOf(product?.barcode ?: "") }
    var category by remember { mutableStateOf(product?.category ?: "Groceries") }
    var purchasePriceStr by remember { mutableStateOf(product?.purchasePrice?.toString() ?: "") }
    var sellingPriceStr by remember { mutableStateOf(product?.sellingPrice?.toString() ?: "") }
    var qtyStr by remember { mutableStateOf(product?.quantity?.toString() ?: "10") }
    var lowLimitStr by remember { mutableStateOf(product?.lowStockThreshold?.toString() ?: "5") }
    var unit by remember { mutableStateOf(product?.unit ?: "pcs") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = if (product == null) "Add New Product" else "Edit Product")
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Product Name *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Category") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = unit,
                        onValueChange = { unit = it },
                        label = { Text("Unit (pcs/kg/pkt)") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = purchasePriceStr,
                        onValueChange = { purchasePriceStr = it },
                        label = { Text("Buy Price (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = sellingPriceStr,
                        onValueChange = { sellingPriceStr = it },
                        label = { Text("Sell Price (₹) *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = qtyStr,
                        onValueChange = { qtyStr = it },
                        label = { Text("Quantity") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = lowLimitStr,
                        onValueChange = { lowLimitStr = it },
                        label = { Text("Low Stock Limit") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = barcode,
                        onValueChange = { barcode = it },
                        label = { Text("Barcode / EAN") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = sku,
                        onValueChange = { sku = it },
                        label = { Text("SKU Code") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onSave(
                            name,
                            sku,
                            barcode,
                            category,
                            purchasePriceStr.toDoubleOrNull() ?: 0.0,
                            sellingPriceStr.toDoubleOrNull() ?: 0.0,
                            qtyStr.toIntOrNull() ?: 0,
                            lowLimitStr.toIntOrNull() ?: 5,
                            unit
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Text("Save Product")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
