package com.example.macrosocietyapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {
    private static final SimpleDateFormat utcFormat;
    private static final SimpleDateFormat localFormat;

    static {
        utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        localFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
    }

    public static Date parseUtc(String dateStr) throws ParseException {
        return utcFormat.parse(dateStr);
    }

    public static String formatUtc(Date date) {
        return utcFormat.format(date);
    }

    public static String formatLocal(Date date) {
        return localFormat.format(date);
    }
}

