package BOT;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Boraso_bot extends TelegramLongPollingBot {

    // Username del bot (NON segreto)
    private static final String BOT_USERNAME = "Boraso_bot";

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

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
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            if (text.equalsIgnoreCase("/start")) {
                sendText(chatId, "Bot collegato correttamente.");
            }
        }
    }

    private void sendText(Long chatId, String text) {
        SendMessage message = new SendMessage(chatId.toString(), text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
