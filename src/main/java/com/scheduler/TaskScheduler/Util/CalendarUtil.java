package com.scheduler.TaskScheduler.Util;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class CalendarUtil {
    public static LocalDate[][] calendarByLocalDate(LocalDate localDate) {
        int lastDayOfMonth = localDate.plusMonths(1).minusDays(1).getDayOfMonth();
        int daysInFirstWeek = 8-localDate.getDayOfWeek().getValue();
        int rows = 1 + (int)Math.ceil((lastDayOfMonth - daysInFirstWeek)/7.0);

        LocalDate[][] calendar = new LocalDate[rows][7];
        int month = localDate.getMonthValue();

        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        int weekDayValue = dayOfWeek.getValue();
        int row = 0, column = 0;
        for (int i = 1; i < weekDayValue; ++i) {
            calendar[row][column] = null;
            ++column;
        }

        while (localDate.getMonthValue() == month) {
            calendar[row][column] = localDate;

            localDate = localDate.plusDays(1);
            if (localDate.getDayOfWeek().getValue() == 1) {
                ++row;
                column = 0;
            } else {
                ++column;
            }
        }

        return calendar;
    }
}
