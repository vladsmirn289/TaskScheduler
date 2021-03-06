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

    private final PeriodFacade periodFacade = new PeriodFacade(repeatableTask, new PeriodParameters());

    private void checkParams(Task task, LocalDate date, String name, String description, Priority priority, int progress) {
        assertThat(task.getDate()).isEqualTo(date);
        assertThat(task.getName()).isEqualTo(name);
        assertThat(task.getDescription()).isEqualTo(description);
        assertThat(task.getPriority()).isEqualTo(priority);
        assertThat(task.getProgress()).isEqualTo(progress);
    }

    @Test
    public void shouldInitTasksWithEachDayPeriod() {
        RepeatableTask rt = periodFacade.initTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(4);

        Task task1 = tasks.get(0);
        checkParams(task1, LocalDate.of(2020, 11, 1),
                "RT1", "RD1", Priority.MEDIUM, 0);

        Task task2 = tasks.get(1);
        checkParams(task2, LocalDate.of(2020, 11, 2),
                "RT1", "RD1", Priority.MEDIUM, 0);

        Task task3 = tasks.get(2);
        checkParams(task3, LocalDate.of(2020, 11, 3),
                "RT1", "RD1", Priority.MEDIUM, 0);

        Task task4 = tasks.get(3);
        checkParams(task4, LocalDate.of(2020, 11, 4),
                "RT1", "RD1", Priority.MEDIUM, 0);
    }

    @Test
    public void shouldUpdateTasksWithEachDayPeriod() {
        repeatableTask.setEndDate(LocalDate.of(2020, 11, 2));
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
        checkParams(task1, LocalDate.of(2020, 11, 1),
                "New name", "New description", Priority.HIGH, 0);

        Task task2 = tasks.get(1);
        checkParams(task2, LocalDate.of(2020, 11, 2),
                "New name", "New description", Priority.HIGH, 0);
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
        checkParams(task1, LocalDate.of(2020, 11, 1),
                "New name", "New description", Priority.HIGH, 0);

        Task task2 = tasks.get(1);
        checkParams(task2, LocalDate.of(2020, 11, 2),
                "New name", "New description", Priority.HIGH, 0);
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
        checkParams(task1, LocalDate.of(2020, 11, 1),
                "New name", "New description", Priority.HIGH, 0);

        Task task2 = tasks.get(1);
        checkParams(task2, LocalDate.of(2020, 11, 2),
                "New name", "New description", Priority.HIGH, 0);
    }

    @Test
    public void shouldInitTasksWithEachWeekPeriod() {
        PeriodParameters periodParameters = new PeriodParameters();
        periodParameters.setSunday(true);
        periodParameters.setThursday(true);
        periodParameters.setSaturday(true);

        repeatableTask.setEndDate(LocalDate.of(2020, 11, 8));
        repeatableTask.setPeriodMode(PeriodMode.EACH_WEEK);

        periodFacade.setRepeatableTask(repeatableTask);
        periodFacade.setPeriodParameters(periodParameters);
        RepeatableTask rt = periodFacade.initTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(4);

        Task task1 = tasks.get(0);
        checkParams(task1, LocalDate.of(2020, 11, 1),
                "RT1", "RD1", Priority.MEDIUM, 0);

        Task task2 = tasks.get(1);
        checkParams(task2, LocalDate.of(2020, 11, 5),
                "RT1", "RD1", Priority.MEDIUM, 0);

        Task task3 = tasks.get(2);
        checkParams(task3, LocalDate.of(2020, 11, 7),
                "RT1", "RD1", Priority.MEDIUM, 0);

        Task task4 = tasks.get(3);
        checkParams(task4, LocalDate.of(2020, 11, 8),
                "RT1", "RD1", Priority.MEDIUM, 0);
    }

    @Test
    public void shouldUpdateTasksWithEachWeekPeriod() {
        PeriodParameters periodParameters = new PeriodParameters();
        periodParameters.setMonday(true);
        periodParameters.setWednesday(true);

        repeatableTask.setEndDate(LocalDate.of(2020, 11, 9));
        repeatableTask.setPeriodMode(PeriodMode.EACH_WEEK);
        RepeatableTask rt = periodFacade.initTasks();

        PeriodParameters periodParameters2 = new PeriodParameters();
        periodParameters2.setTuesday(true);
        periodParameters2.setFriday(true);
        periodParameters2.setSunday(true);

        rt.setDescription("New description");
        rt.setName("New name");
        rt.setPriority(Priority.HIGH);

        periodFacade.setRepeatableTask(rt);
        periodFacade.setPeriodParameters(periodParameters2);
        rt = periodFacade.updateTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(4);

        Task task1 = tasks.get(0);
        checkParams(task1, LocalDate.of(2020, 11, 1),
                "New name", "New description", Priority.HIGH, 0);

        Task task2 = tasks.get(1);
        checkParams(task2, LocalDate.of(2020, 11, 3),
                "New name", "New description", Priority.HIGH, 0);

        Task task3 = tasks.get(2);
        checkParams(task3, LocalDate.of(2020, 11, 6),
                "New name", "New description", Priority.HIGH, 0);

        Task task4 = tasks.get(3);
        checkParams(task4, LocalDate.of(2020, 11, 8),
                "New name", "New description", Priority.HIGH, 0);
    }

    @Test
    public void shouldUpdateTasksWithEachWeekPeriodAndDateDecrementing() {
        PeriodParameters periodParameters = new PeriodParameters();
        periodParameters.setMonday(true);
        periodParameters.setWednesday(true);

        repeatableTask.setEndDate(LocalDate.of(2020, 11, 9));
        repeatableTask.setPeriodMode(PeriodMode.EACH_WEEK);
        periodFacade.setPeriodParameters(periodParameters);
        RepeatableTask rt = periodFacade.initTasks();
        rt.setDescription("New description");
        rt.setName("New name");
        rt.setPriority(Priority.HIGH);
        rt.setEndDate(LocalDate.of(2020, 11, 4));

        periodParameters.setMonday(false);
        periodParameters.setTuesday(true);
        periodFacade.setRepeatableTask(rt);
        rt = periodFacade.updateTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(2);

        Task task1 = tasks.get(0);
        checkParams(task1, LocalDate.of(2020, 11, 3),
                "New name", "New description", Priority.HIGH, 0);

        Task task2 = tasks.get(1);
        checkParams(task2, LocalDate.of(2020, 11, 4),
                "New name", "New description", Priority.HIGH, 0);
    }

    @Test
    public void shouldUpdateTasksWithEachWeekPeriodAndDateIncrementing() {
        PeriodParameters periodParameters = new PeriodParameters();
        periodParameters.setMonday(true);
        periodParameters.setWednesday(true);

        repeatableTask.setEndDate(LocalDate.of(2020, 11, 9));
        repeatableTask.setPeriodMode(PeriodMode.EACH_WEEK);
        periodFacade.setPeriodParameters(periodParameters);
        RepeatableTask rt = periodFacade.initTasks();
        rt.setDescription("New description");
        rt.setName("New name");
        rt.setPriority(Priority.HIGH);
        rt.setEndDate(LocalDate.of(2020, 11, 14));

        periodParameters.setMonday(false);
        periodParameters.setThursday(true);
        periodFacade.setRepeatableTask(rt);
        rt = periodFacade.updateTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(4);

        Task task1 = tasks.get(0);
        checkParams(task1, LocalDate.of(2020, 11, 4),
                "New name", "New description", Priority.HIGH, 0);

        Task task2 = tasks.get(1);
        checkParams(task2, LocalDate.of(2020, 11, 5),
                "New name", "New description", Priority.HIGH, 0);

        Task task3 = tasks.get(2);
        checkParams(task3, LocalDate.of(2020, 11, 11),
                "New name", "New description", Priority.HIGH, 0);

        Task task4 = tasks.get(3);
        checkParams(task4, LocalDate.of(2020, 11, 12),
                "New name", "New description", Priority.HIGH, 0);
    }

    @Test
    public void shouldInitTasksWithEachDayOfMonthPeriod() {
        PeriodParameters periodParameters = new PeriodParameters();
        periodParameters.setDayOfMonth(31);

        repeatableTask.setStartDate(LocalDate.of(2020, 1, 8));
        repeatableTask.setEndDate(LocalDate.of(2020, 5, 30));
        repeatableTask.setPeriodMode(PeriodMode.EACH_DAY_OF_MONTH);

        periodFacade.setRepeatableTask(repeatableTask);
        periodFacade.setPeriodParameters(periodParameters);
        RepeatableTask rt = periodFacade.initTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(2);

        Task task1 = tasks.get(0);
        checkParams(task1, LocalDate.of(2020, 1, 31),
                "RT1", "RD1", Priority.MEDIUM, 0);

        Task task2 = tasks.get(1);
        checkParams(task2, LocalDate.of(2020, 3, 31),
                "RT1", "RD1", Priority.MEDIUM, 0);

        periodParameters.setDayOfMonth(5);
        rt = periodFacade.initTasks();
        tasks = rt.getTasks();
        assertThat(tasks.size()).isEqualTo(4);

        task1 = tasks.get(0);
        checkParams(task1, LocalDate.of(2020, 2, 5),
                "RT1", "RD1", Priority.MEDIUM, 0);

        task2 = tasks.get(1);
        checkParams(task2, LocalDate.of(2020, 3, 5),
                "RT1", "RD1", Priority.MEDIUM, 0);

        Task task3 = tasks.get(2);
        checkParams(task3, LocalDate.of(2020, 4, 5),
                "RT1", "RD1", Priority.MEDIUM, 0);

        Task task4 = tasks.get(3);
        checkParams(task4, LocalDate.of(2020, 5, 5),
                "RT1", "RD1", Priority.MEDIUM, 0);
    }

    @Test
    public void shouldUpdateTasksWithEachDayOfMonthPeriod() {
        PeriodParameters periodParameters = new PeriodParameters();
        periodParameters.setDayOfMonth(5);

        repeatableTask.setStartDate(LocalDate.of(2020, 1, 30));
        repeatableTask.setEndDate(LocalDate.of(2020, 5, 9));
        repeatableTask.setPeriodMode(PeriodMode.EACH_DAY_OF_MONTH);
        RepeatableTask rt = periodFacade.initTasks();

        PeriodParameters periodParameters2 = new PeriodParameters();
        periodParameters2.setDayOfMonth(9);

        rt.setDescription("New description");
        rt.setName("New name");
        rt.setPriority(Priority.HIGH);

        periodFacade.setRepeatableTask(rt);
        periodFacade.setPeriodParameters(periodParameters2);
        rt = periodFacade.updateTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(4);

        Task task1 = tasks.get(0);
        checkParams(task1, LocalDate.of(2020, 2, 9),
                "New name", "New description", Priority.HIGH, 0);

        Task task2 = tasks.get(1);
        checkParams(task2, LocalDate.of(2020, 3, 9),
                "New name", "New description", Priority.HIGH, 0);

        Task task3 = tasks.get(2);
        checkParams(task3, LocalDate.of(2020, 4, 9),
                "New name", "New description", Priority.HIGH, 0);

        Task task4 = tasks.get(3);
        checkParams(task4, LocalDate.of(2020, 5, 9),
                "New name", "New description", Priority.HIGH, 0);
    }

    @Test
    public void shouldUpdateTasksWithEachDayOfMonthPeriodAndDateDecrementing() {
        PeriodParameters periodParameters = new PeriodParameters();
        periodParameters.setDayOfMonth(31);

        repeatableTask.setStartDate(LocalDate.of(2020, 1, 5));
        repeatableTask.setEndDate(LocalDate.of(2020, 5, 31));
        repeatableTask.setPeriodMode(PeriodMode.EACH_DAY_OF_MONTH);
        periodFacade.setRepeatableTask(repeatableTask);
        periodFacade.setPeriodParameters(periodParameters);
        RepeatableTask rt = periodFacade.initTasks();
        rt.setDescription("New description");
        rt.setName("New name");
        rt.setPriority(Priority.HIGH);
        rt.setEndDate(LocalDate.of(2020, 3, 4));

        periodFacade.setRepeatableTask(rt);
        rt = periodFacade.updateTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(1);

        Task task1 = tasks.get(0);
        checkParams(task1, LocalDate.of(2020, 1, 31),
                "New name", "New description", Priority.HIGH, 0);
    }

    @Test
    public void shouldUpdateTasksWithEachDayOfMonthPeriodAndDateIncrementing() {
        PeriodParameters periodParameters = new PeriodParameters();
        periodParameters.setDayOfMonth(31);

        repeatableTask.setStartDate(LocalDate.of(2020, 1, 5));
        repeatableTask.setEndDate(LocalDate.of(2020, 5, 31));
        repeatableTask.setPeriodMode(PeriodMode.EACH_DAY_OF_MONTH);
        periodFacade.setRepeatableTask(repeatableTask);
        periodFacade.setPeriodParameters(periodParameters);
        RepeatableTask rt = periodFacade.initTasks();
        rt.setDescription("New description");
        rt.setName("New name");
        rt.setPriority(Priority.HIGH);
        rt.setEndDate(LocalDate.of(2020, 8, 31));

        periodFacade.setRepeatableTask(rt);
        rt = periodFacade.updateTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(5);

        Task task1 = tasks.get(0);
        checkParams(task1, LocalDate.of(2020, 1, 31),
                "New name", "New description", Priority.HIGH, 0);

        Task task2 = tasks.get(1);
        checkParams(task2, LocalDate.of(2020, 3, 31),
                "New name", "New description", Priority.HIGH, 0);

        Task task3 = tasks.get(2);
        checkParams(task3, LocalDate.of(2020, 5, 31),
                "New name", "New description", Priority.HIGH, 0);

        Task task4 = tasks.get(3);
        checkParams(task4, LocalDate.of(2020, 7, 31),
                "New name", "New description", Priority.HIGH, 0);

        Task task5 = tasks.get(4);
        checkParams(task5, LocalDate.of(2020, 8, 31),
                "New name", "New description", Priority.HIGH, 0);
    }

    @Test
    public void shouldInitTasksWithEachWeekOfMonthPeriod() {
        PeriodParameters periodParameters = new PeriodParameters();
        periodParameters.setMonthWeek("third");

        repeatableTask.setStartDate(LocalDate.of(2020, 11, 1));
        repeatableTask.setEndDate(LocalDate.of(2020, 11, 30));
        repeatableTask.setPeriodMode(PeriodMode.EACH_WEEK_OF_MONTH);

        periodFacade.setRepeatableTask(repeatableTask);
        periodFacade.setPeriodParameters(periodParameters);
        RepeatableTask rt = periodFacade.initTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(7);

        Task first = tasks.get(0);
        checkParams(first, LocalDate.of(2020, 11, 9),
                "RT1", "RD1", Priority.MEDIUM, 0);

        Task last = tasks.get(6);
        checkParams(last, LocalDate.of(2020, 11, 15),
                "RT1", "RD1", Priority.MEDIUM, 0);

        periodParameters.setMonthWeek("first");
        rt = periodFacade.initTasks();
        tasks = rt.getTasks();
        assertThat(tasks.size()).isEqualTo(1);

        first = tasks.get(0);
        checkParams(first, LocalDate.of(2020, 11, 1),
                "RT1", "RD1", Priority.MEDIUM, 0);

        periodParameters.setMonthWeek("sixth");
        repeatableTask.setStartDate(LocalDate.of(2020, 12, 1));
        repeatableTask.setStartDate(LocalDate.of(2020, 12, 31));
        periodFacade.setRepeatableTask(repeatableTask);
        rt = periodFacade.initTasks();
        tasks = rt.getTasks();
        assertThat(tasks.size()).isEqualTo(0);
    }

    @Test
    public void shouldUpdateTasksWithEachWeekOfMonthPeriod() {
        PeriodParameters periodParameters = new PeriodParameters();
        periodParameters.setMonthWeek("second");

        repeatableTask.setStartDate(LocalDate.of(2020, 1, 1));
        repeatableTask.setEndDate(LocalDate.of(2020, 1, 31));
        repeatableTask.setPeriodMode(PeriodMode.EACH_WEEK_OF_MONTH);
        RepeatableTask rt = periodFacade.initTasks();

        PeriodParameters periodParameters2 = new PeriodParameters();
        periodParameters2.setMonthWeek("third");

        rt.setDescription("New description");
        rt.setName("New name");
        rt.setPriority(Priority.HIGH);

        periodFacade.setRepeatableTask(rt);
        periodFacade.setPeriodParameters(periodParameters2);
        rt = periodFacade.updateTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(7);

        Task first = tasks.get(0);
        checkParams(first, LocalDate.of(2020, 1, 13),
                "New name", "New description", Priority.HIGH, 0);

        Task last = tasks.get(6);
        checkParams(last, LocalDate.of(2020, 1, 19),
                "New name", "New description", Priority.HIGH, 0);
    }

    @Test
    public void shouldUpdateTasksWithEachWeekOfMonthPeriodAndDateDecrementing() {
        PeriodParameters periodParameters = new PeriodParameters();
        periodParameters.setMonthWeek("second");

        repeatableTask.setStartDate(LocalDate.of(2020, 1, 1));
        repeatableTask.setEndDate(LocalDate.of(2020, 1, 31));
        repeatableTask.setPeriodMode(PeriodMode.EACH_WEEK_OF_MONTH);
        periodFacade.setRepeatableTask(repeatableTask);
        periodFacade.setPeriodParameters(periodParameters);
        RepeatableTask rt = periodFacade.initTasks();
        rt.setDescription("New description");
        rt.setName("New name");
        rt.setPriority(Priority.HIGH);
        rt.setEndDate(LocalDate.of(2020, 1, 9));

        periodFacade.setRepeatableTask(rt);
        rt = periodFacade.updateTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(4);

        Task first = tasks.get(0);
        checkParams(first, LocalDate.of(2020, 1, 6),
                "New name", "New description", Priority.HIGH, 0);

        Task last = tasks.get(3);
        checkParams(last, LocalDate.of(2020, 1, 9),
                "New name", "New description", Priority.HIGH, 0);

        rt.setEndDate(LocalDate.of(2020, 1, 5));
        periodFacade.setRepeatableTask(rt);
        rt = periodFacade.updateTasks();
        tasks = rt.getTasks();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(0);
    }

    @Test
    public void shouldUpdateTasksWithEachWeekOfMonthPeriodAndDateIncrementing() {
        PeriodParameters periodParameters = new PeriodParameters();
        periodParameters.setMonthWeek("second");

        repeatableTask.setStartDate(LocalDate.of(2020, 1, 1));
        repeatableTask.setEndDate(LocalDate.of(2020, 1, 31));
        repeatableTask.setPeriodMode(PeriodMode.EACH_WEEK_OF_MONTH);
        periodFacade.setRepeatableTask(repeatableTask);
        periodFacade.setPeriodParameters(periodParameters);
        RepeatableTask rt = periodFacade.initTasks();
        rt.setDescription("New description");
        rt.setName("New name");
        rt.setPriority(Priority.HIGH);
        rt.setEndDate(LocalDate.of(2020, 2, 29));

        periodFacade.setRepeatableTask(rt);
        rt = periodFacade.updateTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(14);

        Task firstDayOfFirstWeek = tasks.get(0);
        checkParams(firstDayOfFirstWeek, LocalDate.of(2020, 1, 6),
                "New name", "New description", Priority.HIGH, 0);

        Task lastDayOfFirstWeek = tasks.get(6);
        checkParams(lastDayOfFirstWeek, LocalDate.of(2020, 1, 12),
                "New name", "New description", Priority.HIGH, 0);

        Task firstDayOfLastWeek = tasks.get(7);
        checkParams(firstDayOfLastWeek, LocalDate.of(2020, 2, 3),
                "New name", "New description", Priority.HIGH, 0);

        Task lastDayOfLastWeek = tasks.get(13);
        checkParams(lastDayOfLastWeek, LocalDate.of(2020, 2, 9),
                "New name", "New description", Priority.HIGH, 0);
    }

    @Test
    public void shouldInitTasksWithEachDayOfWeekOfMonthPeriod() {
        PeriodParameters periodParameters = new PeriodParameters();
        periodParameters.setNumberDayOfWeek("third");
        periodParameters.setDayOfWeek("tuesday");

        repeatableTask.setStartDate(LocalDate.of(2020, 11, 1));
        repeatableTask.setEndDate(LocalDate.of(2020, 12, 31));
        repeatableTask.setPeriodMode(PeriodMode.EACH_DAY_OF_WEEK_OF_MONTH);

        periodFacade.setRepeatableTask(repeatableTask);
        periodFacade.setPeriodParameters(periodParameters);
        RepeatableTask rt = periodFacade.initTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(2);

        Task task1 = tasks.get(0);
        checkParams(task1, LocalDate.of(2020, 11, 17),
                "RT1", "RD1", Priority.MEDIUM, 0);

        Task task2 = tasks.get(1);
        checkParams(task2, LocalDate.of(2020, 12, 15),
                "RT1", "RD1", Priority.MEDIUM, 0);

        periodParameters.setNumberDayOfWeek("fifth");
        periodParameters.setDayOfWeek("wednesday");
        rt = periodFacade.initTasks();
        tasks = rt.getTasks();
        assertThat(tasks.size()).isEqualTo(1);

        Task task = tasks.get(0);
        checkParams(task, LocalDate.of(2020, 12, 30),
                "RT1", "RD1", Priority.MEDIUM, 0);
    }

    @Test
    public void shouldUpdateTasksWithEachDayOfWeekOfMonthPeriod() {
        PeriodParameters periodParameters = new PeriodParameters();
        periodParameters.setNumberDayOfWeek("third");
        periodParameters.setDayOfWeek("tuesday");

        repeatableTask.setStartDate(LocalDate.of(2020, 11, 1));
        repeatableTask.setEndDate(LocalDate.of(2020, 12, 31));
        repeatableTask.setPeriodMode(PeriodMode.EACH_DAY_OF_WEEK_OF_MONTH);
        RepeatableTask rt = periodFacade.initTasks();

        PeriodParameters periodParameters2 = new PeriodParameters();
        periodParameters2.setNumberDayOfWeek("fourth");
        periodParameters2.setDayOfWeek("monday");

        rt.setDescription("New description");
        rt.setName("New name");
        rt.setPriority(Priority.HIGH);

        periodFacade.setRepeatableTask(rt);
        periodFacade.setPeriodParameters(periodParameters2);
        rt = periodFacade.updateTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(2);

        Task task1 = tasks.get(0);
        checkParams(task1, LocalDate.of(2020, 11, 23),
                "New name", "New description", Priority.HIGH, 0);

        Task task2 = tasks.get(1);
        checkParams(task2, LocalDate.of(2020, 12, 28),
                "New name", "New description", Priority.HIGH, 0);
    }

    @Test
    public void shouldUpdateTasksWithEachDayOfWeekOfMonthPeriodAndDateDecrementing() {
        PeriodParameters periodParameters = new PeriodParameters();
        periodParameters.setNumberDayOfWeek("third");
        periodParameters.setDayOfWeek("tuesday");

        repeatableTask.setStartDate(LocalDate.of(2020, 11, 1));
        repeatableTask.setEndDate(LocalDate.of(2020, 12, 31));
        repeatableTask.setPeriodMode(PeriodMode.EACH_DAY_OF_WEEK_OF_MONTH);
        periodFacade.setRepeatableTask(repeatableTask);
        periodFacade.setPeriodParameters(periodParameters);
        RepeatableTask rt = periodFacade.initTasks();
        rt.setDescription("New description");
        rt.setName("New name");
        rt.setPriority(Priority.HIGH);
        rt.setEndDate(LocalDate.of(2020, 11, 30));

        periodFacade.setRepeatableTask(rt);
        rt = periodFacade.updateTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(1);

        Task first = tasks.get(0);
        checkParams(first, LocalDate.of(2020, 11, 17),
                "New name", "New description", Priority.HIGH, 0);

        periodParameters.setNumberDayOfWeek("fifth");
        periodParameters.setDayOfWeek("tuesday");
        rt = periodFacade.updateTasks();
        tasks = rt.getTasks();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(0);
    }

    @Test
    public void shouldUpdateTasksWithEachDayOfWeekOfMonthPeriodAndDateIncrementing() {
        PeriodParameters periodParameters = new PeriodParameters();
        periodParameters.setNumberDayOfWeek("third");
        periodParameters.setDayOfWeek("tuesday");

        repeatableTask.setStartDate(LocalDate.of(2020, 11, 1));
        repeatableTask.setEndDate(LocalDate.of(2020, 11, 30));
        repeatableTask.setPeriodMode(PeriodMode.EACH_DAY_OF_WEEK_OF_MONTH);
        periodFacade.setRepeatableTask(repeatableTask);
        periodFacade.setPeriodParameters(periodParameters);
        RepeatableTask rt = periodFacade.initTasks();
        rt.setDescription("New description");
        rt.setName("New name");
        rt.setPriority(Priority.HIGH);
        rt.setEndDate(LocalDate.of(2020, 12, 31));

        periodFacade.setRepeatableTask(rt);
        rt = periodFacade.updateTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(2);

        Task task1 = tasks.get(0);
        checkParams(task1, LocalDate.of(2020, 11, 17),
                "New name", "New description", Priority.HIGH, 0);

        Task task2 = tasks.get(1);
        checkParams(task2, LocalDate.of(2020, 12, 15),
                "New name", "New description", Priority.HIGH, 0);
    }

    @Test
    public void shouldInitTasksWithEachDayPeriodWithGaps() {
        PeriodParameters periodParameters = new PeriodParameters();
        periodParameters.setGapDays(1);
        periodFacade.setPeriodParameters(periodParameters);
        RepeatableTask rt = periodFacade.initTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(2);

        Task task1 = tasks.get(0);
        checkParams(task1, LocalDate.of(2020, 11, 1),
                "RT1", "RD1", Priority.MEDIUM, 0);

        Task task2 = tasks.get(1);
        checkParams(task2, LocalDate.of(2020, 11, 3),
                "RT1", "RD1", Priority.MEDIUM, 0);
    }

    @Test
    public void shouldUpdateTasksWithEachDayPeriodWithGaps() {
        repeatableTask.setEndDate(LocalDate.of(2020, 11, 10));
        PeriodParameters periodParameters = new PeriodParameters();
        periodParameters.setGapDays(1);
        periodFacade.setPeriodParameters(periodParameters);
        RepeatableTask rt = periodFacade.initTasks();

        rt.setDescription("New description");
        rt.setName("New name");
        rt.setPriority(Priority.HIGH);

        periodParameters.setGapDays(3);
        periodFacade.setRepeatableTask(rt);
        rt = periodFacade.updateTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(3);

        Task task1 = tasks.get(0);
        checkParams(task1, LocalDate.of(2020, 11, 1),
                "New name", "New description", Priority.HIGH, 0);

        Task task2 = tasks.get(1);
        checkParams(task2, LocalDate.of(2020, 11, 5),
                "New name", "New description", Priority.HIGH, 0);

        Task task3 = tasks.get(2);
        checkParams(task3, LocalDate.of(2020, 11, 9),
                "New name", "New description", Priority.HIGH, 0);
    }

    @Test
    public void shouldInitTasksWithEachWeekPeriodWithGaps() {
        PeriodParameters periodParameters = new PeriodParameters();
        periodParameters.setSunday(true);
        periodParameters.setThursday(true);
        periodParameters.setGapWeeks(1);

        repeatableTask.setEndDate(LocalDate.of(2020, 11, 30));
        repeatableTask.setPeriodMode(PeriodMode.EACH_WEEK);

        periodFacade.setRepeatableTask(repeatableTask);
        periodFacade.setPeriodParameters(periodParameters);
        periodFacade.setPeriodParameters(periodParameters);
        RepeatableTask rt = periodFacade.initTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();

        for (Task t : tasks) {
            System.out.println(t.getDate());
        }

        assertThat(tasks.size()).isEqualTo(5);

        Task task1 = tasks.get(0);
        checkParams(task1, LocalDate.of(2020, 11, 1),
                "RT1", "RD1", Priority.MEDIUM, 0);

        Task task2 = tasks.get(1);
        checkParams(task2, LocalDate.of(2020, 11, 12),
                "RT1", "RD1", Priority.MEDIUM, 0);

        Task task3 = tasks.get(2);
        checkParams(task3, LocalDate.of(2020, 11, 15),
                "RT1", "RD1", Priority.MEDIUM, 0);

        Task task4 = tasks.get(3);
        checkParams(task4, LocalDate.of(2020, 11, 26),
                "RT1", "RD1", Priority.MEDIUM, 0);

        Task task5 = tasks.get(4);
        checkParams(task5, LocalDate.of(2020, 11, 29),
                "RT1", "RD1", Priority.MEDIUM, 0);
    }

    @Test
    public void shouldUpdateTasksWithEachWeekPeriodWithGaps() {
        PeriodParameters periodParameters = new PeriodParameters();
        periodParameters.setMonday(true);
        periodParameters.setWednesday(true);
        periodParameters.setGapWeeks(1);

        repeatableTask.setEndDate(LocalDate.of(2020, 11, 30));
        repeatableTask.setPeriodMode(PeriodMode.EACH_WEEK);
        periodFacade.setPeriodParameters(periodParameters);
        RepeatableTask rt = periodFacade.initTasks();

        PeriodParameters periodParameters2 = new PeriodParameters();
        periodParameters2.setTuesday(true);
        periodParameters2.setFriday(true);
        periodParameters2.setGapWeeks(2);

        rt.setDescription("New description");
        rt.setName("New name");
        rt.setPriority(Priority.HIGH);

        periodFacade.setRepeatableTask(rt);
        periodFacade.setPeriodParameters(periodParameters2);
        rt = periodFacade.updateTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();

        for (Task t : tasks) {
            System.out.println(t.getDate());
        }

        assertThat(tasks.size()).isEqualTo(4);

        Task task1 = tasks.get(0);
        checkParams(task1, LocalDate.of(2020, 11, 10),
                "New name", "New description", Priority.HIGH, 0);

        Task task2 = tasks.get(1);
        checkParams(task2, LocalDate.of(2020, 11, 13),
                "New name", "New description", Priority.HIGH, 0);

        Task task3 = tasks.get(2);
        checkParams(task3, LocalDate.of(2020, 11, 24),
                "New name", "New description", Priority.HIGH, 0);

        Task task4 = tasks.get(3);
        checkParams(task4, LocalDate.of(2020, 11, 27),
                "New name", "New description", Priority.HIGH, 0);
    }

    @Test
    public void shouldInitTasksWithEachDayOfMonthPeriodWithGaps() {
        PeriodParameters periodParameters = new PeriodParameters();
        periodParameters.setDayOfMonth(15);
        periodParameters.setGapMonths(1);

        repeatableTask.setStartDate(LocalDate.of(2020, 1, 1));
        repeatableTask.setEndDate(LocalDate.of(2020, 5, 31));
        repeatableTask.setPeriodMode(PeriodMode.EACH_DAY_OF_MONTH);

        periodFacade.setRepeatableTask(repeatableTask);
        periodFacade.setPeriodParameters(periodParameters);
        RepeatableTask rt = periodFacade.initTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(3);

        Task task1 = tasks.get(0);
        checkParams(task1, LocalDate.of(2020, 1, 15),
                "RT1", "RD1", Priority.MEDIUM, 0);

        Task task2 = tasks.get(1);
        checkParams(task2, LocalDate.of(2020, 3, 15),
                "RT1", "RD1", Priority.MEDIUM, 0);

        Task task3 = tasks.get(2);
        checkParams(task3, LocalDate.of(2020, 5, 15),
                "RT1", "RD1", Priority.MEDIUM, 0);
    }

    @Test
    public void shouldUpdateTasksWithEachDayOfMonthPeriodWithGap() {
        PeriodParameters periodParameters = new PeriodParameters();
        periodParameters.setDayOfMonth(5);
        periodParameters.setGapMonths(1);

        repeatableTask.setStartDate(LocalDate.of(2020, 1, 1));
        repeatableTask.setEndDate(LocalDate.of(2020, 5, 31));
        repeatableTask.setPeriodMode(PeriodMode.EACH_DAY_OF_MONTH);
        RepeatableTask rt = periodFacade.initTasks();

        PeriodParameters periodParameters2 = new PeriodParameters();
        periodParameters2.setDayOfMonth(9);
        periodParameters2.setGapMonths(2);

        rt.setDescription("New description");
        rt.setName("New name");
        rt.setPriority(Priority.HIGH);

        periodFacade.setRepeatableTask(rt);
        periodFacade.setPeriodParameters(periodParameters2);
        rt = periodFacade.updateTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(2);

        Task task1 = tasks.get(0);
        checkParams(task1, LocalDate.of(2020, 1, 9),
                "New name", "New description", Priority.HIGH, 0);

        Task task2 = tasks.get(1);
        checkParams(task2, LocalDate.of(2020, 4, 9),
                "New name", "New description", Priority.HIGH, 0);
    }

    @Test
    public void shouldInitTasksWithEachWeekOfMonthPeriodWithGaps() {
        PeriodParameters periodParameters = new PeriodParameters();
        periodParameters.setMonthWeek("third");
        periodParameters.setGapMonths(1);

        repeatableTask.setStartDate(LocalDate.of(2020, 1, 1));
        repeatableTask.setEndDate(LocalDate.of(2020, 5, 30));
        repeatableTask.setPeriodMode(PeriodMode.EACH_WEEK_OF_MONTH);

        periodFacade.setRepeatableTask(repeatableTask);
        periodFacade.setPeriodParameters(periodParameters);
        RepeatableTask rt = periodFacade.initTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(21);

        Task first1 = tasks.get(0);
        checkParams(first1, LocalDate.of(2020, 1, 13),
                "RT1", "RD1", Priority.MEDIUM, 0);

        Task last1 = tasks.get(6);
        checkParams(last1, LocalDate.of(2020, 1, 19),
                "RT1", "RD1", Priority.MEDIUM, 0);

        Task first2 = tasks.get(7);
        checkParams(first2, LocalDate.of(2020, 3, 9),
                "RT1", "RD1", Priority.MEDIUM, 0);

        Task last2 = tasks.get(13);
        checkParams(last2, LocalDate.of(2020, 3, 15),
                "RT1", "RD1", Priority.MEDIUM, 0);

        Task first3 = tasks.get(14);
        checkParams(first3, LocalDate.of(2020, 5, 11),
                "RT1", "RD1", Priority.MEDIUM, 0);

        Task last3 = tasks.get(20);
        checkParams(last3, LocalDate.of(2020, 5, 17),
                "RT1", "RD1", Priority.MEDIUM, 0);
    }

    @Test
    public void shouldUpdateTasksWithEachWeekOfMonthPeriodWithGaps() {
        PeriodParameters periodParameters = new PeriodParameters();
        periodParameters.setMonthWeek("second");
        periodParameters.setGapMonths(1);

        repeatableTask.setStartDate(LocalDate.of(2020, 1, 1));
        repeatableTask.setEndDate(LocalDate.of(2020, 5, 31));
        repeatableTask.setPeriodMode(PeriodMode.EACH_WEEK_OF_MONTH);
        RepeatableTask rt = periodFacade.initTasks();

        PeriodParameters periodParameters2 = new PeriodParameters();
        periodParameters2.setMonthWeek("third");
        periodParameters2.setGapMonths(2);

        rt.setDescription("New description");
        rt.setName("New name");
        rt.setPriority(Priority.HIGH);

        periodFacade.setRepeatableTask(rt);
        periodFacade.setPeriodParameters(periodParameters2);
        rt = periodFacade.updateTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(14);

        Task first1 = tasks.get(0);
        checkParams(first1, LocalDate.of(2020, 1, 13),
                "New name", "New description", Priority.HIGH, 0);

        Task last1 = tasks.get(6);
        checkParams(last1, LocalDate.of(2020, 1, 19),
                "New name", "New description", Priority.HIGH, 0);

        Task first2 = tasks.get(7);
        checkParams(first2, LocalDate.of(2020, 4, 13),
                "New name", "New description", Priority.HIGH, 0);

        Task last2 = tasks.get(13);
        checkParams(last2, LocalDate.of(2020, 4, 19),
                "New name", "New description", Priority.HIGH, 0);
    }

    @Test
    public void shouldInitTasksWithEachDayOfWeekOfMonthPeriodWithGaps() {
        PeriodParameters periodParameters = new PeriodParameters();
        periodParameters.setNumberDayOfWeek("third");
        periodParameters.setDayOfWeek("tuesday");
        periodParameters.setGapMonths(1);

        repeatableTask.setStartDate(LocalDate.of(2020, 1, 1));
        repeatableTask.setEndDate(LocalDate.of(2020, 5, 31));
        repeatableTask.setPeriodMode(PeriodMode.EACH_DAY_OF_WEEK_OF_MONTH);

        periodFacade.setRepeatableTask(repeatableTask);
        periodFacade.setPeriodParameters(periodParameters);
        RepeatableTask rt = periodFacade.initTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();
        assertThat(tasks.size()).isEqualTo(3);

        Task task1 = tasks.get(0);
        checkParams(task1, LocalDate.of(2020, 1, 21),
                "RT1", "RD1", Priority.MEDIUM, 0);

        Task task2 = tasks.get(1);
        checkParams(task2, LocalDate.of(2020, 3, 17),
                "RT1", "RD1", Priority.MEDIUM, 0);

        Task task3 = tasks.get(2);
        checkParams(task3, LocalDate.of(2020, 5, 19),
                "RT1", "RD1", Priority.MEDIUM, 0);
    }

    @Test
    public void shouldUpdateTasksWithEachDayOfWeekOfMonthPeriodWithGaps() {
        PeriodParameters periodParameters = new PeriodParameters();
        periodParameters.setNumberDayOfWeek("third");
        periodParameters.setDayOfWeek("tuesday");
        periodParameters.setGapMonths(1);

        repeatableTask.setStartDate(LocalDate.of(2020, 1, 1));
        repeatableTask.setEndDate(LocalDate.of(2020, 5, 31));
        repeatableTask.setPeriodMode(PeriodMode.EACH_DAY_OF_WEEK_OF_MONTH);
        RepeatableTask rt = periodFacade.initTasks();

        PeriodParameters periodParameters2 = new PeriodParameters();
        periodParameters2.setNumberDayOfWeek("fourth");
        periodParameters2.setDayOfWeek("monday");
        periodParameters2.setGapMonths(2);

        rt.setDescription("New description");
        rt.setName("New name");
        rt.setPriority(Priority.HIGH);

        periodFacade.setRepeatableTask(rt);
        periodFacade.setPeriodParameters(periodParameters2);
        rt = periodFacade.updateTasks();
        List<Task> tasks = rt.getTasks();
        assertThat(tasks).isNotNull();

        for (Task t : tasks) {
            System.out.println(t.getDate());
        }

        assertThat(tasks.size()).isEqualTo(2);

        Task task1 = tasks.get(0);
        checkParams(task1, LocalDate.of(2020, 1, 27),
                "New name", "New description", Priority.HIGH, 0);

        Task task2 = tasks.get(1);
        checkParams(task2, LocalDate.of(2020, 4, 27),
                "New name", "New description", Priority.HIGH, 0);
    }
}
