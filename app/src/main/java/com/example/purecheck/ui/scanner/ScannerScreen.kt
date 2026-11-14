package com.example.purecheck.ui.scanner

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.purecheck.data.HistoryItem
import com.example.purecheck.data.PureCheckPreferences
import com.example.purecheck.utils.GeminiClient
import androidx.compose.ui.res.painterResource
import com.example.purecheck.ui.components.*
import com.example.purecheck.R
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(
    onBack: () -> Unit,
    onAnalysisFinished: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

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

    // CameraX logic states
    val imageCapture = remember { ImageCapture.Builder().build() }
    val previewView = remember { PreviewView(context) }
    var isAnalyzing by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var loadingMessage by remember { mutableStateOf("Analyzing image...") }
    var camera by remember { mutableStateOf<androidx.camera.core.Camera?>(null) }
    var isTorchOn by remember { mutableStateOf(false) }
    var currentStepIndex by remember { mutableIntStateOf(0) }
    var isWideViewfinder by remember { mutableStateOf(true) }

    val widthFraction by animateFloatAsState(
        targetValue = if (isWideViewfinder) 0.9f else 0.7f,
        animationSpec = tween(durationMillis = 300),
        label = "widthFraction"
    )
    val heightFraction by animateFloatAsState(
        targetValue = if (isWideViewfinder) 0.22f else 0.45f,
        animationSpec = tween(durationMillis = 300),
        label = "heightFraction"
    )

    val darkBg = Color(0xFF0B0F19)
    val accentGreen = Color(0xFF10B981)

    // Animation for scanning line
    val infiniteTransition = rememberInfiniteTransition(label = "scanner")
    val scanProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scannerLine"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scan Ingredients", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, enabled = !isAnalyzing) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    if (hasCameraPermission && !isAnalyzing) {
                        IconButton(onClick = {
                            isWideViewfinder = !isWideViewfinder
                        }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Toggle Viewfinder",
                                tint = if (isWideViewfinder) accentGreen else Color.White
                            )
                        }
                        IconButton(onClick = {
                            isTorchOn = !isTorchOn
                            camera?.cameraControl?.enableTorch(isTorchOn)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_flash),
                                contentDescription = "Flashlight",
                                tint = if (isTorchOn) Color.Yellow else Color.White
                            )
                        }
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
                // Request Permission Screen
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
                        "PureCheck needs camera access to snap photos of ingredient lists.",
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
                val stepsList = listOf(
                    "Image captured" to (currentStepIndex > 1),
                    "Extracting text via OCR" to (currentStepIndex > 2),
                    "Evaluating additives & risks" to (currentStepIndex > 3),
                    "Generating structured report" to (currentStepIndex > 4)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(darkBg),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingState(
                        message = loadingMessage,
                        steps = stepsList
                    )
                }
            } else {
                // Active Camera Screen
                LaunchedEffect(hasCameraPermission) {
                    if (hasCameraPermission) {
                        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                        cameraProviderFuture.addListener({
                            val cameraProvider = cameraProviderFuture.get()
                            val preview = Preview.Builder().build().also {
                                it.surfaceProvider = previewView.surfaceProvider
                            }

                            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                            try {
                                cameraProvider.unbindAll()
                                camera = cameraProvider.bindToLifecycle(
                                    lifecycleOwner,
                                    cameraSelector,
                                    preview,
                                    imageCapture
                                )
                            } catch (exc: Exception) {
                                errorMessage = "Failed to start camera preview: ${exc.message}"
                            }
                        }, ContextCompat.getMainExecutor(context))
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    // Camera view container
                    AndroidView(
                        factory = { previewView },
                        modifier = Modifier.fillMaxSize()
                    )

                    // Target Finder Overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f))
                    ) {
                        // Punch a transparent hole in the overlay for viewfinder
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height
                            val rectWidth = w * widthFraction
                            val rectHeight = h * heightFraction
                            val left = (w - rectWidth) / 2
                            val top = (h - rectHeight) / 2

                            // Draw scanning laser line
                            val lineY = top + (rectHeight * scanProgress)
                            drawLine(
                                color = accentGreen,
                                start = Offset(left, lineY),
                                end = Offset(left + rectWidth, lineY),
                                strokeWidth = 6f
                            )
                        }

                        // Viewfinder border box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(widthFraction)
                                .fillMaxHeight(heightFraction)
                                .align(Alignment.Center)
                                .border(2.dp, accentGreen, RoundedCornerShape(12.dp))
                        )
                    }

                    // Scan instruction text
                    Text(
                        "Align ingredient text inside the green box",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 24.dp)
                            .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    // Capture Trigger Panel
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = {
                                isAnalyzing = true
                                currentStepIndex = 1
                                errorMessage = null
                                loadingMessage = "Snapping photo..."
                                captureImage(context, imageCapture, { step ->
                                    currentStepIndex = step
                                }) { result ->
                                    result.fold(
                                        onSuccess = { scanData ->
                                            currentStepIndex = 3
                                            loadingMessage = "Evaluating additives & risks..."
                                            val prefs = PureCheckPreferences(context)
                                            val profile = prefs.getUserProfile()
                                            val provider = prefs.getProvider()
                                            val apiKey = when (provider.lowercase()) {
                                                "groq" -> prefs.getGroqApiKey()
                                                "deepseek" -> prefs.getDeepSeekApiKey()
                                                "bytez" -> prefs.getBytezApiKey()
                                                else -> prefs.getApiKey()
                                            }

                                            val onApiFinished: (Result<String>) -> Unit = { apiResult ->
                                                currentStepIndex = 4
                                                loadingMessage = "Generating structured report..."
                                                apiResult.fold(
                                                    onSuccess = { jsonText ->
                                                        try {
                                                            val parsed = JSONObject(jsonText)
                                                            val name = parsed.optString("productName", "Scanned Product")
                                                            val grade = parsed.optString("overallGrade", "B")
                                                            val score = parsed.optInt("overallScore", 80)
                                                            val summary = parsed.optString("summary", "")

                                                            // Add to scan history
                                                            val item = HistoryItem(
                                                                id = UUID.randomUUID().toString(),
                                                                productName = name,
                                                                overallGrade = grade,
                                                                overallScore = score,
                                                                summary = summary,
                                                                jsonResult = jsonText,
                                                                timestamp = System.currentTimeMillis()
                                                            )
                                                            prefs.addHistoryItem(item)

                                                            // Navigate on Main thread
                                                            ContextCompat.getMainExecutor(context).execute {
                                                                onAnalysisFinished(jsonText)
                                                            }
                                                        } catch (e: Exception) {
                                                            ContextCompat.getMainExecutor(context).execute {
                                                                errorMessage = "JSON parsing error: ${e.message}"
                                                                isAnalyzing = false
                                                            }
                                                        }
                                                    },
                                                    onFailure = { err ->
                                                        ContextCompat.getMainExecutor(context).execute {
                                                            errorMessage = err.message ?: "Failed to query API."
                                                            isAnalyzing = false
                                                        }
                                                    }
                                                )
                                            }

                                            val useTextModel = provider.lowercase() == "deepseek" || provider.lowercase() == "groq"
                                            if (useTextModel) {
                                                val textToAnalyze = scanData.extractedText ?: ""
                                                if (textToAnalyze.isBlank()) {
                                                    ContextCompat.getMainExecutor(context).execute {
                                                        errorMessage = "No text could be extracted from the image. Please try again under better lighting."
                                                        isAnalyzing = false
                                                    }
                                                    return@fold
                                                }
                                                GeminiClient.analyzeIngredientsText(textToAnalyze, profile, provider, apiKey, onApiFinished)
                                            } else {
                                                GeminiClient.analyzeIngredients(scanData.base64, scanData.extractedText ?: "", profile, provider, apiKey, onApiFinished)
                                            }
                                        },
                                        onFailure = { err ->
                                            errorMessage = "Capture failed: ${err.message}"
                                            isAnalyzing = false
                                        }
                                    )
                                }
                            },
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(4.dp, accentGreen, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Capture",
                                tint = accentGreen,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }

            // Error Display Modal / Text
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
                    containerColor = Color(0xFF1E293B),
                    titleContentColor = Color.White,
                    textContentColor = Color.LightGray
                )
            }
        }
    }
}

