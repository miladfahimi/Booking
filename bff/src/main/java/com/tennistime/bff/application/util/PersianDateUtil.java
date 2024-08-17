package com.tennistime.bff.application.util;

import com.github.mfathi91.time.PersianDate;

import java.time.LocalDate;

public class PersianDateUtil {

    public static String localDateToString(LocalDate date) {
        return date != null ? PersianDate.fromGregorian(date).toString() : "Does not filled by User";
    }

    public static LocalDate stringToLocalDate(String date) {
        return date != null ? PersianDate.parse(date).toGregorian() : null;
    }
}
