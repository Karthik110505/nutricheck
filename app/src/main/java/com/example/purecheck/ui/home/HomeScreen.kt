package com.example.purecheck.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import com.example.purecheck.Result
import com.example.purecheck.Scanner
import com.example.purecheck.data.*
import com.example.purecheck.ui.components.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigate: (NavKey) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs = remember { PureCheckPreferences(context) }
    var history by remember { mutableStateOf(emptyList<HistoryItem>()) }
    var userProfile by remember { mutableStateOf(prefs.getUserProfile()) }

    // Load states when viewed
    LaunchedEffect(Unit) {
        history = prefs.getHistory()
        userProfile = prefs.getUserProfile()
    }

    val totalScans = history.size
    val averageScore = if (history.isEmpty()) 0 else (history.sumOf { it.overallScore } / history.size)
    
    val flaggedIssue = remember(history) {
        if (history.isEmpty()) {
            "None"
        } else {
            val counts = mutableMapOf<String, Int>()
            history.forEach { item ->
                try {
                    val obj = JSONObject(item.jsonResult)
                    val risks = obj.optJSONArray("healthRisks")
                    if (risks != null) {
                        for (i in 0 until risks.length()) {
                            val risk = risks.getString(i).lowercase()
                            when {
                                risk.contains("sodium") || risk.contains("salt") -> counts["High Sodium"] = (counts["High Sodium"] ?: 0) + 1
                                risk.contains("sugar") || risk.contains("sweetener") -> counts["High Sugar"] = (counts["High Sugar"] ?: 0) + 1
                                risk.contains("palm") || risk.contains("saturated fat") -> counts["Palm Oil/Fats"] = (counts["Palm Oil/Fats"] ?: 0) + 1
                                risk.contains("additive") || risk.contains("preservative") -> counts["Additives"] = (counts["Additives"] ?: 0) + 1
                                risk.contains("animal") || risk.contains("gelatin") -> counts["Non-Vegan Content"] = (counts["Non-Vegan Content"] ?: 0) + 1
                            }
                        }
                    }
                } catch (e: Exception) {}
            }
            counts.maxByOrNull { it.value }?.key ?: "None Detected"
        }
    }

    val insightText = remember(averageScore, totalScans, flaggedIssue) {
        when {
            totalScans == 0 -> "Scan your first food item using ingredients or barcode scanner to begin tracking your food choices."
            averageScore >= 80 -> "Fantastic! Your average food rating is A/B. You are choosing highly clean and natural foods."
            averageScore >= 65 -> "Your average food rating is B/C. Reducing processed snacks with high sodium or hidden sugars will improve your health metrics."
            else -> "Warning: Your scanned products have an average score below 60. Try avoiding items flagged with '$flaggedIssue' and check clean alternatives."
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PureCheckColors.darkBg)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        // Welcome and Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                NutriCheckWordmark(showTagline = false)
                Text(
                    text = "Welcome back, ${userProfile.name}",
                    color = PureCheckColors.textSecondary,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Standard Dashboard view
        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            PremiumCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "Your Food Snapshot",
                        color = PureCheckColors.textPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Average Score", color = PureCheckColors.textSecondary, fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (totalScans == 0) "--" else "$averageScore",
                                color = if (averageScore >= 75) PureCheckColors.accentGreen else if (averageScore >= 55) PureCheckColors.warningYellow else PureCheckColors.errorRed,
                                fontWeight = FontWeight.Black,
                                fontSize = 24.sp
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Total Scans", color = PureCheckColors.textSecondary, fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$totalScans",
                                color = PureCheckColors.textPrimary,
                                fontWeight = FontWeight.Black,
                                fontSize = 24.sp
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Most Flagged", color = PureCheckColors.textSecondary, fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = flaggedIssue,
                                color = if (flaggedIssue == "None") PureCheckColors.accentGreen else PureCheckColors.warningOrange,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            PureCheckSectionHeader(title = "Quick Scan")
            Spacer(modifier = Modifier.height(8.dp))
            PrimaryActionButton(
                text = "Scan Ingredients / Barcode",
                onClick = { onNavigate(Scanner) },
                icon = Icons.Default.Search,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))
            PureCheckSectionHeader(title = "Personalized Health Insight")
            Spacer(modifier = Modifier.height(8.dp))
            PureCheckCard(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = PureCheckColors.accentGreen, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(insightText, color = PureCheckColors.textSecondary, fontSize = 13.sp, lineHeight = 18.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            PureCheckSectionHeader(title = "Recent Scans")
            Spacer(modifier = Modifier.height(8.dp))

            if (history.isEmpty()) {
                EmptyState(
                    title = "No scans yet",
                    description = "Get started by scanning the ingredient list or barcode of any package.",
                    icon = Icons.Default.Search,
                    actionLabel = "Scan Now",
                    onActionClick = { onNavigate(Scanner) }
                )
            } else {
                val recentItems = history.take(5)
                val sdf = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())
                recentItems.forEach { item ->
                    ScanHistoryItem(
                        name = item.productName,
                        score = item.overallScore,
                        grade = item.overallGrade,
                        dateString = sdf.format(Date(item.timestamp)),
                        onClick = { onNavigate(Result(item.jsonResult)) },
                        onDelete = {
                            prefs.deleteHistoryItem(item.id)
                            history = prefs.getHistory()
                        },
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}
