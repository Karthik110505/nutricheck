import os
from PIL import Image, ImageDraw, ImageFont

OUT_DIR = r'd:\projects\Major_Project_Part1\nutricheck_latex_report\figures'
os.makedirs(OUT_DIR, exist_ok=True)

def get_font(size, bold=False):
    font_paths = [
        'C:/Windows/Fonts/arialbd.ttf' if bold else 'C:/Windows/Fonts/arial.ttf',
        'C:/Windows/Fonts/segoeuib.ttf' if bold else 'C:/Windows/Fonts/segoeui.ttf',
        'C:/Windows/Fonts/calibrib.ttf' if bold else 'C:/Windows/Fonts/calibri.ttf',
    ]
    for p in font_paths:
        if os.path.exists(p):
            try:
                return ImageFont.truetype(p, size)
            except Exception:
                pass
    return ImageFont.load_default()

def draw_rounded_rect(draw, bbox, fill, outline, width=2, radius=12):
    draw.rounded_rectangle(bbox, radius=radius, fill=fill, outline=outline, width=width)

# ----------------- FIGURE 1: Limitations of Existing Systems -----------------
def generate_fig1():
    w, h = 1400, 800
    img = Image.new('RGB', (w, h), color=(250, 252, 250))
    draw = ImageDraw.Draw(img)
    
    # Title Banner
    draw.rectangle([(0, 0), (w, 80)], fill=(16, 42, 34))
    f_title = get_font(30, bold=True)
    f_box_h = get_font(18, bold=True)
    f_box_b = get_font(14, bold=False)
    
    draw.text((w/2, 40), "Limitations of Traditional Packaged Food and Nutrition Platforms", fill=(255, 255, 255), anchor="mm", font=f_title)
    
    cards = [
        ("Obfuscated E-Numbers and Chemical Jargon", 
         ["- Uses technical food codes (INS 102, INS 471, E621) hiding harmful additives.",
          "- Consumers cannot easily decipher carcinogenic, hyperactive, or allergen risks.",
          "- Microscopic text printed on packaging hinders manual visual verification.",
          "- Traditional tools lack instant optical recognition of complex chemical names."],
         (220, 60, 60)),
        ("Misleading Front-of-Package Marketing", 
         ["- Claims like 'Zero Added Sugar' conceal high maltodextrin and sweeteners.",
          "- '100% Natural' or 'Heart Healthy' claims contradict high sodium content.",
          "- Serving sizes are manipulated to artificially reduce calorie figures.",
          "- Existing systems lack automated marketing claim verification against facts."],
         (230, 120, 30)),
        ("One-Size-Fits-All Universal Scoring", 
         ["- Nutri-Score and generic ratings evaluate foods for an average person only.",
          "- Completely ignores personal medical conditions (Diabetes, Hypertension).",
          "- Fails to alert for individual allergens (gluten, peanuts, dairy, soy).",
          "- A product rated 'Green / Good' can be hazardous for a diabetic consumer."],
         (180, 50, 120)),
        ("Barcode-Exclusivity Bottlenecks", 
         ["- Traditional scanners fail if a barcode is damaged, torn, or unindexed.",
          "- Regional, local grocery items, and bakery foods have no global barcode entry.",
          "- Heavy latency when querying remote cloud barcode registries.",
          "- Complete absence of fallback to real-time optical text analysis."],
         (90, 70, 160)),
        ("Fragmented and Static Ecosystems", 
         ["- Requires separate apps for barcode scanning, macro tracking, and recipes.",
          "- Static scoreboards provide no conversational explanation or deep advice.",
          "- Lacks personalized clean alternatives to swap out ultra-processed items.",
          "- Consumers are left confused without actionable shopping intelligence."],
         (30, 110, 180)),
    ]
    
    positions = [
        (80, 120, 680, 310),
        (720, 120, 1320, 310),
        (80, 340, 680, 530),
        (720, 340, 1320, 530),
        (400, 560, 1000, 750)
    ]
    
    for idx, (title, bullets, accent) in enumerate(cards):
        bx1, by1, bx2, by2 = positions[idx]
        draw_rounded_rect(draw, (bx1, by1, bx2, by2), fill=(255, 255, 255), outline=accent, width=2, radius=12)
        draw_rounded_rect(draw, (bx1, by1, bx2, by1+45), fill=accent, outline=accent, width=1, radius=10)
        draw.text((bx1+15, by1+22), title, fill=(255, 255, 255), anchor="lm", font=f_box_h)
        cur_y = by1 + 60
        for b in bullets:
            draw.text((bx1+15, cur_y), b, fill=(40, 50, 60), font=f_box_b)
            cur_y += 30
            
    img.save(os.path.join(OUT_DIR, 'fig1_limitations.png'))
    print("fig1_limitations.png generated.")

