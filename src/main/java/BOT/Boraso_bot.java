package BOT;

import dao.UserDao;
import dao.StockRequestDao;
import service.StockService;
import model.StockResult;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

public class Boraso_bot extends TelegramLongPollingBot {

    // Username del bot (NON segreto)
    private static final String BOT_USERNAME = "Boraso_bot";

    // DAO e servizi
    private final StockService stockService = new StockService();
    private final UserDao userDao = new UserDao();
    private final StockRequestDao stockRequestDao = new StockRequestDao();



    // Mappa per richieste stock in attesa
    private final Map<Long, String> pendingStockRequests = new HashMap<>();

    // Costruttore


    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        String token = System.getenv("token_BOT_boraso");
        if (token == null || token.isBlank()) {
            throw new RuntimeException("TELEGRAM_BOT_TOKEN non impostato come variabile di sistema");
        }
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            System.out.println("DEBUG: testo ricevuto: " + text);

            // Salvataggio utente in SQLite
            userDao.saveUser(
                    chatId,
                    update.getMessage().getFrom().getUserName(),
                    update.getMessage().getFrom().getFirstName()
            );

            // /start
            if (text.equalsIgnoreCase("/start")) {
                sendText(chatId, "Bot collegato correttamente.");
                return;
            }

            // Comando /stats
            if (text.equalsIgnoreCase("/stats")) {
                String statsMessage = stockService.getGeneralStats(); // o metti la logica DAO
                sendText(chatId, statsMessage);
                return;
            }

// Comando /mystats
            if (text.equalsIgnoreCase("/mystats")) {
                String myStatsMessage = stockService.getUserStats(chatId); // logica DAO per utente
                sendText(chatId, myStatsMessage);
                return;
            }


            // /comandi
            if (text.equalsIgnoreCase("/comandi")) {
                sendText(chatId,
                        "/start - attiva il bot\n" +
                                "/stock <TICKER> <GIORNI> - richiede dati storici del titolo (max 10 giorni)\n" +
                                "/stats - statistiche generali\n" +
                                "/mystats - statistiche personali"
                );
                return;
            }

            // /stock TICKER [GIORNI]
            if (text.toLowerCase().startsWith("/stock")) {
                String[] parts = text.split(" ");
                if (parts.length >= 2) {
                    String ticker = parts[1].toUpperCase();
                    int giorni = 1; // default 1 giorno
                    if (parts.length >= 3) {
                        try {
                            giorni = Math.min(Integer.parseInt(parts[2]), 10);
                        } catch (NumberFormatException e) {
                            sendText(chatId, "Numero di giorni non valido. Uso 1 giorno.");
                        }
                    }

                    System.out.println("DEBUG: handleStock chiamato per " + ticker);

                    // Salvataggio richiesta in SQLite
                    stockRequestDao.saveRequest(chatId, ticker, giorni);

                    // Chiamata al servizio stock
                    StockResult result = stockService.getStockResult(ticker, giorni);

                    if (result != null) {
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
                    } else {
                        sendText(chatId, "Errore nel recupero dei dati per " + ticker);
                    }

                } else {
                    sendText(chatId, "Formato comando non corretto. Usa: /stock <TICKER> <GIORNI>");
                }
                return;
            }

            // Comando non riconosciuto
            sendText(chatId, "Comando non riconosciuto. Usa /comandi per la lista dei comandi disponibili.");
        }
    }

    // Metodo helper per inviare testo
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
