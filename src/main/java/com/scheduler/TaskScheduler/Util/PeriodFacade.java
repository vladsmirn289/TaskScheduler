package com.scheduler.TaskScheduler.Util;

import com.scheduler.TaskScheduler.DTO.PeriodParameters;
import com.scheduler.TaskScheduler.Model.*;

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

    private List<Task> createTasksOnEachDay(LocalDate start, LocalDate end) {
        List<Task> tasks = new ArrayList<>();

        while (start.isBefore(end) || start.isEqual(end)) {
            Task task = new Task(name, description, priority, start, 0);
            task.setClient(client);

            tasks.add(task);
            start = start.plusDays(1);
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
            date = date.plusDays(1);
            resultTasks.add(t);
        }

        if (date.isBefore(endDate) || date.isEqual(endDate)) {
            resultTasks.addAll(createTasksOnEachDay(date, endDate));
        }

        return resultTasks;
    }

    private List<Task> createTasksOnEachWeek(LocalDate start, LocalDate end) {
        List<Task> tasks = new ArrayList<>();
        List<DayOfWeek> daysOfWeeks = CalendarUtil.daysOfWeekByPeriodParams(periodParameters);

        while (start.isBefore(end) || start.isEqual(end)) {
            while (!daysOfWeeks.contains(start.getDayOfWeek()) && !start.isAfter(end)) {
                start = start.plusDays(1);
            }
            if (start.isAfter(end)) {
                break;
            }

            Task task = new Task(name, description, priority, start, 0);
            task.setClient(client);

            tasks.add(task);
            start = start.plusDays(1);
        }

        return tasks;
    }

    private List<Task> updateTasksOnEachWeek(List<Task> tasks) {
        LocalDate date = startDate;
        List<Task> resultTasks = new ArrayList<>();
        List<DayOfWeek> daysOfWeeks = CalendarUtil.daysOfWeekByPeriodParams(periodParameters);

        for (Task t : tasks) {
            while (!daysOfWeeks.contains(date.getDayOfWeek()) && !date.isAfter(endDate)) {
                date = date.plusDays(1);
            }
            if (date.isAfter(endDate)) {
                break;
            }
            setTaskProperties(t, date);
            date = date.plusDays(1);
            resultTasks.add(t);
        }

        if (date.isBefore(endDate) || date.isEqual(endDate)) {
            resultTasks.addAll(createTasksOnEachWeek(date, endDate));
        }

        return resultTasks;
    }

    private List<Task> createTasksOnEachDayOfMonth(LocalDate start, LocalDate end) {
        List<Task> tasks = new ArrayList<>();
        int dayNumber = periodParameters.getDayOfMonth();
        LocalDate date = start;

        while (date.isBefore(end) || date.isEqual(end)) {
            try {
                date = date.withDayOfMonth(dayNumber);
            } catch (DateTimeException e) {
                date = date.plusMonths(1);
                continue;
            }

            if (date.isBefore(start)) {
                date = date.plusMonths(1);
                continue;
            }

            if (date.isAfter(end)) {
                break;
            }

            Task task = new Task(name, description, priority, date, 0);
            task.setClient(client);

            tasks.add(task);
            date = date.plusMonths(1);
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
                    date = date.plusMonths(1);
                    continue;
                }

                if (date.isBefore(startDate)) {
                    date = date.plusMonths(1);
                }
            }

            if (date.isAfter(endDate)) {
                break;
            }
            setTaskProperties(t, date);
            date = date.plusMonths(1);
            resultTasks.add(t);
        }

        if (date.isBefore(endDate) || date.isEqual(endDate)) {
            resultTasks.addAll(createTasksOnEachDayOfMonth(date, endDate));
        }

        return resultTasks;
    }

    private List<Task> createTasksOnEachWeekOfMonth(LocalDate start, LocalDate end) {
        List<Task> tasks = new ArrayList<>();
        String monthWeek = periodParameters.getMonthWeek();
        LocalDate date = start;
        int monthValue = date.getMonthValue();
        LocalDate[][] calendar = CalendarUtil.calendarByLocalDate(date);
        List<LocalDate> nWeek = new ArrayList<>();

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

            date = date.plusMonths(1).withDayOfMonth(1);
        }

        return tasks;
    }

    private List<Task> updateTasksOnEachWeekOfMonth() {
        return createTasksOnEachWeekOfMonth(startDate, endDate);
    }

    private List<Task> createTasksOnEachDayOfWeekOfMonth(LocalDate start, LocalDate end) {
        List<Task> tasks = new ArrayList<>();
        String numberDayOfWeek = periodParameters.getNumberDayOfWeek();
        DayOfWeek dayOfWeek = CalendarUtil.parseString(periodParameters.getDayOfWeek());
        LocalDate date = start;
        int currentMonth = date.getMonthValue();

        while (date.isBefore(end) || date.isEqual(end)) {
            if (numberDayOfWeek.equals("first")) {
                date = date.with(TemporalAdjusters.firstInMonth(dayOfWeek));
            } else if (numberDayOfWeek.equals("second")) {
                date = date.with(TemporalAdjusters.dayOfWeekInMonth(2, dayOfWeek));
                if (date.getMonthValue() != currentMonth) {
                    date = date.withDayOfMonth(1);
                    currentMonth = currentMonth % 12 + 1;
                    continue;
                }
            } else if (numberDayOfWeek.equals("third")) {
                date = date.with(TemporalAdjusters.dayOfWeekInMonth(3, dayOfWeek));
                if (date.getMonthValue() != currentMonth) {
                    date = date.withDayOfMonth(1);
                    currentMonth = currentMonth % 12 + 1;
                    continue;
                }
            } else if (numberDayOfWeek.equals("fourth")) {
                date = date.with(TemporalAdjusters.dayOfWeekInMonth(4, dayOfWeek));
                if (date.getMonthValue() != currentMonth) {
                    date = date.withDayOfMonth(1);
                    currentMonth = currentMonth % 12 + 1;
                    continue;
                }
            } else if (numberDayOfWeek.equals("fifth")) {
                date = date.with(TemporalAdjusters.dayOfWeekInMonth(5, dayOfWeek));
                if (date.getMonthValue() != currentMonth) {
                    date = date.withDayOfMonth(1);
                    currentMonth = currentMonth % 12 + 1;
                    continue;
                }
            } else if (numberDayOfWeek.equals("last")) {
                date = date.with(TemporalAdjusters.lastInMonth(dayOfWeek));
            }

            if (date.isBefore(start)) {
                date = date.withDayOfMonth(1).plusMonths(1);
                currentMonth = currentMonth % 12 + 1;
                continue;
            }

            if (date.isAfter(end)) {
                break;
            }

            Task task = new Task(name, description, priority, date, 0);
            task.setClient(client);

            tasks.add(task);
            date = date.withDayOfMonth(1).plusMonths(1);
            currentMonth = currentMonth % 12 + 1;
        }

        return tasks;
    }

    private List<Task> updateTasksOnEachDayOfWeekOfMonth(List<Task> tasks) {
        LocalDate date = startDate;
        List<Task> resultTasks = new ArrayList<>();
        String numberDayOfWeek = periodParameters.getNumberDayOfWeek();
        DayOfWeek dayOfWeek = CalendarUtil.parseString(periodParameters.getDayOfWeek());
        int currentMonth = date.getMonthValue();

        for (Task t : tasks) {
            while (date.getDayOfWeek() != dayOfWeek && !date.isAfter(endDate) || date.isBefore(startDate)) {
                if (numberDayOfWeek.equals("first")) {
                    date = date.with(TemporalAdjusters.firstInMonth(dayOfWeek));
                } else if (numberDayOfWeek.equals("second")) {
                    date = date.with(TemporalAdjusters.dayOfWeekInMonth(2, dayOfWeek));
                    if (date.getMonthValue() != currentMonth) {
                        date = date.withDayOfMonth(1);
                        currentMonth = currentMonth % 12 + 1;
                        continue;
                    }
                } else if (numberDayOfWeek.equals("third")) {
                    date = date.with(TemporalAdjusters.dayOfWeekInMonth(3, dayOfWeek));
                    if (date.getMonthValue() != currentMonth) {
                        date = date.withDayOfMonth(1);
                        currentMonth = currentMonth % 12 + 1;
                        continue;
                    }
                } else if (numberDayOfWeek.equals("fourth")) {
                    date = date.with(TemporalAdjusters.dayOfWeekInMonth(4, dayOfWeek));
                    if (date.getMonthValue() != currentMonth) {
                        date = date.withDayOfMonth(1);
                        currentMonth = currentMonth % 12 + 1;
                        continue;
                    }
                } else if (numberDayOfWeek.equals("fifth")) {
                    date = date.with(TemporalAdjusters.dayOfWeekInMonth(5, dayOfWeek));
                    if (date.getMonthValue() != currentMonth) {
                        date = date.withDayOfMonth(1);
                        currentMonth = currentMonth % 12 + 1;
                        continue;
                    }
                } else if (numberDayOfWeek.equals("last")) {
                    date = date.with(TemporalAdjusters.lastInMonth(dayOfWeek));
                }

                if (date.isBefore(startDate)) {
                    date = date.withDayOfMonth(1).plusMonths(1);
                    currentMonth = currentMonth % 12 + 1;
                }
            }

            if (date.isAfter(endDate)) {
                break;
            }
            setTaskProperties(t, date);
            date = date.withDayOfMonth(1).plusMonths(1);
            resultTasks.add(t);
        }

        if (date.isBefore(endDate) || date.isEqual(endDate)) {
            resultTasks.addAll(createTasksOnEachDayOfWeekOfMonth(date, endDate));
        }

        return resultTasks;
    }
}
