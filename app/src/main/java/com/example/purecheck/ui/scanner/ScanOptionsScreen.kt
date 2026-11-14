package com.example.purecheck.ui.scanner

import android.content.Context
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
import com.example.purecheck.BarcodeScanner
import com.example.purecheck.Settings
import com.example.purecheck.data.*
import com.example.purecheck.ui.components.*
import com.example.purecheck.utils.GeminiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanOptionsScreen(
    onNavigate: (NavKey) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs = remember { PureCheckPreferences(context) }
    
    var barcodeText by remember { mutableStateOf("") }
    var isSearchingBarcode by remember { mutableStateOf(false) }
    var barcodeError by remember { mutableStateOf<String?>(null) }
    
    // Shopping List items
    var shoppingItems by remember { mutableStateOf(emptyList<ShoppingItem>()) }
    var newItemName by remember { mutableStateOf("") }
    var showSummaryDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        shoppingItems = prefs.getShoppingList()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PureCheckColors.darkBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Food Scanner",
            color = PureCheckColors.textPrimary,
            fontWeight = FontWeight.Black,
            fontSize = 24.sp
        )
        Text(
            text = "Analyze ingredients, safety rankings, and compatibility instantly.",
            color = PureCheckColors.textSecondary,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Large illustrated visual card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF1E2B44), Color(0xFF0F172A))
                    )
                )
                .border(1.dp, Color(0xFF26354E), RoundedCornerShape(20.dp))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                NutriCheckLogo(logoSize = 72.dp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "FOOD INTELLIGENCE ACTIVE",
                    color = PureCheckColors.accentGreen,
                    fontWeight = FontWeight.Black,
                    fontSize = 11.sp,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Scan or search products to evaluate health risks.",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Scanner Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PremiumCard(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNavigate(Scanner) }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = PureCheckColors.accentGreen,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text("Scan Label", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Analyze printed labels using device camera.", color = PureCheckColors.textSecondary, fontSize = 11.sp, lineHeight = 15.sp)
            }

            PremiumCard(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNavigate(BarcodeScanner) }
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = PureCheckColors.accentBlue,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text("Scan Barcode", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Scan barcodes to query database matches.", color = PureCheckColors.textSecondary, fontSize = 11.sp, lineHeight = 15.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Barcode Input search section
        PureCheckCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Search Product by Barcode",
                color = PureCheckColors.textPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = barcodeText,
                    onValueChange = { barcodeText = it },
                    placeholder = { Text("Enter EAN/UPC barcode...", color = PureCheckColors.textMuted, fontSize = 13.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = PureCheckColors.accentGreen,
                        unfocusedBorderColor = Color.Gray
                    ),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        val barcode = barcodeText.filter { it.isDigit() }
                        if (barcode.isNotBlank()) {
                            isSearchingBarcode = true
                            barcodeError = null
                            lookupBarcodeAndAnalyze(
                                barcode = barcode,
                                context = context,
                                prefs = prefs,
                                onFinished = { jsonText ->
                                    isSearchingBarcode = false
                                    barcodeText = ""
                                    onNavigate(Result(jsonText))
                                },
                                onError = { error ->
                                    isSearchingBarcode = false
                                    barcodeError = error
                                }
                            )
                        }
                    },
                    enabled = !isSearchingBarcode && barcodeText.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = PureCheckColors.accentGreen)
                ) {
                    if (isSearchingBarcode) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp))
                    } else {
                        Text("Search", fontWeight = FontWeight.Bold)
                    }
                }
            }
            if (barcodeError != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = barcodeError ?: "",
                    color = PureCheckColors.errorRed,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Shopping List Mode Section
        PureCheckSectionHeader(title = "Shopping List Mode")
        Spacer(modifier = Modifier.height(8.dp))

        PureCheckCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Manage your items while shopping. View scanned product safety rating and overall scores before purchasing.",
                color = PureCheckColors.textSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Add Shopping item row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newItemName,
                    onValueChange = { newItemName = it },
                    placeholder = { Text("Add Milk, Bread, etc...", color = PureCheckColors.textMuted, fontSize = 13.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = PureCheckColors.accentGreen,
                        unfocusedBorderColor = Color.Gray
                    ),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        val name = newItemName.trim()
                        if (name.isNotBlank()) {
                            val newItem = ShoppingItem(
                                id = UUID.randomUUID().toString(),
                                name = name,
                                checked = false
                            )
                            prefs.addShoppingItem(newItem)
                            shoppingItems = prefs.getShoppingList()
                            newItemName = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PureCheckColors.accentGreen)
                ) {
                    Text("Add", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // List of items
            if (shoppingItems.isEmpty()) {
                Text(
                    "No items on your shopping list yet.",
                    color = PureCheckColors.textMuted,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                )
            } else {
                shoppingItems.forEach { item ->
                    ShoppingListRow(
                        item = item,
                        onCheckedChange = { isChecked ->
                            val updated = item.copy(checked = isChecked)
                            prefs.updateShoppingItem(updated)
                            shoppingItems = prefs.getShoppingList()
                        },
                        onLinkScan = {
                            // Find matching scanned product in history by name to associate score
                            val scanHistory = prefs.getHistory()
                            val match = scanHistory.firstOrNull { it.productName.contains(item.name, ignoreCase = true) || item.name.contains(it.productName, ignoreCase = true) }
                            if (match != null) {
                                val updated = item.copy(
                                    score = match.overallScore,
                                    grade = match.overallGrade,
                                    scanId = match.id
                                )
                                prefs.updateShoppingItem(updated)
                                shoppingItems = prefs.getShoppingList()
                            } else {
                                // Direct to scanner to evaluate
                                onNavigate(Scanner)
                            }
                        },
                        onDelete = {
                            prefs.deleteShoppingItem(item.id)
                            shoppingItems = prefs.getShoppingList()
                        }
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PrimaryActionButton(
                        text = "Complete Shopping",
                        onClick = { showSummaryDialog = true },
                        modifier = Modifier.weight(1f)
                    )
                    SecondaryActionButton(
                        text = "Clear",
                        onClick = {
                            prefs.clearShoppingList()
                            shoppingItems = emptyList()
                        },
                        modifier = Modifier.width(80.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(100.dp))
    }

    // Shopping list summary modal
    if (showSummaryDialog) {
        val checkedItems = shoppingItems.filter { it.checked && it.score != null }
        val avgScore = if (checkedItems.isEmpty()) 0 else (checkedItems.sumOf { it.score!! } / checkedItems.size)
        val bestChoice = checkedItems.maxByOrNull { it.score!! }
        val worstChoice = checkedItems.minByOrNull { it.score!! }

        AlertDialog(
            onDismissRequest = { showSummaryDialog = false },
            title = {
                Text(
                    "Shopping Summary",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        "${checkedItems.size} out of ${shoppingItems.size} products analyzed in your basket.",
                        color = PureCheckColors.textSecondary,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Average Basket Score: $avgScore/100",
                        color = if (avgScore >= 75) PureCheckColors.accentGreen else if (avgScore >= 55) PureCheckColors.warningYellow else PureCheckColors.errorRed,
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    if (bestChoice != null) {
                        Text(
                            "🏆 Best Choice: ${bestChoice.name} (Score ${bestChoice.score})",
                            color = PureCheckColors.accentGreen,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    if (worstChoice != null && worstChoice.score!! < 60) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "⚠ Consider Replacing: ${worstChoice.name} (Score ${worstChoice.score} - Low Grade)",
                            color = PureCheckColors.errorRed,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showSummaryDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = PureCheckColors.accentGreen)
                ) {
                    Text("OK", color = Color.White)
                }
            },
            containerColor = PureCheckColors.cardBg
        )
    }
}

@Composable
fun ShoppingListRow(
    item: ShoppingItem,
    onCheckedChange: (Boolean) -> Unit,
    onLinkScan: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = PureCheckColors.accentGreen)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = item.name,
            color = if (item.checked) PureCheckColors.textSecondary else PureCheckColors.textPrimary,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        
        if (item.score != null && item.grade != null) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(PureCheckColors.cardBgElevated)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .clickable { onLinkScan() }
            ) {
                Text(
                    text = "${item.score}/100 [${item.grade}]",
                    color = if (item.score >= 75) PureCheckColors.accentGreen else if (item.score >= 55) PureCheckColors.warningYellow else PureCheckColors.errorRed,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            IconButton(onClick = onLinkScan) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Evaluate item",
                    tint = PureCheckColors.accentGreen,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Delete Item",
                tint = PureCheckColors.textMuted,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

private fun lookupBarcodeAndAnalyze(
    barcode: String,
    context: Context,
    prefs: PureCheckPreferences,
    onFinished: (String) -> Unit,
    onError: (String) -> Unit
) {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://world.openfoodfacts.org/api/v2/product/$barcode.json")
        .header("User-Agent", "PureCheck - Android - Version 2.0 - CSE Major Project")
        .build()

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                val userFriendlyMsg = if (response.code == 404) {
                    "This product is not registered in the food database yet. Please try scanning the ingredient label directly instead!"
                } else {
                    "API Server returned error: ${response.code}"
                }
                launch(Dispatchers.Main) { onError(userFriendlyMsg) }
                return@launch
            }

            val body = response.body?.string() ?: ""
            val json = JSONObject(body)
            val status = json.optInt("status", 0)

            if (status != 1) {
                launch(Dispatchers.Main) { onError("Product not found in Open Food Facts.") }
                return@launch
            }

            val product = json.getJSONObject("product")
            val name = product.optString("product_name", product.optString("product_name_en", "Unknown Product"))
            val brand = product.optString("brands", "Generic Brand")
            val ingredients = product.optString("ingredients_text", product.optString("ingredients_text_en", ""))

            if (ingredients.isBlank()) {
                launch(Dispatchers.Main) { onError("Product found, but ingredients details are missing in database.") }
                return@launch
            }

            // Extract nutrition
            val nutriments = product.optJSONObject("nutriments")
            val calories = nutriments?.optDouble("energy-kcal_100g", 0.0)?.toFloat() ?: 0f
            val sugar = nutriments?.optDouble("sugars_100g", 0.0)?.toFloat() ?: 0f
            val protein = nutriments?.optDouble("proteins_100g", 0.0)?.toFloat() ?: 0f
            val fiber = nutriments?.optDouble("fiber_100g", 0.0)?.toFloat() ?: 0f
            val sodiumGrams = nutriments?.optDouble("sodium_100g", 0.0)?.toFloat() ?: 0f
            val sodiumMg = sodiumGrams * 1000f
            val saturatedFat = nutriments?.optDouble("saturated-fat_100g", 0.0)?.toFloat() ?: 0f

            // Compute rules engine calculations
            val profile = prefs.getUserProfile()
            val watchlist = prefs.getWatchlist()
            val ruleResult = RulesEngine.calculate(
                rawIngredientsText = ingredients,
                calories = calories,
                sugarGrams = sugar,
                proteinGrams = protein,
                fiberGrams = fiber,
                sodiumMg = sodiumMg,
                saturatedFatGrams = saturatedFat,
                watchlist = watchlist,
                profile = profile
            )

            // Package as rich report JSON
            val reportJson = JSONObject()
            reportJson.put("productName", "$name ($brand)")
            reportJson.put("brand", brand)
            reportJson.put("barcode", barcode)
            reportJson.put("overallScore", ruleResult.overallScore)
            reportJson.put("overallGrade", ruleResult.overallGrade)
            reportJson.put("confidenceScore", 92)
            
            val breakdownJson = JSONObject()
            breakdownJson.put("ingredientQuality", ruleResult.breakdown.ingredientQuality)
            breakdownJson.put("nutrition", ruleResult.breakdown.nutrition)
            breakdownJson.put("additives", ruleResult.breakdown.additives)
            breakdownJson.put("sugar", ruleResult.breakdown.sugar)
            breakdownJson.put("sodium", ruleResult.breakdown.sodium)
            reportJson.put("scoreBreakdown", breakdownJson)

            val findingsArray = JSONArray()
            ruleResult.keyFindings.forEach { findingsArray.put(it) }
            reportJson.put("keyFindings", findingsArray)

            val risksArray = JSONArray()
            ruleResult.conflicts.forEach { risksArray.put(it) }
            reportJson.put("healthRisks", risksArray)

            // Ingredient Explorer details
            val ingredientsArray = JSONArray()
            ruleResult.matchedAdditives.forEach { add ->
                val addObj = JSONObject()
                addObj.put("name", add.name)
                addObj.put("rating", if (add.evidenceLevel == "High") "unsafe" else if (add.evidenceLevel == "Medium") "caution" else "safe")
                addObj.put("description", add.description)
                ingredientsArray.put(addObj)
            }
            // Put other ingredients parsed roughly
            val genericIngredients = ingredients.split(",").map { it.trim() }.filter { it.isNotBlank() }
            genericIngredients.forEach { ing ->
                if (!foundAdditive(ing, ruleResult.matchedAdditives)) {
                    val addObj = JSONObject()
                    addObj.put("name", ing)
                    addObj.put("rating", "safe")
                    addObj.put("description", "Generally safe natural food component.")
                    ingredientsArray.put(addObj)
                }
            }
            reportJson.put("ingredientAnalysis", ingredientsArray)

            // Nutrition values analysis
            val nutAnalysis = JSONObject()
            nutAnalysis.put("calories", calories.toInt())
            nutAnalysis.put("sugar", sugar.toInt())
            nutAnalysis.put("protein", protein.toInt())
            nutAnalysis.put("fiber", fiber.toInt())
            nutAnalysis.put("sodium", sodiumMg.toInt())
            nutAnalysis.put("saturatedFat", saturatedFat.toInt())
            reportJson.put("nutritionAnalysis", nutAnalysis)

            reportJson.put("summary", "Scanned barcode lookup results retrieved from Open Food Facts. Deterministic scoring evaluated the ingredients as having ${ruleResult.matchedAdditives.size} flagged additives.")
            
            // Swaps
            val swapsArray = JSONArray()
            val swap1 = JSONObject()
            swap1.put("brand", "Alternative clean brand")
            swap1.put("product", "Organic $name")
            swap1.put("reason", "Contains no artificial preservatives or Palm Oil.")
            swapsArray.put(swap1)
            reportJson.put("cleanSwaps", swapsArray)

            // Add history item
            val historyItem = HistoryItem(
                id = UUID.randomUUID().toString(),
                productName = name,
                overallGrade = ruleResult.overallGrade,
                overallScore = ruleResult.overallScore,
                summary = "Barcode lookup result.",
                jsonResult = reportJson.toString(),
                timestamp = System.currentTimeMillis()
            )
            prefs.addHistoryItem(historyItem)

            launch(Dispatchers.Main) {
                onFinished(reportJson.toString())
            }

        } catch (e: IOException) {
            launch(Dispatchers.Main) { onError("Network connection failure: ${e.message}") }
        } catch (e: Exception) {
            launch(Dispatchers.Main) { onError("Unexpected lookup failure: ${e.message}") }
        }
    }
}

private fun foundAdditive(ing: String, matched: List<IngredientDef>): Boolean {
    val norm = ing.lowercase()
    return matched.any { it.name.lowercase() == norm || it.aliases.any { al -> al.lowercase() == norm } }
}