# ----------------- FIGURE 2: Workflow -----------------
def generate_fig2():
    w, h = 1400, 850
    img = Image.new('RGB', (w, h), color=(248, 250, 252))
    draw = ImageDraw.Draw(img)
    
    draw.rectangle([(0, 0), (w, 80)], fill=(16, 42, 34))
    f_title = get_font(30, bold=True)
    f_box_h = get_font(16, bold=True)
    f_box_b = get_font(13, bold=False)
    
    draw.text((w/2, 40), "NutriCheck End-to-End Processing Workflow and Pipeline", fill=(255, 255, 255), anchor="mm", font=f_title)
    
    steps = [
        ("1. DUAL INGESTION LAYER", 
         ["CameraX Live OCR", "ML Kit Latin Engine", "Barcode Scan (ZXing)", "Open Food Facts API"], 
         (230, 245, 235), (16, 140, 80)),
        ("2. INGREDIENT TOKENIZER", 
         ["Text Normalization", "INS / E-Number Regex", "Macro Table Parsing", "Portion Normalization"], 
         (235, 245, 255), (30, 110, 200)),
        ("3. DETERMINISTIC ENGINE", 
         ["Quality Sub-score (0-100)", "Additive Hazard Penalty", "Nutritional Balance Index", "Sugar and Sodium Check"], 
         (255, 245, 230), (220, 130, 20)),
        ("4. PROFILE CONFLICT AUDIT", 
         ["Diabetic Sugar Clamping", "Hypertension Salt Alert", "Vegan / Veg Audit", "Allergen and Watchlist Clash"], 
         (255, 235, 235), (220, 50, 50)),
        ("5. INTELLIGENCE AND REPORT", 
         ["Grade (A-F) Assignment", "Gemini 2.0 Flash AI", "SharedPreferences Cache", "Interactive Chatbot"], 
         (245, 235, 255), (140, 50, 210)),
    ]
    
    box_w = 230
    box_h = 420
    start_x = 45
    spacing = 40
    y_top = 180
    
    for i, (title, items, fill_col, border_col) in enumerate(steps):
        bx = start_x + i * (box_w + spacing)
        draw_rounded_rect(draw, (bx, y_top, bx+box_w, y_top+box_h), fill=fill_col, outline=border_col, width=2, radius=14)
        draw_rounded_rect(draw, (bx, y_top, bx+box_w, y_top+55), fill=border_col, outline=border_col, width=1, radius=12)
        draw.text((bx+box_w/2, y_top+27), title, fill=(255, 255, 255), anchor="mm", font=f_box_h)
        
        cy = y_top + 80
        for item in items:
            draw_rounded_rect(draw, (bx+12, cy, bx+box_w-12, cy+55), fill=(255, 255, 255), outline=border_col, width=1, radius=8)
            draw.text((bx+box_w/2, cy+27), item, fill=(30, 40, 50), anchor="mm", font=f_box_b)
            cy += 75
            
        if i < len(steps) - 1:
            ax = bx + box_w
            ay = y_top + box_h/2
            draw.line([(ax+5, ay), (ax+spacing-5, ay)], fill=(16, 42, 34), width=3)
            draw.polygon([(ax+spacing-5, ay), (ax+spacing-14, ay-7), (ax+spacing-14, ay+7)], fill=(16, 42, 34))

    draw_rounded_rect(draw, (45, 660, w-45, 790), fill=(240, 245, 242), outline=(16, 140, 80), width=2, radius=12)
    f_bot_h = get_font(17, bold=True)
    f_bot_b = get_font(14, bold=False)
    draw.text((70, 685), "GUARANTEED DETERMINISTIC AND ZERO-HALLUCINATION ARCHITECTURE", fill=(16, 140, 80), font=f_bot_h)
    draw.text((70, 720), "- Edge CameraX and ML Kit execute locally without network dependency for 100% privacy and zero data leakage.", fill=(40, 50, 60), font=f_bot_b)
    draw.text((70, 750), "- RulesEngine performs exact mathematical scoring on-device. Cloud LLMs are restricted strictly to qualitative explanation.", fill=(40, 50, 60), font=f_bot_b)

    img.save(os.path.join(OUT_DIR, 'fig2_workflow.png'))
    print("fig2_workflow.png generated.")

