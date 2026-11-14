# NutriCheck — Official Major Project LaTeX Report

This folder contains the complete, academic college project report in LaTeX (`.tex`) for **NutriCheck: AI-Powered Food Intelligence and Nutrition Analysis Platform**.

The report is styled and structured strictly according to the format of the ElevateMe project report (`main (1).tex`), including:
- Page borders via `\AddToShipoutPicture`
- Title page with RGUKT logo, certificate of completion with signatures, student declaration, and acknowledgement
- Formatted Table of Contents, List of Tables, and List of Figures
- Roman numerals for front matter, Arabic numerals for Chapters 1 through 18
- Complete academic narrative grounded 100% in the real NutriCheck Kotlin source code (no fabricated databases or ML models)

---

## Directory Structure

```text
nutricheck_latex_report/
│
├── main.tex                    # Primary complete LaTeX source code (18 Chapters)
├── references.bib              # Complete BibTeX bibliography (20+ citations)
├── logo.png                    # RGUKT University Logo for front matter
├── README.md                   # This compilation and submission guide
│
├── figures/                    # High-resolution architectural and system diagrams
│   ├── fig1_limitations.png    # Figure 1: Limitations of Existing Packaged Food Systems
│   ├── fig2_workflow.png       # Figure 2: End-to-End Processing Workflow & Pipeline
│   ├── fig3_architecture.png   # Figure 3: Multi-Tier Layered System Architecture
│   └── fig4_data_schema.png    # Figure 4: SharedPreferences Storage & Object Schemas
│
└── screenshots/                # Real Android UI photos from the physical device
    ├── screenshot_home.jpg               # Figure 5: Home Dashboard & Health Gauge
    ├── screenshot_scanner.jpg            # Figure 6: Scanner Interface & Shopping Mode
    ├── screenshot_report_summary.jpg     # Figure 7: Food Audit Report Summary (Grade B)
    ├── screenshot_report_breakdown.jpg   # Figure 8: Safety Breakdown & Clean Alternatives
    ├── screenshot_report_ingredients.jpg # Figure 9: Interactive Additive Chips (INS codes)
    ├── screenshot_inspector_chat.jpg     # Figure 10: Intelligence Inspector AI Chatbot
    ├── screenshot_profile.jpg            # Figure 11: User Profile & Biometric Tracker
    └── screenshot_settings.jpg           # Figure 12: Settings & Multi-Provider LLM Config
```

---

## Chapter Overview

| Chapter | Title | Key Contents Grounded in Source Code |
|---|---|---|
| **1** | Abstract | High-level summary of dual-ingestion (OCR + Barcode), RulesEngine, and LLM explanation. |
| **2** | Introduction | UPFs, NOVA classification, chemical numbering (INS/E-codes), health halos. |
| **3** | Literature Review | Comparative table across 8 platforms (Nutri-Score, Yuka, MyFitnessPal, etc.). |
| **4** | Problem Statement | Obfuscation, one-size-fits-all scoring, barcode-only bottlenecks, marketing tricks. |
| **5** | Objectives | 6 specific engineering and research objectives. |
| **6** | Existing System | Review of current front-of-pack labels and barcode scanners. Includes Figure 1. |
| **7** | Proposed System | Edge-cloud architecture, deterministic scoring, conflict audits. Includes Figure 2. |
| **8** | Requirement Analysis | Functional & Non-Functional specifications, Hardware/Software table. |
| **9** | System Architecture | Presentation, Vision, Domain, and Persistence tiers. Includes Figure 3. |
| **10** | Modules Description | Comprehensive breakdown of Home, Scanner, RulesEngine, Audit, Chat, Profile. |
| **11** | Database Design | SharedPreferences key-value store, JSON serialization, object schemas. Includes Figure 4. |
| **12** | UI/UX & Screenshots | Integration of all 8 real mobile UI screenshots with academic captions (Figures 5–12). |
| **13** | Implementation Details | Exact Kotlin code listings from `RulesEngine.kt`, `ScannerScreen.kt`, `GeminiClient.kt`. |
| **14** | Testing & Results | Experimental test cases table (TC-01 through TC-07) with real test outcomes. |
| **15** | Challenges Faced | Cylindrical OCR distortion, portion normalization, AI hallucination mitigation. |
| **16** | Future Scope | Indic language OCR, on-device SLMs (Gemma 2B), side-by-side product duel view. |
| **17** | Conclusion | Summary of project outcomes, technical contributions, and public health impact. |
| **18** | References | 20 formal academic publications and technical documentation citations. |

---

## How to Compile the Report

### Option 1: On Overleaf (Easiest & Recommended)
1. Compress the entire `nutricheck_latex_report` folder into a `.zip` file.
2. Go to [Overleaf](https://www.overleaf.com/) and click **New Project** $\rightarrow$ **Upload Project**.
3. Upload the `.zip` archive.
4. Set the compiler to **pdfLaTeX** (default) or **XeLaTeX**.
5. Click **Recompile**. The complete PDF will render with borders, figures, screenshots, and tables.

### Option 2: Using Local TeXstudio / MiKTeX / TeX Live
1. Open `main.tex` in TeXstudio or your preferred LaTeX editor.
2. Ensure `pdflatex` is configured as the default compiler.
3. Build and view (F5 in TeXstudio).

### Option 3: Command Line (Linux / macOS / Windows with MiKTeX or TeX Live installed)
```bash
cd nutricheck_latex_report
pdflatex main.tex
pdflatex main.tex   # Run twice to resolve references and page counts
```

---

## Academic & Student Information Included
- **Author:** Veera Venkata Karthik Barrenkala (Roll No: **R210111**)
- **Department:** Department of Computer Science and Engineering
- **Institution:** Rajiv Gandhi University of Knowledge Technologies (RGUKT) - RK Valley, Y.S.R Kadapa, Andhra Pradesh – 516330
- **Guide:** S. Rajeswari (Department of CSE)
- **Head of Department:** Dr. Ch. Ratna Kumari (M.Tech., Ph.D., Assistant Professor)
- **Director:** Dr. A. V. S. Kumara Swami Gupta
- **Academic Year:** 2025–2026
