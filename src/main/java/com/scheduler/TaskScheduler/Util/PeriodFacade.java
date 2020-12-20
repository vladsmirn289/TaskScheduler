package com.scheduler.TaskScheduler.Util;

import com.scheduler.TaskScheduler.DTO.PeriodParameters;
import com.scheduler.TaskScheduler.Model.*;
import com.scheduler.TaskScheduler.Util.Pair;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

public class PeriodFacade {
    private RepeatableTask repeatableTask;
    private PeriodParameters periodParameters;

    private LocalDate startDate;
    private LocalDate endDate;
    private String name;
    private String description;
    private Priority priority;
    private PeriodMode periodMode;
    private Client client;

    public PeriodFacade(RepeatableTask repeatableTask, PeriodParameters periodParameters) {
        this.repeatableTask = repeatableTask;
        this.periodParameters = periodParameters;

        this.startDate = repeatableTask.getStartDate();
        this.endDate = repeatableTask.getEndDate();
        this.name = repeatableTask.getName();
        this.description = repeatableTask.getDescription();
        this.priority = repeatableTask.getPriority();
        this.periodMode = repeatableTask.getPeriodMode();
        this.client = repeatableTask.getClient();
    }

    public RepeatableTask getRepeatableTask() {
        return repeatableTask;
    }

    public void setRepeatableTask(RepeatableTask repeatableTask) {
        this.repeatableTask = repeatableTask;

        this.startDate = repeatableTask.getStartDate();
        this.endDate = repeatableTask.getEndDate();
        this.name = repeatableTask.getName();
        this.description = repeatableTask.getDescription();
        this.priority = repeatableTask.getPriority();
        this.periodMode = repeatableTask.getPeriodMode();
        this.client = repeatableTask.getClient();
    }

    public PeriodParameters getPeriodParameters() {
        return periodParameters;
    }

    public void setPeriodParameters(PeriodParameters periodParameters) {
        this.periodParameters = periodParameters;
    }

    public RepeatableTask initTasks() {
        List<Task> tasks = new ArrayList<>();

        if (periodMode.equals(PeriodMode.EACH_DAY)) {
            tasks = createTasksOnEachDay(startDate, endDate);
        } else if (periodMode.equals(PeriodMode.EACH_WEEK)) {
            tasks = createTasksOnEachWeek(startDate, endDate);
        } else if (periodMode.equals(PeriodMode.EACH_DAY_OF_MONTH)) {
            tasks = createTasksOnEachDayOfMonth(startDate, endDate);
        } else if (periodMode.equals(PeriodMode.EACH_WEEK_OF_MONTH)) {
            tasks = createTasksOnEachWeekOfMonth(startDate, endDate);
        } else if (periodMode.equals(PeriodMode.EACH_DAY_OF_WEEK_OF_MONTH)) {
            tasks = createTasksOnEachDayOfWeekOfMonth(startDate, endDate);
        }

        repeatableTask.setTasks(tasks);
        return repeatableTask;
    }

    public RepeatableTask updateTasks() {
        List<Task> tasks = repeatableTask.getTasks();
        tasks.sort(Comparator.comparing(Task::getDate));

        if (periodMode.equals(PeriodMode.EACH_DAY)) {
            repeatableTask.setTasks(updateTasksOnEachDay(tasks));
        } else if (periodMode.equals(PeriodMode.EACH_WEEK)) {
            repeatableTask.setTasks(updateTasksOnEachWeek(tasks));
        } else if (periodMode.equals(PeriodMode.EACH_DAY_OF_MONTH)) {
            repeatableTask.setTasks(updateTasksOnEachDayOfMonth(tasks));
        } else if (periodMode.equals(PeriodMode.EACH_WEEK_OF_MONTH)) {
            repeatableTask.setTasks(updateTasksOnEachWeekOfMonth());
        } else if (periodMode.equals(PeriodMode.EACH_DAY_OF_WEEK_OF_MONTH)) {
            repeatableTask.setTasks(updateTasksOnEachDayOfWeekOfMonth(tasks));
        }

        return repeatableTask;
    }

    private void setTaskProperties(Task t, LocalDate date) {
        t.setDate(date);
        t.setDescription(description);
        t.setName(name);
        t.setPriority(priority);
        t.setId(null);
    }

