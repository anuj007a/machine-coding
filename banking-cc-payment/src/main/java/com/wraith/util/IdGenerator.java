package com.wraith.util;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class IdGenerator {
    private static final Set<Long> generatedCardNumbers = new HashSet<>();
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateId() {
        return UUID.randomUUID().toString();
    }

    public static String generateCardNumber(){
        long cardNumber;
        boolean isUnique;

        // Loop until a unique number is found (or a sufficient number of attempts)
        do {
            // Generate a random long (up to 19 digits generally)
            // Use bitwise AND with a mask to ensure a positive number
            cardNumber = secureRandom.nextLong() & Long.MAX_VALUE;

            // Format the long to a 16-digit string, left-padded with zeros if necessary
            String cardNumberString = String.format("%016d", cardNumber);

            // Trim to ensure exactly 16 digits if the formatted string is longer due to overflow edge cases
            if (cardNumberString.length() > 16) {
                cardNumberString = cardNumberString.substring(0, 16);
            }

            // In-memory uniqueness check (replace with database lookup)
            isUnique = generatedCardNumbers.add(Long.valueOf(cardNumberString));

        } while (!isUnique);

        return String.format("%016d", cardNumber);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            System.out.println("Generated Card Number: " + generateCardNumber());
        }
    }
}