package com.example.purecheck.ui.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import com.example.purecheck.Author
import com.example.purecheck.MyProfile
import com.example.purecheck.data.PureCheckPreferences
import com.example.purecheck.ui.components.PureCheckColors
import com.example.purecheck.ui.components.PureCheckCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onNavigate: (NavKey) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs = remember { PureCheckPreferences(context) }
    val scrollState = rememberScrollState()

    // API Key States
    var apiKey by remember { mutableStateOf(prefs.getApiKey()) }
    var groqApiKey by remember { mutableStateOf(prefs.getGroqApiKey()) }
    var deepSeekApiKey by remember { mutableStateOf(prefs.getDeepSeekApiKey()) }
    var bytezApiKey by remember { mutableStateOf(prefs.getBytezApiKey()) }
    var activeProvider by remember { mutableStateOf(prefs.getProvider()) }

    // Dummy Notification state
    var notificationsEnabled by remember { mutableStateOf(true) }

    var showClearedMessage by remember { mutableStateOf(false) }

    val darkBg = Color(0xFF0B0F19)
    val cardBg = Color(0xFF1E293B)
    val accentGreen = Color(0xFF10B981)
    val accentBlue = Color(0xFF3B82F6)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
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
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // ==========================================
            // ACCOUNT SECTION
            // ==========================================
            SettingsHeader("ACCOUNT")
            PureCheckCard(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                SettingsRow(
                    title = "Edit Biometric Profile",
                    description = "Edit personal info, body metrics, and demographics",
                    icon = Icons.Default.Person,
                    onClick = { onNavigate(MyProfile) }
                )
            }

            // ==========================================
            // INTELLIGENCE CONFIGURATION SECTION
            // ==========================================
            SettingsHeader("INTELLIGENCE ENGINE")
            PureCheckCard(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                Text("Select Provider", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("gemini" to "Gemini", "groq" to "Groq", "deepseek" to "DeepSeek", "bytez" to "Bytez").forEach { (id, label) ->
                        val isSelected = activeProvider == id
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) accentGreen.copy(alpha = 0.2f) else PureCheckColors.cardBgElevated)
                                .clickable {
                                    activeProvider = id
                                    prefs.saveProvider(id)
                                }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(label, color = if (isSelected) accentGreen else Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                when (activeProvider) {
                    "gemini" -> {
                        Text("Gemini API Key", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        OutlinedTextField(
                            value = apiKey,
                            onValueChange = {
                                apiKey = it
                                prefs.saveApiKey(it)
                            },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    "groq" -> {
                        Text("Groq API Key", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        OutlinedTextField(
                            value = groqApiKey,
                            onValueChange = {
                                groqApiKey = it
                                prefs.saveGroqApiKey(it)
                            },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    "deepseek" -> {
                        Text("DeepSeek API Key", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        OutlinedTextField(
                            value = deepSeekApiKey,
                            onValueChange = {
                                deepSeekApiKey = it
                                prefs.saveDeepSeekApiKey(it)
                            },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    "bytez" -> {
                        Text("Bytez API Key", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        OutlinedTextField(
                            value = bytezApiKey,
                            onValueChange = {
                                bytezApiKey = it
                                prefs.saveBytezApiKey(it)
                            },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // ==========================================
            // APP SECTION
            // ==========================================
            SettingsHeader("APP CONFIGURATION")
            PureCheckCard(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                SettingsToggleRow(
                    title = "Push Notifications",
                    description = "Notify when scanning allergen products",
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it }
                )
                Divider(color = Color(0xFF334155), modifier = Modifier.padding(vertical = 4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Active Theme", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        Text("NutriCheck Dark Theme active", color = Color.Gray, fontSize = 10.sp)
                    }
                    Text("DARK", color = accentGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Divider(color = Color(0xFF334155), modifier = Modifier.padding(vertical = 4.dp))
                
                // Clear scan history inside card list
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Data Management", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        Text("Wipes out local scan history records", color = Color.Gray, fontSize = 10.sp)
                    }
                    TextButton(
                        onClick = {
                            prefs.clearHistory()
                            showClearedMessage = true
                        }
                    ) {
                        Text("Clear History", color = Color.Red, fontWeight = FontWeight.Bold)
                    }
                }
                if (showClearedMessage) {
                    Text("History wiped successfully!", color = Color.Red, fontSize = 11.sp)
                }
            }

            // ==========================================
            // ABOUT SECTION
            // ==========================================
            SettingsHeader("ABOUT & SUPPORT")
            PureCheckCard(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("App Version", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Text("v2.0", color = Color.Gray, fontSize = 13.sp)
                }
                Divider(color = Color(0xFF334155), modifier = Modifier.padding(vertical = 4.dp))
                SettingsRow(
                    title = "About NutriCheck",
                    description = "Developer credits, institution, and project details",
                    icon = Icons.Default.Info,
                    onClick = { onNavigate(Author) }
                )
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun SettingsHeader(title: String) {
    Text(
        text = title,
        color = Color(0xFF94A3B8),
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
    )
}

@Composable
fun SettingsRow(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF10B981),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            Text(description, color = Color.Gray, fontSize = 10.sp)
        }
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun SettingsToggleRow(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            Text(description, color = Color.Gray, fontSize = 10.sp, lineHeight = 14.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF10B981)
            )
        )
    }
}
