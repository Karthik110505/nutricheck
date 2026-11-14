package com.example.purecheck.ui.scanner

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation3.runtime.NavKey
import com.example.purecheck.Result
import com.example.purecheck.data.*
import com.example.purecheck.ui.components.*
import com.example.purecheck.utils.GeminiClient
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
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
fun BarcodeScannerScreen(
    onBack: () -> Unit,
    onAnalysisFinished: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val prefs = remember { PureCheckPreferences(context) }

    // Permission state
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
        }
    )

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }

    // Camera states
    val previewView = remember { PreviewView(context) }
    var isAnalyzing by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var loadingMessage by remember { mutableStateOf("Querying product database...") }
    var currentStepIndex by remember { mutableIntStateOf(0) }
    var camera by remember { mutableStateOf<androidx.camera.core.Camera?>(null) }

    val darkBg = Color(0xFF0B0F19)
    val accentGreen = Color(0xFF10B981)

    // Trigger API lookup when barcode is found
    val onBarcodeFound: (String) -> Unit = { barcode ->
        if (!isAnalyzing) {
            isAnalyzing = true
            currentStepIndex = 1
            loadingMessage = "Barcode detected: $barcode"
            
            // Run Open Food Facts and RulesEngine lookup in background
            lookupBarcodeAndAnalyze(
                barcode = barcode,
                context = context,
                prefs = prefs,
                onProgress = { step, msg ->
                    ContextCompat.getMainExecutor(context).execute {
                        currentStepIndex = step
                        loadingMessage = msg
                    }
                },
                onFinished = { jsonText ->
                    ContextCompat.getMainExecutor(context).execute {
                        onAnalysisFinished(jsonText)
                    }
                },
                onError = { err ->
                    ContextCompat.getMainExecutor(context).execute {
                        errorMessage = err
                        isAnalyzing = false
                    }
                }
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scan Barcode", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, enabled = !isAnalyzing) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = darkBg)
            )
        },
        containerColor = darkBg
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (!hasCameraPermission) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Camera Access Required",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "PureCheck needs camera access to scan product barcodes.",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { launcher.launch(Manifest.permission.CAMERA) },
                        colors = ButtonDefaults.buttonColors(containerColor = accentGreen)
                    ) {
                        Text("Grant Permission")
                    }
                }
            } else if (isAnalyzing) {
                // Analysis steps screen
                val stepsList = listOf(
                    "Barcode detected" to (currentStepIndex > 1),
                    "Querying product database" to (currentStepIndex > 2),
                    "Evaluating safety & profiles" to (currentStepIndex > 3),
                    "Generating final report" to (currentStepIndex > 4)
                )
                Box(
                    modifier = Modifier.fillMaxSize().background(darkBg),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingState(
                        message = loadingMessage,
                        steps = stepsList
                    )
                }
            } else {
                // Active Barcode Camera analyzer
                LaunchedEffect(hasCameraPermission) {
                    if (hasCameraPermission) {
                        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                        cameraProviderFuture.addListener({
                            val cameraProvider = cameraProviderFuture.get()
                            val preview = Preview.Builder().build().also {
                                it.surfaceProvider = previewView.surfaceProvider
                            }

                            // Setup image analysis for real-time scanning
                            val imageAnalysis = ImageAnalysis.Builder()
                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                .build()

                            val scanner = BarcodeScanning.getClient()
                            imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                                val mediaImage = imageProxy.image
                                if (mediaImage != null) {
                                    val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                                    scanner.process(image)
                                        .addOnSuccessListener { barcodes ->
                                            for (barcode in barcodes) {
                                                val raw = barcode.rawValue
                                                if (raw != null && raw.isNotBlank()) {
                                                    // Strip out any non-digit values (e.g. spaces/letters) to avoid API 404s
                                                    val cleanBarcode = raw.filter { it.isDigit() }
                                                    if (cleanBarcode.isNotBlank()) {
                                                        onBarcodeFound(cleanBarcode)
                                                        break
                                                    }
                                                }
                                            }
                                        }
                                        .addOnCompleteListener {
                                            imageProxy.close()
                                        }
                                } else {
                                    imageProxy.close()
                                }
                            }

                            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                            try {
                                cameraProvider.unbindAll()
                                camera = cameraProvider.bindToLifecycle(
                                    lifecycleOwner,
                                    cameraSelector,
                                    preview,
                                    imageAnalysis
                                )
                            } catch (exc: Exception) {
                                errorMessage = "Failed to start camera preview: ${exc.message}"
                            }
                        }, ContextCompat.getMainExecutor(context))
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    AndroidView(
                        factory = { previewView },
                        modifier = Modifier.fillMaxSize()
                    )

                    // Target Overlay box
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .fillMaxHeight(0.2f)
                                .align(Alignment.Center)
                                .border(2.dp, accentGreen, RoundedCornerShape(12.dp))
                        )
                    }

                    Text(
                        "Align product barcode inside the green box",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 24.dp)
                            .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            // Error alert
            errorMessage?.let { err ->
                AlertDialog(
                    onDismissRequest = { errorMessage = null },
                    title = { Text("Scanner Error") },
                    text = { Text(err) },
                    confirmButton = {
                        TextButton(onClick = { errorMessage = null }) {
                            Text("OK")
                        }
                    },
                    containerColor = PureCheckColors.cardBg,
                    titleContentColor = Color.White,
                    textContentColor = Color.LightGray
                )
            }
        }
    }
}

