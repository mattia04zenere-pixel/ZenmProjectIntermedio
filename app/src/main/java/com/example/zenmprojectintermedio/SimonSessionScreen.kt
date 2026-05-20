package com.example.zenmprojectintermedio

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
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
    started: Boolean,
    isPaused: Boolean,
    isGameOver: Boolean,
    isCompTurn: Boolean,
    onStartUpdate: (Boolean) -> Unit,
    onPauseToggle: () -> Unit,
    onFinishAndExit: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Pulsante per Iniziare / Terminare il gioco
        ExtendedFloatingActionButton(
            onClick = {
                if (!started) {
                    onStartUpdate(true)
                } else {
                    onFinishAndExit()
                }
            },
            modifier = Modifier.padding(4.dp)
        ) {
            if(!started) {
                Text(stringResource(R.string.startGame))
            }
            else {
                Text(stringResource(R.string.endGame))
            }
        }

        // Il pulsante Pausa appare SOLO se la partita è avviata E NON è finita per un errore
        if (started && !isGameOver) {
            // Calcoliamo se deve essere attivo logicamente
            val isClickable = isCompTurn || isPaused

            ExtendedFloatingActionButton(
                // Se è cliccabile esegue il toggle, altrimenti la lambda è vuota e il tasto non fa nulla
                onClick = { if (isClickable) onPauseToggle() },
                modifier = Modifier.padding(4.dp),
            ) {
                if (isPaused) {
                    Text(stringResource(R.string.resume))
                } else {
                    Text(stringResource(R.string.pause))
                }
            }
        }
    }
}