    //Each day methods group

    private List<Task> createTasksOnEachDay(LocalDate start, LocalDate end) {
        List<Task> tasks = new ArrayList<>();

        while (start.isBefore(end) || start.isEqual(end)) {
            Task task = new Task(name, description, priority, start, 0);
            task.setClient(client);

            tasks.add(task);

            start = calcGapDaysForEachDay(start);
        }

        return tasks;
    }

    private List<Task> updateTasksOnEachDay(List<Task> tasks) {
        LocalDate date = startDate;
        List<Task> resultTasks = new ArrayList<>();
        for (Task t : tasks) {
            if (date.isAfter(endDate)) {
                break;
            }
            setTaskProperties(t, date);
            resultTasks.add(t);

            date = calcGapDaysForEachDay(date);
        }

        if (date.isBefore(endDate) || date.isEqual(endDate)) {
            resultTasks.addAll(createTasksOnEachDay(date, endDate));
        }

        return resultTasks;
    }

    private LocalDate calcGapDaysForEachDay(LocalDate date) {
        int gapDays = periodParameters.getGapDays();
        if (gapDays != 0) {
            return date.plusDays(1 + gapDays);
        } else {
            return date.plusDays(1);
        }
    }

    //Each week methods group

    private List<Task> createTasksOnEachWeek(LocalDate start, LocalDate end) {
        List<Task> tasks = new ArrayList<>();

        while (start.isBefore(end) || start.isEqual(end)) {
            start = defineStartForEachWeek(start, end);
            if (start.isAfter(end)) {
                break;
            }

            Task task = new Task(name, description, priority, start, 0);
            task.setClient(client);

            tasks.add(task);

            start = calcGapDaysForEachWeek(start);
        }

        return tasks;
    }

    private List<Task> updateTasksOnEachWeek(List<Task> tasks) {
        LocalDate date = startDate;
        List<Task> resultTasks = new ArrayList<>();

        for (Task t : tasks) {
            date = defineStartForEachWeek(date, endDate);
            if (date.isAfter(endDate)) {
                break;
            }
            setTaskProperties(t, date);
            resultTasks.add(t);

            date = calcGapDaysForEachDay(date);
        }

        if (date.isBefore(endDate) || date.isEqual(endDate)) {
            resultTasks.addAll(createTasksOnEachWeek(date, endDate));
        }

        return resultTasks;
    }

    private LocalDate defineStartForEachWeek(LocalDate start, LocalDate end) {
        List<DayOfWeek> daysOfWeeks = CalendarUtil.daysOfWeekByPeriodParams(periodParameters);

        while (!daysOfWeeks.contains(start.getDayOfWeek()) && !start.isAfter(end)) {
            start = calcGapDaysForEachWeek(start);
        }

        return start;
    }

    private LocalDate calcGapDaysForEachWeek(LocalDate start) {
        int gapWeeks = periodParameters.getGapWeeks();

        if (gapWeeks != 0 && start.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            return start.plusDays(8);
        } else {
            return start.plusDays(1);
        }
    }

    //Each day of month methods group

    private List<Task> createTasksOnEachDayOfMonth(LocalDate start, LocalDate end) {
        LocalDate date = start;
        List<Task> tasks = new ArrayList<>();
        int dayNumber = periodParameters.getDayOfMonth();

        while (date.isBefore(end) || date.isEqual(end)) {
            try {
                date = date.withDayOfMonth(dayNumber);
            } catch (DateTimeException e) {
                date = calcGapDaysForEachDayOfMonth(date);
                continue;
            }

            if (date.isBefore(start)) {
                date = calcGapDaysForEachDayOfMonth(date);
                continue;
            }

            if (date.isAfter(end)) {
                break;
            }

            Task task = new Task(name, description, priority, date, 0);
            task.setClient(client);

            tasks.add(task);

            date = calcGapDaysForEachDayOfMonth(date);
        }

        return tasks;
    }

