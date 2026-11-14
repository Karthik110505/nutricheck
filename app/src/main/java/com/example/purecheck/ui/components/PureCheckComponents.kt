package com.example.purecheck.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min

// Centralized Colors for Google-quality design system
object PureCheckColors {
    val darkBg = Color(0xFF090D16)
    val cardBg = Color(0xFF131C2E)
    val cardBgElevated = Color(0xFF1E2B44)
    val accentGreen = Color(0xFF10B981)
    val accentBlue = Color(0xFF3B82F6)
    val warningYellow = Color(0xFFF59E0B)
    val warningOrange = Color(0xFFF97316)
    val errorRed = Color(0xFFEF4444)
    val textPrimary = Color(0xFFFFFFFF)
    val textSecondary = Color(0xFF94A3B8)
    val textMuted = Color(0xFF475569)
    val cardBorder = Color(0xFF1E2B44)
}

@Composable
fun PureCheckCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    backgroundColor: Color = PureCheckColors.cardBg,
    content: @Composable ColumnScope.() -> Unit
) {
    val cardModifier = if (onClick != null) {
        modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .border(1.dp, Color(0xFF1E2B44), RoundedCornerShape(16.dp))
            .clickable { onClick() }
    } else {
        modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .border(1.dp, Color(0xFF1E2B44), RoundedCornerShape(16.dp))
    }
    Column(
        modifier = cardModifier.padding(16.dp),
        content = content
    )
}

@Composable
fun PremiumCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    gradientColors: List<Color> = listOf(PureCheckColors.cardBg, PureCheckColors.cardBg.copy(alpha = 0.8f)),
    content: @Composable ColumnScope.() -> Unit
) {
    val cardModifier = if (onClick != null) {
        modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.verticalGradient(gradientColors))
            .border(1.dp, Color(0xFF26354E), RoundedCornerShape(20.dp))
            .clickable { onClick() }
    } else {
        modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.verticalGradient(gradientColors))
            .border(1.dp, Color(0xFF26354E), RoundedCornerShape(20.dp))
    }
    Column(
        modifier = cardModifier.padding(18.dp),
        content = content
    )
}

@Composable
fun PureCheckSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(PureCheckColors.accentGreen)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                color = PureCheckColors.textPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
        if (actionText != null && onActionClick != null) {
            Text(
                text = actionText,
                color = PureCheckColors.accentGreen,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                modifier = Modifier.clickable { onActionClick() }
            )
        }
    }
}

@Composable
fun ScoreIndicator(
    score: Int,
    modifier: Modifier = Modifier,
    size: Int = 100
) {
    val scoreColor = when {
        score >= 80 -> PureCheckColors.accentGreen
        score >= 60 -> PureCheckColors.warningYellow
        score >= 45 -> PureCheckColors.warningOrange
        else -> PureCheckColors.errorRed
    }

    Box(
        modifier = modifier.size(size.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size.dp)) {
            drawCircle(
                color = Color(0xFF1E2B44),
                style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = scoreColor,
                startAngle = -90f,
                sweepAngle = (score / 100f) * 360f,
                useCenter = false,
                style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$score",
                color = PureCheckColors.textPrimary,
                fontWeight = FontWeight.Black,
                fontSize = (size * 0.28).sp
            )
            Text(
                text = "Score",
                color = PureCheckColors.textSecondary,
                fontWeight = FontWeight.Bold,
                fontSize = (size * 0.11).sp
            )
        }
    }
}

@Composable
fun GradeBadge(
    grade: String,
    modifier: Modifier = Modifier,
    size: Int = 48
) {
    val gradeColor = when (grade.uppercase()) {
        "A" -> PureCheckColors.accentGreen
        "B" -> PureCheckColors.accentGreen
        "C" -> PureCheckColors.warningYellow
        "D" -> PureCheckColors.warningOrange
        else -> PureCheckColors.errorRed
    }

    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(gradeColor.copy(alpha = 0.15f))
            .border(2.dp, gradeColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = grade,
            color = gradeColor,
            fontWeight = FontWeight.Black,
            fontSize = (size * 0.42).sp
        )
    }
}

