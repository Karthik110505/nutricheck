# NutriCheck — Presentation Notes

## Source-grounding note
This presentation was revised using the submitted `Major_Project_Part1.zip`, its Android Kotlin source, `ABSTRACT_NUTRICHECK.pdf`, the submitted PowerPoint, and the included product concept visual. The archive's `screenshot_home.png` is empty (0 bytes), so the deck does not pretend that the concept artwork is a live application screenshot. UI/report elements are represented with editable PowerPoint shapes.

## Slide guide
1. **Title** — Introduce NutriCheck / PureCheck AI, the student team and the goal: translating product labels into an understandable ingredient audit.
2. **What it does** — Explain the four-stage flow: capture, decode, evaluate and understand.
3. **Problem** — Explain why ingredient codes, aliases, incomplete catalogue records and generic advice create friction for consumers.
4. **Solution** — Emphasize the hybrid design. Local rules establish the score; cloud AI explains the result.
5. **Features** — Describe label scanning, barcode lookup, ingredient analysis, profile checks, result report, AI chat, favorites, history and shopping list.
6. **User journey** — Walk through the app from home dashboard to action. Barcode and label paths converge on the audit.
7. **Architecture** — Describe the Compose UI, edge vision, domain rules, orchestration and local persistence layers.
8. **Scoring** — Explain the five score dimensions and grade thresholds. The profile conflict score is separate from the overall grade.
9. **Personalization** — Show supported profile checks: vegan, vegetarian, diabetic, heart health, gut health, cosmetic-safe, low sugar, low sodium and watchlist.
10. **Result experience** — Explain report sections: grade, breakdown, flagged ingredients, profile conflicts, nutrition, clean swaps and inspector chat.
11. **Everyday use** — Explain profile setup, shopping-time scan and later revisit via history/favorites/list. A dedicated side-by-side comparison feature is not present in the submitted Kotlin source.
12. **Advantages** — Emphasize dual input, repeatable local scoring, personal context, AI provider fallback, saved products and transparent breakdown.
13. **Implementation** — Summarize Kotlin, Compose, CameraX, ML Kit, RulesEngine, IngredientDatabase, OkHttp, Open Food Facts and AI providers.
14. **Trust boundary** — Local deterministic scoring is separate from generative explanation. State clearly that this is informational, not medical advice.
15. **Scope** — Distinguish implemented workflows from non-verified or out-of-scope claims.
16. **Limitations** — Discuss OCR conditions, catalogue coverage, dictionary coverage, network needs and heuristic scoring.
17. **Future scope** — Present language support, richer ingredient knowledge, offline AI, comparison, validation and cosmetic coverage as proposals.
18. **Conclusion** — Close with the product's three ideas: capture, evaluate and understand.

## Technical accuracy notes
- The five dimensions are ingredient quality, additives, nutrition, sugar and sodium.
- The overall score is the integer arithmetic mean of those five values.
- Grade bands in `RulesEngine.kt`: A >= 80; B >= 68; C >= 52; D >= 38; otherwise F.
- Profile conflicts reduce a separate profile score; they do not change the general overall grade.
- The app has configured AI paths for Gemini, Groq (LLaMA/Qwen), DeepSeek and Bytez.
- The app includes history, favorites and shopping-list persistence through SharedPreferences/Gson.
- The app contains result chat and clean-swap report fields.
- The abstract mentions side-by-side comparison, but no dedicated comparison workflow was found in the submitted Kotlin source; it is therefore framed as future scope.
- The abstract describes food and cosmetics, and the code includes a cosmetic-safe profile flag and some cosmetic ingredient context. A comprehensive cosmetic product audit catalogue is not established by the submitted implementation.
- Do not present heuristic grades as medical or clinically validated safety determinations.
