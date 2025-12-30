package BOT;

import controller.CommandController;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Boraso_bot extends TelegramLongPollingBot {

    private static final String BOT_USERNAME = "Boraso_bot";

    private final CommandController controller = new CommandController();

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        String token = System.getenv("token_BOT_boraso");

        if (token == null || token.isBlank()) {
            throw new RuntimeException("token_BOT_boraso non impostato");
        }

        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        controller.handle(update, this);
    }
}
