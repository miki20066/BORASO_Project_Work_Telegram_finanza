package org.example;

import api.ApiClient;


import BOT.Boraso_bot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {

    public static void main(String[] args) {

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Boraso_bot());

            System.out.println("Bot Telegram avviato correttamente.");

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
