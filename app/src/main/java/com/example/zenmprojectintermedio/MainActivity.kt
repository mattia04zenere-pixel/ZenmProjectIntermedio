package com.example.zenmprojectintermedio

import android.os.Bundle
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.toMutableStateList
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

            // Lista delle partite salvata nella sessione corrente
            val historyList = rememberSaveable(
                saver = listSaver(
                    save = { it.toList() },
                    restore = { it.toMutableStateList() }
                )
            ) { mutableStateListOf<String>() }

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
                                    // Aggiungiamo la sequenza alla lista globale prima di navigare
                                    if (seq.isNotEmpty()) {
                                        historyList.add(seq)
                                    }
                                    navController.navigate("finish/${Uri.encode(seq)}")
                                }
                            )
                        }

                        composable("finish/{seqGen}") {
                            // Passiamo l'intera historyList invece di una singola stringa
                            SimonHystoryScreen(
                                onBackClicked = { navController.navigate("game") },
                                historyList = historyList
                            )
                        }
                    }
                }
            }
        }
    }
}
