package com.example.purecheck.ui.main

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.example.purecheck.ui.components.PureCheckColors
import com.example.purecheck.ui.home.HomeScreen
import com.example.purecheck.ui.scanner.ScanOptionsScreen
import com.example.purecheck.ui.profile.ProfileScreen

enum class AppTab {
    Home,
    Scan,
    Profile
}

@Composable
fun MainAppContainer(
    onNavigate: (NavKey) -> Unit,
    modifier: Modifier = Modifier
) {
    var activeTab by remember { mutableStateOf(AppTab.Home) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = PureCheckColors.darkBg,
        bottomBar = {
            NavigationBar(
                containerColor = PureCheckColors.cardBg,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .border(
                        width = 1.dp,
                        color = Color(0xFF1E2B44),
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
            ) {
                NavigationBarItem(
                    selected = activeTab == AppTab.Home,
                    onClick = { activeTab = AppTab.Home },
                    icon = { 
                        Icon(
                            imageVector = Icons.Default.Home, 
                            contentDescription = "Home",
                            modifier = Modifier.size(if (activeTab == AppTab.Home) 24.dp else 20.dp)
                        ) 
                    },
                    label = { Text("Home", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PureCheckColors.accentGreen,
                        selectedTextColor = PureCheckColors.accentGreen,
                        unselectedIconColor = PureCheckColors.textSecondary,
                        unselectedTextColor = PureCheckColors.textSecondary,
                        indicatorColor = PureCheckColors.cardBgElevated
                    )
                )
                NavigationBarItem(
                    selected = activeTab == AppTab.Scan,
                    onClick = { activeTab = AppTab.Scan },
                    icon = { 
                        Icon(
                            imageVector = Icons.Default.Search, 
                            contentDescription = "Scan",
                            modifier = Modifier.size(if (activeTab == AppTab.Scan) 24.dp else 20.dp)
                        ) 
                    },
                    label = { Text("Scan", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PureCheckColors.accentGreen,
                        selectedTextColor = PureCheckColors.accentGreen,
                        unselectedIconColor = PureCheckColors.textSecondary,
                        unselectedTextColor = PureCheckColors.textSecondary,
                        indicatorColor = PureCheckColors.cardBgElevated
                    )
                )
                NavigationBarItem(
                    selected = activeTab == AppTab.Profile,
                    onClick = { activeTab = AppTab.Profile },
                    icon = { 
                        Icon(
                            imageVector = Icons.Default.Person, 
                            contentDescription = "Profile",
                            modifier = Modifier.size(if (activeTab == AppTab.Profile) 24.dp else 20.dp)
                        ) 
                    },
                    label = { Text("Profile", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PureCheckColors.accentGreen,
                        selectedTextColor = PureCheckColors.accentGreen,
                        unselectedIconColor = PureCheckColors.textSecondary,
                        unselectedTextColor = PureCheckColors.textSecondary,
                        indicatorColor = PureCheckColors.cardBgElevated
                    )
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (activeTab) {
                AppTab.Home -> HomeScreen(onNavigate = onNavigate)
                AppTab.Scan -> ScanOptionsScreen(onNavigate = onNavigate)
                AppTab.Profile -> ProfileScreen(onNavigate = onNavigate)
            }
        }
    }
}