    private List<Task> updateTasksOnEachDayOfMonth(List<Task> tasks) {
        LocalDate date = startDate;
        List<Task> resultTasks = new ArrayList<>();
        int dayNumber = periodParameters.getDayOfMonth();

        for (Task t : tasks) {
            while (date.getDayOfMonth() != dayNumber && !date.isAfter(endDate) || date.isBefore(startDate)) {
                try {
                    date = date.withDayOfMonth(dayNumber);
                } catch (DateTimeException e) {
                    date = calcGapDaysForEachDayOfMonth(date);
                    continue;
                }

                if (date.isBefore(startDate)) {
                    date = calcGapDaysForEachDayOfMonth(date);
                }
            }

            if (date.isAfter(endDate)) {
                break;
            }
            setTaskProperties(t, date);
            resultTasks.add(t);

            date = calcGapDaysForEachDayOfMonth(date);
        }

        if (date.isBefore(endDate) || date.isEqual(endDate)) {
            resultTasks.addAll(createTasksOnEachDayOfMonth(date, endDate));
        }

        return resultTasks;
    }

    private LocalDate calcGapDaysForEachDayOfMonth(LocalDate date) {
        int gapMonths = periodParameters.getGapMonths();

        if (gapMonths != 0) {
            return date.withDayOfMonth(1).plusMonths(1 + gapMonths);
        } else {
            return date.withDayOfMonth(1).plusMonths(1);
        }
    }

    //Each week of month methods group

    private List<Task> createTasksOnEachWeekOfMonth(LocalDate start, LocalDate end) {
        List<Task> tasks = new ArrayList<>();
        String monthWeek = periodParameters.getMonthWeek();
        LocalDate date = start;
        int monthValue = date.getMonthValue();
        LocalDate[][] calendar = CalendarUtil.calendarByLocalDate(date);
        List<LocalDate> nWeek = new ArrayList<>();
        int gapMonths = periodParameters.getGapMonths();

        while (date.isBefore(end) || date.isEqual(end)) {
            if (date.getMonthValue() != monthValue) {
                calendar = CalendarUtil.calendarByLocalDate(date);
            }

            if (monthWeek.equals("first")) {
                if (calendar.length >= 1) {
                    nWeek = Arrays.stream(calendar[0])
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                }
            } else if (monthWeek.equals("second")) {
                if (calendar.length >= 2) {
                    nWeek = Arrays.stream(calendar[1])
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                }
            } else if (monthWeek.equals("third")) {
                if (calendar.length >= 3) {
                    nWeek = Arrays.stream(calendar[2])
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                }
            } else if (monthWeek.equals("fourth")) {
                if (calendar.length >= 4) {
                    nWeek = Arrays.stream(calendar[3])
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                }
            } else if (monthWeek.equals("fifth")) {
                if (calendar.length >= 5) {
                    nWeek = Arrays.stream(calendar[4])
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                }
            } else if (monthWeek.equals("sixth")) {
                if (calendar.length >= 6) {
                    nWeek = Arrays.stream(calendar[5])
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                }
            } else if (monthWeek.equals("last")) {
                nWeek = Arrays.stream(calendar[calendar.length-1])
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }

            nWeek = nWeek.stream().filter(d -> d.isBefore(end) || d.isEqual(end))
                    .collect(Collectors.toList());

            Task task;
            for (LocalDate d : nWeek) {
                task = new Task(name, description, priority, d, 0);
                task.setClient(client);
                tasks.add(task);
            }

            if (gapMonths != 0) {
                date = date.withDayOfMonth(1).plusMonths(1 + gapMonths);
            } else {
                date = date.withDayOfMonth(1).plusMonths(1);
            }
        }

        return tasks;
    }

    private List<Task> updateTasksOnEachWeekOfMonth() {
        return createTasksOnEachWeekOfMonth(startDate, endDate);
    }

    //Each day of week of month methods group

