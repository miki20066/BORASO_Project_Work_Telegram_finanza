package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String apiKey = System.getenv("ITICK_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException("API key non trovata. Variabile d'ambiente mancante.");
        }

        System.out.println("API key caricata correttamente");


    }
}