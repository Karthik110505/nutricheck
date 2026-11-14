package com.example.purecheck.data

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
            if (matched) {
                foundAdditives.add(def)
            }
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
        
        // Extra penalties for known problematic ones
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
            if (lowerText.contains("sugar") || lowerText.contains("sucrose")) {
                sugarScore = 75
            }
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
            if (lowerText.contains("salt") || lowerText.contains("sodium")) {
                sodiumScore = 75
            }
        }
        sodiumScore = max(10, min(100, sodiumScore))

        // 7. Compute Nutrition Score (Base 100)
        var nutritionScore = 100
        if (calories > 350f) {
            nutritionScore -= 15
        }
        if (proteinGrams > 8f) {
            nutritionScore += 10
        }
        if (fiberGrams > 3f) {
            nutritionScore += 10
        }
        if (saturatedFatGrams > 5f) {
            nutritionScore -= 15
        }
        // Average with sugar and sodium scores
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

        // Add findings
        if (sugarScore < 60) keyFindings.add("⚠ High sugar content.")
        if (sodiumScore < 60) keyFindings.add("⚠ High sodium content.")
        if (additivesCount == 0) keyFindings.add("✓ Free from artificial colors, preservatives, and flavor enhancers.")
        if (qualityScore > 85) keyFindings.add("✓ High ingredient quality.")

        // 9. Compute Personalized Profile Score
        var profileScore = overallScore
        val conflicts = mutableListOf<String>()

        if (profile.vegan) {
            val nonVeganMatched = mutableListOf<String>()
            val nonVeganTerms = mapOf(
                "egg" to "EGG",
                "milk" to "MILK",
                "whey" to "WHEY",
                "casein" to "CASEIN",
                "gelatin" to "GELATIN",
                "meat" to "MEAT",
                "chicken" to "CHICKEN",
                "beef" to "BEEF",
                "pork" to "PORK",
                "mutton" to "MUTTON",
                "fish" to "FISH",
                "shrimp" to "SHRIMP",
                "seafood" to "SEAFOOD",
                "carmine" to "CARMINE",
                "lactose" to "LACTOSE",
                "honey" to "HONEY",
                "dairy" to "DAIRY",
                "butter" to "BUTTER",
                "cheese" to "CHEESE",
                "cream" to "CREAM",
                "anchovy" to "ANCHOVY",
                "lard" to "LARD",
                "tallow" to "TALLOW",
                "bone" to "BONE",
                "cochineal" to "COCHINEAL"
            )
            for ((term, label) in nonVeganTerms) {
                if (lowerText.contains(term)) {
                    nonVeganMatched.add(label)
                }
            }
            val hasAnimalAdditives = foundAdditives.any { it.dietaryFlags.contains("vegan_conflict") }
            if (hasAnimalAdditives && !nonVeganMatched.contains("ANIMAL ADDITIVES")) {
                nonVeganMatched.add("ANIMAL ADDITIVES")
            }
            if (nonVeganMatched.isNotEmpty()) {
                profileScore -= 30
                conflicts.add("✕ VEGAN CONFLICT: This product contains: ${nonVeganMatched.joinToString(", ")}. Your Vegan preference is enabled. Recommendation: Avoid this product.")
            }
        } else if (profile.vegetarian) {
            val nonVegMatched = mutableListOf<String>()
            val nonVegTerms = mapOf(
                "meat" to "MEAT",
                "chicken" to "CHICKEN",
                "beef" to "BEEF",
                "pork" to "PORK",
                "mutton" to "MUTTON",
                "fish" to "FISH",
                "shrimp" to "SHRIMP",
                "seafood" to "SEAFOOD",
                "carmine" to "CARMINE",
                "gelatin" to "GELATIN",
                "anchovy" to "ANCHOVY",
                "lard" to "LARD",
                "tallow" to "TALLOW",
                "bone" to "BONE",
                "cochineal" to "COCHINEAL"
            )
            for ((term, label) in nonVegTerms) {
                if (lowerText.contains(term)) {
                    nonVegMatched.add(label)
                }
            }
            if (nonVegMatched.isNotEmpty()) {
                profileScore -= 20
                conflicts.add("✕ VEGETARIAN CONFLICT: This product contains: ${nonVegMatched.joinToString(", ")}. Your Vegetarian preference is enabled. Recommendation: Avoid this product.")
            }
        }

        if (profile.diabetic) {
            if (sugarGrams > 10f || lowerText.contains("corn syrup") || lowerText.contains("aspartame") || lowerText.contains("high fructose")) {
                profileScore -= 20
                conflicts.add("✕ DIABETIC CONFLICT: Contains high sugars/glycemic agents. Conflicts with your Diabetic-Friendly preference.")
            }
        }

        if (profile.heartHealth) {
            if (sodiumMg > 360f || lowerText.contains("palm oil") || lowerText.contains("hydrogenated")) {
                profileScore -= 20
                conflicts.add("✕ HEART HEALTH CONFLICT: Contains high sodium or trans-fats. Conflicts with your Heart Health preference.")
            }
        }

        if (profile.gutHealth) {
            val gutIrritants = foundAdditives.filter { it.dietaryFlags.contains("gut_risk") }
            if (gutIrritants.isNotEmpty()) {
                profileScore -= 15
                conflicts.add("✕ GUT HEALTH CONFLICT: Contains gut emulsifiers/stabilizers like ${gutIrritants.joinToString { it.name }}. Conflicts with your Gut Health preference.")
            }
        }

        if (profile.cosmeticSafe) {
            val skinIrritants = foundAdditives.filter { it.dietaryFlags.contains("skin_risk") }
            if (skinIrritants.isNotEmpty()) {
                profileScore -= 20
                conflicts.add("✕ SKIN/COSMETIC CONFLICT: Contains parabens or sulfates. Conflicts with your Skin/Cosmetic Safety preference.")
            }
        }

        if (profile.lowSugar && (sugarGrams > 10f || (sugarGrams <= 0f && (lowerText.contains("sugar") || lowerText.contains("syrup"))))) {
            profileScore -= 15
            conflicts.add("⚠ LOW SUGAR ALERT: High sugar content detected. Conflicts with your Low Sugar preference.")
        }

        if (profile.lowSodium && (sodiumMg > 300f || (sodiumMg <= 0f && (lowerText.contains("salt") || lowerText.contains("sodium"))))) {
            profileScore -= 15
            conflicts.add("⚠ LOW SODIUM ALERT: High sodium content detected. Conflicts with your Low Sodium preference.")
        }

        if (matchedWatchlist.isNotEmpty()) {
            profileScore -= (matchedWatchlist.size * 25)
            conflicts.add("✕ WATCHLIST CONFLICT: Contains ingredients on your watchlist: ${matchedWatchlist.joinToString(", ")}.")
        }

        profileScore = max(10, min(overallScore, profileScore))

        return DeterministicResult(
            overallScore = overallScore,
            overallGrade = overallGrade,
            breakdown = ScoreBreakdown(
                ingredientQuality = qualityScore,
                nutrition = nutritionScore,
                additives = additivesScore,
                sugar = sugarScore,
                sodium = sodiumScore
            ),
            profileScore = profileScore,
            matchedWatchlist = matchedWatchlist,
            matchedAdditives = foundAdditives,
            keyFindings = keyFindings,
            conflicts = conflicts
        )
    }
}
