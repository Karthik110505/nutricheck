/**
 * NutriCheck Learning Platform Master Data Repository
 * 100% Traceable to Major_Project_Part1 Source Code
 * Author & Student: V. V. Karthik Barrenkala (R210111) & Machipalli Uday Bhaskar (R210288)
 * Institution: Rajiv Gandhi University of Knowledge Technologies (RGUKT)
 * Department: Computer Science & Engineering
 */

const NutriCheckData = {
  meta: {
    appName: "NutriCheck",
    internalNamespace: "com.example.purecheck",
    version: "2.0 (Production Build)",
    targetSdk: 36,
    minSdk: 24,
    buildTool: "Gradle 8.x with Kotlin DSL",
    author: "V. V. Karthik Barrenkala (R210111) & Machipalli Uday Bhaskar (R210288)",
    institution: "Rajiv Gandhi University of Knowledge Technologies (RGUKT)",
    dept: "Computer Science & Engineering",
    academicContext: "B.Tech CSE Major Project Evaluation"
  },

  modules: [
    {
      id: "module-1",
      number: "01",
      title: "Project Introduction & Vision",
      shortDesc: "Understand the core motivation, consumer challenges, and why NutriCheck is a paradigm shift in food transparency.",
      badge: "Foundations",
      sections: [
        {
          heading: "1. What is NutriCheck?",
          beginner: "Think of NutriCheck as a knowledgeable biochemical scientist living right inside your smartphone. When you pick up a packaged food item at a supermarket—like a box of cereal, cookies, or energy drink—the back label is covered with tiny, confusing text: scientific jargon, INS numbers, artificial preservatives, and misleading health claims. NutriCheck lets you take a photo of that label or scan its barcode, and instantly translates that chemical list into an easy-to-read letter grade (from A down to F), flags harmful additives, calculates exact nutritional ratings, and checks if it clashes with your personal health goals (like diabetes, high blood pressure, or a vegan diet).",
          panel: "NutriCheck is an Android-native Edge-to-Cloud Food Intelligence Platform engineered in Kotlin and Jetpack Compose. It resolves consumer asymmetric information in modern retail packaging by providing a dual-input ingestion pipeline: on-device high-throughput Barcode scanning backed by Open Food Facts v2 REST API, and on-device Optical Character Recognition (OCR) powered by Google Play Services ML Kit Latin Text Recognition. What makes NutriCheck technically distinctive is its hybrid computational architecture: rather than delegating safety classifications to unpredictable Large Language Models (LLMs), it executes a local, mathematically deterministic Rules Engine (RulesEngine.kt) across five weighted dimensions, while reserving Cloud GenAI models (Google Gemini 2.0 Flash, Groq LLaMA-3.3, DeepSeek-V3) exclusively for structured natural-language contextualization and conversational audit defense."
        },
        {
          heading: "2. The Critical Problem with Modern Packaged Foods",
          beginner: "Have you ever tried reading the ingredients on a packet of chips or a health bar? Most consumers assume that if a product is on a store shelf, it must be safe. However: 1) Chemical codes like E621, E211, or E102 hide common allergens and irritants. 2) Manufacturers use obfuscated terms like 'Refined Vegetable Oil' instead of admitting they use cheap Palm Oil high in saturated fats. 3) 'Zero Sugar' claims often conceal artificial non-caloric sweeteners like Aspartame which can alter gut flora. 4) People with hypertension or diabetes cannot manually calculate their daily safe limit while standing in a busy supermarket aisle.",
          panel: "Modern industrial food processing relies heavily on chemical preservation, artificial stabilization, and sensory enhancement. The regulatory framework allows manufacturers to utilize standardized alphanumeric identifiers (such as European E-numbers and International Numbering System codes) and broad umbrella terms (e.g. 'Hydrogenated Fats', 'Flavor Enhancers', 'Hydrolyzed Vegetable Protein'). This creates substantial cognitive friction for end-users, particularly populations managing chronic non-communicable diseases (hypertension, Type-2 diabetes, cardiovascular disease, IBS). Static scanning tools either rely on crowdsourced, often outdated static databases or perform superficial barcode matching without personalized metabolic context."
        },
        {
          heading: "3. NutriCheck's Core Proposed Solution",
          beginner: "NutriCheck bridges this gap through four clear steps: 1) CAPTURE: Instant CameraX viewfinder with real-time green laser alignment. 2) DECODE: On-device ML Kit OCR extracts the raw text in under 200 milliseconds. 3) EVALUATE: A strictly deterministic local mathematical formula calculates five health scores (Quality, Additives, Sugar, Sodium, Nutrition) and checks against your medical preferences. 4) UNDERSTAND: A conversational AI inspector allows you to ask questions in plain English ('Is this safe for my diabetic mother?') without medical jargon.",
          panel: "NutriCheck introduces a reliable Zero-Hallucination Safety Protocol. In classical AI applications, feeding raw text to a generative model results in variable, subjective scoring. NutriCheck eliminates this vulnerability by enforcing a strict trust boundary: the mathematical score and letter grade are computed locally in RulesEngine.kt. These immutable computed values are then injected into the prompt payload delivered to Gemini 2.0 Flash or Groq LLaMA-3.3 with explicit constraints: 'YOU MUST USE THESE EXACT CALCULATED VALUES FOR YOUR REPORT. Do not recalculate or invent these scores.' Thus, generative AI acts strictly as an explanatory interface, never as the authoritative judge."
        },
        {
          heading: "4. Target Users and Real-World Impact",
          beginner: "NutriCheck is built for everyday families, health-conscious fitness enthusiasts, diabetic patients monitoring glycemic spikes, parents protecting children from hyperactive food dyes, vegans avoiding hidden animal gelatin, and individuals with cardiovascular risks needing strict sodium monitoring.",
          panel: "The primary consumer personas encompass: 1) Medical Dietary Managers (diabetic, hypertensive, renal patients requiring precise mg-level sodium and sugar thresholds). 2) Ethical & Lifestyle Consumers (strict vegan, vegetarian, or kosher diets sensitive to animal-derived emulsifiers like gelatin E441 or carmine E120). 3) Preventive Wellness & Fitness Cohorts (tracking macronutrient ratios, high-protein density, and whole-food purity). 4) Everyday Consumers seeking rapid pre-purchase validation in low-connectivity retail settings."
        }
      ]
    },
    {
      id: "module-2",
      number: "02",
      title: "Problem Statement, Scope & Trust Boundary",
      shortDesc: "Academic defense of research motivation, formal problem formulation, and strict architectural trust boundaries.",
      badge: "Academic Defense",
      sections: [
        {
          heading: "1. Formal Problem Statement",
          beginner: "Consumers are buying food products whose true health impacts are hidden behind confusing marketing and technical chemical lists. Existing apps only check barcodes, but millions of local or fresh packaged products don't exist in international barcode databases. NutriCheck solves both the barcode limitation and the ingredient comprehension problem.",
          panel: "Formally, consumer packaged goods (CPG) verification suffers from: 1) Catalogue Incompleteness: New, regional, or repackaged goods lack registered Global Trade Item Numbers (GTIN/EAN) in open repositories. 2) Semantic Obfuscation: Manufacturers replace regulated allergens with chemically equivalent synonyms. 3) Heuristic Decoupling: Generic front-of-package nutrition scoring systems (such as Nutri-Score) fail to penalize chemical additives or adjust dynamically for user-specific metabolic contraindications. NutriCheck solves this dual challenge by coupling visual OCR text extraction with a multi-layered deterministic expert rules system."
        },
        {
          heading: "2. The Trust Boundary (Critical Academic Review Point)",
          beginner: "NutriCheck provides informational dietary guidance, NOT formal medical prescriptions. More importantly, the application never lets AI make up a safety score. A transparent local formula does the math, and the AI merely explains that math in simple terms.",
          panel: "Architectural Trust Boundary Definition: 1) Deterministic Core: All scoring, grade allocation, E-number mapping, and profile conflict deductions are executed synchronously within the Android runtime without network dependencies. 2) Generative Layer: Cloud LLM services are restricted to text-to-JSON formatting, semantic summarization, and clean-swap alternative discovery. 3) Disclaimer Constraint: As mandated by healthcare software compliance, all outputs are categorized as educational dietary audits and explicitly state that heuristic grades do not constitute clinical diagnostic advice."
        },
        {
          heading: "3. Scope & Explicit Implementation Boundaries",
          beginner: "What NutriCheck DOES: Scans labels, scans barcodes, runs local 5-axis scoring, detects 17+ hazardous additives, manages user profiles & watchlists, scales nutrition by portion, saves favorites/history, and provides interactive AI chat. What it does NOT do yet: It does not perform laboratory chemical spectroscopy, does not store clinical medical records, and does not have a side-by-side comparison screen in the current Kotlin build.",
          panel: "In Scope (Implemented): 1) Edge vision via CameraX with dynamic viewfinder framing. 2) Synchronous ML Kit OCR & Barcode processing. 3) Open Food Facts v2 JSON client via OkHttp. 4) Multi-provider fallback engine (Gemini 2.0 Flash, Groq, DeepSeek, Bytez). 5) SharedPreferences + Gson local persistence. 6) Portion scaling multiplier. 7) Interactive 3-tier inspector chat. Out of Scope (Planned for Part 2): Real-time peer-to-peer product comparison view (mentioned in preliminary abstract, framed as future work), offline on-device quantized LLM inference, and multi-lingual regional Indian dialect OCR."
        }
      ]
    },
    {
      id: "module-3",
      number: "03",
      title: "Complete Implemented Features Catalogue",
      shortDesc: "Exhaustive breakdown of all 20+ features, detailing UI inputs, processing logic, files involved, and error handling.",
      badge: "Feature Audit",
      sections: [
        {
          heading: "Feature 1: Home Dashboard & Historical Snapshot",
          beginner: "The first screen you see when opening the app. Shows your average food score, total scans, most flagged issue (like High Sodium or Additives), a quick scan action button, personalized health recommendations, and your 5 most recent scans.",
          panel: "Component: HomeScreen.kt. Lifecycle: Launches in MainAppContainer as AppTab.Home. Data Source: PureCheckPreferences.getHistory(). Computes: aggregate averageScore, totalScans, and parses healthRisks JSON arrays across all historical entries to identify the modal flagged hazard (e.g. 'High Sodium', 'Palm Oil/Fats'). Renders responsive NutriCheckWordmark and dynamic insight card with fallback for first-time users."
        },
        {
          heading: "Feature 2: Real-Time Label Scanning (CameraX + ML Kit)",
          beginner: "A live camera screen with an animated green laser scanning line. You align the ingredients list inside the box, tap the scan button, and it captures, resizes, extracts text, and sends it for analysis.",
          panel: "Component: ScannerScreen.kt. Implementation: CameraX Preview + ImageCapture bound to LocalLifecycleOwner. Viewfinder: Custom Compose Canvas drawing an animated laser line via rememberInfiniteTransition (2500ms tween). Pipeline: captureImage() saves to cacheDir, downscales via resizeBitmap(768) to optimize bandwidth, compresses to JPEG 70%, converts to Base64, and invokes Tasks.await(recognizer.process(inputImage)) via ML Kit TextRecognition client. Dispatches to GeminiClient.analyzeIngredients()."
        },
        {
          heading: "Feature 3: On-Device Barcode Scanner & Open Food Facts",
          beginner: "Point your camera at any product barcode. The app automatically detects the barcode number without you needing to press any button, looks up the product in the Open Food Facts global database, and calculates the score immediately.",
          panel: "Component: BarcodeScannerScreen.kt. Implementation: CameraX ImageAnalysis with STRATEGY_KEEP_ONLY_LATEST. Analyzer: BarcodeScanning.getClient().processes(InputImage). Extracts raw barcode digits. Network: lookupBarcodeAndAnalyze() issues OkHttp GET to https://world.openfoodfacts.org/api/v2/product/{barcode}.json with custom User-Agent. Extracts nutriments (energy-kcal, sugars, proteins, fiber, sodium, saturated-fat) and raw ingredients_text, executes RulesEngine.calculate(), and generates report JSON."
        },
        {
          heading: "Feature 4: Manual Barcode Search",
          beginner: "If a barcode is smudged or your camera is damaged, you can type the barcode digits into a text box in the Scan tab to look up the product.",
          panel: "Component: ScanOptionsScreen.kt. Takes numeric input, filters non-digit characters via barcodeText.filter { it.isDigit() }, and triggers lookupBarcodeAndAnalyze() asynchronously with coroutine error handling for 404 unregistered products."
        },
        {
          heading: "Feature 5: Deterministic 5-Axis Food Scoring & Grading",
          beginner: "The product gets an overall grade (A, B, C, D, or F) and score out of 100 based on five specific areas: Ingredient Quality, Additives, Sugar, Sodium, and Nutrition.",
          panel: "Component: RulesEngine.kt. Executes calculate(). Computes 5 distinct metrics clamped between 10 and 100: ingredientQuality (base 100 with evidence-weighted deductions and obfuscation penalties), additives (count x -8 plus preservative penalties), sugar (scaled excess over 5g or keyword deduction), sodium (scaled excess over 140mg or keyword deduction), and nutrition (macro adjustments blended with sugar and sodium). Final arithmetic mean determines grade: A (>=80), B (>=68), C (>=52), D (>=38), F (<38)."
        },
        {
          heading: "Feature 6: Chemical Additive & E-Number Detection",
          beginner: "Scans for 17+ dangerous preservatives, colors, artificial sweeteners, and thickeners using both standard names (like Carrageenan) and codes (like E407).",
          panel: "Component: IngredientDatabase.kt. Pre-compiled in-memory dictionary of IngredientDef objects with name, aliases, eNumber, category, function, description, potentialConcerns, evidenceLevel (High/Medium/Low), and dietaryFlags (vegan_conflict, heart_risk, diabetic_risk, gut_risk, skin_risk). Matching logic performs multi-pass case-insensitive normalized substring matching."
        },
        {
          heading: "Feature 7: Ingredient Obfuscation Unmasking",
          beginner: "Companies often disguise unhealthy ingredients under clean-sounding names. NutriCheck detects when 'Refined Vegetable Oil' is actually Palm Oil or 'Yeast Extract' is hidden MSG.",
          panel: "Component: RulesEngine.kt & ResultScreen.kt. The rules engine penalizes obfuscated vegetable oils (-10) and hydrolyzed protein/yeast extract (-8). The LLM output schema parses obfuscatedIngredients array specifying printed label, actual identity, biochemical rationale, and severity (high/medium/low)."
        },
        {
          heading: "Feature 8: Personalized Dietary Profile Conflict Engine",
          beginner: "NutriCheck checks if a product is safe for YOUR body. If you are Vegan and the product contains gelatin, or Diabetic and it contains high-fructose corn syrup, you get a bright red warning banner and a reduced Profile Score.",
          panel: "Component: RulesEngine.kt. Operates on UserProfile entity. Checks vegan (-30 for 25+ animal derivatives including casein, whey, carmine, gelatin, cochineal), vegetarian (-20 for meat/fish derivatives), diabetic (-20 for sugar >10g or HFCS/aspartame), heartHealth (-20 for sodium >360mg or hydrogenated fats), gutHealth (-15 for gut-irritant emulsifiers like E407, E433), cosmeticSafe (-20 for parabens/SLS), lowSugar (-15), lowSodium (-15), and custom watchlist (-25 per matched token). Clamps to [10, min(overallScore, profileScore)]."
        },
        {
          heading: "Feature 9: Interactive Serving Size / Portion Scaler",
          beginner: "Nutrition labels list values per 100g, but what if you eat a 250g serving? In the results screen, tap '+' or '-' to scale the calories, protein, carbs, sugar, and sodium in real-time.",
          panel: "Component: ResultScreen.kt. Uses quantityMultiplier state variable with 0.25x step increments. Dynamically multiplies baseCalories, baseProtein, baseCarbs, baseFat, baseFiber, baseSugar, and baseSodium, dynamically triggering remember(profileResult) recalculation of profile conflict thresholds."
        },
        {
          heading: "Feature 10: Full-Screen Conversational Inspector (AI Chat)",
          beginner: "Got a question about the scanned product? Tap the chat button to talk with the AI Food Inspector. Choose between Simple Mode (plain everyday language), Detailed Mode, or Scientific Mode (biochemical mechanisms).",
          panel: "Component: ResultScreen.kt (FullScreenChatView) and GeminiClient.kt (discussResult). Features: Strict mobile formatting rules (forbids Markdown tables to prevent horizontal overflow; mandates structured bullet points and '### Parameter' headers). Incorporates active explanationMode state token ('[Simple Mode]', '[Detailed Mode]', '[Scientific Mode]'). Dispatches multi-turn chat history via OkHttp to the active provider (Gemini 2.0 Flash, Groq, DeepSeek)."
        },
        {
          heading: "Feature 11: Clean Swaps Alternative Recommender",
          beginner: "If a product gets a bad score (like D or F), NutriCheck suggests healthier, cleaner alternatives you can buy instead.",
          panel: "Component: ResultScreen.kt. Parses cleanSwaps JSON array containing brand, product, and reason. Displayed as a distinct recommendation card with an emerald swap badge."
        },
        {
          heading: "Feature 12: Integrated Shopping List Mode",
          beginner: "Add items you need to buy at the store. When you scan an item, it links the scan result to your shopping item so you can verify safety before purchasing.",
          panel: "Component: ScanOptionsScreen.kt. Backed by PureCheckPreferences.getShoppingList() / addShoppingItem(). Stores ShoppingItem(id, name, checked, score, grade, scanId). Features quick completion toggling, deletion, and summary modal."
        },
        {
          heading: "Feature 13: Product Favorites & Multi-Filter Search",
          beginner: "Save your favorite healthy items. Filter your saved items by 'Grade A', 'High Protein' (>15g), or 'Low Calorie' (<150 kcal).",
          panel: "Component: FavoritesScreen.kt. Queries FavoriteProduct records from SharedPreferences. Renders responsive search bar and chip-based filter row (All, Grade A, High Protein, Low Calorie). Clicking any item re-opens the full ResultScreen report via JSON deserialization."
        },
        {
          heading: "Feature 14: Biometric Profile & BMI Calculator",
          beginner: "Enter your age, gender, height (cm), and weight (kg). NutriCheck calculates your Body Mass Index (BMI) and personalizes your nutrition thresholds.",
          panel: "Component: MyProfileScreen.kt and ProfileScreen.kt. Manages UserProfile. Automatically computes BMI = weight / (height / 100)^2. Persists to SharedPreferences with immediate updates reflected in HomeScreen greeting and dashboard metrics."
        },
        {
          heading: "Feature 15: Custom Ingredient Watchlist",
          beginner: "Allergic to peanuts, gluten, or soy? Add them to your personal Watchlist. If NutriCheck finds any of your blacklisted words on a label, it flags it instantly with a 25-point penalty.",
          panel: "Component: ProfileScreen.kt & PureCheckPreferences.kt. Stores List<String> as a JSON array under 'watchlist'. Tokenized and evaluated in RulesEngine.kt via case-insensitive containment check, generating dedicated '✕ WATCHLIST CONFLICT' alerts."
        },
        {
          heading: "Feature 16: Multi-Provider Intelligence Settings",
          beginner: "You can choose which AI model powers the app: Google Gemini, Groq (ultra-fast LLaMA), DeepSeek, or Bytez. Simply enter your API key in Settings.",
          panel: "Component: SettingsScreen.kt. Allows switching active_provider between 'gemini', 'groq', 'deepseek', and 'bytez'. Securely captures and persists respective API keys with PasswordVisualTransformation. Implements automatic candidate model fallback sequences in GeminiClient.kt."
        },
        {
          heading: "Feature 17: Local Data Management & Cache Clearing",
          beginner: "A one-tap button in Settings to wipe your scan history records whenever you want privacy.",
          panel: "Component: SettingsScreen.kt. Calls prefs.clearHistory(), rewriting 'scan_history' key in SharedPreferences to '[]' with atomic commit/apply and UI feedback alert."
        },
        {
          heading: "Feature 18: Author & Academic Credits Module",
          beginner: "Displays project information, developer names, roll numbers, department, and university credentials.",
          panel: "Component: AuthorScreen.kt. Credits student developers V. V. Karthik Barrenkala (R210111) and Machipalli Uday Bhaskar (R210288), Rajiv Gandhi University of Knowledge Technologies (RGUKT), B.Tech Major Project context."
        }
      ]
    },
    {
      id: "module-4",
      number: "04",
      title: "Technology Stack Deep Dive",
      shortDesc: "Complete breakdown of programming languages, libraries, SDKs, and why each was chosen over alternatives.",
      badge: "Architecture Stack",
      sections: [
        {
          heading: "1. Core Language & Runtime: Kotlin 1.9 / JVM 17",
          beginner: "Kotlin is modern, concise, and Google's official preferred language for Android. It eliminates common programming bugs (like null pointer crashes) and allows writing cleaner code.",
          panel: "Language: Kotlin configured via jvmToolchain(17) and Java 17 compatibility. Rationale: Idiomatic null safety, coroutines for non-blocking asynchronous I/O, data classes for immutable domain models (ScoreBreakdown, DeterministicResult, UserProfile), and first-class integration with Jetpack Compose. Alternatives: Java (rejected due to excessive boilerplate and lack of declarative UI conciseness) and Flutter/Dart (rejected to maintain native zero-overhead CameraX and ML Kit integration)."
        },
        {
          heading: "2. UI Toolkit: Jetpack Compose & Material 3",
          beginner: "Instead of old-fashioned XML files, Jetpack Compose lets us build the entire user interface using modern Kotlin code that automatically updates when data changes.",
          panel: "Framework: AndroidX Compose BOM with Material3. Architecture: Unidirectional Data Flow (UDF) where state flows down and events flow up. Utilizes remember, mutableStateOf, LaunchedEffect for side-effects, and custom Canvas draw routines for scanning reticles. Eliminates View hierarchy overhead and provides seamless dark theme styling via PureCheckColors."
        },
        {
          heading: "3. Navigation: Jetpack Navigation 3 (Nav3)",
          beginner: "Handles moving between screens (Home -> Scanner -> Result -> Settings). Navigation 3 is the newest, most modern way to navigate in Compose.",
          panel: "Library: androidx.navigation3.ui and androidx.navigation3.runtime. Key primitives: NavKey polymorphic serializable destinations (Main, Scanner, Result(jsonResult), Settings, Author, BarcodeScanner, MyProfile, Favorites). Managed via rememberNavBackStack(Main) and rendered using NavDisplay with an entryProvider. This is cutting-edge Android development, avoiding fragile string-based URI route matching."
        },
        {
          heading: "4. Computer Vision: CameraX (Core, Camera2, Lifecycle, View)",
          beginner: "CameraX is Android's camera library that works reliably across thousands of different phone models, managing focus, flash, and orientation automatically.",
          panel: "Libraries: androidx.camera:camera-core, camera-camera2, camera-lifecycle, camera-view. Bindings: ProcessCameraProvider.bindToLifecycle() attaches Preview and ImageCapture or ImageAnalysis to the lifecycle of the composable. Supports hardware torch toggling via camera.cameraControl.enableTorch() and background image buffer delivery."
        },
        {
          heading: "5. On-Device AI/Vision: Google ML Kit Text Recognition & Barcode Scanning",
          beginner: "ML Kit runs fast machine learning models directly on your phone hardware. It reads barcodes in milliseconds and recognizes English text from photos without needing an internet connection.",
          panel: "Libraries: play-services-mlkit-text-recognition:19.0.0 (Latin alphabet OCR) and barcode-scanning:17.3.0. Execution: On-device inference. Text recognition processes InputImage.fromBitmap(scaledBitmap, 0) synchronously via Tasks.await(task) on background dispatchers. Barcode scanning analyzes live YUV_420_888 frame proxies via ImageAnalysis.Analyzer with zero network latency."
        },
        {
          heading: "6. Networking & HTTP Client: Square OkHttp 3",
          beginner: "OkHttp is the high-performance internet engine that sends API requests to Open Food Facts and the AI servers and gets back responses.",
          panel: "Library: com.squareup.okhttp3:okhttp. Configuration: Dedicated connection pool with 30-second connect, read, and write timeouts. Handles multipart JSON streaming, asynchronous call enqueueing, and automatic gzip compression."
        },
        {
          heading: "7. Data Persistence: SharedPreferences + Google Gson",
          beginner: "We store user profiles, saved favorites, and scan history directly on the phone using lightweight SharedPreferences and Gson. No heavy, complex SQL database is needed for local shopping data.",
          panel: "Storage: Android SharedPreferences (purecheck_prefs, Context.MODE_PRIVATE) coupled with Google Gson. Why no Room/SQLite? For a consumer utility caching max 50 recent scans, user biometric flags, and shopping items, SharedPreferences provides zero-migration schema agility, instant sub-millisecond synchronous reads, and avoids Room annotation processing (KSP) compilation overhead. Complex lists are serialized into JSON arrays using TypeToken<List<T>>."
        },
        {
          heading: "8. External Cloud Generative AI Ecosystem",
          beginner: "NutriCheck supports four major AI providers: Google Gemini 2.0 Flash (for multimodal image + text reasoning), Groq (for blazing fast LLaMA 3.3 inference), DeepSeek-V3, and Bytez.",
          panel: "Orchestration: GeminiClient.kt implements a dynamic multi-provider routing matrix. Gemini uses generativelanguage.googleapis.com with responseMimeType: 'application/json'. Groq and DeepSeek use OpenAI-compatible /chat/completions endpoints with candidate model fallback chains (e.g., llama-3.3-70b-versatile -> llama-3.1-8b-instant -> qwen3.6-27b) to guarantee 99.9% uptime against quota exhaustion."
        }
      ]
    },
    {
      id: "module-5",
      number: "05",
      title: "Complete System Architecture",
      shortDesc: "End-to-end multi-tier architectural diagrams, component relationships, and communication topologies.",
      badge: "System Design",
      sections: [
        {
          heading: "1. High-Level Architectural Layers",
          beginner: "NutriCheck is built like a secure 5-story building: Floor 1 is the Screen you touch (Compose UI). Floor 2 is Navigation (Nav3). Floor 3 is the Local Eyes (CameraX + ML Kit OCR/Barcode). Floor 4 is the Mathematical Brain (RulesEngine). Floor 5 is the Cloud Assistant (Open Food Facts + LLM APIs).",
          panel: "Tier 1: Presentation Layer (Jetpack Compose, PureCheckTheme, Material3). Tier 2: Controller & Navigation Layer (Navigation 3 backstack, NavDisplay, entryProvider). Tier 3: Edge Sensing Layer (CameraX, ML Kit Vision, ImageAnalysis, Task Await). Tier 4: Domain & Business Logic Layer (RulesEngine.kt, IngredientDatabase.kt, UserProfile evaluator). Tier 5: Persistence Layer (PureCheckPreferences, SharedPreferences, Gson). Tier 6: Integration Layer (OkHttp3, Open Food Facts v2 REST API, Gemini 2.0 Flash, Groq, DeepSeek)."
        },
        {
          heading: "2. The Zero-Hallucination Pipeline Topology",
          beginner: "Here is the golden rule of NutriCheck: The score is NEVER decided by AI. The score is calculated by pure math on your phone first. The score is then passed to the AI as an unchangeable fact, and the AI is commanded to only explain that score.",
          panel: "Topological Sequence: Raw Image/Barcode -> Edge Vision -> Raw Normalized Text -> Local Deterministic Rules Engine -> Immutable DeterministicResult(overallScore, breakdown, matchedAdditives, conflicts) -> Injected into Prompt Context Payload -> Dispatched via OkHttp -> Cloud LLM Generative Formulation -> Structured JSON Output -> Strict JSON Sanitization -> UI Display & History Log."
        },
        {
          heading: "3. Component Interaction Diagram",
          beginner: "When you tap 'Scan', MainActivity triggers Navigation, which mounts ScannerScreen. CameraX takes a picture, ML Kit extracts text, RulesEngine calculates the score, GeminiClient calls Google's server, and ResultScreen renders the final cards.",
          panel: "MainActivity -> MainNavigation (Navigation.kt) -> ScannerScreen -> CameraX ImageCapture -> captureImage() -> resizeBitmap() -> ML Kit TextRecognition -> GeminiClient.analyzeIngredients() -> RulesEngine.calculate() -> OkHttp Request -> Gemini 2.0 Flash -> Callback -> ResultScreen -> PureCheckPreferences.addHistoryItem()."
        }
      ]
    },
    {
      id: "module-6",
      number: "06",
      title: "Complete Application Workflows & Math Formulas",
      shortDesc: "Step-by-step pipeline flows with the exact mathematical formulas and worked calculation examples from source code.",
      badge: "Core Algorithm",
      sections: [
        {
          heading: "1. The 5-Axis Deterministic Scoring Formula",
          beginner: "NutriCheck calculates five sub-scores from 0 to 100, then averages them to find your final overall score. Every single deduction is based on scientific evidence.",
          panel: "Mathematical Formulation in RulesEngine.kt: Overall Score S_overall is the arithmetic mean of five clamped dimensions D_i in [10, 100]:\nS_overall = (Q_quality + A_additives + N_nutrition + S_sugar + Na_sodium) / 5\n\nGrade Allocation Function G(S):\n- G(S) = 'A' if S >= 80\n- G(S) = 'B' if 68 <= S < 80\n- G(S) = 'C' if 52 <= S < 68\n- G(S) = 'D' if 38 <= S < 52\n- G(S) = 'F' if S < 38"
        },
        {
          heading: "2. Dimension-by-Dimension Mathematical Rules",
          beginner: "Here is exactly how each dimension is calculated:\n1) Quality: Starts at 100. High-risk chemicals deduct 15, Medium deducts 8, Low deducts 4. Hidden palm oil deducts 10.\n2) Additives: Starts at 100. Each chemical dye or preservative deducts 8 points.\n3) Sugar: Starts at 100. Every gram of sugar above 5g deducts 4.5 points!\n4) Sodium: Starts at 100. Every milligram of sodium above 140mg deducts 0.15 points!\n5) Nutrition: Starts at 100. High calories deduct 15, high protein adds 10, high fiber adds 10, saturated fat deducts 15. Then it is averaged with sugar and sodium.",
          panel: "Exact Equations:\n1. Q_quality = max(10, min(100, 100 - sum(d_additive) - delta_oil - delta_msg))\n   where d_additive in {High: 15, Medium: 8, Low: 4, None: 2}, delta_oil = 10 (vegetable/refined oil), delta_msg = 8 (yeast extract/hydrolyzed protein).\n2. A_additives = max(10, min(100, 100 - (count_additives * 8) - sum(delta_high_preservative)))\n   where delta_high_preservative = 5.\n3. S_sugar:\n   If sugarGrams > 5g: S_sugar = max(10, min(100, 100 - (sugarGrams - 5.0) * 4.5))\n   Else if keyword 'sugar': 75; if keyword 'corn syrup': 60.\n4. Na_sodium:\n   If sodiumMg > 140mg: Na_sodium = max(10, min(100, 100 - (sodiumMg - 140.0) * 0.15))\n   Else if keyword 'salt': 75.\n5. N_nutrition:\n   N_raw = 100 - (15 if cal > 350) + (10 if prot > 8g) + (10 if fiber > 3g) - (15 if satFat > 5g)\n   N_blended = max(10, min(100, round((N_raw + S_sugar + Na_sodium) / 3)))"
        },
        {
          heading: "3. Worked Real-World Example (Packaged Hazelnut Chocolate Spread)",
          beginner: "Let's test a real jar of chocolate spread with:\n- Sugar: 56g per 100g\n- Sodium: 40mg\n- Ingredients: Sugar, Palm Oil, Hazelnuts, Cocoa, Skimmed Milk Powder, Lecithin (E322), Vanillin.\nLet's see why it fails with a low score!",
          panel: "Manual Calculation Verification:\n1. Additives Matched: Palm Oil (High evidence fat), Lecithin E322 (Emulsifier).\n2. Quality: 100 - 15 (Palm Oil) - 10 (Refined Oil heuristic) = 75.\n3. Additives: 100 - (1 * 8) = 92.\n4. Sugar: sugarGrams = 56g (>5g). Excess = 51g. Deduction = 51 * 4.5 = 229.5 points! Score = max(10, 100 - 229.5) = 10.\n5. Sodium: sodiumMg = 40mg (<=140mg threshold). Deduction = 0. Score = 100.\n6. Nutrition: Calories = 540 (>350 -> -15), Protein = 6g (<8g -> 0), SatFat = 11g (>5g -> -15). N_raw = 100 - 15 - 15 = 70. N_blended = round((70 + 10 + 100) / 3) = 60.\n7. Overall Score S = round((75 + 92 + 60 + 10 + 100) / 5) = round(337 / 5) = 67. Grade = 'C' (just 1 point below B: 68).\n8. Profile Conflicts (if Diabetic enabled): sugar = 56g > 10g -> Profile Score = 67 - 20 = 47 ('✕ DIABETIC CONFLICT')."
        }
      ]
    },
    {
      id: "module-7",
      number: "07",
      title: "API Deep Dive & Cloud Integration",
      shortDesc: "Complete specification of Open Food Facts v2 REST API and Multi-LLM provider endpoints, schemas, and security.",
      badge: "API Contracts",
      sections: [
        {
          heading: "1. Open Food Facts v2 Product Query API",
          beginner: "When you scan a barcode (like 8901030383701), NutriCheck asks Open Food Facts for that product's information. It receives back a clean JSON document with the brand name, full ingredients list, and 100g nutritional breakdown.",
          panel: "Provider: Open Food Facts Foundation (France / International). Protocol: HTTP/1.1 REST GET. Endpoint: https://world.openfoodfacts.org/api/v2/product/{barcode}.json. Header: User-Agent: PureCheck - Android - Version 2.0 - CSE Major Project. Status Codes: 200 OK (status=1 product found; status=0 not in catalogue), 404 Not Found. Extracted JSON Nodes: product.product_name, product.brands, product.ingredients_text, product.nutriments.energy-kcal_100g, product.nutriments.sugars_100g, product.nutriments.proteins_100g, product.nutriments.sodium_100g."
        },
        {
          heading: "2. Google Gemini 2.0 Flash GenerateContent API",
          beginner: "We send the ingredient photo (encoded as Base64 text) along with our pre-calculated scores to Google's Gemini server. Gemini inspects the image and returns a structured JSON summary matching our exact numbers.",
          panel: "Provider: Google AI Studio / Cloud Vertex. Protocol: HTTPS POST. Endpoint: https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key={API_KEY}. Body Payload: contents[].parts[] containing text (systemInstruction + deterministicContext) and inlineData (mimeType: image/jpeg, data: base64Image). Configuration: generationConfig: { responseMimeType: 'application/json' }. Extracted Node: candidates[0].content.parts[0].text."
        },
        {
          heading: "3. Groq Cloud Fast Inference API",
          beginner: "Groq is a specialized AI cloud provider that runs open-source models like Meta LLaMA 3.3 at lightning speeds (hundreds of words per second).",
          panel: "Provider: Groq Inc. Protocol: HTTPS POST. Endpoint: https://api.groq.com/openai/v1/chat/completions. Headers: Authorization: Bearer {API_KEY}. Candidate Fallback Sequence: llama-3.3-70b-versatile -> llama-3.1-8b-instant -> llama-3.3-70b-specdec -> qwen/qwen3.6-27b. Response Format: { type: 'json_object' }."
        },
        {
          heading: "4. DeepSeek-V3 Chat API",
          beginner: "DeepSeek is an advanced reasoning and language model integrated for conversational questions and detailed biochemical analysis.",
          panel: "Provider: DeepSeek AI. Protocol: HTTPS POST. Endpoint: https://api.deepseek.com/chat/completions. Header: Authorization: Bearer {API_KEY}. Model: deepseek-chat. Invoked in GeminiClient.discussResult() for multi-turn inspector dialogue."
        },
        {
          heading: "5. Security & Credential Isolation Architecture",
          beginner: "Important: NutriCheck NEVER hardcodes secret API keys into the source code or app files! Users enter their own keys into the app Settings, and they are saved only on their personal device.",
          panel: "Security Posture: Adheres to OWASP Mobile Security guidelines. API keys are injected at runtime via PureCheckPreferences, stored in private mode SharedPreferences inaccessible to external apps on non-rooted devices. Git repository contains zero private API secrets or bearer tokens."
        }
      ]
    },
    {
      id: "module-8",
      number: "08",
      title: "Database & Local Persistence Architecture",
      shortDesc: "Complete storage architecture, data models, serialization workflows, and rationale for SharedPreferences over SQLite/Room.",
      badge: "Data Storage",
      sections: [
        {
          heading: "1. Storage Technology Selection Rationale",
          beginner: "Why didn't we use an SQLite database? For our app, SharedPreferences is faster, lighter, has zero setup overhead, and stores our JSON history without complex SQL migration headaches.",
          panel: "Architectural Justification: SharedPreferences (backed by private XML /data/data/com.example.purecheck/shared_prefs/purecheck_prefs.xml) was deliberately selected over Room ORM / SQLite. NutriCheck's offline persistence scope consists of: 1) A single UserProfile state record. 2) A FIFO queue of max 50 HistoryItem records. 3) A moderate list of FavoriteProduct objects. 4) Shopping list items. Storing structured polymorphic JSON strings via Google Gson avoids table schema migrations when LLM response fields evolve, achieves instantaneous synchronous reads upon application start, and eliminates Room compile-time KSP code generation overhead."
        },
        {
          heading: "2. The 5 Core Data Entities",
          beginner: "Our app stores five main types of information: 1) User Profile (name, height, weight, dietary flags). 2) History Item (saved scans with full JSON). 3) Favorite Product (bookmarked healthy items). 4) Shopping Item (store checklist). 5) Watchlist (custom forbidden words).",
          panel: "Formal Kotlin Data Models in PureCheckPreferences.kt:\n1. UserProfile(name, age, gender, height, weight, gutHealth, heartHealth, vegan, vegetarian, diabetic, cosmeticSafe, lowSugar, lowSodium, highProtein, cleanIngredients)\n2. HistoryItem(id: UUID, productName, overallGrade, overallScore, summary, jsonResult: String, timestamp: Long)\n3. FavoriteProduct(id, productName, overallGrade, overallScore, calories, protein, jsonResult, timestamp)\n4. ShoppingItem(id, name, checked: Boolean, score: Int?, grade: String?, scanId: String?)\n5. Watchlist: List<String> serialized as JSON array."
        },
        {
          heading: "3. FIFO Scan History Eviction Policy",
          beginner: "To keep the phone running fast and prevent memory overload, NutriCheck keeps your last 50 scans. When you scan the 51st item, the oldest scan is automatically removed.",
          panel: "Implementation in PureCheckPreferences.addHistoryItem():\nval history = getHistory().toMutableList()\nhistory.removeAll { it.id == item.id }\nhistory.add(0, item) // Prepend newest to top\nif (history.size > 50) history.removeAt(history.size - 1) // Enforce FIFO bound\nprefs.edit().putString('scan_history', gson.toJson(history)).apply()"
        }
      ]
    },
    {
      id: "module-9",
      number: "09",
      title: "Interactive Source Code Explorer",
      shortDesc: "Comprehensive directory tree, file-to-feature mapping, and line-by-line breakdown of every critical source file.",
      badge: "Code Audit",
      sections: [
        {
          heading: "1. Repository Architecture Tree",
          beginner: "All our code lives under `app/src/main/java/com/example/purecheck/`. It is neatly organized into `data/` for logic and storage, `ui/` for screens and components, `theme/` for styling, and `utils/` for API communication.",
          panel: "Directory Structure:\napp/src/main/java/com/example/purecheck/\n├── MainActivity.kt (App entry, edge-to-edge Compose host)\n├── Navigation.kt (NavDisplay, backstack, Nav3 routing)\n├── NavigationKeys.kt (NavKey serializable destination sealed objects)\n├── data/\n│   ├── RulesEngine.kt (Deterministic 5-axis math engine & profile conflict evaluator)\n│   ├── IngredientDatabase.kt (In-memory catalogue of 17+ hazardous chemical additives)\n│   └── PureCheckPreferences.kt (SharedPreferences + Gson persistence & UserProfile)\n├── theme/\n│   ├── Color.kt, Theme.kt, Type.kt (Emerald dark theme design system)\n├── ui/\n│   ├── main/ (MainAppContainer.kt, MainScreen.kt)\n│   ├── home/ (HomeScreen.kt)\n│   ├── scanner/ (ScannerScreen.kt, BarcodeScannerScreen.kt, ScanOptionsScreen.kt)\n│   ├── result/ (ResultScreen.kt)\n│   ├── profile/ (ProfileScreen.kt, MyProfileScreen.kt)\n│   ├── favorites/ (FavoritesScreen.kt)\n│   ├── settings/ (SettingsScreen.kt)\n│   ├── author/ (AuthorScreen.kt)\n│   └── components/ (PureCheckComponents.kt)\n└── utils/\n    └── GeminiClient.kt (Multi-LLM OkHttp orchestrator for Gemini, Groq, DeepSeek, Bytez)"
        },
        {
          heading: "2. Key File Responsibilities & Interactions",
          beginner: "Here is how the files talk to each other: MainActivity opens Navigation. Navigation opens MainAppContainer. When you tap Scan, ScannerScreen takes a photo, calls GeminiClient, which runs RulesEngine, queries the AI, and hands the result to ResultScreen.",
          panel: "Source Call Graph:\nMainActivity.kt -> MainNavigation() in Navigation.kt -> MainAppContainer.kt -> HomeScreen / ScanOptionsScreen / ProfileScreen.\nScannerScreen.kt -> CameraX ImageCapture -> ML Kit TextRecognition -> GeminiClient.kt -> RulesEngine.kt -> OkHttp -> Gemini 2.0 Flash -> Navigation3 backstack.add(Result(jsonResult)) -> ResultScreen.kt."
        }
      ]
    },
    {
      id: "module-10",
      number: "10",
      title: "Data Flow & Information Lifecycle",
      shortDesc: "End-to-end data lifecycle from photon capture on camera sensor to JSON serialization and UI rendering.",
      badge: "Data Pipelines",
      sections: [
        {
          heading: "1. Food Label Scanning Data Flow",
          beginner: "From camera photon to final score: 1) Camera sensor captures image. 2) Image compressed to 768px JPEG. 3) ML Kit OCR turns pixels into raw text string. 4) Text passed into RulesEngine. 5) RulesEngine computes scores and flags additives. 6) Prompt sent to Gemini server. 7) JSON returned and parsed. 8) Result card rendered.",
          panel: "Pipeline Steps: 1) Camera Sensor Photons -> 2) Bitmap Capture -> 3) resizeBitmap(768) JPEG 70% -> 4) Base64 String & ML Kit TextRecognition -> 5) Raw Extracted Text -> 6) RulesEngine.calculate(text, profile) -> 7) Injected DeterministicContext -> 8) OkHttp POST to GenerativeLanguage API -> 9) Response Sanitization sanitizeJson() -> 10) HistoryItem Persistence -> 11) Compose ResultScreen Recomposition."
        },
        {
          heading: "2. Barcode Scanning Data Flow",
          beginner: "From barcode lines to product audit: 1) Camera reads lines in real-time. 2) ML Kit extracts 13-digit EAN number. 3) OkHttp queries Open Food Facts. 4) JSON nutrition and ingredients parsed. 5) Local RulesEngine computes exact score. 6) Report displayed instantly without calling any LLM!",
          panel: "Pipeline Steps: 1) CameraX ImageAnalysis -> 2) ML Kit BarcodeScanning -> 3) Clean Numeric Digits -> 4) OkHttp GET to Open Food Facts v2 -> 5) JSON Parsing (nutriments + ingredients_text) -> 6) Synchronous RulesEngine.calculate() -> 7) Direct Synthesis into Audit JSON Schema -> 8) PureCheckPreferences.addHistoryItem() -> 9) Instantaneous UI Navigation to ResultScreen."
        }
      ]
    },
    {
      id: "module-11",
      number: "11",
      title: "AI, Machine Learning & OCR Implementation",
      shortDesc: "Rigorous distinction between on-device Computer Vision, rule-based expert logic, and cloud Generative LLMs.",
      badge: "AI/ML Science",
      sections: [
        {
          heading: "1. The Three Distinct AI/ML Technologies in NutriCheck",
          beginner: "Be careful in your presentation! Do NOT say 'the AI calculated the score'. NutriCheck uses three distinct tools: 1) On-device ML Kit OCR for seeing text. 2) Local Deterministic Rules Engine for calculating scores. 3) Cloud LLM (Gemini) for explaining the findings.",
          panel: "Academic Clarification of AI Taxonomy:\n1. On-Device Discriminative Computer Vision: Google Play Services ML Kit uses quantized convolutional and recurrent neural networks (CNN-RNN / MobileNet-style architectures) running locally on Android Neural Networks API (NNAPI) / GPU delegates to perform character boundary detection and text classification.\n2. Expert Rules Engine (Deterministic Domain Logic): RulesEngine.kt is NOT machine learning; it is a deterministic expert rule system executing mathematical penalty equations, normalized string matching, and threshold logic.\n3. Cloud Generative Foundation Models: Google Gemini 2.0 Flash / Groq LLaMA 3.3 are multi-billion parameter autoregressive transformer models deployed via cloud APIs strictly for natural language synthesis, semantic summarization, and query reasoning."
        },
        {
          heading: "2. Text Preprocessing & Character Recognition Pipeline",
          beginner: "Food packages are curved, crinkled, and have reflective glare. To get accurate text, we downscale the photo to 768 pixels, compress it to 70% quality, and feed it into Google's Latin OCR engine.",
          panel: "Image Transformation Routine in ScannerScreen.kt:\n- Bitmap loaded via BitmapFactory.decodeFile().\n- Aspect ratio preserved while constraining max dimension to 768px via resizeBitmap().\n- Quality compressed to 70% JPEG to eliminate high-frequency camera sensor noise while retaining crisp ingredient font edges.\n- Processed synchronously via Tasks.await(recognizer.process(InputImage.fromBitmap(scaledBitmap, 0)))."
        }
      ]
    },
    {
      id: "module-12",
      number: "12",
      title: "Android Native Engineering & Build System",
      shortDesc: "Deep dive into AndroidManifest, runtime permissions, Gradle 8 Kotlin DSL, compileSdk 36, and APK generation.",
      badge: "Android Engineering",
      sections: [
        {
          heading: "1. Android Permissions & Hardware Requirements",
          beginner: "NutriCheck requires two permissions: CAMERA (to snap ingredient photos and read barcodes) and INTERNET (to query Open Food Facts and Gemini AI). If the user denies camera permission, the app shows a helpful message explaining why it is needed.",
          panel: "Manifest Configuration (AndroidManifest.xml):\n- <uses-permission android:name='android.permission.INTERNET' />\n- <uses-permission android:name='android.permission.CAMERA' />\n- <uses-feature android:name='android.hardware.camera' android:required='true' />\nRuntime Security: Uses rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) in Compose. If ContextCompat.checkSelfPermission() returns PERMISSION_DENIED, renders an informative fallback composable prompting the user to grant access."
        },
        {
          heading: "2. Build Configuration & Toolchain",
          beginner: "We use Gradle with Kotlin Script (`build.gradle.kts`). We target Android SDK 36 (the newest Android version) with a minimum requirement of SDK 24 (Android 7.0), meaning NutriCheck runs on over 95% of all active Android devices in the world.",
          panel: "Configuration Parameters in app/build.gradle.kts:\n- namespace = 'com.example.purecheck'\n- compileSdk = 36, minSdk = 24, targetSdk = 36\n- versionCode = 1, versionName = '1.0'\n- Java / Kotlin compatibility: JavaVersion.VERSION_17, jvmToolchain(17)\n- Packaging: excludes += '/META-INF/{AL2.0,LGPL2.1}' to prevent duplicate license merge errors."
        },
        {
          heading: "3. Generating and Installing the Release APK",
          beginner: "To install the app on a physical phone for your college presentation, you run `./gradlew assembleRelease` or `./gradlew assembleDebug` in the terminal. The generated APK file can be directly transferred and installed.",
          panel: "Terminal Commands:\n- Generate Debug APK: `./gradlew assembleDebug` (output at `app/build/outputs/apk/debug/app-debug.apk`)\n- Generate Release APK: `./gradlew assembleRelease` (output at `app/build/outputs/apk/release/app-release-unsigned.apk`)\n- Direct Install via ADB: `adb install -r app/build/outputs/apk/debug/app-debug.apk`\n- View Live Logs: `adb logcat -s PureCheck:V GeminiClient:V`"
        }
      ]
    },
    {
      id: "module-13",
      number: "13",
      title: "Testing, Debugging & Quality Assurance",
      shortDesc: "Unit testing strategies, ViewModel tests, Logcat debugging, network inspection, and live verification checklists.",
      badge: "Quality Assurance",
      sections: [
        {
          heading: "1. Unit Testing the Deterministic Rules Engine",
          beginner: "Before trusting our scoring formula, we test it with known food products to ensure that dangerous ingredients always trigger deductions and clean foods get Grade A.",
          panel: "Testing Framework: JUnit4 and kotlinx-coroutines-test (configured in app/build.gradle.kts). Test targets: MainScreenViewModelTest.kt and RulesEngine unit tests verifying: 1) High-evidence additives deduct exactly 15 points. 2) Sugar exceeding 5g deducts at 4.5 pts/g. 3) Grade boundaries strictly follow thresholds (80/68/52/38). 4) Profile conflict penalties apply without altering the general overall score."
        },
        {
          heading: "2. Practical Pre-Presentation Verification Checklist",
          beginner: "Before walking into your project evaluation, verify these 5 things: 1) Camera works and torch toggles. 2) Test product barcodes exist in Open Food Facts. 3) Valid Gemini or Groq API key is saved in Settings. 4) Internet connection is active. 5) Profile preferences (like Vegan or Diabetic) trigger warnings on appropriate items.",
          panel: "Pre-Evaluation System Health Check:\n1. Connectivity: Verify DNS resolution for world.openfoodfacts.org and generativelanguage.googleapis.com.\n2. API Quota: Ensure API key in SettingsScreen has remaining token quota.\n3. Sample Artifacts: Prepare 2 benchmark physical packages: one clean product (e.g., pure oats or green tea -> Grade A) and one ultra-processed product (e.g., packaged noodles or soda with E102, HFCS, Palm Oil -> Grade D/F with dietary conflicts).\n4. Backstack State: Verify back button cleanly navigates from ResultScreen back to MainAppContainer without crashing."
        }
      ]
    },
    {
      id: "module-14",
      number: "14",
      title: "Limitations & Future Roadmap",
      shortDesc: "Honest technical limitations, edge cases, catalog gaps, and strategic future engineering enhancements.",
      badge: "Future Scope",
      sections: [
        {
          heading: "1. Real Technical Limitations of Current Version",
          beginner: "Every real software project has honest limitations: 1) If an ingredient label is crumpled, blurry, or low-contrast, OCR might miss words. 2) Local street foods or unbranded snacks aren't in Open Food Facts. 3) Ingredients in Indian regional languages (Hindi, Telugu, Tamil) aren't supported by the Latin OCR model yet. 4) The app requires an internet connection for the AI chat.",
          panel: "Systemic Constraints & Technical Debt:\n1. Optical Degradation: ML Kit Latin text recognition accuracy drops significantly on cylindrical curved packaging, reflective cellophane wraps, and low-contrast metallic fonts.\n2. Open Catalogue Density: Open Food Facts has extensive coverage in Western Europe and North America, but regional tier-2 and tier-3 Indian packaged food products frequently return status=0 (unregistered).\n3. Linguistic Range: The current text model is configured strictly for Latin script; regional Indic scripts (Devanagari, Telugu, Tamil) require multi-engine Indic OCR pipelines.\n4. Heuristic Decoupling: In the absence of explicit quantitative gram numbers on OCR scans, sugar and sodium fall back to keyword heuristics (75/60 pts)."
        },
        {
          heading: "2. Strategic Future Engineering Enhancements (Part 2)",
          beginner: "What we plan to build next: 1) Dedicated side-by-side comparison screen to compare two brands directly. 2) Offline Tiny-LLM running on the phone (like Gemma 2B or SmolLM). 3) Multi-lingual OCR for regional Indian languages. 4) Crowdsourced barcode uploading so users can register new products.",
          panel: "Part 2 Roadmap:\n1. True Offline Edge LLM: Quantizing and embedding a 2B parameter on-device LLM (Gemma-2-2B-IT or LLaMA-3.2-1B via MediaPipe GenAI Tasks) for 100% offline natural language explanation.\n2. Side-by-Side Dual Product Comparison Screen: Real-time visual diffing of two conflicting products on a single dual-pane Compose canvas.\n3. Indian FSSAI Catalogue Integration: Building a custom web crawler and scraper for the Food Safety and Standards Authority of India (FSSAI) product approval registry.\n4. Computer Vision Image Segmentation: Preprocessing labels with OpenCV contour rectification to automatically unwrap cylindrical cans into flat rectangular text planes."
        }
      ]
    },
    {
      id: "module-15",
      number: "15",
      title: "Project Presentation & Defense Master Guide",
      shortDesc: "Complete presentation speeches: 2-minute elevator pitch, 5-minute technical review, 10-minute major defense, and live demo sequence.",
      badge: "Oral Defense",
      sections: [
        {
          heading: "1. The 2-Minute Elevator Pitch (Memorize This!)",
          beginner: "Good morning respected guide and panel members. Today, consumers are buying packaged foods with zero real awareness of what they are consuming. Modern food labels are deliberately designed with chemical aliases, E-numbers, and misleading claims like 'Natural' or 'Zero Sugar' that hide harmful palm oils, synthetic dyes, and endocrine disruptors.\n\nTo solve this, my teammate Uday Bhaskar and I developed NutriCheck—an AI-powered food intelligence platform built in native Kotlin and Jetpack Compose. NutriCheck provides a dual-input scanning experience: users can scan any barcode for an instant database lookup, or photograph any printed ingredient label using our on-device ML Kit OCR scanner.\n\nUnlike traditional apps that either hallucinate scores or rely on static tables, NutriCheck uses a hybrid architecture: a local, transparent mathematical Rules Engine calculates five distinct health scores—Quality, Additives, Sugar, Sodium, and Nutrition—and personalizes them against user biometric health profiles (like Diabetes, Hypertension, or Veganism). Then, we use Cloud LLMs like Gemini 2.0 Flash and Groq LLaMA-3.3 strictly to explain the audit in simple language and provide healthy product alternatives.\n\nNutriCheck turns confusing chemical lists into clear, actionable health decisions in less than two seconds.",
          panel: "Pitch Focus Points: 1) Problem of asymmetric label transparency. 2) Native Android + Kotlin + Jetpack Compose stack. 3) Dual-input pipeline (CameraX + ML Kit Barcode & Text OCR). 4) Zero-hallucination trust boundary: local deterministic math + Cloud LLM explanatory contextualization. 5) Real-world personalization for non-communicable lifestyle diseases."
        },
        {
          heading: "2. The 5-Minute Technical Guide Review Speech",
          beginner: "Walk through the project step by step: 1) Problem statement & academic justification. 2) Architecture (Presentation, Navigation 3, Edge Vision, Domain Engine, Multi-LLM API). 3) The mathematical formula in RulesEngine.kt. 4) The live demo showing a high-sugar item getting Grade D with a Diabetic conflict warning. 5) Engineering constraints and future enhancements.",
          panel: "Structure: 60s Problem & Ingestion -> 90s Edge Vision & ML Kit OCR -> 90s Rules Engine Mathematical Derivation & Zero-Hallucination Protocol -> 60s Multi-LLM Fallback Matrix & Jetpack Compose UI."
        },
        {
          heading: "3. The 10-Minute Major Project Final Defense (Slide-by-Slide Guide)",
          beginner: "Slide 1: Title & Authors (V. V. Karthik Barrenkala, M. Uday Bhaskar, RGUKT CSE).\nSlide 2: Motivation & The Industrial Food Crisis.\nSlide 3: Existing Systems vs NutriCheck (Table comparing Nutri-Score, Yuka, and NutriCheck).\nSlide 4: System Architecture Diagram.\nSlide 5: Edge Vision & CameraX Pipeline.\nSlide 6: On-Device OCR vs Barcode Pipeline.\nSlide 7: The Deterministic Scoring Engine (Mathematical Equations & Proofs).\nSlide 8: Personalization Engine & Profile Conflicts.\nSlide 9: Multi-LLM Orchestration & Security.\nSlide 10: Live Application Demo.\nSlide 11: Testing & QA Results.\nSlide 12: Limitations, Future Scope & Conclusion.",
          panel: "Defense Strategy: Emphasize that generative AI was NOT trusted with numerical scoring. External examiners love to grill students on 'How do you prevent the LLM from making up scores?' Your answer: 'Sir, our architecture enforces a strict trust boundary: the score is computed locally in RulesEngine.kt via deterministic arithmetic, and injected as an immutable constraint into the prompt payload.'"
        },
        {
          heading: "4. Step-by-Step Live Demonstration Script",
          beginner: "Follow this exact sequence in front of your panel:\nStep 1: Open Home Dashboard. Show average score and recent scans.\nStep 2: Tap Profile. Show your Biometrics (BMI 22.9) and enable 'Vegan' and 'Diabetic' toggles.\nStep 3: Tap Scan -> 'Scan Barcode'. Point at a registered product. Show instant Open Food Facts lookup.\nStep 4: Tap Scan -> 'Scan Label'. Point at a processed food label. Watch the green laser sweep and snap.\nStep 5: View the Audit Report. Point out the Grade (e.g. 'D'), the 5-axis score breakdown, the unmasked Palm Oil, and the bright red '✕ DIABETIC CONFLICT' warning.\nStep 6: Open the AI Inspector Chat. Switch to 'Scientific Mode' and send: 'Explain the metabolic impact of this sugar content.' Show the structured bullet points.\nStep 7: Tap 'Save to Favorites' and show it appearing in the Favorites list.",
          panel: "Demonstration Sequence: 1) State initialization in ProfileScreen. 2) Barcode real-time acquisition. 3) CameraX OCR snapshot & ML Kit parsing. 4) Audit report rendering with scaled serving portion. 5) Dual-mode inspector chat interaction. 6) Local persistence verification in Favorites."
        }
      ]
    },
    {
      id: "module-16",
      number: "16",
      title: "Technical Viva Preparation (65+ Questions)",
      shortDesc: "Comprehensive database of 65+ college viva and external examiner questions with code-grounded answers.",
      badge: "Viva Examination",
      sections: [
        {
          heading: "Viva Examination Center",
          beginner: "This section contains over 65 questions divided into 12 categories that external examiners love to ask during project evaluations. Click any question to reveal the beginner explanation, the panel-level technical defense, and the examiner's trap follow-up!",
          panel: "Categorized into: 1. Project Fundamentals, 2. System Architecture, 3. Jetpack Compose & Navigation, 4. CameraX & Vision, 5. ML Kit OCR & Barcodes, 6. Scoring Algorithm & Mathematics, 7. Multi-LLM Orchestration, 8. APIs & Networking, 9. Database & Persistence, 10. Android Build & Gradle, 11. Testing & Debugging, 12. Limitations & Future Scope."
        }
      ]
    },
    {
      id: "module-17",
      number: "17",
      title: "Technical Terminology Glossary",
      shortDesc: "Complete A-to-Z dictionary of all technical, biochemical, and Android concepts used in NutriCheck.",
      badge: "Glossary",
      sections: [
        {
          heading: "NutriCheck Technical Lexicon",
          beginner: "Browse through definitions of all major terms—from E-numbers and Carrageenan to CameraX, Base64, and LLM temperature.",
          panel: "Searchable reference containing over 40 formal software engineering, computer vision, and biochemical definitions."
        }
      ]
    }
  ],

  vivaQuestions: [
    // Category 1: Project Fundamentals
    {
      id: "viva-1",
      category: "Project Fundamentals",
      question: "What is the core problem NutriCheck solves that barcode-only apps cannot?",
      beginner: "Barcode-only apps fail whenever a product is new, local, or unlisted in databases like Open Food Facts. NutriCheck has an on-device OCR camera scanner that reads the actual printed ingredients directly off any package in real time.",
      panel: "NutriCheck resolves the catalogue incompleteness problem of traditional GTIN/EAN barcode lookups by implementing a dual-input ingestion pipeline. When barcode matching returns HTTP 404, the user seamlessly falls back to on-device ML Kit OCR, extracting raw ingredient text directly from physical packaging.",
      trap: "Examiner follow-up: 'What if the packaging is crumpled or reflective?' Answer: 'In that scenario, our UI allows switching the viewfinder aspect ratio, toggling the hardware LED flash via CameraControl.enableTorch(), or manually entering the barcode in ScanOptionsScreen.'"
    },
    {
      id: "viva-2",
      category: "Project Fundamentals",
      question: "Why is NutriCheck called a 'Food Intelligence Platform' instead of just a calorie counter?",
      beginner: "A calorie counter only tracks how much you eat. NutriCheck analyzes WHAT you eat—unmasking dangerous chemical additives, artificial sweeteners, hidden palm oils, and checking whether ingredients clash with your specific medical conditions.",
      panel: "Traditional fitness trackers treat food purely as energy intake (calories and macronutrients). NutriCheck functions as a qualitative biochemical auditor: it evaluates food processing intensity, identifies endocrine-disrupting additives (like BHA/BHT), unmasks obfuscated ingredient nomenclature, and evaluates personalized metabolic contraindications.",
      trap: "Examiner follow-up: 'Do you track daily calories?' Answer: 'We provide per-portion scaled macronutrient audits in ResultScreen and store biometric BMI profiles, while dedicating full focus to ingredient safety and additive auditing.'"
    },
    {
      id: "viva-3",
      category: "Project Fundamentals",
      question: "What is an E-number and why does NutriCheck flag them?",
      beginner: "E-numbers are standard codes used by food manufacturers for additives like food colorings, preservatives, and emulsifiers. Some are harmless, but others—like E102 (Tartrazine) or E211 (Sodium Benzoate)—are linked to hyperactivity, gut inflammation, or allergic reactions.",
      panel: "E-numbers represent the European Community alphanumeric classification for food additives (adopted internationally as INS codes). NutriCheck maintains an in-memory dictionary of high-risk additives (IngredientDatabase.kt), mapping codes (e.g. E171 Titanium Dioxide, E621 MSG, E407 Carrageenan) to evidence levels ('High', 'Medium', 'Low') and dietary hazard flags.",
      trap: "Examiner follow-up: 'Are all E-numbers harmful?' Answer: 'No, Sir. For instance, Vitamin C is E300 and Curcumin is E100. Our engine only penalizes additives documented in scientific literature with Medium or High evidence of adverse reactions, as catalogued in IngredientDatabase.kt.'"
    },
    {
      id: "viva-4",
      category: "Project Fundamentals",
      question: "Who developed NutriCheck and in what academic context?",
      beginner: "NutriCheck was developed by V. V. Karthik Barrenkala (R210111) and Machipalli Uday Bhaskar (R210288), Computer Science & Engineering students at Rajiv Gandhi University of Knowledge Technologies (RGUKT), as our Major Project Part 1.",
      panel: "The system was designed and authored by V. V. Karthik Barrenkala (Roll No: R210111) and Machipalli Uday Bhaskar (Roll No: R210288), Department of Computer Science & Engineering, RGUKT, submitted for B.Tech Major Project technical evaluation.",
      trap: "Examiner follow-up: 'What was each member's technical contribution?' Answer: 'Karthik focused on the Jetpack Compose UI architecture, Navigation 3, and Multi-LLM API orchestration; Uday focused on the CameraX edge vision pipeline, ML Kit OCR integration, and the mathematical Rules Engine logic.'"
    },
    {
      id: "viva-5",
      category: "Project Fundamentals",
      question: "What is the difference between NutriCheck and PureCheck?",
      beginner: "NutriCheck is the official consumer and university presentation brand name. In the actual source code, the Android application package namespace is `com.example.purecheck`, which was the internal development codename.",
      panel: "The production brand name is 'NutriCheck: Food Intelligence Platform' as declared in strings.xml (<string name='app_name'>NutriCheck</string>). The Kotlin codebase and package identifiers utilize `com.example.purecheck` as the persistent internal module namespace.",
      trap: "Examiner follow-up: 'Why not rename the package to nutricheck?' Answer: 'In Android development, changing the applicationId and namespace in a production-ready application requires migrating SharedPreferences keys and build paths; keeping purecheck as internal namespace while branding as NutriCheck is standard industry practice.'"
    },
    {
      id: "viva-6",
      category: "Project Fundamentals",
      question: "Is NutriCheck certified as a medical diagnostic tool?",
      beginner: "No. NutriCheck provides educational and informational food safety audits. It explicitly reminds users that its heuristic grades do not replace professional clinical or medical advice.",
      panel: "Architecturally and legally, NutriCheck operates under informational dietary audit guidelines. The application explicitly incorporates a disclaimer boundary: heuristic scores and conflict warnings assist consumer decision-making but do not constitute medical device software (SaMD) or clinical diagnostic claims.",
      trap: "Examiner follow-up: 'What if a diabetic user eats a product marked Grade A and suffers a glycemic spike?' Answer: 'NutriCheck evaluates products based on standard nutritional declarations per 100g. We include explicit disclaimers in the UI and report chat stating that clinical responses vary and individuals must follow their physician's recommendations.'"
    },

    // Category 2: Architecture & Design
    {
      id: "viva-7",
      category: "System Architecture",
      question: "Explain the complete architectural flow when a user takes a photo of a label.",
      beginner: "Camera takes photo -> Saved to temporary cache file -> Scaled down to 768px -> ML Kit reads text -> Local Rules Engine calculates scores -> Text and scores sent to Gemini AI -> Gemini returns structured JSON -> Saved to phone history -> ResultScreen displays report.",
      panel: "Pipeline: CameraX ImageCapture.takePicture() -> cacheDir temporary file -> BitmapFactory downscale to max 768px at 70% JPEG quality -> ML Kit TextRecognition Tasks.await() -> Raw text string -> RulesEngine.calculate() yields DeterministicResult -> Injected into Gemini 2.0 Flash prompt payload -> OkHttp async call -> JSON sanitization -> SharedPreferences persistence via Gson -> Jetpack Compose recomposition in ResultScreen.",
      trap: "Examiner follow-up: 'Where could this pipeline fail and how do you handle it?' Answer: 'Camera failure triggers ImageCaptureException; empty OCR text triggers a user-friendly error asking for better lighting; network failure triggers an AlertDialog; and API model exhaustion triggers candidate model fallback.'"
    },
    {
      id: "viva-8",
      category: "System Architecture",
      question: "What is the 'Zero-Hallucination Safety Protocol' in NutriCheck?",
      beginner: "We never ask the AI to calculate the food score. If you ask an AI for a score, it might give 80 today and 40 tomorrow. Instead, our Kotlin Rules Engine calculates the exact math first, and forces the AI to use and explain those exact numbers.",
      panel: "Generative Large Language Models are stochastic token predictors susceptible to non-deterministic variance and hallucinations. NutriCheck enforces a strict architectural boundary: RulesEngine.kt computes the mathematical ground-truth scores, grades, and profile penalties locally. The prompt payload explicitly instructs: 'YOU MUST USE THESE EXACT CALCULATED VALUES... Do not recalculate or invent these scores.' Thus, the LLM acts purely as a semantic translator.",
      trap: "Examiner follow-up: 'Can the LLM still hallucinate ingredient descriptions?' Answer: 'To prevent that, we supply the extracted additive definitions from our verified in-memory dictionary directly in the prompt context, anchoring the model's explanations.'"
    },
    {
      id: "viva-9",
      category: "System Architecture",
      question: "Why did you choose a hybrid edge-cloud architecture over 100% cloud or 100% on-device?",
      beginner: "Running OCR and math on the phone makes scanning instant and reliable. But running a 70-billion parameter AI model on a smartphone would overheat the battery and take gigabytes of RAM. Cloud AI gives us deep explanations while keeping the app fast.",
      panel: "Edge computing provides sub-200ms latency for vision tasks (ML Kit OCR & Barcodes) and 100% determinism for scoring algorithms without data transfer costs. However, complex natural language contextualization and conversational inspector chat require high-parameter LLMs. Hybrid edge-cloud balances edge speed and privacy with cloud generative reasoning.",
      trap: "Examiner follow-up: 'What happens if there is no internet connection?' Answer: 'For barcodes, Open Food Facts requires network access, but our barcode scanner immediately suggests scanning the label; for labels, the local RulesEngine can score offline, while cloud natural language summary requires connectivity.'"
    },
    {
      id: "viva-10",
      category: "System Architecture",
      question: "How does NutriCheck isolate user privacy?",
      beginner: "Your biometric information (name, height, weight, medical preferences) and scan history are stored ONLY on your phone in private SharedPreferences. Nothing is stored on an external tracking server.",
      panel: "NutriCheck implements on-device privacy-by-design. The application has no centralized user-tracking backend or Firebase telemetry. Biometric metrics and preferences reside in private sandbox storage (/data/data/com.example.purecheck/). When communicating with Gemini or Groq, only the product ingredient text and anonymized profile flags are transmitted.",
      trap: "Examiner follow-up: 'Does Google Gemini receive the user's name?' Answer: 'No, Sir. In GeminiClient.kt, the user profile parameters sent in the prompt are strictly dietary flags like vegan, diabetic, and lowSodium. Personal identifiers like name, age, and weight are never sent.'"
    },

    // Category 3: Jetpack Compose & Navigation
    {
      id: "viva-11",
      category: "Frontend & UI",
      question: "What is Jetpack Navigation 3 (Nav3) and how does it differ from older Android Navigation?",
      beginner: "Older navigation used XML files or fragile URL strings like 'screen/123'. Navigation 3 uses strongly-typed Kotlin objects (`NavKey`). It treats the screen history as a simple Kotlin list where you push and pop screens safely.",
      panel: "Jetpack Navigation 3 (androidx.navigation3) is Google's modern declarative navigation library. Destinations are strongly-typed `@Serializable data object` or `data class` instances implementing `NavKey` (e.g. `Result(val jsonResult: String)`). It uses `rememberNavBackStack(Main)` and `NavDisplay(backStack, entryProvider)`. This eliminates runtime string-formatting crashes and provides compile-time type safety.",
      trap: "Examiner follow-up: 'How does back navigation work in Nav3?' Answer: 'In Navigation.kt, onBack simply executes `backStack.removeLastOrNull()`, popping the top NavKey off the stack.'"
    },
    {
      id: "viva-12",
      category: "Frontend & UI",
      question: "What is Unidirectional Data Flow (UDF) in Jetpack Compose?",
      beginner: "State flows down, events flow up. The screen displays whatever state you give it. When you tap a button, an event is sent up, the data changes, and Compose automatically redraws only the parts of the screen that changed.",
      panel: "Unidirectional Data Flow is the architectural pattern where state is passed down to composable functions via arguments, while user interactions trigger callbacks (lambdas) passed up to state holders. For example, in ResultScreen.kt, quantityMultiplier state flows down to MacroBoxCompact, and clicking '+' triggers `quantityMultiplier += 0.25f`, causing Compose to intelligently recompose only the scaled macro widgets.",
      trap: "Examiner follow-up: 'What is the purpose of remember in Compose?' Answer: '`remember` preserves state across recompositions. Without `remember`, a state variable like `var count = 0` would be re-initialized to 0 every time the screen redraws.'"
    },
    {
      id: "viva-13",
      category: "Frontend & UI",
      question: "How did you implement the scanning laser animation in Compose without external GIF or video files?",
      beginner: "We drew it using pure code! Compose has a Canvas element where we draw a green line, and an `infiniteTransition` moves the line smoothly up and down across the viewfinder box.",
      panel: "In ScannerScreen.kt, we instantiate `val infiniteTransition = rememberInfiniteTransition()`, driving an animated float `scanProgress` from 0f to 1f with a 2500ms `LinearEasing` and `RepeatMode.Reverse`. Inside Compose `Canvas`, we compute `lineY = top + (rectHeight * scanProgress)` and call `drawLine(color = accentGreen, start = Offset(left, lineY), end = Offset(left + rectWidth, lineY), strokeWidth = 6f)`.",
      trap: "Examiner follow-up: 'Does this canvas animation consume high CPU on older phones?' Answer: 'No, Sir. Compose Canvas draw operations are hardware-accelerated directly on the GPU render thread, consuming minimal battery compared to video playback.'"
    },

    // Category 4: CameraX & Computer Vision
    {
      id: "viva-14",
      category: "CameraX & Vision",
      question: "How does CameraX bind to the Android Lifecycle?",
      beginner: "CameraX uses `ProcessCameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture)`. This means when you minimize the app or switch screens, the camera automatically turns off to save battery.",
      panel: "CameraX is lifecycle-aware. By binding use cases (Preview, ImageCapture, ImageAnalysis) to `LocalLifecycleOwner.current`, CameraX automatically opens the camera hardware when the composable enters the STARTED state and releases hardware buffers when the screen STOPPED or DESTROYED. This eliminates camera memory leaks and crashes.",
      trap: "Examiner follow-up: 'Why call cameraProvider.unbindAll() before binding?' Answer: 'To ensure any previously bound use cases from prior screens are detached, preventing the Camera2 framework from throwing IllegalStateException.'"
    },
    {
      id: "viva-15",
      category: "CameraX & Vision",
      question: "Why did you resize captured images to 768px before running OCR or sending to the AI?",
      beginner: "A modern smartphone camera takes 48-megapixel photos that are 10MB to 20MB in size! Uploading a 20MB photo would freeze the app and waste mobile data. Resizing to 768 pixels shrinks the file to under 150KB while keeping text completely readable.",
      panel: "In ScannerScreen.kt, `resizeBitmap(source, 768)` downsamples the bitmap while strictly maintaining aspect ratio, followed by 70% JPEG compression. Benefits: 1) Optimizes memory footprint, avoiding OutOfMemoryError (OOM) during bitmap manipulation. 2) Dramatically accelerates Base64 encoding. 3) Complies with LLM multimodal token input limits. 4) Reduces cloud network upload time from 5 seconds to sub-200ms.",
      trap: "Examiner follow-up: 'Does downscaling to 768px degrade OCR text accuracy?' Answer: 'No, Sir. Standard packaging typography requires only 30-40 pixels of character height for ML Kit's Latin text recognizer to achieve >95% character classification confidence.'"
    },
    {
      id: "viva-16",
      category: "CameraX & Vision",
      question: "How does BarcodeScannerScreen scan in real time without lag?",
      beginner: "It uses CameraX's `ImageAnalysis` with a setting called `STRATEGY_KEEP_ONLY_LATEST`. This means the phone only processes the current camera frame and automatically discards old frames if processing takes time.",
      panel: "In BarcodeScannerScreen.kt, `ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build()` is configured. In the analyzer callback, `imageProxy.image` is converted to `InputImage.fromMediaImage()`. Once `scanner.process(image)` completes, we invoke `imageProxy.close()` inside `addOnCompleteListener`. This prevents frame queue buffer starvation and maintains a steady 30 FPS camera preview.",
      trap: "Examiner follow-up: 'What happens if you forget to call imageProxy.close()?' Answer: 'The camera preview pipeline will stall and freeze permanently after 3 to 5 frames because CameraX runs out of available image buffers in its surface pool.'"
    },

    // Category 5: ML Kit OCR & Barcodes
    {
      id: "viva-17",
      category: "ML Kit OCR & Vision",
      question: "How does Google ML Kit perform Text Recognition on device?",
      beginner: "ML Kit runs an optimized machine learning model inside your phone. It detects where text exists in an image, breaks it into blocks, lines, and words, and recognizes the characters—all without an internet connection.",
      panel: "ML Kit Latin Text Recognition (play-services-mlkit-text-recognition:19.0.0) employs an on-device neural network pipeline: 1) Text Detection (identifying oriented bounding boxes using lightweight CNNs). 2) Text Recognition (recurrent neural network or CTC-based character sequence classification). In our code, we call `Tasks.await(recognizer.process(inputImage))` synchronously on a background thread to yield `visionText.text`.",
      trap: "Examiner follow-up: 'Why did you use Tasks.await() instead of addOnSuccessListener()?' Answer: 'Because we are inside a background worker thread (`Thread { ... }`); using Tasks.await() allows clean, linear sequential execution without callback nesting hell.'"
    },
    {
      id: "viva-18",
      category: "ML Kit OCR & Vision",
      question: "What barcode formats does NutriCheck support?",
      beginner: "It supports standard retail grocery barcodes like EAN-13, EAN-8, UPC-A, and UPC-E found on packaged food items worldwide.",
      panel: "Google ML Kit's BarcodeScanning client natively supports 1D linear barcodes (EAN-13, EAN-8, UPC-A, UPC-E, Code-39, Code-128, ITF) and 2D matrix symbologies (QR Code, Data Matrix, Aztec). In our implementation, we extract `barcode.rawValue`, filter for digit characters via `.filter { it.isDigit() }`, and query Open Food Facts.",
      trap: "Examiner follow-up: 'Why filter only digits?' Answer: 'Some scanners accidentally append format prefixes or whitespace; Open Food Facts requires the exact GTIN numeric sequence to prevent HTTP 404 false negatives.'"
    },

    // Category 6: Scoring Algorithm & Mathematics
    {
      id: "viva-19",
      category: "Scoring Algorithm",
      question: "State the mathematical formula used to calculate the overall NutriCheck score.",
      beginner: "The overall score is the simple average of five scores: Quality, Additives, Nutrition, Sugar, and Sodium: `(Quality + Additives + Nutrition + Sugar + Sodium) / 5`. If the score is 80 or above, it gets an A; 68 or above gets a B; 52 gets a C; 38 gets a D; otherwise F.",
      panel: "Overall Score S in RulesEngine.kt: `S = (Q_quality + A_additives + N_nutrition + S_sugar + Na_sodium) / 5` where all five dimensions are clamped between 10 and 100. Grade Mapping: S >= 80 -> A; 68 <= S < 80 -> B; 52 <= S < 68 -> C; 38 <= S < 52 -> D; S < 38 -> F.",
      trap: "Examiner follow-up: 'Why divide by 5 equally instead of using different weights?' Answer: 'Equal dimension weighting provides transparent, easily auditable scores without arbitrary bias. Furthermore, high sugar or high sodium already penalizes the Nutrition dimension because Nutrition is a blended average with Sugar and Sodium!'"
    },
    {
      id: "viva-20",
      category: "Scoring Algorithm",
      question: "How does the Sugar scoring formula work when nutrition grams are available vs when they are not?",
      beginner: "When grams are available: Any sugar over 5g deducts 4.5 points per gram! When grams are missing: The app looks for keywords—finding 'sugar' sets the score to 75; finding 'high fructose corn syrup' drops it to 60.",
      panel: "In RulesEngine.kt: If `sugarGrams > 0`: if `sugarGrams > 5f`, `excess = sugarGrams - 5f; sugarScore = 100 - (excess * 4.5f).toInt()`. If `sugarGrams == 0` (OCR fallback mode): if text contains 'sugar' or 'sucrose', sugarScore = 75; if text contains 'corn syrup', 'dextrose', or 'fructose', sugarScore = 60 with a warning. Result is clamped to [10, 100].",
      trap: "Examiner follow-up: 'Why is 5g chosen as the threshold?' Answer: '5g per 100g is the internationally recognized World Health Organization (WHO) and UK FSA threshold for low-sugar food classifications.'"
    },
    {
      id: "viva-21",
      category: "Scoring Algorithm",
      question: "How does the Sodium scoring formula work?",
      beginner: "Products with over 140mg of sodium per 100g are penalized by deducting 0.15 points per extra milligram. If no numbers exist, detecting 'salt' or 'sodium' drops the score to 75.",
      panel: "In RulesEngine.kt: If `sodiumMg > 0`: if `sodiumMg > 140f`, `excess = sodiumMg - 140f; sodiumScore = 100 - (excess * 0.15f).toInt()`. If no numeric value: detecting 'salt' or 'sodium' sets sodiumScore = 75. Clamped to [10, 100]. The 140mg threshold aligns with FDA regulations for 'Low Sodium' food labeling.",
      trap: "Examiner follow-up: 'What if a food has 1000mg of sodium?' Answer: 'Excess = 860mg. Deduction = 860 * 0.15 = 129 points. 100 - 129 = -29, which is clamped to our minimum floor of 10 points.'"
    },
    {
      id: "viva-22",
      category: "Scoring Algorithm",
      question: "How does NutriCheck calculate profile conflicts and why don't they change the overall product grade?",
      beginner: "If a product is a high-quality milk chocolate, its general grade might be a B. But if YOU are Vegan, that product is unsafe for you! So we keep the general grade as B, but deduct 30 points from your personal Profile Score and show a red warning banner.",
      panel: "Separation of Concerns: A food item has an objective physical quality (overallScore/Grade) independent of who consumes it. Changing the overall grade based on personal preferences would corrupt the database rating. Instead, RulesEngine.kt computes a separate `profileScore`: Vegan conflicts (-30), Vegetarian (-20), Diabetic (-20), Heart Health (-20), Gut Health (-15), Cosmetic Safe (-20), and Watchlist (-25 each).",
      trap: "Examiner follow-up: 'Can the profile score become negative?' Answer: 'No, Sir. RulesEngine clamps profileScore to `max(10, min(overallScore, profileScore))`.'"
    },

    // Category 7: Multi-LLM Orchestration
    {
      id: "viva-23",
      category: "Multi-LLM Orchestration",
      question: "How does NutriCheck support multiple AI providers (Gemini, Groq, DeepSeek)?",
      beginner: "In Settings, the user picks their provider. Our `GeminiClient.kt` file checks which provider is selected and sends the request to the correct cloud server with the right JSON format.",
      panel: "GeminiClient.kt implements dynamic provider routing. For Gemini, it targets Google Generative Language API with `gemini-2.0-flash`. For Groq, DeepSeek, and Bytez, it targets OpenAI-compatible `/chat/completions` endpoints with bearer authentication. It also implements candidate model arrays: if a model returns 404 or rate-limit error, it automatically falls back to the next candidate model.",
      trap: "Examiner follow-up: 'What if all candidate models fail?' Answer: 'The callback receives a detailed aggregated error message via `errorLogs.joinToString(\"\\n\")` and displays a user-friendly AlertDialog in the UI.'"
    },
    {
      id: "viva-24",
      category: "Multi-LLM Orchestration",
      question: "How do you enforce simple language in LLM responses?",
      beginner: "In the system prompt sent to the AI, we include strict rules: 'Write in VERY SIMPLE English. Avoid medical jargon. If you must use a scientific word, define it immediately in parentheses.'",
      panel: "Prompt Engineering in GeminiClient.kt explicitly specifies: 'CRITICAL LANGUAGE REQUIREMENT: Write all descriptions, reasons, summaries in VERY SIMPLE English that an ordinary consumer can easily understand. Avoid complex technical or medical jargon. If you MUST mention a scientific term, define it in very simple words in parentheses immediately.'",
      trap: "Examiner follow-up: 'How do you prevent the AI from returning Markdown tables that break phone screens?' Answer: 'Rule #1 in our systemPrompt explicitly states: NEVER output Markdown tables with pipes (|) or dashed dividers. Format all assessments using clean structured sections with bullet points.'"
    },

    // Category 8: APIs & Networking
    {
      id: "viva-25",
      category: "APIs & Networking",
      question: "What HTTP method and headers are used to query Open Food Facts?",
      beginner: "We use an HTTP GET request to `world.openfoodfacts.org/api/v2/product/{barcode}.json` with a custom User-Agent identifying our app.",
      panel: "Protocol: HTTP/1.1 REST GET. URL: https://world.openfoodfacts.org/api/v2/product/{barcode}.json. Headers: `User-Agent: PureCheck - Android - Version 2.0 - CSE Major Project`. Open Food Facts strictly requires a descriptive User-Agent; requests without one are throttled or blocked with HTTP 403 Forbidden.",
      trap: "Examiner follow-up: 'What if Open Food Facts returns HTTP 404?' Answer: 'In BarcodeScannerScreen.kt, we capture 404 and display: This product is not registered in the food database yet. Please try scanning the ingredient label directly instead!'"
    },
    {
      id: "viva-26",
      category: "APIs & Networking",
      question: "Why use OkHttp directly instead of Retrofit?",
      beginner: "Retrofit is built on top of OkHttp. Because we only have a couple of straightforward API calls with dynamic JSON schemas, using OkHttp directly eliminates unnecessary library bloat and gives us full control over network timeouts.",
      panel: "Retrofit is an abstraction layer that excels at rigid, pre-defined REST schemas. However, our application deals with polymorphic multi-provider LLM endpoints (Gemini JSON structures vs OpenAI-compatible chat structures) and dynamic Open Food Facts JSON. OkHttp provides lightweight, zero-dependency streaming, direct header manipulation, and custom timeout configuration.",
      trap: "Examiner follow-up: 'What timeouts did you configure in OkHttp?' Answer: '30 seconds connect, 30 seconds read, and 30 seconds write timeouts in GeminiClient.kt to accommodate high-latency cloud LLM generation.'"
    },

    // Category 9: Database & Persistence
    {
      id: "viva-27",
      category: "Database & Storage",
      question: "Explain how SharedPreferences stores complex objects like HistoryItem.",
      beginner: "SharedPreferences can only store simple text and numbers. So we convert our Kotlin objects into JSON text strings using Google Gson, and store that text. When reading it back, Gson turns the JSON text back into Kotlin objects.",
      panel: "SharedPreferences stores key-value pairs. To persist polymorphic lists like `List<HistoryItem>`, `PureCheckPreferences.kt` uses `Gson().toJson(history)`. When retrieving, it utilizes reflection via `object : TypeToken<List<HistoryItem>>() {}.type` to deserialize the JSON string back into a type-safe Kotlin collection.",
      trap: "Examiner follow-up: 'What is the risk of storing JSON strings in SharedPreferences?' Answer: 'If the JSON string grows too large (several megabytes), it can cause memory pressure on startup. We prevent this by enforcing a strict FIFO limit of 50 history items.'"
    },
    {
      id: "viva-28",
      category: "Database & Storage",
      question: "What is the difference between apply() and commit() in SharedPreferences?",
      beginner: "`apply()` saves the data in the background asynchronously without freezing the app. `commit()` writes synchronously and can cause the UI to stutter.",
      panel: "`commit()` writes data to disk synchronously and returns a boolean status, blocking the calling thread. `apply()` commits data to in-memory storage immediately and schedules an asynchronous background disk write without blocking the UI main thread. In NutriCheck, we strictly use `.apply()` across all preference updates.",
      trap: "Examiner follow-up: 'Could apply() lose data if the phone crashes?' Answer: 'In-memory state is updated immediately, so subsequent reads in the same session are 100% consistent, but a hard power loss before OS disk flush could theoretically lose the last write.'"
    },

    // Category 10: Android Build System
    {
      id: "viva-29",
      category: "Android Build System",
      question: "What is compileSdk, targetSdk, and minSdk in your build.gradle.kts?",
      beginner: "minSdk (24) is the oldest Android version that can run our app (Android 7.0). targetSdk (36) is the Android version we tested and optimized for. compileSdk (36) is the Android version used by Gradle to compile our code.",
      panel: "In app/build.gradle.kts: minSdk = 24 ensures compatibility with API level 24 (Android 7.0 Nougat) and above (~96% global device coverage). targetSdk = 36 indicates compliance with the latest Android 16 runtime security and behavioral changes. compileSdk = 36 determines the platform APIs available during bytecode compilation.",
      trap: "Examiner follow-up: 'Why did you exclude META-INF/AL2.0 and LGPL2.1 in packaging?' Answer: 'Because multiple third-party dependencies (like OkHttp and ML Kit) bundle identical open-source license files; without `resources.excludes`, the Gradle APK packager halts with a DuplicateFileException.'"
    },
    {
      id: "viva-30",
      category: "Android Build System",
      question: "What is the difference between a Debug APK and a Release APK?",
      beginner: "A Debug APK is signed with a temporary debug key and includes extra logging tools so you can test on an emulator. A Release APK is optimized, stripped of debug overhead, and signed with a permanent key for app store distribution.",
      panel: "Debug builds (`./gradlew assembleDebug`) are signed with an auto-generated keystore (`debug.keystore`), enable JDWP debugger attachment, and include test manifests. Release builds (`./gradlew assembleRelease`) can enable code shrinking (ProGuard/R8), disable debugging flags, optimize bytecode, and require cryptographic production keystore signing.",
      trap: "Examiner follow-up: 'Did you enable minify in your release build?' Answer: 'In app/build.gradle.kts, `isMinifyEnabled = false` is currently configured to prevent R8 from obfuscating our Gson data model class names without custom keep rules.'"
    },

    // Category 11: Testing & QA
    {
      id: "viva-31",
      category: "Testing & QA",
      question: "What unit tests exist in your submitted project repository?",
      beginner: "We have unit test files located at `app/src/test/` including `MainScreenViewModelTest.kt` to test our state logic and `RulesEngine` scoring math.",
      panel: "The repository contains local unit tests in `app/src/test/java/com/example/purecheck/ui/main/MainScreenViewModelTest.kt` and instrumented UI tests in `app/src/androidTest/java/com/example/purecheck/ui/main/MainScreenTest.kt` using JUnit4 and Compose UI test manifest.",
      trap: "Examiner follow-up: 'How do you run local unit tests from the command line?' Answer: 'By executing `./gradlew test` in the project root directory.'"
    },

    // Category 12: Limitations & Future Scope
    {
      id: "viva-32",
      category: "Limitations & Roadmap",
      question: "Does the submitted Kotlin source code have a dedicated side-by-side comparison screen?",
      beginner: "No, Sir. While our preliminary project abstract mentioned side-by-side comparison, in the current Part 1 Kotlin build, comparison is performed by comparing scores in History, Favorites, and Shopping List. A dedicated split-screen comparison is our planned Part 2 feature.",
      panel: "Honest Academic Discloser: The submitted Kotlin codebase contains Home, Scanner, BarcodeScanner, ScanOptions, Result, Profile, MyProfile, Favorites, Settings, and Author screens. A dedicated two-column synchronous comparison screen is not yet instantiated in NavigationKeys.kt; product evaluation is performed via sequential audits, and dedicated side-by-side diffing is framed as Part 2 future scope.",
      trap: "Examiner follow-up: 'Excellent honesty. How will you implement side-by-side comparison in Part 2?' Answer: 'By creating a `ComparisonScreen(productA: String, productB: String)` NavKey in Navigation 3, taking two JSON payloads and displaying differential delta badges for protein, sugar, and additive counts.'"
    },

    // Additional Rigorous Viva Examination Questions (33-66)
    {
      id: "viva-33",
      category: "Project Fundamentals",
      question: "How do you distinguish ultra-processed foods (UPF) from minimally processed foods in NutriCheck?",
      beginner: "Minimally processed foods have ingredients you find in a home kitchen (oats, milk, almonds). Ultra-processed foods contain industrial substances you never find in a home pantry: high-fructose corn syrup, hydrolyzed proteins, synthetic dyes, and chemical emulsifiers.",
      panel: "NutriCheck incorporates principles from the NOVA food classification framework. Foods with multiple cosmetic additives (colors E102/E129, artificial sweeteners E951, emulsifiers E407/E433, or unmasked palm oil) suffer cascading penalties in both the 'Ingredient Quality' and 'Additives' dimensions, driving their overall score into the C, D, or F tier.",
      trap: "Examiner follow-up: 'Is NOVA classification explicitly coded into the app?' Answer: 'Our RulesEngine.kt embeds NOVA principles through its categorization of industrial additives (Color, Preservative, Sweetener, Emulsifier, Enhancer), where each presence directly reduces the additive score by 8 points.'"
    },
    {
      id: "viva-34",
      category: "Project Fundamentals",
      question: "What is consumer asymmetric information and how does NutriCheck resolve it?",
      beginner: "The food manufacturer knows everything about what is inside the product, but the consumer standing in the supermarket aisle knows almost nothing. NutriCheck instantly unmasks the hidden chemicals so the buyer has equal knowledge.",
      panel: "Asymmetric information in consumer retail occurs when product manufacturers hold private qualitative information regarding synthetic stabilizers, high-fructose syrups, and palm oil obfuscation that consumers cannot decipher in reasonable shopping time. NutriCheck eliminates this friction through real-time OCR extraction, deterministic evaluation, and plain-English unmasking in under 2 seconds.",
      trap: "Examiner follow-up: 'Doesn't the mandatory nutrition facts panel already solve this?' Answer: 'No, Sir. Front-of-package claims like \"Multigrain\" or \"Low Fat\" often mask 30g+ of hidden sugar and artificial emulsifiers in the fine print on the back.'"
    },
    {
      id: "viva-35",
      category: "System Architecture",
      question: "Why is MainAppContainer structured with a bottom navigation bar instead of a navigation drawer?",
      beginner: "Bottom bars are within easy reach of the user's thumb when holding a smartphone in one hand while shopping. A drawer menu requires reaching to the top left corner with a second hand.",
      panel: "UX Thumb-Zone Ergonomics: In retail mobile interactions, one-handed operation is critical. In MainAppContainer.kt, NavigationBar hosts Home, Scan, and Profile tabs anchored to the bottom edge with tonal elevation. Tapping any tab updates the local activeTab state without tearing down or rebuilding the root NavDisplay backstack.",
      trap: "Examiner follow-up: 'Does changing tabs destroy the current screen state?' Answer: 'No, Sir. MainAppContainer retains state through remember { mutableStateOf(AppTab.Home) } and Compose's internal slot table preserves sub-composable state.'"
    },
    {
      id: "viva-36",
      category: "System Architecture",
      question: "Explain the role of NavigationKeys.kt in the overall system architecture.",
      beginner: "It is the master list of all valid destinations in the app. Each screen (Main, Scanner, BarcodeScanner, Result, Settings, etc.) is defined as a strongly-typed Kotlin object or class.",
      panel: "In NavigationKeys.kt, all destinations implement the sealed interface `NavKey`. Parameterless screens are defined as `@Serializable data object Main : NavKey`, while parameterized destinations are declared as `@Serializable data class Result(val jsonResult: String) : NavKey`. This enforces architectural modularity and type-safe argument passing across the entire app.",
      trap: "Examiner follow-up: 'Why not pass a parsed object instead of a JSON string in Result(jsonResult)?' Answer: 'Because Nav3 requires serializable keys; passing the sanitized JSON string decouples the navigation layer from the internal domain parser, allowing instant re-serialization to SharedPreferences history without reflection overhead.'"
    },
    {
      id: "viva-37",
      category: "System Architecture",
      question: "What happens if the Gemini API returns malformed or non-JSON text?",
      beginner: "Our app has a `sanitizeJson()` function that cleans away any accidental Markdown text, and a `try-catch` block that catches parsing errors and shows an alert instead of crashing.",
      panel: "Fault-Tolerance in GeminiClient.kt: 1) `generationConfig.put(\"responseMimeType\", \"application/json\")` instructs Gemini's constrained decoding grammar to output strictly valid JSON. 2) `sanitizeJson()` strips leading and trailing Markdown code fences (```json ... ```). 3) In ScannerScreen.kt, the parsing logic is wrapped in `try { JSONObject(jsonText) ... } catch (e: Exception) { errorMessage = \"JSON parsing error: \${e.message}\" }`, cleanly informing the user without terminating the app process.",
      trap: "Examiner follow-up: 'Could an invalid JSON response cause an ANR (Application Not Responding)?' Answer: 'No, Sir. OkHttp network callbacks and JSON parsing occur on background I/O worker threads; only the final UI update is dispatched to the main thread via ContextCompat.getMainExecutor().'"
    },
    {
      id: "viva-38",
      category: "Frontend & UI",
      question: "How does the portion scaler in ResultScreen update the nutrition display in real time?",
      beginner: "We store a multiplier (like 1.0x, 1.25x, 1.5x) in a state variable. When you tap '+' or '-', Jetpack Compose instantly multiplies all base calories and grams, and redraws the boxes.",
      panel: "State-Driven Reactivity: In ResultScreen.kt, `var quantityMultiplier by remember { mutableStateOf(1.0f) }` tracks the serving factor. Base values (`baseCalories`, `baseProtein`, `baseSugar`) parsed from the audit payload are multiplied: `val calories = baseCalories * quantityMultiplier`. Compose detects reads to `quantityMultiplier` and schedules an optimized recomposition of only the dependent `MacroBoxCompact` composables.",
      trap: "Examiner follow-up: 'Does scaling the portion size affect the overall product grade?' Answer: 'No, Sir. The overall product grade is based on the standardized 100g density; however, scaling DOES re-evaluate personalized profile conflicts (e.g. if a 2x portion causes absolute sugar to exceed the 10g diabetic threshold).'"
    },
    {
      id: "viva-39",
      category: "Frontend & UI",
      question: "Explain the implementation of PureCheckTheme and PureCheckColors.",
      beginner: "We created a centralized color palette in `PureCheckComponents.kt` with dark navy backgrounds and emerald green accents. All buttons, cards, and text use these standardized colors.",
      panel: "Design System Tokens: Object `PureCheckColors` defines semantic color constants: `darkBg = Color(0xFF090D16)`, `cardBg = Color(0xFF131C2E)`, `cardBgElevated = Color(0xFF1E2B44)`, and `accentGreen = Color(0xFF10B981)`. `Theme.kt` wraps the root in `MaterialTheme` with custom color schemes, typography, and shapes, ensuring consistent contrast ratios across all Android screen densities.",
      trap: "Examiner follow-up: 'Does your app support Android Light Mode?' Answer: 'Currently, the application enforces a purpose-built high-contrast Dark Theme (`PureCheckTheme`) optimized for retail low-light shopping environments and OLED power efficiency.'"
    },
    {
      id: "viva-40",
      category: "Frontend & UI",
      question: "What is the purpose of rememberScrollState() and verticalScroll() in your Compose screens?",
      beginner: "Without them, screens with lots of information (like the audit report or settings) would get cut off on smaller phones. They enable smooth vertical scrolling.",
      panel: "In Compose, Column composables do not scroll by default and will clip overflowing children. Adding `Modifier.verticalScroll(rememberScrollState())` binds the column to a scrollable state holder that coordinates nested scrolling, fling inertia, and edge glow effects across all device screen heights.",
      trap: "Examiner follow-up: 'Why not use LazyColumn instead of verticalScroll()?' Answer: 'LazyColumn is optimal for unbound dynamic lists (like 50 history items) because it recycles view items; for fixed structured reports with heterogeneous sections (header, scores, nutrition, chat), a single Column with verticalScroll() is cleaner and avoids nested lazy list measurement exceptions.'"
    },
    {
      id: "viva-41",
      category: "CameraX & Vision",
      question: "How does NutriCheck toggle the smartphone flashlight during label scanning?",
      beginner: "There is a flash icon in the top bar. Tapping it calls `camera.cameraControl.enableTorch(isTorchOn)` which turns the physical phone LED on or off.",
      panel: "Hardware Torch Control: In ScannerScreen.kt, the bound `Camera` instance exposes `camera.cameraControl.enableTorch(isTorchOn)`. This dispatches an asynchronous command through the Camera2 HAL to illuminate the device's physical LED diode, providing critical illumination in dim supermarket aisles without requiring camera unbinding.",
      trap: "Examiner follow-up: 'What if a device does not have a physical flash?' Answer: 'CameraX safely returns a failure ListenableFuture without crashing, and the app remains fully functional using ambient lighting.'"
    },
    {
      id: "viva-42",
      category: "CameraX & Vision",
      question: "What is the difference between CameraSelector.DEFAULT_BACK_CAMERA and DEFAULT_FRONT_CAMERA?",
      beginner: "Back camera is the main high-resolution camera on the back of the phone used for scanning; front camera is the selfie camera.",
      panel: "CameraSelector specifies camera lens facing filters. NutriCheck explicitly configures `CameraSelector.DEFAULT_BACK_CAMERA` to ensure the framework binds to the primary rear sensor equipped with optical autofocus and high-resolution macro capabilities required for barcode and fine-print ingredient extraction.",
      trap: "Examiner follow-up: 'Could a user scan labels using the front camera?' Answer: 'Technically yes if CameraSelector were changed, but front cameras lack variable macro autofocus, leading to severe OCR character blurring.'"
    },
    {
      id: "viva-43",
      category: "ML Kit OCR & Vision",
      question: "Why did you choose Google Play Services ML Kit over Tesseract OCR?",
      beginner: "Tesseract is an older C++ library that requires compiling large native libraries (NDK), is slow on mobile, and struggles with curved packaging. Google ML Kit is optimized for mobile hardware and runs in under 200ms.",
      panel: "Comparative Benchmark: Tesseract requires embedding heavy native C++ binaries (`.so` files via NDK) increasing APK size by 25MB+, relies on rigid binarization heuristics, and exhibits poor accuracy on wrinkled surfaces. Google Play Services ML Kit (19.0.0) utilizes MobileNet deep learning backbones optimized via NNAPI, downloads dynamically via Google Play Services to save APK size, and achieves superior accuracy on complex packaged packaging fonts.",
      trap: "Examiner follow-up: 'What if Google Play Services is missing on a phone (e.g. Huawei)?' Answer: 'For devices lacking Google Play Services, ML Kit also offers a bundled model dependency (`com.google.mlkit:text-recognition`) that embeds the model weights directly inside the APK.'"
    },
    {
      id: "viva-44",
      category: "ML Kit OCR & Vision",
      question: "How does ML Kit handle text rotation and orientation?",
      beginner: "When you hold your phone sideways or upside down, CameraX provides the rotation degrees, and ML Kit automatically rotates the image before reading the text.",
      panel: "In ScannerScreen.kt and BarcodeScannerScreen.kt, the frame is wrapped in `InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)`. ML Kit internally normalizes orientation using this sensor rotation metadata, eliminating text recognition failures caused by portrait vs landscape phone orientation.",
      trap: "Examiner follow-up: 'What if the text on the box itself is printed vertically?' Answer: 'ML Kit's TextRecognizer detects individual character line bounding polygons and groups characters along the dominant line vector, supporting multi-oriented text lines.'"
    },
    {
      id: "viva-45",
      category: "ML Kit OCR & Vision",
      question: "Does ML Kit OCR work without internet access?",
      beginner: "Yes! 100% offline. The machine learning model runs directly on the phone processor.",
      panel: "Google Play Services ML Kit Latin Text Recognition is an entirely on-device edge ML framework. Character inference is computed locally on the device CPU/GPU/NPU without sending a single pixel over the network, ensuring zero data consumption and instantaneous response.",
      trap: "Examiner follow-up: 'If OCR is offline, why does your app need internet?' Answer: 'Internet is required for two other stages: querying the Open Food Facts cloud database for barcodes, and invoking cloud LLMs (Gemini/Groq) for plain-English explanations and conversational inspector chat.'"
    },
    {
      id: "viva-46",
      category: "Scoring Algorithm",
      question: "What is the penalty for Titanium Dioxide (E171) in NutriCheck?",
      beginner: "Titanium Dioxide is a white food coloring that was banned in Europe due to DNA damage concerns. In NutriCheck, it is marked as a High-evidence additive, deducting 15 points from Ingredient Quality and flagging a gut health risk.",
      panel: "In IngredientDatabase.kt, Titanium Dioxide (`eNumber = \"E171\"`, category = 'Colorant / Opacifier') is classified with `evidenceLevel = \"High\"` and `dietaryFlags = [\"gut_risk\"]`. When detected in RulesEngine.kt, it triggers a 15-point deduction from `qualityScore`, an 8-point deduction from `additivesScore`, and an additional 15-point deduction from `profileScore` if the user has Gut Health preferences enabled.",
      trap: "Examiner follow-up: 'Why did Europe ban E171 while the US FDA still permits it?' Answer: 'In 2022, the European Food Safety Authority (EFSA) concluded that titanium dioxide nanoparticles can cause genotoxicity (DNA damage) and accumulate in human tissue; NutriCheck adopts the stricter European precautionary health standard.'"
    },
    {
      id: "viva-47",
      category: "Scoring Algorithm",
      question: "How does NutriCheck detect hidden Monosodium Glutamate (MSG)?",
      beginner: "Manufacturers know consumers look for 'MSG', so they hide it under names like 'Yeast Extract' or 'Hydrolyzed Vegetable Protein'. NutriCheck's dictionary contains these exact aliases and flags them automatically.",
      panel: "Nomenclature Unmasking: In IngredientDatabase.kt, the Monosodium Glutamate definition includes `aliases = listOf(\"MSG\", \"Yeast Extract\", \"Hydrolyzed Vegetable Protein\", \"Sodium Glutamate\")`. Furthermore, RulesEngine.kt contains a dedicated heuristic: `if (lowerText.contains(\"yeast extract\") || lowerText.contains(\"hydrolyzed protein\")) { qualityScore -= 8; keyFindings.add(\"⚠ Contains flavor-masking flavor enhancers (possible MSG).\") }`.",
      trap: "Examiner follow-up: 'Is yeast extract chemically identical to pure MSG?' Answer: 'Yeast extract contains naturally high concentrations of free glutamates; industrial food processors use it as an artificial flavor-masking substitute to legally claim \"No Added MSG\" on the front label.'"
    },
    {
      id: "viva-48",
      category: "Scoring Algorithm",
      question: "Explain the Blended Nutrition score calculation in RulesEngine.kt.",
      beginner: "Raw nutrition gives bonuses for protein and fiber and penalties for high calories and saturated fat. Then, that score is averaged together with the Sugar and Sodium scores: `(RawNutrition + SugarScore + SodiumScore) / 3`.",
      panel: "Mathematical Blending: In RulesEngine.kt:\n`var nutritionScore = 100`\n`if (calories > 350f) nutritionScore -= 15`\n`if (proteinGrams > 8f) nutritionScore += 10`\n`if (fiberGrams > 3f) nutritionScore += 10`\n`if (saturatedFatGrams > 5f) nutritionScore -= 15`\n`nutritionScore = (nutritionScore + sugarScore + sodiumScore) / 3`\n`nutritionScore = max(10, min(100, nutritionScore))`\nThis prevents a high-sugar candy bar from scoring high in nutrition merely by fortifying it with a trace amount of protein powder.",
      trap: "Examiner follow-up: 'Why blend sugar and sodium into nutrition when they already have their own dimensions?' Answer: 'Because a food product cannot be considered fundamentally \"nutritious\" if it is saturated with sodium and sugar; blending prevents misleading isolated high scores.'"
    },
    {
      id: "viva-49",
      category: "Scoring Algorithm",
      question: "What is the clamping function used in RulesEngine and why is it necessary?",
      beginner: "We use `max(10, min(100, score))`. This guarantees that no score can ever go higher than 100 or lower than 10, preventing mathematical bugs or negative numbers.",
      panel: "Domain Boundary Enforcement: Clamping via `max(10, min(100, x))` enforces that every score dimension resides strictly in the closed interval [10, 100]. Without clamping, an ultra-processed product with 60g of sugar and 2000mg of sodium would produce negative scores (e.g. -150), distorting the arithmetic mean and breaking downstream visual progress bar scaling.",
      trap: "Examiner follow-up: 'Why clamp the lower bound to 10 instead of 0?' Answer: 'In consumer psychometrics, a floor of 10 maintains visible bar renders and aligns with standard grading baselines where 10 represents the lowest tier (Grade F) rather than a null value.'"
    },
    {
      id: "viva-50",
      category: "Multi-LLM Orchestration",
      question: "What is the purpose of the 3 explanation modes in FullScreenChatView?",
      beginner: "1) Simple Mode: Explains in 2-3 short bullet points using kitchen analogies (like 'contains 4 teaspoons of sugar'). 2) Detailed Mode: Shows clear breakdowns. 3) Scientific Mode: Explains exact biochemical mechanisms like glycemic spikes and arterial plaque.",
      panel: "Audience-Tailored Cognitive Granularity: In ResultScreen.kt and GeminiClient.kt, `explanationMode` injects an operational prompt directive: 1) Simple: Everyday vernacular, practical analogies. 2) Detailed: Quantitative nutritional ratios and daily intake limits. 3) Scientific: Exact biochemical pathways (e.g. lipid peroxidation, insulin resistance, LDL upregulation, E-number chemical synthesis) formatted strictly via '### Parameter' headers.",
      trap: "Examiner follow-up: 'Does changing the mode trigger a new API call?' Answer: 'Yes, Sir. When the user submits a chat query, the active mode token is prepended to the user message: `[\${explanationMode} Mode] \${input}`, directing the LLM's response style.'"
    },
    {
      id: "viva-51",
      category: "Multi-LLM Orchestration",
      question: "How does GeminiClient handle rate limits and 429 Too Many Requests errors?",
      beginner: "If one AI model is busy or hits a rate limit, our code automatically tries the next backup model in its list.",
      panel: "Candidate Fallback Recursion: In GeminiClient.kt, candidate model arrays (e.g. `llama-3.3-70b-versatile`, `llama-3.1-8b-instant`, `llama-3.3-70b-specdec`, `qwen3.6-27b`) are evaluated via recursive function `tryChatModel(modelIdx)`. If the HTTP response is 404, 400, or contains rate limit signatures, `tryChatModel(modelIdx + 1)` is invoked immediately, guaranteeing high availability.",
      trap: "Examiner follow-up: 'What happens when all candidate models are exhausted?' Answer: 'The callback delivers a structured failure Result containing the aggregated error log from each model attempt, which displays in an AlertDialog.'"
    },
    {
      id: "viva-52",
      category: "Multi-LLM Orchestration",
      question: "Why did you use Gemini 2.0 Flash instead of Gemini 1.5 Pro?",
      beginner: "Gemini 2.0 Flash is much faster (under 1 second response time), uses less memory, supports native JSON output, and costs significantly less while providing excellent accuracy.",
      panel: "Latency & Throughput Optimization: Mobile shopping requires near-instantaneous feedback. Gemini 2.0 Flash offers sub-second time-to-first-token (TTFT), native support for `responseMimeType = \"application/json\"`, and superior vision processing efficiency compared to high-latency Pro models.",
      trap: "Examiner follow-up: 'Does Gemini 2.0 Flash have lower reasoning capabilities than Pro?' Answer: 'For structured food audit JSON generation, extensive mathematical reasoning is handled by our local RulesEngine; the LLM only performs summarization and formatting, where 2.0 Flash excels.'"
    },
    {
      id: "viva-53",
      category: "APIs & Networking",
      question: "How does NutriCheck handle device network loss during a scan?",
      beginner: "If you lose Wi-Fi or mobile data, the app catches the connection error and shows an alert saying 'Network connection failure' instead of crashing.",
      panel: "Exception Handling in BarcodeScannerScreen.kt and GeminiClient.kt: Network calls wrap execution in `try { ... } catch (e: IOException) { onError(\"Network connection failure: \${e.message}\") }`. The error callback is marshaled back to the UI thread via `ContextCompat.getMainExecutor(context)` to display an AlertDialog and reset the `isAnalyzing` loading state.",
      trap: "Examiner follow-up: 'Could you cache previous Open Food Facts results offline?' Answer: 'Yes, Sir! Scanned products are already cached in scan_history via SharedPreferences; if a previously scanned barcode is revisited offline, it can be loaded directly from local history.'"
    },
    {
      id: "viva-54",
      category: "APIs & Networking",
      question: "What is JSON and why is it preferred over XML for mobile APIs?",
      beginner: "JSON is a lightweight text format that is easy for computers to parse and humans to read. It uses fewer characters than XML, making it faster to download over mobile networks.",
      panel: "JavaScript Object Notation (JSON) is a text-based open standard for serialized data exchange. Compared to XML, JSON has lower syntactic overhead (no redundant closing tags), maps directly to primitive native data structures (key-value maps and arrays), and consumes less mobile bandwidth and CPU during deserialization via Google Gson.",
      trap: "Examiner follow-up: 'What JSON parser does Android include natively?' Answer: 'Android provides the `org.json` package (JSONObject, JSONArray) built into the platform SDK, which we use extensively alongside Google Gson.'"
    },
    {
      id: "viva-55",
      category: "APIs & Networking",
      question: "Why did you include a custom User-Agent in Open Food Facts requests?",
      beginner: "Open Food Facts is a free community service. They require every app to identify itself with a custom User-Agent so they can manage traffic. If you don't send one, they block your app.",
      panel: "API Policy Compliance: Open Food Facts terms of service strictly enforce that all automated and mobile clients identify themselves via the HTTP `User-Agent` header. In BarcodeScannerScreen.kt, we supply: `User-Agent: PureCheck - Android - Version 2.0 - CSE Major Project`. Anonymous requests with default OkHttp user-agents are rejected with HTTP 403 Forbidden.",
      trap: "Examiner follow-up: 'What happens if millions of users use your User-Agent?' Answer: 'For large commercial deployments, Open Food Facts requests that apps register for a dedicated commercial mirror or donate server infrastructure.'"
    },
    {
      id: "viva-56",
      category: "Database & Storage",
      question: "Where are SharedPreferences files stored on the Android filesystem?",
      beginner: "They are stored in a private folder inside the phone's internal storage: `/data/data/com.example.purecheck/shared_prefs/purecheck_prefs.xml`.",
      panel: "Physical Filesystem Path: Android sandboxes private application storage at `/data/data/<package_name>/shared_prefs/<name>.xml`. For NutriCheck, this corresponds to `/data/data/com.example.purecheck/shared_prefs/purecheck_prefs.xml`. Linux file permissions (`-rw-rw----`) restrict access strictly to the application's assigned Linux User ID (UID).",
      trap: "Examiner follow-up: 'Can another app on the same phone read your SharedPreferences?' Answer: 'No, Sir. Android's Linux kernel process isolation and sandboxing prevent other applications from accessing private internal storage on non-rooted devices.'"
    },
    {
      id: "viva-57",
      category: "Database & Storage",
      question: "How does the custom Watchlist feature store and check ingredients?",
      beginner: "In Profile, you can type forbidden words like 'Peanuts' or 'Gluten'. They are saved in a JSON list. When a label is scanned, the Rules Engine checks if any of your watchlist words appear on the label and deducts 25 points for each match.",
      panel: "In PureCheckPreferences.kt, `getWatchlist()` deserializes a JSON array of strings (`prefs.getString(\"watchlist\", \"[]\")`). In RulesEngine.kt: `for (item in watchlist) { if (item.isNotBlank() && lowerText.contains(item.lowercase().trim())) matchedWatchlist.add(item.trim()) }`. Each matched token triggers `profileScore -= (matchedWatchlist.size * 25)` and logs a '✕ WATCHLIST CONFLICT' finding.",
      trap: "Examiner follow-up: 'What if the user adds duplicate words to the watchlist?' Answer: 'In PureCheckPreferences.addToWatchlist(), we check `if (!list.any { it.equals(cleanedItem, ignoreCase = true) })` before inserting, preventing duplicate entries.'"
    },
    {
      id: "viva-58",
      category: "Database & Storage",
      question: "What happens when scan history reaches 50 items?",
      beginner: "The oldest scan at the bottom of the list is automatically deleted so the phone storage never gets cluttered.",
      panel: "Bounded FIFO Queue: In `PureCheckPreferences.addHistoryItem()`, if `history.size > 50`, `history.removeAt(history.size - 1)` is called before writing back to SharedPreferences. This guarantees an O(1) bound on history storage size and eliminates memory leaks.",
      trap: "Examiner follow-up: 'Why not allow unlimited history items?' Answer: 'Because SharedPreferences loads the entire XML file into memory upon initialization; an unbounded list would eventually cause startup latency and heap bloat.'"
    },
    {
      id: "viva-59",
      category: "Android Build System",
      question: "What is Gradle and what is the difference between build.gradle (Groovy) and build.gradle.kts (Kotlin)?",
      beginner: "Gradle is the automated build tool that compiles our Kotlin code, downloads libraries, and packages everything into an APK. `build.gradle.kts` uses Kotlin script, giving us code completion and compile-time error checking that old Groovy scripts lacked.",
      panel: "Gradle Build Automation: Gradle compiles source code, links dependencies from Maven Central / Google Maven, runs AAPT2 resource compilation, and executes DEX bytecode packaging. Kotlin DSL (`.kts`) provides strong static typing, IDE autocompletion in Android Studio, and eliminates dynamic runtime script errors common in legacy Groovy build files.",
      trap: "Examiner follow-up: 'What is libs.versions.toml in your gradle folder?' Answer: 'It is Gradle's standard Version Catalog file, centralizing library versions and dependency coordinates across all submodules in one declarative place.'"
    },
    {
      id: "viva-60",
      category: "Android Build System",
      question: "What is DEX compilation in Android?",
      beginner: "Android phones don't run standard Java bytecode. Android has its own optimized virtual machine (ART). DEX compilation turns Java/Kotlin `.class` files into `.dex` (Dalvik Executable) files that run fast on mobile.",
      panel: "Dalvik Executable (DEX) Transformation: The Kotlin compiler generates JVM `.class` bytecode. The Android D8/R8 compiler then transpiles this bytecode into optimized Dalvik Executable (`classes.dex`) format, register-based and optimized for mobile memory constraints and Android Runtime (ART) Ahead-Of-Time (AOT) and Just-In-Time (JIT) execution.",
      trap: "Examiner follow-up: 'What is MultiDex and does NutriCheck need it?' Answer: 'A single DEX file has a 65,536 method reference limit. Because our minSdk is 24, MultiDex is natively supported by the Android OS and enabled automatically.'"
    },
    {
      id: "viva-61",
      category: "Android Build System",
      question: "What is an Android Manifest file and why is it mandatory?",
      beginner: "It is the root passport of your app. It tells the Android operating system the app's name, which permissions it needs (Camera, Internet), and which screen to open first (`MainActivity`).",
      panel: "`AndroidManifest.xml` is the binary XML metadata descriptor required by the Android OS package manager. It declares the app's namespace (`com.example.purecheck`), hardware requirements (`android.hardware.camera`), runtime permissions, application themes, and entry activity with `<category android:name=\"android.intent.category.LAUNCHER\" />`.",
      trap: "Examiner follow-up: 'What is android:exported=\"true\" on MainActivity?' Answer: 'Since Android 12 (API 31), any activity with an intent-filter MUST explicitly set `android:exported=\"true\"` so the Android OS launcher can start it; setting it to false prevents the app from launching.'"
    },
    {
      id: "viva-62",
      category: "Testing & QA",
      question: "How do you test camera features on an Android emulator that has no physical camera?",
      beginner: "Android Studio's emulator has a Virtual Scene camera mode that simulates walking around a virtual room with posters, or you can feed a static image file into the virtual camera.",
      panel: "Emulator Camera Emulation: The Android Virtual Device (AVD) manager allows configuring the rear camera to 'VirtualScene' or 'Webcam0'. VirtualScene generates a 3D navigable room containing barcode posters and nutrition labels, allowing full CameraX and ML Kit validation without a physical device.",
      trap: "Examiner follow-up: 'Did you test NutriCheck on physical hardware?' Answer: 'Yes, Sir! We tested on physical Android smartphones to verify real CameraX optical autofocus, LED flashlight toggling, and physical retail label scanning under variable lighting.'"
    },
    {
      id: "viva-63",
      category: "Testing & QA",
      question: "What is Logcat and how did you use it during development?",
      beginner: "Logcat is Android's real-time console log viewer. When our app runs, it prints debug messages so we can see network responses, barcode digits, and error stack traces.",
      panel: "Android Logging Framework: `android.util.Log` outputs diagnostic messages categorized by priority (V=Verbose, D=Debug, I=Info, W=Warn, E=Error) to the system circular buffer. In our testing, we filter via `adb logcat -s PureCheck:V GeminiClient:V` to monitor OCR execution latency, OkHttp network response codes, and RulesEngine scoring breakdowns.",
      trap: "Examiner follow-up: 'Should verbose logging remain enabled in production release builds?' Answer: 'No, Sir. Production release builds should disable verbose logging via ProGuard/R8 rules or Timber trees to prevent sensitive data leakage.'"
    },
    {
      id: "viva-64",
      category: "Limitations & Roadmap",
      question: "How will you handle multilingual labels (e.g. Hindi, Telugu) in Part 2?",
      beginner: "Currently we use ML Kit's Latin model for English labels. In Part 2, we will add ML Kit's Devanagari and regional Indic script language packs to recognize Indian regional languages.",
      panel: "Linguistic Expansion Architecture: Google ML Kit offers specialized script recognition packages (e.g. `com.google.mlkit:text-recognition-devanagari`). In Part 2, we will implement multi-script language detection, routing Hindi/Indic characters to localized translation pipelines and expanding `IngredientDatabase.kt` with regional food aliases (e.g. 'Maida' for refined flour, 'Ghee' for clarified butter).",
      trap: "Examiner follow-up: 'Will adding Indic OCR increase APK size?' Answer: 'If using bundled models, each script adds approximately 4-6MB; if using Google Play Services dynamic delivery, the model is downloaded on-demand over the air with zero initial APK footprint.'"
    },
    {
      id: "viva-65",
      category: "Limitations & Roadmap",
      question: "Why does the app currently focus on packaged food rather than restaurant meals?",
      beginner: "Packaged foods have standardized legal ingredient labels on the back that our OCR can read with high accuracy. Restaurant meals don't have ingredient labels, so analyzing them requires speculative visual guessing.",
      panel: "Verification Ground Truth: Packaged foods provide a legally mandated, standardized ingredient label and nutritional panel that serves as ground truth for deterministic scoring. Estimating calories from a photo of a restaurant curry or plate of biryani involves massive volumetric estimation errors and speculative ingredient guessing that violates our Zero-Hallucination Safety Protocol.",
      trap: "Examiner follow-up: 'Could computer vision estimate recipe ingredients in the future?' Answer: 'Yes, but it would remain a probabilistic estimate; for medical dietary safety (diabetic/allergen auditing), verified packaged labels remain the gold standard.'"
    },
    {
      id: "viva-66",
      category: "Limitations & Roadmap",
      question: "Summarize in one sentence why your guide and panel should approve NutriCheck Part 1.",
      beginner: "NutriCheck is an authentic, production-grade Android application that replaces food label confusion with transparent, mathematically proven health grades and interactive AI guidance.",
      panel: "NutriCheck successfully implements an end-to-end edge vision, on-device OCR, and deterministic mathematical scoring platform that solves retail consumer information asymmetry with zero-hallucination architectural integrity.",
      trap: "Examiner follow-up: 'Are you ready for your demonstration?' Answer: 'Yes, Sir! Our application is installed and ready for live physical packaging scans.'"
    }
  ],

  glossary: [
    { term: "CameraX", category: "Android", def: "A Google Jetpack support library built on top of Camera2 that provides a consistent, lifecycle-aware camera experience across 98%+ of Android devices." },
    { term: "Jetpack Compose", category: "Frontend", def: "Android's modern, fully declarative UI toolkit that accelerates UI development with concise Kotlin code, state-driven recomposition, and no XML layout files." },
    { term: "Navigation 3 (Nav3)", category: "Android", def: "Google's newest type-safe Android navigation library featuring polymorphic NavKey serializable objects and compile-time route verification." },
    { term: "ML Kit", category: "Machine Learning", def: "Google's suite of on-device machine learning SDKs for mobile developers, providing pre-trained models for text recognition, barcode scanning, and face detection that run with zero network latency." },
    { term: "OCR (Optical Character Recognition)", category: "Computer Vision", def: "The computational process of converting two-dimensional digital images containing text glyphs into machine-encoded character strings." },
    { term: "E-Number", category: "Biochemistry", def: "Standardized alphanumeric codes established by the European Union and adopted by the International Numbering System (INS) to identify chemical food additives." },
    { term: "Obfuscated Ingredient", category: "Biochemistry", def: "A generic or deceptive label term used by food manufacturers to mask low-quality or controversial ingredients (e.g. 'Refined Vegetable Oil' used to conceal Palm Oil)." },
    { term: "RulesEngine", category: "Architecture", def: "The local, mathematically deterministic Kotlin evaluation engine in NutriCheck that calculates 5-axis food safety and nutritional scores without depending on AI models." },
    { term: "Zero-Hallucination Protocol", category: "Architecture", def: "NutriCheck's core design principle where scores are calculated locally via pure mathematics, and cloud LLMs are strictly forbidden from recalculating or inventing numerical ratings." },
    { term: "Open Food Facts", category: "APIs", def: "A collaborative, open global database of food products, packaging ingredients, and nutritional declarations accessible via a public REST API." },
    { term: "Gemini 2.0 Flash", category: "AI Models", def: "Google's state-of-the-art high-speed multimodal generative AI model optimized for structured JSON generation and real-time vision-language reasoning." },
    { term: "Groq Cloud", category: "AI Models", def: "An ultra-fast cloud AI inference provider running open-weights LLMs (such as Meta LLaMA 3.3) on custom Language Processing Units (LPUs)." },
    { term: "SharedPreferences", category: "Android", def: "Android's native persistent key-value storage framework used to save application preferences, user biometric profiles, and serialized JSON records." },
    { term: "Gson", category: "Data", def: "A Java/Kotlin serialization library developed by Google that converts Java/Kotlin objects into their JSON representation and vice versa." },
    { term: "OkHttp 3", category: "Networking", def: "An efficient, production-grade HTTP & HTTP/2 client for Android and Java applications, supporting connection pooling, transparent compression, and request timeouts." },
    { term: "BMI (Body Mass Index)", category: "Health", def: "A person's weight in kilograms divided by the square of height in meters (kg/m²), used in NutriCheck's profile engine to contextualize nutritional advice." },
    { term: "Carrageenan (E407)", category: "Biochemistry", def: "A red seaweed extract used as a thickening agent; catalogued in NutriCheck as a Medium-evidence gut irritant linked to intestinal inflammation." },
    { term: "High Fructose Corn Syrup (HFCS)", category: "Biochemistry", def: "A liquid corn sweetener high in free fructose; catalogued in NutriCheck as a High-evidence dietary risk promoting visceral fat and insulin resistance." },
    { term: "Unidirectional Data Flow (UDF)", category: "Architecture", def: "A design pattern where state flows downward through the component hierarchy and events flow upward to state holders." },
    { term: "ProGuard / R8", category: "Android", def: "The Android build toolchain optimizer that performs dead code elimination, name obfuscation, and bytecode shrinking on release APKs." }
  ]
};

if (typeof window !== "undefined") {
  window.NutriCheckData = NutriCheckData;
}
if (typeof global !== "undefined") {
  global.NutriCheckData = NutriCheckData;
}
if (typeof module !== "undefined" && module.exports) {
  module.exports = { NutriCheckData };
}
