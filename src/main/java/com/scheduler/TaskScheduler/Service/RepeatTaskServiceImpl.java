package com.scheduler.TaskScheduler.Service;

import com.scheduler.TaskScheduler.Model.Client;
import com.scheduler.TaskScheduler.Model.RepeatableTask;
import com.scheduler.TaskScheduler.Repository.RepeatTaskRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RepeatTaskServiceImpl implements RepeatTaskService {
    private static final Logger logger = LoggerFactory.getLogger(RepeatTaskServiceImpl.class);
    private final RepeatTaskRepo repeatTaskRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public RepeatTaskServiceImpl(RepeatTaskRepo repeatTaskRepo) {
        this.repeatTaskRepo = repeatTaskRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RepeatableTask> findById(Long id) {
        logger.info("Finding repeatable task by id");

        return repeatTaskRepo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RepeatableTask> findByClient(Client client) {
        logger.info("Finding repeatable tasks belonging to the client");

        return repeatTaskRepo.findByClient(client);
    }

    @Override
    public void save(RepeatableTask task) {
        logger.info("Saving repeatable task");

        if (task == null) {
            return;
        }

        Client client = entityManager.merge(task.getClient());
        List<RepeatableTask> tasks = client.getRepeatableTasks();

        if (task.getId() != null) {
            RepeatableTask persistTask = entityManager.merge(task);
            BeanUtils.copyProperties(task, persistTask);
            repeatTaskRepo.save(persistTask);

            tasks.remove(task);
            tasks.add(persistTask);
        } else {
            repeatTaskRepo.save(task);
            tasks.add(task);
        }
    }

    @Override
    public void delete(RepeatableTask task) {
        logger.info("Deleting repeatable task");

        if (task == null) {
            return;
        }

        Client client = entityManager.merge(task.getClient());
        repeatTaskRepo.delete(task);
        client.getRepeatableTasks().remove(task);
    }

    @Override
    public void deleteById(Long id) {
        logger.info("Deleting repeatable task by id - " + id);

        RepeatableTask task = repeatTaskRepo.findById(id).orElse(null);
        if (task == null) {
            return;
        }

        Client client = entityManager.merge(task.getClient());
        repeatTaskRepo.deleteById(id);
        client.getRepeatableTasks().remove(task);
    }

    @Override
    public boolean clientHasRepeatTask(Client client, RepeatableTask task) {
        List<RepeatableTask> tasks = findByClient(client);

        return tasks.contains(task);
    }
}
