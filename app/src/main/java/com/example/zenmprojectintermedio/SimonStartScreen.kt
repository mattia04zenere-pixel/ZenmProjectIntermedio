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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@Composable
fun SimonStartScreen(onStartClicked: () -> Unit) {

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


@Composable
fun MinimalDropdownMenu() {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .padding(12.dp)
    ) {



        IconButton(onClick = { expanded = !expanded }) {


            Icon(
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
            DropdownMenuItem(
                text = { Text("English") },
                onClick = {
                    expanded = false
                    //default
                    //fare in modo di cambiare le risorse della mia app

                }
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text("Italian") },
                onClick = {
                    expanded = false
                    //cambiare lingua in italiano
                    //cambiare il percorso per dove va a prendere le risorse
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
