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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.ceil

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
        val count = ceil(sequence.length / 3.0).toInt()
        val points = count -1
        Text(
            text = "$points " + stringResource(R.string.points),
            fontSize = 80.sp,
            fontWeight = FontWeight.Bold
        )


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
            text = sequence,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Companion.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Pulsante per tornare alla cronologia
        ExtendedFloatingActionButton(onClick = onBackClicked) {
            Text(text = stringResource(R.string.back))
        }
    }
}