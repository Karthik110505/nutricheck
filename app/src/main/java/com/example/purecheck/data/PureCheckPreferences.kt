package com.example.purecheck.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.UUID

data class UserProfile(
    val name: String = "Veera Venkata Karthik",
    val age: Int = 21,
    val gender: String = "Male",
    val height: Float = 175f,
    val weight: Float = 70f,
    val gutHealth: Boolean = false,
    val heartHealth: Boolean = false,
    val vegan: Boolean = false,
    val vegetarian: Boolean = false,
    val diabetic: Boolean = false,
    val cosmeticSafe: Boolean = false,
    val lowSugar: Boolean = false,
    val lowSodium: Boolean = false,
    val highProtein: Boolean = false,
    val cleanIngredients: Boolean = false
)

data class FavoriteProduct(
    val id: String = UUID.randomUUID().toString(),
    val productName: String,
    val overallGrade: String,
    val overallScore: Int,
    val calories: Int,
    val protein: Int,
    val jsonResult: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class HistoryItem(
    val id: String,
    val productName: String,
    val overallGrade: String,
    val overallScore: Int,
    val summary: String,
    val jsonResult: String,
    val timestamp: Long
)

data class ShoppingItem(
    val id: String,
    val name: String,
    val checked: Boolean = false,
    val score: Int? = null,
    val grade: String? = null,
    val scanId: String? = null
)

class PureCheckPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("purecheck_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun getApiKey(): String {
        return prefs.getString("gemini_api_key", "") ?: ""
    }

    fun saveApiKey(key: String) {
        prefs.edit().putString("gemini_api_key", key).apply()
    }

    fun getGroqApiKey(): String {
        return prefs.getString("groq_api_key", "") ?: ""
    }

    fun saveGroqApiKey(key: String) {
        prefs.edit().putString("groq_api_key", key).apply()
    }

    fun getDeepSeekApiKey(): String {
        return prefs.getString("deepseek_api_key", "") ?: ""
    }

    fun saveDeepSeekApiKey(key: String) {
        prefs.edit().putString("deepseek_api_key", key).apply()
    }

    fun getBytezApiKey(): String {
        return prefs.getString("bytez_api_key", "") ?: ""
    }

    fun saveBytezApiKey(key: String) {
        prefs.edit().putString("bytez_api_key", key).apply()
    }

    fun getOpenAiApiKey(): String {
        return prefs.getString("openai_api_key", "") ?: ""
    }

    fun saveOpenAiApiKey(key: String) {
        prefs.edit().putString("openai_api_key", key).apply()
    }

    fun getProvider(): String {
        return prefs.getString("active_provider", "gemini") ?: "gemini"
    }

    fun saveProvider(provider: String) {
        prefs.edit().putString("active_provider", provider).apply()
    }

    fun getUserProfile(): UserProfile {
        return UserProfile(
            name = prefs.getString("profile_name", "Veera Venkata Karthik") ?: "Veera Venkata Karthik",
            age = prefs.getInt("profile_age", 21),
            gender = prefs.getString("profile_gender", "Male") ?: "Male",
            height = prefs.getFloat("profile_height", 175f),
            weight = prefs.getFloat("profile_weight", 70f),
            gutHealth = prefs.getBoolean("profile_gut_health", false),
            heartHealth = prefs.getBoolean("profile_heart_health", false),
            vegan = prefs.getBoolean("profile_vegan", false),
            vegetarian = prefs.getBoolean("profile_vegetarian", false),
            diabetic = prefs.getBoolean("profile_diabetic", false),
            cosmeticSafe = prefs.getBoolean("profile_cosmetic_safe", false),
            lowSugar = prefs.getBoolean("profile_low_sugar", false),
            lowSodium = prefs.getBoolean("profile_low_sodium", false),
            highProtein = prefs.getBoolean("profile_high_protein", false),
            cleanIngredients = prefs.getBoolean("profile_clean_ingredients", false)
        )
    }

    fun saveUserProfile(profile: UserProfile) {
        prefs.edit()
            .putString("profile_name", profile.name)
            .putInt("profile_age", profile.age)
            .putString("profile_gender", profile.gender)
            .putFloat("profile_height", profile.height)
            .putFloat("profile_weight", profile.weight)
            .putBoolean("profile_gut_health", profile.gutHealth)
            .putBoolean("profile_heart_health", profile.heartHealth)
            .putBoolean("profile_vegan", profile.vegan)
            .putBoolean("profile_vegetarian", profile.vegetarian)
            .putBoolean("profile_diabetic", profile.diabetic)
            .putBoolean("profile_cosmetic_safe", profile.cosmeticSafe)
            .putBoolean("profile_low_sugar", profile.lowSugar)
            .putBoolean("profile_low_sodium", profile.lowSodium)
            .putBoolean("profile_high_protein", profile.highProtein)
            .putBoolean("profile_clean_ingredients", profile.cleanIngredients)
            .apply()
    }

    // Watchlist Management
    fun getWatchlist(): List<String> {
        val json = prefs.getString("watchlist", "[]") ?: "[]"
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun addToWatchlist(item: String) {
        if (item.isBlank()) return
        val list = getWatchlist().toMutableList()
        val cleanedItem = item.trim()
        if (!list.any { it.equals(cleanedItem, ignoreCase = true) }) {
            list.add(cleanedItem)
            saveWatchlist(list)
        }
    }

    fun removeFromWatchlist(item: String) {
        val list = getWatchlist().toMutableList()
        list.removeAll { it.equals(item.trim(), ignoreCase = true) }
        saveWatchlist(list)
    }

    private fun saveWatchlist(list: List<String>) {
        val json = gson.toJson(list)
        prefs.edit().putString("watchlist", json).apply()
    }

    // History Management
    fun getHistory(): List<HistoryItem> {
        val json = prefs.getString("scan_history", "[]") ?: "[]"
        val type = object : TypeToken<List<HistoryItem>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun addHistoryItem(item: HistoryItem) {
        val history = getHistory().toMutableList()
        history.removeAll { it.id == item.id }
        history.add(0, item)
        if (history.size > 50) {
            history.removeAt(history.size - 1)
        }
        val json = gson.toJson(history)
        prefs.edit().putString("scan_history", json).apply()
    }

    fun updateHistoryItemTitle(id: String, newTitle: String) {
        val history = getHistory().map {
            if (it.id == id) {
                it.copy(productName = newTitle)
            } else {
                it
            }
        }
        val json = gson.toJson(history)
        prefs.edit().putString("scan_history", json).apply()
    }

    fun deleteHistoryItem(id: String) {
        val history = getHistory().toMutableList()
        history.removeAll { it.id == id }
        val json = gson.toJson(history)
        prefs.edit().putString("scan_history", json).apply()
    }

    fun clearHistory() {
        prefs.edit().putString("scan_history", "[]").apply()
    }

    // Shopping List Management
    fun getShoppingList(): List<ShoppingItem> {
        val json = prefs.getString("shopping_list", "[]") ?: "[]"
        val type = object : TypeToken<List<ShoppingItem>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun addShoppingItem(item: ShoppingItem) {
        val list = getShoppingList().toMutableList()
        list.add(item)
        saveShoppingList(list)
    }

    fun updateShoppingItem(item: ShoppingItem) {
        val list = getShoppingList().map {
            if (it.id == item.id) item else it
        }
        saveShoppingList(list)
    }

    fun deleteShoppingItem(id: String) {
        val list = getShoppingList().toMutableList()
        list.removeAll { it.id == id }
        saveShoppingList(list)
    }

    fun clearShoppingList() {
        prefs.edit().putString("shopping_list", "[]").apply()
    }

    private fun saveShoppingList(list: List<ShoppingItem>) {
        val json = gson.toJson(list)
        prefs.edit().putString("shopping_list", json).apply()
    }

    // Favorites Management
    fun getFavorites(): List<FavoriteProduct> {
        val json = prefs.getString("favorite_products", "[]") ?: "[]"
        val type = object : TypeToken<List<FavoriteProduct>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun addFavorite(fav: FavoriteProduct) {
        val list = getFavorites().toMutableList()
        list.removeAll { it.id == fav.id || it.productName.equals(fav.productName, ignoreCase = true) }
        list.add(0, fav)
        val json = gson.toJson(list)
        prefs.edit().putString("favorite_products", json).apply()
    }

    fun removeFavorite(id: String) {
        val list = getFavorites().toMutableList()
        list.removeAll { it.id == id }
        val json = gson.toJson(list)
        prefs.edit().putString("favorite_products", json).apply()
    }

    fun isFavorite(id: String): Boolean {
        return getFavorites().any { it.id == id }
    }

    fun isFavoriteByName(name: String): Boolean {
        return getFavorites().any { it.productName.equals(name, ignoreCase = true) }
    }
}
