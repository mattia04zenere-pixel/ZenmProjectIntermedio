package com.example.zenmprojectintermedio

import android.os.Bundle
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.zenmprojectintermedio.ui.theme.ZenmProjectIntermedioTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()




            ZenmProjectIntermedioTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {

                        composable("home") {
                            SimonStartScreen(
                                onStartClicked = {
                                    navController.navigate("game")
                                }
                            )
                        }


                        composable("game") {
                            SimonSessionScreen(
                                onFinishClicked = { seq ->
                                    navController.navigate("finish/${Uri.encode(seq)}")
                                }
                            )
                        }


                        composable("finish/{seqGen}") {
                                backStackEntry ->
                            SimonHystoryScreen(
                                onBackClicked = { navController.navigate("game") },
                                Uri.decode(backStackEntry.arguments?.getString("seqGen").orEmpty()),


                            )
                        }
                    }
                }
            }
        }
    }
}
