package com.scheduler.TaskScheduler.Util;

import com.scheduler.TaskScheduler.Model.Task;

import java.util.Comparator;

public class TaskIdComparator implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {
        return (int)(o1.getId() - o2.getId());
    }
}
