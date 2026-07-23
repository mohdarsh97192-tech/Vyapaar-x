package com.example.ui.screens.sales

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.PrimaryBlue
import com.example.viewmodel.BizNovaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceDetailScreen(
    viewModel: BizNovaViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val sale by viewModel.selectedSaleForDetail.collectAsState()
    val user by viewModel.currentUser.collectAsState()

    val currentSale = sale ?: return

    val dateFormatted = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(
        Date(currentSale.timestamp)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tax Invoice #${currentSale.invoiceNumber}", fontWeight = FontWeight.Bold) },
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
            // Receipt Card Styled like Thermal GST Bill
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = user?.businessName ?: "Sharma Kirana Store",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        ),
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = user?.address ?: "Shop No 12, Main Market, New Delhi",
                        style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                    if (!user?.gstNumber.isNullOrBlank()) {
                        Text(
                            text = "GSTIN: ${user?.gstNumber}",
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                            color = Color.DarkGray
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = Color.LightGray)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Invoice #: ${currentSale.invoiceNumber}",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                ),
                                color = Color.Black
                            )
                            Text(
                                text = "Date: $dateFormatted",
                                style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                                color = Color.Gray
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(
                                    if (currentSale.paymentMethod == "Udhar") Color(0xFFEF4444) else EmeraldGreen,
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = currentSale.paymentMethod.uppercase(),
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Customer: ${currentSale.customerName}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Monospace
                        ),
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = Color.LightGray)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Purchased Items Summary
                    Text(
                        text = "ITEMS PURCHASED",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        ),
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = currentSale.itemsSummary,
                        style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
                        color = Color.DarkGray,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = Color.LightGray)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Calculation Table
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Subtotal:", fontFamily = FontFamily.Monospace, color = Color.Gray)
                            Text("₹${String.format("%.2f", currentSale.totalAmount)}", fontFamily = FontFamily.Monospace)
                        }
                        if (currentSale.discountAmount > 0) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Discount:", fontFamily = FontFamily.Monospace, color = Color(0xFFDC2626))
                                Text("-₹${String.format("%.2f", currentSale.discountAmount)}", fontFamily = FontFamily.Monospace, color = Color(0xFFDC2626))
                            }
                        }
                        if (currentSale.gstAmount > 0) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("GST Amount:", fontFamily = FontFamily.Monospace, color = Color.Gray)
                                Text("+₹${String.format("%.2f", currentSale.gstAmount)}", fontFamily = FontFamily.Monospace)
                            }
                        }
                        Divider(modifier = Modifier.padding(vertical = 4.dp), color = Color.LightGray)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("TOTAL PAID:", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace))
                            Text(
                                text = "₹${String.format("%.2f", currentSale.netAmount)}",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace),
                                color = EmeraldGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Thank you for shopping with us!",
                        style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                        color = Color.Gray
                    )
                    Text(
                        text = "Powered by BizNova OS",
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                        color = PrimaryBlue
                    )
                }
            }

            // Share & Print Actions
            Button(
                onClick = {
                    val shareMsg = "Invoice #${currentSale.invoiceNumber} from ${user?.businessName ?: "Shop"}\n" +
                            "Customer: ${currentSale.customerName}\n" +
                            "Items: ${currentSale.itemsSummary}\n" +
                            "Total Amount: ₹${String.format("%.2f", currentSale.netAmount)}\n" +
                            "Payment Mode: ${currentSale.paymentMethod}\n" +
                            "Thank you!"

                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, shareMsg)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, "Share Receipt")
                    context.startActivity(shareIntent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen)
            ) {
                Icon(Icons.Default.Share, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Share WhatsApp / Digital Bill", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            }

            OutlinedButton(
                onClick = {
                    Toast.makeText(context, "Thermal POS Printer simulated. Invoice printed!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Print, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Print Receipt / PDF")
            }
        }
    }
}
