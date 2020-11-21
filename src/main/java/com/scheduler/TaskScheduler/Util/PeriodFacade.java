package com.scheduler.TaskScheduler.Util;

import com.scheduler.TaskScheduler.Model.PeriodMode;
import com.scheduler.TaskScheduler.Model.RepeatableTask;
import com.scheduler.TaskScheduler.Model.Task;
import com.scheduler.TaskScheduler.Service.TaskService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class PeriodFacade {
    public RepeatableTask initTasks(RepeatableTask repeatableTask) {
        PeriodMode periodMode = repeatableTask.getPeriodMode();
        List<Task> tasks = new ArrayList<>();

        if (periodMode.equals(PeriodMode.EACH_DAY)) {
            tasks = createTasksOnEachDay(repeatableTask.getStartDate(), repeatableTask.getEndDate(), repeatableTask);
        }

        repeatableTask.setTasks(tasks);
        return repeatableTask;
    }

    public RepeatableTask updateTasks(RepeatableTask repeatableTask, TaskService taskService) {
        PeriodMode periodMode = repeatableTask.getPeriodMode();

        if (periodMode.equals(PeriodMode.EACH_DAY)) {
             repeatableTask.setTasks(updateTasksOnEachDay(repeatableTask, taskService));
        }

        return repeatableTask;
    }

    private List<Task> createTasksOnEachDay(LocalDate start, LocalDate end, RepeatableTask repeatableTask) {
        List<Task> tasks = new ArrayList<>();

        while (start.isBefore(end) || start.isEqual(end)) {
            Task task = new Task(
                    repeatableTask.getName(), repeatableTask.getDescription(), repeatableTask.getPriority(),
                    start, 0
            );
            task.setClient(repeatableTask.getClient());

            tasks.add(task);
            start = start.plusDays(1);
        }

        return tasks;
    }

    private List<Task> updateTasksOnEachDay(RepeatableTask repeatableTask, TaskService taskService) {
        List<Task> tasks = taskService.findAllByRepeatableTask(repeatableTask);
        tasks.sort(Comparator.comparing(Task::getDate));
        LocalDate date = repeatableTask.getStartDate();
        List<Task> resultTasks = new ArrayList<>();
        for (Task t : tasks) {
            if (date.isAfter(repeatableTask.getEndDate())) {
                taskService.delete(t);
                continue;
            }
            t.setDate(date);
            date = date.plusDays(1);

            t.setDescription(repeatableTask.getDescription());
            t.setName(repeatableTask.getName());
            t.setPriority(repeatableTask.getPriority());

            resultTasks.add(t);
        }

        return resultTasks;
    }
}
