package com.example.zenmprojectintermedio

import android.os.Bundle
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.zenmprojectintermedio.ui.theme.ZenmProjectIntermedioTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//classe principale del progetto Simon
class MainActivity : ComponentActivity() {

    // ottengo la lista delle partite dal Database SQLite
    private val database by lazy { SimonSqliteDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val coroutineScope = rememberCoroutineScope()

            // creazione della lista di partite giocate mutabile inizialmente vuota
            val historyList = rememberSaveable(
                saver = listSaver(
                    save = { it.toList() },
                    restore = { it.toMutableStateList() }
                )
            ) { mutableStateListOf<String>() }

            // carichiamo i dati dal Database in background tramite Coroutine IO non appena lo schermo si avvia, evitando blocchi e crash
            LaunchedEffect(Unit) {
                if (historyList.isEmpty()) {
                    val savedData = withContext(Dispatchers.IO) {
                        database.getAllGames()
                    }
                    historyList.addAll(savedData)
                }
            }

            // variabile per tenere traccia dell'indice dell'elemento selezionato nella lista
            val selectedIndex = rememberSaveable { mutableStateOf(-1) }

            //vsolita organizzazione delle schermata per la navigazione interna
            ZenmProjectIntermedioTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        // modificato per partire direttamente dalla schermata history (mappata su finish/{seqGen})
                        startDestination = "finish/view",
                        modifier = Modifier.padding(innerPadding)
                    ) {


                        composable("game") {
                            SimonSessionScreen(
                                onFinishClicked = { seq ->
                                    // aggiungo la sequenza appena premuta alla lista di sequenze
                                    if (seq.isNotEmpty()) {
                                        historyList.add(seq)

                                        // salvataggio asincrono nel database su thread IO dedicato
                                        // usiamo il coroutineScope della MainActivity così siamo sicuri che sopravviva alla navigazione
                                        coroutineScope.launch(Dispatchers.IO) {
                                            database.insertGame(seq)
                                        }

                                        // se la partita è salvata con una sequenza reale, andiamo alla schermata dei risultati specifici
                                        navController.navigate("finish/${Uri.encode(seq)}") {
                                            popUpTo("game") { inclusive = true } // Pulisce lo stack per evitare loop con il tasto back
                                        }
                                    } else {
                                        // se la sequenza è vuota (es. uscita immediata alla prima mossa), torniamo semplicemente alla lista generale
                                        navController.navigate("finish/view") {
                                            popUpTo("game") { inclusive = true }
                                        }
                                    }
                                },
                                onBackClicked = {
                                    // gestisce il click sul pulsante grafico di uscita quando non c'è una partita da salvare
                                    navController.navigate("finish/view") {
                                        popUpTo("game") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("finish/{seqGen}") {
                                backStackEntry ->
                            SimonHystoryScreen(
                                onBackClicked = { navController.navigate("game") },
                                historyList = historyList,
                                onItemClicked = { index ->
                                    // salvataggio dell'indice selezionato e navighiamo ai dettagli
                                    selectedIndex.value = index
                                    navController.navigate("details")
                                }
                            )
                        }

                        composable("details") {
                            // visualizzazione della schermata dei dettagli usando l'indice salvato nel rememberSaveable
                            if (selectedIndex.value != -1 && selectedIndex.value < historyList.size) {
                                SimonGameDetailsScreen(
                                    sequence = historyList[selectedIndex.value],
                                    onBackClicked = {
                                        // ritorno dallla schermata di dettaglio
                                        navController.popBackStack()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}