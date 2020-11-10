package com.scheduler.TaskScheduler.Service;

import com.scheduler.TaskScheduler.Model.Client;
import com.scheduler.TaskScheduler.Model.Task;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {
    List<Task> findByClient(Client client);
    List<Task> findByClientAndDate(Client client, LocalDate date);

    void save(Task task);

    void delete(Task task);
    void deleteById(Long id);
}
