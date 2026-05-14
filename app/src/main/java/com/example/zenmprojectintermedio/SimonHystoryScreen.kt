package com.example.zenmprojectintermedio

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.ceil
import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.ui.text.style.TextOverflow

//funzione principale per la visualizzazione delle parrite giocate
@Composable
fun SimonHystoryScreen(
    onBackClicked: () -> Unit,
    historyList: List<String>,
    onItemClicked: (Int) -> Unit // Aggiunta per gestire il click e passare l'indice
) {
    val orientation = LocalConfiguration.current.orientation

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        //Spacer(modifier = Modifier.height(16.dp))

        //Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.games)+":",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp)
                .then(if (orientation == Configuration.ORIENTATION_LANDSCAPE) Modifier.align(Alignment.CenterHorizontally) else Modifier)
        )

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {

            //se non ci sono partite giocate mostro un messaggio inerente
            if (historyList.isEmpty()) {

                Text(
                    text = stringResource(R.string.noGames),
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier
                       .align(Alignment.TopCenter)
                        .padding(top = 16.dp)
                        .then(if (orientation == Configuration.ORIENTATION_LANDSCAPE) Modifier.align(Alignment.TopCenter) else Modifier)
                )
            } else {
                //lazycolumn per la visualizzazione delle partite giocate,
                //se non si premono tasti si visualizzano le partite giocate fino a quel momento oppure il messaggio
                // che non sono ancora state giocate delle partite
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()


                        .then(
                            if (orientation == Configuration.ORIENTATION_LANDSCAPE)
                                Modifier
                                    .widthIn(max = 600.dp)
                                    .align(Alignment.TopCenter)
                            else Modifier
                        ),
                    contentPadding = PaddingValues(bottom = 70.dp)
                ) {
                    itemsIndexed(historyList) { index, sequence ->
                        // Avvolgiamo l'elemento in un Box o aggiungiamo il clickable al Modifier di HistoryItem
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // Usiamo la lambda passata dalla MainActivity che gestisce il rememberSaveable
                                onItemClicked(index)
                            }
                        ) {
                            // Passiamo i dati al tuo componente esistente
                            HistoryItem( index + 1, sequence)
                        }
                    }
                }
            }

            // Pulsante per tornare indietro alla schermata 1
            ExtendedFloatingActionButton(
                onClick = onBackClicked,
                //if per deerminare dove mettere il floating button
                //if(orientation == Configuration.ORIENTATION_LANDSCAPE){}


                modifier = Modifier
                    .align(Alignment.BottomCenter)

                    .padding(bottom = if(orientation == Configuration.ORIENTATION_LANDSCAPE){ 50.dp}else{  200.dp})
            ) {
                Text(stringResource(R.string.play),
                    fontSize = 16.sp)
            }


        }




    }
}

//funzione per la visualizzazione delle partite giocate fino a quel momento
@Composable
fun HistoryItem(gameNumber: Int, sequence: String) {
    val orientation = LocalConfiguration.current.orientation

    //ho aggiunto la orientation perchè altrimenti la lazy column veniva visualizzata tutta a sinistra
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement =
            if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            androidx.compose.foundation.layout.Arrangement.Center
        else
            androidx.compose.foundation.layout.Arrangement.Start

    ) {

        //colonna per la visualizzazione della sequenza di tasti premuti
        // in modalità landscape è centrale e non occupa tutto lo schermo, così da renderla più bella livello visivo
        Column(
            modifier = Modifier.widthIn(max = 300.dp, min = 150.dp),

            horizontalAlignment = Alignment.Start

        ) {
            //calcolo in base alla lunghezza della stringa i punti fatti dal giocatore
            //altro non sono che la lunghezza totale di tasti premuti -1
            val count = ceil(sequence.length / 3.0).toInt() -1
            Row {
                //testo che visualizza il numero di tasti premuti e la sequenza di tasti premuti
                Text(
                    text = count.toString(),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium,
                    modifier= Modifier.padding(start = 20.dp)
                        .widthIn(max = 40.dp ,min=40.dp)
                )

                Spacer(modifier = Modifier.padding(16.dp))
                Text(

                    //testo che visualizza la sequenza di tasti premuti, con troncamento a dure righe al massimo
                    text = sequence,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

