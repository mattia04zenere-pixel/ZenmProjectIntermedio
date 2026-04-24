package com.example.zenmprojectintermedio

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.text.style.TextOverflow

//funzione principale per la visualizzazione delle parrite giocate
@Composable
fun SimonHystoryScreen(onBackClicked: () -> Unit, historyList: List<String>) {
    val orientation = LocalConfiguration.current.orientation

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Pulsante per tornare indietro alla schermata 2
        Button(onClick = onBackClicked) {
            Text("< " + stringResource(R.string.back))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.games),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .then(if (orientation == Configuration.ORIENTATION_LANDSCAPE) Modifier.align(Alignment.CenterHorizontally) else Modifier)
        )

        //se non ci sono partite giocate mostro un messaggio inerente
        if (historyList.isEmpty()) {

            Text(
                text = stringResource(R.string.noGames),
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .then(if (orientation == Configuration.ORIENTATION_LANDSCAPE) Modifier.align(Alignment.CenterHorizontally) else Modifier)
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
                            .align(Alignment.CenterHorizontally)
                        else Modifier
                    ),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                itemsIndexed(historyList) { index, sequence ->
                    HistoryItem(index + 1, sequence)
                }
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
        Text(
            //il titolo della partita, in pratica la posizione che occupa all'interno
            //della lista delle partite
            text = "#$gameNumber",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 20.dp)
        )

        //colonna per la visualizzazione della sequenza di tasti premuti
        // in modalità landscape è centrale e non occupa tutto lo schermo, così da renderla più bella livello visivo
        Column(
            modifier = Modifier
                .widthIn(max = 300.dp, min = 300.dp),
            horizontalAlignment = Alignment.Start


        ) {
            //variabile count aggiunta solo per avere un riscontro grammaticale sulla stringa
            //altrimenti era brutto da leggere
            val count = ceil(sequence.length / 3.0).toInt()
            val t = if (count == 1) {
                "$count " + stringResource(R.string.clickedNum)
            } else {
                "$count " + stringResource(R.string.clickedNums)
            }

            //testo che visualizza il numero di tasti premuti e la sequenza di tasti premuti
            Text(
                text = t,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
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
