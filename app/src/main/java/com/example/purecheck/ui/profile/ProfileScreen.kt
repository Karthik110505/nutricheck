package com.example.purecheck.ui.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import com.example.purecheck.Favorites
import com.example.purecheck.MyProfile
import com.example.purecheck.Settings
import com.example.purecheck.data.PureCheckPreferences
import com.example.purecheck.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigate: (NavKey) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs = remember { PureCheckPreferences(context) }

    var userProfile by remember { mutableStateOf(prefs.getUserProfile()) }
    var favoritesCount by remember { mutableStateOf(0) }

    // Section collapse states
    var myProfileExpanded by remember { mutableStateOf(true) }

    // Reload values when viewed
    LaunchedEffect(Unit) {
        userProfile = prefs.getUserProfile()
        favoritesCount = prefs.getFavorites().size
    }

    val darkBg = Color(0xFF0B0F19)
    val cardBg = Color(0xFF1E293B)
    val accentGreen = Color(0xFF10B981)
    val accentBlue = Color(0xFF3B82F6)

    val heightMeters = userProfile.height / 100f
    val bmi = if (heightMeters > 0) userProfile.weight / (heightMeters * heightMeters) else 0f
    val bmiString = if (bmi > 0) String.format("%.1f", bmi) else "--"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile Dashboard", color = Color.White, fontWeight = FontWeight.Black) },
                actions = {
                    IconButton(onClick = { onNavigate(Settings) }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = darkBg)
            )
        },
        containerColor = darkBg
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // User Info Card
            PremiumCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(accentGreen.copy(alpha = 0.15f))
                            .border(2.dp, accentGreen, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = accentGreen,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = userProfile.name,
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "PureCheck Profile",
                            color = Color.Gray,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = Color(0xFF1E2B44))
                Spacer(modifier = Modifier.height(16.dp))

                // Stats Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricWidget("Height", "${userProfile.height.toInt()} cm", Modifier.weight(1f))
                    MetricWidget("Weight", "${userProfile.weight.toInt()} kg", Modifier.weight(1f))
                    MetricWidget("BMI", bmiString, Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { onNavigate(MyProfile) },
                    colors = ButtonDefaults.buttonColors(containerColor = cardBg),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .border(1.dp, Color(0xFF1E2B44), RoundedCornerShape(12.dp))
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = accentGreen, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Edit Profile", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = { onNavigate(Favorites) },
                    colors = ButtonDefaults.buttonColors(containerColor = cardBg),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .border(1.dp, Color(0xFF1E2B44), RoundedCornerShape(12.dp))
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = accentGreen, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Favorites ($favoritesCount)", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ==========================================
            // SECTIONS (COLLAPSIBLE)
            // ==========================================

            // MY PROFILE SECTION
            CollapsibleSectionHeader(
                title = "MY BIOMETRICS",
                isExpanded = myProfileExpanded,
                onClick = { myProfileExpanded = !myProfileExpanded }
            )
            AnimatedVisibility(visible = myProfileExpanded) {
                PureCheckCard(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
                    ProfileItemRow("Name", userProfile.name)
                    ProfileItemRow("Age", "${userProfile.age} yrs")
                    ProfileItemRow("Gender", userProfile.gender)
                    ProfileItemRow("Height", "${userProfile.height.toInt()} cm")
                    ProfileItemRow("Weight", "${userProfile.weight.toInt()} kg")
                    ProfileItemRow("BMI", bmiString)
                }
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun MetricWidget(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(PureCheckColors.cardBg)
            .border(1.dp, Color(0xFF1E2B44), RoundedCornerShape(12.dp))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = label,
                color = PureCheckColors.textSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CollapsibleSectionHeader(
    title: String,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = Color(0xFF94A3B8),
            fontWeight = FontWeight.Black,
            fontSize = 12.sp
        )
        Icon(
            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}

@Composable
fun ProfileItemRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, fontSize = 13.sp)
        Text(value, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
    }
}
