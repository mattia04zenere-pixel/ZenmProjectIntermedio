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


//schermata che si presenta all'apertura dell'app
//flag: Start

//gestisco internamente l'orientamento della mia app

//usare le variabili per ricordare al sequenza di lettere premute
//come: rememberSaveable o un ViewModel

// versione due con funzioni dedicate per rendere più semplice e leggibile il codice

// funzione che mi da il colore e il testo del pulsante


//funzione di prova per vedere se la cosa può funzioanre anche così
@Composable
fun SimonSessionScreen(onFinishClicked: (String) -> Unit) {

    //creazione della variabile per essere memorizzata
    var seqGen by rememberSaveable { mutableStateOf("") }

    //variabile per capire l'orientamento del dispositivo
    val orientation = LocalConfiguration.current.orientation

    @Composable
    //creo due funzioni, una per la modalità landascape e una per quella portrait
    fun ColorGridPor() {

        Column (

            modifier = Modifier.padding(top = 120.dp)

        ){


            Row {
                SimonButton("R", Color.Red) {}
                SimonButton("G", Color.Green) {}
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
        Column (modifier = Modifier.padding(start=50.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ){

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
            verticalAlignment = Alignment.CenterVertically) {

            Button(
                //pulsante di fine partita
                onClick = { onFinishClicked(seqGen) }
            ) {
                Text(stringResource(R.string.endgame))

            }
            Button(
                //pulsante per cancellare la sequenza appena premuta
                onClick = { seqGen = "" }

            ) {
                Text(stringResource(R.string.del))
            }

        }

    }


    // if per determinare l'orientamento del dispositivo
    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        Row(
            modifier = Modifier.fillMaxSize().padding(32.dp),

            verticalAlignment = Alignment.CenterVertically

        )
        {

            //ho deciso di fare una matrice 3x2 in modalità lendscape in modo che fosse più bello da vedere
            //Spacer(modifier = Modifier.width(50.dp))
            ColorGridLan()

            Row(

                modifier = Modifier.fillMaxSize().padding(32.dp),
                verticalAlignment = Alignment.CenterVertically

                ) {

                Column{
                    TextField(
                        placeholder = { Text(text = stringResource(R.string.clickedSeq)) },
                        readOnly = true,
                        value = "",
                        onValueChange = { },
                        maxLines = 5
                    )

                    Buttons()

                }
            }

        }
        //condizione di default(il telefono è in modalità portrait
    } else {

        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {

            ColorGridPor()
            Spacer(modifier = Modifier.height(32.dp))
            Column(

                horizontalAlignment = Alignment.CenterHorizontally,

                ) {

                TextField(

                    placeholder = { Text(text = stringResource(R.string.clickedSeq)) },
                    readOnly = true,
                    value = "",
                    onValueChange = { },
                    shape = RoundedCornerShape(12.dp),
                  maxLines = 5
                    )
                Buttons()

            }
        }
    }
//fiine della funzione principale della classe
}


//funzione contenitore dei pulsanti che andrò ad utilizzare, visto che sono 6 il codice veniva troppo
//lungo e con una funzione dedicata è più leggibile
@Composable
fun SimonButton(label: String, color: Color, onClick: (String) -> Unit, ) {
    Button(
        onClick = {

         onClick(label)

                  },
        //shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(containerColor = color, contentColor = Color.Black),
        modifier = Modifier
            .padding(4.dp)
            .width(120.dp)
            .height(80.dp),
        shape = RoundedCornerShape(12.dp),


    ) {
        Text(label)
    }
}