# ----------------- FIGURE 3: System Architecture -----------------
def generate_fig3():
    w, h = 1400, 920
    img = Image.new('RGB', (w, h), color=(252, 253, 252))
    draw = ImageDraw.Draw(img)
    
    draw.rectangle([(0, 0), (w, 80)], fill=(16, 42, 34))
    f_title = get_font(30, bold=True)
    f_layer = get_font(19, bold=True)
    f_block_h = get_font(15, bold=True)
    f_block_b = get_font(12, bold=False)
    
    draw.text((w/2, 40), "NutriCheck Multi-Tier Layered System Architecture", fill=(255, 255, 255), anchor="mm", font=f_title)
    
    layers = [
        ("PRESENTATION LAYER (Jetpack Compose UI and Material 3)", (235, 248, 240), (16, 140, 80), [
            ("HomeScreen", "Nutritional gauge, daily scan stats, recent items cards"),
            ("ScannerScreen", "Camera preview, mode switch, capture triggers"),
            ("AuditReportScreen", "Grade badge, 5 index bars, additive chips, port scaler"),
            ("ChatScreen", "Intelligence Inspector, tone switch (Simple/Scientific)"),
            ("Profile and Settings", "Biometric inputs, disease toggles, API provider config")
        ]),
        ("EDGE PERCEPTION AND VISION LAYER (On-Device ML Kit)", (240, 245, 255), (30, 100, 190), [
            ("AndroidX CameraX", "PreviewView, ImageCapture, lifecycle binding"),
            ("ML Kit Latin OCR", "On-device Latin text recognition (v19.0.0)"),
            ("ML Kit Barcode Scan", "EAN-13, UPC-A, QR barcode extractor (v17.3.0)"),
            ("Bitmap Normalizer", "Rotation fix, crop optimizer, contrast enhancer")
        ]),
        ("DOMAIN AND SCORING LAYER (Mathematical Logic)", (255, 250, 235), (210, 130, 20), [
            ("RulesEngine.kt", "OverallScore = (Q + A + N + S + Na) / 5"),
            ("INS Additive Registry", "200+ food additives with hazard ratings (1 to 5)"),
            ("Profile Conflict Auditor", "Diabetic, Hypertension, Vegan, Allergen deductions"),
            ("Portion Scaler Model", "Dynamic macro recalibration based on serving weight")
        ]),
        ("DATA PERSISTENCE AND INTEGRATION LAYER", (255, 240, 245), (190, 40, 80), [
            ("SharedPreferences", "Private app storage: purecheck_prefs (Gson JSON)"),
            ("Open Food Facts API", "REST v2 JSON endpoint for verified product barcodes"),
            ("Cloud LLM Clients", "Gemini 2.0 Flash, Groq LLaMA-3.3, DeepSeek-V3"),
            ("OkHttp 3 Network Client", "Asynchronous HTTP/2 calls with bearer token auth")
        ])
    ]
    
    cur_y = 110
    layer_h = 175
    spacing = 25
    
    for layer_name, fill_col, border_col, blocks in layers:
        draw_rounded_rect(draw, (50, cur_y, w-50, cur_y+layer_h), fill=fill_col, outline=border_col, width=2, radius=14)
        draw.text((70, cur_y+24), layer_name, fill=border_col, font=f_layer)
        
        n = len(blocks)
        bw = (w - 140 - (n - 1) * 20) / n
        bx = 70
        by = cur_y + 55
        bh = layer_h - 75
        
        for b_title, b_desc in blocks:
            draw_rounded_rect(draw, (bx, by, bx+bw, by+bh), fill=(255, 255, 255), outline=border_col, width=1, radius=8)
            draw.text((bx+10, by+18), b_title, fill=(20, 30, 40), font=f_block_h)
            draw.text((bx+10, by+44), b_desc[:38], fill=(70, 80, 90), font=f_block_b)
            if len(b_desc) > 38:
                draw.text((bx+10, by+64), b_desc[38:76], fill=(70, 80, 90), font=f_block_b)
            bx += bw + 20
            
        cur_y += layer_h + spacing

    img.save(os.path.join(OUT_DIR, 'fig3_architecture.png'))
    print("fig3_architecture.png generated.")

