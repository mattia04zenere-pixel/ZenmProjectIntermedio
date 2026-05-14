package com.example.zenmprojectintermedio

import android.content.Context
import android.os.Bundle
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.zenmprojectintermedio.ui.theme.ZenmProjectIntermedioTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.zenmprojectintermedio.SimonGameDetailsScreen

//classe principale del progetto Simon
class MainActivity : ComponentActivity() {

    // Metodo per salvare la lista delle partite nelle SharedPreferences
    private fun saveHistory(history: List<String>) {
        val prefs = getSharedPreferences("simon_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("history_data", history.joinToString("§")).apply()
    }

    // Metodo per caricare la lista delle partite dalle SharedPreferences
    private fun loadHistory(): List<String> {
        val prefs = getSharedPreferences("simon_prefs", Context.MODE_PRIVATE)
        val savedData = prefs.getString("history_data", "") ?: ""
        return if (savedData.isEmpty()) emptyList() else savedData.split("§")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            // Carichiamo i dati salvati all'avvio
            val savedHistory = remember { loadHistory() }

            // genero una lista di partite con remember savable, in questo modo ogni schermata chiamata da qui
            // potrà vederla, in questo modo all'apertura dell'app viene generata la lista e alla sua chiusara
            // verrà eliminata (Nota: ora inizializzata con i dati persistenti)
            val historyList = rememberSaveable(
                saver = listSaver(
                    save = { it.toList() },
                    restore = { it.toMutableStateList() }
                )
            ) { savedHistory.toMutableStateList() }

            // vorrei che clicando su un elemento della lazycolumn, esso venisse passato alla schermata SimonGameDetailsScreen,
            // attraverso un remember savable, in modo che l'elemento della lazy column possa essere visualizzato
            // più in grande e senza troncamenti. Qui memorizziamo l'indice dell'elemento selezionato.
            val selectedIndex = rememberSaveable { mutableStateOf(-1) }

            //solita organizzazione delle schermata per la navigazione interna
            ZenmProjectIntermedioTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        // Modificato per partire direttamente dalla schermata history (mappata su finish/{seqGen})
                        startDestination = "finish/view",
                        modifier = Modifier.padding(innerPadding)
                    ) {


                        composable("game") {
                            SimonSessionScreen(
                                onFinishClicked = { seq ->
                                    //aggiungo la sequenza appena premuta alla lista di sequenze
                                    if (seq.isNotEmpty()) {
                                        historyList.add(seq)
                                        // Salvataggio persistente su SharedPreferences
                                        saveHistory(historyList.toList())
                                    }
                                    navController.navigate("finish/${Uri.encode(seq)}")
                                },
                                onBackClicked = { navController.navigate("finish/view") }
                            )
                        }

                        composable("finish/{seqGen}") {
                                backStackEntry ->
                            //qui prima passavo una lista intera, mentre ora passo tutta la lista
                            SimonHystoryScreen(
                                onBackClicked = { navController.navigate("game") },
                                historyList = historyList,
                                onItemClicked = { index ->
                                    // Salviamo l'indice selezionato e navighiamo ai dettagli
                                    selectedIndex.value = index
                                    navController.navigate("details")
                                }
                            )
                        }

                        composable("details") {
                            // Visualizziamo la schermata dei dettagli usando l'indice salvato nel rememberSaveable
                            val index = selectedIndex.value
                            if (index != -1 && index < historyList.size) {
                                SimonGameDetailsScreen(
                                    gameNumber = index + 1,
                                    sequence = historyList[index],
                                    onBackClicked = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
