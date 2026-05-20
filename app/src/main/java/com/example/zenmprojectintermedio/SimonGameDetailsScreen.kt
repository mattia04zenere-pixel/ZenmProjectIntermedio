package com.example.zenmprojectintermedio

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
        var stileAttuale: Color = Color.Black // Di base partiamo in Nero (le mosse corrette dell'utente)

        // Creiamo una stringa pulita senza i tag per sapere quanti caratteri effettivi andremo a stampare
        val stringaSenzaTag = savedString.replace("/", "").replace("&", "")
        var contatoreCaratteriStampati = 0

        for (char in savedString) {
            when (char) {
                '/' -> {
                    // Quando incontra '/', la lettera successiva (l'errore) diventa Rosso Acceso
                    stileAttuale = Color(0xFFFF0000)
                    continue // Salta la stampa del carattere '/' grafico
                }
                '&' -> {
                    // Quando incontra '&', tutto il resto della sequenza non premuta diventa Rosso Chiaro
                    stileAttuale = Color(0xFFFF8080)
                    continue // Salta la stampa del carattere '&' grafico
                }
            }

            // Applica il colore impostato in questo momento al carattere attuale (R, G, B, ecc.)
            withStyle(style = SpanStyle(color = stileAttuale, fontWeight = FontWeight.Bold)) {
                append(char)
            }

            contatoreCaratteriStampati++

            // Se non è l'ultimo carattere effettivo della stringa, aggiungiamo il separatore in Nero
            if (contatoreCaratteriStampati < stringaSenzaTag.length) {
                withStyle(style = SpanStyle(color = Color.Black, fontWeight = FontWeight.Normal)) {
                    append(", ")
                }
            }
        }
    }
}

@Composable
fun SimonGameDetailsScreen(
    gameNumber: Int,
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
        val cleanUserPart = sequence.substringBefore('/')
        val points = if (cleanUserPart.isEmpty()) 0 else cleanUserPart.length

        Text(
            text = "$points " + stringResource(R.string.points),
            fontSize = 80.sp,
            fontWeight = FontWeight.Bold
        )

        // Il conteggio totale dei clic visibili (inclusi errore e rimanenti del PC)
        val stringaSenzaTag = sequence.replace("/", "").replace("&", "")
        val count = stringaSenzaTag.length

        val textCount = if (count == 1) {
            "$count " + stringResource(R.string.clickedNum)
        } else {
            "$count " + stringResource(R.string.clickedNums)
        }

        Text(
            text = textCount,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.Companion.height(32.dp))

        // Sequenza completa visualizzata in grande e senza troncamenti
        Text(
            // Modificato per passare la stringa elaborata con i 3 colori e separata da virgole
            text = decodeStringToColorsDetails(sequence),
            fontSize = 32.sp,
            lineHeight = 40.sp,
            textAlign = TextAlign.Companion.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Pulsante per tornare alla cronologia
        ExtendedFloatingActionButton(onClick = onBackClicked) {
            Text(text = stringResource(R.string.back))
        }
    }
}