# ----------------- FIGURE 4: Data Schema -----------------
def generate_fig4():
    w, h = 1400, 850
    img = Image.new('RGB', (w, h), color=(250, 252, 251))
    draw = ImageDraw.Draw(img)
    
    draw.rectangle([(0, 0), (w, 80)], fill=(16, 42, 34))
    f_title = get_font(30, bold=True)
    f_card_h = get_font(18, bold=True)
    f_field = get_font(13, bold=False)
    f_type = get_font(12, bold=True)
    
    draw.text((w/2, 40), "NutriCheck SharedPreferences Local Storage and Object Schemas", fill=(255, 255, 255), anchor="mm", font=f_title)
    
    schemas = [
        ("UserProfile (JSON Object)", [
            ("allergens", "List<String>", "e.g. ['Gluten', 'Peanut']"),
            ("dietType", "DietType", "VEGAN, VEGETARIAN, STANDARD"),
            ("healthConditions", "List<Condition>", "DIABETES, HYPERTENSION, GUT"),
            ("watchlist", "List<String>", "Custom chemical keywords"),
            ("heightCm", "Float", "User height for BMI computation"),
            ("weightKg", "Float", "User weight for calorie targeting"),
            ("gender", "String", "MALE / FEMALE biometric basis"),
            ("activityLevel", "ActivityLevel", "SEDENTARY, MODERATE, ACTIVE"),
            ("caloricTarget", "Int", "Calculated daily kilocalories")
        ], (230, 245, 235), (16, 140, 80)),
        
        ("ScannedItem (History Item)", [
            ("id", "String (UUID)", "Unique primary scan identifier"),
            ("name", "String", "Product brand and trade name"),
            ("timestamp", "Long", "System epoch millisecond time"),
            ("overallScore", "Int (0-100)", "Harmonized health grade score"),
            ("safetyGrade", "Grade (A-F)", "Computed categorical letter grade"),
            ("ingredientsList", "List<String>", "Extracted raw and cleaned tokens"),
            ("detectedAdditives", "List<Additive>", "INS codes, hazard ratings (1-5)"),
            ("healthAlerts", "List<Alert>", "Personalized diet/conflict alerts"),
            ("nutritionFacts", "NutritionMap", "Energy, Sugars, Sodium per 100g")
        ], (235, 245, 255), (30, 110, 200)),
        
        ("ShoppingItem (List Entry)", [
            ("id", "String (UUID)", "Unique item item key"),
            ("productName", "String", "Item description on list"),
            ("isChecked", "Boolean", "Purchased status toggle"),
            ("targetGrade", "Grade", "Minimum acceptable safety grade"),
            ("category", "String", "Produce, Dairy, Snack, Pantry"),
            ("cleanAlternative", "String", "Recommended low-chemical swap"),
            ("addedTimestamp", "Long", "Creation timestamp"),
            ("associatedScanId", "String?", "Optional reference to past scan")
        ], (255, 245, 230), (220, 130, 20)),
        
        ("AppSettings (System Config)", [
            ("selectedLlmProvider", "Enum", "GEMINI, GROQ, DEEPSEEK, BYTEZ"),
            ("apiKeyCustom", "String", "User-provided runtime BYOK token"),
            ("chatToneMode", "ToneMode", "SIMPLE, DETAILED, SCIENTIFIC"),
            ("isDarkTheme", "Boolean", "UI styling state toggle"),
            ("cachedScanCount", "Int", "Total processed product count"),
            ("lastCacheSync", "Long", "Cache invalidation epoch"),
            ("isOfflineModeOnly", "Boolean", "Forces pure rule-based evaluation")
        ], (255, 240, 245), (190, 40, 80))
    ]
    
    card_w = 295
    card_h = 680
    start_x = 45
    spacing = 40
    y_top = 120
    
    for i, (title, fields, fill_col, border_col) in enumerate(schemas):
        bx = start_x + i * (card_w + spacing)
        draw_rounded_rect(draw, (bx, y_top, bx+card_w, y_top+card_h), fill=fill_col, outline=border_col, width=2, radius=12)
        draw_rounded_rect(draw, (bx, y_top, bx+card_w, y_top+50), fill=border_col, outline=border_col, width=1, radius=10)
        draw.text((bx+card_w/2, y_top+25), title, fill=(255, 255, 255), anchor="mm", font=f_card_h)
        
        cy = y_top + 65
        for fname, ftype, fdesc in fields:
            draw_rounded_rect(draw, (bx+10, cy, bx+card_w-10, cy+60), fill=(255, 255, 255), outline=(220, 225, 230), width=1, radius=6)
            draw.text((bx+15, cy+18), fname, fill=(16, 42, 34), font=f_field)
            draw.text((bx+card_w-18, cy+18), ftype, fill=border_col, anchor="rm", font=f_type)
            draw.text((bx+15, cy+42), fdesc, fill=(100, 110, 120), font=f_field)
            cy += 67

    img.save(os.path.join(OUT_DIR, 'fig4_data_schema.png'))
    print("fig4_data_schema.png generated.")

if __name__ == '__main__':
    generate_fig1()
    generate_fig2()
    generate_fig3()
    generate_fig4()
    print("All diagrams generated successfully!")
