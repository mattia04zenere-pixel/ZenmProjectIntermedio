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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun SimonSessionScreen(onFinishClicked: (String) -> Unit, onBackClicked: () -> Unit) {

    // Questa variabile memorizza la sequenza di lettere premute
    var seqGen by rememberSaveable { mutableStateOf("") }

    //questa variabile mi serve per capire l'orientamento del dispositivio
    //come visto in Orientation
    val orientation = LocalConfiguration.current.orientation


    //funzione di aggiornamento della stringa, che viene richiamata da ogni pulsante, non è composable dato che
    // si possono fare chiamate composable solo da funzioni composable
    //aggiunta questa funzione per evitare di scrivere 12 volte lo stesso if
    fun updateSeqGen(buttonLabel: String) {

        if (seqGen.length == 0)
            seqGen = buttonLabel
        else
            seqGen = seqGen + ", " + buttonLabel
    }

    //grligia dei pulsanti per la modalità portrait
    @Composable
    fun ColorGridPor() {
        Column(modifier = Modifier.padding(top = 120.dp)) {
            Row {
                SimonButton("R", Color.Red) { updateSeqGen(it) }
                SimonButton("G", Color.Green) { updateSeqGen(it) }
            }
            Row {
                SimonButton("B", Color.Blue) { updateSeqGen(it) }
                SimonButton("Y", Color.Yellow) { updateSeqGen(it) }
            }
            Row {
                SimonButton("M", Color.Magenta) { updateSeqGen(it) }
                SimonButton("C", Color.Cyan) { updateSeqGen(it) }
            }
        }
    }

    //griglia dei pulsanti per la modalità landscape
    @Composable
    fun ColorGridLan() {
        Column(
            modifier = Modifier.padding(start = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                SimonButton("G", Color.Green) { updateSeqGen(it) }
                SimonButton("Y", Color.Yellow) { updateSeqGen(it) }
                SimonButton("C", Color.Cyan) { updateSeqGen(it) }
            }
            Row {
                SimonButton("R", Color.Red) { updateSeqGen(it) }
                SimonButton("B", Color.Blue) { updateSeqGen(it) }
                SimonButton("M", Color.Magenta) { updateSeqGen(it) }
            }
        }
    }

    //dato che i pulsanti per cancellare e finire la partita sono due e sempre messi allo stesso moodo,
    //in modo analogo a quanto fatto per i pulsanti del gioco ho creato una funzione per rendere il codice più leggibile e corto

    @Composable
    fun Buttons() {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cambiato in ExtendedFloatingActionButton per coerenza grafica
            ExtendedFloatingActionButton(
                onClick = {
                    //passo alla schermata successiva la seqGen, e subito dopo la cancello
                    onFinishClicked(seqGen)
                    seqGen = ""
                },
                modifier = Modifier.padding(4.dp)
            ) {
                Text(stringResource(R.string.endgame))
            }
            //pulsante cancella che azzera la stringa della sequenza
            ExtendedFloatingActionButton(
                onClick = { seqGen = "" },
                modifier = Modifier.padding(4.dp)
            ) {
                Text(stringResource(R.string.del))
            }
        }
    }


    //if che mi cambia l'orientamento, purtroppo ho dovuto creare due griglie per i pulsanti, che altrimenti risultavano uguali
    //e in modalità landscape mi sembravano brutti da vedere dei pulsanti che fossero 2x3 invece che 3x2
    // ho mantenuto però l'ordine dei colori, sono praticamente li stessi ma girati

    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

        Box(modifier = Modifier.fillMaxSize()) {
            // Pulsante per tornare alla cronologia
            ExtendedFloatingActionButton(onClick = onBackClicked,
                modifier = Modifier.align(Alignment.TopStart).padding(top = 20.dp, start = 20.dp)
            ) {
                Text(text = "< "+stringResource(R.string.back))
            }


            Row(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ColorGridLan()
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
                        Buttons()
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


                ColorGridPor()
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
                    Buttons()
                }
            }

            // Pulsante per tornare alla cronologia
            ExtendedFloatingActionButton(onClick = onBackClicked,
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
fun SimonButton(label: String, color: Color, onClick: (String) -> Unit) {
    // Sorgente di interazione per rilevare quando il pulsante viene premuto
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // Forma che cambia solo durante la pressione (diventa rotondo quando premuto)
    val currentShape = if (isPressed) CircleShape else RoundedCornerShape(12.dp)

    // Cambiato in FloatingActionButton per un effetto grafico più moderno
    FloatingActionButton(
        onClick = { onClick(label) },
        interactionSource = interactionSource,
        containerColor = color,
        contentColor = Color.Black,
        modifier = Modifier.padding(4.dp).width(120.dp).height(80.dp),
        shape = currentShape,
    ) {
        Text(label)
    }
}
