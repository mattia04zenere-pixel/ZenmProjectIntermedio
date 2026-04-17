package com.example.zenmprojectintermedio

import android.content.res.Configuration
import android.widget.Button
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp


//schermata che si presenta all'apertura dell'app
//flag: Start

//gestisco internamente l'orientamento della mia app

//usare le variabili per ricordare al sequenza di lettere premute
//come: rememberSaveable o un ViewModel

fun onDeleteClicked(){



}


// versione due con funzioni dedicate per rendere più semplice e leggibile il codice

// funzione che mi da il colore e il testo del pulsante
@Composable
fun SimonButton(label: String, color: Color, onClick: (String) -> Unit) {
    Button(
        onClick = { onClick(label) },
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(containerColor = color, contentColor = Color.Black),
        modifier = Modifier.size(100.dp)
    ) {
        Text(label)
    }
}

//funzione di prova per vedere se la cosa può funzioanre anche così
@Composable
fun SimonSessionScreen(onFinishClicked: () -> Unit) {
    val orientation = LocalConfiguration.current.orientation


    // nella prima versione ho visto che molto codice era ripetuto quindi ho cercato di minimizzarlo

    @Composable
    //creo due funzioni, una per la modalità landascape e una per quella portrait invece
    fun ColorGridPor() {
        Column {

            Row {
                SimonButton("R", Color.Red) {}
                SimonButton("G", Color.Green) { }
            }

            Row {
                SimonButton("B", Color.Blue) { }
                SimonButton("Y", Color.Yellow) { }
            }

            Row {
                SimonButton("M", Color.Magenta) { }
                SimonButton("C", Color.Cyan) { }
            }
        }
    }

    //funzione per la modalità landascape
    @Composable
    fun ColorGridLan() {
        Column {

            Row {
                SimonButton("G", Color.Green) { }
                SimonButton("Y", Color.Yellow) { }
                SimonButton("C", Color.Cyan) { }
            }

            Row {
                SimonButton("R", Color.Red) { }
                SimonButton("B", Color.Blue) { }
                SimonButton("M", Color.Magenta) { }
            }

        }
    }

    //funzione per i pulsanti cancella e fine partita
    @Composable
    fun Buttons() {

           //i due pulsanti sono sempre messi allo stesso modo, uno di fianco all'altro
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){

            Button(
                //pulsante di fine partita
                onClick = onFinishClicked
            ){
                Text(stringResource(R.string.endgame))

            }
            Button(
                //pulsante per cancellare la sequenza appena premuta
                onClick = { onDeleteClicked() }

            ){
                Text(stringResource(R.string.del))
            }

        }




    }

    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        Row(
            modifier = Modifier.fillMaxSize().padding(50.dp)
        )
        {
            Spacer(modifier = Modifier.width(50.dp))
            ColorGridLan()
            Row(

                modifier = Modifier.fillMaxSize().padding(32.dp),

                ) {

                Column(

                    horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                    TextField(
                        label = { "sequenza di pulsanti premuti" },
                        readOnly = true,
                        value = "",
                        onValueChange = { },
                        maxLines = 5
                    )
                    Buttons()

                }
            }

        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ColorGridPor()
            Column(

                modifier = Modifier.fillMaxSize().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {

                TextField(
                    label = {"sequenza di pulsanti premuti"},
                    readOnly = true,
                    value = "",
                    onValueChange = { },
                  maxLines = 5
                    )
                Buttons()


            }
        }
    }

}
