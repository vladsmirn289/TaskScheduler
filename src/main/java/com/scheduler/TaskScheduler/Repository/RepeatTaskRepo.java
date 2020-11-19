package com.scheduler.TaskScheduler.Repository;

import com.scheduler.TaskScheduler.Model.Client;
import com.scheduler.TaskScheduler.Model.RepeatableTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepeatTaskRepo extends JpaRepository<RepeatableTask, Long> {
    List<RepeatableTask> findByClient(Client client);
}
