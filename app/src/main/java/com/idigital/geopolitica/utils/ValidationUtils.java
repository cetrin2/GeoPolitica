package com.idigital.geopolitica.utils;

import android.util.Patterns;

import java.util.regex.Pattern;

public class ValidationUtils {

    private static final Pattern CADASTRAL_PATTERN =
            Pattern.compile("^\\d{2}:\\d{2}:\\d{6,7}:\\d+$");

    public static boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^\\+?[0-9]{10,15}$");
    }

    public static boolean isValidCadastralNumber(String number) {
        return number != null && CADASTRAL_PATTERN.matcher(number).matches();
    }

    public static String formatCadastralNumber(String input) {
        if (input == null) return "";

        // Remove all non-digit characters
        String digits = input.replaceAll("[^0-9]", "");

        // Format as XX:XX:XXXXXX:XXX
        StringBuilder formatted = new StringBuilder();

        if (digits.length() > 0) {
            formatted.append(digits.substring(0, Math.min(2, digits.length())));
        }
        if (digits.length() > 2) {
            formatted.append(":").append(digits.substring(2, Math.min(4, digits.length())));
        }
        if (digits.length() > 4) {
            formatted.append(":").append(digits.substring(4, Math.min(10, digits.length())));
        }
        if (digits.length() > 10) {
            formatted.append(":").append(digits.substring(10));
        }

        return formatted.toString();
    }
}