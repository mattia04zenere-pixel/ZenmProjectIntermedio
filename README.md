# Simon Game - ZenmProjectIntermedio.

##  Descrizione
L'app permette di giocare ad una versione semplificata di Simon, con 3 schermate, la prima quella di benvenuto, poi schermata principale di gioco e infine schermata che visualizza lo storico partite


##  Funzionalità
nella schermata 1 è presente in alto a destra un IconButton che se cliccato mi dice quali lingue sono supportate dall'app, sono 5, le principali europee, che cambiano in base alla lingua del dispositivo.
la lingua principale è l'inglese.
ho utilizzato 6 pulsanti, colorati con il colore che rappresentano con al centro la lettera che si andrà ad aggiungere alla stringa premuta
una stringa di testo non editabile e due pulsanti cancella e fine partita.
la schermata di visualizzazione partite è formata da un pulsate in alto a sinistra per tornare indieotr, e una lazy column ch esi aggiorna ogni volta che clicco "fine partita". 
Se non ci sono partite giocate visualizza un messaggio inerente

##  Tecnologie Utilizzate
- **Kotlin**
- **Jetpack Compose**
- **Jetpack Navigation**
- **rememberSaveable**

## Info utili
ho rinominato le schermate: Schermata 1: SimonSessionScreen, schermata 2: SimonHystoryScreen. Inoltre ho agginto una schermata di benvenuto, visualizzata all'apertura dell'app

per il progetto io ho utilizzato un telefono (xiaomi redmi note 9s) con Android 12 e API 31, ho provato anche su un altro telefono (xiaomi 12) con Android 15 e API 35, e l'app funzionava correttamente in entrambi i casi

il codice è propriamente commentato e spiegato, sopratutto per quanto riguarda il componimento dell'app nelle modalità landscape e portrai e
per spiegare a che cosa servono molte funzioni presenti all'interno

