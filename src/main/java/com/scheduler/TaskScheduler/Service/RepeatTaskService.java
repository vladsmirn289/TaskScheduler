package com.scheduler.TaskScheduler.Service;

import com.scheduler.TaskScheduler.Model.Client;
import com.scheduler.TaskScheduler.Model.RepeatableTask;

import java.util.List;
import java.util.Optional;

public interface RepeatTaskService {
    Optional<RepeatableTask> findById(Long id);
    List<RepeatableTask> findByClient(Client client);

    void save(RepeatableTask task);

    void delete(RepeatableTask task);
    void deleteById(Long id);
}
