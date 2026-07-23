package com.example.ui.screens.customers

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.PrimaryBlue
import com.example.viewmodel.BizNovaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDetailScreen(
    viewModel: BizNovaViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val customer by viewModel.selectedCustomerForDetail.collectAsState()
    val allSales by viewModel.allSales.collectAsState()

    val currentCustomer = customer ?: return
    val customerInvoices = allSales.filter { it.customerId == currentCustomer.id || it.customerName == currentCustomer.name }

    var showPaymentDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentCustomer.name, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.deleteCustomer(currentCustomer.id)
                        onBackClick()
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
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
            // Customer Header Profile
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(PrimaryBlue.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(28.dp))
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = currentCustomer.name,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = currentCustomer.phone,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                            if (currentCustomer.address.isNotBlank()) {
                                Text(
                                    text = currentCustomer.address,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total Pending Udhar Balance", style = MaterialTheme.typography.labelMedium)
                            Text(
                                text = "₹${String.format("%.2f", currentCustomer.creditBalance)}",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (currentCustomer.creditBalance > 0) Color(0xFFDC2626) else EmeraldGreen
                            )
                        }

                        Button(
                            onClick = { showPaymentDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Record Payment")
                        }
                    }
                }
            }

            // WhatsApp Payment Reminder CTA
            if (currentCustomer.creditBalance > 0) {
                Button(
                    onClick = {
                        val msg = "Dear ${currentCustomer.name}, gentle payment reminder from ${viewModel.currentUser.value?.businessName ?: "Sharma Kirana Store"}. Your total outstanding balance is ₹${currentCustomer.creditBalance}. Kindly pay via UPI/Cash."
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("https://api.whatsapp.com/send?phone=${currentCustomer.phone}&text=${Uri.encode(msg)}")
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
                ) {
                    Icon(Icons.Default.Message, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Send WhatsApp Payment Link / Reminder",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }

            // Invoice History for Customer
            Text(
                text = "Transaction History (${customerInvoices.size} Invoices)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            if (customerInvoices.isEmpty()) {
                Text(
                    text = "No recorded invoices for this customer yet.",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            } else {
                customerInvoices.forEach { sale ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(sale.invoiceNumber, fontWeight = FontWeight.Bold)
                                Text(sale.itemsSummary, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("₹${sale.netAmount}", fontWeight = FontWeight.Bold, color = EmeraldGreen)
                                Text(sale.paymentMethod, style = MaterialTheme.typography.labelSmall, color = PrimaryBlue)
                            }
                        }
                    }
                }
            }
        }

        if (showPaymentDialog) {
            var amountStr by remember { mutableStateOf("") }
            AlertDialog(
                onDismissRequest = { showPaymentDialog = false },
                title = { Text("Record Received Payment") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Current Udhar: ₹${currentCustomer.creditBalance}")
                        OutlinedTextField(
                            value = amountStr,
                            onValueChange = { amountStr = it },
                            label = { Text("Amount Received (₹)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val paid = amountStr.toDoubleOrNull() ?: 0.0
                            if (paid > 0) {
                                viewModel.updateCustomerCreditPayment(currentCustomer, paid)
                                Toast.makeText(context, "Payment of ₹$paid recorded!", Toast.LENGTH_SHORT).show()
                                showPaymentDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen)
                    ) {
                        Text("Save Payment")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showPaymentDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}
