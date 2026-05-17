package com.example.zenmprojectintermedio

import android.content.res.Configuration
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//funzione principale della schermata di gioco
//quella che utilizza le funzioni figlie per far giocare l'utente
@Composable
fun SimonSessionScreen(onFinishClicked: (String) -> Unit, onBackClicked: () -> Unit) {

    //questa è una sfilza di remember savable e mutable state of, in modo tale che ogni volta che una di queste
    // si aggiornano, la schermata viene ricaricata, e tutte le altre variabili rimangono salvate dato che sono
    // remember savable, altrimenti se fossero variabili comuni verrebbero cancellate

    // Questa variabile memorizza la sequenza di lettere premute
    var seqGen by rememberSaveable { mutableStateOf("") }

    // variabile utilizzata per cambiare valore e utilizzo del pulsante "avvia partita" e "fine partita"
    var started by rememberSaveable { mutableStateOf(false) }

    // Variabile per capire se la sessione è attiva (per il testo del pulsante)
    var isGameActive by rememberSaveable { mutableStateOf(false) }

    // //stringa per la sequenza del computer
    var computerSeq by rememberSaveable { mutableStateOf("") }
    // variabile per il colore evidenziato scelto casualmente dal computer
    var activeHighlight by remember { mutableStateOf("") }

    // Coroutine scope per gestire i ritardi durante l'interazione utente
    val scope = rememberCoroutineScope()


    // Funzione per generare la mossa successiva, una stringa con i colori,
    // poi il coputer genera un numero casuale e prende iil carattere corrispondente nella stringa
    fun generateNextMove() {
        val colors = "RGBYMC"
        computerSeq += colors.random()
    }

    // funzione che rende possibile l'animazione da parte del computer, segue la sequenza delle stringhe,
    // e utilizzo la funzione delay in mood da evidenziare per un tot di millisecondi il pulsante da schiacciare
    LaunchedEffect(computerSeq) {
        if (computerSeq.isNotEmpty()) {
            started = false // Blocca l'utente mentre il PC mostra la sequenza
            delay(600)
            for (char in computerSeq) {
                delay(300)
                activeHighlight = char.toString()
                delay(600)
                activeHighlight = ""
            }
            started = true // Turno dell'utente
        }
    }

    // funzione aggiunta in modo tale da averne una sola sia per il back che per end game
    val handleSaveAndExit = {
        if (seqGen.isNotEmpty()) {
            //salva la stringa e passa alla schermata successiva (quella della lista)
            onFinishClicked(seqGen)
        }
        // esce dalla schermata senza salvare la stringa, quando è vuota
        onBackClicked()
    }

    //questa variabile mi serve per capire l'orientamento del dispositivio
    //come visto in Orientation
    val orientation = LocalConfiguration.current.orientation

    //funzione di aggiornamento della stringa, che viene richiamata da ogni pulsante, non è composable dato che
    // si possono fare chiamate composable solo da funzioni composable
    //aggiunta questa funzione per evitare di scrivere 12 volte lo stesso if
    fun updateSeqGen(buttonLabel: String) {
        if(started) {
            val currentInput = if (seqGen.isEmpty()) buttonLabel else "$seqGen, $buttonLabel"
            seqGen = currentInput

            val cleanUserSeq = seqGen.replace(", ", "")

            // Verifica se il tasto premuto è corretto rispetto alla sequenza del PC
            if (cleanUserSeq.last() == computerSeq[cleanUserSeq.length - 1]) {

                // ho aggiunto un delay che mi permette di vedere per un attimo la sequenza di pulsanti premuta,
                // per poi passare a quella successiva del computer, se corretta quella inserita lato utente
                if (cleanUserSeq.length == computerSeq.length) {
                    scope.launch{
                        delay(300)
                    seqGen = "" // Reset visualizzazione per il nuovo turno
                    generateNextMove()
                }
                }
            } else {
                // se sbaglio, la schermata rimane attiva, e posso schiacciare sia back che end game
                // per uscire e salvare quello che ho appena fatto
                started = false
            }
        }
    }

    //if che mi cambia l'orientamento, purtroppo ho dovuto creare due griglie per i pulsanti, che altrimenti risultavano uguali
    //e in modalità landscape mi sembravano brutti da vedere dei pulsanti che fossero 2x3 invece che 3x2
    // ho mantenuto però l'ordine dei colori, sono praticamente li stessi ma girati
    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

        Box(modifier = Modifier.fillMaxSize()) {
            // Pulsante per tornare alla cronologia
            ExtendedFloatingActionButton(onClick = handleSaveAndExit,
                modifier = Modifier.align(Alignment.TopStart).padding(top = 20.dp, start = 20.dp)
            ) {
                Text(text = "< "+stringResource(R.string.back))
            }

            Row(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ColorGridLan(started, activeHighlight) { updateSeqGen(it) }
                Row(
                    modifier = Modifier.fillMaxSize().padding(32.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        TextField(
                            placeholder = { Text(text = stringResource(R.string.clickedSeq)) },
                            readOnly = true,
                            //mostro la sequenza memorizzata, che viene aggiornata alla pressione dei pulsanti
                            value = seqGen,
                            onValueChange = { },
                            maxLines = 5
                        )
                        Buttons(
                            isGameActive = isGameActive,
                            onStartUpdate = {
                                isGameActive = true
                                seqGen = ""
                                computerSeq = ""
                                generateNextMove()
                            },
                            onSaveAndExit = handleSaveAndExit
                        )
                    }
                }
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {

            //else per la modalità portrait dell'app
            Column(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                ColorGridPor(started, activeHighlight) { updateSeqGen(it) }
                Spacer(modifier = Modifier.height(32.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    TextField(
                        placeholder = { Text(text = stringResource(R.string.clickedSeq)) },
                        readOnly = true,
                        //mostro la sequenza memorizzata, che viene aggiornata alla pressione dei pulsanti
                        value = seqGen,
                        onValueChange = { },
                        shape = RoundedCornerShape(12.dp),
                        maxLines = 5
                    )
                    Buttons(
                        isGameActive = isGameActive,
                        onStartUpdate = {
                            isGameActive = true
                            seqGen = ""
                            computerSeq = ""
                            generateNextMove()
                        },
                        onSaveAndExit = handleSaveAndExit
                    )
                }
            }

            // Pulsante per tornare alla cronologia
            ExtendedFloatingActionButton(
                //ora il pulsante back gestisce sia il click per tornare indietro che per finire la partita
                //finisce la partita solo se è stata cliccata almeno una sequenza,altrimenti non salva la partita
                onClick = handleSaveAndExit,
                modifier = Modifier.align(Alignment.TopStart).padding(top = 20.dp, start = 20.dp)
            ) {
                Text(text ="< "+ stringResource(R.string.back))
            }
        }
    }
}

// funzione che popola i pulsanti della griglia, dato che sono 6 tutti uguali, richiamo 6 volte questa funzione invece di
// creare 6 pulsanti che facciano la stesa cosa, così il codice risulta molto più corto
@Composable
fun SimonButton(
    // aggiunti vari boolean a questa funzione, per sapere se possono essere cliccati
    // e per sapere se è il turno dell'utente o del computer
    label: String,
    color: Color,
    isEnabled: Boolean,
    // parametro per sapere se è stato cliccato dal computer
    isHighlighted: Boolean = false,
    onClick: (String) -> Unit
) {
    //variabili per capire se è stato premuto o no
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Cambia di forma se premuto, così da far vedere che è stato premuto
    // Ora cambia forma anche se evidenziato dal computer
    val currentShape = if ((isPressed && isEnabled) || isHighlighted) CircleShape else RoundedCornerShape(12.dp)

    Button(
        onClick = { onClick(label) },
        //copiato il controllo dello stato come in simple BG PLayer
        enabled = isEnabled,
        interactionSource = interactionSource,
        shape = currentShape,
        modifier = Modifier.padding(4.dp).width(120.dp).height(80.dp),
        // Parametri colore corretti per il componente Button
        colors = ButtonDefaults.buttonColors(
            //aggiunti i colori per quando è premibile oppure no
            containerColor = color,
            contentColor = Color.Black,
            // Se il PC evidenzia il tasto, mostriamo il colore originale anche se disabled
            disabledContainerColor = if (isHighlighted) color else Color.LightGray.copy(alpha = 0.5f),
            disabledContentColor = if (isHighlighted) Color.Black else Color.Gray
        )
    ) {
        Text(label)
    }
}

//grligia dei pulsanti per la modalità portrait
@Composable
fun ColorGridPor(isStarted: Boolean, activeHighlight: String, onButtonClick: (String) -> Unit) {
    Column(modifier = Modifier.padding(top = 120.dp)) {
        Row {
            SimonButton("R", Color.Red, isStarted, activeHighlight == "R") { onButtonClick(it) }
            SimonButton("G", Color.Green, isStarted, activeHighlight == "G") { onButtonClick(it) }
        }
        Row {
            SimonButton("B", Color.Blue, isStarted, activeHighlight == "B") { onButtonClick(it) }
            SimonButton("Y", Color.Yellow, isStarted, activeHighlight == "Y") { onButtonClick(it) }
        }
        Row {
            SimonButton("M", Color.Magenta, isStarted, activeHighlight == "M") { onButtonClick(it) }
            SimonButton("C", Color.Cyan, isStarted, activeHighlight == "C") { onButtonClick(it) }
        }
    }
}

//griglia dei pulsanti per la modalità landscape
@Composable
fun ColorGridLan(isStarted: Boolean, activeHighlight: String, onButtonClick: (String) -> Unit) {
    Column(
        modifier = Modifier.padding(start = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            SimonButton("G", Color.Green, isStarted, activeHighlight == "G") { onButtonClick(it) }
            SimonButton("Y", Color.Yellow, isStarted, activeHighlight == "Y") { onButtonClick(it) }
            SimonButton("C", Color.Cyan, isStarted, activeHighlight == "C") { onButtonClick(it) }
        }
        Row {
            SimonButton("R", Color.Red, isStarted, activeHighlight == "R") { onButtonClick(it) }
            SimonButton("B", Color.Blue, isStarted, activeHighlight == "B") { onButtonClick(it) }
            SimonButton("M", Color.Magenta, isStarted, activeHighlight == "M") { onButtonClick(it) }
        }
    }
}

//dato che i pulsanti per cancellare e finire la partita sono due e sempre messi allo stesso moodo,
//in modo analogo a quanto fatto per i pulsanti del gioco ho creato una funzione per rendere il codice più leggibile e corto
@Composable
fun Buttons(
    isGameActive: Boolean, // Variabile per mantenere il testo "End Game"
    onStartUpdate: () -> Unit,
    onSaveAndExit: () -> Unit // Azione unificata per salvare ed uscire
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Cambiato in ExtendedFloatingActionButton per coerenza grafica
        ExtendedFloatingActionButton(
            onClick = {
                if (!isGameActive) {
                    onStartUpdate()
                } else {
                    // Se il gioco è attivo, salva ed esce dalla schermata
                    onSaveAndExit()
                }
            },
            modifier = Modifier.padding(4.dp)
        ) {
            if(!isGameActive) {
                Text(stringResource(R.string.startGame))
            }
            else {
                Text(stringResource(R.string.endGame))
            }
        }
        //pulsante mette il gioco in pausa
        //non ancora attivo, va pensato a come implementarlo poi
        ExtendedFloatingActionButton(
            onClick = { },
            modifier = Modifier.padding(4.dp)
        ) {
            Text(stringResource(R.string.pause))
        }
    }
}