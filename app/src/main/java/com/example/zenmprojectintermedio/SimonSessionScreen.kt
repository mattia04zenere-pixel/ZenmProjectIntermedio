// codice per prendere le lettere premute dai tasti coi colori
package com.example.zenmprojectintermedio

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun SimonSessionScreen(onFinishClicked: (String) -> Unit) {

    // Questa variabile memorizza la sequenza di lettere premute
    var seqGen by rememberSaveable { mutableStateOf("") }
    //questa variabile mi serve per capire l'orientamento del dispositivio
    //come visto in Orientation
    val orientation = LocalConfiguration.current.orientation

    @Composable
    fun ColorGridPor() {
        Column(modifier = Modifier.padding(top = 120.dp)) {
            Row {
                SimonButton("R", Color.Red) { seqGen += it }
                SimonButton("G", Color.Green) { seqGen += it }
            }
            Row {
                SimonButton("B", Color.Blue) { seqGen += it }
                SimonButton("Y", Color.Yellow) { seqGen += it }
            }
            Row {
                SimonButton("M", Color.Magenta) { seqGen += it }
                SimonButton("C", Color.Cyan) { seqGen += it }
            }
        }
    }

    @Composable
    fun ColorGridLan() {
        Column(modifier = Modifier.padding(start = 50.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row {
                SimonButton("G", Color.Green) { seqGen += it }
                SimonButton("Y", Color.Yellow) { seqGen += it }
                SimonButton("C", Color.Cyan) { seqGen += it }
            }
            Row {
                SimonButton("R", Color.Red) { seqGen += it }
                SimonButton("B", Color.Blue) { seqGen += it }
                SimonButton("M", Color.Magenta) { seqGen += it }
            }
        }
    }

    //dato che i pulsanti per cancellare e finire la partita sono due e sempre messi allo stesso moodo,
    //in modo analogo a quanto fatto per i pulsanti del gioco ho creato una funzione per rendere il codice più leggibile e corto

    @Composable
    fun Buttons() {
        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { onFinishClicked(seqGen) }) {
                Text(stringResource(R.string.endgame))
            }
            Button(onClick = { seqGen = "" }) {
                Text(stringResource(R.string.del))
            }
        }
    }


    //if che mi cambia l'orientamento, purtroppo ho dovuto creare due griglie per i pulsanti, che altrimenti risultavano uguali
    //e in modalità landscape mi sembravano brutti da vedere dei pulsanti che fossero 2x3 invece che 3x2

    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        Row(modifier = Modifier.fillMaxSize().padding(32.dp), verticalAlignment = Alignment.CenterVertically) {
            ColorGridLan()
            Row(modifier = Modifier.fillMaxSize().padding(32.dp), verticalAlignment = Alignment.CenterVertically) {
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
    } else {
        //else per la modalità portrait dell'app
        Column(modifier = Modifier.fillMaxSize().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
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
    }
}

// funzione che popola i pulsanti della griglia, dato che sono 6 tutti uguali, richiamo 6 volte questa funzione invece di
// creare 6 pulsanti che facciano la stesa cosa, così il codice risulta molto più corto
@Composable
fun SimonButton(label: String, color: Color, onClick: (String) -> Unit) {
    Button(
        onClick = { onClick(label) },
        colors = ButtonDefaults.buttonColors(containerColor = color, contentColor = Color.Black),
        modifier = Modifier.padding(4.dp).width(120.dp).height(80.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        Text(label)
    }
}
