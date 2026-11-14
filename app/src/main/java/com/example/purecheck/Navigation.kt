package com.example.purecheck

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.purecheck.ui.main.MainAppContainer
import com.example.purecheck.ui.main.MainScreen
import com.example.purecheck.ui.scanner.ScannerScreen
import com.example.purecheck.ui.scanner.BarcodeScannerScreen
import com.example.purecheck.ui.result.ResultScreen
import com.example.purecheck.ui.settings.SettingsScreen
import com.example.purecheck.ui.author.AuthorScreen
import com.example.purecheck.ui.profile.MyProfileScreen
import com.example.purecheck.ui.favorites.FavoritesScreen

@Composable
fun MainNavigation() {
  val backStack = rememberNavBackStack(Main)

  NavDisplay(
    backStack = backStack,
    onBack = { backStack.removeLastOrNull() },
    entryProvider =
      entryProvider {
        entry<Main> {
          MainAppContainer(
              onNavigate = { navKey -> backStack.add(navKey) }
          )
        }
        entry<Scanner> {
          ScannerScreen(
              onBack = { backStack.removeLastOrNull() },
              onAnalysisFinished = { jsonResult ->
                  // Go back, then open the result
                  backStack.removeLastOrNull()
                  backStack.add(Result(jsonResult))
              }
          )
        }
        entry<Result> { key ->
          ResultScreen(
              jsonResult = key.jsonResult,
              onBack = { backStack.removeLastOrNull() },
              onNavigate = { dest -> backStack.add(dest) }
          )
        }
        entry<Settings> {
          SettingsScreen(
              onBack = { backStack.removeLastOrNull() },
              onNavigate = { dest -> backStack.add(dest) }
          )
        }
        entry<Author> {
          AuthorScreen(
              onBack = { backStack.removeLastOrNull() }
          )
        }
        entry<MyProfile> {
          MyProfileScreen(
              onBack = { backStack.removeLastOrNull() }
          )
        }
        entry<Favorites> {
          FavoritesScreen(
              onBack = { backStack.removeLastOrNull() },
              onNavigate = { dest -> backStack.add(dest) }
          )
        }
        entry<BarcodeScanner> {
          BarcodeScannerScreen(
              onBack = { backStack.removeLastOrNull() },
              onAnalysisFinished = { jsonResult ->
                  // Go back, then open the result
                  backStack.removeLastOrNull()
                  backStack.add(Result(jsonResult))
              }
          )
        }
      },
  )
}
