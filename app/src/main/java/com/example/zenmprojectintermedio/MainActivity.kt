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

    // Otteniamo l'istanza del Database SQLite Nativo
    private val database by lazy { SimonSqliteDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val coroutineScope = rememberCoroutineScope()

            // Creiamo una lista mutabile inizialmente vuota per garantire un avvio istantaneo e leggero
            val historyList = rememberSaveable(
                saver = listSaver(
                    save = { it.toList() },
                    restore = { it.toMutableStateList() }
                )
            ) { mutableStateListOf<String>() }

            // Carichiamo i dati dal Database in background tramite Coroutine IO non appena lo schermo si avvia, evitando blocchi e crash
            LaunchedEffect(Unit) {
                if (historyList.isEmpty()) {
                    val savedData = withContext(Dispatchers.IO) {
                        database.getAllGames()
                    }
                    historyList.addAll(savedData)
                }
            }

            // Variabile per tenere traccia dell'indice dell'elemento selezionato nella lista
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

                                        // Salvataggio asincrono nel database nativo su thread IO dedicato
                                        coroutineScope.launch(Dispatchers.IO) {
                                            database.insertGame(seq)
                                        }
                                    }
                                    navController.navigate("finish/${Uri.encode(seq)}")
                                },
                                onBackClicked = { navController.navigate("finish/view") }
                            )
                        }

                        composable("finish/{seqGen}") {
                                backStackEntry ->
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