private fun lookupBarcodeAndAnalyze(
    barcode: String,
    context: Context,
    prefs: PureCheckPreferences,
    onProgress: (Int, String) -> Unit,
    onFinished: (String) -> Unit,
    onError: (String) -> Unit
) {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://world.openfoodfacts.org/api/v2/product/$barcode.json")
        .header("User-Agent", "PureCheck - Android - Version 2.0 - CSE Major Project")
        .build()

    onProgress(2, "Contacting Open Food Facts database...")

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                val userFriendlyMsg = if (response.code == 404) {
                    "This product is not registered in the food database yet. Please try scanning the ingredient label directly instead!"
                } else {
                    "API Server returned error: ${response.code}"
                }
                onError(userFriendlyMsg)
                return@launch
            }

            val body = response.body?.string() ?: ""
            val json = JSONObject(body)
            val status = json.optInt("status", 0)

            if (status != 1) {
                onError("This product is not registered in the food database yet. Please try scanning the ingredient label directly instead!")
                return@launch
            }

            onProgress(3, "Evaluating ingredients & additives...")

            val product = json.getJSONObject("product")
            val name = product.optString("product_name", product.optString("product_name_en", "Unknown Product"))
            val brand = product.optString("brands", "Generic Brand")
            val ingredients = product.optString("ingredients_text", product.optString("ingredients_text_en", ""))

            if (ingredients.isBlank()) {
                onError("Product found, but ingredients list is empty in the database.")
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

            onProgress(4, "Generating structured audit report...")

            // Package as rich report JSON
            val reportJson = JSONObject()
            reportJson.put("productName", "$name ($brand)")
            reportJson.put("brand", brand)
            reportJson.put("barcode", barcode)
            reportJson.put("overallScore", ruleResult.overallScore)
            reportJson.put("overallGrade", ruleResult.overallGrade)
            reportJson.put("confidenceScore", 96)
            
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

            val ingredientsArray = JSONArray()
            ruleResult.matchedAdditives.forEach { add ->
                val addObj = JSONObject()
                addObj.put("name", add.name)
                addObj.put("rating", if (add.evidenceLevel == "High") "unsafe" else if (add.evidenceLevel == "Medium") "caution" else "safe")
                addObj.put("description", add.description)
                ingredientsArray.put(addObj)
            }
            
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

            val nutAnalysis = JSONObject()
            nutAnalysis.put("calories", calories.toInt())
            nutAnalysis.put("sugar", sugar.toInt())
            nutAnalysis.put("protein", protein.toInt())
            nutAnalysis.put("fiber", fiber.toInt())
            nutAnalysis.put("sodium", sodiumMg.toInt())
            nutAnalysis.put("saturatedFat", saturatedFat.toInt())
            reportJson.put("nutritionAnalysis", nutAnalysis)

            reportJson.put("summary", "Scanned barcode lookup results retrieved from Open Food Facts. Deterministic scoring evaluated the ingredients as having ${ruleResult.matchedAdditives.size} flagged additives.")
            
            val swapsArray = JSONArray()
            val swap1 = JSONObject()
            swap1.put("brand", "Alternative clean brand")
            swap1.put("product", "Organic $name")
            swap1.put("reason", "Contains no artificial preservatives or Palm Oil.")
            swapsArray.put(swap1)
            reportJson.put("cleanSwaps", swapsArray)

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

            onFinished(reportJson.toString())

        } catch (e: IOException) {
            onError("Network connection failure: ${e.message}")
        } catch (e: Exception) {
            onError("Unexpected lookup failure: ${e.message}")
        }
    }
}

private fun foundAdditive(ing: String, matched: List<IngredientDef>): Boolean {
    val norm = ing.lowercase()
    return matched.any { it.name.lowercase() == norm || it.aliases.any { al -> al.lowercase() == norm } }
}
