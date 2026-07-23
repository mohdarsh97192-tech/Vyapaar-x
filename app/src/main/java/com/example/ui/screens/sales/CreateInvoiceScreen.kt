package com.example.ui.screens.sales

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.PrimaryBlue
import com.example.viewmodel.BizNovaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateInvoiceScreen(
    viewModel: BizNovaViewModel,
    onBackClick: () -> Unit,
    onInvoiceCreated: (Long) -> Unit
) {
    val allProducts by viewModel.allProducts.collectAsState()
    val allCustomers by viewModel.allCustomers.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val selectedCust by viewModel.selectedCustomerForInvoice.collectAsState()
    val discountVal by viewModel.invoiceDiscount.collectAsState()
    val gstRate by viewModel.invoiceGstRate.collectAsState()
    val payMethod by viewModel.selectedPaymentMethod.collectAsState()

    var productSearchQuery by remember { mutableStateOf("") }
    var discountInput by remember { mutableStateOf(if (discountVal > 0) discountVal.toString() else "") }
    var showCustomerPicker by remember { mutableStateOf(false) }

    val filteredProducts = allProducts.filter { p ->
        p.name.contains(productSearchQuery, ignoreCase = true) ||
                p.sku.contains(productSearchQuery, ignoreCase = true) ||
                p.barcode.contains(productSearchQuery, ignoreCase = true)
    }

    var subtotal = 0.0
    for ((prod, qty) in cartItems) {
        subtotal += prod.sellingPrice * qty
    }
    val numericDiscount = discountInput.toDoubleOrNull() ?: 0.0
    val taxable = (subtotal - numericDiscount).coerceAtLeast(0.0)
    val gstAmount = taxable * (gstRate / 100.0)
    val grandTotal = taxable + gstAmount

    val paymentMethods = listOf("Cash", "UPI", "Card", "Udhar")
    val gstOptions = listOf(0.0, 5.0, 12.0, 18.0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create POS Invoice", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section 1: Customer Selection
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1. Customer Information",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { showCustomerPicker = !showCustomerPicker }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryBlue)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = selectedCust?.name ?: "Walk-in Customer",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            if (selectedCust != null) {
                                Text(
                                    text = "Phone: ${selectedCust!!.phone} • Udhar Bal: ₹${selectedCust!!.creditBalance}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            }
                        }
                        Text(
                            text = if (showCustomerPicker) "Close" else "Select Customer >",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = PrimaryBlue
                        )
                    }

                    if (showCustomerPicker) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.setSelectedCustomerForInvoice(null)
                                        showCustomerPicker = false
                                    },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
                            ) {
                                Text("Walk-in Customer (General)", modifier = Modifier.padding(10.dp))
                            }
                            allCustomers.forEach { cust ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.setSelectedCustomerForInvoice(cust)
                                            showCustomerPicker = false
                                        },
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(cust.name, fontWeight = FontWeight.SemiBold)
                                        Text("Udhar: ₹${cust.creditBalance}", color = Color(0xFFDC2626))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Section 2: Product Picker & Active Cart
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "2. Add Products to Invoice",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = productSearchQuery,
                        onValueChange = { productSearchQuery = it },
                        placeholder = { Text("Type item name to add...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Product Suggestions List
                    if (productSearchQuery.isNotBlank()) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            filteredProducts.take(4).forEach { prod ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .clickable {
                                            viewModel.addToCart(prod, 1)
                                            productSearchQuery = ""
                                        }
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(prod.name, fontWeight = FontWeight.Bold, maxLines = 1)
                                        Text("Stock: ${prod.quantity} ${prod.unit} • ₹${prod.sellingPrice}", style = MaterialTheme.typography.bodySmall)
                                    }
                                    Icon(Icons.Default.Add, contentDescription = "Add", tint = EmeraldGreen)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    // Cart Items List
                    if (cartItems.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Cart is empty. Search above to add items.", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            cartItems.forEach { (prod, qty) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(MaterialTheme.colorScheme.background)
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = prod.name,
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "₹${prod.sellingPrice} x $qty = ₹${prod.sellingPrice * qty}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = EmeraldGreen,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(
                                            onClick = { viewModel.updateCartQty(prod, qty - 1) },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = Color.Red)
                                        }
                                        Text(
                                            text = "$qty",
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp)
                                        )
                                        IconButton(
                                            onClick = { viewModel.updateCartQty(prod, qty + 1) },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(Icons.Default.Add, contentDescription = "Increase", tint = PrimaryBlue)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Section 3: Billing & Payment Method
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "3. Payment & GST Details",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    // Payment Method Chips
                    Text("Select Payment Method:", style = MaterialTheme.typography.labelMedium)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        paymentMethods.forEach { method ->
                            val isSelected = payMethod == method
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.setSelectedPaymentMethod(method) },
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) {
                                        if (method == "Udhar") Color(0xFFEF4444) else PrimaryBlue
                                    } else MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = method,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }

                    // GST Rate Selector
                    Text("GST Rate:", style = MaterialTheme.typography.labelMedium)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        gstOptions.forEach { rate ->
                            val isSelected = gstRate == rate
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.setInvoiceGstRate(rate) },
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) EmeraldGreen else MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${rate.toInt()}%",
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // Discount Input
                    OutlinedTextField(
                        value = discountInput,
                        onValueChange = {
                            discountInput = it
                            viewModel.setInvoiceDiscount(it.toDoubleOrNull() ?: 0.0)
                        },
                        label = { Text("Special Discount Amount (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Divider(modifier = Modifier.padding(vertical = 4.dp))

                    // Billing Calculation Summary
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Subtotal:", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                            Text("₹${String.format("%.2f", subtotal)}", fontWeight = FontWeight.SemiBold)
                        }
                        if (numericDiscount > 0) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Discount:", color = Color(0xFFDC2626))
                                Text("-₹${String.format("%.2f", numericDiscount)}", color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
                            }
                        }
                        if (gstAmount > 0) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("GST (${gstRate.toInt()}%):", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                                Text("+₹${String.format("%.2f", gstAmount)}", fontWeight = FontWeight.SemiBold)
                            }
                        }
                        Divider(modifier = Modifier.padding(vertical = 4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Grand Total:", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                            Text(
                                text = "₹${String.format("%.2f", grandTotal)}",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                color = EmeraldGreen
                            )
                        }
                    }
                }
            }

            // Finalize Button
            Button(
                onClick = {
                    if (cartItems.isNotEmpty()) {
                        viewModel.finalizeAndCreateInvoice { saleId ->
                            onInvoiceCreated(saleId)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = cartItems.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen)
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Generate Invoice & Digital Bill",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}
