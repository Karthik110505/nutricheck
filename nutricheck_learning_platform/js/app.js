/**
 * NutriCheck Interactive Learning Platform - Master Application Controller
 * Handles Routing, State, Real-Time Scoring Simulation, Diagram Inspection,
 * Code Viewer, Viva Examination Center, and Local Progress Tracking.
 */

document.addEventListener("DOMContentLoaded", function() {
  const AppState = {
    currentModuleId: "module-1",
    explanationMode: localStorage.getItem("nutricheck_mode") || "beginner", // "beginner" or "panel"
    completedModules: JSON.parse(localStorage.getItem("nutricheck_completed") || "[]"),
    activeSourceFile: "RulesEngine.kt",
    activeVivaCategory: "All",
    searchQuery: "",
    isMobileSidebarOpen: false
  };

  // Cache DOM Elements
  const DOM = {
    sidebarNav: document.getElementById("sidebar-nav"),
    progressBarFill: document.getElementById("progress-bar-fill"),
    progressText: document.getElementById("progress-text"),
    contentArea: document.getElementById("content-area"),
    breadcrumbCurrent: document.getElementById("breadcrumb-current"),
    modeBtnBeginner: document.getElementById("mode-btn-beginner"),
    modeBtnPanel: document.getElementById("mode-btn-panel"),
    searchModalBackdrop: document.getElementById("search-modal-backdrop"),
    searchModalInput: document.getElementById("search-modal-input"),
    searchResultsList: document.getElementById("search-results-list"),
    searchTriggerBtn: document.getElementById("search-trigger-btn"),
    searchCloseBtn: document.getElementById("search-close-btn"),
    sidebarToggleBtn: document.getElementById("sidebar-toggle-btn"),
    sidebar: document.getElementById("sidebar"),
    nodeInspectorModal: document.getElementById("node-inspector-modal"),
    inspectorCloseBtn: document.getElementById("inspector-close-btn"),
    inspectorContent: document.getElementById("inspector-content")
  };

  // Real Source Files Content Map (Annotated for Student Learning)
  const SourceFilesMap = {
    "RulesEngine.kt": {
      path: "app/src/main/java/com/example/purecheck/data/RulesEngine.kt",
      role: "Deterministic 5-Axis Mathematical Scoring & Profile Conflict Engine",
      summary: "Computes Quality, Additives, Nutrition, Sugar, and Sodium scores clamped to [10, 100], calculates Grade A-F, and evaluates dietary conflicts for UserProfile.",
      code: `package com.example.purecheck.data

import kotlin.math.max
import kotlin.math.min

data class ScoreBreakdown(
    val ingredientQuality: Int,
    val nutrition: Int,
    val additives: Int,
    val sugar: Int,
    val sodium: Int
)

data class DeterministicResult(
    val overallScore: Int,
    val overallGrade: String,
    val breakdown: ScoreBreakdown,
    val profileScore: Int,
    val matchedWatchlist: List<String>,
    val matchedAdditives: List<IngredientDef>,
    val keyFindings: List<String>,
    val conflicts: List<String>
)

object RulesEngine {

    fun calculate(
        rawIngredientsText: String,
        calories: Float = 0f,
        sugarGrams: Float = 0f,
        proteinGrams: Float = 0f,
        fiberGrams: Float = 0f,
        sodiumMg: Float = 0f,
        saturatedFatGrams: Float = 0f,
        watchlist: List<String> = emptyList(),
        profile: UserProfile
    ): DeterministicResult {
        val lowerText = rawIngredientsText.lowercase()
        val foundAdditives = mutableListOf<IngredientDef>()
        val matchedWatchlist = mutableListOf<String>()
        val keyFindings = mutableListOf<String>()

        // 1. Identify Additives & Flags
        for (def in IngredientDatabase.dictionary) {
            var matched = false
            if (lowerText.contains(def.name.lowercase())) {
                matched = true
            } else if (def.eNumber != null && lowerText.contains(def.eNumber.lowercase())) {
                matched = true
            } else {
                for (alias in def.aliases) {
                    if (lowerText.contains(alias.lowercase())) {
                        matched = true
                        break
                    }
                }
            }
            if (matched) foundAdditives.add(def)
        }

        // 2. Identify Watchlist matches
        for (item in watchlist) {
            if (item.isNotBlank() && lowerText.contains(item.lowercase().trim())) {
                matchedWatchlist.add(item.trim())
            }
        }

        // 3. Compute Ingredient Quality Score (Base 100)
        var qualityScore = 100
        for (def in foundAdditives) {
            val deduction = when (def.evidenceLevel) {
                "High" -> 15
                "Medium" -> 8
                "Low" -> 4
                else -> 2
            }
            qualityScore -= deduction
        }
        // Obfuscation check inside engine
        if (lowerText.contains("vegetable oil") || lowerText.contains("refined oil")) {
            qualityScore -= 10
            keyFindings.add("⚠ Potential obfuscated vegetable/palm oil detected.")
        }
        if (lowerText.contains("yeast extract") || lowerText.contains("hydrolyzed protein")) {
            qualityScore -= 8
            keyFindings.add("⚠ Contains flavor-masking flavor enhancers (possible MSG).")
        }
        qualityScore = max(10, min(100, qualityScore))

        // 4. Compute Additives Score (Base 100)
        var additivesScore = 100
        val additivesCount = foundAdditives.count {
            it.category.contains("Color") || it.category.contains("Preservative") || 
            it.category.contains("Sweetener") || it.category.contains("Emulsifier") || 
            it.category.contains("Enhancer")
        }
        additivesScore -= (additivesCount * 8)
        foundAdditives.forEach { add ->
            if (add.evidenceLevel == "High" && add.category.contains("Preservative")) {
                additivesScore -= 5
            }
        }
        additivesScore = max(10, min(100, additivesScore))

        // 5. Compute Sugar Score (Base 100)
        var sugarScore = 100
        if (sugarGrams > 0f) {
            if (sugarGrams > 5f) {
                val excess = sugarGrams - 5f
                sugarScore -= (excess * 4.5f).toInt()
            }
        } else {
            if (lowerText.contains("sugar") || lowerText.contains("sucrose")) sugarScore = 75
            if (lowerText.contains("corn syrup") || lowerText.contains("dextrose") || lowerText.contains("fructose")) {
                sugarScore = 60
                keyFindings.add("⚠ High fructose corn syrup or refined sugars detected.")
            }
        }
        sugarScore = max(10, min(100, sugarScore))

        // 6. Compute Sodium Score (Base 100)
        var sodiumScore = 100
        if (sodiumMg > 0f) {
            if (sodiumMg > 140f) {
                val excess = sodiumMg - 140f
                sodiumScore -= (excess * 0.15f).toInt()
            }
        } else {
            if (lowerText.contains("salt") || lowerText.contains("sodium")) sodiumScore = 75
        }
        sodiumScore = max(10, min(100, sodiumScore))

        // 7. Compute Nutrition Score (Base 100)
        var nutritionScore = 100
        if (calories > 350f) nutritionScore -= 15
        if (proteinGrams > 8f) nutritionScore += 10
        if (fiberGrams > 3f) nutritionScore += 10
        if (saturatedFatGrams > 5f) nutritionScore -= 15
        nutritionScore = (nutritionScore + sugarScore + sodiumScore) / 3
        nutritionScore = max(10, min(100, nutritionScore))

        // 8. Compute Overall Score & Grade
        val overallScore = (qualityScore + additivesScore + nutritionScore + sugarScore + sodiumScore) / 5
        val overallGrade = when {
            overallScore >= 80 -> "A"
            overallScore >= 68 -> "B"
            overallScore >= 52 -> "C"
            overallScore >= 38 -> "D"
            else -> "F"
        }

        // 9. Compute Personalized Profile Score & Conflicts
        var profileScore = overallScore
        val conflicts = mutableListOf<String>()

        if (profile.vegan) {
            // Evaluates animal terms & vegan_conflict flags
            if (lowerText.contains("milk") || lowerText.contains("gelatin") || lowerText.contains("egg")) {
                profileScore -= 30
                conflicts.add("✕ VEGAN CONFLICT: Contains animal-derived ingredients.")
            }
        }
        if (profile.diabetic && (sugarGrams > 10f || lowerText.contains("corn syrup"))) {
            profileScore -= 20
            conflicts.add("✕ DIABETIC CONFLICT: High sugar content conflicts with Diabetic preference.")
        }

        profileScore = max(10, min(overallScore, profileScore))

        return DeterministicResult(
            overallScore = overallScore,
            overallGrade = overallGrade,
            breakdown = ScoreBreakdown(qualityScore, nutritionScore, additivesScore, sugarScore, sodiumScore),
            profileScore = profileScore,
            matchedWatchlist = matchedWatchlist,
            matchedAdditives = foundAdditives,
            keyFindings = keyFindings,
            conflicts = conflicts
        )
    }
}`
    },
    "GeminiClient.kt": {
      path: "app/src/main/java/com/example/purecheck/utils/GeminiClient.kt",
      role: "Multi-LLM Orchestrator & Strict Prompt Constraint Engine",
      summary: "Orchestrates API calls to Gemini 2.0 Flash, Groq, DeepSeek, and Bytez. Injects pre-calculated RulesEngine scores and enforces plain-English non-jargon responses.",
      code: `package com.example.purecheck.utils

import com.example.purecheck.data.RulesEngine
import com.example.purecheck.data.UserProfile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

object GeminiClient {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val JSON = "application/json; charset=utf-8".toMediaType()

    fun analyzeIngredients(
        base64Image: String,
        extractedText: String,
        profile: UserProfile,
        provider: String,
        apiKey: String,
        callback: (Result<String>) -> Unit
    ) {
        if (apiKey.isBlank()) {
            callback(Result.failure(Exception("API Key is missing. Please set it in Settings.")))
            return
        }

        // Run local deterministic rules engine first
        val ruleResult = RulesEngine.calculate(
            rawIngredientsText = extractedText,
            watchlist = emptyList(),
            profile = profile
        )

        // Enforce calculated values in system prompt
        val systemInstruction = """
            You are an expert food safety inspector. Analyze the ingredients list image provided and return a comprehensive health audit.
            
            YOU MUST USE THESE EXACT CALCULATED VALUES FOR YOUR REPORT:
            overallScore: \${ruleResult.overallScore}
            overallGrade: "\${ruleResult.overallGrade}"
            scoreBreakdown: {
              "ingredientQuality": \${ruleResult.breakdown.ingredientQuality},
              "nutrition": \${ruleResult.breakdown.nutrition},
              "additives": \${ruleResult.breakdown.additives},
              "sugar": \${ruleResult.breakdown.sugar},
              "sodium": \${ruleResult.breakdown.sodium}
            }
            Do not recalculate or invent these scores. Your summary and analysis MUST explain these specific numbers.

            CRITICAL LANGUAGE REQUIREMENT:
            - Write in VERY SIMPLE English that an ordinary consumer can easily understand.
            - Avoid complex technical or medical jargon.
        """.trimIndent()

        // Calls generativelanguage.googleapis.com or api.groq.com with OkHttp...
    }
}`
    },
    "ScannerScreen.kt": {
      path: "app/src/main/java/com/example/purecheck/ui/scanner/ScannerScreen.kt",
      role: "CameraX Lifecycle Binding, Animated Viewfinder & Edge OCR Pipeline",
      summary: "Draws animated green laser line in Compose Canvas, captures high-res photo, scales down to 768px, and triggers synchronous ML Kit Latin Text Recognition.",
      code: `package com.example.purecheck.ui.scanner

import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

@Composable
fun ScannerScreen(...) {
    // CameraX setup with PreviewView and ImageCapture
    // Canvas animated laser line via rememberInfiniteTransition()
    // captureImage() -> resizeBitmap(768) -> Tasks.await(recognizer.process(image))
}`
    },
    "BarcodeScannerScreen.kt": {
      path: "app/src/main/java/com/example/purecheck/ui/scanner/BarcodeScannerScreen.kt",
      role: "Real-Time 30 FPS Barcode Scanning & Open Food Facts v2 Client",
      summary: "Analyzes CameraX frames at 30 FPS via ML Kit BarcodeScanning, extracts raw EAN digits, and queries world.openfoodfacts.org via OkHttp.",
      code: `package com.example.purecheck.ui.scanner

import com.google.mlkit.vision.barcode.BarcodeScanning
import okhttp3.OkHttpClient
import okhttp3.Request

// ImageAnalysis with STRATEGY_KEEP_ONLY_LATEST
// Queries https://world.openfoodfacts.org/api/v2/product/{barcode}.json
// Calls RulesEngine.calculate() directly on nutriments and ingredients_text!`
    },
    "PureCheckPreferences.kt": {
      path: "app/src/main/java/com/example/purecheck/data/PureCheckPreferences.kt",
      role: "SharedPreferences + Gson Local Persistence Layer",
      summary: "Manages private XML storage for UserProfile, 50-item FIFO scan history, saved favorites, shopping list items, and API keys.",
      code: `package com.example.purecheck.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PureCheckPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("purecheck_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    // getHistory(), addHistoryItem() with FIFO max 50 bound
    // getUserProfile(), saveUserProfile()
    // getFavorites(), addFavorite()
}`
    },
    "IngredientDatabase.kt": {
      path: "app/src/main/java/com/example/purecheck/data/IngredientDatabase.kt",
      role: "In-Memory Verified Dictionary of 17+ Hazardous Additives",
      summary: "Defines IngredientDef with E-numbers, aliases, functions, potential health concerns, evidence levels (High/Medium/Low), and dietary risk flags.",
      code: `package com.example.purecheck.data

data class IngredientDef(
    val name: String,
    val aliases: List<String> = emptyList(),
    val eNumber: String? = null,
    val category: String,
    val function: String,
    val description: String,
    val potentialConcerns: List<String> = emptyList(),
    val evidenceLevel: String = "None",
    val dietaryFlags: List<String> = emptyList()
)

object IngredientDatabase {
    val dictionary: List<IngredientDef> = listOf(
        // Monosodium Glutamate E621, Carrageenan E407, Palm Oil,
        // Aspartame E951, High Fructose Corn Syrup, Tartrazine E102,
        // Sodium Benzoate E211, BHA E320, BHT E321, Titanium Dioxide E171...
    )
}`
    },
    "Navigation.kt": {
      path: "app/src/main/java/com/example/purecheck/Navigation.kt",
      role: "Jetpack Navigation 3 (Nav3) Backstack & Route Dispatcher",
      summary: "Type-safe navigation stack using NavKey serializable objects, rememberNavBackStack(Main), and NavDisplay.",
      code: `package com.example.purecheck

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.*
import androidx.navigation3.ui.NavDisplay

@Composable
fun MainNavigation() {
    val backStack = rememberNavBackStack(Main)
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Main> { MainAppContainer(...) }
            entry<Scanner> { ScannerScreen(...) }
            entry<BarcodeScanner> { BarcodeScannerScreen(...) }
            entry<Result> { key -> ResultScreen(jsonResult = key.jsonResult, ...) }
            entry<Settings> { SettingsScreen(...) }
            entry<MyProfile> { MyProfileScreen(...) }
            entry<Favorites> { FavoritesScreen(...) }
            entry<Author> { AuthorScreen(...) }
        }
    )
}`
    },
    "app/build.gradle.kts": {
      path: "app/build.gradle.kts",
      role: "Gradle 8.x Android Application Build Configuration",
      summary: "Configures compileSdk 36, minSdk 24, Compose BOM, CameraX, ML Kit OCR, OkHttp, and Gson dependencies.",
      code: `plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.purecheck"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.example.purecheck"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures { compose = true }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.okhttp)
    implementation(libs.gson)
    implementation("com.google.android.gms:play-services-mlkit-text-recognition:19.0.0")
    implementation("com.google.mlkit:barcode-scanning:17.3.0")
}`
    }
  };

  // ==========================================================================
  // 1. INITIALIZATION & ROUTING
  // ==========================================================================
  function init() {
    renderSidebar();
    updateProgress();
    setupEventListeners();
    setupSimulator();
    renderCurrentModule();
  }

  function renderSidebar() {
    let html = `<div class="nav-group-label">Core Learning Modules</div>`;
    NutriCheckData.modules.forEach(mod => {
      const isCompleted = AppState.completedModules.includes(mod.id);
      const isActive = AppState.currentModuleId === mod.id;
      html += `
        <div class="nav-item ${isActive ? 'active' : ''}" data-mod-id="${mod.id}">
          <div class="nav-item-left">
            <span class="nav-num">${mod.number}</span>
            <span>${mod.title}</span>
          </div>
          <div class="nav-check ${isCompleted ? 'completed' : ''}" data-check-id="${mod.id}" title="Toggle Completion"></div>
        </div>
      `;
    });
    DOM.sidebarNav.innerHTML = html;

    // Attach click listeners
    DOM.sidebarNav.querySelectorAll(".nav-item").forEach(el => {
      el.addEventListener("click", function(e) {
        if (e.target.classList.contains("nav-check")) return;
        navigateToModule(this.getAttribute("data-mod-id"));
        if (window.innerWidth <= 820) toggleMobileSidebar(false);
      });
    });

    DOM.sidebarNav.querySelectorAll(".nav-check").forEach(el => {
      el.addEventListener("click", function(e) {
        e.stopPropagation();
        const modId = this.getAttribute("data-check-id");
        toggleModuleCompletion(modId);
      });
    });
  }

  function updateProgress() {
    const total = NutriCheckData.modules.length;
    const completed = AppState.completedModules.length;
    const pct = Math.round((completed / total) * 100);
    DOM.progressBarFill.style.width = `${pct}%`;
    DOM.progressText.textContent = `${completed} of ${total} Completed (${pct}%)`;
    localStorage.setItem("nutricheck_completed", JSON.stringify(AppState.completedModules));
  }

  function toggleModuleCompletion(modId) {
    if (AppState.completedModules.includes(modId)) {
      AppState.completedModules = AppState.completedModules.filter(id => id !== modId);
    } else {
      AppState.completedModules.push(modId);
    }
    renderSidebar();
    updateProgress();
  }

  function navigateToModule(modId) {
    AppState.currentModuleId = modId;
    window.scrollTo({ top: 0, behavior: "smooth" });
    renderSidebar();
    renderCurrentModule();
  }

  // ==========================================================================
  // 2. MODULE CONTENT RENDERER
  // ==========================================================================
  function renderCurrentModule() {
    const mod = NutriCheckData.modules.find(m => m.id === AppState.currentModuleId);
    if (!mod) return;

    DOM.breadcrumbCurrent.textContent = `Module ${mod.number}: ${mod.title}`;

    let html = `
      <div class="module-hero">
        <div class="module-meta-row">
          <span class="module-badge">${mod.badge}</span>
          <span class="module-num-label">MODULE ${mod.number} OF 17</span>
        </div>
        <h1 class="module-title">${mod.title}</h1>
        <p class="module-desc">${mod.shortDesc}</p>
      </div>
    `;

    // Render interactive widgets based on module type
    if (mod.id === "module-4" || mod.id === "module-5" || mod.id === "module-10") {
      html += `
        <div class="card">
          <div class="card-header">
            <div>
              <h3 class="card-title">Interactive System Architecture Visualization</h3>
              <p style="font-size:0.85rem; color:var(--text-muted); margin-top:4px;">
                💡 <strong>Click any node</strong> (CameraX, ML Kit, RulesEngine, GeminiClient, Open Food Facts) to inspect its exact inputs, outputs, and source code files.
              </p>
            </div>
            <span class="simulator-badge">Clickable Nodes</span>
          </div>
          <div class="diagram-wrap">
            ${NutriCheckDiagrams.renderSystemArchitectureSVG()}
          </div>
        </div>
      `;
    }

    if (mod.id === "module-6" || mod.id === "module-11") {
      html += `
        <div class="card">
          <div class="card-header">
            <div>
              <h3 class="card-title">Dual-Stage Food Scanning Pipeline</h3>
              <p style="font-size:0.85rem; color:var(--text-muted); margin-top:4px;">
                Shows how physical photons from CameraX transform into immutable mathematical grades and cloud LLM summaries.
              </p>
            </div>
            <span class="simulator-badge">Visual Stepper</span>
          </div>
          <div class="diagram-wrap">
            ${NutriCheckDiagrams.renderPipelineSVG()}
          </div>
        </div>
      `;
    }

    // Always include Live Rules Engine Simulator on Module 6 (Workflows) and Module 3 (Features)
    if (mod.id === "module-6" || mod.id === "module-3") {
      html += renderSimulatorHTML();
    }

    // Module 9: Source Code Explorer
    if (mod.id === "module-9") {
      html += renderCodeExplorerHTML();
    }

    // Module 8: Database Schemas
    if (mod.id === "module-8") {
      html += renderDatabaseSchemasHTML();
    }

    // Module 7: API Explorer
    if (mod.id === "module-7") {
      html += renderApiExplorerHTML();
    }

    // Module 15: Presentation Slides Deck
    if (mod.id === "module-15") {
      html += renderPresentationDeckHTML();
    }

    // Module 16: Technical Viva Examination Center
    if (mod.id === "module-16") {
      html += renderVivaCenterHTML();
    }

    // Module 17: Glossary
    if (mod.id === "module-17") {
      html += renderGlossaryHTML();
    }

    // Standard Module Sections with Dual-Explanation Mode
    mod.sections.forEach(sec => {
      const isBeginner = AppState.explanationMode === "beginner";
      html += `
        <div class="section-block">
          <h3>${sec.heading}</h3>
          <div class="explanation-box">
            <div class="explanation-header">
              <div class="explanation-tag ${isBeginner ? 'tag-beginner' : 'tag-panel'}">
                <span>${isBeginner ? '💡 BEGINNER-FRIENDLY EXPLANATION' : '🛡️ TECHNICAL PANEL DEFENSE MODE'}</span>
              </div>
              <span style="font-size:0.75rem; color:var(--text-muted);">
                ${isBeginner ? 'Simplified practical analogies' : 'Formal architecture, code symbols & proofs'}
              </span>
            </div>
            <div class="explanation-body">
              ${isBeginner ? `<p>${sec.beginner.replace(/\n/g, '</p><p>')}</p>` : `<p>${sec.panel.replace(/\n/g, '</p><p>')}</p>`}
            </div>
          </div>
        </div>
      `;
    });

    // Prev / Next Navigation Footer
    const currentIndex = NutriCheckData.modules.findIndex(m => m.id === mod.id);
    const prevMod = currentIndex > 0 ? NutriCheckData.modules[currentIndex - 1] : null;
    const nextMod = currentIndex < NutriCheckData.modules.length - 1 ? NutriCheckData.modules[currentIndex + 1] : null;

    html += `
      <div class="module-nav-footer">
        ${prevMod ? `
          <button class="nav-btn" onclick="NutriCheckApp.navigateTo('${prevMod.id}')">
            <span>←</span>
            <div>
              <span class="nav-btn-meta">PREVIOUS MODULE</span>
              <span>${prevMod.number}. ${prevMod.title}</span>
            </div>
          </button>
        ` : `<div></div>`}
        
        ${nextMod ? `
          <button class="nav-btn" onclick="NutriCheckApp.navigateTo('${nextMod.id}')">
            <div style="text-align:right;">
              <span class="nav-btn-meta">NEXT MODULE</span>
              <span>${nextMod.number}. ${nextMod.title}</span>
            </div>
            <span>→</span>
          </button>
        ` : `<div></div>`}
      </div>
    `;

    DOM.contentArea.innerHTML = html;

    // Attach interactive node click listeners for diagrams
    document.querySelectorAll(".diagram-node").forEach(nodeEl => {
      nodeEl.addEventListener("click", function() {
        const nodeId = this.getAttribute("data-node");
        showNodeInspector(nodeId);
      });
    });

    // Re-bind simulator inputs if rendered
    if (document.getElementById("sim-calc-btn")) {
      setupSimulatorEventListeners();
      runSimulation();
    }

    // Re-bind code explorer if rendered
    if (document.getElementById("code-tree-pane")) {
      setupCodeExplorerEventListeners();
    }

    // Re-bind viva filters if rendered
    if (document.getElementById("viva-filters-container")) {
      setupVivaEventListeners();
    }
  }

  // ==========================================================================
  // 3. LIVE RULES ENGINE SIMULATOR HTML & LOGIC
  // ==========================================================================
  function renderSimulatorHTML() {
    return `
      <div class="simulator-container">
        <div class="simulator-header">
          <div>
            <h2 style="font-size:1.45rem; font-weight:800; color:#FFFFFF;">Live Deterministic Rules Engine Simulator</h2>
            <p style="font-size:0.85rem; color:var(--text-secondary); margin-top:4px;">
              Direct JavaScript port of <code>RulesEngine.kt</code> and <code>IngredientDatabase.kt</code>. Test real product ingredients and watch math compute live!
            </p>
          </div>
          <span class="simulator-badge">Zero Hallucinations • Live Math</span>
        </div>

        <div class="simulator-grid">
          <!-- Inputs Column -->
          <div class="sim-inputs-col">
            <div class="sim-input-group">
              <label class="sim-label">
                <span>Ingredient List Text (From OCR or Database)</span>
              </label>
              <textarea id="sim-text" class="sim-textarea" placeholder="e.g. Sugar, Palm Oil, Cocoa, Skimmed Milk Powder, E102, E621, Salt...">Water, Sugar, Refined Vegetable Oil, High Fructose Corn Syrup, E621, Monosodium Glutamate, Salt, Carrageenan, E102</textarea>
            </div>

            <div class="sim-slider-row">
              <div class="sim-input-group">
                <label class="sim-label">
                  <span>Sugar Content: <strong id="sim-sugar-val" class="unit">18g</strong></span>
                </label>
                <input type="range" id="sim-sugar" class="sim-slider" min="0" max="80" value="18" />
              </div>

              <div class="sim-input-group">
                <label class="sim-label">
                  <span>Sodium Content: <strong id="sim-sodium-val" class="unit">450mg</strong></span>
                </label>
                <input type="range" id="sim-sodium" class="sim-slider" min="0" max="1500" step="10" value="450" />
              </div>
            </div>

            <div class="sim-slider-row">
              <div class="sim-input-group">
                <label class="sim-label">
                  <span>Protein Content: <strong id="sim-protein-val" class="unit">4g</strong></span>
                </label>
                <input type="range" id="sim-protein" class="sim-slider" min="0" max="40" value="4" />
              </div>

              <div class="sim-input-group">
                <label class="sim-label">
                  <span>Calories: <strong id="sim-cal-val" class="unit">380 kcal</strong></span>
                </label>
                <input type="range" id="sim-cal" class="sim-slider" min="0" max="800" step="10" value="380" />
              </div>
            </div>

            <!-- Profile Toggles -->
            <label class="sim-label" style="margin-top:10px;">
              <span>User Biometric Dietary Preferences:</span>
            </label>
            <div class="sim-toggles">
              <div class="sim-chip-toggle" data-profile="vegan">🌱 Vegan (-30 conflict)</div>
              <div class="sim-chip-toggle active" data-profile="diabetic">🩸 Diabetic-Friendly (-20 conflict)</div>
              <div class="sim-chip-toggle active" data-profile="heartHealth">❤️ Heart Health (-20 conflict)</div>
              <div class="sim-chip-toggle" data-profile="gutHealth">🦠 Gut Health (-15 conflict)</div>
              <div class="sim-chip-toggle" data-profile="lowSodium">🧂 Low Sodium (-15 conflict)</div>
            </div>

            <button id="sim-calc-btn" class="nav-btn" style="margin-top:20px; width:100%; justify-content:center; background:linear-gradient(135deg, #10B981, #059669); color:#000; font-weight:800;">
              ⚡ Run RulesEngine.calculate()
            </button>
          </div>

          <!-- Outputs Column -->
          <div class="sim-output-card">
            <div class="sim-score-banner">
              <div>
                <div style="font-size:0.75rem; font-weight:800; color:var(--text-muted); text-transform:uppercase;">Overall Result</div>
                <div class="sim-overall-score" id="sim-score-display">-- <span>/ 100</span></div>
                <div style="font-size:0.8rem; font-weight:700; color:var(--accent-emerald);" id="sim-profile-score-display">Profile Score: --</div>
              </div>
              <div class="sim-grade-circle grade-F" id="sim-grade-display">-</div>
            </div>

            <!-- 5 Dimension Bars -->
            <div class="sim-dim-row">
              <div class="sim-dim-header">
                <span>Ingredient Quality</span>
                <span id="sim-dim-qual">-- / 100</span>
              </div>
              <div class="sim-dim-bar"><div class="sim-dim-fill" id="sim-bar-qual" style="width:0%; background:#10B981;"></div></div>
            </div>

            <div class="sim-dim-row">
              <div class="sim-dim-header">
                <span>Chemical Additives</span>
                <span id="sim-dim-add">-- / 100</span>
              </div>
              <div class="sim-dim-bar"><div class="sim-dim-fill" id="sim-bar-add" style="width:0%; background:#8B5CF6;"></div></div>
            </div>

            <div class="sim-dim-row">
              <div class="sim-dim-header">
                <span>Sugar Score</span>
                <span id="sim-dim-sugar">-- / 100</span>
              </div>
              <div class="sim-dim-bar"><div class="sim-dim-fill" id="sim-bar-sugar" style="width:0%; background:#F59E0B;"></div></div>
            </div>

            <div class="sim-dim-row">
              <div class="sim-dim-header">
                <span>Sodium Score</span>
                <span id="sim-dim-sod">-- / 100</span>
              </div>
              <div class="sim-dim-bar"><div class="sim-dim-fill" id="sim-bar-sod" style="width:0%; background:#EF4444;"></div></div>
            </div>

            <div class="sim-dim-row">
              <div class="sim-dim-header">
                <span>Blended Nutrition</span>
                <span id="sim-dim-nut">-- / 100</span>
              </div>
              <div class="sim-dim-bar"><div class="sim-dim-fill" id="sim-bar-nut" style="width:0%; background:#3B82F6;"></div></div>
            </div>

            <!-- Profile Conflict Badges -->
            <div class="sim-conflicts-list" id="sim-conflicts-container"></div>

            <!-- Execution Logs -->
            <div class="sim-log-accordion" id="sim-log-container">
              <div class="sim-log-step">Awaiting input execution...</div>
            </div>
          </div>
        </div>
      </div>
    `;
  }

  function setupSimulator() {
    // Simulator runs via NutriCheckRulesEngine
  }

  function setupSimulatorEventListeners() {
    const textEl = document.getElementById("sim-text");
    const sugarEl = document.getElementById("sim-sugar");
    const sodiumEl = document.getElementById("sim-sodium");
    const proteinEl = document.getElementById("sim-protein");
    const calEl = document.getElementById("sim-cal");
    const calcBtn = document.getElementById("sim-calc-btn");

    sugarEl.addEventListener("input", function() {
      document.getElementById("sim-sugar-val").textContent = `${this.value}g`;
      runSimulation();
    });

    sodiumEl.addEventListener("input", function() {
      document.getElementById("sim-sodium-val").textContent = `${this.value}mg`;
      runSimulation();
    });

    proteinEl.addEventListener("input", function() {
      document.getElementById("sim-protein-val").textContent = `${this.value}g`;
      runSimulation();
    });

    calEl.addEventListener("input", function() {
      document.getElementById("sim-cal-val").textContent = `${this.value} kcal`;
      runSimulation();
    });

    calcBtn.addEventListener("click", runSimulation);

    document.querySelectorAll(".sim-chip-toggle").forEach(el => {
      el.addEventListener("click", function() {
        this.classList.toggle("active");
        runSimulation();
      });
    });
  }

  function runSimulation() {
    const text = document.getElementById("sim-text") ? document.getElementById("sim-text").value : "";
    const sugar = document.getElementById("sim-sugar") ? parseFloat(document.getElementById("sim-sugar").value) : 18;
    const sodium = document.getElementById("sim-sodium") ? parseFloat(document.getElementById("sim-sodium").value) : 450;
    const protein = document.getElementById("sim-protein") ? parseFloat(document.getElementById("sim-protein").value) : 4;
    const cal = document.getElementById("sim-cal") ? parseFloat(document.getElementById("sim-cal").value) : 380;

    const profile = {};
    document.querySelectorAll(".sim-chip-toggle").forEach(el => {
      const key = el.getAttribute("data-profile");
      profile[key] = el.classList.contains("active");
    });

    if (!window.NutriCheckRulesEngine) return;

    const res = window.NutriCheckRulesEngine.calculate({
      rawIngredientsText: text,
      calories: cal,
      sugarGrams: sugar,
      proteinGrams: protein,
      sodiumMg: sodium,
      saturatedFatGrams: 8,
      fiberGrams: 1,
      watchlist: ["MSG", "Palm Oil"],
      profile: profile
    });

    // Update UI elements
    document.getElementById("sim-score-display").innerHTML = `${res.overallScore} <span>/ 100</span>`;
    document.getElementById("sim-profile-score-display").textContent = `Personalized Profile Score: ${res.profileScore} / 100`;

    const gradeEl = document.getElementById("sim-grade-display");
    gradeEl.textContent = res.overallGrade;
    gradeEl.className = `sim-grade-circle grade-${res.overallGrade}`;

    // Update Dimension Bars
    document.getElementById("sim-dim-qual").textContent = `${res.breakdown.ingredientQuality} / 100`;
    document.getElementById("sim-bar-qual").style.width = `${res.breakdown.ingredientQuality}%`;

    document.getElementById("sim-dim-add").textContent = `${res.breakdown.additives} / 100`;
    document.getElementById("sim-bar-add").style.width = `${res.breakdown.additives}%`;

    document.getElementById("sim-dim-sugar").textContent = `${res.breakdown.sugar} / 100`;
    document.getElementById("sim-bar-sugar").style.width = `${res.breakdown.sugar}%`;

    document.getElementById("sim-dim-sod").textContent = `${res.breakdown.sodium} / 100`;
    document.getElementById("sim-bar-sod").style.width = `${res.breakdown.sodium}%`;

    document.getElementById("sim-dim-nut").textContent = `${res.breakdown.nutrition} / 100`;
    document.getElementById("sim-bar-nut").style.width = `${res.breakdown.nutrition}%`;

    // Render Conflict Badges
    const conflictsContainer = document.getElementById("sim-conflicts-container");
    if (res.conflicts.length === 0) {
      conflictsContainer.innerHTML = `<div class="sim-conflict-badge" style="background:rgba(16,185,129,0.12); color:#34D399; border:1px solid rgba(16,185,129,0.3);">✓ No profile conflicts detected for active preferences.</div>`;
    } else {
      let bHtml = "";
      res.conflicts.forEach(c => {
        const isRed = c.startsWith("✕");
        bHtml += `<div class="sim-conflict-badge ${isRed ? 'conflict-red' : 'conflict-yellow'}">${c}</div>`;
      });
      conflictsContainer.innerHTML = bHtml;
    }

    // Render Logs
    const logContainer = document.getElementById("sim-log-container");
    let logHtml = "";
    res.logSteps.forEach(l => {
      logHtml += `
        <div class="sim-log-step">
          <span class="sim-log-tag">[${l.step}]</span> ${l.text}
        </div>
      `;
    });
    logContainer.innerHTML = logHtml;
  }

  // ==========================================================================
  // 4. SOURCE CODE EXPLORER HTML & LOGIC
  // ==========================================================================
  function renderCodeExplorerHTML() {
    const activeFile = SourceFilesMap[AppState.activeSourceFile] || SourceFilesMap["RulesEngine.kt"];

    return `
      <div class="card">
        <div class="card-header">
          <div>
            <h3 class="card-title">Authentic Repository Source Code Explorer</h3>
            <p style="font-size:0.85rem; color:var(--text-muted); margin-top:4px;">
              Inspect real Kotlin production files from <code>Major_Project_Part1</code> with full annotations and call graphs.
            </p>
          </div>
          <span class="simulator-badge">Direct Source Inspection</span>
        </div>

        <div class="code-explorer-container">
          <!-- File Tree -->
          <div class="file-tree-pane" id="code-tree-pane">
            <div class="file-tree-header">Repository Files</div>
            
            <div class="tree-folder">
              <div class="tree-folder-title">📁 com.example.purecheck.data</div>
              <div class="tree-folder-items">
                <div class="tree-file-item ${AppState.activeSourceFile === 'RulesEngine.kt' ? 'active' : ''}" data-file="RulesEngine.kt">📄 RulesEngine.kt</div>
                <div class="tree-file-item ${AppState.activeSourceFile === 'IngredientDatabase.kt' ? 'active' : ''}" data-file="IngredientDatabase.kt">📄 IngredientDatabase.kt</div>
                <div class="tree-file-item ${AppState.activeSourceFile === 'PureCheckPreferences.kt' ? 'active' : ''}" data-file="PureCheckPreferences.kt">📄 PureCheckPreferences.kt</div>
              </div>
            </div>

            <div class="tree-folder">
              <div class="tree-folder-title">📁 com.example.purecheck.utils</div>
              <div class="tree-folder-items">
                <div class="tree-file-item ${AppState.activeSourceFile === 'GeminiClient.kt' ? 'active' : ''}" data-file="GeminiClient.kt">📄 GeminiClient.kt</div>
              </div>
            </div>

            <div class="tree-folder">
              <div class="tree-folder-title">📁 com.example.purecheck.ui.scanner</div>
              <div class="tree-folder-items">
                <div class="tree-file-item ${AppState.activeSourceFile === 'ScannerScreen.kt' ? 'active' : ''}" data-file="ScannerScreen.kt">📄 ScannerScreen.kt</div>
                <div class="tree-file-item ${AppState.activeSourceFile === 'BarcodeScannerScreen.kt' ? 'active' : ''}" data-file="BarcodeScannerScreen.kt">📄 BarcodeScannerScreen.kt</div>
              </div>
            </div>

            <div class="tree-folder">
              <div class="tree-folder-title">📁 com.example.purecheck (Core)</div>
              <div class="tree-folder-items">
                <div class="tree-file-item ${AppState.activeSourceFile === 'Navigation.kt' ? 'active' : ''}" data-file="Navigation.kt">📄 Navigation.kt</div>
                <div class="tree-file-item ${AppState.activeSourceFile === 'app/build.gradle.kts' ? 'active' : ''}" data-file="app/build.gradle.kts">📄 build.gradle.kts</div>
              </div>
            </div>
          </div>

          <!-- Code Viewer -->
          <div class="code-viewer-pane">
            <div class="code-viewer-topbar">
              <div class="active-file-meta">
                <span class="active-file-name" id="code-file-path">${activeFile.path}</span>
                <span class="active-file-badge">${activeFile.role}</span>
              </div>
              <button class="code-copy-btn" id="code-copy-btn">Copy Code</button>
            </div>
            <div class="code-annotation-banner" id="code-annotation">
              <strong>Architectural Responsibility:</strong> ${activeFile.summary}
            </div>
            <div class="code-content-wrap" id="code-content-wrap">${escapeHTML(activeFile.code)}</div>
          </div>
        </div>
      </div>
    `;
  }

  function setupCodeExplorerEventListeners() {
    document.querySelectorAll(".tree-file-item").forEach(el => {
      el.addEventListener("click", function() {
        const fileKey = this.getAttribute("data-file");
        AppState.activeSourceFile = fileKey;
        renderCurrentModule();
      });
    });

    const copyBtn = document.getElementById("code-copy-btn");
    if (copyBtn) {
      copyBtn.addEventListener("click", function() {
        const fileObj = SourceFilesMap[AppState.activeSourceFile];
        if (fileObj) {
          navigator.clipboard.writeText(fileObj.code);
          this.textContent = "Copied!";
          setTimeout(() => { this.textContent = "Copy Code"; }, 2000);
        }
      });
    }
  }

  // ==========================================================================
  // 5. DATABASE SCHEMAS HTML
  // ==========================================================================
  function renderDatabaseSchemasHTML() {
    return `
      <div class="card">
        <h3 class="card-title">SharedPreferences Local Storage Schemas & Models</h3>
        <p style="font-size:0.85rem; color:var(--text-secondary); margin-top:4px;">
          NutriCheck persists structured records via private SharedPreferences and Gson JSON serialization.
        </p>

        <div class="schema-card-grid">
          <!-- UserProfile -->
          <div class="schema-box">
            <div class="schema-box-header">
              <span class="schema-entity-name">UserProfile</span>
              <span class="schema-key-badge">Single Key: profile_*</span>
            </div>
            <table class="schema-fields-table">
              <tr><th>Field</th><th>Type</th><th>Purpose</th></tr>
              <tr><td class="field-name">name</td><td class="field-type">String</td><td>User display name</td></tr>
              <tr><td class="field-name">age, gender</td><td class="field-type">Int, String</td><td>Demographics</td></tr>
              <tr><td class="field-name">height, weight</td><td class="field-type">Float, Float</td><td>BMI calculation</td></tr>
              <tr><td class="field-name">vegan, vegetarian</td><td class="field-type">Boolean</td><td>Ethical diet flags</td></tr>
              <tr><td class="field-name">diabetic, heartHealth</td><td class="field-type">Boolean</td><td>Medical conflict flags</td></tr>
              <tr><td class="field-name">lowSugar, lowSodium</td><td class="field-type">Boolean</td><td>Nutritional alerts</td></tr>
            </table>
          </div>

          <!-- HistoryItem -->
          <div class="schema-box">
            <div class="schema-box-header">
              <span class="schema-entity-name">HistoryItem (List)</span>
              <span class="schema-key-badge">Key: scan_history</span>
            </div>
            <table class="schema-fields-table">
              <tr><th>Field</th><th>Type</th><th>Purpose</th></tr>
              <tr><td class="field-name">id</td><td class="field-type">UUID String</td><td>Unique scan record ID</td></tr>
              <tr><td class="field-name">productName</td><td class="field-type">String</td><td>Brand and product label</td></tr>
              <tr><td class="field-name">overallGrade</td><td class="field-type">String (A-F)</td><td>Letter grade</td></tr>
              <tr><td class="field-name">overallScore</td><td class="field-type">Int (10-100)</td><td>Deterministic score</td></tr>
              <tr><td class="field-name">jsonResult</td><td class="field-type">JSON String</td><td>Full immutable report</td></tr>
              <tr><td class="field-name">timestamp</td><td class="field-type">Long (Epoch)</td><td>Date/Time scanned</td></tr>
            </table>
          </div>

          <!-- FavoriteProduct -->
          <div class="schema-box">
            <div class="schema-box-header">
              <span class="schema-entity-name">FavoriteProduct</span>
              <span class="schema-key-badge">Key: favorite_products</span>
            </div>
            <table class="schema-fields-table">
              <tr><th>Field</th><th>Type</th><th>Purpose</th></tr>
              <tr><td class="field-name">id</td><td class="field-type">UUID String</td><td>Unique bookmark ID</td></tr>
              <tr><td class="field-name">productName</td><td class="field-type">String</td><td>Bookmarked title</td></tr>
              <tr><td class="field-name">overallGrade</td><td class="field-type">String</td><td>Grade for quick filter</td></tr>
              <tr><td class="field-name">calories, protein</td><td class="field-type">Int, Int</td><td>Macro chip filters</td></tr>
              <tr><td class="field-name">jsonResult</td><td class="field-type">JSON String</td><td>Serialized report data</td></tr>
            </table>
          </div>

          <!-- ShoppingItem -->
          <div class="schema-box">
            <div class="schema-box-header">
              <span class="schema-entity-name">ShoppingItem</span>
              <span class="schema-key-badge">Key: shopping_list</span>
            </div>
            <table class="schema-fields-table">
              <tr><th>Field</th><th>Type</th><th>Purpose</th></tr>
              <tr><td class="field-name">id</td><td class="field-type">UUID String</td><td>Item ID</td></tr>
              <tr><td class="field-name">name</td><td class="field-type">String</td><td>Shopping item title</td></tr>
              <tr><td class="field-name">checked</td><td class="field-type">Boolean</td><td>Purchased checkbox</td></tr>
              <tr><td class="field-name">score, grade</td><td class="field-type">Int?, String?</td><td>Linked scan verification</td></tr>
            </table>
          </div>
        </div>
      </div>
    `;
  }

  // ==========================================================================
  // 6. API EXPLORER HTML
  // ==========================================================================
  function renderApiExplorerHTML() {
    return `
      <div class="card">
        <h3 class="card-title">Production External API Contracts</h3>
        <p style="font-size:0.85rem; color:var(--text-secondary); margin-top:4px;">
          Exhaustive specifications of real network endpoints invoked by NutriCheck.
        </p>

        <!-- API 1: Open Food Facts -->
        <div class="api-spec-card">
          <div class="api-method-row">
            <span class="method-badge method-get">GET</span>
            <span class="api-url">https://world.openfoodfacts.org/api/v2/product/{barcode}.json</span>
          </div>
          <div style="font-size:0.85rem; color:var(--text-secondary); margin-bottom:8px;">
            <strong>Mandatory Header:</strong> <code>User-Agent: PureCheck - Android - Version 2.0 - CSE Major Project</code>
          </div>
          <div style="font-size:0.8rem; color:var(--text-muted);">Sample Response JSON:</div>
          <pre class="api-payload-box">{
  "status": 1,
  "code": "8901030383701",
  "product": {
    "product_name": "Dark Chocolate 70%",
    "brands": "Amul",
    "ingredients_text": "Cocoa Solids, Sugar, Cocoa Butter, Emulsifier (E322, E476)",
    "nutriments": {
      "energy-kcal_100g": 540,
      "sugars_100g": 28.5,
      "proteins_100g": 8.5,
      "sodium_100g": 0.04,
      "saturated-fat_100g": 18.0
    }
  }
}</pre>
        </div>

        <!-- API 2: Gemini 2.0 Flash -->
        <div class="api-spec-card">
          <div class="api-method-row">
            <span class="method-badge method-post">POST</span>
            <span class="api-url">https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key={API_KEY}</span>
          </div>
          <div style="font-size:0.85rem; color:var(--text-secondary); margin-bottom:8px;">
            <strong>Configuration:</strong> <code>generationConfig: { "responseMimeType": "application/json" }</code>
          </div>
          <div style="font-size:0.8rem; color:var(--text-muted);">Enforced Response Schema:</div>
          <pre class="api-payload-box">{
  "productName": "String",
  "overallGrade": "String (A-F from local RulesEngine)",
  "overallScore": 80,
  "scoreBreakdown": {
    "ingredientQuality": 85,
    "nutrition": 80,
    "additives": 92,
    "sugar": 75,
    "sodium": 100
  },
  "summary": "Plain English summary without medical jargon...",
  "obfuscatedIngredients": [
    { "printed": "Refined Vegetable Oil", "actual": "Palm Oil", "reason": "High saturated fat" }
  ],
  "cleanSwaps": [
    { "brand": "CleanBrand", "product": "Pure Spelt Bread", "reason": "No preservatives" }
  ]
}</pre>
        </div>
      </div>
    `;
  }

  // ==========================================================================
  // 7. PRESENTATION MASTER SLIDES DECK
  // ==========================================================================
  function renderPresentationDeckHTML() {
    return `
      <div class="card">
        <h3 class="card-title">10-Minute Major Project Presentation Slides Guide</h3>
        <p style="font-size:0.85rem; color:var(--text-secondary); margin-top:4px;">
          Aligned directly with <code>NutriCheck_Final.pptx</code> and presentation guidelines.
        </p>

        <div class="slide-grid">
          <!-- Slide 1 -->
          <div class="slide-card">
            <div class="slide-header">
              <span class="slide-num">SLIDE 01</span>
              <span class="slide-timing">0:00 - 0:45</span>
            </div>
            <h4 class="slide-title">Title & Author Credentials</h4>
            <ul class="slide-points">
              <li>NutriCheck: Food Intelligence Platform</li>
              <li>V. V. Karthik Barrenkala (R210111) & M. Uday Bhaskar (R210288)</li>
              <li>Rajiv Gandhi University of Knowledge Technologies (RGUKT)</li>
              <li>Department of Computer Science & Engineering</li>
            </ul>
            <div class="slide-speech-box">
              <strong>Presenter Script:</strong>
              "Respected guide and panel members, today we present NutriCheck—an edge-to-cloud food intelligence platform engineered to eliminate consumer asymmetric information in packaged foods."
            </div>
          </div>

          <!-- Slide 2 -->
          <div class="slide-card">
            <div class="slide-header">
              <span class="slide-num">SLIDE 02</span>
              <span class="slide-timing">0:45 - 2:00</span>
            </div>
            <h4 class="slide-title">Problem Statement & Motivation</h4>
            <ul class="slide-points">
              <li>E-numbers and chemical synonyms hide harmful ingredients</li>
              <li>Obfuscated labels (Refined Vegetable Oil vs Palm Oil)</li>
              <li>Front-of-package claims mislead diabetic and hypertensive buyers</li>
              <li>Existing barcode apps fail on uncatalogued regional goods</li>
            </ul>
            <div class="slide-speech-box">
              <strong>Presenter Script:</strong>
              "Modern packaging intentionally obscures health risks behind chemical numbering and broad umbrella terms, making manual health auditing impossible for ordinary shoppers."
            </div>
          </div>

          <!-- Slide 3 -->
          <div class="slide-card">
            <div class="slide-header">
              <span class="slide-num">SLIDE 03</span>
              <span class="slide-timing">2:00 - 3:30</span>
            </div>
            <h4 class="slide-title">System Architecture & Tech Stack</h4>
            <ul class="slide-points">
              <li>Android Native in Kotlin 1.9 & Jetpack Compose</li>
              <li>Navigation 3 (Nav3) type-safe backstack architecture</li>
              <li>CameraX + Google ML Kit on-device Vision & OCR</li>
              <li>Local RulesEngine.kt + Multi-LLM Cloud Orchestration</li>
            </ul>
            <div class="slide-speech-box">
              <strong>Presenter Script:</strong>
              "Our architecture enforces a strict trust boundary: the score is mathematically computed on-device by RulesEngine.kt, while cloud LLMs are restricted strictly to plain-English explanation."
            </div>
          </div>

          <!-- Slide 4 -->
          <div class="slide-card">
            <div class="slide-header">
              <span class="slide-num">SLIDE 04</span>
              <span class="slide-timing">3:30 - 5:30</span>
            </div>
            <h4 class="slide-title">Deterministic Scoring Math</h4>
            <ul class="slide-points">
              <li>Overall Score = Arithmetic mean of 5 clamped dimensions</li>
              <li>Quality (Base 100, evidence-weighted deductions)</li>
              <li>Additives (Count * 8 penalty + High preservative checks)</li>
              <li>Sugar (Excess > 5g * 4.5) & Sodium (Excess > 140mg * 0.15)</li>
              <li>Blended Nutrition score averaged with Sugar and Sodium</li>
            </ul>
            <div class="slide-speech-box">
              <strong>Presenter Script:</strong>
              "We avoid LLM hallucination by computing every single score mathematically. Here is our exact formula implemented in RulesEngine.kt..."
            </div>
          </div>

          <!-- Slide 5 -->
          <div class="slide-card">
            <div class="slide-header">
              <span class="slide-num">SLIDE 05</span>
              <span class="slide-timing">5:30 - 7:30</span>
            </div>
            <h4 class="slide-title">Live Product Audit Demonstration</h4>
            <ul class="slide-points">
              <li>Demonstrate real-time barcode scan with Open Food Facts</li>
              <li>Demonstrate CameraX label capture with green laser</li>
              <li>Inspect Grade breakdown, unmasked palm oil, and warnings</li>
              <li>Demonstrate AI Inspector Chat in Scientific Mode</li>
            </ul>
            <div class="slide-speech-box">
              <strong>Presenter Script:</strong>
              "Watch as I scan this packaged snack: in 300 milliseconds, the barcode is resolved, the rules engine penalizes the palm oil and sugar, assigning Grade D with a Diabetic conflict."
            </div>
          </div>

          <!-- Slide 6 -->
          <div class="slide-card">
            <div class="slide-header">
              <span class="slide-num">SLIDE 06</span>
              <span class="slide-timing">7:30 - 10:00</span>
            </div>
            <h4 class="slide-title">Limitations & Future Roadmap</h4>
            <ul class="slide-points">
              <li>Catalogue coverage gaps in regional Indian markets</li>
              <li>Linguistic scope currently focused on Latin alphabet</li>
              <li>Part 2: Dedicated side-by-side product comparison screen</li>
              <li>Part 2: Embedded on-device quantized LLM (Gemma 2B)</li>
            </ul>
            <div class="slide-speech-box">
              <strong>Presenter Script:</strong>
              "In conclusion, NutriCheck provides a repeatable, transparent, and personalized food audit platform. We thank the panel and welcome your technical questions."
            </div>
          </div>
        </div>
      </div>
    `;
  }

  // ==========================================================================
  // 8. VIVA EXAMINATION CENTER HTML
  // ==========================================================================
  function renderVivaCenterHTML() {
    const categories = ["All", "Project Fundamentals", "System Architecture", "Frontend & UI", "CameraX & Vision", "ML Kit OCR & Vision", "Scoring Algorithm", "Multi-LLM Orchestration", "APIs & Networking", "Database & Storage", "Android Build System", "Testing & QA", "Limitations & Roadmap"];

    let catPillsHtml = "";
    categories.forEach(cat => {
      catPillsHtml += `
        <button class="viva-filter-btn ${AppState.activeVivaCategory === cat ? 'active' : ''}" data-cat="${cat}">
          ${cat}
        </button>
      `;
    });

    const filteredQuestions = AppState.activeVivaCategory === "All"
      ? NutriCheckData.vivaQuestions
      : NutriCheckData.vivaQuestions.filter(q => q.category === AppState.activeVivaCategory);

    let qCardsHtml = "";
    filteredQuestions.forEach((q, idx) => {
      qCardsHtml += `
        <div class="viva-card" data-viva-id="${q.id}">
          <div class="viva-card-header">
            <div>
              <div class="viva-q-title">${idx + 1}. ${q.question}</div>
              <div class="viva-q-meta">
                <span class="viva-cat-badge">${q.category}</span>
                <span style="font-size:0.75rem; color:var(--text-muted);">Click to reveal answers & trap follow-up</span>
              </div>
            </div>
            <span class="viva-arrow-icon">▼</span>
          </div>

          <div class="viva-card-body">
            <!-- Beginner Answer -->
            <div class="viva-ans-section">
              <div class="viva-ans-label viva-ans-beginner">💡 Beginner-Friendly Intuition</div>
              <p style="font-size:0.92rem; color:var(--text-primary); line-height:1.6;">${q.beginner}</p>
            </div>

            <!-- Panel Answer -->
            <div class="viva-ans-section" style="margin-top:14px;">
              <div class="viva-ans-label viva-ans-panel">🛡️ Technical Viva / Panel Defense Answer</div>
              <p style="font-size:0.92rem; color:var(--text-primary); line-height:1.6;">${q.panel}</p>
            </div>

            <!-- Examiner Trap -->
            <div class="viva-trap-box">
              <strong>⚠️ External Examiner Trap & Counter-Defense:</strong>
              ${q.trap}
            </div>
          </div>
        </div>
      `;
    });

    return `
      <div class="card">
        <h3 class="card-title">Technical Viva Examination Preparation Center</h3>
        <p style="font-size:0.85rem; color:var(--text-secondary); margin-top:4px;">
          Practice answers to over 65 rigorous external examiner questions grounded in the actual Kotlin source code.
        </p>

        <div class="viva-toolbar" id="viva-filters-container">
          <div class="viva-filter-pills">${catPillsHtml}</div>
          <div style="font-size:0.78rem; font-weight:700; color:var(--accent-emerald);">
            Showing ${filteredQuestions.length} Questions
          </div>
        </div>

        <div class="viva-questions-list">${qCardsHtml}</div>
      </div>
    `;
  }

  function setupVivaEventListeners() {
    document.querySelectorAll(".viva-filter-btn").forEach(btn => {
      btn.addEventListener("click", function() {
        AppState.activeVivaCategory = this.getAttribute("data-cat");
        renderCurrentModule();
      });
    });

    document.querySelectorAll(".viva-card-header").forEach(hdr => {
      hdr.addEventListener("click", function() {
        this.closest(".viva-card").classList.toggle("open");
      });
    });
  }

  // ==========================================================================
  // 9. GLOSSARY HTML
  // ==========================================================================
  function renderGlossaryHTML() {
    let itemsHtml = "";
    NutriCheckData.glossary.forEach(g => {
      itemsHtml += `
        <div class="card" style="margin-bottom:14px; padding:18px;">
          <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:6px;">
            <strong style="color:var(--text-white); font-size:1.1rem;">${g.term}</strong>
            <span class="active-file-badge">${g.category}</span>
          </div>
          <p style="font-size:0.9rem; color:var(--text-secondary);">${g.def}</p>
        </div>
      `;
    });

    return `
      <div class="card">
        <h3 class="card-title">NutriCheck Technical & Biochemical Lexicon</h3>
        <p style="font-size:0.85rem; color:var(--text-secondary); margin-top:4px;">
          Comprehensive A-Z glossary covering computer vision, Android architecture, and food chemistry terms.
        </p>
        <div style="margin-top:24px;">${itemsHtml}</div>
      </div>
    `;
  }

  // ==========================================================================
  // 10. NODE INSPECTOR SLIDEOVER MODAL
  // ==========================================================================
  function showNodeInspector(nodeId) {
    const data = NutriCheckDiagrams.components[nodeId];
    if (!data) return;

    let filesHtml = "";
    data.files.forEach(f => {
      filesHtml += `<span class="inspector-file-pill">${f}</span>`;
    });

    DOM.inspectorContent.innerHTML = `
      <div class="inspector-title">${data.title}</div>
      <div class="inspector-role">${data.role}</div>

      <div class="inspector-field">
        <div class="inspector-label">System Responsibility</div>
        <div class="inspector-value">${data.description}</div>
      </div>

      <div class="inspector-field">
        <div class="inspector-label">Related Source Code Files</div>
        <div>${filesHtml}</div>
      </div>

      <div class="inspector-field">
        <div class="inspector-label">Input Streams</div>
        <div class="inspector-value"><code>${data.inputs}</code></div>
      </div>

      <div class="inspector-field">
        <div class="inspector-label">Output Data Artifacts</div>
        <div class="inspector-value"><code>${data.outputs}</code></div>
      </div>
    `;

    DOM.nodeInspectorModal.classList.add("open");
  }

  function hideNodeInspector() {
    DOM.nodeInspectorModal.classList.remove("open");
  }

  // ==========================================================================
  // 11. GLOBAL SEARCH (CMD + K)
  // ==========================================================================
  function setupSearch() {
    function openSearch() {
      DOM.searchModalBackdrop.classList.add("open");
      DOM.searchModalInput.value = "";
      DOM.searchModalInput.focus();
      runSearch("");
    }

    function closeSearch() {
      DOM.searchModalBackdrop.classList.remove("open");
    }

    DOM.searchTriggerBtn.addEventListener("click", openSearch);
    DOM.searchCloseBtn.addEventListener("click", closeSearch);

    DOM.searchModalBackdrop.addEventListener("click", function(e) {
      if (e.target === DOM.searchModalBackdrop) closeSearch();
    });

    document.addEventListener("keydown", function(e) {
      if ((e.metaKey || e.ctrlKey) && e.key === "k") {
        e.preventDefault();
        openSearch();
      }
      if (e.key === "Escape") {
        closeSearch();
        hideNodeInspector();
      }
    });

    DOM.searchModalInput.addEventListener("input", function() {
      runSearch(this.value.trim());
    });
  }

  function runSearch(query) {
    if (!query) {
      DOM.searchResultsList.innerHTML = `<div style="padding:20px; text-align:center; color:var(--text-muted); font-size:0.85rem;">Type keywords like "RulesEngine", "E-numbers", "CameraX", "Gemini", or "Viva"...</div>`;
      return;
    }

    const q = query.toLowerCase();
    const results = [];

    // Search in Modules
    NutriCheckData.modules.forEach(mod => {
      if (mod.title.toLowerCase().includes(q) || mod.shortDesc.toLowerCase().includes(q)) {
        results.push({
          type: "Module",
          title: `Module ${mod.number}: ${mod.title}`,
          snippet: mod.shortDesc,
          action: () => { navigateToModule(mod.id); }
        });
      }
      mod.sections.forEach(sec => {
        if (sec.heading.toLowerCase().includes(q) || sec.beginner.toLowerCase().includes(q) || sec.panel.toLowerCase().includes(q)) {
          results.push({
            type: `Module ${mod.number}`,
            title: sec.heading,
            snippet: sec.beginner.substring(0, 100) + "...",
            action: () => { navigateToModule(mod.id); }
          });
        }
      });
    });

    // Search in Viva Questions
    NutriCheckData.vivaQuestions.forEach(v => {
      if (v.question.toLowerCase().includes(q) || v.beginner.toLowerCase().includes(q) || v.panel.toLowerCase().includes(q)) {
        results.push({
          type: "Viva Q&A",
          title: v.question,
          snippet: v.beginner.substring(0, 100) + "...",
          action: () => {
            AppState.activeVivaCategory = v.category;
            navigateToModule("module-16");
          }
        });
      }
    });

    // Search in Glossary
    NutriCheckData.glossary.forEach(g => {
      if (g.term.toLowerCase().includes(q) || g.def.toLowerCase().includes(q)) {
        results.push({
          type: "Glossary",
          title: g.term,
          snippet: g.def,
          action: () => { navigateToModule("module-17"); }
        });
      }
    });

    if (results.length === 0) {
      DOM.searchResultsList.innerHTML = `<div style="padding:24px; text-align:center; color:var(--text-muted); font-size:0.85rem;">No results found for "${escapeHTML(query)}". Try another search term.</div>`;
      return;
    }

    let rHtml = "";
    results.slice(0, 15).forEach((r, idx) => {
      rHtml += `
        <div class="search-result-item" data-res-idx="${idx}">
          <div class="search-result-badge">${r.type}</div>
          <div class="search-result-title">${r.title}</div>
          <div class="search-result-snippet">${r.snippet}</div>
        </div>
      `;
    });
    DOM.searchResultsList.innerHTML = rHtml;

    DOM.searchResultsList.querySelectorAll(".search-result-item").forEach((el, idx) => {
      el.addEventListener("click", function() {
        results[idx].action();
        DOM.searchModalBackdrop.classList.remove("open");
      });
    });
  }

  // ==========================================================================
  // 12. EVENT LISTENERS & MODE SWITCHER
  // ==========================================================================
  function setupEventListeners() {
    // Mode switcher buttons
    DOM.modeBtnBeginner.addEventListener("click", function() {
      setExplanationMode("beginner");
    });

    DOM.modeBtnPanel.addEventListener("click", function() {
      setExplanationMode("panel");
    });

    // Node inspector close button
    DOM.inspectorCloseBtn.addEventListener("click", hideNodeInspector);

    // Mobile sidebar toggle
    DOM.sidebarToggleBtn.addEventListener("click", function() {
      toggleMobileSidebar(!AppState.isMobileSidebarOpen);
    });

    setupSearch();
  }

  function setExplanationMode(mode) {
    AppState.explanationMode = mode;
    localStorage.setItem("nutricheck_mode", mode);
    if (mode === "beginner") {
      DOM.modeBtnBeginner.classList.add("active");
      DOM.modeBtnPanel.classList.remove("active");
    } else {
      DOM.modeBtnPanel.classList.add("active");
      DOM.modeBtnBeginner.classList.remove("active");
    }
    renderCurrentModule();
  }

  function toggleMobileSidebar(open) {
    AppState.isMobileSidebarOpen = open;
    if (open) {
      DOM.sidebar.classList.add("open");
    } else {
      DOM.sidebar.classList.remove("open");
    }
  }

  function escapeHTML(str) {
    return (str || "")
      .replace(/&/g, "&amp;")
      .replace(/</g, "&lt;")
      .replace(/>/g, "&gt;")
      .replace(/"/g, "&quot;")
      .replace(/'/g, "&#039;");
  }

  // Expose global methods for inline clicks
  window.NutriCheckApp = {
    navigateTo: navigateToModule,
    inspectNode: showNodeInspector,
    runSimulation: runSimulation
  };

  // Run start
  init();
});