    private List<Task> createTasksOnEachDayOfWeekOfMonth(LocalDate start, LocalDate end) {
        List<Task> tasks = new ArrayList<>();
        LocalDate date = start;
        int currentMonth = date.getMonthValue();
        Pair<LocalDate, Integer> pair;

        while (date.isBefore(end) || date.isEqual(end)) {
            date = defineStartForEachDayOfWeekOfMonth(date);

            if (date.getMonthValue() != currentMonth) {
                pair = calcGapDaysForEachDayOfWeekOfMonth(date, currentMonth, false);
                date = pair.getKey();
                currentMonth = pair.getValue();
                continue;
            }

            if (date.isBefore(start)) {
                pair = calcGapDaysForEachDayOfWeekOfMonth(date, currentMonth, true);
                date = pair.getKey();
                currentMonth = pair.getValue();
                continue;
            }

            if (date.isAfter(end)) {
                break;
            }

            Task task = new Task(name, description, priority, date, 0);
            task.setClient(client);

            tasks.add(task);

            pair = calcGapDaysForEachDayOfWeekOfMonth(date, currentMonth, true);
            date = pair.getKey();
            currentMonth = pair.getValue();
        }

        return tasks;
    }

    private List<Task> updateTasksOnEachDayOfWeekOfMonth(List<Task> tasks) {
        LocalDate date = startDate;
        List<Task> resultTasks = new ArrayList<>();
        DayOfWeek dayOfWeek = CalendarUtil.parseString(periodParameters.getDayOfWeek());
        int currentMonth = date.getMonthValue();
        Pair<LocalDate, Integer> pair;

        for (Task t : tasks) {
            while (date.getDayOfWeek() != dayOfWeek && !date.isAfter(endDate) || date.isBefore(startDate)) {
                date = defineStartForEachDayOfWeekOfMonth(date);

                if (date.getMonthValue() != currentMonth) {
                    pair = calcGapDaysForEachDayOfWeekOfMonth(date, currentMonth, false);
                    date = pair.getKey();
                    currentMonth = pair.getValue();
                    continue;
                }

                if (date.isBefore(startDate)) {
                    pair = calcGapDaysForEachDayOfWeekOfMonth(date, currentMonth, true);
                    date = pair.getKey();
                    currentMonth = pair.getValue();
                }
            }

            if (date.isAfter(endDate)) {
                break;
            }
            setTaskProperties(t, date);
            resultTasks.add(t);

            pair = calcGapDaysForEachDayOfWeekOfMonth(date, currentMonth, true);
            date = pair.getKey();
            currentMonth = pair.getValue();
        }

        if (date.isBefore(endDate) || date.isEqual(endDate)) {
            resultTasks.addAll(createTasksOnEachDayOfWeekOfMonth(date, endDate));
        }

        return resultTasks;
    }

    private LocalDate defineStartForEachDayOfWeekOfMonth(LocalDate date) {
        String numberDayOfWeek = periodParameters.getNumberDayOfWeek();
        DayOfWeek dayOfWeek = CalendarUtil.parseString(periodParameters.getDayOfWeek());

        switch (numberDayOfWeek) {
            case "first": return date.with(TemporalAdjusters.firstInMonth(dayOfWeek));
            case "second": return date.with(TemporalAdjusters.dayOfWeekInMonth(2, dayOfWeek));
            case "third": return date.with(TemporalAdjusters.dayOfWeekInMonth(3, dayOfWeek));
            case "fourth": return date.with(TemporalAdjusters.dayOfWeekInMonth(4, dayOfWeek));
            case "fifth": return date.with(TemporalAdjusters.dayOfWeekInMonth(5, dayOfWeek));
            case "last": return date.with(TemporalAdjusters.lastInMonth(dayOfWeek));
        }

        throw new RuntimeException("Unknown number of week parameter: " + numberDayOfWeek);
    }

    private Pair<LocalDate, Integer> calcGapDaysForEachDayOfWeekOfMonth(LocalDate date, int currentMonth, boolean plusOne) {
        int gapMonths = periodParameters.getGapMonths();

        if (gapMonths != 0) {
            if (plusOne) {
                date = date.withDayOfMonth(1).plusMonths(1 + gapMonths);
            } else {
                date = date.withDayOfMonth(1).plusMonths(gapMonths);
            }
            currentMonth = (currentMonth+gapMonths) % 12 + 1;
        } else {
            if (plusOne) {
                date = date.withDayOfMonth(1).plusMonths(1);
            } else {
                date = date.withDayOfMonth(1);
            }
            currentMonth = currentMonth % 12 + 1;
        }

        return new Pair<>(date, currentMonth);
    }
}
