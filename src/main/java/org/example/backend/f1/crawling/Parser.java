package org.example.backend.f1.crawling;

public final class Parser {
    private Parser() {}

    public static Integer parseAsInteger(String input) {
        if (input == null || input.isBlank()) {
            return null;
        }
        try {
            // "1st" -> "1", "10위" -> "10"
            String numberOnly = input.replaceAll("[^0-9]", "");

            if (numberOnly.isEmpty()) {
                return null;
            }
            return Integer.parseInt(numberOnly);
        } catch (NumberFormatException e) {
            System.err.println("정수 파싱 실패: " + input);
            return null;
        }
    }

    public static Double parseAsDouble(String input) {
        if (input == null || input.isBlank()) {
            return null;
        }
        try {
            return Double.parseDouble(input.trim());
        } catch (NumberFormatException e) {
            System.err.println("실수 파싱 실패: " + input);
            return null;
        }
    }

}
