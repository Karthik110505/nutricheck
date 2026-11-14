package com.example.purecheck.utils

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
            watchlist = emptyList(), // Checked in preferences, or pass custom list if needed
            profile = profile
        )

        // Build instructions based on user profile and deterministic calculations
        val deterministicContext = """
            DETERMINISTIC ANALYSIS RESULTS:
            - Overall Score: ${ruleResult.overallScore}
            - Overall Grade: ${ruleResult.overallGrade}
            - Score Breakdown:
              - Ingredient Quality: ${ruleResult.breakdown.ingredientQuality}
              - Nutrition: ${ruleResult.breakdown.nutrition}
              - Additives: ${ruleResult.breakdown.additives}
              - Sugar: ${ruleResult.breakdown.sugar}
              - Sodium: ${ruleResult.breakdown.sodium}
            - Flagged Additives: ${ruleResult.matchedAdditives.joinToString { "${it.name} (${it.eNumber ?: "no E-number"})" }}
            - Key Findings: ${ruleResult.keyFindings.joinToString()}
            - Conflicts detected: ${ruleResult.conflicts.joinToString()}
        """.trimIndent()

        val systemInstruction = """
            You are an expert food safety inspector, biochemist, and cosmetic auditor. Your job is to analyze the ingredients list image provided and return a comprehensive health and transparency audit.
            
            YOU MUST USE THESE EXACT CALCULATED VALUES FOR YOUR REPORT:
            overallScore: ${ruleResult.overallScore}
            overallGrade: "${ruleResult.overallGrade}"
            scoreBreakdown: {
              "ingredientQuality": ${ruleResult.breakdown.ingredientQuality},
              "nutrition": ${ruleResult.breakdown.nutrition},
              "additives": ${ruleResult.breakdown.additives},
              "sugar": ${ruleResult.breakdown.sugar},
              "sodium": ${ruleResult.breakdown.sodium}
            }
            Do not recalculate or invent these scores. Your summary and analysis MUST explain these specific numbers.

            CRITICAL LANGUAGE REQUIREMENT:
            - Write all descriptions, reasons, summaries, and titles in VERY SIMPLE English that an ordinary consumer can easily understand.
            - Avoid complex technical or medical jargon. If you MUST mention a scientific term, define it in very simple words in parentheses immediately.
            - Keep explanations extremely short, direct, and simple.

            Return ONLY a JSON object that matches the following structure:
            {
              "productName": "String (Estimated product name or category based on packaging)",
              "overallGrade": "${ruleResult.overallGrade}",
              "overallScore": ${ruleResult.overallScore},
              "confidenceScore": 95,
              "scoreBreakdown": {
                "ingredientQuality": ${ruleResult.breakdown.ingredientQuality},
                "nutrition": ${ruleResult.breakdown.nutrition},
                "additives": ${ruleResult.breakdown.additives},
                "sugar": ${ruleResult.breakdown.sugar},
                "sodium": ${ruleResult.breakdown.sodium}
              },
              "summary": "String (A 2-3 sentence overall summary explaining the score and grade.)",
              "keyFindings": ["List of findings matching the deterministic findings"],
              "healthRisks": ["List of profile conflict reasons or health risks"],
              "obfuscatedIngredients": [
                {
                  "printed": "String (e.g., 'Refined Vegetable Oil')",
                  "actual": "String (e.g., 'Palm Oil')",
                  "reason": "String (Why they use this label, health/ecological impact.)",
                  "severity": "high" | "medium" | "low"
                }
              ],
              "ingredientAnalysis": [
                {
                  "name": "String (Ingredient name)",
                  "rating": "safe" | "caution" | "unsafe",
                  "description": "String (Brief 1-2 sentence description explaining what it is.)"
                }
              ],
              "nutritionAnalysis": {
                "calories": 0,
                "protein": 0,
                "carbs": 0,
                "fat": 0,
                "fiber": 0,
                "sugar": 0,
                "sodium": 0,
                "saturatedFat": 0
              },
              "cleanSwaps": [
                {
                  "brand": "String",
                  "product": "String",
                  "reason": "String"
                }
              ]
            }

            Deterministic context:
            $deterministicContext
            
            Do not wrap the JSON in Markdown code block tags.
        """.trimIndent()

        if (provider.lowercase() == "groq" || provider.lowercase() == "bytez") {
            val isBytez = provider.lowercase() == "bytez"
            val candidateVisionModels = if (isBytez) listOf("meta/llama-3.2-11b-vision-instruct") 
                                         else listOf("llama-3.2-11b-vision-preview", "qwen/qwen3.6-27b")
            val errorLogs = mutableListOf<String>()

            fun tryVisionModel(modelIdx: Int) {
                if (modelIdx >= candidateVisionModels.size) {
                    callback(Result.failure(Exception("All candidate vision models failed for $provider API:\n" + errorLogs.joinToString("\n"))))
                    return
                }

                val currentModel = candidateVisionModels[modelIdx]
                val requestJson = JSONObject()
                requestJson.put("model", currentModel)
                
                val messagesArray = JSONArray()
                val messageObject = JSONObject()
                messageObject.put("role", "user")
                
                val contentArray = JSONArray()
                
                val textPart = JSONObject()
                textPart.put("type", "text")
                textPart.put("text", systemInstruction)
                contentArray.put(textPart)
                
                val imagePart = JSONObject()
                imagePart.put("type", "image_url")
                val imageUrl = JSONObject()
                imageUrl.put("url", "data:image/jpeg;base64,$base64Image")
                imagePart.put("image_url", imageUrl)
                contentArray.put(imagePart)
                
                messageObject.put("content", contentArray)
                messagesArray.put(messageObject)
                requestJson.put("messages", messagesArray)
                
                val responseFormat = JSONObject()
                responseFormat.put("type", "json_object")
                requestJson.put("response_format", responseFormat)
                
                val requestBody = requestJson.toString().toRequestBody(JSON)
                val apiUrl = if (isBytez) "https://api.bytez.com/models/v2/openai/v1/chat/completions" else "https://api.groq.com/openai/v1/chat/completions"
                
                val request = Request.Builder()
                    .url(apiUrl)
                    .header("Authorization", "Bearer $apiKey")
                    .header("User-Agent", "Mozilla/5.0")
                    .post(requestBody)
                    .build()
                    
                client.newCall(request).enqueue(object : okhttp3.Callback {
                    override fun onFailure(call: okhttp3.Call, e: IOException) {
                        errorLogs.add("- $currentModel network failure: ${e.message}")
                        tryVisionModel(modelIdx + 1)
                    }

                    override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                        response.use {
                            val code = response.code
                            val bodyString = response.body?.string() ?: ""

                            if (!response.isSuccessful) {
                                errorLogs.add("- $currentModel returned code $code: $bodyString")
                                if (code == 404 || code == 400 || bodyString.contains("model_not_found") || bodyString.contains("does not exist")) {
                                    tryVisionModel(modelIdx + 1)
                                } else {
                                    callback(Result.failure(IOException("${provider.replaceFirstChar { it.uppercase() }} error $code: $bodyString")))
                                }
                                return
                            }
                            
                            try {
                                val json = JSONObject(bodyString)
                                val choices = json.getJSONArray("choices")
                                if (choices.length() > 0) {
                                    val message = choices.getJSONObject(0).getJSONObject("message")
                                    val textResult = message.getString("content").trim()
                                    callback(Result.success(sanitizeJson(textResult)))
                                    return
                                }
                                callback(Result.failure(Exception("Empty response from $provider API")))
                            } catch (e: Exception) {
                                callback(Result.failure(e))
                            }
                        }
                    }
                })
            }

            tryVisionModel(0)
        } else {
            // Gemini API Implementation (gemini-2.0-flash)
            val requestJson = JSONObject()
            val contentsArray = JSONArray()
            val contentObject = JSONObject()
            val partsArray = JSONArray()

            val textPart = JSONObject()
            textPart.put("text", systemInstruction)
            partsArray.put(textPart)

            val imagePart = JSONObject()
            val inlineData = JSONObject()
            inlineData.put("mimeType", "image/jpeg")
            inlineData.put("data", base64Image)
            imagePart.put("inlineData", inlineData)
            partsArray.put(imagePart)

            contentObject.put("parts", partsArray)
            contentsArray.put(contentObject)
            requestJson.put("contents", contentsArray)

            val generationConfig = JSONObject()
            generationConfig.put("responseMimeType", "application/json")
            requestJson.put("generationConfig", generationConfig)

            val requestBody = requestJson.toString().toRequestBody(JSON)
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=$apiKey"

            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "Mozilla/5.0")
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    callback(Result.failure(e))
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    response.use {
                        if (!response.isSuccessful) {
                            val bodyString = response.body?.string() ?: ""
                            callback(Result.failure(IOException("Gemini error ${response.code}: $bodyString")))
                            return
                        }

                        val responseBody = response.body?.string() ?: ""
                        try {
                            val json = JSONObject(responseBody)
                            val candidates = json.getJSONArray("candidates")
                            if (candidates.length() > 0) {
                                val firstCandidate = candidates.getJSONObject(0)
                                val content = firstCandidate.getJSONObject("content")
                                val parts = content.getJSONArray("parts")
                                if (parts.length() > 0) {
                                    val textResult = parts.getJSONObject(0).getString("text").trim()
                                    callback(Result.success(sanitizeJson(textResult)))
                                    return
                                }
                            }
                            callback(Result.failure(Exception("Empty response from Gemini API")))
                        } catch (e: Exception) {
                            callback(Result.failure(e))
                        }
                    }
                }
            })
        }
    }

    private fun sanitizeJson(rawText: String): String {
        var text = rawText.trim()
        if (text.startsWith("```")) {
            val lines = text.lines()
            if (lines.size >= 2) {
                text = lines.subList(1, lines.size - 1).joinToString("\n").trim()
            }
        }
        return text
    }

    fun analyzeIngredientsText(
        ingredientsText: String,
        profile: UserProfile,
        provider: String,
        apiKey: String,
        callback: (Result<String>) -> Unit
    ) {
        if (apiKey.isBlank()) {
            callback(Result.failure(Exception("API Key is missing.")))
            return
        }

        // Run local deterministic rules engine first
        val ruleResult = RulesEngine.calculate(
            rawIngredientsText = ingredientsText,
            watchlist = emptyList(),
            profile = profile
        )

        // Build instructions based on user profile and deterministic calculations
        val deterministicContext = """
            DETERMINISTIC ANALYSIS RESULTS:
            - Overall Score: ${ruleResult.overallScore}
            - Overall Grade: ${ruleResult.overallGrade}
            - Score Breakdown:
              - Ingredient Quality: ${ruleResult.breakdown.ingredientQuality}
              - Nutrition: ${ruleResult.breakdown.nutrition}
              - Additives: ${ruleResult.breakdown.additives}
              - Sugar: ${ruleResult.breakdown.sugar}
              - Sodium: ${ruleResult.breakdown.sodium}
            - Flagged Additives: ${ruleResult.matchedAdditives.joinToString { "${it.name} (${it.eNumber ?: "no E-number"})" }}
            - Key Findings: ${ruleResult.keyFindings.joinToString()}
            - Conflicts detected: ${ruleResult.conflicts.joinToString()}
        """.trimIndent()

        val systemInstruction = """
            You are an expert food safety inspector, biochemist, and cosmetic auditor. Your job is to analyze the ingredients list text provided and return a comprehensive health and transparency audit.

            YOU MUST USE THESE EXACT CALCULATED VALUES FOR YOUR REPORT:
            overallScore: ${ruleResult.overallScore}
            overallGrade: "${ruleResult.overallGrade}"
            scoreBreakdown: {
              "ingredientQuality": ${ruleResult.breakdown.ingredientQuality},
              "nutrition": ${ruleResult.breakdown.nutrition},
              "additives": ${ruleResult.breakdown.additives},
              "sugar": ${ruleResult.breakdown.sugar},
              "sodium": ${ruleResult.breakdown.sodium}
            }
            Do not recalculate or invent these scores. Your summary and analysis MUST explain these specific numbers.

            CRITICAL LANGUAGE REQUIREMENT:
            - Write all descriptions, reasons, summaries, and titles in VERY SIMPLE English that an ordinary consumer can easily understand.
            - Avoid complex technical or medical jargon. If you MUST mention a scientific term, define it in very simple words in parentheses immediately.
            - Keep explanations extremely short, direct, and simple.

            Return ONLY a JSON object that matches the following structure:
            {
              "productName": "String (Estimated product name or category based on packaging)",
              "overallGrade": "${ruleResult.overallGrade}",
              "overallScore": ${ruleResult.overallScore},
              "confidenceScore": 95,
              "scoreBreakdown": {
                "ingredientQuality": ${ruleResult.breakdown.ingredientQuality},
                "nutrition": ${ruleResult.breakdown.nutrition},
                "additives": ${ruleResult.breakdown.additives},
                "sugar": ${ruleResult.breakdown.sugar},
                "sodium": ${ruleResult.breakdown.sodium}
              },
              "summary": "String (A 2-3 sentence overall summary explaining the score and grade.)",
              "keyFindings": ["List of findings matching the deterministic findings"],
              "healthRisks": ["List of profile conflict reasons or health risks"],
              "obfuscatedIngredients": [
                {
                  "printed": "String (e.g., 'Refined Vegetable Oil')",
                  "actual": "String (e.g., 'Palm Oil')",
                  "reason": "String (Why they use this label, health/ecological impact.)",
                  "severity": "high" | "medium" | "low"
                }
              ],
              "ingredientAnalysis": [
                {
                  "name": "String (Ingredient name)",
                  "rating": "safe" | "caution" | "unsafe",
                  "description": "String (Brief 1-2 sentence description explaining what it is.)"
                }
              ],
              "nutritionAnalysis": {
                "calories": 0,
                "protein": 0,
                "carbs": 0,
                "fat": 0,
                "fiber": 0,
                "sugar": 0,
                "sodium": 0,
                "saturatedFat": 0
              },
              "cleanSwaps": [
                {
                  "brand": "String",
                  "product": "String",
                  "reason": "String"
                }
              ]
            }

            Deterministic context:
            $deterministicContext

            Do not wrap the JSON in Markdown code block tags.
        """.trimIndent()

        val isDeepSeek = provider.lowercase() == "deepseek"
        val isBytez = provider.lowercase() == "bytez"
        val url = if (isBytez) "https://api.bytez.com/models/v2/openai/v1/chat/completions"
                  else if (isDeepSeek) "https://api.deepseek.com/chat/completions"
                  else "https://api.groq.com/openai/v1/chat/completions"
        val candidateModels = if (isBytez) listOf("meta/llama-3.3-70b-instruct", "meta/llama-3.1-8b-instruct")
                              else if (isDeepSeek) listOf("deepseek-chat")
                              else listOf("llama-3.3-70b-specdec", "llama-3.3-70b-versatile", "llama-3.1-8b-instant", "openai/gpt-oss-120b", "groq/compound", "qwen/qwen3.6-27b")
        val errorLogs = mutableListOf<String>()

        fun tryModelRequest(modelIdx: Int) {
            if (modelIdx >= candidateModels.size) {
                callback(Result.failure(Exception("All candidate models failed for $provider API:\n" + errorLogs.joinToString("\n"))))
                return
            }

            val currentModel = candidateModels[modelIdx]
            val requestJson = JSONObject()
            requestJson.put("model", currentModel)

            val messagesArray = JSONArray()
            
            val systemObj = JSONObject()
            systemObj.put("role", "system")
            systemObj.put("content", systemInstruction)
            messagesArray.put(systemObj)

            val userObj = JSONObject()
            userObj.put("role", "user")
            userObj.put("content", "Here is the raw text scanned from the product label:\n\n$ingredientsText")
            messagesArray.put(userObj)

            requestJson.put("messages", messagesArray)

            val responseFormat = JSONObject()
            responseFormat.put("type", "json_object")
            requestJson.put("response_format", responseFormat)

            val requestBody = requestJson.toString().toRequestBody(JSON)
            val request = Request.Builder()
                .url(url)
                .header("Authorization", "Bearer $apiKey")
                .header("User-Agent", "Mozilla/5.0")
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    errorLogs.add("- $currentModel network failure: ${e.message}")
                    tryModelRequest(modelIdx + 1)
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    response.use {
                        val code = response.code
                        val bodyString = response.body?.string() ?: ""
                        
                        if (!response.isSuccessful) {
                            errorLogs.add("- $currentModel returned code $code: $bodyString")
                            if (code == 404 || code == 400 || bodyString.contains("model_not_found") || bodyString.contains("does not exist")) {
                                tryModelRequest(modelIdx + 1)
                            } else {
                                callback(Result.failure(IOException("${provider.replaceFirstChar { it.uppercase() }} error $code: $bodyString")))
                            }
                            return
                        }

                        try {
                            val json = JSONObject(bodyString)
                            val choices = json.getJSONArray("choices")
                            if (choices.length() > 0) {
                                val message = choices.getJSONObject(0).getJSONObject("message")
                                val textResult = message.getString("content").trim()
                                callback(Result.success(sanitizeJson(textResult)))
                                return
                            }
                            callback(Result.failure(Exception("Empty response from $provider API")))
                        } catch (e: Exception) {
                            callback(Result.failure(e))
                        }
                    }
                }
            })
        }

        tryModelRequest(0)
    }

    fun discussResult(
        scannedResultJson: String,
        chatHistory: List<ChatMessage>,
        provider: String,
        apiKey: String,
        callback: (Result<String>) -> Unit
    ) {
        if (apiKey.isBlank()) {
            callback(Result.failure(Exception("API Key is missing.")))
            return
        }

        val systemPrompt = """
            You are PureCheck's expert Food, Cosmetic & Health Safety Inspector. You are analyzing the following product scan report with the user:
            $scannedResultJson

            CRITICAL MOBILE FORMATTING RULES:
            1. NEVER output Markdown tables with pipes (|) or dashed dividers (|---|). Tables break mobile phone screens and are strictly prohibited.
            2. Instead of tables, format all comparisons, parameters, and assessments using clean structured sections with bullet points:
               ### [Parameter / Nutrient Name]
               • **Amount & DRI:** [Value] ([% Daily Intake])
               • **Health Impact:** [Clear explanation]
            3. Start with a direct, clear verdict (e.g. "**Verdict:** Not recommended for everyday consumption due to high sugar and saturated fat.").
            4. Tailor detail to the active user mode:
               - Simple Mode: Everyday language, 2-3 short bullet points, practical analogies (e.g. teaspoons of sugar).
               - Detailed Mode: Clear breakdown of ingredients, additives, nutrition, and daily safety limits.
               - Scientific Mode: Exact biochemical mechanisms, quantitative DRI/RDA %, E-numbers, metabolic pathways (e.g. glycemic spike, insulin resistance, lipid profile). ALWAYS format using '### Parameter' and bullet points, NEVER a table.
            5. Explain medical jargon in parentheses (e.g. gut/stomach for gastrointestinal, hardening of arteries for atherosclerosis).
        """.trimIndent()

        if (provider.lowercase() == "deepseek") {
            val requestJson = JSONObject()
            requestJson.put("model", "deepseek-chat")

            val messagesArray = JSONArray()

            val systemObj = JSONObject()
            systemObj.put("role", "system")
            systemObj.put("content", systemPrompt)
            messagesArray.put(systemObj)

            for (msg in chatHistory) {
                val msgObj = JSONObject()
                msgObj.put("role", if (msg.isUser) "user" else "assistant")
                msgObj.put("content", msg.text)
                messagesArray.put(msgObj)
            }

            requestJson.put("messages", messagesArray)

            val requestBody = requestJson.toString().toRequestBody(JSON)
            val request = Request.Builder()
                .url("https://api.deepseek.com/chat/completions")
                .header("Authorization", "Bearer $apiKey")
                .header("User-Agent", "Mozilla/5.0")
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    callback(Result.failure(e))
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    try {
                        response.use {
                            val code = response.code
                            val bodyString = response.body?.string() ?: ""
                            if (!response.isSuccessful) {
                                callback(Result.failure(IOException("DeepSeek error $code: $bodyString")))
                                return
                            }

                            val json = JSONObject(bodyString)
                            val choices = json.getJSONArray("choices")
                            if (choices.length() > 0) {
                                val message = choices.getJSONObject(0).getJSONObject("message")
                                val textResult = message.getString("content").trim()
                                callback(Result.success(textResult))
                                return
                            }
                            callback(Result.failure(Exception("Empty response from DeepSeek API")))
                        }
                    } catch (e: Exception) {
                        callback(Result.failure(e))
                    }
                }
            })
        } else if (provider.lowercase() == "groq" || provider.lowercase() == "bytez") {
            val isBytez = provider.lowercase() == "bytez"
            val candidateChatModels = if (isBytez) listOf("meta/llama-3.3-70b-instruct", "meta/llama-3.1-8b-instruct") 
                                       else listOf("llama-3.3-70b-versatile", "llama-3.1-8b-instant", "llama-3.3-70b-specdec", "llama-3.2-11b-vision-preview", "openai/gpt-oss-120b", "groq/compound", "qwen/qwen3.6-27b")
            val errorLogs = mutableListOf<String>()

            fun tryChatModel(modelIdx: Int) {
                if (modelIdx >= candidateChatModels.size) {
                    callback(Result.failure(Exception("All candidate chat models failed for $provider API:\n" + errorLogs.joinToString("\n"))))
                    return
                }

                val currentModel = candidateChatModels[modelIdx]
                val requestJson = JSONObject()
                requestJson.put("model", currentModel)

                val messagesArray = JSONArray()

                val systemObj = JSONObject()
                systemObj.put("role", "system")
                systemObj.put("content", systemPrompt)
                messagesArray.put(systemObj)

                for (msg in chatHistory) {
                    val msgObj = JSONObject()
                    msgObj.put("role", if (msg.isUser) "user" else "assistant")
                    msgObj.put("content", msg.text)
                    messagesArray.put(msgObj)
                }

                requestJson.put("messages", messagesArray)

                val requestBody = requestJson.toString().toRequestBody(JSON)
                val apiUrl = if (isBytez) "https://api.bytez.com/models/v2/openai/v1/chat/completions" else "https://api.groq.com/openai/v1/chat/completions"
                val request = Request.Builder()
                    .url(apiUrl)
                    .header("Authorization", "Bearer $apiKey")
                    .header("User-Agent", "Mozilla/5.0")
                    .post(requestBody)
                    .build()

                client.newCall(request).enqueue(object : okhttp3.Callback {
                    override fun onFailure(call: okhttp3.Call, e: IOException) {
                        errorLogs.add("- $currentModel network failure: ${e.message}")
                        tryChatModel(modelIdx + 1)
                    }

                    override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                        try {
                            response.use {
                                val code = response.code
                                val bodyString = response.body?.string() ?: ""

                                if (!response.isSuccessful) {
                                    errorLogs.add("- $currentModel returned code $code: $bodyString")
                                    if (code == 404 || code == 400 || bodyString.contains("model_not_found") || bodyString.contains("does not exist")) {
                                        tryChatModel(modelIdx + 1)
                                    } else {
                                        callback(Result.failure(IOException("${provider.replaceFirstChar { it.uppercase() }} error $code: $bodyString")))
                                    }
                                    return
                                }

                                val json = JSONObject(bodyString)
                                val choices = json.getJSONArray("choices")
                                if (choices.length() > 0) {
                                    val message = choices.getJSONObject(0).getJSONObject("message")
                                    val textResult = message.getString("content").trim()
                                    callback(Result.success(textResult))
                                    return
                                }
                                callback(Result.failure(Exception("Empty response from $provider API")))
                            }
                        } catch (e: Exception) {
                            callback(Result.failure(e))
                        }
                    }
                })
            }

            tryChatModel(0)
        } else {
            val requestJson = JSONObject()
            val contentsArray = JSONArray()

            val contextObj = JSONObject()
            contextObj.put("role", "user")
            val contextParts = JSONArray()
            val contextText = JSONObject()
            contextText.put("text", systemPrompt)
            contextParts.put(contextText)
            contextObj.put("parts", contextParts)
            contentsArray.put(contextObj)

            val ackObj = JSONObject()
            ackObj.put("role", "model")
            val ackParts = JSONArray()
            val ackText = JSONObject()
            ackText.put("text", "Understood. I have read the scan audit report. I am ready to answer any questions from the user in simple, plain English.")
            ackParts.put(ackText)
            ackObj.put("parts", ackParts)
            contentsArray.put(ackObj)

            for (msg in chatHistory) {
                val msgObj = JSONObject()
                msgObj.put("role", if (msg.isUser) "user" else "model")
                val parts = JSONArray()
                val textObj = JSONObject()
                textObj.put("text", msg.text)
                parts.put(textObj)
                msgObj.put("parts", parts)
                contentsArray.put(msgObj)
            }

            requestJson.put("contents", contentsArray)

            val requestBody = requestJson.toString().toRequestBody(JSON)
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=$apiKey"

            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "Mozilla/5.0")
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    callback(Result.failure(e))
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    try {
                        response.use {
                            val code = response.code
                            val bodyString = response.body?.string() ?: ""
                            if (!response.isSuccessful) {
                                callback(Result.failure(IOException("Gemini error $code: $bodyString")))
                                return
                            }

                            val json = JSONObject(bodyString)
                            val candidates = json.getJSONArray("candidates")
                            if (candidates.length() > 0) {
                                val firstCandidate = candidates.getJSONObject(0)
                                val content = firstCandidate.getJSONObject("content")
                                val parts = content.getJSONArray("parts")
                                if (parts.length() > 0) {
                                    val textResult = parts.getJSONObject(0).getString("text").trim()
                                    callback(Result.success(textResult))
                                    return
                                }
                            }
                            callback(Result.failure(Exception("Empty response from Gemini API")))
                        }
                    } catch (e: Exception) {
                        callback(Result.failure(e))
                    }
                }
            })
        }
    }
}

data class ChatMessage(
    val isUser: Boolean,
    val text: String
)
