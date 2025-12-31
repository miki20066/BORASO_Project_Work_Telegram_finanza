package controller;

import BOT.Boraso_bot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import service.StockService;

public class CommandController {

    public void handle(Update update, Boraso_bot bot) {

        String text = update.getMessage().getText().trim();
        Long chatId = update.getMessage().getChatId();


        System.out.println("DEBUG: sono nel controller");
        System.out.println("DEBUG testo: " + text);

        if (text.equalsIgnoreCase("/start")) {
            send(bot, chatId, "Benvenuto nel bot finanziario.");
            return;
        }

        if (text.startsWith("/stock")) {
            handleStock(bot, chatId, text);
            return;
        }

        send(bot, chatId, "Comando non riconosciuto.");
    }

    private final StockService stockService = new StockService();

    private void handleStock(Boraso_bot bot, Long chatId, String text) {

        System.out.println("DEBUG: handleStock chiamato");
        String[] parts = text.split(" ");

        if (parts.length != 2) {
            send(bot, chatId, "Uso corretto: /stock AAPL");
            return;
        }

        String ticker = parts[1].toUpperCase();
        String response = stockService.getStockSummary(ticker);

        send(bot, chatId, response);
    }


    private void send(Boraso_bot bot, Long chatId, String msg) {
        try {
            bot.execute(new SendMessage(chatId.toString(), msg));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
