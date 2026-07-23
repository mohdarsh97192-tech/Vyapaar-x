package com.example.ui.screens.ai

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.remote.BizNovaAiService
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.PrimaryBlue
import com.example.viewmodel.BizNovaViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiAssistantScreen(
    viewModel: BizNovaViewModel,
    onBackClick: () -> Unit
) {
    val lang by viewModel.selectedLanguage.collectAsState()
    val todayRev by viewModel.todayRevenue.collectAsState()
    val todayProf by viewModel.todayProfit.collectAsState()
    val monthlyRev by viewModel.monthlyRevenue.collectAsState()
    val lowStockList by viewModel.lowStockProducts.collectAsState()
    val pendingCredit by viewModel.totalPendingCredit.collectAsState()
    val user by viewModel.currentUser.collectAsState()

    var customPrompt by remember { mutableStateOf("") }
    var responseText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BizNova AI Advisor", fontWeight = FontWeight.Bold) },
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
            // Language Toggle Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.setLanguage("en") },
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (lang == "en") PrimaryBlue else Color.Transparent
                    )
                ) {
                    Box(modifier = Modifier.padding(10.dp), contentAlignment = Alignment.Center) {
                        Text("English", color = if (lang == "en") Color.White else MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
                    }
                }
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.setLanguage("hi") },
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (lang == "hi") EmeraldGreen else Color.Transparent
                    )
                ) {
                    Box(modifier = Modifier.padding(10.dp), contentAlignment = Alignment.Center) {
                        Text("हिंदी (Hindi)", color = if (lang == "hi") Color.White else MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Quick AI Action Cards
            Text("Select AI Insight Query:", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            isLoading = true
                            CoroutineScope(Dispatchers.Main).launch {
                                val summary = BizNovaAiService.getDailySummary(
                                    businessType = user?.businessType ?: "Kirana",
                                    todaySales = todayRev ?: 0.0,
                                    todayProfit = todayProf ?: 0.0,
                                    invoiceCount = 4,
                                    pendingUdhar = pendingCredit ?: 0.0,
                                    language = lang
                                )
                                responseText = summary
                                isLoading = false
                            }
                        },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = EmeraldGreen)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(if (lang == "hi") "दैनिक रिपोर्ट" else "Daily Summary", fontWeight = FontWeight.Bold)
                        Text(if (lang == "hi") "बिक्री और लाभ" else "Sales & Profit", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            isLoading = true
                            CoroutineScope(Dispatchers.Main).launch {
                                val lowNames = lowStockList.map { it.name }
                                val advice = BizNovaAiService.getInventoryAdvice(lowNames, lang)
                                responseText = advice
                                isLoading = false
                            }
                        },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Icon(Icons.Default.Inventory, contentDescription = null, tint = PrimaryBlue)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(if (lang == "hi") "स्टॉक सुझाव" else "Stock Reorder", fontWeight = FontWeight.Bold)
                        Text(if (lang == "hi") "कम स्टॉक चेतावनी" else "Low stock advice", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            isLoading = true
                            CoroutineScope(Dispatchers.Main).launch {
                                val analysis = BizNovaAiService.getSalesAnalysis(
                                    todaySales = todayRev ?: 0.0,
                                    monthSales = monthlyRev ?: 0.0,
                                    topPaymentMethod = "UPI",
                                    language = lang
                                )
                                responseText = analysis
                                isLoading = false
                            }
                        },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color(0xFF8B5CF6))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(if (lang == "hi") "बिक्री विश्लेषण" else "Growth Tips", fontWeight = FontWeight.Bold)
                        Text(if (lang == "hi") "मार्जिन बढ़ाएं" else "Increase Margin", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    }
                }
            }

            // Custom Question Input Field
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = customPrompt,
                    onValueChange = { customPrompt = it },
                    placeholder = { Text(if (lang == "hi") "बिजनेस संबंधी सवाल पूछें..." else "Ask any business question...") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (customPrompt.isNotBlank()) {
                            isLoading = true
                            CoroutineScope(Dispatchers.Main).launch {
                                val res = BizNovaAiService.getDailySummary(
                                    businessType = user?.businessType ?: "Kirana",
                                    todaySales = todayRev ?: 0.0,
                                    todayProfit = todayProf ?: 0.0,
                                    invoiceCount = 4,
                                    pendingUdhar = pendingCredit ?: 0.0,
                                    language = lang
                                )
                                responseText = "• $customPrompt Recommendation:\n\n$res"
                                customPrompt = ""
                                isLoading = false
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                    modifier = Modifier.height(52.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send")
                }
            }

            // Response Box
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(EmeraldGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (lang == "hi") "BizNova AI सलाहकार उत्तर" else "BizNova AI Executive Report",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = EmeraldGreen)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Analyzing sales, inventory & margins...", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    } else if (responseText.isNotBlank()) {
                        Text(
                            text = responseText,
                            style = MaterialTheme.typography.bodyLarge,
                            lineHeight = 22.sp
                        )
                    } else {
                        Text(
                            text = if (lang == "hi") "ऊपर दिए गए विकल्पों में से किसी एक पर टैप करें।" else "Tap one of the AI Insight buttons above or ask a question.",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}
