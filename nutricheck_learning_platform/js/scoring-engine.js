/**
 * NutriCheck Deterministic Rules Engine (JavaScript Port)
 * Direct 1:1 port of com.example.purecheck.data.RulesEngine and IngredientDatabase
 * Grounded in actual source code from Major_Project_Part1
 */

const IngredientDatabase = {
  dictionary: [
    {
      name: "Monosodium Glutamate",
      aliases: ["MSG", "Yeast Extract", "Hydrolyzed Vegetable Protein", "Sodium Glutamate"],
      eNumber: "E621",
      category: "Flavour Enhancer",
      function: "Enhances savory/umami flavor in food.",
      description: "A widely used flavor enhancer. In sensitive individuals, it may trigger minor symptoms, though overall evidence for long-term harm is low.",
      potentialConcerns: ["Sensitivities in specific individuals (headaches, flushing).", "Often used to mask low-quality ingredients."],
      evidenceLevel: "Low",
      dietaryFlags: ["diabetic_risk"]
    },
    {
      name: "Carrageenan",
      aliases: ["Carrageenan Gum", "Irish Moss Extract"],
      eNumber: "E407",
      category: "Thickener / Stabilizer",
      function: "Improves texture and gel-like consistency in dairy and plant milks.",
      description: "Derived from red seaweed. Some animal studies suggest it may promote intestinal inflammation, but human evidence remains limited.",
      potentialConcerns: ["May irritate gut lining.", "Associated with bloating and digestive discomfort in sensitive stomachs."],
      evidenceLevel: "Medium",
      dietaryFlags: ["gut_risk"]
    },
    {
      name: "Palm Oil",
      aliases: ["Refined Vegetable Oil", "Palmitic Acid", "Fractionated Palm Oil"],
      category: "Fats & Oils",
      function: "Provides structure, stability, and smooth texture.",
      description: "A highly popular cheap oil. High in saturated fats and heavily criticized for deforestation and environmental impacts.",
      potentialConcerns: ["High saturated fat content can elevate bad LDL cholesterol.", "High consumption linked to cardiovascular concerns."],
      evidenceLevel: "High",
      dietaryFlags: ["heart_risk"]
    },
    {
      name: "Aspartame",
      aliases: ["Nutrasweet", "Equal", "Phenylalanine source"],
      eNumber: "E951",
      category: "Artificial Sweetener",
      function: "Provides intense sweetness with zero calories.",
      description: "An artificial non-saccharide sweetener. Used in diet sodas and sugar-free foods.",
      potentialConcerns: ["Potential alteration of gut microbiome.", "Some users report headaches or mood changes."],
      evidenceLevel: "Medium",
      dietaryFlags: ["diabetic_risk", "gut_risk"]
    },
    {
      name: "High Fructose Corn Syrup",
      aliases: ["HFCS", "Isoglucose", "Maize Syrup"],
      category: "Sweetener",
      function: "Provides sweet taste and extends shelf life.",
      description: "A liquid sweetener made from corn starch. Extremely common in processed foods, sodas, and condiments.",
      potentialConcerns: ["Directly promotes visceral fat deposition.", "Excessive consumption strongly linked to insulin resistance, obesity, and fatty liver disease."],
      evidenceLevel: "High",
      dietaryFlags: ["diabetic_risk", "heart_risk"]
    },
    {
      name: "Tartrazine",
      aliases: ["FD&C Yellow No. 5", "Yellow 5"],
      eNumber: "E102",
      category: "Artificial Color",
      function: "Imparts a bright yellow hue to foods and drinks.",
      description: "A synthetic azo dye. Banned or restricted in several countries due to potential hyperactivity links.",
      potentialConcerns: ["May cause hyperactivity and behavior changes in children.", "Associated with allergic reactions like asthma or hives in sensitive individuals."],
      evidenceLevel: "Medium",
      dietaryFlags: ["skin_risk"]
    },
    {
      name: "Allura Red AC",
      aliases: ["FD&C Red No. 40", "Red 40"],
      eNumber: "E129",
      category: "Artificial Color",
      function: "Imparts a bright red color to food products.",
      description: "A widely used red food dye derived from petroleum.",
      potentialConcerns: ["Linked to hyperactivity in children.", "May irritate gut mucosa in high quantities."],
      evidenceLevel: "Medium",
      dietaryFlags: ["gut_risk", "skin_risk"]
    },
    {
      name: "Sodium Benzoate",
      aliases: ["Benzoate of Soda"],
      eNumber: "E211",
      category: "Preservative",
      function: "Prevents growth of bacteria, yeast, and mold in acidic foods.",
      description: "A very common food preservative. Can react with Vitamin C under heat/light to form Benzene (a carcinogen).",
      potentialConcerns: ["Can form benzene in the presence of ascorbic acid (Vitamin C).", "Associated with hyperactive behavior in kids."],
      evidenceLevel: "Medium",
      dietaryFlags: ["heart_risk"]
    },
    {
      name: "Potassium Sorbate",
      aliases: ["Sorbate"],
      eNumber: "E202",
      category: "Preservative",
      function: "Inhibits mold and yeast growth in cheese, wine, and baked goods.",
      description: "A mild synthetic preservative. Widely considered safe and well-tolerated, but can cause skin irritation in cosmetics.",
      potentialConcerns: ["Generally safe, but prolonged high intake is discouraged.", "Can cause contact dermatitis in personal care products."],
      evidenceLevel: "Low",
      dietaryFlags: ["skin_risk"]
    },
    {
      name: "Butylated Hydroxyanisole",
      aliases: ["BHA"],
      eNumber: "E320",
      category: "Preservative / Antioxidant",
      function: "Prevents fats and oils from oxidizing and turning rancid.",
      description: "A synthetic antioxidant. Classified by the WHO as a potential human endocrine disruptor and carcinogen.",
      potentialConcerns: ["Classified as a possible human carcinogen (cancer-causing).", "Disrupts endocrine system hormones."],
      evidenceLevel: "High",
      dietaryFlags: ["heart_risk"]
    },
    {
      name: "Butylated Hydroxytoluene",
      aliases: ["BHT"],
      eNumber: "E321",
      category: "Preservative / Antioxidant",
      function: "Maintains freshness, flavor, and color of fats.",
      description: "Similar to BHA, BHT is a synthetic antioxidant widely criticized for potential liver and kidney issues in animal testing.",
      potentialConcerns: ["Linked to liver and kidney issues in animal testing.", "Weak estrogenic activity (hormone disruption)."],
      evidenceLevel: "Medium",
      dietaryFlags: ["heart_risk"]
    },
    {
      name: "Gelatin",
      aliases: ["Gelatine", "Animal Collagen"],
      category: "Thickener / Gelling Agent",
      function: "Creates a chewy, gelatinous texture in candies, desserts, and capsules.",
      description: "A protein obtained by boiling skin, tendons, ligaments, and/or bones of animals (usually pigs or cows).",
      potentialConcerns: ["Derived directly from animal connective tissues."],
      evidenceLevel: "High",
      dietaryFlags: ["vegan_conflict"]
    },
    {
      name: "Carmine",
      aliases: ["Cochineal Extract", "Natural Red 4", "Carminic Acid"],
      eNumber: "E120",
      category: "Natural Color",
      function: "Adds a deep red color.",
      description: "A red dye obtained by crushing female cochineal insects.",
      potentialConcerns: ["Animal-derived (crushed insects).", "Can cause severe allergic reactions or asthma in rare cases."],
      evidenceLevel: "High",
      dietaryFlags: ["vegan_conflict", "skin_risk"]
    },
    {
      name: "Sulfur Dioxide",
      aliases: ["Sulphites", "Sodium Metabisulfite", "Sodium Sulfite"],
      eNumber: "E220",
      category: "Preservative",
      function: "Prevents browning in dried fruits and preserves wine quality.",
      description: "A gas preservative. Known allergen that can trigger severe asthma attacks in asthmatic individuals.",
      potentialConcerns: ["Highly allergenic, particularly for asthmatics.", "Destroys Vitamin B1 in the body."],
      evidenceLevel: "High",
      dietaryFlags: ["gut_risk"]
    },
    {
      name: "Titanium Dioxide",
      aliases: ["Titanium White", "Pigment White 6"],
      eNumber: "E171",
      category: "Colorant / Opacifier",
      function: "Makes foods look whiter and brighter (e.g. gum, powdered sugar).",
      description: "Banned by the European Food Safety Authority (EFSA) in 2022 due to concerns over genotoxicity (DNA damage) when consumed.",
      potentialConcerns: ["EFSA ruled it can no longer be considered safe due to genotoxicity concerns.", "Can accumulate in the body."],
      evidenceLevel: "High",
      dietaryFlags: ["gut_risk"]
    },
    {
      name: "Sodium Lauryl Sulfate",
      aliases: ["SLS", "Sodium Dodecyl Sulfate"],
      category: "Surfactant / Foaming Agent",
      function: "Creates rich lather in toothpaste, face washes, and shampoos.",
      description: "A chemical surfactant. Known to be highly irritating to skin and mucous membranes.",
      potentialConcerns: ["Known skin, eye, and mouth irritant.", "Can contribute to painful canker sores in toothpastes."],
      evidenceLevel: "High",
      dietaryFlags: ["skin_risk"]
    },
    {
      name: "Methylparaben",
      aliases: ["Propylparaben", "Butylparaben", "Parabens"],
      eNumber: "E218",
      category: "Preservative",
      function: "Prevents bacterial and fungal growth in cosmetics and foods.",
      description: "A class of widely used synthetic preservatives. Can mimic estrogen and bind to estrogen receptors, raising safety debates.",
      potentialConcerns: ["Endocrine disruption (estrogen mimicry).", "Associated with allergic skin reactions."],
      evidenceLevel: "High",
      dietaryFlags: ["skin_risk"]
    },
    {
      name: "Polysorbate 80",
      aliases: ["Tween 80", "Polyoxyethylene Sorbitan Monooleate"],
      eNumber: "E433",
      category: "Emulsifier",
      function: "Helps bind oil and water together in ice cream and dressings.",
      description: "Synthetic emulsifier. Animal studies show it can disrupt gut microbiota, promote inflammation, and trigger metabolic syndrome.",
      potentialConcerns: ["May degrade gut mucosal barrier.", "Promotes low-grade gut inflammation."],
      evidenceLevel: "Medium",
      dietaryFlags: ["gut_risk"]
    }
  ]
};

