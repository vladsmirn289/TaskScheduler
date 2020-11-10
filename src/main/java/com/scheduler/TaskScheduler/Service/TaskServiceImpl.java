package com.scheduler.TaskScheduler.Service;

import com.scheduler.TaskScheduler.Model.Client;
import com.scheduler.TaskScheduler.Model.Task;
import com.scheduler.TaskScheduler.Repository.TaskRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
    private final TaskRepo taskRepo;

    public TaskServiceImpl(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> findByClient(Client client) {
        logger.info("Finding tasks belonging to the client");

        return taskRepo.findByClient(client);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> findByClientAndDate(Client client, LocalDate date) {
        logger.info("Finding tasks belonging to the client by date");

        return taskRepo.findByClientAndDate(client, date);
    }

    @Override
    public void save(Task task) {
        logger.info("Saving task");

        if (task == null) {
            return;
        }

        taskRepo.save(task);
        task.getClient().getTasks().add(task);
    }

    @Override
    public void delete(Task task) {
        logger.info("Deleting task");

        if (task == null) {
            return;
        }

        taskRepo.delete(task);
        task.getClient().getTasks().remove(task);
    }

    @Override
    public void deleteById(Long id) {
        logger.info("Deleting task by id - " + id);

        Task task = taskRepo.findById(id).orElse(null);
        if (task == null) {
            return;
        }

        taskRepo.deleteById(id);
        task.getClient().getTasks().remove(task);
    }
}
