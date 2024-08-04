package com.ahuynh.muzi_music_api.utils;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {

    public static String convertInstantToTime(Instant instant) {
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return zonedDateTime.format(formatter);
    }

    public static String getTime(Instant inputInstant) {
        Instant now = Instant.now();
        Duration duration = Duration.between(inputInstant, now);

        long seconds = duration.getSeconds();
        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();

        if (seconds < 59) {
            return "Now";
        } else if (minutes < 59) {
            return minutes + " minutes ago";
        } else if (hours < 24) {
            return hours + " hours ago";
        } else {
            return convertInstantToTime(inputInstant);
        }
    }



}
