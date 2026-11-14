package com.example.purecheck.ui.author

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.purecheck.R
import com.example.purecheck.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val darkBg = Color(0xFF0B0F19)
    val cardBg = Color(0xFF1E293B)
    val accentGreen = Color(0xFF10B981)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About NutriCheck", color = Color.White, fontWeight = FontWeight.Bold) },
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
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // App Logo Placeholder
            NutriCheckLogo(logoSize = 80.dp)

            Spacer(modifier = Modifier.height(16.dp))

            // App Title and Version
            Text(
                text = "NutriCheck",
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 24.sp
            )
            Text(
                text = "Version 2.0 (Production Build)",
                color = Color.Gray,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Short Description
            Text(
                text = "A production-grade food and cosmetic safety scanner app that decodes package labels, unmasks hidden chemical additives, flags allergens, and audits personal health risks.",
                color = Color.LightGray,
                fontSize = 13.sp,
                lineHeight = 19.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Developer Section Card
            Text(
                text = "DEVELOPER PROFILE",
                color = accentGreen,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(8.dp))
            PureCheckCard(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(Color.DarkGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.author_photo),
                            contentDescription = "Author Photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "V. V. Karthik Barrenkala",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Roll Number: R210111",
                            color = accentGreen,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = Color(0xFF334155))
                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(Color.DarkGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.author_uday),
                            contentDescription = "Contributor Photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Machipalli Uday Bhaskar",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Roll Number: R210288",
                            color = accentGreen,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = Color(0xFF334155))
                Spacer(modifier = Modifier.height(12.dp))
                
                Text("Department", color = Color.Gray, fontSize = 10.sp)
                Text("Computer Science & Engineering", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text("Institution", color = Color.Gray, fontSize = 10.sp)
                Text("Rajiv Gandhi University of Knowledge Technologies", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text("Project Context", color = Color.Gray, fontSize = 10.sp)
                Text("Submitted as Major Project Evaluation System", color = Color.White, fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 16.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Tech Stack Section
            Text(
                text = "TECHNOLOGY & INTELLIGENCE STACK",
                color = accentGreen,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(8.dp))
            PureCheckCard(modifier = Modifier.fillMaxWidth()) {
                Text("Core Infrastructure", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text("• Kotlin, Jetpack Compose, Navigation3\n• CameraX API, Google ML Kit (Barcode & Text OCR)\n• SharedPreferences & JSON GSON Serialization", color = Color.LightGray, fontSize = 12.sp, lineHeight = 18.sp, modifier = Modifier.padding(top = 4.dp))
                
                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = Color(0xFF334155))
                Spacer(modifier = Modifier.height(12.dp))

                Text("Core Intelligence Engines", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text("• Google Gemini 2.0 Flash (GenerateContent API)\n• LLaMA 3.2 Vision & Qwen Models (Groq Cloud & Bytez APIs)\n• DeepSeek v3 Chat API integration", color = Color.LightGray, fontSize = 12.sp, lineHeight = 18.sp, modifier = Modifier.padding(top = 4.dp))
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
