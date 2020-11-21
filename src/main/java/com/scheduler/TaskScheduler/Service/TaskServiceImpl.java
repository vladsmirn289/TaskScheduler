package com.scheduler.TaskScheduler.Service;

import com.scheduler.TaskScheduler.Model.Client;
import com.scheduler.TaskScheduler.Model.RepeatableTask;
import com.scheduler.TaskScheduler.Model.Task;
import com.scheduler.TaskScheduler.Repository.TaskRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
    private final TaskRepo taskRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public TaskServiceImpl(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Task> findById(Long id) {
        logger.info("Finding task by id");

        return taskRepo.findById(id);
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
    @Transactional(readOnly = true)
    public List<Task> findAllByRepeatableTask(RepeatableTask repeatableTask) {
        return taskRepo.findAllByRepeatableTask(repeatableTask);
    }

    @Override
    public void save(Task task) {
        logger.info("Saving task");

        if (task == null) {
            return;
        }

        Client client = entityManager.merge(task.getClient());
        List<Task> tasks = client.getTasks();

        if (task.getId() != null) {
            Task persistTask = findById(task.getId()).get();
            BeanUtils.copyProperties(task, persistTask, "repeatableTask");
            taskRepo.save(persistTask);

            tasks.remove(task);
            tasks.add(persistTask);
        } else {
            taskRepo.save(task);
            tasks.add(task);
        }
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

    public boolean clientHasTask(Client client, Task task) {
        List<Task> taskList = findByClientAndDate(client, task.getDate());

        return taskList.contains(task);
    }
}
