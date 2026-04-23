package com.example.zenmprojectintermedio

import android.R.attr.text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn


//richiamata dalla session screen di gioco, quindi devo avere nel main activity un metodo che mi richiami
//flag: Hystory

//gestisco internamente l'orientamento della mia app

@Composable
fun SimonHystoryScreen(onBackClicked: () -> Unit, seqGen: String ) {


    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        //pulsante per tornare indietro
        Button(onBackClicked,

            ) {

            Text("< "+ stringResource(R.string.back))
        }



        LazyColumn(modifier = Modifier.fillMaxSize()) {



            //visualizzare tutte le partite fatte fino ad adesso





        }





    }



//pulsante per tornare indietro (circolare e con un simbolo per tornare idnietro semplice,
    //vorrei fosse tondo e con una scritta di fianco per far capire a che cosa serve



    //due cosi di testo, o anche solo uno volendo uno ch edice il numero di rettangoli premuti mentre l'altro mi da

    //usare le liste dinamiche viste a lezione, recyclerview ecc, ma gemini dice di usare lazycolumn (mai vista a lezione)

    //guardare la app del prof per capire ocme usare lazy column





}


