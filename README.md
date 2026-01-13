# BORASO Telegram Bot Finanziario

## Descrizione
Bot Telegram sviluppato in Java che fornisce dati storici
su titoli azionari tramite API esterna e memorizza le richieste
degli utenti in un database SQLite per analisi statistiche.

## Tecnologie
- Java 21
- Maven
- TelegramBots Java Library
- SQLite
- API: financialdata.net

## Setup
1. Clonare il repository
2. Creare `config/config.properties`
3. Inserire API key e Bot Token
4. Avviare il progetto con Maven

## Comandi disponibili
- /start
- /comandi
- /stock <TICKER> <GIORNI>
- /stats
- /mystats

## Database
### Tabelle
- users
- stock_requests

### Relazioni
- users → stock_requests (1:N)

## Esempi di utilizzo
![esempio](img/Screenshot.png)

## Statistiche implementate
- Numero totale richieste
- Ticker più richiesti
- Statistiche per singolo utente
