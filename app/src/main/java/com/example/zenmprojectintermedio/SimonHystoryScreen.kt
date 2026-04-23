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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.ceil

@Composable
fun SimonHystoryScreen(onBackClicked: () -> Unit, historyList: List<String>) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Pulsante per tornare indietro
        Button(onClick = onBackClicked) {
            Text("< " + stringResource(R.string.back))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.games),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (historyList.isEmpty()) {
            // Messaggio mostrato quando non ci sono partite
            Text(
                text = stringResource(R.string.noGames),
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                itemsIndexed(historyList) { index, sequence ->
                    HistoryItem(index + 1, sequence)
                }
            }
        }
    }
}

@Composable
fun HistoryItem(gameNumber: Int, sequence: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "#$gameNumber",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 20.dp)
        )

        Column {
            val count = ceil(sequence.length / 3.0).toInt()
            
            val t = if (count == 1) {
                "$count " + stringResource(R.string.clickedNum)
            } else {
                "$count " + stringResource(R.string.clickedNums)
            }

            Text(
                text = t,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = sequence,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}