//funzione principale della schermata di gioco
@Composable
fun SimonSessionScreen(onFinishClicked: (String) -> Unit, onBackClicked: () -> Unit) {

    // Questa variabile memorizza la sequenza di lettere premute
    var seqGen by rememberSaveable { mutableStateOf("") }

    //variabile aggiunta per l'uscita senza errori dalla schermata principale
    var seqGenNoError by rememberSaveable { mutableStateOf("") }

    // variabile utilizzata per cambiare valore e utilizzo del pulsante "avvia partita" e "fine partita"
    var started by rememberSaveable { mutableStateOf(false) }

    // Variabile di stato per capire se il gioco si trova in uno stato di pausa temporanea
    var isPaused by rememberSaveable { mutableStateOf(false) }

    // Sequenza interna del computer
    var computerSeq by rememberSaveable { mutableStateOf("") }

    // Tasto attualemente illuminato dal computer
    var activeHighlight by remember { mutableStateOf("") }

    // Memorizza l'indice della letterai che il computer sta riproducendo per non resettarsi alla rotazione
    //altrimenti iniziava di nuovo dal punto di partenza, e non era il requisito della consegna
    var currentAnimIndex by rememberSaveable { mutableStateOf(-1) }

    // CORRETTO: Coroutine scope per gestire i ritardi durante l'interazione utente
    // così da non passare subito al pulsante successivo ma si ha il tempo di memorizzare la sequenza
    val scope = rememberCoroutineScope()

    // Funzione per aggiungere una mossa casuale da parte del computer, le mosse vengono prese con la funzione random
    // e fanno riferimento ad uno dei colori dei pulsanti
    fun generateNextMove() {
        // variabile aggiunta in modo che alla rotaazione dello schermo, quando la sequenza del computer è finita
        // essa non riparta dall'inizio a generarsi
        seqGenNoError = seqGen
        currentAnimIndex = -1
        val colors = "RGBYMC"
        computerSeq += colors.random()
        seqGen= ""
    }

    // Innesca la prima mossa all'avvio della partita
    // generata casualmente partendo dalla stringa di colori sopra
    LaunchedEffect(started) {
        if (started && computerSeq.isEmpty()) {
            generateNextMove()
        }
    }

    // Gestione dell'animazione del computer senza ripartire da zero alla rotazione dello schermo
    LaunchedEffect(started, computerSeq, isPaused) {
        //solo se è iniziata la partita, la stringa del computer non è vuota e NON siamo in pausa
        // entr ain questo if per continuare con la sequenza
        if (started && computerSeq.isNotEmpty() && !isPaused) {
            // indici per capire a che punto è il computer con la sua sequenza
            if (currentAnimIndex == -1) {
                currentAnimIndex = 0
                delay(400)
            }
            // while per gestire l'animazione del computer
            while (currentAnimIndex < computerSeq.length && !isPaused) {
                val char = computerSeq[currentAnimIndex]
                // delay per far vedere all'utente la mossa
                delay(200)
                // Controllo di sicurezza se l'utente mette in pausa proprio mentre la coroutine sta aspettando
                if (isPaused) break
                activeHighlight = char.toString()
                //tempo totale tra una mossa e l'altra
                delay(400)
                activeHighlight = ""
                // aggiornamento indice del computer
                currentAnimIndex++
            }
        }
    }

    // Funzione centralizzata per gestire l'uscita salvando la partita se è in corso
    val handleExit = {
        // recupero l'ultima sequenza valida digitata se l'utente si trova nel mezzo dell'esposizione del PC
        val stringToSave = if (seqGen.isNotEmpty()) seqGen else seqGenNoError

        if (started && stringToSave.isNotEmpty()) {
            onFinishClicked(seqExitHighlightedError(computerSeq, stringToSave))
            seqGen = ""
            seqGenNoError = ""
            started = false
            isPaused = false
            currentAnimIndex = -1 // Resetti l'indice anche qui per sicurezza
            onBackClicked() // Torna indietro dopo aver salvato la partita terminata con successo
        }
        // di default, se la stringa è vuota, quella inserita dall'utente (ergo alla prima volta che si entra nella schermata e si preme avvio)
        // esce senza salvare nulla, avviene solo se non metto input
        else onBackClicked()
    }

    // Intercetta il tasto back fisico del telefono e lo costringe a usare handleExit
    BackHandler {
        handleExit()
    }

    //questa variabile mi serve per capire l'orientamento del dispositivio
    //come visto in Orientation
    val orientation = LocalConfiguration.current.orientation

    // Calcolo dinamico del reale stato del turno utente (tiene conto dello stato di avanzamento dell'animazione PC e della pausa)
    val isUserTurnNow = started && !isPaused && (currentAnimIndex == -1 || currentAnimIndex >= computerSeq.length)

    // Calcolo dinamico del turno del computer (ovvero quando NON è il turno dell'utente)
    val isComputerTurnNow = started && !isUserTurnNow

    //funzione di aggiornamento della stringa, che viene richiamata da ogni pulsante, non è composable dato che
    // si possono fare chiamate composable solo da funzioni composable
    //aggiunta questa funzione per evitare di scrivere 12 volte lo stesso if
    // inoltre controlla anche la correttzza della stringa inserita dall'utente
    fun updateSeqGen(buttonLabel: String) {
        if(isUserTurnNow) {
            if (seqGen.length == 0)
                seqGen = buttonLabel
            else
                seqGen = seqGen + ", " + buttonLabel
            //variabile generata per confronto con stringa del computer
            val cleanUserSeq = seqGen.replace(", ", "")

            // sincronizzo la variabile di backup a ogni tocco valido dell'utente
            seqGenNoError = seqGen

            if (computerSeq.isNotEmpty() && cleanUserSeq.length <= computerSeq.length) {
                if (cleanUserSeq.last() == computerSeq[cleanUserSeq.length - 1]) {
                    if (cleanUserSeq.length == computerSeq.length) {
                        scope.launch {
                            delay(200)
                            generateNextMove()
                        }
                    }
                } else {
                    //meno due così la schermata si blocca e non permette più inseriemnto da parte dell'utente
                    currentAnimIndex = -2
                }
            }
        }
    }

    //if che mi cambia l'orientamento, purtroppo ho dovuto creare due griglie per i pulsanti, che altrimenti risultavano uguali
    //e in modalità landscape mi sembravano brutti da vedere dei pulsanti che fossero 2x3 invece che 3x2
    // ho mantenuto però l'ordine dei colori, sono praticamente li stessi ma girati
    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

        Box(modifier = Modifier.fillMaxSize()) {
            // Pulsante per tornare alla cronologia
            ExtendedFloatingActionButton(onClick = handleExit,
                modifier = Modifier.align(Alignment.TopStart).padding(top = 20.dp, start = 20.dp)
            ) {
                Text(text = "< "+stringResource(R.string.back))
            }

            Row(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ColorGridLan(isStarted = isUserTurnNow, activeHighlight = activeHighlight) { updateSeqGen(it) }
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
                            started = started,
                            isPaused = isPaused,
                            isGameOver = (currentAnimIndex == -2),
                            isCompTurn = isComputerTurnNow, // Passiamo lo stato del turno PC
                            onStartUpdate = { started = it },
                            onPauseToggle = { isPaused = !isPaused }, // Cambia lo stato logico di pausa ad ogni clic
                            onFinishAndExit = handleExit // Passato handleExit per il pulsante EndGame in Landscape
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

                ColorGridPor(isStarted = isUserTurnNow, activeHighlight = activeHighlight) { updateSeqGen(it) }
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
                        started = started,
                        isPaused = isPaused,
                        isGameOver = (currentAnimIndex == -2),
                        isCompTurn = isComputerTurnNow, // Passamo lo stato del turno PC
                        onStartUpdate = { started = it },
                        onPauseToggle = { isPaused = !isPaused }, // Cambia lo stato logico di pausa ad ogni clic
                        onFinishAndExit = handleExit // Passato handleExit per il pulsante EndGame in Portrait
                    )
                }
            }

            // Pulsante per tornare alla cronologia
            //ora il pulsante back gestisce sia il click per tornare indietro che per finire la partita
            //finisce la partita solo se è stata cliccata almeno una sequenza,altrimenti non salva la partita
            ExtendedFloatingActionButton(
                onClick = { handleExit() },
                modifier = Modifier.align(Alignment.TopStart).padding(top = 20.dp, start = 20.dp)
            ) {
                Text(text ="< "+ stringResource(R.string.back))
            }
        }
    }
}

