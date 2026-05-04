package com.example.zenmprojectintermedio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import  androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource


//schermata aggiunta per evitare di entrare subito nel gioco, in alto a sinistra c'è un icona
// che contiene un mondo, per indicare le lingue, cliccandoci sopra si visualizzano tutte le lingue supportate, che sono 5
// ho cercato di fare in modo che cliccando sulla scritta della lingua cambiasse, ma non sono riuscito


//le lingue cambiano in base alla lingua del dispositivo, con la lingua di default inglese
@Composable
fun SimonGameDetailsScreen(onStartClicked: () -> Unit) {

Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.End

) {

    MinimalDropdownMenu()

}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.welcome_text),
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onStartClicked) {
            Text(text = stringResource(R.string.start))
        }


    }
}


//drop down menu per la visualizzazione delle lingue supportate
@Composable
fun MinimalDropdownMenu() {

    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .padding(12.dp)
    ) {


      //icona per cambiare lingua, un'immagine di un mondo all'interno della cartella "drawable"
        // cliccandola si visualizzano le lingue supportate
        IconButton(onClick = { expanded = !expanded }) {


            Icon(
                // risorsa dove prende l'immagine
                painter = painterResource(R.drawable.mondoicon),
                    contentDescription = "Change Language",
                     tint = Color.Unspecified
                    )

        }


            //dropdown menu inizio
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }


        ) {
            // testo del titolo del dropdown che mi da le lingue supportate (stringa che cambia in base alla lingua del dispositivo)
            Text(
                text = stringResource(R.string.supplang),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary


            )


            //in questo caso ill nome della lingua supportata è scritto nella stessa lingua supportata, ad esempio se ho la lingua tedesco, la stringa
            // che mi dice che l'italiano è supportato sarà scritta in italiano, quindi ho scritto
            // direttamente delle stringhe con il nome della lignua e non ho usato un R.string

            //lingua di default inglese
            DropdownMenuItem(

                text = { Text("English") },
                onClick = {
                    expanded = false

                }
            )
            // solo per estetica ho aggiunto un horizontal divider per separare ogni lingua
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text("Italian") },
                onClick = {

                    expanded = false

                }
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text("Français") },
                onClick = { expanded = false

                }
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text("Español") },
                onClick = { expanded = false


                }
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text("Deutsch") },
                onClick = { expanded = false

                }
            )
        }
    }
}
