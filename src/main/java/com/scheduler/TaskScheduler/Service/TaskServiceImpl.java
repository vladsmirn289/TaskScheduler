package com.scheduler.TaskScheduler.Service;

import com.scheduler.TaskScheduler.Model.Client;
import com.scheduler.TaskScheduler.Model.Task;
import com.scheduler.TaskScheduler.Repository.ClientRepo;
import com.scheduler.TaskScheduler.Repository.TaskRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
    private final TaskRepo taskRepo;
    private final ClientRepo clientRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public TaskServiceImpl(TaskRepo taskRepo, ClientRepo clientRepo) {
        this.taskRepo = taskRepo;
        this.clientRepo = clientRepo;
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
        Client client = entityManager.merge(task.getClient());
        client.getTasks().add(task);
    }

    @Override
    public void delete(Task task) {
        logger.info("Deleting task");

        if (task == null) {
            return;
        }

        Client client = entityManager.merge(task.getClient());
        taskRepo.delete(task);
        client.getTasks().remove(task);
    }

    @Override
    public void deleteById(Long id) {
        logger.info("Deleting task by id - " + id);

        Task task = taskRepo.findById(id).orElse(null);
        if (task == null) {
            return;
        }

        Client client = entityManager.merge(task.getClient());
        taskRepo.deleteById(id);
        client.getTasks().remove(task);
    }
}
