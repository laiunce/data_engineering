package com.dna.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cristian Laiun

 */

public class DateRange extends Range<LocalDate> {

    private static final long serialVersionUID = -1L;

    public DateRange(LocalDate from, LocalDate to) {
        super(from, to);
    }

    public List<LocalDate> getAllDates() {
        return getAllDates(from(), to());
    }

    public List<LocalDate> getDifference(Range<LocalDate> otherRange) {
        List<LocalDate> dates = new ArrayList<>();
        for (LocalDate dayDate : this.getAllDates()) {
            if (otherRange == null || dayDate.isBefore(otherRange.from()) || dayDate.isAfter(otherRange.to())) {
                dates.add(dayDate);
            }
        }
        return dates;
    }


    public static List<LocalDate> getAllDates(LocalDate from, LocalDate to) {
        List<LocalDate> dates = new ArrayList<>();
        for (LocalDate d = from; !d.isAfter(to); d = d.plusDays(1)) {
            dates.add(d);
        }
        return dates;
    }


}
