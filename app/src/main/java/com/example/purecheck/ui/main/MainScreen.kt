package com.example.purecheck.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import com.example.purecheck.Scanner
import com.example.purecheck.Settings
import com.example.purecheck.Result
import com.example.purecheck.Author
import com.example.purecheck.data.HistoryItem
import com.example.purecheck.data.PureCheckPreferences
import com.example.purecheck.ui.components.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigate: (NavKey) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs = remember { PureCheckPreferences(context) }
    var history by remember { mutableStateOf(emptyList<HistoryItem>()) }
    var apiKeySet by remember { mutableStateOf(false) }

    var showEditDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<HistoryItem?>(null) }
    var editTitleText by remember { mutableStateOf("") }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var deletingItem by remember { mutableStateOf<HistoryItem?>(null) }

    // Load history when screen is viewed
    LaunchedEffect(Unit) {
        history = prefs.getHistory()
        val provider = prefs.getProvider()
        val activeKey = if (provider.lowercase() == "groq") prefs.getGroqApiKey() else prefs.getApiKey()
        apiKeySet = activeKey.isNotBlank()
    }

    val darkBg = Color(0xFF0B0F19)
    val cardBg = Color(0xFF1E293B)
    val accentGreen = Color(0xFF10B981)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    NutriCheckWordmark(showTagline = false)
                },
                actions = {
                    IconButton(onClick = { onNavigate(Author) }) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "About Author",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { onNavigate(Settings) }) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = darkBg)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (apiKeySet) {
                        onNavigate(Scanner)
                    } else {
                        onNavigate(Settings)
                    }
                },
                containerColor = accentGreen,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Search, contentDescription = "Scan Product", modifier = Modifier.size(28.dp))
            }
        },
        containerColor = darkBg
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Welcome Section
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B))
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        "Know What You Eat.",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Evaluate food packages, scan chemical additives, unmask hidden ingredients, and verify dietary alerts.",
                        color = Color.LightGray,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // History Label
            Text(
                "Scan History",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (!apiKeySet) {
                // Warning if API Key is not configured
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF7F1D1D)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "API Key Required",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Please go to Settings and enter your free Gemini API Key to enable scanning.",
                            color = Color(0xFFFECACA),
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { onNavigate(Settings) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Configure Now", color = Color(0xFF7F1D1D), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            if (history.isEmpty()) {
                EmptyState(
                    title = "No scanned products yet",
                    description = "Take photos of ingredient lists or scan barcodes to evaluate food safety.",
                    icon = Icons.Default.Search,
                    actionLabel = "Scan First Product",
                    onActionClick = {
                        if (apiKeySet) {
                            onNavigate(Scanner)
                        } else {
                            onNavigate(Settings)
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(history) { item ->
                        HistoryCard(
                            item = item,
                            onClick = {
                                onNavigate(Result(item.jsonResult))
                            },
                            onEditClick = {
                                editingItem = item
                                editTitleText = item.productName
                                showEditDialog = true
                            },
                            onDeleteClick = {
                                deletingItem = item
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    if (showEditDialog && editingItem != null) {
        AlertDialog(
            onDismissRequest = {
                showEditDialog = false
                editingItem = null
            },
            title = {
                Text("Rename Scan", color = Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text("Enter a new title for this scan:", color = Color.LightGray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editTitleText,
                        onValueChange = { editTitleText = it },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = accentGreen,
                            unfocusedBorderColor = Color.Gray
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val item = editingItem
                        if (item != null && editTitleText.isNotBlank()) {
                            prefs.updateHistoryItemTitle(item.id, editTitleText)
                            history = prefs.getHistory()
                        }
                        showEditDialog = false
                        editingItem = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = accentGreen)
                ) {
                    Text("Save", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showEditDialog = false
                        editingItem = null
                    }
                ) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            containerColor = cardBg
        )
    }

    if (showDeleteDialog && deletingItem != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                deletingItem = null
            },
            title = {
                Text("Delete Scan", color = Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "Are you sure you want to delete the scan history for \"${deletingItem?.productName}\"?",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val item = deletingItem
                        if (item != null) {
                            prefs.deleteHistoryItem(item.id)
                            history = prefs.getHistory()
                        }
                        showDeleteDialog = false
                        deletingItem = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        deletingItem = null
                    }
                ) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            containerColor = cardBg
        )
    }
}

@Composable
fun HistoryCard(item: HistoryItem, onClick: () -> Unit, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    val cardBg = Color(0xFF1E293B)
    val gradeColor = when (item.overallGrade) {
        "A" -> Color(0xFF10B981)
        "B" -> Color(0xFF34D399)
        "C" -> Color(0xFFF59E0B)
        "D" -> Color(0xFFF97316)
        else -> Color(0xFFEF4444)
    }

    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    val dateString = sdf.format(Date(item.timestamp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(cardBg)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Grade Indicator
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(gradeColor.copy(alpha = 0.15f))
                .border(2.dp, gradeColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                item.overallGrade,
                color = gradeColor,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Text Info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                item.productName,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                "Score: ${item.overallScore}/100",
                color = Color.LightGray,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                dateString,
                color = Color.Gray,
                fontSize = 10.sp
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onEditClick,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Rename Scan",
                    tint = Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Scan",
                    tint = Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
