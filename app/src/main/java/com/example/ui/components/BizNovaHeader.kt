package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryBlue

@Composable
fun BizNovaHeader(
    shopName: String,
    businessType: String,
    unreadNotifs: Int,
    onAiClick: () -> Unit,
    onNotifClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // App Logo Box
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(PrimaryBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Storefront,
                        contentDescription = "BizNova OS",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = shopName.ifBlank { "BizNova" },
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            letterSpacing = (-0.3).sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = (businessType.ifBlank { "AI BUSINESS OS" }).uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp,
                            fontSize = 10.sp
                        ),
                        color = PrimaryBlue,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // AI Insight Button
                IconButton(onClick = onAiClick) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(PrimaryBlue.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI Assistant",
                            tint = PrimaryBlue,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Notification Bell
                IconButton(onClick = onNotifClick) {
                    BadgedBox(
                        badge = {
                            if (unreadNotifs > 0) {
                                Badge(containerColor = MaterialTheme.colorScheme.error) {
                                    Text(text = "$unreadNotifs", color = Color.White)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Profile Avatar "SK" / Store Initials
                val initials = shopName.split(" ")
                    .mapNotNull { it.firstOrNull()?.toString() }
                    .take(2)
                    .joinToString("")
                    .ifBlank { "SK" }

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(PrimaryBlue.copy(alpha = 0.15f))
                        .border(2.dp, Color.White, CircleShape)
                        .clickable { onProfileClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        ),
                        color = PrimaryBlue
                    )
                }
            }
            HorizontalDivider(color = BorderLight.copy(alpha = 0.6f), thickness = 1.dp)
        }
    }
}

