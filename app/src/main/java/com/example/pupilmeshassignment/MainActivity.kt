package com.example.pupilmeshassignment

import android.os.Bundle
import android.provider.ContactsContract.Data
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.isPopupLayout
import androidx.core.view.ViewCompat
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pupilmeshassignment.auth.SignInScreen
import com.example.pupilmeshassignment.home.HomeScreen
import com.example.pupilmeshassignment.home.HomeScreenPreview
import com.example.pupilmeshassignment.ui.theme.PupilMeshAssignmentTheme
import com.example.pupilmeshassignment.utils.DataStoreManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PupilMeshAssignmentTheme {
                App()
            }
        }
    }
}

@Composable
fun App() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val dataStoreManager = remember { DataStoreManager.getInstance(context) }
    val isUserSignedIn = dataStoreManager.getUserSignedIn.collectAsState(initial = false)
    val startDestination = if (isUserSignedIn.value) "Home" else "SignIn"
    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = "SignIn") {
            SignInScreen() {
                navController.navigate("Home") {
                    popUpTo("SignIn") {
                        inclusive = true
                    }
                }
            }
        }
        composable(route = "Home") {
            HomeScreen()
        }
    }
}


