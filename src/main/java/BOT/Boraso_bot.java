package BOT;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import service.StockService;
import model.StockResult;
import service.StockService;

import java.util.HashMap;
import java.util.Map;

public class Boraso_bot extends TelegramLongPollingBot {

    // Username del bot (NON segreto)
    private static final String BOT_USERNAME = "Boraso_bot";

    // Token da variabile di sistema
    @Override
    public String getBotToken() {
        String token = System.getenv("token_BOT_boraso");
        if (token == null || token.isBlank()) {
            throw new RuntimeException(
                    "TELEGRAM_BOT_TOKEN non impostato come variabile di sistema"
            );
        }
        return token;
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    private final StockService stockService = new StockService();

    // Mappa per tracciare richieste stock in attesa di numero di giorni
    private final Map<Long, String> pendingStockRequests = new HashMap<>();

    @Override
    public void onUpdateReceived(Update update) {

        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        String text = update.getMessage().getText().trim();
        Long chatId = update.getMessage().getChatId();

        // ---------- LOGICA COMANDI ----------

        //start
        if (text.equalsIgnoreCase("/start")) {
            sendText(chatId, "Bot collegato correttamente. Usa /comandi per vedere tutti i comandi disponibili.");
            return;
        }

        //comandi
        if (text.equalsIgnoreCase("/comandi")) {
            String comandi = """
                    Comandi disponibili:
                    /start - avvia il bot
                    /stock NOME - richiede info sulle azioni
                    /comandi - mostra tutti i comandi
                    """;
            sendText(chatId, comandi);
            return;
        }

        // stock
        if (text.startsWith("/stock")) {
            String[] parts = text.split(" ");
            if (parts.length < 2) {
                sendText(chatId, "Devi inserire il ticker es. /stock MSFT");
                return;
            }
            String ticker = parts[1].toUpperCase();
            pendingStockRequests.put(chatId, ticker);
            sendText(chatId, "Quanti giorni vuoi vedere (max 10)?");
            return;
        }

        // ---------- RISPOSTA NUMERO GIORNI ----------

        if (pendingStockRequests.containsKey(chatId)) {
            String ticker = pendingStockRequests.get(chatId);
            try {
                int giorni = Integer.parseInt(text);
                if (giorni < 1 || giorni > 10) {
                    sendText(chatId, "Inserisci un numero valido tra 1 e 10.");
                    return;
                }

                StockResult result = stockService.getStockResult(ticker, giorni);

                sendText(chatId, result.message);

                if (result.chart != null) {
                    SendPhoto photo = new SendPhoto();
                    photo.setChatId(chatId.toString());
                    photo.setPhoto(new InputFile(result.chart));
                    try {
                        execute(photo);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }


            } catch (NumberFormatException e) {
                sendText(chatId, "Inserisci un numero valido tra 1 e 10.");
            }

            // Rimuovo dalla mappa dopo aver processato la richiesta
            pendingStockRequests.remove(chatId);
        }
    }

    // Metodo ausiliario per inviare testo
    private void sendText(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
