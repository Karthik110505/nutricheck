package com.example.purecheck.ui.result

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONArray
import org.json.JSONObject
import androidx.core.content.ContextCompat
import com.example.purecheck.data.*
import com.example.purecheck.ui.components.*
import com.example.purecheck.utils.ChatMessage
import com.example.purecheck.utils.GeminiClient
import androidx.navigation3.runtime.NavKey
import java.util.UUID

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    jsonResult: String,
    onBack: () -> Unit,
    onNavigate: (NavKey) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs = remember { PureCheckPreferences(context) }
    val userProfile = remember { prefs.getUserProfile() }
    val history = remember { prefs.getHistory() }

    val parsed = remember(jsonResult) {
        try {
            JSONObject(jsonResult)
        } catch (e: Exception) {
            null
        }
    }

    var isChatFullScreen by remember { mutableStateOf(false) }
    var chatInput by remember { mutableStateOf("") }
    var chatMessages by remember { mutableStateOf(listOf<ChatMessage>()) }
    var isSending by remember { mutableStateOf(false) }
    var chatError by remember { mutableStateOf<String?>(null) }
    var explanationMode by remember { mutableStateOf(ChatExplanationMode.Simple) }

    val promptChips = listOf(
        "Is this safe for daily consumption?",
        "Does this contain harmful preservatives?",
        "Why is this score assigned?",
        "Check sugar and sodium impact"
    )

    val sendCurrentMessage: (String) -> Unit = { textToSend ->
        val input = textToSend.trim()
        if (input.isNotBlank() && !isSending) {
            val promptWithMode = "[$explanationMode Mode] $input"
            val newHistory = chatMessages + ChatMessage(isUser = true, text = promptWithMode)
            chatMessages = newHistory
            chatInput = ""
            isSending = true
            chatError = null

            val provider = prefs.getProvider()
            val apiKey = when (provider.lowercase()) {
                "groq" -> prefs.getGroqApiKey()
                "deepseek" -> prefs.getDeepSeekApiKey()
                "bytez" -> prefs.getBytezApiKey()
                else -> prefs.getApiKey()
            }

            val contextJson = try { JSONObject(jsonResult).toString() } catch (e: Exception) { jsonResult }
            val appContext = context.applicationContext

            GeminiClient.discussResult(
                scannedResultJson = contextJson,
                chatHistory = newHistory,
                provider = provider,
                apiKey = apiKey,
                callback = { res ->
                    ContextCompat.getMainExecutor(appContext).execute {
                        isSending = false
                        res.fold(
                            onSuccess = { reply ->
                                chatMessages = chatMessages + ChatMessage(isUser = false, text = reply)
                            },
                            onFailure = { err ->
                                chatError = err.message ?: "Failed to get reply."
                            }
                        )
                    }
                }
            )
        }
    }

    val scrollState = rememberScrollState()
    var selectedIngredientName by remember { mutableStateOf("") }
    var selectedIngredientDetail by remember { mutableStateOf<IngredientDef?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }

    // Serving / Quantity states
    var quantityMultiplier by remember { mutableStateOf(1.0f) }

    var actionSuccessMessage by remember { mutableStateOf<String?>(null) }
    
    // Favorites status
    var isSavedToFav by remember { mutableStateOf(false) }

    if (parsed == null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Audit Report", color = Color.White, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = PureCheckColors.darkBg)
                )
            },
            containerColor = PureCheckColors.darkBg
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Error reading scan results.", color = PureCheckColors.errorRed)
            }
        }
    } else if (isChatFullScreen) {
        val productName = parsed.optString("productName", "Scanned Product")
        FullScreenChatView(
            productName = productName,
            chatMessages = chatMessages,
            chatInput = chatInput,
            onChatInputChange = { chatInput = it },
            explanationMode = explanationMode,
            onExplanationModeChange = { explanationMode = it },
            promptChips = promptChips,
            isSending = isSending,
            chatError = chatError,
            onSendMessage = { sendCurrentMessage(it) },
            onClose = { isChatFullScreen = false }
        )
    } else {
        val productName = parsed.optString("productName", "Scanned Product")
        val grade = parsed.optString("overallGrade", "B")
        val score = parsed.optInt("overallScore", 80)
        val summary = parsed.optString("summary", "")
        
        val obfuscatedArray = parsed.optJSONArray("obfuscatedIngredients") ?: JSONArray()
        val analysisArray = parsed.optJSONArray("ingredientAnalysis") ?: JSONArray()
        val swapsArray = parsed.optJSONArray("cleanSwaps") ?: JSONArray()

        val breakdown = remember(parsed) {
            val b = parsed.optJSONObject("scoreBreakdown")
            if (b != null) {
                ScoreBreakdown(
                    ingredientQuality = b.optInt("ingredientQuality", 80),
                    nutrition = b.optInt("nutrition", 80),
                    additives = b.optInt("additives", 80),
                    sugar = b.optInt("sugar", 80),
                    sodium = b.optInt("sodium", 80)
                )
            } else {
                ScoreBreakdown(score, score, score, score, score)
            }
        }

        // Parse base nutrition facts
        val nutritionObject = parsed.optJSONObject("nutritionAnalysis")
        val baseCalories = nutritionObject?.optDouble("calories", 0.0)?.toFloat() ?: 0f
        val baseProtein = nutritionObject?.optDouble("protein", 0.0)?.toFloat() ?: 0f
        val baseCarbs = nutritionObject?.optDouble("carbs", 0.0)?.toFloat() ?: 0f
        val baseFat = nutritionObject?.optDouble("fat", 0.0)?.toFloat() ?: 0f
        val baseFiber = nutritionObject?.optDouble("fiber", 0.0)?.toFloat() ?: 0f
        val baseSugar = nutritionObject?.optDouble("sugar", 0.0)?.toFloat() ?: 0f
        val baseSodium = nutritionObject?.optDouble("sodium", 0.0)?.toFloat() ?: 0f
        val baseSaturatedFat = nutritionObject?.optDouble("saturatedFat", 0.0)?.toFloat() ?: 0f

        // Scaled values
        val calories = baseCalories * quantityMultiplier
        val protein = baseProtein * quantityMultiplier
        val carbs = baseCarbs * quantityMultiplier
        val fat = baseFat * quantityMultiplier
        val fiber = baseFiber * quantityMultiplier
        val sugar = baseSugar * quantityMultiplier
        val sodium = baseSodium * quantityMultiplier
        val saturatedFat = baseSaturatedFat * quantityMultiplier

        // Initialize Favorite state
        LaunchedEffect(productName) {
            isSavedToFav = prefs.isFavoriteByName(productName)
        }

        // Compute personalized profile score dynamically
        val profileResult = remember(analysisArray, userProfile, calories, protein, carbs, fat, fiber, sugar, sodium) {
            val names = mutableListOf<String>()
            for (i in 0 until analysisArray.length()) {
                names.add(analysisArray.getJSONObject(i).optString("name", ""))
            }
            val rawText = names.joinToString(", ")
            
            RulesEngine.calculate(
                rawIngredientsText = rawText,
                calories = calories,
                sugarGrams = sugar,
                proteinGrams = protein,
                fiberGrams = fiber,
                sodiumMg = sodium,
                saturatedFatGrams = saturatedFat,
                watchlist = prefs.getWatchlist(),
                profile = userProfile
            )
        }

        // Dynamic Verdict highlights
        val personalizedVerdict = remember(profileResult) {
            val hasRed = profileResult.conflicts.any { it.startsWith("✕") }
            val hasYellow = profileResult.conflicts.any { it.startsWith("⚠") }
            when {
                hasRed -> Pair("✕ DIETARY CONFLICT", PureCheckColors.errorRed)
                hasYellow -> Pair("⚠ CAUTION", PureCheckColors.warningYellow)
                else -> Pair("✓ SUITABLE", PureCheckColors.accentGreen)
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Audit Report", color = Color.White, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = PureCheckColors.darkBg)
                )
            },
            containerColor = PureCheckColors.darkBg
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
                // 1. PRODUCT HEADER
                // ==========================================
                PremiumCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FoodProductImage(name = productName, modifier = Modifier.size(72.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = productName,
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                maxLines = 2
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Scan Verified Analysis",
                                color = PureCheckColors.textSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    PremiumFoodScoreBadge(grade = grade, score = score)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // ==========================================
                // 2. SERVING SIZE SCALER
                // ==========================================
                PureCheckCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Adjust Serving / Portion", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Values will scale automatically", color = Color.Gray, fontSize = 10.sp)
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(PureCheckColors.cardBgElevated)
                        ) {
                            IconButton(
                                onClick = { if (quantityMultiplier > 0.25f) quantityMultiplier -= 0.25f },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Text("-", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Black)
                            }
                            Text(
                                text = String.format("%.2fx", quantityMultiplier),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            IconButton(
                                onClick = { quantityMultiplier += 0.25f },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Text("+", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // ==========================================
                // 3. QUICK NUTRITION
                // ==========================================
                PremiumCard(modifier = Modifier.fillMaxWidth()) {
                    Text("Nutrition Details (Scaled Portion)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MacroBoxCompact("Calories", "${calories.toInt()} kcal", PureCheckColors.accentGreen, Modifier.weight(1f))
                        MacroBoxCompact("Protein", "${String.format("%.1f", protein)}g", PureCheckColors.accentBlue, Modifier.weight(1f))
                        MacroBoxCompact("Carbs", "${String.format("%.1f", carbs)}g", Color(0xFFF59E0B), Modifier.weight(1f))
                        MacroBoxCompact("Fat", "${String.format("%.1f", fat)}g", PureCheckColors.errorRed, Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MacroBoxCompact("Fiber", "${String.format("%.1f", fiber)}g", Color(0xFF8B5CF6), Modifier.weight(1f))
                        MacroBoxCompact("Sugar", "${String.format("%.1f", sugar)}g", Color(0xFFE2E8F0), Modifier.weight(1f))
                        MacroBoxCompact("Sodium", "${sodium.toInt()}mg", Color(0xFFFF8B8B), Modifier.weight(1f))
                        MacroBoxCompact("Portion", "${(quantityMultiplier * 100).toInt()}g/ml", Color(0xFF64748B), Modifier.weight(1f))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // ==========================================
                // 4. YOUR PERSONALIZED RESULT
                // ==========================================
                PureCheckCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = personalizedVerdict.second.copy(alpha = 0.12f)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (personalizedVerdict.first.startsWith("✓")) Icons.Default.CheckCircle else Icons.Default.Warning,
                            contentDescription = null,
                            tint = personalizedVerdict.second,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = personalizedVerdict.first,
                                color = personalizedVerdict.second,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                            val conflictText = if (profileResult.conflicts.isNotEmpty()) {
                                profileResult.conflicts.first()
                            } else {
                                "Matches all your configured food and dietary preferences."
                            }
                            Text(
                                text = conflictText,
                                color = Color.White,
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ==========================================
                // 5. DIETARY CONFLICTS & WARNING DETAILS
                // ==========================================
                if (profileResult.conflicts.size > 1) {
                    PureCheckSectionHeader(title = "Dietary Preferences Alerts")
                    Spacer(modifier = Modifier.height(8.dp))
                    PureCheckCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, PureCheckColors.errorRed.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
                        backgroundColor = Color(0xFF3F1B1B)
                    ) {
                        profileResult.conflicts.drop(1).forEach { conflict ->
                            Row(
                                modifier = Modifier.padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Alert",
                                    tint = PureCheckColors.errorRed,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(conflict, color = Color(0xFFFCA5A5), fontSize = 12.sp, lineHeight = 16.sp)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // ==========================================
                // 7. OBFUSCATED INGREDIENTS
                // ==========================================
                if (obfuscatedArray.length() > 0) {
                    PureCheckSectionHeader(title = "Label Cover Ups")
                    Spacer(modifier = Modifier.height(8.dp))
                    for (i in 0 until obfuscatedArray.length()) {
                        val obf = obfuscatedArray.getJSONObject(i)
                        ObfuscatedCard(
                            printed = obf.optString("printed", ""),
                            actual = obf.optString("actual", ""),
                            reason = obf.optString("reason", "")
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // ==========================================
                // 8. INGREDIENT SAFETY BREAKDOWN
                // ==========================================
                PureCheckSectionHeader(title = "Safety Breakdown Index")
                Spacer(modifier = Modifier.height(8.dp))
                PureCheckCard(modifier = Modifier.fillMaxWidth()) {
                    ScoreBar(label = "Ingredient Quality Index", value = breakdown.ingredientQuality)
                    ScoreBar(label = "Nutrition Suitability Index", value = breakdown.nutrition)
                    ScoreBar(label = "Additive Risk Index", value = breakdown.additives)
                    ScoreBar(label = "Glycemic/Sugar Index", value = breakdown.sugar)
                    ScoreBar(label = "Sodium Load Index", value = breakdown.sodium)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ==========================================
                // 9. CLEAN ALTERNATIVES
                // ==========================================
                if (swapsArray.length() > 0) {
                    PureCheckSectionHeader(title = "Clean Alternatives")
                    Spacer(modifier = Modifier.height(8.dp))
                    for (i in 0 until swapsArray.length()) {
                        val swap = swapsArray.getJSONObject(i)
                        SwapCard(
                            brand = swap.optString("brand", ""),
                            product = swap.optString("product", ""),
                            reason = swap.optString("reason", ""),
                            accentGreen = PureCheckColors.accentGreen
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // ==========================================
                // 10. INGREDIENT LIST AUDIT
                // ==========================================
                PureCheckSectionHeader(title = "Ingredient List Audit")
                Spacer(modifier = Modifier.height(8.dp))
                PureCheckCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Click on highlighted additives to view health safety details.",
                        color = PureCheckColors.textSecondary,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (i in 0 until analysisArray.length()) {
                            val ingObj = analysisArray.getJSONObject(i)
                            val name = ingObj.optString("name", "")
                            val rating = ingObj.optString("rating", "safe")

                            val chipBg = when (rating) {
                                "unsafe" -> PureCheckColors.errorRed.copy(alpha = 0.15f)
                                "caution" -> PureCheckColors.warningOrange.copy(alpha = 0.15f)
                                else -> PureCheckColors.accentGreen.copy(alpha = 0.1f)
                            }
                            val chipBorder = when (rating) {
                                "unsafe" -> PureCheckColors.errorRed
                                "caution" -> PureCheckColors.warningOrange
                                else -> PureCheckColors.accentGreen.copy(alpha = 0.5f)
                            }
                            val chipTextColor = when (rating) {
                                "unsafe" -> PureCheckColors.errorRed
                                "caution" -> PureCheckColors.warningOrange
                                else -> Color.White
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(chipBg)
                                    .border(1.dp, chipBorder, RoundedCornerShape(8.dp))
                                    .clickable {
                                        selectedIngredientName = name
                                        selectedIngredientDetail = IngredientDatabase.find(name)
                                        showBottomSheet = true
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = name,
                                    color = chipTextColor,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ==========================================
                // 11. RECOMMENDATION SUMMARY
                // ==========================================
                PureCheckSectionHeader(title = "Health Recommendation")
                Spacer(modifier = Modifier.height(8.dp))
                PureCheckCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = summary.ifBlank { "Based on our analysis, this product is generally safe to consume." },
                        color = Color.White,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ==========================================
                // 11. ACTIONS SECTION
                // ==========================================
                PureCheckSectionHeader(title = "Actions")
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            prefs.addShoppingItem(
                                ShoppingItem(
                                    id = UUID.randomUUID().toString(),
                                    name = productName,
                                    score = score,
                                    grade = grade
                                )
                            )
                            actionSuccessMessage = "Added $productName to Shopping List!"
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PureCheckColors.cardBgElevated),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .border(1.dp, Color(0xFF334155), RoundedCornerShape(12.dp))
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = PureCheckColors.accentGreen, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Add to List", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (isSavedToFav) {
                                val match = prefs.getFavorites().find { it.productName.equals(productName, ignoreCase = true) }
                                if (match != null) {
                                    prefs.removeFavorite(match.id)
                                }
                                isSavedToFav = false
                                actionSuccessMessage = "Removed from Favorites!"
                            } else {
                                prefs.addFavorite(
                                    FavoriteProduct(
                                        productName = productName,
                                        overallGrade = grade,
                                        overallScore = score,
                                        calories = calories.toInt(),
                                        protein = protein.toInt(),
                                        jsonResult = jsonResult
                                    )
                                )
                                isSavedToFav = true
                                actionSuccessMessage = "Saved to Favorites!"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PureCheckColors.cardBg),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .border(1.dp, Color(0xFF334155), RoundedCornerShape(12.dp))
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (isSavedToFav) PureCheckColors.warningYellow else Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isSavedToFav) "Remove Fav" else "Favorite",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                if (actionSuccessMessage != null) {
                    Text(
                        text = actionSuccessMessage!!,
                        color = PureCheckColors.accentGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ==========================================
                // 12. CONTEXT-AWARE INTELLIGENCE INSPECTOR CHAT
                // ==========================================
                PureCheckSectionHeader(
                    title = "Intelligence Inspector",
                    actionText = "Full Screen ↗",
                    onActionClick = { isChatFullScreen = true }
                )
                Spacer(modifier = Modifier.height(8.dp))
                PureCheckCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Discuss ingredient safety, chemical additives, and health risks with the inspector.",
                            color = PureCheckColors.textSecondary,
                            fontSize = 11.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(PureCheckColors.accentGreen.copy(alpha = 0.15f))
                                .border(1.dp, PureCheckColors.accentGreen.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                .clickable { isChatFullScreen = true }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("Expand ↗", color = PureCheckColors.accentGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        for (mode in ChatExplanationMode.values()) {
                            val isSel = explanationMode == mode
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSel) PureCheckColors.accentGreen else PureCheckColors.cardBgElevated)
                                    .clickable { explanationMode = mode }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = mode.name,
                                    color = if (isSel) Color.White else Color.Gray,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // 1. Hide prompt recommendations after chatting starts
                    if (chatMessages.isEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            promptChips.forEach { chipText ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFF334155).copy(alpha = 0.5f))
                                        .clickable {
                                            chatInput = chipText
                                            sendCurrentMessage(chipText)
                                        }
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                ) {
                                    Text(chipText, color = Color.White, fontSize = 10.sp)
                                }
                            }
                        }
                    }

                    if (chatMessages.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 260.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            chatMessages.forEach { msg ->
                                PureCheckChatBubble(msg = msg)
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                "Open Full Screen Chat ↗",
                                color = PureCheckColors.accentGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable { isChatFullScreen = true }
                            )
                        }
                    }

                    if (isSending) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            CircularProgressIndicator(
                                color = PureCheckColors.accentGreen,
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Inspector is typing...",
                                color = PureCheckColors.textMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    chatError?.let { err ->
                        Text(err, color = PureCheckColors.errorRed, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = chatInput,
                            onValueChange = { chatInput = it },
                            placeholder = { Text("Ask Inspector...", color = PureCheckColors.textMuted, fontSize = 12.sp) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = PureCheckColors.accentGreen,
                                unfocusedBorderColor = Color.Gray
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(
                            onClick = { sendCurrentMessage(chatInput) },
                            enabled = chatInput.isNotBlank() && !isSending,
                            colors = IconButtonDefaults.iconButtonColors(contentColor = PureCheckColors.accentGreen)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send",
                                tint = if (chatInput.isNotBlank()) PureCheckColors.accentGreen else Color.Gray
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // Ingredient Explorer Bottom Sheet (Keep existing functionality)
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                containerColor = PureCheckColors.cardBg,
                contentColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    val detail = selectedIngredientDetail
                    if (detail != null) {
                        Text(
                            text = detail.name,
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        )
                        if (detail.eNumber != null) {
                            Text(
                                text = "Also known as ${detail.eNumber} • ${detail.aliases.joinToString(", ")}",
                                color = PureCheckColors.accentGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text("Category", color = PureCheckColors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(detail.category, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text("Why is it used?", color = PureCheckColors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(detail.function, color = Color.White, fontSize = 13.sp, lineHeight = 18.sp)
                        
                        Spacer(modifier = Modifier.height(12.dp))

                        Text("What does the evidence say?", color = PureCheckColors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(detail.description, color = Color.White, fontSize = 13.sp, lineHeight = 18.sp)
                        
                        if (detail.potentialConcerns.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Potential Concerns (Evidence level: ${detail.evidenceLevel})", color = PureCheckColors.errorRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            detail.potentialConcerns.forEach { concern ->
                                Text("• $concern", color = Color.LightGray, fontSize = 13.sp, lineHeight = 18.sp)
                            }
                        }
                    } else {
                        Text(
                            text = selectedIngredientName,
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No detailed listing found in offline database. This ingredient is generally classified as a natural food component or a common nutrient.",
                            color = PureCheckColors.textSecondary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

enum class ChatExplanationMode {
    Simple,
    Detailed,
    Scientific
}

@Composable
fun MacroBoxCompact(label: String, value: String, accentColor: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(PureCheckColors.cardBgElevated)
            .border(1.dp, Color(0xFF26354E), RoundedCornerShape(10.dp))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(accentColor)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = label, color = PureCheckColors.textSecondary, fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
            Text(text = value, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PureCheckChatBubble(
    msg: ChatMessage,
    modifier: Modifier = Modifier
) {
    val isUser = msg.isUser
    val bubbleBg = if (isUser) PureCheckColors.accentGreen.copy(alpha = 0.18f) else PureCheckColors.cardBgElevated
    val align = if (isUser) Alignment.End else Alignment.Start

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = align
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 3.dp, start = if (!isUser) 4.dp else 0.dp, end = if (isUser) 4.dp else 0.dp)
        ) {
            if (!isUser) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(PureCheckColors.accentGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Text("✓", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    "Inspector",
                    color = PureCheckColors.accentGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    "You",
                    color = PureCheckColors.textMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Box(
            modifier = Modifier
                .then(if (isUser) Modifier.widthIn(max = 320.dp) else Modifier.fillMaxWidth())
                .clip(
                    RoundedCornerShape(
                        topStart = 14.dp,
                        topEnd = 14.dp,
                        bottomStart = if (isUser) 14.dp else 2.dp,
                        bottomEnd = if (isUser) 2.dp else 14.dp
                    )
                )
                .background(bubbleBg)
                .border(
                    width = 1.dp,
                    color = if (isUser) PureCheckColors.accentGreen.copy(alpha = 0.4f) else PureCheckColors.cardBorder,
                    shape = RoundedCornerShape(
                        topStart = 14.dp,
                        topEnd = 14.dp,
                        bottomStart = if (isUser) 14.dp else 2.dp,
                        bottomEnd = if (isUser) 2.dp else 14.dp
                    )
                )
                .padding(14.dp)
        ) {
            val formattedText = remember(msg.text) {
                val preprocessed = preprocessMarkdownForMobile(msg.text)
                parseMarkdownToAnnotatedString(preprocessed)
            }
            Text(
                text = formattedText,
                color = Color.White,
                fontSize = 12.sp,
                lineHeight = 19.sp
            )
        }
    }
}

/**
 * Converts Markdown tables and raw artifacts into clean, readable mobile bullet structures.
 */
fun preprocessMarkdownForMobile(rawText: String): String {
    val lines = rawText.split("\n")
    val result = mutableListOf<String>()
    var i = 0

    while (i < lines.size) {
        val line = lines[i].trim()

        // Detect Markdown Table: Starts with '|' and followed by a divider row with '---'
        if (line.startsWith("|") && line.contains("|") && i + 1 < lines.size) {
            val nextLine = lines[i + 1].trim()
            if (nextLine.startsWith("|") && nextLine.contains("---")) {
                val rawHeaders = line.split("|")
                    .map { it.trim().removePrefix("**").removeSuffix("**") }
                    .filter { it.isNotEmpty() }

                i += 2 // skip header and divider row

                while (i < lines.size) {
                    val rowLine = lines[i].trim()
                    if (!rowLine.startsWith("|") || !rowLine.endsWith("|")) {
                        break
                    }
                    val cells = rowLine.split("|")
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }

                    if (cells.isNotEmpty()) {
                        when (cells.size) {
                            1 -> {
                                result.add("• **${cells[0]}**")
                            }
                            2 -> {
                                val label = cells[0].removePrefix("**").removeSuffix("**")
                                result.add("• **$label:** ${cells[1]}")
                            }
                            3 -> {
                                val label = cells[0].removePrefix("**").removeSuffix("**")
                                val val1 = cells[1]
                                val val2 = cells[2]
                                result.add("• **$label:** $val1 ($val2)")
                            }
                            else -> {
                                val title = cells[0].removePrefix("**").removeSuffix("**")
                                result.add("### $title")

                                val amount = cells.getOrNull(1) ?: ""
                                val dri = cells.getOrNull(2) ?: ""
                                val impact = cells.getOrNull(3) ?: ""

                                if (amount.isNotBlank() && dri.isNotBlank()) {
                                    result.add("  • **Amount & DRI:** $amount ($dri)")
                                } else if (amount.isNotBlank()) {
                                    result.add("  • **Amount:** $amount")
                                }

                                if (impact.isNotBlank()) {
                                    result.add("  • **Health Impact:** $impact")
                                }

                                for (c in 4 until cells.size) {
                                    val headerName = rawHeaders.getOrNull(c) ?: "Detail"
                                    result.add("  • **$headerName:** ${cells[c]}")
                                }
                                result.add("") // Spacer between items
                            }
                        }
                    }
                    i++
                }
                continue
            }
        }

        // Filter out standalone markdown dividers like '---', '===', '***'
        if (line == "---" || line == "***" || line == "___" || line.matches(Regex("""^-{3,}$""")) || line.matches(Regex("""^={3,}$"""))) {
            i++
            continue
        }

        // Filter out stray pipe separator lines
        if (line.startsWith("|") && line.contains("---")) {
            i++
            continue
        }

        result.add(lines[i])
        i++
    }

    return result.joinToString("\n")
}

fun parseMarkdownToAnnotatedString(
    text: String,
    primaryColor: Color = Color(0xFFF1F5F9),
    accentColor: Color = PureCheckColors.accentGreen,
    boldColor: Color = Color.White
): AnnotatedString {
    return buildAnnotatedString {
        val lines = text.split("\n")
        lines.forEachIndexed { lineIdx, line ->
            val trimmed = line.trim()

            val isBullet = trimmed.startsWith("- ") || trimmed.startsWith("* ") || trimmed.startsWith("• ") || trimmed.startsWith("+ ")
            val isNumbered = trimmed.matches(Regex("""^\d+[\.)]\s.*"""))
            val isHeader4 = trimmed.startsWith("#### ")
            val isHeader3 = trimmed.startsWith("### ")
            val isHeader2 = trimmed.startsWith("## ")
            val isHeader1 = trimmed.startsWith("# ")
            val isBlockquote = trimmed.startsWith("> ")

            val contentToParse = when {
                isHeader1 -> {
                    withStyle(SpanStyle(color = accentColor, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)) {
                        append("■ ")
                    }
                    trimmed.removePrefix("#").trim()
                }
                isHeader2 -> {
                    withStyle(SpanStyle(color = accentColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)) {
                        append("■ ")
                    }
                    trimmed.removePrefix("##").trim()
                }
                isHeader3 -> {
                    withStyle(SpanStyle(color = accentColor, fontWeight = FontWeight.Bold, fontSize = 13.sp)) {
                        append("■ ")
                    }
                    trimmed.removePrefix("###").trim()
                }
                isHeader4 -> {
                    withStyle(SpanStyle(color = accentColor, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)) {
                        append("■ ")
                    }
                    trimmed.removePrefix("####").trim()
                }
                isBlockquote -> {
                    withStyle(SpanStyle(color = accentColor, fontWeight = FontWeight.Bold)) {
                        append("  ▍ ")
                    }
                    trimmed.removePrefix(">").trim()
                }
                isBullet -> {
                    withStyle(SpanStyle(color = accentColor, fontWeight = FontWeight.Bold)) {
                        append(" • ")
                    }
                    when {
                        trimmed.startsWith("- ") -> trimmed.substring(2).trim()
                        trimmed.startsWith("* ") -> trimmed.substring(2).trim()
                        trimmed.startsWith("+ ") -> trimmed.substring(2).trim()
                        else -> trimmed.removePrefix("•").trim()
                    }
                }
                isNumbered -> {
                    val prefix = trimmed.takeWhile { it.isDigit() || it == '.' || it == ')' }
                    withStyle(SpanStyle(color = accentColor, fontWeight = FontWeight.Bold)) {
                        append(" $prefix ")
                    }
                    trimmed.removePrefix(prefix).trim()
                }
                else -> line
            }

            var i = 0
            while (i < contentToParse.length) {
                // Check for **bold**
                if (i + 1 < contentToParse.length && contentToParse.startsWith("**", i)) {
                    val endIdx = contentToParse.indexOf("**", i + 2)
                    if (endIdx != -1) {
                        val boldText = contentToParse.substring(i + 2, endIdx)
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = boldColor)) {
                            append(boldText)
                        }
                        i = endIdx + 2
                        continue
                    }
                }

                // Check for *italic*
                if (contentToParse[i] == '*' && (i + 1 >= contentToParse.length || contentToParse[i + 1] != '*')) {
                    val endIdx = contentToParse.indexOf('*', i + 1)
                    if (endIdx != -1 && endIdx > i + 1 && (endIdx + 1 >= contentToParse.length || contentToParse[endIdx + 1] != '*')) {
                        val italicText = contentToParse.substring(i + 1, endIdx)
                        withStyle(SpanStyle(fontStyle = FontStyle.Italic, color = primaryColor.copy(alpha = 0.9f))) {
                            append(italicText)
                        }
                        i = endIdx + 1
                        continue
                    }
                }

                // Check for `inline code`
                if (contentToParse[i] == '`') {
                    val endIdx = contentToParse.indexOf('`', i + 1)
                    if (endIdx != -1) {
                        val codeText = contentToParse.substring(i + 1, endIdx)
                        withStyle(SpanStyle(color = accentColor, fontWeight = FontWeight.SemiBold)) {
                            append(codeText)
                        }
                        i = endIdx + 1
                        continue
                    }
                }

                append(contentToParse[i])
                i++
            }

            if (lineIdx < lines.size - 1) {
                append("\n")
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FullScreenChatView(
    productName: String,
    chatMessages: List<ChatMessage>,
    chatInput: String,
    onChatInputChange: (String) -> Unit,
    explanationMode: ChatExplanationMode,
    onExplanationModeChange: (ChatExplanationMode) -> Unit,
    promptChips: List<String>,
    isSending: Boolean,
    chatError: String?,
    onSendMessage: (String) -> Unit,
    onClose: () -> Unit
) {
    BackHandler {
        onClose()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Intelligence Inspector",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            productName.ifBlank { "Scan Audit Chat" },
                            color = PureCheckColors.accentGreen,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to Report",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PureCheckColors.darkBg)
            )
        },
        containerColor = PureCheckColors.darkBg
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Mode selector row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (mode in ChatExplanationMode.values()) {
                    val isSel = explanationMode == mode
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSel) PureCheckColors.accentGreen else PureCheckColors.cardBgElevated)
                            .clickable { onExplanationModeChange(mode) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = mode.name,
                            color = if (isSel) Color.White else Color.Gray,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            HorizontalDivider(color = PureCheckColors.cardBorder, thickness = 1.dp)

            // Chat Messages List
            val listState = rememberLazyListState()
            LaunchedEffect(chatMessages.size, isSending) {
                if (chatMessages.isNotEmpty()) {
                    listState.animateScrollToItem(chatMessages.size - 1)
                }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
            ) {
                // If chatting hasn't started, show introduction & prompt recommendations
                if (chatMessages.isEmpty()) {
                    item {
                        PureCheckCard(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                            Text(
                                "🤖 AI Food & Health Inspector",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "Ask any question about safety, chemical preservatives, sugar/sodium thresholds, health risks, or why certain ingredients were flagged.",
                                color = PureCheckColors.textSecondary,
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            "Recommended Questions:",
                            color = PureCheckColors.textSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            promptChips.forEach { chipText ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(PureCheckColors.cardBgElevated)
                                        .border(1.dp, PureCheckColors.cardBorder, RoundedCornerShape(12.dp))
                                        .clickable { onSendMessage(chipText) }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Text(chipText, color = Color.White, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                } else {
                    // Chat messages with formatted markdown
                    items(chatMessages) { msg ->
                        PureCheckChatBubble(msg = msg)
                    }
                }

                if (isSending) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 4.dp)
                        ) {
                            CircularProgressIndicator(
                                color = PureCheckColors.accentGreen,
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                "Inspector is typing analysis...",
                                color = PureCheckColors.textMuted,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                chatError?.let { err ->
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(PureCheckColors.errorRed.copy(alpha = 0.15f))
                                .border(1.dp, PureCheckColors.errorRed.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            Text(err, color = PureCheckColors.errorRed, fontSize = 12.sp)
                        }
                    }
                }
            }

            HorizontalDivider(color = PureCheckColors.cardBorder, thickness = 1.dp)

            // Bottom Input Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PureCheckColors.cardBg)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = chatInput,
                    onValueChange = onChatInputChange,
                    placeholder = { Text("Ask Inspector...", color = PureCheckColors.textMuted, fontSize = 13.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = PureCheckColors.accentGreen,
                        unfocusedBorderColor = PureCheckColors.cardBorder,
                        focusedContainerColor = PureCheckColors.darkBg,
                        unfocusedContainerColor = PureCheckColors.darkBg
                    ),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = { onSendMessage(chatInput) },
                    enabled = chatInput.isNotBlank() && !isSending,
                    colors = IconButtonDefaults.iconButtonColors(contentColor = PureCheckColors.accentGreen)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = if (chatInput.isNotBlank()) PureCheckColors.accentGreen else Color.Gray
                    )
                }
            }
        }
    }
}