@Composable
fun RiskChip(
    label: String,
    modifier: Modifier = Modifier,
    severity: String = "medium"
) {
    val chipColor = when (severity.lowercase()) {
        "high" -> PureCheckColors.errorRed
        "medium" -> PureCheckColors.warningOrange
        else -> PureCheckColors.warningYellow
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(chipColor.copy(alpha = 0.15f))
            .border(1.dp, chipColor.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Risk",
                tint = chipColor,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                color = chipColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun IngredientChip(
    name: String,
    rating: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val ratingColor = when (rating.lowercase()) {
        "safe" -> PureCheckColors.accentGreen
        "caution" -> PureCheckColors.warningYellow
        else -> PureCheckColors.errorRed
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(PureCheckColors.cardBg)
            .border(1.dp, Color(0xFF1E2B44), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(ratingColor)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = name,
                color = PureCheckColors.textPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun HealthMetric(
    label: String,
    value: String,
    rating: String = "Moderate",
    modifier: Modifier = Modifier
) {
    val ratingColor = when (rating.lowercase()) {
        "low" -> PureCheckColors.accentBlue
        "moderate", "good" -> PureCheckColors.accentGreen
        else -> PureCheckColors.errorRed
    }

    PureCheckCard(
        modifier = modifier.width(100.dp),
        backgroundColor = PureCheckColors.cardBg
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = label,
                color = PureCheckColors.textSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = PureCheckColors.textPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(ratingColor.copy(alpha = 0.15f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = rating.uppercase(),
                    color = ratingColor,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

@Composable
fun ScanHistoryItem(
    name: String,
    score: Int,
    grade: String,
    dateString: String,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(PureCheckColors.cardBg)
            .border(1.dp, Color(0xFF1E2B44), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FoodProductImage(name = name, modifier = Modifier.size(54.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                color = PureCheckColors.textPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Score: $score • $dateString",
                color = PureCheckColors.textSecondary,
                fontSize = 11.sp
            )
        }
        GradeBadge(grade = grade, size = 36)
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = PureCheckColors.textMuted,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun EmptyState(
    title: String,
    description: String,
    icon: ImageVector = Icons.Default.Info,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(PureCheckColors.cardBgElevated),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PureCheckColors.accentGreen,
                modifier = Modifier.size(44.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = title,
            color = PureCheckColors.textPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            color = PureCheckColors.textSecondary,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
        if (actionLabel != null && onActionClick != null) {
            Spacer(modifier = Modifier.height(20.dp))
            PrimaryActionButton(
                text = actionLabel,
                onClick = onActionClick
            )
        }
    }
}

@Composable
fun LoadingState(
    message: String = "Loading...",
    steps: List<Pair<String, Boolean>> = emptyList(),
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = PureCheckColors.accentGreen,
            strokeWidth = 4.dp,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = message,
            color = PureCheckColors.textPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        if (steps.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.widthIn(max = 240.dp)
            ) {
                steps.forEach { (step, isDone) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Default.Info,
                            contentDescription = null,
                            tint = if (isDone) PureCheckColors.accentGreen else PureCheckColors.textMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = step,
                            color = if (isDone) PureCheckColors.textPrimary else PureCheckColors.textSecondary,
                            fontSize = 13.sp,
                            fontWeight = if (isDone) FontWeight.Medium else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorState(
    title: String = "Something went wrong",
    description: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = PureCheckColors.errorRed,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            color = PureCheckColors.textPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            color = PureCheckColors.textSecondary,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        PrimaryActionButton(
            text = "Retry",
            onClick = onRetry
        )
    }
}

@Composable
fun PrimaryActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = PureCheckColors.accentGreen,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.height(48.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(imageVector = icon, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun SecondaryActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = PureCheckColors.textPrimary
        ),
        border = BorderStroke(1.dp, Color(0xFF1E2B44)),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.height(48.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(imageVector = icon, contentDescription = null, tint = PureCheckColors.textPrimary)
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ProfileToggleRow(
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

@Composable
fun ObfuscatedCard(
    printed: String,
    actual: String,
    reason: String,
    modifier: Modifier = Modifier
) {
    PureCheckCard(modifier = modifier.fillMaxWidth(), backgroundColor = Color(0xFF3F1B1B)) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = PureCheckColors.errorRed, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Flagged Label: $printed", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text("Actual Ingredient: $actual", color = Color(0xFFFCA5A5), fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(reason, color = Color.LightGray, fontSize = 11.sp, lineHeight = 15.sp)
        }
    }
}

@Composable
fun ScoreBar(
    label: String,
    value: Int,
    modifier: Modifier = Modifier
) {
    val color = when {
        value >= 80 -> PureCheckColors.accentGreen
        value >= 60 -> PureCheckColors.warningYellow
        else -> PureCheckColors.errorRed
    }
    Column(modifier = modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = Color.White, fontSize = 11.sp)
            Text("$value/100", color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { value / 100f },
            color = color,
            trackColor = Color(0xFF1E2B44),
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
        )
    }
}

@Composable
fun SwapCard(
    brand: String,
    product: String,
    reason: String,
    accentGreen: Color,
    modifier: Modifier = Modifier
) {
    PureCheckCard(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = accentGreen,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("$brand - $product", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text(reason, color = Color.Gray, fontSize = 11.sp, lineHeight = 15.sp)
            }
        }
    }
}

@Composable
fun PremiumSegmentedControl(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(PureCheckColors.cardBg)
            .border(1.dp, Color(0xFF1E2B44), RoundedCornerShape(12.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        options.forEach { option ->
            val isSelected = selectedOption == option
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) PureCheckColors.accentGreen else Color.Transparent)
                    .clickable { onOptionSelected(option) }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    color = if (isSelected) Color.White else PureCheckColors.textSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun PremiumFoodScoreBadge(
    grade: String,
    score: Int,
    modifier: Modifier = Modifier
) {
    val color = when (grade.uppercase()) {
        "A" -> PureCheckColors.accentGreen
        "B" -> PureCheckColors.accentGreen
        "C" -> PureCheckColors.warningYellow
        "D" -> PureCheckColors.warningOrange
        else -> PureCheckColors.errorRed
    }
    val textLabel = when (grade.uppercase()) {
        "A" -> "EXCELLENT"
        "B" -> "GOOD"
        "C" -> "MODERATE"
        "D" -> "POOR"
        else -> "AVOID"
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(color.copy(alpha = 0.1f))
            .border(1.5.dp, color, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = grade,
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 28.sp
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = textLabel,
                color = color,
                fontWeight = FontWeight.Black,
                fontSize = 14.sp,
                letterSpacing = 0.5.sp
            )
            Text(
                text = "Safety Score: $score / 100",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            Text(
                text = "Fully compliance checked safety grade",
                color = PureCheckColors.textSecondary,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun FoodProductImage(
    name: String,
    modifier: Modifier = Modifier
) {
    val gradient = remember(name) {
        val colors = when {
            name.contains("bread", ignoreCase = true) -> listOf(Color(0xFFF59E0B), Color(0xFFD97706))
            name.contains("milk", ignoreCase = true) -> listOf(Color(0xFF3B82F6), Color(0xFF1D4ED8))
            name.contains("yogurt", ignoreCase = true) -> listOf(Color(0xFFEC4899), Color(0xFFBE185D))
            name.contains("cola", ignoreCase = true) -> listOf(Color(0xFFEF4444), Color(0xFFB91C1C))
            name.contains("salad", ignoreCase = true) -> listOf(Color(0xFF10B981), Color(0xFF047857))
            else -> listOf(Color(0xFF8B5CF6), Color(0xFF6D28D9))
        }
        Brush.linearGradient(colors)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(gradient),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = when {
                name.contains("bread", ignoreCase = true) -> Icons.Default.Star
                name.contains("milk", ignoreCase = true) -> Icons.Default.ShoppingCart
                else -> Icons.Default.ShoppingCart
            },
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.8f),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun NutriCheckLogo(
    modifier: Modifier = Modifier,
    logoSize: androidx.compose.ui.unit.Dp = 64.dp
) {
    Canvas(modifier = modifier.size(logoSize)) {
        val w = this.size.width
        val h = this.size.height
        
        val emerald = Color(0xFF10B981)
        val mint = Color(0xFF34D399)
        
        val bracketSize = w * 0.18f
        val strokeW = 2.dp.toPx()
        
        // Draw brackets
        drawLine(Color(0xFF475569), androidx.compose.ui.geometry.Offset(0f, bracketSize), androidx.compose.ui.geometry.Offset(0f, 0f), strokeWidth = strokeW)
        drawLine(Color(0xFF475569), androidx.compose.ui.geometry.Offset(0f, 0f), androidx.compose.ui.geometry.Offset(bracketSize, 0f), strokeWidth = strokeW)
        
        drawLine(Color(0xFF475569), androidx.compose.ui.geometry.Offset(w - bracketSize, 0f), androidx.compose.ui.geometry.Offset(w, 0f), strokeWidth = strokeW)
        drawLine(Color(0xFF475569), androidx.compose.ui.geometry.Offset(w, 0f), androidx.compose.ui.geometry.Offset(w, bracketSize), strokeWidth = strokeW)
        
        drawLine(Color(0xFF475569), androidx.compose.ui.geometry.Offset(0f, h - bracketSize), androidx.compose.ui.geometry.Offset(0f, h), strokeWidth = strokeW)
        drawLine(Color(0xFF475569), androidx.compose.ui.geometry.Offset(0f, h), androidx.compose.ui.geometry.Offset(bracketSize, h), strokeWidth = strokeW)
        
        drawLine(Color(0xFF475569), androidx.compose.ui.geometry.Offset(w - bracketSize, h), androidx.compose.ui.geometry.Offset(w, h), strokeWidth = strokeW)
        drawLine(Color(0xFF475569), androidx.compose.ui.geometry.Offset(w, h), androidx.compose.ui.geometry.Offset(w, h - bracketSize), strokeWidth = strokeW)
        
        // Draw "N"
        val paddingX = w * 0.25f
        val paddingY = h * 0.22f
        val nWidth = w - (paddingX * 2)
        val nHeight = h - (paddingY * 2)
        
        drawLine(
            brush = Brush.verticalGradient(listOf(emerald, mint)),
            start = androidx.compose.ui.geometry.Offset(paddingX, paddingY + nHeight),
            end = androidx.compose.ui.geometry.Offset(paddingX, paddingY),
            strokeWidth = 5.dp.toPx(),
            cap = StrokeCap.Round
        )
        
        drawLine(
            brush = Brush.verticalGradient(listOf(mint, emerald)),
            start = androidx.compose.ui.geometry.Offset(paddingX + nWidth, paddingY + nHeight),
            end = androidx.compose.ui.geometry.Offset(paddingX + nWidth, paddingY),
            strokeWidth = 5.dp.toPx(),
            cap = StrokeCap.Round
        )
        
        drawLine(
            brush = Brush.linearGradient(listOf(emerald, mint)),
            start = androidx.compose.ui.geometry.Offset(paddingX, paddingY),
            end = androidx.compose.ui.geometry.Offset(paddingX + nWidth, paddingY + nHeight),
            strokeWidth = 5.dp.toPx(),
            cap = StrokeCap.Round
        )
        
        // Draw leaf
        val leafPath = androidx.compose.ui.graphics.Path().apply {
            val leafStartX = paddingX + (nWidth * 0.5f)
            val leafStartY = paddingY + (nHeight * 0.2f)
            moveTo(leafStartX, leafStartY)
            quadraticTo(
                leafStartX + (nWidth * 0.35f), leafStartY - (nHeight * 0.1f),
                leafStartX + (nWidth * 0.45f), leafStartY - (nHeight * 0.3f)
            )
            quadraticTo(
                leafStartX + (nWidth * 0.1f), leafStartY - (nHeight * 0.3f),
                leafStartX, leafStartY
            )
            close()
        }
        drawPath(
            path = leafPath,
            color = mint
        )
    }
}

@Composable
fun NutriCheckWordmark(
    modifier: Modifier = Modifier,
    showTagline: Boolean = true
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NutriCheckLogo(logoSize = 36.dp)
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = "NutriCheck",
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                letterSpacing = (-0.5).sp
            )
            if (showTagline) {
                Text(
                    text = "Know what you eat.",
                    color = PureCheckColors.textSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
