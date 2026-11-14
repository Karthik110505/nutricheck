package com.example.purecheck

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data object Main : NavKey
@Serializable data object Scanner : NavKey
@Serializable data class Result(val jsonResult: String) : NavKey
@Serializable data object Settings : NavKey
@Serializable data object Author : NavKey
@Serializable data object BarcodeScanner : NavKey
@Serializable data object MyProfile : NavKey
@Serializable data object Favorites : NavKey