// funzione che popola i pulsanti della griglia, dato che sono 6 tutti uguali, richiamo 6 volte questa funzione invece di
// creare 6 pulsanti che facciano la stesa cosa, così il codice resulta molto più corto
@Composable
fun SimonButton(label: String, color: Color, isEnabled: Boolean, isHighlighted: Boolean, onClick: (String) -> Unit) {
    //variabili per capire se è stato premuto o no
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Cambia di forma se premuto, così da far vedere che è stato premuto
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
            disabledContainerColor = if (isHighlighted) color else Color.LightGray.copy(alpha = 0.5f),
            disabledContentColor = if (isHighlighted) Color.Black else Color.Gray
        )
    ) {
        Text(label)
    }
}

//funzione per la visualizzazione corretta dell'uscita, se sbagliato il resto della sequenza è in rosso chiaro, mentre l'errore in rosso acceso
fun seqExitHighlightedError(compseq: String, seqgen: String): String {

    //inserire uno slash / e un & per cambiare prima da rosso vivo e poi da rosso un po' più chiaro
    //così da avere sequenza dell'errore e il resto della sequenza che non è stata premuta ma bisognava premere, inoltre va salvato il punteggio
    //che a dire a verità è sicuramente computerSeq.length-1 (prendo la stringa più lunga messa prima)

    //stringa computer, passata, poi devo aggiornare la stringa, quindi chiamo sta funzione da quello che voglio passare alla scheramta successiva
    //poi da qui handle exit e siamo a posto

    //stringa inserita pulita del resto dei caratteri (spazie e virgole)
    val userClean = seqgen.replace(", ", "")
    var exitSeq = ""
    var errorIndex = -1

    // 1. Aggiungiamo i caratteri che l'utente ha premuto correttamente (in Nero)
    // Troviamo anche il punto preciso in cui le due stringhe si separano (l'errore)
    for (i in userClean.indices) {
        if (i < compseq.length && userClean[i] == compseq[i]) {
            exitSeq += userClean[i]
        } else {
            errorIndex = i
            break
        }
    }

    // Se l'utente ha commesso un errore esplicito (ha premuto un tasto sbagliato)
    if (errorIndex != -1) {
        // Se l'utente ha sbagliato prima della fine, marchiamo l'errore con lo slash / (Rosso Acceso)
        // CORRETTO: Prende il carattere errato digitato dall'utente (userClean)
        exitSeq += "/" + userClean[errorIndex]

        // Tutto il resto della sequenza rimasta la marchiamo con il simbolo & (Rosso Chiaro)
        if (errorIndex + 1 < compseq.length) {
            exitSeq += "&" + compseq.substring(errorIndex + 1)
        }
    }
    // Se l'utente ha premuto tutto giusto ma si è fermato a metà (ha cliccato End Game volontariamente)
    else if (userClean.length < compseq.length) {
        val nextCorrectIndex = userClean.length

        // Non ha inserito lettere sbagliate, quindi marchiamo come interruzione (/) la mossa che il PC si aspettava
        exitSeq += "/" + compseq[nextCorrectIndex]

        // Tutto il resto della sequenza rimasta la marchiamo con il simbolo & (Rosso Chiaro)
        if (nextCorrectIndex + 1 < compseq.length) {
            exitSeq += "&" + compseq.substring(nextCorrectIndex + 1)
        }
    }

    // ritorno della stringa con i tag speciali per cambiare colore, cambio vhe verrà fatto nella schermata della lazy column
    return exitSeq
}