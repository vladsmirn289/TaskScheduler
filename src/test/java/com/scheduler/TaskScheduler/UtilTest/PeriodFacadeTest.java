package com.scheduler.TaskScheduler.UtilTest;

import com.scheduler.TaskScheduler.Model.PeriodMode;
import com.scheduler.TaskScheduler.Model.Priority;
import com.scheduler.TaskScheduler.Model.RepeatableTask;
import com.scheduler.TaskScheduler.Model.Task;
import com.scheduler.TaskScheduler.Service.TaskService;
import com.scheduler.TaskScheduler.Util.PeriodFacade;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PeriodFacadeTest {
    @MockBean
    private TaskService taskService;

    @Autowired
    private PeriodFacade periodFacade;

    private final RepeatableTask repeatableTask =
            new RepeatableTask("RT1", "RD1", Priority.MEDIUM,
                    LocalDate.of(2020, 11, 1), LocalDate.of(2020, 11, 4),
                    PeriodMode.EACH_DAY);

    @Test
    public void shouldInitTasksWithEachDayPeriod() {
        RepeatableTask rt = periodFacade.initTasks(repeatableTask);
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
        RepeatableTask rt = periodFacade.initTasks(repeatableTask);

        Mockito.doReturn(rt.getTasks())
                .when(taskService)
                .findAllByRepeatableTask(rt);

        rt.setDescription("New description");
        rt.setName("New name");
        rt.setPriority(Priority.HIGH);

        rt = periodFacade.updateTasks(repeatableTask, taskService);
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
    public void shouldUpdateTasksWithEachDayPeriodAndDateChanging() {
        RepeatableTask rt = periodFacade.initTasks(repeatableTask);

        Mockito.doReturn(rt.getTasks())
                .when(taskService)
                .findAllByRepeatableTask(rt);

        rt.setDescription("New description");
        rt.setName("New name");
        rt.setPriority(Priority.HIGH);
        rt.setEndDate(LocalDate.of(2020, 11, 2));

        rt = periodFacade.updateTasks(repeatableTask, taskService);
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
