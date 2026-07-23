package com.example.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.PrimaryBlue

@Composable
fun BusinessSetupScreen(
    initialName: String,
    initialPhone: String,
    onSetupComplete: (
        name: String,
        phone: String,
        email: String,
        bName: String,
        bType: String,
        gst: String,
        address: String
    ) -> Unit
) {
    val businessTypes = listOf(
        "Kirana & General Store",
        "Medical & Pharmacy Store",
        "Hardware & Electricals",
        "Mobile & Electronics Shop",
        "Garments & Cloth Store",
        "Restaurant & Food Corner",
        "Small Manufacturer"
    )

    var selectedType by remember { mutableStateOf(businessTypes[0]) }
    var shopName by remember { mutableStateOf("Sharma Kirana Store") }
    var gstNo by remember { mutableStateOf("07AAAAA0000A1Z5") }
    var addressText by remember { mutableStateOf("Shop 12, Main Market, Sector 15, New Delhi") }
    var emailText by remember { mutableStateOf("sharma.store@biznova.in") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Business,
                contentDescription = null,
                tint = PrimaryBlue,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "Business Profile Setup",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
        }

        Text(
            text = "Tailor BizNova OS for your specific shop category and print custom invoices.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        Text(
            text = "Select Business Category:",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(businessTypes) { type ->
                val isSelected = selectedType == type
                Card(
                    modifier = Modifier.clickable { selectedType = type },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) PrimaryBlue else MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                        Text(
                            text = type,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }

        OutlinedTextField(
            value = shopName,
            onValueChange = { shopName = it },
            label = { Text("Shop / Business Name *") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = gstNo,
            onValueChange = { gstNo = it },
            label = { Text("GSTIN Number (Optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = addressText,
            onValueChange = { addressText = it },
            label = { Text("Shop Address") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = emailText,
            onValueChange = { emailText = it },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onSetupComplete(
                    initialName,
                    initialPhone,
                    emailText,
                    shopName,
                    selectedType,
                    gstNo,
                    addressText
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen)
        ) {
            Text(
                text = "Save & Open Business Dashboard",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}
