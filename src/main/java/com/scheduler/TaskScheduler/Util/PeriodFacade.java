package com.scheduler.TaskScheduler.Util;

import com.scheduler.TaskScheduler.DTO.PeriodParameters;
import com.scheduler.TaskScheduler.Model.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
        }

        return repeatableTask;
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
            t.setDate(date);
            date = date.plusDays(1);

            t.setDescription(description);
            t.setName(name);
            t.setPriority(priority);

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
            t.setDate(date);
            date = date.plusDays(1);

            t.setDescription(description);
            t.setName(name);
            t.setPriority(priority);

            resultTasks.add(t);
        }

        if (date.isBefore(endDate) || date.isEqual(endDate)) {
            resultTasks.addAll(createTasksOnEachWeek(date, endDate));
        }

        return resultTasks;
    }
}
