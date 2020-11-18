package com.scheduler.TaskScheduler.ServiceTest;

import com.scheduler.TaskScheduler.Model.Client;
import com.scheduler.TaskScheduler.Model.Priority;
import com.scheduler.TaskScheduler.Model.Task;
import com.scheduler.TaskScheduler.Repository.TaskRepo;
import com.scheduler.TaskScheduler.Service.ClientService;
import com.scheduler.TaskScheduler.Service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@PropertySource(value = "classpath:application.properties")
@Sql(value = {
        "classpath:db/H2/client-test.sql",
        "classpath:db/H2/task-test.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:db/H2/after-test.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class TaskServiceTest {
    @Autowired
    private TaskService taskService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TaskRepo taskRepo;

    @Test
    public void shouldFindTasksByClient() {
        Client client = clientService.findByLogin("anotherUser").orElse(null);
        assertThat(client).isNotNull();

        List<Task> taskList = taskService.findByClient(client);
        assertThat(taskList.size()).isEqualTo(2);
    }

    @Test
    public void shouldFindTasksByNullClient() {
        Client client = clientService.findByLogin("123").orElse(null);
        assertThat(client).isNull();

        List<Task> taskList = taskService.findByClient(client);
        assertThat(taskList.size()).isEqualTo(0);
    }

    @Test
    public void shouldFindTasksById() {
        Task task = taskService.findById(104L).orElse(null);
        assertThat(task).isNotNull();
        assertThat(task.getName()).isEqualTo("Task2");
    }

    @Test
    public void shouldFindTasksByClientAndDate() {
        Client client = clientService.findByLogin("anotherUser").orElse(null);
        assertThat(client).isNotNull();

        List<Task> taskList = taskService.findByClientAndDate(client, LocalDate.of(2020, 11, 29));
        assertThat(taskList.size()).isEqualTo(1);
    }

    @Test
    public void shouldFindTasksByClientAndIncorrectDate() {
        Client client = clientService.findByLogin("anotherUser").orElse(null);
        assertThat(client).isNotNull();

        List<Task> taskList = taskService.findByClientAndDate(client, LocalDate.of(2000, 1, 29));
        assertThat(taskList.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    public void shouldSaveTask() {
        Client client = clientService.findByLogin("simpleUser").orElse(null);
        assertThat(client).isNotNull();
        assertThat(client.getTasks().size()).isEqualTo(1);

        Task task = new Task("NewTask", "descript", Priority.NO, LocalDate.of(2021, 1, 15), 0);
        task.setClient(client);
        taskService.save(task);

        List<Task> found = taskService.findByClient(client);
        assertThat(found.size()).isEqualTo(2);

        assertThat(client.getTasks().size()).isEqualTo(2);
    }

    @Test
    @Transactional
    public void shouldDeleteTask() {
        Client client = clientService.findByLogin("simpleUser").orElse(null);
        assertThat(client).isNotNull();

        List<Task> tasks = client.getTasks();
        assertThat(tasks.size()).isEqualTo(1);

        taskService.delete(tasks.get(0));

        List<Task> found = taskService.findByClient(client);
        assertThat(found.size()).isEqualTo(0);

        assertThat(tasks.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    public void shouldDeleteTaskById() {
        Client client = clientService.findByLogin("simpleUser").orElse(null);
        assertThat(client).isNotNull();

        List<Task> tasks = client.getTasks();
        assertThat(tasks.size()).isEqualTo(1);

        taskService.deleteById(103L);

        List<Task> found = taskService.findByClient(client);
        assertThat(found.size()).isEqualTo(0);

        assertThat(tasks.size()).isEqualTo(0);
    }
}
