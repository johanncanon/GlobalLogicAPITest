package com.johanncanon.globallogic.user_management_service.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

public class Utils {

    private Utils() {
    }

    public static final DateTimeFormatter FORMATER = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm:ss a", Locale.US);

    public static UUID generateRandomUUID() {
        return UUID.randomUUID();
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(FORMATER);
    }

}
