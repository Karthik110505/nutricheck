package com.example.purecheck.data

data class IngredientDef(
    val name: String,
    val aliases: List<String> = emptyList(),
    val eNumber: String? = null,
    val category: String,
    val function: String,
    val description: String,
    val potentialConcerns: List<String> = emptyList(),
    val evidenceLevel: String = "None",
    val dietaryFlags: List<String> = emptyList() // "vegan_conflict", "heart_risk", "diabetic_risk", "gut_risk", "skin_risk"
)

object IngredientDatabase {
    val dictionary: List<IngredientDef> = listOf(
        IngredientDef(
            name = "Monosodium Glutamate",
            aliases = listOf("MSG", "Yeast Extract", "Hydrolyzed Vegetable Protein", "Sodium Glutamate"),
            eNumber = "E621",
            category = "Flavour Enhancer",
            function = "Enhances savory/umami flavor in food.",
            description = "A widely used flavor enhancer. In sensitive individuals, it may trigger minor symptoms, though overall evidence for long-term harm is low.",
            potentialConcerns = listOf("Sensitivities in specific individuals (headaches, flushing).", "Often used to mask low-quality ingredients."),
            evidenceLevel = "Low",
            dietaryFlags = listOf("diabetic_risk") // Often found in highly processed, high-sodium foods
        ),
        IngredientDef(
            name = "Carrageenan",
            aliases = listOf("Carrageenan Gum", "Irish Moss Extract"),
            eNumber = "E407",
            category = "Thickener / Stabilizer",
            function = "Improves texture and gel-like consistency in dairy and plant milks.",
            description = "Derived from red seaweed. Some animal studies suggest it may promote intestinal inflammation, but human evidence remains limited.",
            potentialConcerns = listOf("May irritate gut lining.", "Associated with bloating and digestive discomfort in sensitive stomachs."),
            evidenceLevel = "Medium",
            dietaryFlags = listOf("gut_risk")
        ),
        IngredientDef(
            name = "Palm Oil",
            aliases = listOf("Refined Vegetable Oil", "Palmitic Acid", "Fractionated Palm Oil"),
            category = "Fats & Oils",
            function = "Provides structure, stability, and smooth texture.",
            description = "A highly popular cheap oil. High in saturated fats and heavily criticized for deforestation and environmental impacts.",
            potentialConcerns = listOf("High saturated fat content can elevate bad LDL cholesterol.", "High consumption linked to cardiovascular concerns."),
            evidenceLevel = "High",
            dietaryFlags = listOf("heart_risk")
        ),
        IngredientDef(
            name = "Aspartame",
            aliases = listOf("Nutrasweet", "Equal", "Phenylalanine source"),
            eNumber = "E951",
            category = "Artificial Sweetener",
            function = "Provides intense sweetness with zero calories.",
            description = "An artificial non-saccharide sweetener. Used in diet sodas and sugar-free foods.",
            potentialConcerns = listOf("Potential alteration of gut microbiome.", "Some users report headaches or mood changes."),
            evidenceLevel = "Medium",
            dietaryFlags = listOf("diabetic_risk", "gut_risk") // Sweeteners can impact insulin sensitivity and gut flora
        ),
        IngredientDef(
            name = "High Fructose Corn Syrup",
            aliases = listOf("HFCS", "Isoglucose", "Maize Syrup"),
            category = "Sweetener",
            function = "Provides sweet taste and extends shelf life.",
            description = "A liquid sweetener made from corn starch. Extremely common in processed foods, sodas, and condiments.",
            potentialConcerns = listOf("Directly promotes visceral fat deposition.", "Excessive consumption strongly linked to insulin resistance, obesity, and fatty liver disease."),
            evidenceLevel = "High",
            dietaryFlags = listOf("diabetic_risk", "heart_risk")
        ),
        IngredientDef(
            name = "Tartrazine",
            aliases = listOf("FD&C Yellow No. 5", "Yellow 5"),
            eNumber = "E102",
            category = "Artificial Color",
            function = "Imparts a bright yellow hue to foods and drinks.",
            description = "A synthetic azo dye. Banned or restricted in several countries due to potential hyperactivity links.",
            potentialConcerns = listOf("May cause hyperactivity and behavior changes in children.", "Associated with allergic reactions like asthma or hives in sensitive individuals."),
            evidenceLevel = "Medium",
            dietaryFlags = listOf("skin_risk")
        ),
        IngredientDef(
            name = "Allura Red AC",
            aliases = listOf("FD&C Red No. 40", "Red 40"),
            eNumber = "E129",
            category = "Artificial Color",
            function = "Imparts a bright red color to food products.",
            description = "A widely used red food dye derived from petroleum.",
            potentialConcerns = listOf("Linked to hyperactivity in children.", "May irritate gut mucosa in high quantities."),
            evidenceLevel = "Medium",
            dietaryFlags = listOf("gut_risk", "skin_risk")
        ),
        IngredientDef(
            name = "Sodium Benzoate",
            aliases = listOf("Benzoate of Soda"),
            eNumber = "E211",
            category = "Preservative",
            function = "Prevents growth of bacteria, yeast, and mold in acidic foods.",
            description = "A very common food preservative. Can react with Vitamin C under heat/light to form Benzene (a carcinogen).",
            potentialConcerns = listOf("Can form benzene in the presence of ascorbic acid (Vitamin C).", "Associated with hyperactive behavior in kids."),
            evidenceLevel = "Medium",
            dietaryFlags = listOf("heart_risk")
        ),
        IngredientDef(
            name = "Potassium Sorbate",
            aliases = listOf("Sorbate"),
            eNumber = "E202",
            category = "Preservative",
            function = "Inhibits mold and yeast growth in cheese, wine, and baked goods.",
            description = "A mild synthetic preservative. Widely considered safe and well-tolerated, but can cause skin irritation in cosmetics.",
            potentialConcerns = listOf("Generally safe, but prolonged high intake is discouraged.", "Can cause contact dermatitis in personal care products."),
            evidenceLevel = "Low",
            dietaryFlags = listOf("skin_risk")
        ),
        IngredientDef(
            name = "Butylated Hydroxyanisole",
            aliases = listOf("BHA"),
            eNumber = "E320",
            category = "Preservative / Antioxidant",
            function = "Prevents fats and oils from oxidizing and turning rancid.",
            description = "A synthetic antioxidant. Classified by the WHO as a potential human endocrine disruptor and carcinogen.",
            potentialConcerns = listOf("Classified as a possible human carcinogen (cancer-causing).", "Disrupts endocrine system hormones."),
            evidenceLevel = "High",
            dietaryFlags = listOf("heart_risk")
        ),
        IngredientDef(
            name = "Butylated Hydroxytoluene",
            aliases = listOf("BHT"),
            eNumber = "E321",
            category = "Preservative / Antioxidant",
            function = "Maintains freshness, flavor, and color of fats.",
            description = "Similar to BHA, BHT is a synthetic antioxidant widely criticized for potential liver and lung toxicity at high doses in animals.",
            potentialConcerns = listOf("Linked to liver and kidney issues in animal testing.", "Weak estrogenic activity (hormone disruption)."),
            evidenceLevel = "Medium",
            dietaryFlags = listOf("heart_risk")
        ),
        IngredientDef(
            name = "Gelatin",
            aliases = listOf("Gelatine", "Animal Collagen"),
            category = "Thickener / Gelling Agent",
            function = "Creates a chewy, gelatinous texture in candies, desserts, and capsules.",
            description = "A protein obtained by boiling skin, tendons, ligaments, and/or bones of animals (usually pigs or cows).",
            potentialConcerns = listOf("Derived directly from animal connective tissues."),
            evidenceLevel = "High",
            dietaryFlags = listOf("vegan_conflict")
        ),
        IngredientDef(
            name = "Carmine",
            aliases = listOf("Cochineal Extract", "Natural Red 4", "Carminic Acid"),
            eNumber = "E120",
            category = "Natural Color",
            function = "Adds a deep red color.",
            description = "A red dye obtained by crushing female cochineal insects.",
            potentialConcerns = listOf("Animal-derived (crushed insects).", "Can cause severe allergic reactions or asthma in rare cases."),
            evidenceLevel = "High",
            dietaryFlags = listOf("vegan_conflict", "skin_risk")
        ),
        IngredientDef(
            name = "Sulfur Dioxide",
            aliases = listOf("Sulphites", "Sodium Metabisulfite", "Sodium Sulfite"),
            eNumber = "E220",
            category = "Preservative",
            function = "Prevents browning in dried fruits and preserves wine quality.",
            description = "A gas preservative. Known allergen that can trigger severe asthma attacks in asthmatic individuals.",
            potentialConcerns = listOf("Highly allergenic, particularly for asthmatics.", "Destroys Vitamin B1 in the body."),
            evidenceLevel = "High",
            dietaryFlags = listOf("gut_risk")
        ),
        IngredientDef(
            name = "Titanium Dioxide",
            aliases = listOf("Titanium White", "Pigment White 6"),
            eNumber = "E171",
            category = "Colorant / Opacifier",
            function = "Makes foods look whiter and brighter (e.g. gum, powdered sugar).",
            description = "Banned by the European Food Safety Authority (EFSA) in 2022 due to concerns over genotoxicity (DNA damage) when consumed.",
            potentialConcerns = listOf("EFSA ruled it can no longer be considered safe due to genotoxicity concerns.", "Can accumulate in the body."),
            evidenceLevel = "High",
            dietaryFlags = listOf("gut_risk")
        ),
        IngredientDef(
            name = "Sodium Lauryl Sulfate",
            aliases = listOf("SLS", "Sodium Dodecyl Sulfate"),
            category = "Surfactant / Foaming Agent",
            function = "Creates rich lather in toothpaste, face washes, and shampoos.",
            description = "A chemical surfactant. Known to be highly irritating to skin and mucous membranes.",
            potentialConcerns = listOf("Known skin, eye, and mouth irritant.", "Can contribute to painful canker sores in toothpastes."),
            evidenceLevel = "High",
            dietaryFlags = listOf("skin_risk")
        ),
        IngredientDef(
            name = "Methylparaben",
            aliases = listOf("Propylparaben", "Butylparaben", "Parabens"),
            eNumber = "E218",
            category = "Preservative",
            function = "Prevents bacterial and fungal growth in cosmetics and foods.",
            description = "A class of widely used synthetic preservatives. Can mimic estrogen and bind to estrogen receptors, raising safety debates.",
            potentialConcerns = listOf("Endocrine disruption (estrogen mimicry).", "Associated with allergic skin reactions."),
            evidenceLevel = "High",
            dietaryFlags = listOf("skin_risk")
        ),
        IngredientDef(
            name = "Polysorbate 80",
            aliases = listOf("Tween 80", "Polyoxyethylene Sorbitan Monooleate"),
            eNumber = "E433",
            category = "Emulsifier",
            function = "Helps bind oil and water together in ice cream and dressings.",
            description = "Synthetic emulsifier. Animal studies show it can disrupt gut microbiota, promote inflammation, and trigger metabolic syndrome.",
            potentialConcerns = listOf("May degrade gut mucosal barrier.", "Promotes low-grade gut inflammation."),
            evidenceLevel = "Medium",
            dietaryFlags = listOf("gut_risk")
        )
    )

    fun find(query: String): IngredientDef? {
        val normalized = query.lowercase().trim()
        return dictionary.firstOrNull { def ->
            def.name.lowercase() == normalized ||
            def.eNumber?.lowercase() == normalized ||
            def.aliases.any { it.lowercase() == normalized }
        }
    }

    fun search(query: String): List<IngredientDef> {
        val normalized = query.lowercase().trim()
        if (normalized.isBlank()) return dictionary
        return dictionary.filter { def ->
            def.name.lowercase().contains(normalized) ||
            def.eNumber?.lowercase()?.contains(normalized) == true ||
            def.aliases.any { it.lowercase().contains(normalized) } ||
            def.category.lowercase().contains(normalized)
        }
    }
}
