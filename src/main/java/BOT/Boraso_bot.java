package BOT;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class Boraso_bot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return System.getenv("Boraso_bot");
    }

    @Override
    public String getBotToken() {
        return System.getenv("token_BOT_Boraso");
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
