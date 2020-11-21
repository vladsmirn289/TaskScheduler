package com.scheduler.TaskScheduler.ServiceTest;

import com.scheduler.TaskScheduler.Model.Client;
import com.scheduler.TaskScheduler.Model.PeriodMode;
import com.scheduler.TaskScheduler.Model.Priority;
import com.scheduler.TaskScheduler.Model.RepeatableTask;
import com.scheduler.TaskScheduler.Service.ClientService;
import com.scheduler.TaskScheduler.Service.RepeatTaskService;
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
        "classpath:db/H2/repeatTask-test.sql",
        "classpath:db/H2/task-test.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:db/H2/after-test.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RepeatTaskServiceTest {
    @Autowired
    private RepeatTaskService repeatTaskService;

    @Autowired
    private ClientService clientService;

    @Test
    public void shouldFindTasksById() {
        RepeatableTask task = repeatTaskService.findById(200L).orElse(null);
        assertThat(task).isNotNull();
        assertThat(task.getName()).isEqualTo("RepeatableTask1");
    }

    @Test
    public void shouldFindTasksByClient() {
        Client client = clientService.findByLogin("anotherUser").orElse(null);
        assertThat(client).isNotNull();

        List<RepeatableTask> taskList = repeatTaskService.findByClient(client);
        assertThat(taskList.size()).isEqualTo(1);
    }

    @Test
    public void shouldFindTasksByNullClient() {
        Client client = clientService.findByLogin("123").orElse(null);
        assertThat(client).isNull();

        List<RepeatableTask> taskList = repeatTaskService.findByClient(client);
        assertThat(taskList.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    public void shouldSaveTask() {
        Client client = clientService.findByLogin("simpleUser").orElse(null);
        assertThat(client).isNotNull();
        assertThat(client.getRepeatableTasks().size()).isEqualTo(0);

        RepeatableTask task = new RepeatableTask("NewTask", "descript", Priority.NO,
                LocalDate.of(2020, 11, 15), LocalDate.of(2020, 11, 30), PeriodMode.EACH_DAY);
        task.setClient(client);
        repeatTaskService.save(task);

        List<RepeatableTask> found = repeatTaskService.findByClient(client);
        assertThat(found.size()).isEqualTo(1);

        assertThat(client.getRepeatableTasks().size()).isEqualTo(1);
    }

    @Test
    @Transactional
    public void shouldDeleteTask() {
        Client client = clientService.findByLogin("anotherUser").orElse(null);
        assertThat(client).isNotNull();

        List<RepeatableTask> tasks = client.getRepeatableTasks();
        assertThat(tasks.size()).isEqualTo(1);

        repeatTaskService.delete(tasks.get(0));

        List<RepeatableTask> found = repeatTaskService.findByClient(client);
        assertThat(found.size()).isEqualTo(0);

        assertThat(tasks.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    public void shouldDeleteTaskById() {
        Client client = clientService.findByLogin("anotherUser").orElse(null);
        assertThat(client).isNotNull();

        List<RepeatableTask> tasks = client.getRepeatableTasks();
        assertThat(tasks.size()).isEqualTo(1);

        repeatTaskService.deleteById(200L);

        List<RepeatableTask> found = repeatTaskService.findByClient(client);
        assertThat(found.size()).isEqualTo(0);

        assertThat(tasks.size()).isEqualTo(0);
    }
}
