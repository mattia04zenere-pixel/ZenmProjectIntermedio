package com.example.zenmprojectintermedio

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Funzione interprete interna per convertire i tag in colori reali e aggiungere le virgole di separazione
fun decodeStringToColorsDetails(savedString: String): AnnotatedString {
    return buildAnnotatedString {
        var currentStyle: Color = Color.Black // Di base partiamo in Nero (le mosse corrette dell'utente)

        // Creiamo una stringa pulita senza i tag per sapere quanti caratteri effettivi andremo a stampare
        val cleanedSeq = savedString.replace("/", "").replace("&", "")
        var printCharCount = 0

        for (char in savedString) {
            when (char) {
                '/' -> {
                    // Quando incontra '/', la lettera successiva (l'errore) diventa Rosso Acceso
                    currentStyle = Color(0xFFFF0000)
                    continue // Salta la stampa del carattere '/' grafico
                }
                '&' -> {
                    // Quando incontra '&', tutto il resto della sequenza non premuta diventa Rosso Chiaro
                    currentStyle = Color(0xFFFF8080)
                    continue // Salta la stampa del carattere '&' grafico
                }
            }

            // Applica il colore impostato in questo momento al carattere attuale (R, G, B, ecc.)
            withStyle(style = SpanStyle(color = currentStyle, fontWeight = FontWeight.Bold)) {
                append(char)
            }

            printCharCount++

            // Se non è l'ultimo carattere effettivo della stringa, aggiungiamo il separatore in Nero
            if (printCharCount < cleanedSeq.length) {
                withStyle(style = SpanStyle(color = Color.Black, fontWeight = FontWeight.Normal)) {
                    append(", ")
                }
            }
        }
    }
}

@Composable
fun SimonGameDetailsScreen(
    sequence: String,
    onBackClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        //numero di pulsanti premuti senza errori visualizzato come primo elemento
        // Modificato per calcolare i punti reali basandosi solo su ciò che l'utente ha indovinato (prima del tag '/')
        val cleanedUserPart = sequence.replace("/", "").replace("&", "")
        //calcolo del punteggio fatto dal giocatore (prende la sequenza precedente)
        val points = if (cleanedUserPart.isEmpty()) 0 else cleanedUserPart.length - 1

        // Gestione grammaticale corretta per Punti/Punto
        val pointsText = if (points == 1) {
            "$points " + stringResource(R.string.point)
        } else {
            "$points " + stringResource(R.string.points)
        }

        Text(
            text = pointsText,
            fontSize = 80.sp,
            fontWeight = FontWeight.Bold
        )

        // Il conteggio totale dei clic visibili (inclusi errore e rimanenti del PC)
        val clickCount = cleanedUserPart.length

        //vecchia stringa rimossa, ora dice il livello del gioco cui si è arrivati
        // livello 1 è il primo, si sale a mano a mano che le stringhe diventano più lunghe
        val levelText = stringResource(R.string.level) + " " + clickCount.toString()


        /* if (clickCount == 1) {
              "$clickCount " + stringResource(R.string.clickedNum)
          } else {
              "$clickCount " + stringResource(R.string.clickedNums)
          }
  */
        Text(
            text = levelText,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Sequenza completa visualizzata in grande e senza troncamenti
        Text(
            // Modificato per passare la stringa distribuita con i 3 colori e separata da virgole
            text = decodeStringToColorsDetails(sequence),
            fontSize = 32.sp,
            lineHeight = 40.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Pulsante per tornare alla cronologia
        ExtendedFloatingActionButton(onClick = onBackClicked) {
            Text(text = stringResource(R.string.back))
        }
    }
}