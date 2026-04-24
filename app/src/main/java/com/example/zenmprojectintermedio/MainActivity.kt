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
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.zenmprojectintermedio.ui.theme.ZenmProjectIntermedioTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


//classe principale del progetto Simon
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            // genero una lista di partite con remember savable, in questo modo ogni schermata chiamata da qui
            // potrà vederla, in questo modo all'apertura dell'app viene generata la lista e alla sua chiusara
            // verrà eliminata
            val historyList = rememberSaveable(
                saver = listSaver(
                    save = { it.toList() },
                    restore = { it.toMutableStateList() }
                )
            ) { mutableStateListOf<String>() }

            //solita organizzazione delle schermata per la navigazione interna
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
                                    //aggiungo la sequenza appena premuta alla lista di sequenze
                                    if (seq.isNotEmpty()) {
                                        historyList.add(seq)
                                    }
                                    navController.navigate("finish/${Uri.encode(seq)}")
                                }
                            )
                        }

                        composable("finish/{seqGen}") {
                            //qui prima passavo una lista intera, mentre ora passo tutta la lista
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
