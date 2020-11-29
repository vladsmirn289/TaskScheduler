package com.scheduler.TaskScheduler.Service;

import com.scheduler.TaskScheduler.Model.Client;
import com.scheduler.TaskScheduler.Model.RepeatableTask;
import com.scheduler.TaskScheduler.Model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskService {
    Optional<Task> findById(Long id);
    List<Task> findByClient(Client client);
    Page<Task> findByClientAndDate(Client client, LocalDate date, Pageable pageable);
    List<Task> findAllByRepeatableTask(RepeatableTask repeatableTask);

    void save(Task task);

    void delete(Task task);
    void deleteById(Long id);

    boolean clientHasTask(Client client, Task task);
}