data class ScanData(
    val base64: String,
    val extractedText: String?
)

private fun captureImage(
    context: Context,
    imageCapture: ImageCapture,
    onProgress: (Int) -> Unit,
    onResult: (Result<ScanData>) -> Unit
) {
    val photoFile = File(
        context.cacheDir,
        "scan_${System.currentTimeMillis()}.jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                ContextCompat.getMainExecutor(context).execute { onProgress(2) }
                // Compress & convert to Base64 in background
                Thread {
                    try {
                        val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                        // Resize image to max 768 width/height for faster upload and processing
                        val scaledBitmap = resizeBitmap(bitmap, 768)
                        
                        val baos = ByteArrayOutputStream()
                        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
                        val bytes = baos.toByteArray()
                        val base64 = Base64.encodeToString(bytes, Base64.NO_WRAP)
                        
                        // Perform local OCR using ML Kit synchronously
                        var extractedText: String? = null
                        try {
                            ContextCompat.getMainExecutor(context).execute { onProgress(3) }
                            val recognizer = com.google.mlkit.vision.text.TextRecognition.getClient(
                                com.google.mlkit.vision.text.latin.TextRecognizerOptions.DEFAULT_OPTIONS
                            )
                            val inputImage = com.google.mlkit.vision.common.InputImage.fromBitmap(scaledBitmap, 0)
                            val task = recognizer.process(inputImage)
                            val visionText = com.google.android.gms.tasks.Tasks.await(task)
                            extractedText = visionText.text
                        } catch (ocrEx: Exception) {
                            ocrEx.printStackTrace()
                        }
                        
                        // Clean up file
                        photoFile.delete()
                        
                        onResult(Result.success(ScanData(base64, extractedText)))
                    } catch (e: Exception) {
                        onResult(Result.failure(e))
                    }
                }.start()
            }

            override fun onError(exception: ImageCaptureException) {
                onResult(Result.failure(exception))
            }
        }
    )
}

private fun resizeBitmap(source: Bitmap, maxDimension: Int): Bitmap {
    val width = source.width
    val height = source.height
    
    if (width <= maxDimension && height <= maxDimension) return source

    val newWidth: Int
    val newHeight: Int
    if (width > height) {
        newWidth = maxDimension
        newHeight = (maxDimension.toFloat() / width * height).toInt()
    } else {
        newHeight = maxDimension
        newWidth = (maxDimension.toFloat() / height * width).toInt()
    }

    return Bitmap.createScaledBitmap(source, newWidth, newHeight, true)
}
