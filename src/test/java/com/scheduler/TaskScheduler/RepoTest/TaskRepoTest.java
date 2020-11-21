package com.scheduler.TaskScheduler.RepoTest;

import com.scheduler.TaskScheduler.Model.Client;
import com.scheduler.TaskScheduler.Model.Priority;
import com.scheduler.TaskScheduler.Model.RepeatableTask;
import com.scheduler.TaskScheduler.Model.Task;
import com.scheduler.TaskScheduler.Repository.ClientRepo;
import com.scheduler.TaskScheduler.Repository.RepeatTaskRepo;
import com.scheduler.TaskScheduler.Repository.TaskRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@PropertySource(value = "classpath:application.properties")
@Sql(value = {
        "classpath:db/H2/client-test.sql",
        "classpath:db/H2/repeatTask-test.sql",
        "classpath:db/H2/task-test.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class TaskRepoTest {
    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private RepeatTaskRepo repeatTaskRepo;

    @Test
    public void shouldFindTasksByClient() {
        Client client = clientRepo.findById(102L).orElse(null);
        assertThat(client).isNotNull();
        List<Task> taskList = taskRepo.findByClient(client);

        assertThat(taskList).isNotNull();
        assertThat(taskList.size()).isEqualTo(2);

        Task task1 = taskList.get(0);
        assertThat(task1.getId()).isEqualTo(105L);
        assertThat(task1.getName()).isEqualTo("Task3");
        assertThat(task1.getDate()).isEqualTo(LocalDate.of(2020, 11, 23));
        assertThat(task1.getClient()).isEqualTo(client);
        assertThat(task1.getDescription()).isEqualTo("This is a description of task3");
        assertThat(task1.getPriority()).isEqualTo(Priority.HIGH);

        Task task2 = taskList.get(1);
        assertThat(task2.getId()).isEqualTo(106L);
        assertThat(task2.getName()).isEqualTo("Task4");
        assertThat(task2.getDate()).isEqualTo(LocalDate.of(2020, 11, 29));
        assertThat(task2.getClient()).isEqualTo(client);
        assertThat(task2.getDescription()).isEqualTo("This is a description of task4");
        assertThat(task2.getPriority()).isEqualTo(Priority.MEDIUM);
    }

    @Test
    public void shouldFindTasksByClientAndDate() {
        Client client = clientRepo.findById(102L).orElse(null);
        assertThat(client).isNotNull();
        List<Task> taskList = taskRepo.findByClientAndDate(client, LocalDate.of(2020, 11, 23));

        assertThat(taskList).isNotNull();
        assertThat(taskList.size()).isEqualTo(1);

        Task task1 = taskList.get(0);
        assertThat(task1.getId()).isEqualTo(105L);
        assertThat(task1.getName()).isEqualTo("Task3");
        assertThat(task1.getDate()).isEqualTo(LocalDate.of(2020, 11, 23));
        assertThat(task1.getClient()).isEqualTo(client);
        assertThat(task1.getDescription()).isEqualTo("This is a description of task3");
        assertThat(task1.getPriority()).isEqualTo(Priority.HIGH);
    }

    @Test
    public void shouldFindTasksByRepeatableTask() {
        RepeatableTask repeatableTask = repeatTaskRepo.findById(200L).get();
        List<Task> tasks = taskRepo.findAllByRepeatableTask(repeatableTask);
        assertThat(tasks.size()).isEqualTo(1);

        Task task = tasks.get(0);
        assertThat(task.getName()).isEqualTo("RTask5");
    }
}
