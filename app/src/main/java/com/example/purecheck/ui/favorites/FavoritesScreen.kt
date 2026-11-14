package com.example.purecheck.ui.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import com.example.purecheck.Result
import com.example.purecheck.data.FavoriteProduct
import com.example.purecheck.data.PureCheckPreferences
import com.example.purecheck.ui.components.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onBack: () -> Unit,
    onNavigate: (NavKey) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs = remember { PureCheckPreferences(context) }
    var favoritesList by remember { mutableStateOf(emptyList<FavoriteProduct>()) }
    var searchQuery by remember { mutableStateOf("") }
    var activeFilter by remember { mutableStateOf("All") } // All, Grade A, High Protein, Low Calorie

    // Load initial favorites
    LaunchedEffect(Unit) {
        favoritesList = prefs.getFavorites()
    }

    val filteredList = remember(favoritesList, searchQuery, activeFilter) {
        var list = favoritesList.filter {
            it.productName.contains(searchQuery, ignoreCase = true)
        }
        list = when (activeFilter) {
            "Grade A" -> list.filter { it.overallGrade.equals("A", ignoreCase = true) }
            "High Protein" -> list.filter { it.protein >= 15 }
            "Low Calorie" -> list.filter { it.calories > 0 && it.calories <= 150 }
            else -> list
        }
        list
    }

    val darkBg = Color(0xFF0B0F19)
    val cardBg = Color(0xFF1E293B)
    val accentGreen = Color(0xFF10B981)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Favorites", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = darkBg)
            )
        },
        containerColor = darkBg
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search saved products...", color = Color.Gray, fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = accentGreen,
                    unfocusedBorderColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All", "Grade A", "High Protein", "Low Calorie").forEach { filter ->
                    val isSelected = activeFilter == filter
                    FilterChip(
                        selected = isSelected,
                        onClick = { activeFilter = filter },
                        label = { Text(filter, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = accentGreen,
                            selectedLabelColor = Color.White,
                            containerColor = cardBg,
                            labelColor = Color.Gray
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyState(
                        title = if (searchQuery.isNotEmpty() || activeFilter != "All") "No matches found" else "No favorites yet",
                        description = if (searchQuery.isNotEmpty() || activeFilter != "All") 
                            "Try adjusting your search query or active filter chips." 
                            else "Save products you frequently use to access them quickly.",
                        icon = Icons.Default.Star
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredList) { item ->
                        FavoriteProductCard(
                            item = item,
                            onClick = {
                                onNavigate(Result(item.jsonResult))
                            },
                            onDelete = {
                                prefs.removeFavorite(item.id)
                                favoritesList = prefs.getFavorites()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteProductCard(
    item: FavoriteProduct,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val cardBg = Color(0xFF1E293B)
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val dateString = sdf.format(Date(item.timestamp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(cardBg)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GradeBadge(grade = item.overallGrade, size = 42)

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                item.productName,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Score: ${item.overallScore}/100 • ${item.calories} kcal • ${item.protein}g Protein",
                color = Color.LightGray,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Saved: $dateString",
                color = Color.Gray,
                fontSize = 10.sp
            )
        }

        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Remove Favorite",
                tint = Color.Gray
            )
        }
    }
}
