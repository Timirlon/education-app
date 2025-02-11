package com.example.education

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.Modifier
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.education.ui.screens.*
import com.example.education.ui.theme.EducationalPlatformTheme
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EducationalPlatformTheme {
                val navController = rememberNavController()
                MainScreen(navController)
            }
        }

        auth = FirebaseAuth.getInstance()
    }

    fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registration successful
                    val user = auth.currentUser
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                } else {
                    // Registration failed
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login successful
                    val user = auth.currentUser
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                } else {
                    // Login failed
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    fun logoutUser() {
        auth.signOut()
        Toast.makeText(this, "Logged out!", Toast.LENGTH_SHORT).show()
    }

}

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    fun MainScreen(navController: NavHostController) {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Educational Platform") }) },
            bottomBar = { BottomNavigationBar(navController) }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                NavigationGraph(navController)
            }
        }
    }

    @Composable
    fun NavigationGraph(navController: NavHostController) {
        NavHost(navController, startDestination = "home") {
            composable("home") { HomeScreen(navController) }
            composable("form") { FormScreen(navController) }
            composable("profile") { ProfileScreen(navController) }
            composable("profession/{professionId}") { backStackEntry ->
                val professionId = backStackEntry.arguments?.getString("professionId") ?: ""
                ProfessionScreen(navController, professionId)
            }
        }
    }

    @Composable
    fun BottomNavigationBar(navController: NavHostController) {
        val currentDestination by navController.currentBackStackEntryAsState()
        NavigationBar {
            NavigationBarItem(
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                label = { Text("Home") },
                selected = currentDestination?.destination?.route == "home",
                onClick = { navController.navigate("home") }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Add, contentDescription = "Form") },
                label = { Text("Form") },
                selected = currentDestination?.destination?.route == "form",
                onClick = { navController.navigate("form") }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                label = { Text("Profile") },
                selected = currentDestination?.destination?.route == "profile",
                onClick = { navController.navigate("profile") }
            )
        }
    }
