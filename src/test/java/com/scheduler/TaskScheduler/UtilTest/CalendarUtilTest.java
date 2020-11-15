package com.scheduler.TaskScheduler.UtilTest;

import com.scheduler.TaskScheduler.Model.Priority;
import com.scheduler.TaskScheduler.Model.Task;
import com.scheduler.TaskScheduler.Util.CalendarUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CalendarUtilTest {
    @Test
    void calendarByLocalDateMethodTestFirst() {
        LocalDate localDate = LocalDate.of(2010, Month.FEBRUARY, 1);
        LocalDate[][] calendar = CalendarUtil.calendarByLocalDate(localDate);

        LocalDate[][] answer = { {LocalDate.of(2010, Month.FEBRUARY, 1),
                                  LocalDate.of(2010, Month.FEBRUARY, 2),
                                  LocalDate.of(2010, Month.FEBRUARY, 3),
                                  LocalDate.of(2010, Month.FEBRUARY, 4),
                                  LocalDate.of(2010, Month.FEBRUARY, 5),
                                  LocalDate.of(2010, Month.FEBRUARY, 6),
                                  LocalDate.of(2010, Month.FEBRUARY, 7)},
                                {
                                  LocalDate.of(2010, Month.FEBRUARY, 8),
                                  LocalDate.of(2010, Month.FEBRUARY, 9),
                                  LocalDate.of(2010, Month.FEBRUARY, 10),
                                  LocalDate.of(2010, Month.FEBRUARY, 11),
                                  LocalDate.of(2010, Month.FEBRUARY, 12),
                                  LocalDate.of(2010, Month.FEBRUARY, 13),
                                  LocalDate.of(2010, Month.FEBRUARY, 14)},
                                {
                                  LocalDate.of(2010, Month.FEBRUARY, 15),
                                  LocalDate.of(2010, Month.FEBRUARY, 16),
                                  LocalDate.of(2010, Month.FEBRUARY, 17),
                                  LocalDate.of(2010, Month.FEBRUARY, 18),
                                  LocalDate.of(2010, Month.FEBRUARY, 19),
                                  LocalDate.of(2010, Month.FEBRUARY, 20),
                                  LocalDate.of(2010, Month.FEBRUARY, 21)},
                                {
                                  LocalDate.of(2010, Month.FEBRUARY, 22),
                                  LocalDate.of(2010, Month.FEBRUARY, 23),
                                  LocalDate.of(2010, Month.FEBRUARY, 24),
                                  LocalDate.of(2010, Month.FEBRUARY, 25),
                                  LocalDate.of(2010, Month.FEBRUARY, 26),
                                  LocalDate.of(2010, Month.FEBRUARY, 27),
                                  LocalDate.of(2010, Month.FEBRUARY, 28)
                                }};

        assertThat(Arrays.deepEquals(calendar, answer)).isTrue();
    }

    @Test
    void calendarByLocalDateMethodTestSecond() {
        LocalDate localDate = LocalDate.of(2020, Month.NOVEMBER, 1);
        LocalDate[][] calendar = CalendarUtil.calendarByLocalDate(localDate);

        LocalDate[][] answer = {
                {
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        LocalDate.of(2020, Month.NOVEMBER, 1)},
                {
                        LocalDate.of(2020, Month.NOVEMBER, 2),
                        LocalDate.of(2020, Month.NOVEMBER, 3),
                        LocalDate.of(2020, Month.NOVEMBER, 4),
                        LocalDate.of(2020, Month.NOVEMBER, 5),
                        LocalDate.of(2020, Month.NOVEMBER, 6),
                        LocalDate.of(2020, Month.NOVEMBER, 7),
                        LocalDate.of(2020, Month.NOVEMBER, 8)},
                {
                        LocalDate.of(2020, Month.NOVEMBER, 9),
                        LocalDate.of(2020, Month.NOVEMBER, 10),
                        LocalDate.of(2020, Month.NOVEMBER, 11),
                        LocalDate.of(2020, Month.NOVEMBER, 12),
                        LocalDate.of(2020, Month.NOVEMBER, 13),
                        LocalDate.of(2020, Month.NOVEMBER, 14),
                        LocalDate.of(2020, Month.NOVEMBER, 15)},
                {
                        LocalDate.of(2020, Month.NOVEMBER, 16),
                        LocalDate.of(2020, Month.NOVEMBER, 17),
                        LocalDate.of(2020, Month.NOVEMBER, 18),
                        LocalDate.of(2020, Month.NOVEMBER, 19),
                        LocalDate.of(2020, Month.NOVEMBER, 20),
                        LocalDate.of(2020, Month.NOVEMBER, 21),
                        LocalDate.of(2020, Month.NOVEMBER, 22)},
                {
                        LocalDate.of(2020, Month.NOVEMBER, 23),
                        LocalDate.of(2020, Month.NOVEMBER, 24),
                        LocalDate.of(2020, Month.NOVEMBER, 25),
                        LocalDate.of(2020, Month.NOVEMBER, 26),
                        LocalDate.of(2020, Month.NOVEMBER, 27),
                        LocalDate.of(2020, Month.NOVEMBER, 28),
                        LocalDate.of(2020, Month.NOVEMBER, 29)},
                {
                        LocalDate.of(2020, Month.NOVEMBER, 30),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                }};

        assertThat(Arrays.deepEquals(calendar, answer)).isTrue();
    }

    @Test
    public void shouldCountTasksByEachDay() {
        Task task1 = new Task("t1", "d1", Priority.LOW, LocalDate.of(2020, 11, 17), 0);
        Task task2 = new Task("t2", "d2", Priority.MEDIUM, LocalDate.of(2020, 11, 17), 0);
        Task task3 = new Task("t3", "d3", Priority.NO, LocalDate.of(2020, 11, 20), 0);
        Task task4 = new Task("t4", "d4", Priority.HIGH, LocalDate.of(2020, 11, 17), 0);
        List<Task> tasks = new ArrayList<>(Arrays.asList(task1, task2, task3, task4));

        LocalDate start = LocalDate.of(2020, 11, 1);
        LocalDate end = LocalDate.of(2020, 11, 30);
        List<Integer> nums = CalendarUtil.countTasksByEachDay(start, end, tasks);

        List<Integer> answer = new ArrayList<>(Arrays.asList(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        ));

        assertThat(nums).isEqualTo(answer);
    }
}
