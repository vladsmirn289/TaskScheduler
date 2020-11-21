package com.scheduler.TaskScheduler.RepoTest;

import com.scheduler.TaskScheduler.Model.*;
import com.scheduler.TaskScheduler.Repository.ClientRepo;
import com.scheduler.TaskScheduler.Repository.RepeatTaskRepo;
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
public class RepeatTaskRepoTest {
    @Autowired
    private RepeatTaskRepo repeatTaskRepo;

    @Autowired
    private ClientRepo clientRepo;

    @Test
    public void shouldFindTasksByClient() {
        Client client = clientRepo.findById(102L).orElse(null);
        assertThat(client).isNotNull();
        List<RepeatableTask> taskList = repeatTaskRepo.findByClient(client);

        assertThat(taskList).isNotNull();
        assertThat(taskList.size()).isEqualTo(1);

        RepeatableTask task1 = taskList.get(0);
        assertThat(task1.getId()).isEqualTo(200L);
        assertThat(task1.getName()).isEqualTo("RepeatableTask1");
        assertThat(task1.getClient()).isEqualTo(client);
        assertThat(task1.getDescription()).isEqualTo("This is a description of the repeatable task1");
        assertThat(task1.getPriority()).isEqualTo(Priority.LOW);
        assertThat(task1.getPeriodMode()).isEqualTo(PeriodMode.EACH_DAY);
        assertThat(task1.getStartDate()).isEqualTo(LocalDate.of(2020, 11, 1));
        assertThat(task1.getEndDate()).isEqualTo(LocalDate.of(2020, 12, 1));
    }
}
