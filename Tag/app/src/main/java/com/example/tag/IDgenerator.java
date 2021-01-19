package com.example.tag;

import java.util.Random;

public class IDgenerator {
    public static String generate() {
        String asciiUpperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String asciiLowerCase = asciiUpperCase.toLowerCase();
        String digits = "1234567890";
        String specialChars = "#@&*";
        String asciiChars = asciiUpperCase + asciiLowerCase + digits+specialChars;

        return IDgenerator.generateRandomString(asciiChars);
    }

    private static String generateRandomString(String seedChars) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        Random rand = new Random();
        while (i < 10) {
            sb.append(seedChars.charAt(rand.nextInt(seedChars.length())));
            i++;
        }
        return sb.toString();
    }
}
