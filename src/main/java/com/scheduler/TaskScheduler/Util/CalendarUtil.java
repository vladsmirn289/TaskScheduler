package com.scheduler.TaskScheduler.Util;

import com.scheduler.TaskScheduler.DTO.PeriodParameters;
import com.scheduler.TaskScheduler.Model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CalendarUtil {
    private static final Logger logger = LoggerFactory.getLogger(CalendarUtil.class);

    public static LocalDate[][] calendarByLocalDate(LocalDate localDate) {
        logger.info("CalendarByLocalDate method is called");

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

    public static List<Integer> countTasksByEachDay(LocalDate start, LocalDate end, List<Task> tasks) {
        logger.info("CountTasksByEachDay method called");
        int[] countTasks = new int[end.getDayOfMonth()];

        tasks.stream()
                .filter(t -> t.getDate().getMonth().equals(start.getMonth()) && t.getProgress() != 100)
                .sorted(Comparator.comparing(Task::getDate))
                .forEach(t -> countTasks[t.getDate().getDayOfMonth()-1] += 1);

        return Arrays.stream(countTasks)
                .boxed()
                .collect(Collectors.toList());
    }

    public static List<DayOfWeek> daysOfWeekByPeriodParams(PeriodParameters periodParameters) {
        List<DayOfWeek> dayOfWeeks = new ArrayList<>();

        if (periodParameters.isMonday()) {
            dayOfWeeks.add(DayOfWeek.MONDAY);
        }

        if (periodParameters.isTuesday()) {
            dayOfWeeks.add(DayOfWeek.TUESDAY);
        }

        if (periodParameters.isWednesday()) {
            dayOfWeeks.add(DayOfWeek.WEDNESDAY);
        }

        if (periodParameters.isThursday()) {
            dayOfWeeks.add(DayOfWeek.THURSDAY);
        }

        if (periodParameters.isFriday()) {
            dayOfWeeks.add(DayOfWeek.FRIDAY);
        }

        if (periodParameters.isSaturday()) {
            dayOfWeeks.add(DayOfWeek.SATURDAY);
        }

        if (periodParameters.isSunday()) {
            dayOfWeeks.add(DayOfWeek.SUNDAY);
        }

        return dayOfWeeks;
    }
}