const RulesEngine = {
  calculate: function(params) {
    const rawText = (params.rawIngredientsText || "").toLowerCase();
    const calories = parseFloat(params.calories) || 0;
    const sugarGrams = parseFloat(params.sugarGrams) || 0;
    const proteinGrams = parseFloat(params.proteinGrams) || 0;
    const fiberGrams = parseFloat(params.fiberGrams) || 0;
    const sodiumMg = parseFloat(params.sodiumMg) || 0;
    const saturatedFatGrams = parseFloat(params.saturatedFatGrams) || 0;
    const watchlist = params.watchlist || [];
    const profile = params.profile || {};

    const foundAdditives = [];
    const matchedWatchlist = [];
    const keyFindings = [];
    const logSteps = [];

    logSteps.push({
      step: "Input Parsing",
      text: `Normalized ingredients string (${rawText.length} chars). Metrics: Cal=${calories}, Sugar=${sugarGrams}g, Protein=${proteinGrams}g, Fiber=${fiberGrams}g, Sodium=${sodiumMg}mg, SatFat=${saturatedFatGrams}g.`
    });

    // 1. Identify Additives & Flags
    for (const def of IngredientDatabase.dictionary) {
      let matched = false;
      if (rawText.includes(def.name.toLowerCase())) {
        matched = true;
      } else if (def.eNumber && rawText.includes(def.eNumber.toLowerCase())) {
        matched = true;
      } else {
        for (const alias of def.aliases) {
          if (rawText.includes(alias.toLowerCase())) {
            matched = true;
            break;
          }
        }
      }
      if (matched) {
        foundAdditives.push(def);
      }
    }

    logSteps.push({
      step: "Additive Matching",
      text: `Scanned against ${IngredientDatabase.dictionary.length} catalog items. Found ${foundAdditives.length} matched additive(s): ${foundAdditives.map(a => a.name).join(", ") || "None"}.`
    });

    // 2. Watchlist matches
    for (const item of watchlist) {
      if (item.trim() && rawText.includes(item.toLowerCase().trim())) {
        matchedWatchlist.push(item.trim());
      }
    }

    // 3. Ingredient Quality Score (Base 100)
    let qualityScore = 100;
    const qualityDeductions = [];
    for (const def of foundAdditives) {
      let deduction = 2;
      if (def.evidenceLevel === "High") deduction = 15;
      else if (def.evidenceLevel === "Medium") deduction = 8;
      else if (def.evidenceLevel === "Low") deduction = 4;
      qualityScore -= deduction;
      qualityDeductions.push(`-${deduction} (${def.name}, ${def.evidenceLevel} evidence)`);
    }

    if (rawText.includes("vegetable oil") || rawText.includes("refined oil")) {
      qualityScore -= 10;
      keyFindings.push("⚠ Potential obfuscated vegetable/palm oil detected.");
      qualityDeductions.push("-10 (Obfuscated vegetable/palm oil)");
    }
    if (rawText.includes("yeast extract") || rawText.includes("hydrolyzed protein")) {
      qualityScore -= 8;
      keyFindings.push("⚠ Contains flavor-masking flavor enhancers (possible MSG).");
      qualityDeductions.push("-8 (Flavor masking yeast/hydrolyzed protein)");
    }
    qualityScore = Math.max(10, Math.min(100, qualityScore));

    logSteps.push({
      step: "Quality Dimension",
      text: `Base: 100. Deductions: [${qualityDeductions.join(", ") || "None"}]. Final Quality Score: ${qualityScore}.`
    });

    // 4. Additives Score (Base 100)
    let additivesScore = 100;
    const additivesCount = foundAdditives.filter(it => 
      it.category.includes("Color") || it.category.includes("Preservative") ||
      it.category.includes("Sweetener") || it.category.includes("Emulsifier") ||
      it.category.includes("Enhancer")
    ).length;

    additivesScore -= (additivesCount * 8);

    foundAdditives.forEach(add => {
      if (add.evidenceLevel === "High" && add.category.includes("Preservative")) {
        additivesScore -= 5;
      }
    });
    additivesScore = Math.max(10, Math.min(100, additivesScore));

    logSteps.push({
      step: "Additives Dimension",
      text: `Base: 100. Categorized additive count: ${additivesCount} (x -8). High-evidence preservative penalties applied. Final Additives Score: ${additivesScore}.`
    });

    // 5. Sugar Score (Base 100)
    let sugarScore = 100;
    if (sugarGrams > 0) {
      if (sugarGrams > 5) {
        const excess = sugarGrams - 5;
        sugarScore -= Math.round(excess * 4.5);
      }
    } else {
      if (rawText.includes("sugar") || rawText.includes("sucrose")) {
        sugarScore = 75;
      }
      if (rawText.includes("corn syrup") || rawText.includes("dextrose") || rawText.includes("fructose")) {
        sugarScore = 60;
        keyFindings.push("⚠ High fructose corn syrup or refined sugars detected.");
      }
    }
    sugarScore = Math.max(10, Math.min(100, sugarScore));

    logSteps.push({
      step: "Sugar Dimension",
      text: sugarGrams > 0 
        ? `Evaluated sugar = ${sugarGrams}g (>5g threshold). Deducted excess * 4.5. Final Sugar Score: ${sugarScore}.`
        : `Fallback keyword heuristics applied (sugar/syrup text detected). Final Sugar Score: ${sugarScore}.`
    });

    // 6. Sodium Score (Base 100)
    let sodiumScore = 100;
    if (sodiumMg > 0) {
      if (sodiumMg > 140) {
        const excess = sodiumMg - 140;
        sodiumScore -= Math.round(excess * 0.15);
      }
    } else {
      if (rawText.includes("salt") || rawText.includes("sodium")) {
        sodiumScore = 75;
      }
    }
    sodiumScore = Math.max(10, Math.min(100, sodiumScore));

    logSteps.push({
      step: "Sodium Dimension",
      text: sodiumMg > 0 
        ? `Evaluated sodium = ${sodiumMg}mg (>140mg threshold). Deducted excess * 0.15. Final Sodium Score: ${sodiumScore}.`
        : `Fallback keyword heuristics applied. Final Sodium Score: ${sodiumScore}.`
    });

    // 7. Nutrition Score (Base 100)
    let nutritionScore = 100;
    const nutDeductions = [];
    if (calories > 350) {
      nutritionScore -= 15;
      nutDeductions.push("-15 (Calories > 350)");
    }
    if (proteinGrams > 8) {
      nutritionScore += 10;
      nutDeductions.push("+10 (Protein > 8g)");
    }
    if (fiberGrams > 3) {
      nutritionScore += 10;
      nutDeductions.push("+10 (Fiber > 3g)");
    }
    if (saturatedFatGrams > 5) {
      nutritionScore -= 15;
      nutDeductions.push("-15 (Saturated Fat > 5g)");
    }

    const rawNutritionBeforeBlend = nutritionScore;
    nutritionScore = Math.round((nutritionScore + sugarScore + sodiumScore) / 3);
    nutritionScore = Math.max(10, Math.min(100, nutritionScore));

    logSteps.push({
      step: "Nutrition Dimension",
      text: `Macro adjustments [${nutDeductions.join(", ") || "None"}]. Raw: ${rawNutritionBeforeBlend}. Blended average with Sugar (${sugarScore}) and Sodium (${sodiumScore}): (${rawNutritionBeforeBlend} + ${sugarScore} + ${sodiumScore})/3 = ${nutritionScore}.`
    });

    // 8. Overall Score & Grade
    const overallScore = Math.round((qualityScore + additivesScore + nutritionScore + sugarScore + sodiumScore) / 5);
    let overallGrade = "F";
    if (overallScore >= 80) overallGrade = "A";
    else if (overallScore >= 68) overallGrade = "B";
    else if (overallScore >= 52) overallGrade = "C";
    else if (overallScore >= 38) overallGrade = "D";

    if (sugarScore < 60) keyFindings.push("⚠ High sugar content.");
    if (sodiumScore < 60) keyFindings.push("⚠ High sodium content.");
    if (additivesCount === 0) keyFindings.push("✓ Free from artificial colors, preservatives, and flavor enhancers.");
    if (qualityScore > 85) keyFindings.push("✓ High ingredient quality.");

    // 9. Personalized Profile Score
    let profileScore = overallScore;
    const conflicts = [];

    if (profile.vegan) {
      const nonVeganMatched = [];
      const nonVeganTerms = {
        "egg": "EGG", "milk": "MILK", "whey": "WHEY", "casein": "CASEIN",
        "gelatin": "GELATIN", "meat": "MEAT", "chicken": "CHICKEN", "beef": "BEEF",
        "pork": "PORK", "mutton": "MUTTON", "fish": "FISH", "shrimp": "SHRIMP",
        "seafood": "SEAFOOD", "carmine": "CARMINE", "lactose": "LACTOSE",
        "honey": "HONEY", "dairy": "DAIRY", "butter": "BUTTER", "cheese": "CHEESE",
        "cream": "CREAM", "anchovy": "ANCHOVY", "lard": "LARD", "tallow": "TALLOW",
        "bone": "BONE", "cochineal": "COCHINEAL"
      };
      for (const [term, label] of Object.entries(nonVeganTerms)) {
        if (rawText.includes(term)) nonVeganMatched.push(label);
      }
      const hasAnimalAdditives = foundAdditives.some(it => it.dietaryFlags && it.dietaryFlags.includes("vegan_conflict"));
      if (hasAnimalAdditives && !nonVeganMatched.includes("ANIMAL ADDITIVES")) {
        nonVeganMatched.push("ANIMAL ADDITIVES");
      }
      if (nonVeganMatched.length > 0) {
        profileScore -= 30;
        conflicts.push(`✕ VEGAN CONFLICT: This product contains: ${nonVeganMatched.joinToString ? nonVeganMatched.joinToString(", ") : nonVeganMatched.join(", ")}. Your Vegan preference is enabled. Recommendation: Avoid this product.`);
      }
    } else if (profile.vegetarian) {
      const nonVegMatched = [];
      const nonVegTerms = {
        "meat": "MEAT", "chicken": "CHICKEN", "beef": "BEEF", "pork": "PORK",
        "mutton": "MUTTON", "fish": "FISH", "shrimp": "SHRIMP", "seafood": "SEAFOOD",
        "carmine": "CARMINE", "gelatin": "GELATIN", "anchovy": "ANCHOVY",
        "lard": "LARD", "tallow": "TALLOW", "bone": "BONE", "cochineal": "COCHINEAL"
      };
      for (const [term, label] of Object.entries(nonVegTerms)) {
        if (rawText.includes(term)) nonVegMatched.push(label);
      }
      if (nonVegMatched.length > 0) {
        profileScore -= 20;
        conflicts.push(`✕ VEGETARIAN CONFLICT: This product contains: ${nonVegMatched.join(", ")}. Your Vegetarian preference is enabled. Recommendation: Avoid this product.`);
      }
    }

    if (profile.diabetic) {
      if (sugarGrams > 10 || rawText.includes("corn syrup") || rawText.includes("aspartame") || rawText.includes("high fructose")) {
        profileScore -= 20;
        conflicts.push("✕ DIABETIC CONFLICT: Contains high sugars/glycemic agents. Conflicts with your Diabetic-Friendly preference.");
      }
    }

    if (profile.heartHealth) {
      if (sodiumMg > 360 || rawText.includes("palm oil") || rawText.includes("hydrogenated")) {
        profileScore -= 20;
        conflicts.push("✕ HEART HEALTH CONFLICT: Contains high sodium or trans-fats. Conflicts with your Heart Health preference.");
      }
    }

    if (profile.gutHealth) {
      const gutIrritants = foundAdditives.filter(it => it.dietaryFlags && it.dietaryFlags.includes("gut_risk"));
      if (gutIrritants.length > 0) {
        profileScore -= 15;
        conflicts.push(`✕ GUT HEALTH CONFLICT: Contains gut emulsifiers/stabilizers like ${gutIrritants.map(i => i.name).join(", ")}. Conflicts with your Gut Health preference.`);
      }
    }

    if (profile.cosmeticSafe) {
      const skinIrritants = foundAdditives.filter(it => it.dietaryFlags && it.dietaryFlags.includes("skin_risk"));
      if (skinIrritants.length > 0) {
        profileScore -= 20;
        conflicts.push(`✕ SKIN/COSMETIC CONFLICT: Contains parabens or sulfates. Conflicts with your Skin/Cosmetic Safety preference.`);
      }
    }

    if (profile.lowSugar && (sugarGrams > 10 || (sugarGrams <= 0 && (rawText.includes("sugar") || rawText.includes("syrup"))))) {
      profileScore -= 15;
      conflicts.push("⚠ LOW SUGAR ALERT: High sugar content detected. Conflicts with your Low Sugar preference.");
    }

    if (profile.lowSodium && (sodiumMg > 300 || (sodiumMg <= 0 && (rawText.includes("salt") || rawText.includes("sodium"))))) {
      profileScore -= 15;
      conflicts.push("⚠ LOW SODIUM ALERT: High sodium content detected. Conflicts with your Low Sodium preference.");
    }

    if (matchedWatchlist.length > 0) {
      profileScore -= (matchedWatchlist.length * 25);
      conflicts.push(`✕ WATCHLIST CONFLICT: Contains ingredients on your watchlist: ${matchedWatchlist.join(", ")}.`);
    }

    profileScore = Math.max(10, Math.min(overallScore, profileScore));

    logSteps.push({
      step: "Final Synthesis",
      text: `Overall Score = (${qualityScore} + ${additivesScore} + ${nutritionScore} + ${sugarScore} + ${sodiumScore}) / 5 = ${overallScore} (Grade: ${overallGrade}). Profile Score = ${profileScore} (${conflicts.length} conflict(s)).`
    });

    return {
      overallScore: overallScore,
      overallGrade: overallGrade,
      breakdown: {
        ingredientQuality: qualityScore,
        nutrition: nutritionScore,
        additives: additivesScore,
        sugar: sugarScore,
        sodium: sodiumScore
      },
      profileScore: profileScore,
      matchedWatchlist: matchedWatchlist,
      matchedAdditives: foundAdditives,
      keyFindings: keyFindings,
      conflicts: conflicts,
      logSteps: logSteps
    };
  }
};

if (typeof window !== "undefined") {
  window.NutriCheckRulesEngine = RulesEngine;
  window.NutriCheckIngredientDatabase = IngredientDatabase;
}
if (typeof global !== "undefined") {
  global.NutriCheckRulesEngine = RulesEngine;
  global.NutriCheckIngredientDatabase = IngredientDatabase;
  global.RulesEngine = RulesEngine;
}
if (typeof module !== "undefined" && module.exports) {
  module.exports = { RulesEngine, IngredientDatabase };
}
