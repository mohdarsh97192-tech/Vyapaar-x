package com.example.ui.screens.settings

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Storefront
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.PrimaryBlue
import com.example.viewmodel.BizNovaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: BizNovaViewModel,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val context = LocalContext.current
    val user by viewModel.currentUser.collectAsState()
    val isDark by viewModel.isDarkMode.collectAsState()
    val lang by viewModel.selectedLanguage.collectAsState()

    var name by remember(user) { mutableStateOf(user?.name ?: "Rajesh Sharma") }
    var phone by remember(user) { mutableStateOf(user?.phone ?: "+91 98765 43210") }
    var email by remember(user) { mutableStateOf(user?.email ?: "sharma.kirana@biznova.in") }
    var shopName by remember(user) { mutableStateOf(user?.businessName ?: "Sharma Kirana & General Store") }
    var bType by remember(user) { mutableStateOf(user?.businessType ?: "Kirana & General Store") }
    var gst by remember(user) { mutableStateOf(user?.gstNumber ?: "07AAAAA0000A1Z5") }
    var address by remember(user) { mutableStateOf(user?.address ?: "Shop No. 12, Sector 15 Market, New Delhi") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings & Business Profile", fontWeight = FontWeight.Bold) },
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
            // Firebase Cloud Sync Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = EmeraldGreen.copy(alpha = 0.12f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(EmeraldGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CloudDone, contentDescription = null, tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Cloud Sync Active",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = EmeraldGreen
                        )
                        Text(
                            text = "Firebase Auth & Firestore real-time cloud backup enabled.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            // Preferences
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("App Preferences", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.DarkMode, contentDescription = null, tint = PrimaryBlue)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Dark Theme Mode")
                        }
                        Switch(
                            checked = isDark,
                            onCheckedChange = { viewModel.toggleDarkMode() },
                            colors = SwitchDefaults.colors(checkedThumbColor = PrimaryBlue)
                        )
                    }

                    Divider(modifier = Modifier.padding(vertical = 10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Language, contentDescription = null, tint = EmeraldGreen)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Language (भाषा)")
                        }
                        Row {
                            Text(
                                text = if (lang == "hi") "हिंदी" else "English",
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue
                            )
                        }
                    }
                }
            }

            // Shop Profile Edit
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Storefront, contentDescription = null, tint = PrimaryBlue)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Business & GST Information", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    }

                    OutlinedTextField(
                        value = shopName,
                        onValueChange = { shopName = it },
                        label = { Text("Shop Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = bType,
                        onValueChange = { bType = it },
                        label = { Text("Business Category") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = gst,
                        onValueChange = { gst = it },
                        label = { Text("GSTIN") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Address") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Owner Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Mobile Phone") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Button(
                        onClick = {
                            viewModel.updateUserProfile(
                                name = name,
                                phone = phone,
                                email = email,
                                bName = shopName,
                                bType = bType,
                                gst = gst,
                                address = address
                            )
                            Toast.makeText(context, "Business profile saved successfully!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save Profile Changes")
                    }
                }
            }

            // Logout Button
            OutlinedButton(
                onClick = {
                    viewModel.logout()
                    onLogoutClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626))
            ) {
                Icon(Icons.Default.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logout Account", fontWeight = FontWeight.Bold)
            }
        }
    }
}
