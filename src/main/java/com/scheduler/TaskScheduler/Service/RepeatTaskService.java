package com.scheduler.TaskScheduler.Service;

import com.scheduler.TaskScheduler.Model.Client;
import com.scheduler.TaskScheduler.Model.RepeatableTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RepeatTaskService {
    Optional<RepeatableTask> findById(Long id);
    Page<RepeatableTask> findByClient(Client client, Pageable pageable);

    void save(RepeatableTask task);

    void delete(RepeatableTask task);
    void deleteById(Long id);

    boolean clientHasRepeatTask(Client client, RepeatableTask task);
}
