package com.example.purecheck.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.purecheck.data.PureCheckPreferences
import com.example.purecheck.ui.components.PureCheckColors
import com.example.purecheck.ui.components.PureCheckCard
import com.example.purecheck.ui.components.PrimaryActionButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs = remember { PureCheckPreferences(context) }

    // Profile States
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("21") }
    var gender by remember { mutableStateOf("Male") }
    var height by remember { mutableStateOf("175") }
    var weight by remember { mutableStateOf("70") }

    var showGenderMenu by remember { mutableStateOf(false) }
    var showSuccessText by remember { mutableStateOf(false) }

    // Load initial values
    LaunchedEffect(Unit) {
        val userProfile = prefs.getUserProfile()
        name = userProfile.name
        age = userProfile.age.toString()
        gender = userProfile.gender
        height = userProfile.height.toInt().toString()
        weight = userProfile.weight.toInt().toString()
    }

    val darkBg = Color(0xFF0B0F19)
    val accentGreen = Color(0xFF10B981)

    val hFloat = height.toFloatOrNull() ?: 175f
    val wFloat = weight.toFloatOrNull() ?: 70f
    val heightMeters = hFloat / 100f
    val bmi = if (heightMeters > 0) wFloat / (heightMeters * heightMeters) else 0f
    val bmiString = if (bmi > 0) String.format("%.1f", bmi) else "--"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Biometric Profile", color = Color.White, fontWeight = FontWeight.Bold) },
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
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Avatar Header
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(accentGreen.copy(alpha = 0.1f))
                        .border(2.dp, accentGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = accentGreen,
                        modifier = Modifier.size(54.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = name.ifBlank { "User Profile" },
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "BMI: $bmiString",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Personal Info Form
            Text("BIOMETRIC INFORMATION", color = accentGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            PureCheckCard(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Display Name") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = age,
                        onValueChange = { age = it },
                        label = { Text("Age (yrs)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                        modifier = Modifier.weight(1f)
                    )
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = gender,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Gender") },
                            trailingIcon = { Icon(Icons.Default.ArrowDropDown, null, tint = Color.White) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                            modifier = Modifier.fillMaxWidth().clickable { showGenderMenu = true }
                        )
                        DropdownMenu(
                            expanded = showGenderMenu,
                            onDismissRequest = { showGenderMenu = false },
                            modifier = Modifier.background(PureCheckColors.cardBg)
                        ) {
                            listOf("Male", "Female", "Other").forEach { g ->
                                DropdownMenuItem(
                                    text = { Text(g, color = Color.White) },
                                    onClick = {
                                        gender = g
                                        showGenderMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = height,
                        onValueChange = { height = it },
                        label = { Text("Height (cm)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = weight,
                        onValueChange = { weight = it },
                        label = { Text("Weight (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Save Buttons
            PrimaryActionButton(
                text = "Save Profile",
                onClick = {
                    val p = prefs.getUserProfile().copy(
                        name = name,
                        age = age.toIntOrNull() ?: 21,
                        gender = gender,
                        height = height.toFloatOrNull() ?: 175f,
                        weight = weight.toFloatOrNull() ?: 70f
                    )
                    prefs.saveUserProfile(p)
                    showSuccessText = true
                },
                modifier = Modifier.fillMaxWidth()
            )

            if (showSuccessText) {
                Text(
                    text = "Profile updated successfully!",
                    color = accentGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp).align(Alignment.CenterHorizontally)
                )
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}
