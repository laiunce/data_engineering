package com.dna.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import com.dna.app.config.DnaAppConstants;

/**
 * @author Cristian Laiun

 */
public class DateUtil {

    protected DateUtil() {
    }

    private static String getSeason(int month) {
        // Feb - July = Spring
        if (month >= 2 && month <= 7) {
            return DnaAppConstants.SEASON_SPRING;
        } else {
            return DnaAppConstants.SEASON_FALL;
        }
    }

    public static String getSeason(LocalDate date) {
        // use the NrfMonthWeek class to find the "week month"
        // Feb (week 1) - July (week 4) will always be Spring
        return getSeason(NrfMonthWeek.getWeek(date).getYearMonth().getMonthValue());
    }

    public static LocalDate getStartOfWeek(LocalDate date) {
        // find the start of the week for this date
        int dayOfWeek = date.getDayOfWeek().getValue();
        if (dayOfWeek == 7) {
            dayOfWeek = 0; // sunday needs no adjustment
        }
        return date.minusDays(dayOfWeek);
    }

    public static int getFiscalYear(LocalDate date) {
        NrfMonthWeek weekMonth = NrfMonthWeek.getWeek(date);
        if (weekMonth.getYearMonth().getMonthValue() == 1) {
            return weekMonth.getYearMonth().getYear() - 1;
        } else {
            return weekMonth.getYearMonth().getYear();
        }
    }

    public static LocalDate getLocalDate(String value, DateTimeFormatter dateTimeFormatter) {
        if (StringUtil.isEmpty(value)) return null;
        value = value.toLowerCase();

        LocalDate localDate = null;

        if (value.equals("today")) {
            localDate = LocalDate.now();
        } else if (value.equals("yesterday")) {
            localDate = LocalDate.now().minus(1, ChronoUnit.DAYS);
        } else if (value.endsWith(" days ago") || value.endsWith(" day ago")) {
            Long days = Long.parseLong(value.substring(0, value.indexOf(" ")));
            localDate = LocalDate.now().minus(days, ChronoUnit.DAYS);
        } else {
            localDate = LocalDate.parse(value, dateTimeFormatter);
        }

        return localDate;
    }

}
