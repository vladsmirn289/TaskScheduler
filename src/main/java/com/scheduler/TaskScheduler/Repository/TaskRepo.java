package com.scheduler.TaskScheduler.Repository;

import com.scheduler.TaskScheduler.Model.Client;
import com.scheduler.TaskScheduler.Model.RepeatableTask;
import com.scheduler.TaskScheduler.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {
    List<Task> findByClient(Client client);
    List<Task> findByClientAndDate(Client client, LocalDate date);
    List<Task> findAllByRepeatableTask(RepeatableTask repeatableTask);
}
