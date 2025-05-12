package com.example.pupilmeshassignment.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun HomeScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.DarkGray, modifier = Modifier.fillMaxWidth()) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = "Home",
                            tint = Color.White
                        )
                    },
                    label = { Text("Manga", color = Color.White) },
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(
                            Icons.Default.Face, contentDescription = "Face", tint = Color.White
                        )
                    },
                    label = { Text("Face", color = Color.White) })
            }
        }
    ) { paddingValues ->
        when (selectedTab) {
            0 -> {
                var currentScreen by remember { mutableStateOf("AnimeScreen") }

                when (currentScreen) {
                    "AnimeScreen" -> {
                        AnimeScreen(paddingValues = paddingValues) {
                            currentScreen = "AnimeDetailScreen"
                        }
                    }

                    "AnimeDetailScreen" -> {
                        AnimeDetailScreen() {
                            currentScreen = "AnimeScreen"
                        }
                    }
                }
            }

            1 -> {
                FaceDetectionScreen(paddingValues)
            }
        }
    }

}

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    HomeScreen()
}