package com.scheduler.TaskScheduler.UtilTest;

import com.scheduler.TaskScheduler.DTO.PeriodParameters;
import com.scheduler.TaskScheduler.Model.PeriodMode;
import com.scheduler.TaskScheduler.Model.Priority;
import com.scheduler.TaskScheduler.Model.RepeatableTask;
import com.scheduler.TaskScheduler.Model.Task;
import com.scheduler.TaskScheduler.Util.PeriodFacade;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PeriodFacadeTest {
    private final RepeatableTask repeatableTask =
            new RepeatableTask("RT1", "RD1", Priority.MEDIUM,
                    LocalDate.of(2020, 11, 1), LocalDate.of(2020, 11, 4),
                    PeriodMode.EACH_DAY);

    private PeriodFacade periodFacade = new PeriodFacade(repeatableTask, new PeriodParameters());

    @Test
    public void shouldInitTasksWithEachDayPeriod() {
        RepeatableTask rt = periodFacade.initTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(4);

        Task task1 = tasks.get(0);
        assertThat(task1.getDate()).isEqualTo(LocalDate.of(2020, 11, 1));
        assertThat(task1.getName()).isEqualTo("RT1");
        assertThat(task1.getDescription()).isEqualTo("RD1");
        assertThat(task1.getPriority()).isEqualTo(Priority.MEDIUM);
        assertThat(task1.getProgress()).isEqualTo(0);

        Task task2 = tasks.get(1);
        assertThat(task2.getDate()).isEqualTo(LocalDate.of(2020, 11, 2));
        assertThat(task2.getName()).isEqualTo("RT1");
        assertThat(task2.getDescription()).isEqualTo("RD1");
        assertThat(task2.getPriority()).isEqualTo(Priority.MEDIUM);
        assertThat(task2.getProgress()).isEqualTo(0);

        Task task3 = tasks.get(2);
        assertThat(task3.getDate()).isEqualTo(LocalDate.of(2020, 11, 3));
        assertThat(task3.getName()).isEqualTo("RT1");
        assertThat(task3.getDescription()).isEqualTo("RD1");
        assertThat(task3.getPriority()).isEqualTo(Priority.MEDIUM);
        assertThat(task3.getProgress()).isEqualTo(0);

        Task task4 = tasks.get(3);
        assertThat(task4.getDate()).isEqualTo(LocalDate.of(2020, 11, 4));
        assertThat(task4.getName()).isEqualTo("RT1");
        assertThat(task4.getDescription()).isEqualTo("RD1");
        assertThat(task4.getPriority()).isEqualTo(Priority.MEDIUM);
        assertThat(task4.getProgress()).isEqualTo(0);
    }

    @Test
    public void shouldUpdateTasksWithEachDayPeriod() {
        repeatableTask.setEndDate(LocalDate.of(2020, 11, 2));
        periodFacade.setRepeatableTask(repeatableTask);
        RepeatableTask rt = periodFacade.initTasks();

        rt.setDescription("New description");
        rt.setName("New name");
        rt.setPriority(Priority.HIGH);

        periodFacade.setRepeatableTask(rt);
        rt = periodFacade.updateTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(2);

        Task task1 = tasks.get(0);
        assertThat(task1.getDate()).isEqualTo(LocalDate.of(2020, 11, 1));
        assertThat(task1.getName()).isEqualTo("New name");
        assertThat(task1.getDescription()).isEqualTo("New description");
        assertThat(task1.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(task1.getProgress()).isEqualTo(0);

        Task task2 = tasks.get(1);
        assertThat(task2.getDate()).isEqualTo(LocalDate.of(2020, 11, 2));
        assertThat(task2.getName()).isEqualTo("New name");
        assertThat(task2.getDescription()).isEqualTo("New description");
        assertThat(task2.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(task2.getProgress()).isEqualTo(0);
    }

    @Test
    public void shouldUpdateTasksWithEachDayPeriodAndDateDecrementing() {
        RepeatableTask rt = periodFacade.initTasks();
        rt.setDescription("New description");
        rt.setName("New name");
        rt.setPriority(Priority.HIGH);
        rt.setEndDate(LocalDate.of(2020, 11, 2));

        periodFacade.setRepeatableTask(rt);
        rt = periodFacade.updateTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(2);

        Task task1 = tasks.get(0);
        assertThat(task1.getDate()).isEqualTo(LocalDate.of(2020, 11, 1));
        assertThat(task1.getName()).isEqualTo("New name");
        assertThat(task1.getDescription()).isEqualTo("New description");
        assertThat(task1.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(task1.getProgress()).isEqualTo(0);

        Task task2 = tasks.get(1);
        assertThat(task2.getDate()).isEqualTo(LocalDate.of(2020, 11, 2));
        assertThat(task2.getName()).isEqualTo("New name");
        assertThat(task2.getDescription()).isEqualTo("New description");
        assertThat(task2.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(task2.getProgress()).isEqualTo(0);
    }

    @Test
    public void shouldUpdateTasksWithEachDayPeriodAndDateIncrementing() {
        repeatableTask.setEndDate(LocalDate.of(2020, 11, 1));
        RepeatableTask rt = periodFacade.initTasks();
        rt.setDescription("New description");
        rt.setName("New name");
        rt.setPriority(Priority.HIGH);
        rt.setEndDate(LocalDate.of(2020, 11, 2));

        periodFacade.setRepeatableTask(rt);
        rt = periodFacade.updateTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(2);

        Task task1 = tasks.get(0);
        assertThat(task1.getDate()).isEqualTo(LocalDate.of(2020, 11, 1));
        assertThat(task1.getName()).isEqualTo("New name");
        assertThat(task1.getDescription()).isEqualTo("New description");
        assertThat(task1.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(task1.getProgress()).isEqualTo(0);

        Task task2 = tasks.get(1);
        assertThat(task2.getDate()).isEqualTo(LocalDate.of(2020, 11, 2));
        assertThat(task2.getName()).isEqualTo("New name");
        assertThat(task2.getDescription()).isEqualTo("New description");
        assertThat(task2.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(task2.getProgress()).isEqualTo(0);
    }
}
