/**
 * NutriCheck Interactive Diagram & Visual Inspector Engine
 * Grounded in Major_Project_Part1 architecture
 */

const NutriCheckDiagrams = {
  // Component detail dictionary for interactive node inspector
  components: {
    "node-user": {
      title: "End User Interaction",
      role: "Consumer Persona & Intent",
      files: ["MainActivity.kt", "MainScreen.kt"],
      inputs: "Physical touch gestures, camera viewfinder positioning, barcode targeting, profile preference toggles.",
      outputs: "Hardware capture commands, UI navigation requests, chat inspector queries.",
      description: "The consumer seeking instantaneous verification of packaged food purity. Interacts through touch targets, camera alignment viewfinders, and interactive portion scalers."
    },
    "node-camerax": {
      title: "CameraX Hardware Abstraction",
      role: "Edge Vision Ingestion",
      files: ["ScannerScreen.kt", "BarcodeScannerScreen.kt"],
      inputs: "Device camera hardware sensor stream (PreviewView, ImageCapture, ImageAnalysis).",
      outputs: "Compressed JPEG 70% scaled bitmap byte array and YUV_420_888 ImageProxy buffers.",
      description: "Google Jetpack CameraX lifecycle-bound subsystem. Provides automatic auto-focus, dynamic exposure correction, hardware flash torch control, and memory-safe frame dropping via STRATEGY_KEEP_ONLY_LATEST."
    },
    "node-mlkit-ocr": {
      title: "Google ML Kit Text Recognition",
      role: "On-Device Character Recognition",
      files: ["ScannerScreen.kt", "app/build.gradle.kts"],
      inputs: "InputImage.fromBitmap(scaledBitmap, 0) (downsampled to 768px maximum dimension).",
      outputs: "Synchronous visionText.text string containing extracted label typography.",
      description: "Quantized on-device neural network pipeline executed locally on Android NNAPI/GPU delegates. Detects text bounding boxes and performs character sequence recognition with sub-200ms latency without transmitting pixel data across the internet."
    },
    "node-mlkit-barcode": {
      title: "Google ML Kit Barcode Scanning",
      role: "Real-Time 1D/2D Symbology Decoder",
      files: ["BarcodeScannerScreen.kt", "app/build.gradle.kts"],
      inputs: "Live camera frame buffers via InputImage.fromMediaImage(mediaImage, rotationDegrees).",
      outputs: "Extracted GTIN/EAN-13 barcode rawValue string.",
      description: "High-throughput on-device scanner analyzing camera frames at 30 FPS. Identifies standard grocery packaging symbologies (EAN-13, EAN-8, UPC-A, UPC-E) and delivers raw numeric codes to trigger catalog queries."
    },
    "node-rules-engine": {
      title: "Deterministic Rules Engine",
      role: "Core Mathematical Domain Scoring",
      files: ["data/RulesEngine.kt", "data/IngredientDatabase.kt"],
      inputs: "rawIngredientsText string, calories, sugarGrams, proteinGrams, fiberGrams, sodiumMg, saturatedFatGrams, watchlist, userProfile.",
      outputs: "DeterministicResult(overallScore, overallGrade, breakdown[5], profileScore, matchedWatchlist, matchedAdditives, keyFindings, conflicts).",
      description: "The authoritative mathematical core of NutriCheck. Evaluates five independent dimensions (Quality, Additives, Sugar, Sodium, Nutrition) clamped between 10 and 100. Operates completely offline with zero stochastic variance, enforcing the Zero-Hallucination Safety Protocol."
    },
    "node-gemini-client": {
      title: "Multi-LLM Orchestrator (GeminiClient)",
      role: "Cloud Intelligence Routing & Fallback",
      files: ["utils/GeminiClient.kt"],
      inputs: "Base64 image, OCR text, UserProfile, active provider ('gemini', 'groq', 'deepseek', 'bytez'), API keys.",
      outputs: "Clean, structured JSON audit object matching strict schema with mobile-safe formatting.",
      description: "OkHttp-powered asynchronous routing matrix. Injects immutable local scores into prompt payloads, mandates simple non-jargon explanations, forbids Markdown tables, and implements candidate model fallback chains against cloud quota errors."
    },
    "node-openfoodfacts": {
      title: "Open Food Facts v2 REST API",
      role: "Global Product Catalogue Registry",
      files: ["ui/scanner/BarcodeScannerScreen.kt", "ui/scanner/ScanOptionsScreen.kt"],
      inputs: "HTTP GET https://world.openfoodfacts.org/api/v2/product/{barcode}.json with custom User-Agent.",
      outputs: "JSON payload containing product_name, brands, ingredients_text, and 100g nutriments array.",
      description: "Open international food database repository. Supplies raw quantitative nutritional declarations and verified ingredients for scanned retail barcodes."
    },
    "node-prefs": {
      title: "SharedPreferences + Gson Storage",
      role: "Local Sandbox Persistence",
      files: ["data/PureCheckPreferences.kt"],
      inputs: "UserProfile edits, new scan history items, bookmarked favorites, shopping items, watchlist tokens.",
      outputs: "Instant synchronous deserialized Kotlin lists and biometric records.",
      description: "Private mode XML storage framework (/data/data/com.example.purecheck/). Serializes complex collections into JSON arrays via Gson TypeToken. Enforces a 50-item FIFO queue on scan history to preserve device memory."
    },
    "node-compose-ui": {
      title: "Jetpack Compose Presentation Layer",
      role: "Declarative UI & Recomposition",
      files: ["ui/result/ResultScreen.kt", "ui/home/HomeScreen.kt", "ui/components/PureCheckComponents.kt"],
      inputs: "DeterministicResult, audit report JSON, quantityMultiplier scaler state, navigation backstack.",
      outputs: "Hardware-accelerated emerald dark theme visual components, dynamic badges, and interactive chat inspector.",
      description: "Modern declarative UI toolkit executing Unidirectional Data Flow. Recomposes only modified UI nodes upon state changes (e.g. portion scaler +/- adjustments or filter tab clicks)."
    }
  },

  renderSystemArchitectureSVG: function() {
    return `
      <svg class="interactive-diagram-svg" viewBox="0 0 1000 680" xmlns="http://www.w3.org/2000/svg">
        <defs>
          <linearGradient id="emeraldGrad" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stop-color="#10B981" />
            <stop offset="100%" stop-color="#059669" />
          </linearGradient>
          <linearGradient id="cardGrad" x1="0%" y1="0%" x2="0%" y2="100%">
            <stop offset="0%" stop-color="#1E2B44" />
            <stop offset="100%" stop-color="#131C2E" />
          </linearGradient>
          <linearGradient id="accentGrad" x1="0%" y1="0%" x2="100%" y2="0%">
            <stop offset="0%" stop-color="#3B82F6" />
            <stop offset="100%" stop-color="#8B5CF6" />
          </linearGradient>
          <filter id="glow" x="-20%" y="-20%" width="140%" height="140%">
            <feGaussianBlur stdDeviation="4" result="blur" />
            <feComposite in="SourceGraphic" in2="blur" operator="over" />
          </filter>
          <marker id="arrow" viewBox="0 0 10 10" refX="6" refY="5" markerWidth="6" markerHeight="6" orient="auto-start-reverse">
            <path d="M 0 1 L 8 5 L 0 9 z" fill="#10B981" />
          </marker>
          <marker id="arrow-blue" viewBox="0 0 10 10" refX="6" refY="5" markerWidth="6" markerHeight="6" orient="auto-start-reverse">
            <path d="M 0 1 L 8 5 L 0 9 z" fill="#3B82F6" />
          </marker>
        </defs>

        <!-- Background zones -->
        <rect x="20" y="20" width="340" height="640" rx="16" fill="#0B1324" stroke="#1E293B" stroke-width="1.5" stroke-dasharray="4 4" />
        <text x="35" y="50" fill="#94A3B8" font-size="12" font-weight="700" letter-spacing="1">CLIENT LAYER (ANDROID EDGE)</text>

        <rect x="380" y="20" width="280" height="640" rx="16" fill="#0D172A" stroke="#1E293B" stroke-width="1.5" stroke-dasharray="4 4" />
        <text x="395" y="50" fill="#94A3B8" font-size="12" font-weight="700" letter-spacing="1">DOMAIN & PERSISTENCE LAYER</text>

        <rect x="680" y="20" width="300" height="640" rx="16" fill="#0B1324" stroke="#1E293B" stroke-width="1.5" stroke-dasharray="4 4" />
        <text x="695" y="50" fill="#94A3B8" font-size="12" font-weight="700" letter-spacing="1">CLOUD INTEGRATION LAYER</text>

        <!-- Connecting Lines -->
        <!-- User to Compose UI -->
        <path d="M 190 120 L 190 155" stroke="#10B981" stroke-width="2" marker-end="url(#arrow)" />
        
        <!-- Compose UI to CameraX -->
        <path d="M 190 225 L 190 260" stroke="#10B981" stroke-width="2" marker-end="url(#arrow)" />

        <!-- CameraX to ML Kit OCR & Barcode -->
        <path d="M 140 330 L 105 385" stroke="#10B981" stroke-width="2" marker-end="url(#arrow)" />
        <path d="M 240 330 L 275 385" stroke="#3B82F6" stroke-width="2" marker-end="url(#arrow-blue)" />

        <!-- ML Kit OCR to Rules Engine -->
        <path d="M 180 430 L 395 380" stroke="#10B981" stroke-width="2" marker-end="url(#arrow)" />

        <!-- ML Kit Barcode to Open Food Facts -->
        <path d="M 350 430 Q 520 540 695 540" stroke="#3B82F6" stroke-width="2" marker-end="url(#arrow-blue)" />

        <!-- Open Food Facts back to Rules Engine -->
        <path d="M 695 520 Q 580 480 520 440" stroke="#3B82F6" stroke-width="2" marker-end="url(#arrow-blue)" />

        <!-- Rules Engine to GeminiClient -->
        <path d="M 645 350 L 695 350" stroke="#10B981" stroke-width="2" marker-end="url(#arrow)" />

        <!-- Rules Engine to SharedPreferences -->
        <path d="M 520 440 L 520 495" stroke="#10B981" stroke-width="2" marker-end="url(#arrow)" />

        <!-- GeminiClient to Cloud LLMs -->
        <path d="M 830 310 L 830 250" stroke="#10B981" stroke-width="2" marker-end="url(#arrow)" />

        <!-- Cloud LLM back to Compose UI via SharedPreferences -->
        <path d="M 830 200 Q 830 110 520 110 Q 320 110 270 160" stroke="#10B981" stroke-width="2" stroke-dasharray="5 5" marker-end="url(#arrow)" />

        <!-- NODES -->
        <!-- 1. End User -->
        <g class="diagram-node" data-node="node-user" cursor="pointer">
          <rect x="70" y="65" width="240" height="55" rx="10" fill="url(#cardGrad)" stroke="#1E2B44" stroke-width="1.5" />
          <circle cx="100" cy="92" r="14" fill="#10B981" fill-opacity="0.2" stroke="#10B981" stroke-width="1.5" />
          <path d="M 100 85 A 4 4 0 1 1 99.9 85 M 93 99 A 7 7 0 0 1 107 99" stroke="#10B981" stroke-width="2" fill="none" />
          <text x="125" y="87" fill="#FFFFFF" font-size="13" font-weight="700">End User Interaction</text>
          <text x="125" y="104" fill="#94A3B8" font-size="11">Camera & Touch Gestures</text>
        </g>

        <!-- 2. Jetpack Compose UI -->
        <g class="diagram-node" data-node="node-compose-ui" cursor="pointer">
          <rect x="60" y="160" width="260" height="65" rx="12" fill="url(#cardGrad)" stroke="#10B981" stroke-width="2" filter="url(#glow)" />
          <rect x="60" y="160" width="6" height="65" fill="#10B981" rx="3" />
          <text x="80" y="187" fill="#FFFFFF" font-size="14" font-weight="800">Jetpack Compose UI</text>
          <text x="80" y="206" fill="#10B981" font-size="11" font-weight="600">HomeScreen • ResultScreen • Nav3</text>
        </g>

        <!-- 3. CameraX Subsystem -->
        <g class="diagram-node" data-node="node-camerax" cursor="pointer">
          <rect x="60" y="265" width="260" height="65" rx="12" fill="url(#cardGrad)" stroke="#1E2B44" stroke-width="1.5" />
          <circle cx="90" cy="297" r="16" fill="#1E2B44" stroke="#3B82F6" stroke-width="1.5" />
          <circle cx="90" cy="297" r="6" fill="#3B82F6" />
          <text x="120" y="292" fill="#FFFFFF" font-size="13" font-weight="700">CameraX Pipeline</text>
          <text x="120" y="311" fill="#94A3B8" font-size="11">Lifecycle Preview & ImageCapture</text>
        </g>

        <!-- 4. ML Kit OCR -->
        <g class="diagram-node" data-node="node-mlkit-ocr" cursor="pointer">
          <rect x="35" y="390" width="145" height="75" rx="10" fill="url(#cardGrad)" stroke="#10B981" stroke-width="1.5" />
          <text x="50" y="416" fill="#10B981" font-size="11" font-weight="800">ML KIT OCR</text>
          <text x="50" y="433" fill="#FFFFFF" font-size="12" font-weight="700">Text Recognizer</text>
          <text x="50" y="450" fill="#94A3B8" font-size="10">Latin Script (On-Device)</text>
        </g>

        <!-- 5. ML Kit Barcode -->
        <g class="diagram-node" data-node="node-mlkit-barcode" cursor="pointer">
          <rect x="195" y="390" width="145" height="75" rx="10" fill="url(#cardGrad)" stroke="#3B82F6" stroke-width="1.5" />
          <text x="210" y="416" fill="#3B82F6" font-size="11" font-weight="800">ML KIT BARCODE</text>
          <text x="210" y="433" fill="#FFFFFF" font-size="12" font-weight="700">Barcode Scanner</text>
          <text x="210" y="450" fill="#94A3B8" font-size="10">EAN-13 / UPC 30 FPS</text>
        </g>

        <!-- 6. Deterministic Rules Engine (The Central Brain) -->
        <g class="diagram-node" data-node="node-rules-engine" cursor="pointer">
          <rect x="400" y="270" width="240" height="150" rx="16" fill="url(#cardGrad)" stroke="#10B981" stroke-width="2.5" filter="url(#glow)" />
          <rect x="415" y="285" width="210" height="26" rx="6" fill="#10B981" fill-opacity="0.15" />
          <text x="425" y="303" fill="#10B981" font-size="12" font-weight="800" letter-spacing="1">DETERMINISTIC ENGINE</text>
          <text x="420" y="333" fill="#FFFFFF" font-size="16" font-weight="800">RulesEngine.kt</text>
          <text x="420" y="353" fill="#94A3B8" font-size="11">5-Axis Score [10-100] • Grades A-F</text>
          <text x="420" y="370" fill="#94A3B8" font-size="11">IngredientDatabase (17+ Toxins)</text>
          <text x="420" y="388" fill="#94A3B8" font-size="11">Profile Conflict Deductions</text>
          <text x="420" y="407" fill="#10B981" font-size="11" font-weight="700">✓ ZERO HALLUCINATIONS</text>
        </g>

        <!-- 7. SharedPreferences & Local DB -->
        <g class="diagram-node" data-node="node-prefs" cursor="pointer">
          <rect x="400" y="500" width="240" height="90" rx="12" fill="url(#cardGrad)" stroke="#1E2B44" stroke-width="1.5" />
          <text x="420" y="528" fill="#FFFFFF" font-size="14" font-weight="700">PureCheckPreferences</text>
          <text x="420" y="547" fill="#94A3B8" font-size="11">SharedPreferences + Gson</text>
          <text x="420" y="565" fill="#94A3B8" font-size="11">UserProfile • 50 Scan History (FIFO)</text>
          <text x="420" y="580" fill="#94A3B8" font-size="11">Favorites • ShoppingList</text>
        </g>

        <!-- 8. GeminiClient Orchestrator -->
        <g class="diagram-node" data-node="node-gemini-client" cursor="pointer">
          <rect x="700" y="315" width="260" height="85" rx="12" fill="url(#cardGrad)" stroke="#8B5CF6" stroke-width="1.5" />
          <text x="720" y="342" fill="#8B5CF6" font-size="11" font-weight="800">INTELLIGENCE ORCHESTRATOR</text>
          <text x="720" y="362" fill="#FFFFFF" font-size="14" font-weight="700">GeminiClient.kt</text>
          <text x="720" y="380" fill="#94A3B8" font-size="11">Multi-LLM Fallback Matrix (OkHttp)</text>
          <text x="720" y="394" fill="#94A3B8" font-size="10">Candidate Retry: Vision & Chat</text>
        </g>

        <!-- 9. Cloud AI Providers -->
        <g class="diagram-node" data-node="node-gemini-client" cursor="pointer">
          <rect x="700" y="165" width="260" height="85" rx="12" fill="url(#cardGrad)" stroke="#1E2B44" stroke-width="1.5" />
          <text x="720" y="192" fill="#10B981" font-size="13" font-weight="800">Cloud Foundation Models</text>
          <text x="720" y="210" fill="#94A3B8" font-size="11">• Google Gemini 2.0 Flash</text>
          <text x="720" y="226" fill="#94A3B8" font-size="11">• Groq LLaMA 3.3 / Qwen 2.5</text>
          <text x="720" y="242" fill="#94A3B8" font-size="11">• DeepSeek-V3 Chat</text>
        </g>

        <!-- 10. Open Food Facts API -->
        <g class="diagram-node" data-node="node-openfoodfacts" cursor="pointer">
          <rect x="700" y="495" width="260" height="85" rx="12" fill="url(#cardGrad)" stroke="#3B82F6" stroke-width="1.5" />
          <text x="720" y="522" fill="#3B82F6" font-size="11" font-weight="800">EXTERNAL REST API</text>
          <text x="720" y="542" fill="#FFFFFF" font-size="14" font-weight="700">Open Food Facts v2</text>
          <text x="720" y="560" fill="#94A3B8" font-size="11">world.openfoodfacts.org</text>
          <text x="720" y="574" fill="#94A3B8" font-size="10">Nutriments (100g) & Ingredients Text</text>
        </g>
      </svg>
    `;
  },

  renderPipelineSVG: function() {
    return `
      <svg class="interactive-diagram-svg" viewBox="0 0 950 480" xmlns="http://www.w3.org/2000/svg">
        <defs>
          <linearGradient id="pipeGrad" x1="0%" y1="0%" x2="100%" y2="0%">
            <stop offset="0%" stop-color="#10B981" />
            <stop offset="50%" stop-color="#3B82F6" />
            <stop offset="100%" stop-color="#8B5CF6" />
          </linearGradient>
          <marker id="pipeArrow" viewBox="0 0 10 10" refX="6" refY="5" markerWidth="6" markerHeight="6" orient="auto-start-reverse">
            <path d="M 0 1 L 8 5 L 0 9 z" fill="#10B981" />
          </marker>
        </defs>

        <!-- Pipeline Bar -->
        <rect x="40" y="40" width="870" height="12" rx="6" fill="#1E293B" />
        <rect x="40" y="40" width="870" height="12" rx="6" fill="url(#pipeGrad)" opacity="0.8" />

        <!-- Steps -->
        <!-- Step 1 -->
        <circle cx="90" cy="46" r="20" fill="#0B1324" stroke="#10B981" stroke-width="3" />
        <text x="90" y="52" fill="#10B981" font-size="13" font-weight="900" text-anchor="middle">1</text>
        <rect x="25" y="80" width="130" height="140" rx="8" fill="#131C2E" stroke="#1E2B44" stroke-width="1" />
        <text x="90" y="105" fill="#FFFFFF" font-size="12" font-weight="700" text-anchor="middle">Physical Capture</text>
        <text x="90" y="125" fill="#94A3B8" font-size="10" text-anchor="middle">CameraX Viewfinder</text>
        <text x="90" y="145" fill="#94A3B8" font-size="10" text-anchor="middle">Green Laser Reticle</text>
        <text x="90" y="165" fill="#94A3B8" font-size="10" text-anchor="middle">Flash Torch Toggle</text>
        <text x="90" y="195" fill="#10B981" font-size="9" font-weight="700" text-anchor="middle">ScannerScreen.kt</text>

        <!-- Step 2 -->
        <circle cx="280" cy="46" r="20" fill="#0B1324" stroke="#10B981" stroke-width="3" />
        <text x="280" y="52" fill="#10B981" font-size="13" font-weight="900" text-anchor="middle">2</text>
        <rect x="215" y="80" width="130" height="140" rx="8" fill="#131C2E" stroke="#1E2B44" stroke-width="1" />
        <text x="280" y="105" fill="#FFFFFF" font-size="12" font-weight="700" text-anchor="middle">Optimization</text>
        <text x="280" y="125" fill="#94A3B8" font-size="10" text-anchor="middle">Downscale 768px</text>
        <text x="280" y="145" fill="#94A3B8" font-size="10" text-anchor="middle">70% JPEG Compress</text>
        <text x="280" y="165" fill="#94A3B8" font-size="10" text-anchor="middle">Base64 Encoding</text>
        <text x="280" y="195" fill="#10B981" font-size="9" font-weight="700" text-anchor="middle">resizeBitmap()</text>

        <!-- Step 3 -->
        <circle cx="470" cy="46" r="20" fill="#0B1324" stroke="#3B82F6" stroke-width="3" />
        <text x="470" y="52" fill="#3B82F6" font-size="13" font-weight="900" text-anchor="middle">3</text>
        <rect x="405" y="80" width="130" height="140" rx="8" fill="#131C2E" stroke="#1E2B44" stroke-width="1" />
        <text x="470" y="105" fill="#FFFFFF" font-size="12" font-weight="700" text-anchor="middle">Edge ML OCR</text>
        <text x="470" y="125" fill="#94A3B8" font-size="10" text-anchor="middle">Google ML Kit</text>
        <text x="470" y="145" fill="#94A3B8" font-size="10" text-anchor="middle">Latin Recognizer</text>
        <text x="470" y="165" fill="#94A3B8" font-size="10" text-anchor="middle">Tasks.await() [Edge]</text>
        <text x="470" y="195" fill="#3B82F6" font-size="9" font-weight="700" text-anchor="middle">TextRecognition</text>

        <!-- Step 4 -->
        <circle cx="660" cy="46" r="20" fill="#0B1324" stroke="#10B981" stroke-width="3" />
        <text x="660" y="52" fill="#10B981" font-size="13" font-weight="900" text-anchor="middle">4</text>
        <rect x="595" y="80" width="130" height="140" rx="8" fill="#131C2E" stroke="#10B981" stroke-width="1.5" />
        <text x="660" y="105" fill="#10B981" font-size="12" font-weight="800" text-anchor="middle">Domain Rules</text>
        <text x="660" y="125" fill="#FFFFFF" font-size="10" text-anchor="middle">RulesEngine.kt</text>
        <text x="660" y="145" fill="#94A3B8" font-size="10" text-anchor="middle">5-Axis Math Formula</text>
        <text x="660" y="165" fill="#94A3B8" font-size="10" text-anchor="middle">Profile Conflicts</text>
        <text x="660" y="195" fill="#10B981" font-size="9" font-weight="700" text-anchor="middle">Strict Determinism</text>

        <!-- Step 5 -->
        <circle cx="850" cy="46" r="20" fill="#0B1324" stroke="#8B5CF6" stroke-width="3" />
        <text x="850" y="52" fill="#8B5CF6" font-size="13" font-weight="900" text-anchor="middle">5</text>
        <rect x="785" y="80" width="130" height="140" rx="8" fill="#131C2E" stroke="#1E2B44" stroke-width="1" />
        <text x="850" y="105" fill="#FFFFFF" font-size="12" font-weight="700" text-anchor="middle">Cloud Explain</text>
        <text x="850" y="125" fill="#94A3B8" font-size="10" text-anchor="middle">Gemini 2.0 Flash</text>
        <text x="850" y="145" fill="#94A3B8" font-size="10" text-anchor="middle">Prompt Constraint</text>
        <text x="850" y="165" fill="#94A3B8" font-size="10" text-anchor="middle">Structured JSON</text>
        <text x="850" y="195" fill="#8B5CF6" font-size="9" font-weight="700" text-anchor="middle">GeminiClient.kt</text>

        <!-- Bottom Narrative Card -->
        <rect x="40" y="260" width="870" height="180" rx="12" fill="#0F172A" stroke="#1E293B" stroke-width="1.5" />
        <text x="70" y="295" fill="#10B981" font-size="14" font-weight="800">WHY THIS DUAL-STAGE PIPELINE GUARDS AGAINST AUDIT CORRUPTION</text>
        <text x="70" y="325" fill="#94A3B8" font-size="12" line-height="20">
          • Stage 1 to 4 execute strictly within the Android sandbox in less than 300 milliseconds.
        </text>
        <text x="70" y="350" fill="#94A3B8" font-size="12">
          • The mathematical score (e.g. S=67, Grade='C') is computed BEFORE network transmission.
        </text>
        <text x="70" y="375" fill="#94A3B8" font-size="12">
          • The LLM is supplied with the exact computed numbers and forbidden from altering them.
        </text>
        <text x="70" y="400" fill="#94A3B8" font-size="12">
          • If the user's internet is disconnected, the score and flagged additives remain 100% available offline!
        </text>
      </svg>
    `;
  }
};

if (typeof window !== "undefined") {
  window.NutriCheckDiagrams = NutriCheckDiagrams;
}
if (typeof global !== "undefined") {
  global.NutriCheckDiagrams = NutriCheckDiagrams;
}
if (typeof module !== "undefined" && module.exports) {
  module.exports = { NutriCheckDiagrams };
}
